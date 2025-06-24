package com.perfulandia.cl.logistica.service;

import java.util.List;
import java.util.Optional;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;

import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar la lógica de negocio de las guías de despacho.
 * Proporciona métodos para crear, leer, actualizar y eliminar guías de despacho.
 */
@Service
public class GuiaDespachoService {

    @Autowired
    private GuiaDespachoRepository guiaDespachoRepository;

    /**
     * Obtiene una lista de todas las guías de despacho.
     * @return una lista de todas las entidades {@link GuiaDespacho}.
     */
    public List<GuiaDespacho> verGuiaDespachos() {

        return guiaDespachoRepository.findAll();
    }
    
    /**
     * Crea una nueva guía de despacho.
     * @param nuevaGuiaDespacho el objeto {@link GuiaDespacho} a crear.
     * @return la {@link GuiaDespacho} guardada.
     */
    @Transactional
    public GuiaDespacho crearGuiaDespacho(GuiaDespacho nuevaGuiaDespacho) {

        return guiaDespachoRepository.save(nuevaGuiaDespacho);

    }

    /**
     * Obtiene una guía de despacho por su ID.
     * @param id el ID de la guía de despacho a buscar.
     * @return un {@link Optional} que contiene la {@link GuiaDespacho} si se encuentra, o vacío si no.
     */
    @Transactional
    public Optional<GuiaDespacho> obtenerGuiaDespachoPorId(Integer id){
        Optional<GuiaDespacho> guiaDespacho = guiaDespachoRepository.findById(id);

        return guiaDespacho;
    }

    /**
     * Actualiza completamente una guía de despacho existente (operación PUT).
     * @param guiaDespacho el objeto {@link GuiaDespacho} con los nuevos datos.
     * @param id el ID de la guía de despacho a actualizar.
     * @return la {@link GuiaDespacho} actualizada.
     * @throws RuntimeException si no se encuentra una guía de despacho con el ID proporcionado.
     */
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

    /**
     * Actualiza parcialmente una guía de despacho existente (operación PATCH).
     * @param guiaDespacho el objeto {@link GuiaDespacho} con los campos a actualizar.
     * @param id el ID de la guía de despacho a actualizar.
     * @return la {@link GuiaDespacho} actualizada.
     * @throws RuntimeCryptoException si no se encuentra la guía de despacho.
     * @throws RuntimeException si el objeto de parcheo está vacío.
     */
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

    /**
     * Elimina una guía de despacho por su ID.
     * @param id el ID de la guía de despacho a eliminar.
     * @throws RuntimeException si no se encuentra una guía de despacho con el ID proporcionado.
     */
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
