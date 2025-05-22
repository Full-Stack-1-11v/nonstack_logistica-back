package com.perfulandia.cl.logistica.controller;

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
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1/logistica/envios")
public class EnvioController {

    @Autowired
    EnvioService envioService;

    @GetMapping("")
    public ResponseEntity<?> getEnvios() {
        List<Envio> envios = envioService.obtenerEnvios();

        if(envios.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<EnvioDTO> envioDTOs = envios.stream()
            .map(EnvioConverter::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(envioDTOs);
    }
    

}
