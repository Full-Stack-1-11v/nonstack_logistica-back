package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;

import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar la lógica de negocio de los vehículos de despacho.
 * Proporciona métodos para crear, leer, actualizar y eliminar vehículos.
 */
@Service
public class VehiculoDespachoService {

    @Autowired
    private VehiculoDespachoRepository vehiculoDespachoRepository;

    /**
     * Obtiene una lista de todos los vehículos de despacho.
     * @return una lista de todas las entidades {@link VehiculoDespacho}.
     */
    public List<VehiculoDespacho> verVehiculosDespachos() {
        return vehiculoDespachoRepository.findAll();
    }
    
    /**
     * Registra un nuevo vehículo de despacho, asegurándose de que la patente no exista previamente.
     * @param vehiculo el objeto {@link VehiculoDespacho} a registrar.
     * @return el {@link VehiculoDespacho} guardado, o null si la patente ya existe.
     */
    @Transactional
    public VehiculoDespacho registrarVehiculoDespacho(VehiculoDespacho vehiculo) {
        VehiculoDespacho vehiculoExistente = vehiculoDespachoRepository.findByPatente(vehiculo.getPatente());
        if (vehiculoExistente == null) {
            vehiculoDespachoRepository.save(vehiculo);
            return vehiculo;
        }

        return null;
    }

    /**
     * Actualiza completamente un vehículo de despacho existente (operación PUT).
     * @param vehiculo el objeto {@link VehiculoDespacho} con los nuevos datos.
     * @param patente la patente del vehículo a actualizar.
     * @return el {@link VehiculoDespacho} actualizado, o null si no se encuentra.
     */
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

    /**
     * Actualiza parcialmente un vehículo de despacho existente (operación PATCH).
     * @param vehiculo el objeto {@link VehiculoDespacho} con los campos a actualizar.
     * @param patente la patente del vehículo a actualizar.
     * @return el {@link VehiculoDespacho} actualizado.
     * @throws RuntimeException si el vehículo no existe o si el objeto de parcheo está vacío.
     */
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

    /**
     * Busca vehículos cuya patente comience con un patrón de dos caracteres.
     * @param patente el patrón de dos caracteres para buscar.
     * @return una lista de {@link VehiculoDespacho} que coinciden con el patrón.
     * @throws RuntimeException si el patrón no tiene exactamente dos caracteres.
     */
    public List<VehiculoDespacho> buscarVehiculoPorPatronPatente(String patente) {
        if(patente.length() != 2){
            throw new RuntimeException("Se deben de colocar dos valores alfanumericos como patron inicial");
        }

        List<VehiculoDespacho> vehiculosEncontrados = vehiculoDespachoRepository.buscarPorPatronPatente(patente);

        return vehiculosEncontrados;
    }

    /**
     * Elimina un vehículo de despacho por su patente.
     * @param patente la patente del vehículo a eliminar.
     * @throws RuntimeException si no se encuentra un vehículo con la patente proporcionada.
     */
    @Transactional
    public void borrarVehiculoDespacho(String patente) {
        if(!vehiculoDespachoRepository.existsByPatente(patente)){
            throw new RuntimeException("Vehiculo con la patente + " + patente + " no existe");
        } else {
            vehiculoDespachoRepository.deleteByPatente(patente);
        }
    }

}
