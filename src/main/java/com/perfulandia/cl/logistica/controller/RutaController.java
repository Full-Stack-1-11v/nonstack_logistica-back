package com.perfulandia.cl.logistica.controller;

import java.util.List;

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

@RestController
@RequestMapping("/api/v1/logistica/envios/rutas")
@Tag(name = "Rutas", description = "Operaciones relacionadas a la información de rutas,")
public class RutaController {

    @Autowired
    private RutaService rutaService;

    @GetMapping("")
    @Operation(summary = "Obtener todos las rutas.", description = "Obtiene una lista de todas las rutas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no hay contenido."),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista con todas las rutas.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del sevidor.")
    })
    public ResponseEntity<?> getRutas() {
        try {
            List<Ruta> rutas = rutaService.getAllRutas();

            if (rutas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity.ok(rutas);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

        try {
            List<Ruta> rutasEncontradas = rutaService.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2);
            if (rutasEncontradas.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity.ok(rutasEncontradas);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("")
    @Operation(summary = "Registra una ruta a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve el la ruta registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ruta.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo registrar(erorr interno)")
    })
    public ResponseEntity<?> createRuta(@RequestBody Ruta nuevaRuta) {

        try {
            rutaService.crearRuta(nuevaRuta);
            return new ResponseEntity<>(nuevaRuta, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
        try {
            Ruta rutaActualizada = rutaService.putRuta(ruta, idRuta);
            return ResponseEntity.ok(rutaActualizada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
        try {
            Ruta rutaActualizada = rutaService.parcharRuta(ruta, idRuta);
            return ResponseEntity.ok(rutaActualizada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{idRuta}")
    @Operation(summary = "Borra una ruta usando su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no devuelve contenido."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> deleteRuta(
    @Parameter(description = "Id de la ruta a eliminar." , required = true)    
    @PathVariable Integer idRuta) {
        try {
            rutaService.deleteRuta(idRuta);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
