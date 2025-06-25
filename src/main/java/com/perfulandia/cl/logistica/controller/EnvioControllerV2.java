package com.perfulandia.cl.logistica.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.assemblers.EnvioDTOModelAssembler;
import com.perfulandia.cl.logistica.converter.EnvioConverter;
import com.perfulandia.cl.logistica.dto.EnvioDTO;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.service.EnvioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los envíos, versión 2.
 * Proporciona endpoints HATEOAS para crear, leer, actualizar y eliminar envíos.
 */
@RestController
@RequestMapping("/api/v2/logistica/envios")
public class EnvioControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(EnvioControllerV2.class);

    @Autowired
    private EnvioService envioService;

    @Autowired
    private EnvioDTOModelAssembler assemblerDTO;

    /**
     * Obtiene una lista de todos los envíos con enlaces HATEOAS.
     * <p>
     * <b>Path:</b> {@code GET /api/v2/logistica/envios}
     * @return Un {@link ResponseEntity} con un {@link CollectionModel} de {@link EntityModel} de {@link EnvioDTO}, o un estado NO_CONTENT si no hay envíos.
     */
    @GetMapping("")
    @Operation(summary = "Obtener todos los envios", description = "Obtiene una lista de todas las carreras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve los envios encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron envios")
    })
    public ResponseEntity<CollectionModel<EntityModel<EnvioDTO>>> getEnvios() {
        logger.info("[getEnvios] Getting all shipments");
        List<Envio> envios = envioService.obtenerEnvios();
        List<EnvioDTO> enviosDTO = envios.stream().map(EnvioConverter::convertToDTO).toList();
        if (envios.isEmpty()) {
            logger.warn("[getEnvios] No shipments found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<EnvioDTO>> enviosHateoas = enviosDTO.stream()
                .map(assemblerDTO::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EnvioDTO>> collectionModel = CollectionModel.of(enviosHateoas);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    /**
     * Obtiene un envío específico por su ID con enlaces HATEOAS.
     * <p>
     * <b>Path:</b> {@code GET /api/v2/logistica/envios/{id}}
     * @param id El ID del envío a obtener.
     * @return Un {@link ResponseEntity} con el {@link EntityModel} del {@link EnvioDTO} correspondiente, o un estado NOT_FOUND si no se encuentra.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene datos de un envios.", description = "A traves de la id de un envio obtiene los detalles de este.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio con el id entregado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron envios con esa id")
    })
    public ResponseEntity<EntityModel<EnvioDTO>> getEnvioPorId(
     @Parameter(description = "ID del envio a obtener", required = true) @PathVariable Integer id) {
        logger.info("[getEnvioPorId] Getting shipment with id {}", id);
        Optional<Envio> envio = envioService.obtenerEnvioPorId(id);
        if(envio.isPresent()){
        EnvioDTO envioDTO = EnvioConverter.convertToDTO(envio.get());
        EntityModel<EnvioDTO> entityModel = assemblerDTO.toModel(envioDTO);
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
        }
        logger.warn("[getEnvioPorId] Shipment with ID: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    /**
     * Busca envíos dentro de un rango de fechas especificado y devuelve una colección con enlaces HATEOAS.
     * <p>
     * <b>Path:</b> {@code GET /api/v2/logistica/envios/buscar-por-fecha/{fechaInicio}/{fechaFin}}
     * @param fechaInicio La fecha de inicio del rango de búsqueda.
     * @param fechaFin La fecha de fin del rango de búsqueda.
     * @return Un {@link ResponseEntity} con un {@link CollectionModel} de {@link EntityModel} de {@link EnvioDTO} encontrados, o un estado NO_CONTENT si no hay resultados.
     */
    @GetMapping("/buscar-por-fecha/{fechaInicio}/{fechaFin}")
    @Operation(summary = "Obtiene envios en un rango de fecha determinado.", description = "A traves del rango de fecha entrega una lista de envios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve los envios encontradas en ese rango de fecha", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "400", description = "Request incorrecto")
    })
    public ResponseEntity<CollectionModel<EntityModel<EnvioDTO>>> getEnvioPorFecha(
            @Parameter(description = "Fecha inicial (inclusive)", required = true, example = "2023-04-01") @PathVariable LocalDate fechaInicio,
            @Parameter(description = "Fecha final (inclusive)", required = true, example = "2025-12-12") @PathVariable LocalDate fechaFin) {
        logger.info("[getEnvioPorFecha] Getting shipments by date range: {} - {}", fechaInicio, fechaFin);
        List<Envio> enviosEncontrados = envioService.buscarEnvioPorRangoDeFecha(fechaInicio, fechaFin);

        if (enviosEncontrados.isEmpty()) {
            logger.warn("[getEnvioPorFecha] No shipments found in the date range: {} - {}", fechaInicio, fechaFin);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EnvioDTO> enviosDTO = enviosEncontrados.stream().map(EnvioConverter::convertToDTO).toList();

        List<EntityModel<EnvioDTO>> enviosHateoas = enviosDTO.stream()
                .map(assemblerDTO::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<EnvioDTO>> collectionModel = CollectionModel.of(enviosHateoas);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
}
    
    /**
     * Crea un nuevo envío y devuelve su representación HATEOAS.
     * <p>
     * <b>Path:</b> {@code POST /api/v2/logistica/envios}
     * @param envio El objeto {@link Envio} a crear.
     * @return Un {@link ResponseEntity} con el {@link EntityModel} del {@link EnvioDTO} creado y un estado CREATED, o INTERNAL_SERVER_ERROR si falla.
     */

    @PostMapping("")
    @Operation(summary = "Registra un envio a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve el envio registrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo registrar(erorr interno)")
    })
    public ResponseEntity<EntityModel<EnvioDTO>> crearEnvio(
            @RequestBody @Schema(description = "Datos del envio a crear", required = true, example = "{\n" +
                    "  \"idCliente\": 999,\n" +
                    "  \"idOrden\": 1,\n" +
                    "  \"fechaEntrega\": \"2024-12-11\",\n" +
                    "  \"entregado\": true,\n" +
                    "  \"observacion\": \"Salio un perro ladrando\",\n" +
                    "  \"guiaDespacho\": {\n" +
                    "    \"idDespacho\": 1\n" +
                    "  },\n" +
                    "  \"vehiculoDespacho\": {\n" +
                    "    \"idVehiculoDespacho\": 1\n" +
                    "  },\n" +
                    "  \"ruta\": {\n" +
                    "    \"idRuta\": 1\n" +
                    "  }\n" +
                    "}") Envio envio) {
        try {
            logger.info("[crearEnvio] Creating shipment.");
            Envio nuevoEnvio = envioService.crearEnvio(envio);
            EnvioDTO nuevoEnvioDTO = EnvioConverter.convertToDTO(nuevoEnvio);
            EntityModel<EnvioDTO> envioEntity = assemblerDTO.toModel(nuevoEnvioDTO);
            return new ResponseEntity<>(envioEntity, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("[crearEnvio] Error creating shipment", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Actualiza completamente un envío existente y devuelve su representación HATEOAS.
     * <p>
     * <b>Path:</b> {@code PUT /api/v2/logistica/envios/{id}}
     * @param id El ID del envío a actualizar.
     * @param envio El objeto {@link Envio} con los nuevos datos.
     * @return Un {@link ResponseEntity} con el {@link EntityModel} del {@link EnvioDTO} actualizado o un estado NOT_FOUND si no se encuentra.
     */

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un envio a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio actualizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<EntityModel<EnvioDTO>> actualizarEnvio(
            @Parameter(description = "Id del envio a actualizar", required = true) @PathVariable Integer id,
            @RequestBody @Schema(description = "Datos de la actualización", required = true, example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idCliente\": 1,\n" +
                    "  \"idOrden\": 1,\n" +
                    "  \"fechaEntrega\": \"2025-06-11\",\n" +
                    "  \"entregado\": true,\n" +
                    "  \"observacion\": \"Se entrego al hijo de la persona.\",\n" +
                    "  \"guiaDespacho\": 1,\n" +
                    "  \"vehiculoDespacho\": 1,\n" +
                    "  \"ruta\": \"2\"\n" +
                    "}") Envio envio) {
        try {
            logger.info("[actualizarEnvio] Updating shipment with id: {}", id);
            Envio envioActualizado = envioService.actualizarEnvio(id, envio);
            if (envioActualizado != null) {
                EnvioDTO envioDTO = EnvioConverter.convertToDTO(envioActualizado);
                EntityModel<EnvioDTO> envioEntity = assemblerDTO.toModel(envioDTO);
                return ResponseEntity.ok(envioEntity);
            } else {
                logger.warn("[actualizarEnvio] Shipment not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("[actualizarEnvio] Error updating shipment with id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza parcialmente un envío existente y devuelve su representación HATEOAS.
     * <p>
     * <b>Path:</b> {@code PATCH /api/v2/logistica/envios/{id}}
     * @param id El ID del envío a actualizar.
     * @param envio El objeto {@link Envio} con los campos a actualizar.
     * @return Un {@link ResponseEntity} con el {@link EntityModel} del {@link EnvioDTO} actualizado o un estado BAD_REQUEST si ocurre un error.
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Parcha un envio a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio parchado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<EntityModel<EnvioDTO>> parcharEnvio(
            @Parameter(description = "Id del envio a actualizar", required = true) @PathVariable Integer id,
            @RequestBody @Schema(description = "Datos de la actualización", required = false, example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idCliente\": 1,\n" +
                    "  \"idOrden\": 1,\n" +
                    "  \"fechaEntrega\": \"2025-06-11\",\n" +
                    "  \"entregado\": true,\n" +
                    "  \"observacion\": \"Se entrego al hijo de la persona.\",\n" +
                    "  \"guiaDespacho\": 1,\n" +
                    "  \"vehiculoDespacho\": 1,\n" +
                    "  \"ruta\": \"2\"\n" +
                    "}") Envio envio) {
        try {
            logger.info("[parcharEnvio] Patching shipment with id: {}", id);
            Envio envioActualizado = envioService.parcharEnvio(id, envio);

            if (envioActualizado != null) {
                EnvioDTO envioDTO = EnvioConverter.convertToDTO(envioActualizado);
                EntityModel<EnvioDTO> envioEntity = assemblerDTO.toModel(envioDTO);
                return ResponseEntity.ok(envioEntity);
            } else {
                logger.warn("[parcharEnvio] Shipment not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            logger.error("[parcharEnvio] Error patching shipment with id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Elimina un envío por su ID.
     * <p>
     * <b>Path:</b> {@code DELETE /api/v2/logistica/envios/{id}}
     * @param id El ID del envío a eliminar.
     * @return Un {@link ResponseEntity} con estado NO_CONTENT si se elimina correctamente, o NOT_FOUND si no se encuentra.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un envio usando su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no devuelve contenido."),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<Void> eliminarEnvio(
            @Parameter(description = "Id del envio a borrar", required = true) @PathVariable Integer id) {
        try {
            logger.info("[eliminarEnvio] Deleting shipment with id: {}", id);
            envioService.eliminarEnvio(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("[eliminarEnvio] Error deleting shipment with id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
