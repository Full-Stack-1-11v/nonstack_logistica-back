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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/logistica/envios/rutas")

public class RutaController {

    @Autowired
    private RutaService rutaService;

    @GetMapping("")
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
    public ResponseEntity<?> getRutaByCoords(@PathVariable Float x_1,
            @PathVariable Float x_2,
            @PathVariable Float y_1,
            @PathVariable Float y_2) {

        try {
            List<Ruta> rutasEncontradas = rutaService.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2);
            if(rutasEncontradas.size() == 0){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity.ok(rutasEncontradas);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @PostMapping("")
    public ResponseEntity<?> createRuta(@RequestBody Ruta nuevaRuta) {

        try {
            rutaService.crearRuta(nuevaRuta);
            return ResponseEntity.ok(nuevaRuta);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{idRuta}")
    public ResponseEntity<?> putRuta(@RequestBody Ruta ruta, @PathVariable Integer idRuta) {
        try {
            Ruta rutaActualizada = rutaService.putRuta(ruta, idRuta);
            return ResponseEntity.ok(rutaActualizada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{idRuta}")
    public ResponseEntity<?> patchRuta(@RequestBody Ruta ruta, @PathVariable Integer idRuta) {
        try {
            Ruta rutaActualizada = rutaService.parcharRuta(ruta, idRuta);
            return ResponseEntity.ok(rutaActualizada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{idRuta}")
    public ResponseEntity<?> deleteRuta(@PathVariable Integer idRuta) {
        try {
            rutaService.deleteRuta(idRuta);
            return ResponseEntity.ok("Ruta eliminada");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
