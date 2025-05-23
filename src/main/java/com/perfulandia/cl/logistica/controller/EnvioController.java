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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/logistica/envios")
public class EnvioController {

    @Autowired
    EnvioService envioService;

    @GetMapping("")
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
    public ResponseEntity<?> getEnvioPorId(@PathVariable Integer id) {
        return envioService.obtenerEnvioPorId(id)
                .map(envio -> {
                    EnvioDTO envioDTO = EnvioConverter.convertToDTO(envio);
                    return ResponseEntity.ok(envioDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-por-fecha/{fechaInicio}/{fechaFin}")
    public ResponseEntity<?> findEnvioByDate(@PathVariable LocalDate fechaInicio,@PathVariable LocalDate fechaFin) {
        try {
            List<Envio> enviosEncontrados = envioService.buscarEnvioPorRangoDeFecha(fechaInicio, fechaFin);
            List<EnvioDTO> enviosDTO = enviosEncontrados.stream()
                .map(EnvioConverter::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(enviosDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    

    @PostMapping("")
    public ResponseEntity<?> crearEnvio(@RequestBody Envio envio) {
        Envio nuevoEnvio = envioService.crearEnvio(envio);
        EnvioDTO envioDTO = EnvioConverter.convertToDTO(nuevoEnvio);
        return new ResponseEntity<>(envioDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEnvio(@PathVariable Integer id, @RequestBody Envio envio) {
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
    public ResponseEntity<?> parcharEnvio(@PathVariable Integer id, @RequestBody Envio envio) {
        try {
            Envio envioActualizado = envioService.parcharEnvio(id, envio);
            EnvioDTO envioDTO = EnvioConverter.convertToDTO(envioActualizado);
            return ResponseEntity.ok(envioDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEnvio(@PathVariable Integer id) {
        try {
            envioService.eliminarEnvio(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
