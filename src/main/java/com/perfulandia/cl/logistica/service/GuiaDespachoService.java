package com.perfulandia.cl.logistica.service;

import java.util.List;
import java.util.Optional;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;

@Service
public class GuiaDespachoService {

    @Autowired
    private GuiaDespachoRepository guiaDespachoRepository;

    public List<GuiaDespacho> verGuiaDespachos() {

        return guiaDespachoRepository.findAll();
    }

    public GuiaDespacho crearGuiaDespacho(GuiaDespacho nuevaGuiaDespacho) throws Exception {

        return guiaDespachoRepository.save(nuevaGuiaDespacho);

    }

    public GuiaDespacho putGuiaDespacho(GuiaDespacho guiaDespacho, Integer id) throws Exception {
        
        if (guiaDespachoRepository.existsById(id)) {
            throw new RuntimeException("El despacho no existe");
        } else {
            Optional<GuiaDespacho> guiaDespachoExistente = guiaDespachoRepository.findById(id);
            guiaDespachoExistente.get().setIdEnvio(guiaDespacho.getIdEnvio());
            guiaDespachoExistente.get().setIdOrden(guiaDespacho.getIdOrden());
            guiaDespachoRepository.save(guiaDespachoExistente.get());
            return guiaDespachoExistente.get();
        }
    }

    public GuiaDespacho parcharGuiaDespacho(GuiaDespacho guiaDespacho, Integer id) throws Exception {
        
        if (!guiaDespachoRepository.existsById(id)) {
            throw new RuntimeCryptoException("El despacho no existe");
        } else {
            Optional<GuiaDespacho> guiaDespachoExistente = guiaDespachoRepository.findById(id);
            if (guiaDespacho.getIdOrden() == null && guiaDespacho.getIdEnvio() == null) {
                throw new RuntimeException("Debe de haber almenos un atributo para parchar");
            }
            if (guiaDespacho.getIdEnvio() != null) {
                guiaDespachoExistente.get().setIdEnvio(guiaDespacho.getIdEnvio());
            }
            if (guiaDespacho.getIdOrden() != null) {
                guiaDespachoExistente.get().setIdOrden(guiaDespacho.getIdOrden());
            }

            guiaDespachoRepository.save(guiaDespachoExistente.get());
            return guiaDespachoExistente.get();
        }
    }

    public void borrarGuiaDespacho(Integer id) throws Exception{
        if(guiaDespachoRepository.existsById(id)){
            Optional<GuiaDespacho> guiaDespachoBorrar = guiaDespachoRepository.findById(id);
            guiaDespachoRepository.delete(guiaDespachoBorrar.get());
        } else {
            throw new RuntimeException("No existe la guia de despacho con esa id.");
        }
    }

}
