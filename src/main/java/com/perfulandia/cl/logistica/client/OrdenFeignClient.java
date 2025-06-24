package com.perfulandia.cl.logistica.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.perfulandia.cl.logistica.dto.OrdenDTO;
/**
 * Cliente Feign para interactuar con el microservicio de Órdenes.
 * Permite consumir los endpoints expuestos por la API de Órdenes.
 */
@FeignClient(name = "ORDENES-API" , url="${external.ordenes.api.base-url}")
public interface OrdenFeignClient {
    /**
     * Obtiene una lista de todas las órdenes desde la API de Órdenes.
     * @return una lista de {@link OrdenDTO}.
     */
    @GetMapping(value = "/listar" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<OrdenDTO> getOrdenes();
    /**
     * Obtiene una orden específica por su ID desde la API de Órdenes.
     * @param id el ID de la orden a obtener.
     * @return la {@link OrdenDTO} correspondiente al ID proporcionado.
     */
    @GetMapping(value = "/{id}")
    public OrdenDTO obtenerOrdenPorId(@PathVariable Integer id);


}
