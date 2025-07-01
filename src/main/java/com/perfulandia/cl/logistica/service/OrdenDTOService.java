package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.dto.OrdenDTO;

/**
 * Servicio para interactuar con el microservicio de Órdenes a través de un cliente Feign.
 * Encapsula la lógica para obtener datos de órdenes desde una fuente externa.
 */
@Service
public class OrdenDTOService {

    @Autowired
    private OrdenFeignClient ordenClient;

    /**
     * Obtiene una lista de todas las órdenes del servicio externo.
     * @return una lista de {@link OrdenDTO}.
     */
    public List<OrdenDTO> verOrdenes(){
        return ordenClient.getOrdenes();
    }

    /**
     * Obtiene una orden específica por su ID desde el servicio externo.
     * @param id el ID de la orden a obtener.
     * @return el {@link OrdenDTO} correspondiente al ID.
     */
    public OrdenDTO obtenerOrdenPorId(Integer id){
        return ordenClient.obtenerOrdenPorId(id);
    }

}
