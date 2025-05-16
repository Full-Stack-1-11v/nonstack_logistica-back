package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.VehiculosDespacho;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;

@Service
public class VehiculoDespachoService {

    @Autowired
    private VehiculoDespachoRepository vehiculoDespachoRepository;

    public List<VehiculosDespacho> verVehiculosDespachos(){
        return vehiculoDespachoRepository.findAll();
    }

    public VehiculosDespacho registrarVehiculoDespacho(VehiculosDespacho vehiculo){
        VehiculosDespacho vehiculoExistente = vehiculoDespachoRepository.findByPatente(vehiculo.getPatente()); // Puede devolver null si no encuentra!
        if(vehiculoExistente == null){
            vehiculoDespachoRepository.save(vehiculo);
            return vehiculo;
        }

        return null;
    }

}
