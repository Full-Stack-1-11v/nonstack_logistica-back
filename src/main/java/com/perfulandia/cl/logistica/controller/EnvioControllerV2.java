package com.perfulandia.cl.logistica.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.assemblers.EnvioModelAssembler;
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

@RestController
@RequestMapping("/api/v2/logistica/envios")
public class EnvioControllerV2 {

    @Autowired
    private EnvioService envioService;

    @Autowired
    private EnvioModelAssembler assembler;

    @GetMapping("")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> getEnvios() {
        List<Envio> envios = envioService.obtenerEnvios();
        if (envios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EntityModel<Envio>> enviosHateoas = envios.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(enviosHateoas);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> getEnvioPorId(@PathVariable Integer id) {
        Optional<Envio> envio = envioService.obtenerEnvioPorId(id);
        return envio
                .map(envioOptional -> assembler.toModel(envioOptional))
                .map(entityModel -> new ResponseEntity<>(entityModel, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-por-fecha/{fechaInicio}/{fechaFin}")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> getEnvioPorFecha(
            @PathVariable LocalDate fechaInicio,
            @PathVariable LocalDate fechaFin) {

        List<Envio> enviosEncontrados = envioService.buscarEnvioPorRangoDeFecha(fechaInicio, fechaFin);

        if (enviosEncontrados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Envio>> enviosHateoas = enviosEncontrados.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(enviosHateoas);

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);

    }

    @PostMapping("")
    @Operation(summary = "Registra un envio a traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve el envio registrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo registrar(erorr interno)")
    })
    public ResponseEntity<EntityModel<Envio>> crearEnvio(
            @RequestBody @Schema(description = "Datos del envio a crear", required = true,example = "{\n" +
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
            EntityModel<Envio> envioEntity = assembler.toModel(nuevoEnvio);
            return new ResponseEntity<>(envioEntity, HttpStatus.CREATED);
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
    public ResponseEntity<EntityModel<Envio>> actualizarEnvio(
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
                EntityModel<Envio> envioEntity = envioActualizado != null ? assembler.toModel(envioActualizado) : null;
                return ResponseEntity.ok(envioEntity);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Parcha un envio a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio parchado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Envio.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<EntityModel<Envio>> parcharEnvio(
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
            EntityModel<Envio> envioEntity = envioActualizado != null ? assembler.toModel(envioActualizado) : null;
            if (envioEntity == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(envioEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borra un envio usando su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, no devuelve contenido."),
            @ApiResponse(responseCode = "404", description = "No se encontro el envio.")
    })
    public ResponseEntity<Void> eliminarEnvio(
            @Parameter(description = "Id del envio a borrar", required = true) @PathVariable Integer id) {
        try {
            envioService.eliminarEnvio(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
