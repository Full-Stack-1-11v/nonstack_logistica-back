package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.dto.OrdenDTO;

@Service
public class OrdenDTOService {

    @Autowired
    private OrdenFeignClient ordenClient;

    public List<OrdenDTO> verOrdenes(){
        return ordenClient.getOrdenes();
    }

    public OrdenDTO obtenerOrdenPorId(Integer id){
        return ordenClient.obtenerOrdenPorId(id);
    }

}
