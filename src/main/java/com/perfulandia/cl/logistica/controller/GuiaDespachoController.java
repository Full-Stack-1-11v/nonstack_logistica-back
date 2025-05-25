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
public class GuiaDespachoController {

    @Autowired
    private GuiaDespachoService guiaDespachoService;
    @Autowired
    private OrdenDTOService UserDTOService;
    @Autowired
    private OrdenFeignClient ordenClient;

    @GetMapping("")
    public ResponseEntity<?> getDespachos() {
        try {
            List<GuiaDespacho> despachos = guiaDespachoService.verGuiaDespachos();
            if (despachos.size() == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<GuiaDespachoDTO> despachosDTO = despachos.stream()
                .map(guia -> GuiaDespachoConverter.convertToDTO(guia,guia.getIdOrden(),ordenClient))
                .collect(Collectors.toList());

            return new ResponseEntity<>(despachosDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping()
    public ResponseEntity<?> createDespacho(@RequestBody GuiaDespacho guiaDespacho) {
        try {
           guiaDespachoService.crearGuiaDespacho(guiaDespacho);
           return ResponseEntity.ok(guiaDespacho);
        } catch (Exception e) {
            return new ResponseEntity<>("Error : " + e.getMessage() + " " + e.getLocalizedMessage()
                                        ,HttpStatus.INTERNAL_SERVER_ERROR);  
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putGuiaDespacho(@PathVariable Integer id, @RequestBody GuiaDespacho guiaDespacho) {
        try {
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.putGuiaDespacho(guiaDespacho,id);
            return ResponseEntity.ok(guiaDespachoParchada);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchGuiaDespacho(@PathVariable Integer id , @RequestBody GuiaDespacho guiaDespacho){
        try {
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.parcharGuiaDespacho(guiaDespacho, id);
            return ResponseEntity.ok(guiaDespachoParchada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGuiaDespacho(@PathVariable Integer id){
        try {
            guiaDespachoService.borrarGuiaDespacho(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
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
