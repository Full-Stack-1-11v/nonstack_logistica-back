package com.perfulandia.cl.logistica.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.converter.GuiaDespachoConverter;
import com.perfulandia.cl.logistica.dto.GuiaDespachoDTO;
import com.perfulandia.cl.logistica.dto.OrdenDTO;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.service.GuiaDespachoService;
import com.perfulandia.cl.logistica.service.OrdenDTOService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controlador REST para gestionar las operaciones relacionadas con las guías de despacho.
 * Proporciona endpoints para crear, leer, actualizar y eliminar guías de despacho.
 */
@RestController
@RequestMapping("/api/v1/logistica/despachos")
@Tag(name = "Guias de Despacho V1", description = "Operaciones relacionadas a las guias de despacho de Perfulandia")
public class GuiaDespachoController {

    private static final Logger logger = LoggerFactory.getLogger(GuiaDespachoController.class);

    @Autowired
    private GuiaDespachoService guiaDespachoService;
    @Autowired
    private OrdenDTOService UserDTOService;
    @Autowired
    private OrdenFeignClient ordenClient;

    /**
     * Obtiene una lista de todas las guías de despacho.
     * <p>
     * <b>Path:</b> {@code GET /api/v1/logistica/despachos}
     * @return Un {@link ResponseEntity} con la lista de {@link GuiaDespachoDTO} o un estado de error.
     */
    @GetMapping("")
    @Operation(summary = "Obtener todos las guias de despacho", description = "Obtiene una lista de todas las guias de despacho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, pero no hay contenido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve todos los despachos."),
            @ApiResponse(responseCode = "500", description = "Error en el codigo.")
    })
    public ResponseEntity<?> getDespachos() {
        try {
            logger.info("[getDespachos] Obteniendo todas las guias de despacho");
            List<GuiaDespacho> despachos = guiaDespachoService.verGuiaDespachos();
            if (despachos.isEmpty()) {
                logger.warn("[getDespachos] No se encontraron guias de despacho");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<GuiaDespachoDTO> despachosDTO = despachos.stream()
                    .map(guia -> GuiaDespachoConverter.convertToDTO(guia, ordenClient))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(despachosDTO, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("[getDespachos] Error al obtener las guias de despacho", e);
            return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    /**
     * Obtiene una guía de despacho específica por su ID.
     * <p>
     * <b>Path:</b> {@code GET /api/v1/logistica/despachos/{id}}
     * @param id El ID de la guía de despacho a obtener.
     * @return Un {@link ResponseEntity} con la {@link GuiaDespacho} encontrada o un estado NOT_FOUND.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GuiaDespacho> getGuiaDespachoById(@PathVariable Integer id) {
        logger.info("[getGuiaDespachoById] Obteniendo guia de despacho con id: {}", id);
        Optional<GuiaDespacho> guiaDespachoOptional = guiaDespachoService.obtenerGuiaDespachoPorId(id);
        if(guiaDespachoOptional.isPresent()){
            GuiaDespacho guiaDespacho = guiaDespachoOptional.get();
            return new ResponseEntity<>(guiaDespacho,HttpStatus.OK);
        } else {
            logger.warn("[getGuiaDespachoById] No se encontró la guia de despacho con ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
        /**
     * Crea una nueva guía de despacho.
     * <p>
     * <b>Path:</b> {@code POST /api/v1/logistica/despachos}
     * @param guiaDespacho El objeto {@link GuiaDespacho} a crear.
     * @return Un {@link ResponseEntity} con la {@link GuiaDespacho} creada y un estado CREATED, o un error del servidor.
     */

    @PostMapping()
    @Operation(summary = "Registra una guia de despacho traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve la guia de despacho registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo registrar(erorr interno)")
    })
    public ResponseEntity<?> createDespacho(
            @RequestBody @Schema(description = "Datos de la guia de despacho a crear", example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idOrden\": 1\n" +
                    "}") GuiaDespacho guiaDespacho) {
        try {
            logger.info("[createDespacho] Creando guia de despacho");
            guiaDespachoService.crearGuiaDespacho(guiaDespacho);
            return new ResponseEntity<>(guiaDespacho, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("[createDespacho] Error al crear guia de despacho", e);
            return new ResponseEntity<>("Error : " + e.getMessage() + " " + e.getLocalizedMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza completamente una guía de despacho existente.
     * <p>
     * <b>Path:</b> {@code PUT /api/v1/logistica/despachos/{id}}
     * @param id El ID de la guía de despacho a actualizar.
     * @param guiaDespacho El objeto {@link GuiaDespacho} con los nuevos datos.
     * @return Un {@link ResponseEntity} con la {@link GuiaDespacho} actualizada o un error del servidor.
     */

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una guia de despacho a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve la guia de despacho actualizada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro la guia de despacho.")
    })
    public ResponseEntity<?> putGuiaDespacho(
            @Parameter(description = "Id guia despacho a actualizar", required = true) @PathVariable Integer id,
            @Schema(description = "Datos de la guia de despacho a realizar put", example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idOrden\": 1\n" +
                    "}") @RequestBody GuiaDespacho guiaDespacho) {
        try {
            logger.info("[putGuiaDespacho] Actualizando guia de despacho con id: {}", id);
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.putGuiaDespacho(guiaDespacho, id);
            return ResponseEntity.ok(guiaDespachoParchada);

        } catch (Exception e) {
            logger.error("[putGuiaDespacho] Error al actualizar guia de despacho con id: {}", id, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Actualiza parcialmente una guía de despacho existente.
     * <p>
     * <b>Path:</b> {@code PATCH /api/v1/logistica/despachos/{id}}
     * @param id El ID de la guía de despacho a actualizar.
     * @param guiaDespacho El objeto {@link GuiaDespacho} con los campos a actualizar.
     * @return Un {@link ResponseEntity} con la {@link GuiaDespacho} actualizada o un error del servidor.
     */
    @PatchMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio parchado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "500", description = "Error al parchar.")
    })
    @Operation(summary = "Parcha una guia de despacho a traves de un body y la id")
    public ResponseEntity<?> patchGuiaDespacho(
            @Parameter(description = "Id guia de despacho a actualizar", required = true) @PathVariable Integer id,
            @Schema(description = "Datos de la guia de despacho a realizar put", example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idOrden\": 1\n" +
                    "}") @RequestBody GuiaDespacho guiaDespacho) {
        try {
            logger.info("[patchGuiaDespacho] Parchando guia de despacho con id: {}", id);
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.parcharGuiaDespacho(guiaDespacho, id);
            return ResponseEntity.ok(guiaDespachoParchada);
        } catch (Exception e) {
            logger.error("[patchGuiaDespacho] Error al parchar guia de despacho con id: {}", id, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Elimina una guía de despacho por su ID.
     * <p>
     * <b>Path:</b> {@code DELETE /api/v1/logistica/despachos/{id}}
     * @param id El ID de la guía de despacho a eliminar.
     * @return Un {@link ResponseEntity} con estado NO_CONTENT si se elimina correctamente, o NOT_FOUND si no se encuentra.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un despacho usando su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no devuelve contenido."),
            @ApiResponse(responseCode = "404", description = "No se encontro el despacho.")
    })
    public ResponseEntity<?> deleteGuiaDespacho(
    @Parameter(description = "Borra una guia de despacho a traves de su id como path variable")
    
    @PathVariable Integer id) {
        try {
            logger.info("[deleteGuiaDespacho] Eliminando guia de despacho con id: {}", id);
            guiaDespachoService.borrarGuiaDespacho(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("[deleteGuiaDespacho] Error al eliminar guia de despacho con id: {}", id, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/test_ordenes")
    @Operation(summary = "Test Feign Client, no para uso final")
     @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, la api externa funciona!."),
            @ApiResponse(responseCode = "404", description = "No se encontro el endpoint.")
    })
    public ResponseEntity<?> getOrdenes() {
        try {
            logger.info("[getOrdenes] Obteniendo ordenes desde Feign Client");
            List<OrdenDTO> ordenes = UserDTOService.verOrdenes();
            return ResponseEntity.ok(ordenes);
        } catch (Exception e) {
            logger.error("[getOrdenes] Error al obtener ordenes desde Feign Client", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
