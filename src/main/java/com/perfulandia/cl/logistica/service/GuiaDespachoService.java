package com.perfulandia.cl.logistica.service;

import java.util.List;
import java.util.Optional;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;

import jakarta.transaction.Transactional;

@Service
public class GuiaDespachoService {

    @Autowired
    private GuiaDespachoRepository guiaDespachoRepository;

    public List<GuiaDespacho> verGuiaDespachos() {

        return guiaDespachoRepository.findAll();
    }
    
    @Transactional
    public GuiaDespacho crearGuiaDespacho(GuiaDespacho nuevaGuiaDespacho) {

        return guiaDespachoRepository.save(nuevaGuiaDespacho);

    }

    @Transactional
    public GuiaDespacho putGuiaDespacho(GuiaDespacho guiaDespacho, Integer id) {

        if (!guiaDespachoRepository.existsById(id)) {
            throw new RuntimeException("El despacho no existe");
        } else {
            Optional<GuiaDespacho> guiaDespachoExistenteOptional = guiaDespachoRepository.findById(id);
            GuiaDespacho guiaDespachoExistente = guiaDespachoExistenteOptional.get();
            guiaDespachoExistente.setIdEnvio(guiaDespacho.getIdEnvio());
            guiaDespachoExistente.setIdOrden(guiaDespacho.getIdOrden());
            guiaDespachoRepository.save(guiaDespachoExistente);
            return guiaDespachoExistente;
        }
    }

    @Transactional
    public GuiaDespacho parcharGuiaDespacho(GuiaDespacho guiaDespacho, Integer id) {

        if (!guiaDespachoRepository.existsById(id)) {
            throw new RuntimeCryptoException("El despacho no existe");
        } else {
            Optional<GuiaDespacho> guiaDespachoExistenteOptional = guiaDespachoRepository.findById(id);
            GuiaDespacho guiaDespachoExistente = guiaDespachoExistenteOptional.get();
            if (guiaDespacho.getIdOrden() == null && guiaDespacho.getIdEnvio() == null) {
                throw new RuntimeException("Debe de haber almenos un atributo para parchar");
            }
            if (guiaDespacho.getIdEnvio() != null) {
                guiaDespachoExistente.setIdEnvio(guiaDespacho.getIdEnvio());
            }
            if (guiaDespacho.getIdOrden() != null) {
                guiaDespachoExistente.setIdOrden(guiaDespacho.getIdOrden());
            }

            guiaDespachoRepository.save(guiaDespachoExistente);
            return guiaDespachoExistente;
        }
    }

    @Transactional
    public void borrarGuiaDespacho(Integer id) {
        if (guiaDespachoRepository.existsById(id)) {
            Optional<GuiaDespacho> guiaDespachoBorrar = guiaDespachoRepository.findById(id);
            guiaDespachoRepository.delete(guiaDespachoBorrar.get());
        } else {
            throw new RuntimeException("No existe la guia de despacho con esa id.");
        }
    }

}
