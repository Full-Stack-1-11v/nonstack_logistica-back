package com.perfulandia.cl.logistica.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.model.VehiculosDespacho;
import com.perfulandia.cl.logistica.service.VehiculoDespachoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/logistica/vehiculos")
public class VehiculoDespachoController {

    @Autowired
    private VehiculoDespachoService vehiculoDespachoService;

    @GetMapping("")
    public ResponseEntity<?> getVehiculosDespacho() {
        try {
            List<VehiculosDespacho> vehiculos = vehiculoDespachoService.verVehiculosDespachos();
            if(vehiculos.size() == 0){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity.ok(vehiculos);
        } catch (Exception e) {
            return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> postMethodName(@RequestBody VehiculosDespacho vehiculo) {
        try {
            VehiculosDespacho vehiculoRegistrar = vehiculoDespachoService.registrarVehiculoDespacho(vehiculo);
            if(vehiculoRegistrar == null){
                return new ResponseEntity<>("vehiculo con esa patente ya existe" , HttpStatus.CONFLICT);
            } 
            
            return new ResponseEntity<>(vehiculo , HttpStatus.CREATED);
            
            
        } catch (Exception e) {
           return new ResponseEntity<>("Error al registrar el vehiculo : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    

}
