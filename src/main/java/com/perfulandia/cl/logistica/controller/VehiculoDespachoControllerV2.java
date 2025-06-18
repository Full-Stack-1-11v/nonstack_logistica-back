package com.perfulandia.cl.logistica.controller;

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

@RestController
@RequestMapping("/api/v2/logistica/vehiculos")
@Tag(name = "Vehículos de Despacho", description = "Operaciones relacionadas a los vehiculos de despacho de Perfulandia")
public class VehiculoDespachoControllerV2 {

    @Autowired
    private VehiculoDespachoService vehiculoDespachoService;
    @Autowired
    private VehiculoDespachoDTOAssembler vehiculoDTOModelAssembler;
    @Autowired
    private VehiculoDespachoAssembler vehiculoDespachoAssembler;

    @GetMapping("")
    @Operation(summary = "Obtener todos los vehiculos de despacho.", description = "Obtiene una lista de todos los vehiculos de despacho.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion exitosa, mo se encontraron vehiculos"),
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista de vehiculos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespachoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron vehiculos")
    })
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDespachoDTO>>> getVehiculosDespacho() {
        try {
            List<VehiculoDespacho> vehiculos = vehiculoDespachoService.verVehiculosDespachos();
            if (vehiculos.size() == 0) {
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

            return ResponseEntity.ok(collectionModel);
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{patron_patente}")
    @Operation(summary = "Obtener todos los vehiculos de despacho por patron de las dos primeras letras de la patente.", description = "Obtiene una lista de todos los vehiculos de despacho por las dos primeras letras de la petente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa, devuelve lista de vehiculos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehiculoDespachoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Busqueda mal realizda.")
    })
    public ResponseEntity<CollectionModel<EntityModel<VehiculoDespachoDTO>>> buscarPorPatronPatente(
            @Parameter(description = "Valor de solo DOS letras", required = true, example = "AA") @PathVariable String patron_patente) {
        try {
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
            return ResponseEntity.ok(collectionModel);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

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
        try {
            VehiculoDespacho vehiculoRegistrar = vehiculoDespachoService.registrarVehiculoDespacho(vehiculo);
            EntityModel<VehiculoDespacho> vehiculoEntity = vehiculoDespachoAssembler.toModel(vehiculoRegistrar);
            if (vehiculoRegistrar == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return new ResponseEntity<>(vehiculoEntity, HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("Error al registrar el vehiculo : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

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
        try {
            VehiculoDespacho vehiculoExistente = vehiculoDespachoService.actualizarVehiculoDespacho(vehiculo, patente);
            EntityModel<VehiculoDespacho> vehiculoEntity = vehiculoDespachoAssembler.toModel(vehiculoExistente);
            if (vehiculoExistente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(vehiculoEntity, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error al actualizar el vehiculo");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
        try {
            VehiculoDespacho vehiculoParchar = vehiculoDespachoService.parcharVehiculoDespacho(vehiculo, patente);
            EntityModel<VehiculoDespacho> vehiculoEntity = vehiculoDespachoAssembler.toModel(vehiculoParchar);
            return ResponseEntity.ok(vehiculoEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{patente}")
    @Operation(summary = "Borra un vehiculo a traves de su patente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehiculo elimnado"),
            @ApiResponse(responseCode = "400", description = "Operacion erronea, vehiculo no existe o request mal realizado")
    })
    public ResponseEntity<?> borrarVehiculoDespacho(@PathVariable String patente) {
        try {
            vehiculoDespachoService.borrarVehiculoDespacho(patente);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
