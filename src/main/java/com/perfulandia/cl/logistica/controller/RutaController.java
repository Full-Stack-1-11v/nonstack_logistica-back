package com.perfulandia.cl.logistica.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.service.RutaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las rutas.
 * Proporciona endpoints para crear, leer, actualizar y eliminar rutas.
 */
@RestController
@RequestMapping("/api/v1/logistica/envios/rutas")
@Tag(name = "Rutas V1", description = "Operaciones relacionadas a la información de rutas,")
public class RutaController {

    private static final Logger logger = LoggerFactory.getLogger(RutaController.class);

    @Autowired
    private RutaService rutaService;

    /**
     * Obtiene una lista de todas las rutas.
     * <p>
     * <b>Path:</b> {@code GET /api/v1/logistica/envios/rutas}
     * @return Un {@link ResponseEntity} con la lista de {@link Ruta} o un estado NO_CONTENT si no hay rutas.
     */

    @GetMapping("")
    @Operation(summary = "Obtener todos las rutas.", description = "Obtiene una lista de todas las rutas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no hay contenido."),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista con todas las rutas.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del sevidor.")
    })
    public ResponseEntity<?> getRutas() {
        logger.info("Obteniendo todas las rutas");
        try {
            List<Ruta> rutas = rutaService.getAllRutas();

            if (rutas.isEmpty()) {
                logger.info("No se encontraron rutas");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            logger.info("Se encontraron {} rutas", rutas.size());
            return ResponseEntity.ok(rutas);
        } catch (Exception e) {
            logger.error("Error al obtener rutas", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene rutas dentro de un rango de coordenadas.
     * <p>
     * <b>Path:</b> {@code GET /api/v1/logistica/envios/rutas/{x_1}/{x_2}/{y_1}/{y_2}}
     * @param x_1 Coordenada X inicial.
     * @param x_2 Coordenada X final.
     * @param y_1 Coordenada Y inicial.
     * @param y_2 Coordenada Y final.
     * @return Un {@link ResponseEntity} con la lista de {@link Ruta} encontradas o un estado NO_CONTENT.
     */
    @GetMapping("/{x_1}/{x_2}/{y_1}/{y_2}")
    @Operation(summary = "Obtener todos las rutas en un rango de coordenadas", description = "Obtiene una lista de todas las rutas dependiendo de las coordenadas iniciales y finales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no hay contenido."),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista con todas las rutas dentro del rango de coordenadas.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del sevidor.")
    })
    public ResponseEntity<?> getRutaByCoords(
            @Parameter(description = "Coordenada X inicial de la ruta", required = true) @PathVariable Float x_1,
            @Parameter(description = "Coordenada X final de la ruta", required = true) @PathVariable Float x_2,
            @Parameter(description = "Coordenada Y inicial de la ruta", required = true) @PathVariable Float y_1,
            @Parameter(description = "Coordenada Y final de la ruta", required = true) @PathVariable Float y_2) {
        logger.info("Buscando rutas por coordenadas: x_1={}, x_2={}, y_1={}, y_2={}", x_1, x_2, y_1, y_2);
        try {
            List<Ruta> rutasEncontradas = rutaService.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2);
            if (rutasEncontradas.isEmpty()) {
                logger.info("No se encontraron rutas para las coordenadas dadas");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            logger.info("Se encontraron {} rutas", rutasEncontradas.size());
            return ResponseEntity.ok(rutasEncontradas);

        } catch (Exception e) {
            logger.error("Error al buscar rutas por coordenadas", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Crea una nueva ruta.
     * <p>
     * <b>Path:</b> {@code POST /api/v1/logistica/envios/rutas}
     * @param nuevaRuta El objeto {@link Ruta} a crear.
     * @return Un {@link ResponseEntity} con la {@link Ruta} creada y un estado CREATED, o un error del servidor.
     */
    @PostMapping("")
    @Operation(summary = "Registra una ruta a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve el la ruta registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo registrar(erorr interno)")
    })
    public ResponseEntity<?> createRuta(@RequestBody Ruta nuevaRuta) {
        logger.info("Creando nueva ruta: {}", nuevaRuta);
        try {
            rutaService.crearRuta(nuevaRuta);
            logger.info("Ruta creada exitosamente");
            return new ResponseEntity<>(nuevaRuta, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear ruta", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza completamente una ruta existente.
     * <p>
     * <b>Path:</b> {@code PUT /api/v1/logistica/envios/rutas/{idRuta}}
     * @param ruta El objeto {@link Ruta} con los nuevos datos.
     * @param idRuta El ID de la ruta a actualizar.
     * @return Un {@link ResponseEntity} con la {@link Ruta} actualizada o un error del servidor.
     */
    @PutMapping("/{idRuta}")
    @Operation(summary = "Actualiza una ruta a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve la ruta actualizada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> putRuta(@RequestBody @Schema(description = "Datos de la ruta a crear", example = "{\n" +
            "  \"coordXInicio\": 25.03,\n" +
            "  \"coordYInicio\": 15.03,\n" +
            "  \"coordXFinal\": -25.03,\n" +
            "  \"coordYFinal\": -15.03\n" +
            "}") Ruta ruta,
            @Parameter(description = "Id de la ruta a realizar el PUT", required = true) @PathVariable Integer idRuta) {
        logger.info("Actualizando ruta con id: {}", idRuta);
        try {
            Ruta rutaActualizada = rutaService.putRuta(ruta, idRuta);
            logger.info("Ruta actualizada exitosamente: {}", rutaActualizada);
            return ResponseEntity.ok(rutaActualizada);
        } catch (Exception e) {
            logger.error("Error al actualizar ruta", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Actualiza parcialmente una ruta existente.
     * <p>
     * <b>Path:</b> {@code PATCH /api/v1/logistica/envios/rutas/{idRuta}}
     * @param ruta El objeto {@link Ruta} con los campos a actualizar.
     * @param idRuta El ID de la ruta a actualizar.
     * @return Un {@link ResponseEntity} con la {@link Ruta} actualizada o un error del servidor.
     */
    @PatchMapping("/{idRuta}")
    @Operation(summary = "Parcha una ruta a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve la ruta parchada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> patchRuta(@RequestBody @Schema(description = "Datos de la ruta a crear", example = "{\n" +
            "  \"coordXInicio\": 25.03,\n" +
            "  \"coordYInicio\": 15.03,\n" +
            "  \"coordXFinal\": -25.03,\n" +
            "  \"coordYFinal\": -15.03\n" +
            "}") Ruta ruta,
            @Parameter(description = "Id de la ruta a PATCH.", required = true) @PathVariable Integer idRuta) {
        logger.info("Parchando ruta con id: {}", idRuta);
        try {
            Ruta rutaActualizada = rutaService.parcharRuta(ruta, idRuta);
            logger.info("Ruta parchada exitosamente: {}", rutaActualizada);
            return ResponseEntity.ok(rutaActualizada);
        } catch (Exception e) {
            logger.error("Error al parchar ruta", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Elimina una ruta por su ID.
     * <p>
     * <b>Path:</b> {@code DELETE /api/v1/logistica/envios/rutas/{idRuta}}
     * @param idRuta El ID de la ruta a eliminar.
     * @return Un {@link ResponseEntity} con estado NO_CONTENT si se elimina correctamente, o un error del servidor.
     */
    @DeleteMapping("/{idRuta}")
    @Operation(summary = "Borra una ruta usando su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no devuelve contenido."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> deleteRuta(
    @Parameter(description = "Id de la ruta a eliminar." , required = true)    
    @PathVariable Integer idRuta) {
        logger.info("Eliminando ruta con id: {}", idRuta);
        try {
            rutaService.deleteRuta(idRuta);
            logger.info("Ruta eliminada exitosamente");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Error al eliminar ruta", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
