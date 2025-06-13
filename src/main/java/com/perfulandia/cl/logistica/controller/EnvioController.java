package com.perfulandia.cl.logistica.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/logistica/envios")
@Tag(name = "Envios", description = "Operaciones relacionadas a los envios de Perfulandia")
public class EnvioController {

    @Autowired
    EnvioService envioService;

    @GetMapping("")
    @Operation(summary = "Obtener todos los envios", description = "Obtiene una lista de todas las carreras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve los envios encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron envios")
    })
    public ResponseEntity<?> getEnvios() {
        List<Envio> envios = envioService.obtenerEnvios();

        if (envios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EnvioDTO> envioDTOs = envios.stream()
                .map(EnvioConverter::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(envioDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene datos de un envios.", description = "A traves de la id de un envio obtiene los detalles de este.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio con el id entregado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron envios con esa id")
    })
    public ResponseEntity<?> getEnvioPorId(
            @Parameter(description = "ID del envio a obtener", required = true) @PathVariable Integer id) {
        return envioService.obtenerEnvioPorId(id)
                .map(envio -> {
                    EnvioDTO envioDTO = EnvioConverter.convertToDTO(envio);
                    return ResponseEntity.ok(envioDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-por-fecha/{fechaInicio}/{fechaFin}")
    @Operation(summary = "Obtiene envios en un rango de fecha determinado.", description = "A traves del rango de fecha entrega una lista de envios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve los envios encontradas en ese rango de fecha", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "400", description = "Request incorrecto")
    })
    public ResponseEntity<?> findEnvioByDate(
            @Parameter(description = "Fecha inicial (inclusive)", required = true, example = "2023-04-01") @PathVariable LocalDate fechaInicio,
            @Parameter(description = "Fecha final (inclusive)", required = true, example = "2025-12-12") @PathVariable LocalDate fechaFin) {
        try {
            List<Envio> enviosEncontrados = envioService.buscarEnvioPorRangoDeFecha(fechaInicio, fechaFin);
            List<EnvioDTO> enviosDTO = enviosEncontrados.stream()
                    .map(EnvioConverter::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(enviosDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    @Operation(summary = "Registra un envio a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve el envio registrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo registrar(erorr interno)")
    })
    public ResponseEntity<?> crearEnvio(
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
            Envio nuevoEnvio = envioService.crearEnvio(envio);
            EnvioDTO envioDTO = EnvioConverter.convertToDTO(nuevoEnvio);
            return new ResponseEntity<>(envioDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un envio a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio actualizado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<?> actualizarEnvio(
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
            Envio envioActualizado = envioService.actualizarEnvio(id, envio);
            if (envioActualizado != null) {
                EnvioDTO envioDTO = EnvioConverter.convertToDTO(envioActualizado);
                return ResponseEntity.ok(envioDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Parcha un envio a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio parchado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<?> parcharEnvio(
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
            Envio envioActualizado = envioService.parcharEnvio(id, envio);
            EnvioDTO envioDTO = EnvioConverter.convertToDTO(envioActualizado);
            return ResponseEntity.ok(envioDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un envio usando su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no devuelve contenido."),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<?> eliminarEnvio(
            @Parameter(description = "Id del envio a borrar", required = true) @PathVariable Integer id) {
        try {
            envioService.eliminarEnvio(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
