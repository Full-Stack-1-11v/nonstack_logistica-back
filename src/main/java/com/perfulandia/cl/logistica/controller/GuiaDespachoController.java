package com.perfulandia.cl.logistica.controller;

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

@RestController
@RequestMapping("/api/v1/logistica/despachos")
@Tag(name = "Guias de Despacho", description = "Operaciones relacionadas a las guias de despacho de Perfulandia")
public class GuiaDespachoController {

    @Autowired
    private GuiaDespachoService guiaDespachoService;
    @Autowired
    private OrdenDTOService UserDTOService;
    @Autowired
    private OrdenFeignClient ordenClient;

    @GetMapping("")
    @Operation(summary = "Obtener todos las guias de despacho", description = "Obtiene una lista de todas las guias de despacho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, pero no hay contenido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuiaDespacho.class))),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve todos los despachos."),
            @ApiResponse(responseCode = "500", description = "Error en el codigo.")
    })
    public ResponseEntity<?> getDespachos() {
        try {
            List<GuiaDespacho> despachos = guiaDespachoService.verGuiaDespachos();
            if (despachos.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<GuiaDespachoDTO> despachosDTO = despachos.stream()
                    .map(guia -> GuiaDespachoConverter.convertToDTO(guia, ordenClient))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(despachosDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

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
            guiaDespachoService.crearGuiaDespacho(guiaDespacho);
            return new ResponseEntity<>(guiaDespacho, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error : " + e.getMessage() + " " + e.getLocalizedMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.putGuiaDespacho(guiaDespacho, id);
            return ResponseEntity.ok(guiaDespachoParchada);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.parcharGuiaDespacho(guiaDespacho, id);
            return ResponseEntity.ok(guiaDespachoParchada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    @GetMapping("/test_ordenes")
    @Operation(summary = "Test Feign Client, no para uso final")
     @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, la api externa funciona!."),
            @ApiResponse(responseCode = "404", description = "No se encontro el endpoint.")
    })
    public ResponseEntity<?> getOrdenes() {
        try {
            List<OrdenDTO> ordenes = UserDTOService.verOrdenes();
            return ResponseEntity.ok(ordenes);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
