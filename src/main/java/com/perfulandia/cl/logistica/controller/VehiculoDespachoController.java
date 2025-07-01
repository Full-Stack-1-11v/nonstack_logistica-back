package com.perfulandia.cl.logistica.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.converter.VehiculoDespachoConverter;
import com.perfulandia.cl.logistica.dto.VehiculoDespachoDTO;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.service.VehiculoDespachoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * Controlador REST para gestionar las operaciones relacionadas con los vehículos de despacho.
 * Proporciona endpoints para crear, leer, actualizar y eliminar vehículos de despacho.
 */
@RestController
@RequestMapping("/api/v1/logistica/vehiculos")
@Tag(name = "Vehículos de Despacho V1", description = "Operaciones relacionadas a los vehiculos de despacho de Perfulandia")
public class VehiculoDespachoController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoDespachoController.class);

    @Autowired
    private VehiculoDespachoService vehiculoDespachoService;
    /**
     * Obtiene una lista de todos los vehículos de despacho.
     * <p>
     * <b>Path:</b> {@code GET /api/v1/logistica/vehiculos}
     * @return Un {@link ResponseEntity} con la lista de {@link VehiculoDespachoDTO} o un estado de error.
     */
    @GetMapping("")
    @Operation(summary = "Obtener todos los vehiculos de despacho.", description = "Obtiene una lista de todos los vehiculos de despacho.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, mo se encontraron vehiculos"),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista de vehiculos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespachoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron vehiculos")
    })
    public ResponseEntity<?> getVehiculosDespacho() {
        logger.info("Obteniendo todos los vehiculos de despacho");
        try {
            List<VehiculoDespacho> vehiculos = vehiculoDespachoService.verVehiculosDespachos();
            if (vehiculos.isEmpty()) {
                logger.info("No se encontraron vehiculos de despacho");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<VehiculoDespachoDTO> vehiculoDespachoDTO = vehiculos.stream()
                    .map(VehiculoDespachoConverter::convertDTOVehiculo)
                    .collect(Collectors.toList());

            logger.info("Se encontraron {} vehiculos de despacho", vehiculos.size());
            return ResponseEntity.ok(vehiculoDespachoDTO);
        } catch (Exception e) {
            logger.error("Error al obtener vehiculos de despacho", e);
            return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Busca vehículos por un patrón en la patente.
     * <p>
     * <b>Path:</b> {@code GET /api/v1/logistica/vehiculos/{patron_patente}}
     * @param patron_patente El patrón de dos letras para buscar en la patente.
     * @return Un {@link ResponseEntity} con la lista de {@link VehiculoDespachoDTO} encontrados o un estado de error.
     */
    @GetMapping("/{patron_patente}")
    @Operation(summary = "Obtener todos los vehiculos de despacho por patron de las dos primeras letras de la patente.", description = "Obtiene una lista de todos los vehiculos de despacho por las dos primeras letras de la petente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista de vehiculos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespachoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Busqueda mal realizda.")
    })
    public ResponseEntity<?> buscarPorPatronPatente(
            @Parameter(description = "Valor de solo DOS letras", required = true, example = "AA") @PathVariable String patron_patente) {
        logger.info("Buscando vehiculos por patron de patente: {}", patron_patente);
        try {
            List<VehiculoDespacho> vehiculosEncontrados = vehiculoDespachoService
                    .buscarVehiculoPorPatronPatente(patron_patente);
            List<VehiculoDespachoDTO> vehiculosDTO = vehiculosEncontrados.stream()
                    .map(VehiculoDespachoConverter::convertDTOVehiculo)
                    .collect(Collectors.toList());
            logger.info("Se encontraron {} vehiculos con el patron '{}'", vehiculosDTO.size(), patron_patente);
            return ResponseEntity.ok(vehiculosDTO);
        } catch (Exception e) {
            logger.error("Error al buscar vehiculos por patron de patente: {}", patron_patente, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Crea un nuevo vehículo de despacho.
     * <p>
     * <b>Path:</b> {@code POST /api/v1/logistica/vehiculos}
     * @param vehiculo El objeto {@link VehiculoDespacho} a crear.
     * @return Un {@link ResponseEntity} con el {@link VehiculoDespacho} creado y un estado CREATED, o un estado de error.
     */
    @PostMapping("")
    @Operation(summary = "Registra un vehiculo a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Operacion erronea, vehiculo ya existe con esa patente"),
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve vehiculo registrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespacho.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> createVehiculoDespacho(
            @RequestBody @Schema(description = "Datos del vehiculo a registrar", example = "{\n" +
                    "  \"patente\": \"AA-11\",\n" +
                    "  \"ano\": 2025\n" +
                    "}") VehiculoDespacho vehiculo) {
        logger.info("Creando vehiculo de despacho: {}", vehiculo);
        try {
            VehiculoDespacho vehiculoRegistrar = vehiculoDespachoService.registrarVehiculoDespacho(vehiculo);
            if (vehiculoRegistrar == null) {
                logger.warn("Vehiculo con patente {} ya existe", vehiculo.getPatente());
                return new ResponseEntity<>("vehiculo con esa patente ya existe", HttpStatus.CONFLICT);
            }

            logger.info("Vehiculo de despacho creado exitosamente: {}", vehiculoRegistrar);
            return new ResponseEntity<>(vehiculo, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error al crear vehiculo de despacho", e);
            return new ResponseEntity<>("Error al registrar el vehiculo : " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    /**
     * Actualiza completamente un vehículo de despacho existente.
     * <p>
     * <b>Path:</b> {@code PUT /api/v1/logistica/vehiculos/{patente}}
     * @param vehiculo El objeto {@link VehiculoDespacho} con los nuevos datos.
     * @param patente La patente del vehículo a actualizar.
     * @return Un {@link ResponseEntity} con el {@link VehiculoDespacho} actualizado o un estado de error.
     */
    @PutMapping("/{patente}")
    @Operation(summary = "Actualiza un vehiculo a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Operacion erronea, vehiculo no existe"),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve vehiculo actualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespacho.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> actualizarVehiculoDespacho(
            @RequestBody @Schema(description = "Datos del vehiculo a registrar", example = "{\n" +
                    "  \"patente\": \"AA-11\",\n" +
                    "  \"ano\": 2025\n" +
                    "}") VehiculoDespacho vehiculo,
            @PathVariable @Parameter(description = "Patente del vehiculo a actualizar", required = true) String patente) {
        logger.info("Actualizando vehiculo de despacho con patente: {}", patente);
        try {
            VehiculoDespacho vehiculoExistente = vehiculoDespachoService.actualizarVehiculoDespacho(vehiculo, patente);
            if (vehiculoExistente == null) {
                logger.warn("Vehiculo con patente {} no encontrado para actualizar", patente);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            logger.info("Vehiculo de despacho actualizado exitosamente: {}", vehiculoExistente);
            return new ResponseEntity<>(vehiculoExistente, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al actualizar vehiculo de despacho", e);
            return new ResponseEntity<>("Error al actualizar el vehiculo, contactar TI",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Actualiza parcialmente un vehículo de despacho existente.
     * <p>
     * <b>Path:</b> {@code PATCH /api/v1/logistica/vehiculos/{patente}}
     * @param vehiculo El objeto {@link VehiculoDespacho} con los campos a actualizar.
     * @param patente La patente del vehículo a actualizar.
     * @return Un {@link ResponseEntity} con el {@link VehiculoDespacho} actualizado o un estado de error.
     */
    @PatchMapping("/{patente}")
    @Operation(summary = "Parcha un vehiculo a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve vehiculo parchado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespacho.class))),
            @ApiResponse(responseCode = "400", description = "Operacion erronea, vehiculo no existe")
    })
    public ResponseEntity<?> parcharVehiculoDespacho(
            @RequestBody @Schema(description = "Datos del vehiculo a registrar", example = "{\n" +
                    "  \"patente\": \"AA-11\",\n" +
                    "  \"ano\": 2025\n" +
                    "}") @Parameter(description = "Patente del vehiculo a actualizar", required = true) VehiculoDespacho vehiculo,
            @PathVariable String patente) {
        logger.info("Parchando vehiculo de despacho con patente: {}", patente);
        try {
            VehiculoDespacho vehiculoParchar = vehiculoDespachoService.parcharVehiculoDespacho(vehiculo, patente);
            logger.info("Vehiculo de despacho parchado exitosamente: {}", vehiculoParchar);
            return ResponseEntity.ok(vehiculoParchar);
        } catch (Exception e) {
            logger.error("Error al parchar vehiculo de despacho", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Elimina un vehículo de despacho por su patente.
     * <p>
     * <b>Path:</b> {@code DELETE /api/v1/logistica/vehiculos/{patente}}
     * @param patente La patente del vehículo a eliminar.
     * @return Un {@link ResponseEntity} con estado NO_CONTENT si se elimina correctamente, o un estado de error.
     */
    @DeleteMapping("/{patente}")
    @Operation(summary = "Borra un vehiculo a traves de su patente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehiculo elimnado"),
            @ApiResponse(responseCode = "400", description = "Operacion erronea, vehiculo no existe o request mal realizado")
    })
    public ResponseEntity<?> borrarVehiculoDespacho(@PathVariable String patente) {
        logger.info("Eliminando vehiculo de despacho con patente: {}", patente);
        try {
            vehiculoDespachoService.borrarVehiculoDespacho(patente);
            logger.info("Vehiculo de despacho con patente {} eliminado exitosamente", patente);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error al eliminar vehiculo de despacho", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
