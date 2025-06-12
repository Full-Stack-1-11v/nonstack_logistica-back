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
import org.springframework.web.server.ResponseStatusException;

import com.perfulandia.cl.logistica.assemblers.EnvioModelAssembler;
import com.perfulandia.cl.logistica.converter.EnvioConverter;
import com.perfulandia.cl.logistica.dto.EnvioDTO;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.service.EnvioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


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
        if(envios.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        List<EntityModel<Envio>> enviosHateoas = envios.stream()
                                                .map(assembler::toModel)
                                                .collect(Collectors.toList());
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(enviosHateoas);

        return new ResponseEntity<>(collectionModel,HttpStatus.OK);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> getEnvioPorId(@PathVariable Integer id) {
        Optional<Envio> envio = envioService.obtenerEnvioPorId(id);
        return envio
            .map(envioOptional -> assembler.toModel(envioOptional))
            .map(entityModel -> new ResponseEntity<>(entityModel,HttpStatus.OK))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-por-fecha/{fechaInicio}/{fechaFin}")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> getEnvioPorFecha(
        @PathVariable LocalDate fechaInicio,
        @PathVariable LocalDate fechaFin){

        List<Envio> enviosEncontrados = envioService.buscarEnvioPorRangoDeFecha(fechaInicio, fechaFin);

        if(enviosEncontrados.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Envio>> enviosHateoas = enviosEncontrados.stream()
                                                .map(assembler::toModel)
                                                .collect(Collectors.toList());
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(enviosHateoas);
        
        return new ResponseEntity<>(collectionModel,HttpStatus.OK);

    }
    

}
