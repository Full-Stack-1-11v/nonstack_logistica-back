package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;

@Service
public class VehiculoDespachoService {

    @Autowired
    private VehiculoDespachoRepository vehiculoDespachoRepository;

    public List<VehiculoDespacho> verVehiculosDespachos(){
        return vehiculoDespachoRepository.findAll();
    }

    public VehiculoDespacho registrarVehiculoDespacho(VehiculoDespacho vehiculo){
        VehiculoDespacho vehiculoExistente = vehiculoDespachoRepository.findByPatente(vehiculo.getPatente()); // Puede devolver null si no encuentra!
        if(vehiculoExistente == null){
            vehiculoDespachoRepository.save(vehiculo);
            return vehiculo;
        }

        return null;
    }

    public VehiculoDespacho actualizarVehiculoDespacho(VehiculoDespacho vehiculo,String patente){
        VehiculoDespacho vehiculoExistente = vehiculoDespachoRepository.findByPatente(patente);
        if(vehiculoExistente != null){
            vehiculoExistente.setAno(vehiculo.getAno());
            vehiculoDespachoRepository.save(vehiculoExistente);
            return vehiculoExistente;
        }

        return null;
    }

}
