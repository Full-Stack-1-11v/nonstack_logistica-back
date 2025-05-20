package com.perfulandia.cl.logistica.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.cl.logistica.dto.OrdenDTO;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.service.GuiaDespachoService;
import com.perfulandia.cl.logistica.service.UserDTOService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/logistica/despachos")
public class GuiaDespachoController {

    @Autowired
    private GuiaDespachoService guiaDespachoService;
    @Autowired
    private UserDTOService UserDTOService;

    @GetMapping("")
    public ResponseEntity<?> getDespachos() {
        try {
            List<GuiaDespacho> despachos = guiaDespachoService.verGuiaDespachos();
            if (despachos.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(despachos, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/test_ordenes")
    public ResponseEntity<?> getOrdenes() {
        try {
            List<OrdenDTO> ordenes = UserDTOService.verOrdenes();
            return ResponseEntity.ok(ordenes);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
