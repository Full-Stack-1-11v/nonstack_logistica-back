package com.perfulandia.cl.logistica.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.assemblers.VehiculoDespachoAssembler;
import com.perfulandia.cl.logistica.assemblers.VehiculoDespachoDTOAssembler;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
 * Controlador REST para gestionar las operaciones relacionadas con los vehículos de despacho, versión 2.
 * Proporciona endpoints HATEOAS para crear, leer, actualizar y eliminar vehículos de despacho.
 */
@RestController
@RequestMapping("/api/v2/logistica/vehiculos")
@Tag(name = "Vehículos de Despacho V2", description = "Operaciones relacionadas a los vehiculos de despacho de Perfulandia")
public class VehiculoDespachoControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoDespachoControllerV2.class);

    @Autowired
    private VehiculoDespachoService vehiculoDespachoService;
    @Autowired
    private VehiculoDespachoDTOAssembler vehiculoDTOModelAssembler;
    @Autowired
    private VehiculoDespachoAssembler vehiculoDespachoAssembler;
    /**
     * Obtiene una lista de todos los vehículos de despacho con enlaces HATEOAS.
     * <p>
     * <b>Path:</b> {@code GET /api/v2/logistica/vehiculos}
     * @return Un {@link ResponseEntity} con un {@link CollectionModel} de {@link EntityModel} de {@link VehiculoDespachoDTO}, o un estado de error.
     */
    @GetMapping("")
    @Operation(summary = "Obtener todos los vehiculos de despacho.", description = "Obtiene una lista de todos los vehiculos de despacho.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no se encontraron vehiculos"),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista de vehiculos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespachoDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDespachoDTO>>> getVehiculosDespacho() {
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

            List<EntityModel<VehiculoDespachoDTO>> vehiculosEntity = vehiculoDespachoDTO
                    .stream()
                    .map(vehiculoDTOModelAssembler::toModel)
                    .toList();
            CollectionModel<EntityModel<VehiculoDespachoDTO>> collectionModel = CollectionModel.of(vehiculosEntity);

            logger.info("Se encontraron {} vehiculos de despacho", vehiculos.size());
            return ResponseEntity.ok(collectionModel);
        } catch (Exception e) {
            logger.error("Error al obtener vehiculos de despacho", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Busca vehículos por un patrón en la patente y devuelve una colección con enlaces HATEOAS.
     * <p>
     * <b>Path:</b> {@code GET /api/v2/logistica/vehiculos/{patron_patente}}
     * @param patron_patente El patrón de dos letras para buscar en la patente.
     * @return Un {@link ResponseEntity} con un {@link CollectionModel} de {@link EntityModel} de {@link VehiculoDespachoDTO} encontrados, o un estado de error.
     */
    @GetMapping("/{patron_patente}")
    @Operation(summary = "Obtener todos los vehiculos de despacho por patron de las dos primeras letras de la patente.", description = "Obtiene una lista de todos los vehiculos de despacho por las dos primeras letras de la petente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista de vehiculos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespachoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Busqueda mal realizda, patron de patente debe ser 2."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDespachoDTO>>> buscarPorPatronPatente(
            @Parameter(description = "Valor de solo DOS letras", required = true, example = "AA") @PathVariable String patron_patente) {
        logger.info("Buscando vehiculos por patron de patente: {}", patron_patente);
        try {
            if (patron_patente.length() != 2) {
                logger.warn("Patron de patente invalido: {}", patron_patente);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                List<VehiculoDespacho> vehiculosEncontrados = vehiculoDespachoService
                        .buscarVehiculoPorPatronPatente(patron_patente);
                List<VehiculoDespachoDTO> vehiculosDTO = vehiculosEncontrados.stream()
                        .map(VehiculoDespachoConverter::convertDTOVehiculo)
                        .collect(Collectors.toList());
                List<EntityModel<VehiculoDespachoDTO>> vehiculosEntity = vehiculosDTO
                        .stream()
                        .map(vehiculoDTOModelAssembler::toModel)
                        .toList();
                CollectionModel<EntityModel<VehiculoDespachoDTO>> collectionModel = CollectionModel.of(vehiculosEntity);
                logger.info("Se encontraron {} vehiculos con el patron '{}'", vehiculosDTO.size(), patron_patente);
                return ResponseEntity.ok(collectionModel);

            }

        } catch (Exception e) {
            logger.error("Error al buscar vehiculos por patron de patente: {}", patron_patente, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Crea un nuevo vehículo de despacho y devuelve su representación HATEOAS.
     * <p>
     * <b>Path:</b> {@code POST /api/v2/logistica/vehiculos}
     * @param vehiculo El objeto {@link VehiculoDespacho} a crear.
     * @return Un {@link ResponseEntity} con el {@link EntityModel} del {@link VehiculoDespacho} creado y un estado CREATED, o un estado de error.
     */
    @PostMapping("")
    @Operation(summary = "Registra un vehiculo a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "Operacion erronea, vehiculo ya existe con esa patente"),
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve vehiculo registrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespacho.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<EntityModel<VehiculoDespacho>> createVehiculoDespacho(
            @RequestBody @Schema(description = "Datos del vehiculo a registrar", example = "{\n" +
                    "  \"patente\": \"AA-11\",\n" +
                    "  \"ano\": 2025\n" +
                    "}") VehiculoDespacho vehiculo) {
        logger.info("Creando vehiculo de despacho: {}", vehiculo);
        try {
            VehiculoDespacho vehiculoRegistrar = vehiculoDespachoService.registrarVehiculoDespacho(vehiculo);
            if (vehiculoRegistrar == null) {
                logger.warn("Vehiculo con patente {} ya existe", vehiculo.getPatente());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            EntityModel<VehiculoDespacho> vehiculoEntity = vehiculoDespachoAssembler.toModel(vehiculoRegistrar);
            logger.info("Vehiculo de despacho creado exitosamente: {}", vehiculoRegistrar);
            return new ResponseEntity<>(vehiculoEntity, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error al crear vehiculo de despacho", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    /**
     * Actualiza completamente un vehículo de despacho existente y devuelve su representación HATEOAS.
     * <p>
     * <b>Path:</b> {@code PUT /api/v2/logistica/vehiculos/{patente}}
     * @param vehiculo El objeto {@link VehiculoDespacho} con los nuevos datos.
     * @param patente La patente del vehículo a actualizar.
     * @return Un {@link ResponseEntity} con el {@link EntityModel} del {@link VehiculoDespacho} actualizado o un estado de error.
     */
    @PutMapping("/{patente}")
    @Operation(summary = "Actualiza un vehiculo a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Operacion erronea, vehiculo no existe"),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve vehiculo actualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespacho.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<EntityModel<VehiculoDespacho>> actualizarVehiculoDespacho(
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
            EntityModel<VehiculoDespacho> vehiculoEntity = vehiculoDespachoAssembler.toModel(vehiculoExistente);
            logger.info("Vehiculo de despacho actualizado exitosamente: {}", vehiculoExistente);
            return new ResponseEntity<>(vehiculoEntity, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al actualizar vehiculo de despacho", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Actualiza parcialmente un vehículo de despacho existente y devuelve su representación HATEOAS.
     * <p>
     * <b>Path:</b> {@code PATCH /api/v2/logistica/vehiculos/{patente}}
     * @param vehiculo El objeto {@link VehiculoDespacho} con los campos a actualizar.
     * @param patente La patente del vehículo a actualizar.
     * @return Un {@link ResponseEntity} con el {@link EntityModel} del {@link VehiculoDespacho} actualizado o un estado de error.
     */
    @PatchMapping("/{patente}")
    @Operation(summary = "Parcha un vehiculo a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve vehiculo parchado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespacho.class))),
            @ApiResponse(responseCode = "400", description = "Operacion erronea, vehiculo no existe")
    })
    public ResponseEntity<EntityModel<VehiculoDespacho>> parcharVehiculoDespacho(
            @RequestBody @Schema(description = "Datos del vehiculo a registrar", example = "{\n" +
                    "  \"patente\": \"AA-11\",\n" +
                    "  \"ano\": 2025\n" +
                    "}") @Parameter(description = "Patente del vehiculo a actualizar", required = true) VehiculoDespacho vehiculo,
            @PathVariable String patente) {
        logger.info("Parchando vehiculo de despacho con patente: {}", patente);
        try {
            VehiculoDespacho vehiculoParchar = vehiculoDespachoService.parcharVehiculoDespacho(vehiculo, patente);
            EntityModel<VehiculoDespacho> vehiculoEntity = vehiculoDespachoAssembler.toModel(vehiculoParchar);
            logger.info("Vehiculo de despacho parchado exitosamente: {}", vehiculoParchar);
            return ResponseEntity.ok(vehiculoEntity);
        } catch (Exception e) {
            logger.error("Error al parchar vehiculo de despacho", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Elimina un vehículo de despacho por su patente.
     * <p>
     * <b>Path:</b> {@code DELETE /api/v2/logistica/vehiculos/{patente}}
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
