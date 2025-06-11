package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;

import jakarta.transaction.Transactional;

@Service
public class VehiculoDespachoService {

    @Autowired
    private VehiculoDespachoRepository vehiculoDespachoRepository;

    public List<VehiculoDespacho> verVehiculosDespachos() {
        return vehiculoDespachoRepository.findAll();
    }
    
    @Transactional
    public VehiculoDespacho registrarVehiculoDespacho(VehiculoDespacho vehiculo) {
        VehiculoDespacho vehiculoExistente = vehiculoDespachoRepository.findByPatente(vehiculo.getPatente());
        if (vehiculoExistente == null) {
            vehiculoDespachoRepository.save(vehiculo);
            return vehiculo;
        }

        return null;
    }

    @Transactional
    public VehiculoDespacho actualizarVehiculoDespacho(VehiculoDespacho vehiculo, String patente) {
        VehiculoDespacho vehiculoExistente = vehiculoDespachoRepository.findByPatente(patente);
        if (vehiculoExistente != null) {
            vehiculoExistente.setAno(vehiculo.getAno());
            vehiculoDespachoRepository.save(vehiculoExistente);
            return vehiculoExistente;
        }

        return null;
    }

    @Transactional
    public VehiculoDespacho parcharVehiculoDespacho(VehiculoDespacho vehiculo, String patente) {
        if (!vehiculoDespachoRepository.existsByPatente(patente)) {
            throw new RuntimeException("Vehiculo con la patente : " + patente + " no existe.");
        } else {
            if (vehiculo.getPatente() == null && vehiculo.getAno() == null) {
                throw new RuntimeException("El objeto debe tener al menos un atributo : patente y/o año");
            } else {
                VehiculoDespacho vehiculoExistente = vehiculoDespachoRepository.findByPatente(patente);
                if (vehiculo.getPatente() != null) {
                    vehiculoExistente.setPatente(vehiculo.getPatente());
                }
                if (vehiculo.getAno() != null) {
                    vehiculoExistente.setAno(vehiculo.getAno());
                }

                vehiculoDespachoRepository.save(vehiculoExistente);
                return vehiculoExistente;
            }

        }

    }

    public List<VehiculoDespacho> buscarVehiculoPorPatronPatente(String patente) {
        if(patente.length() != 2){
            throw new RuntimeException("Se deben de colocar dos valores alfanumericos como patron inicial");
        }

        List<VehiculoDespacho> vehiculosEncontrados = vehiculoDespachoRepository.buscarPorPatronPatente(patente);

        return vehiculosEncontrados;
    }

    @Transactional
    public void borrarVehiculoDespacho(String patente) {
        if(!vehiculoDespachoRepository.existsByPatente(patente)){
            throw new RuntimeException("Vehiculo con la patente + " + patente + " no existe");
        } else {
            vehiculoDespachoRepository.deleteByPatente(patente);
        }
    }

}
