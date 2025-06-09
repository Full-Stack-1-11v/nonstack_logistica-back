package com.perfulandia.cl.logistica.client;

import java.util.List;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.perfulandia.cl.logistica.dto.OrdenDTO;

@FeignClient(name = "ORDENES-API" , url="${external.ordenes.api.base-url}")
public interface OrdenFeignClient {

    @GetMapping(value = "/listar" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<OrdenDTO> getOrdenes();

    @GetMapping(value = "/{id}")
    public OrdenDTO obtenerOrdenPorId(@PathVariable Integer id);


}
