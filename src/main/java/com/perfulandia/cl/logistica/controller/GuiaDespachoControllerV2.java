package com.perfulandia.cl.logistica.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.assemblers.GuiaDespachoAssembler;

import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.service.GuiaDespachoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v2/logistica/despachos")
@Tag(name = "Guias de Despacho", description = "Operaciones relacionadas a las guias de despacho de Perfulandia")
public class GuiaDespachoControllerV2 {

    @Autowired
    private GuiaDespachoService guiaDespachoService;

    @Autowired
    GuiaDespachoAssembler assembler;

    @GetMapping("")
    @Operation(summary = "Obtener todos las guias de despacho", description = "Obtiene una lista de todas las guias de despacho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, pero no hay contenido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve todos los despachos."),
            @ApiResponse(responseCode = "500", description = "Error en el codigo.")
    })
    public ResponseEntity<CollectionModel<EntityModel<GuiaDespacho>>> getDespachos() {
        try {
            List<GuiaDespacho> despachos = guiaDespachoService.verGuiaDespachos();
            if (despachos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EntityModel<GuiaDespacho>> despachosHateoas = despachos.stream()
                    .map(assembler::toModel)
                    .toList();

            CollectionModel<EntityModel<GuiaDespacho>> collection = CollectionModel.of(despachosHateoas);

            return new ResponseEntity<>(collection, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener guia de despacho por id", description = "Obtiene una guia de despacho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el despacho."),
            @ApiResponse(responseCode = "404", description = "No encontrado.")
    }) // Change to path variable
    public ResponseEntity<EntityModel<GuiaDespacho>> getGuiaDespachoById(
            @Parameter(description = "Id guia despacho a buscar", required = true) @PathVariable Integer id) {
        Optional<GuiaDespacho> guiaOpt = guiaDespachoService.obtenerGuiaDespachoPorId(id);
        if (guiaOpt.isPresent()) {
            GuiaDespacho guiaDespacho = guiaOpt.get();
            EntityModel<GuiaDespacho> guiaEntity = assembler.toModel(guiaDespacho);
            return new ResponseEntity<>(guiaEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @Operation(summary = "Registra una guia de despacho traves de un body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Operacion exitosa, devuelve la guia de despacho registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "500", description = "No se pudo registrar(erorr interno)")
    })
    public ResponseEntity<EntityModel<GuiaDespacho>> createDespacho(
            @RequestBody @Schema(description = "Datos de la guia de despacho a crear", example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idOrden\": 1\n" +
                    "}") GuiaDespacho guiaDespacho) {
        try {
            GuiaDespacho guiaDespachoCreada = guiaDespachoService.crearGuiaDespacho(guiaDespacho);
            EntityModel<GuiaDespacho> guiaDespachoHateoas = assembler.toModel(guiaDespachoCreada);
            return new ResponseEntity<>(guiaDespachoHateoas, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una guia de despacho a traves de un body y la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve la guia de despacho actualizada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro la guia de despacho.")
    })
    public ResponseEntity<EntityModel<GuiaDespacho>> putGuiaDespacho(
            @Parameter(description = "Id guia despacho a actualizar", required = true) @PathVariable Integer id,
            @Schema(description = "Datos de la guia de despacho a realizar put", example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idOrden\": 1\n" +
                    "}") @RequestBody GuiaDespacho guiaDespacho) {
        try {
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.putGuiaDespacho(guiaDespacho, id);
            EntityModel<GuiaDespacho> guiaDespachoHateoas = assembler.toModel(guiaDespachoParchada);
            return new ResponseEntity<>(guiaDespachoHateoas, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve el envio parchado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "500", description = "Error al parchar.")
    })
    @Operation(summary = "Parcha una guia de despacho a traves de un body y la id")
    public ResponseEntity<EntityModel<GuiaDespacho>> patchGuiaDespacho(
            @Parameter(description = "Id guia de despacho a actualizar", required = true) @PathVariable Integer id,
            @Schema(description = "Datos de la guia de despacho a realizar put", example = "{\n" +
                    "  \"idEnvio\": 1,\n" +
                    "  \"idOrden\": 1\n" +
                    "}") @RequestBody GuiaDespacho guiaDespacho) {
        try {
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.parcharGuiaDespacho(guiaDespacho, id);
            EntityModel<GuiaDespacho> guiaDespachoHateoas = assembler.toModel(guiaDespachoParchada);
            return ResponseEntity.ok(guiaDespachoHateoas);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
            guiaDespachoService.borrarGuiaDespacho(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
