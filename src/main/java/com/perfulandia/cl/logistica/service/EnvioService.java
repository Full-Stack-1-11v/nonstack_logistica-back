package com.perfulandia.cl.logistica.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;

import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar la lógica de negocio de los envíos.
 * Proporciona métodos para crear, leer, actualizar y eliminar envíos.
 */
@Service
@Transactional
public class EnvioService {

    @Autowired
    EnvioRepository envioRepository;
    @Autowired
    GuiaDespachoRepository guiaDespachoRepository;

    /**
     * Obtiene una lista de todos los envíos.
     * @return una lista de todas las entidades {@link Envio}.
     */
    public List<Envio> obtenerEnvios() {

        List<Envio> envios = envioRepository.findAll();

        return envios;

    }

    /**
     * Obtiene un envío por su ID.
     * @param id el ID del envío a buscar.
     * @return un {@link Optional} que contiene el {@link Envio} si se encuentra, o vacío si no.
     */
    public Optional<Envio> obtenerEnvioPorId(Integer id) {
        return envioRepository.findById(id);
    }

    /**
     * Crea un nuevo envío en la base de datos.
     * Asocia el envío a una guía de despacho existente.
     * @param envio el objeto {@link Envio} a crear.
     * @return el {@link Envio} guardado.
     * @throws IllegalArgumentException si la GuiaDespacho asociada no se encuentra.
     */
    public Envio crearEnvio(Envio envio) {
        // Obtener guiadespacho de la DB
        GuiaDespacho guiaDespacho = guiaDespachoRepository.findById(envio.getGuiaDespacho().getIdDespacho())
                .orElseThrow(() -> new IllegalArgumentException(
                        "GuiaDespacho with id " + envio.getGuiaDespacho().getIdDespacho() + " no encontrado"));

        // Falta verificar si el envio ya existe!
        // Asociar guia despacho al envio
        envio.setGuiaDespacho(guiaDespacho);

        // Guardar el envio
        return envioRepository.save(envio);
    }

    /**
     * Actualiza completamente un envío existente (operación PUT).
     * @param id el ID del envío a actualizar.
     * @param envio el objeto {@link Envio} con los nuevos datos.
     * @return el {@link Envio} actualizado.
     * @throws RuntimeException si no se encuentra un envío con el ID proporcionado.
     */
    public Envio actualizarEnvio(Integer id, Envio envio) {
        Optional<Envio> envioOpcional = envioRepository.findById(id);

        if (envioOpcional.isPresent()) {
            Envio envioExistente = envioOpcional.get();
            envioExistente.setIdCliente(envio.getIdCliente());
            envioExistente.setIdOrden(envio.getIdOrden());
            envioExistente.setFechaEntrega(envio.getFechaEntrega());
            envioExistente.setEntregado(envio.getEntregado());
            envioExistente.setObservacion(envio.getObservacion());

            return envioRepository.save(envioExistente);
        } else {
            throw new RuntimeException("No existe envio con esa id");
        }
    }

    /**
     * Actualiza parcialmente un envío existente (operación PATCH).
     * @param id el ID del envío a actualizar.
     * @param envio el objeto {@link Envio} con los campos a actualizar.
     * @return el {@link Envio} actualizado.
     * @throws RuntimeException si no se encuentra el envío o si el objeto de parcheo está vacío.
     */
    public Envio parcharEnvio(Integer id, Envio envio) {
        if (!envioRepository.existsById(id)) {
            throw new RuntimeException("El envio no existe con esa id :" + id);
        }

        if (envio.getIdCliente() == null &&
                envio.getIdOrden() == null &&
                envio.getFechaEntrega() == null &&
                envio.getEntregado() == null && // Cambiado a getEntregado() == null
                envio.getObservacion() == null) {
            throw new RuntimeException("El objeto PATCH no contiene ningún atributo para actualizar.");
        }

        Optional<Envio> envioExistenteOptional = envioRepository.findById(id);

        Envio envioExistente = envioExistenteOptional.get();

        if (envio.getIdCliente() != null) {
            envioExistente.setIdCliente(envio.getIdCliente());
        }
        if (envio.getIdOrden() != null) {
            envioExistente.setIdOrden(envio.getIdOrden());
        }
        if (envio.getFechaEntrega() != null) {
            envioExistente.setFechaEntrega(envio.getFechaEntrega());
        }
        if (envio.getEntregado() != null) {
            envioExistente.setEntregado(envio.getEntregado());
        }
        if (envio.getObservacion() != null) {
            envioExistente.setObservacion(envio.getObservacion());
        }

        return envioRepository.save(envioExistente);
    }

    /**
     * Busca envíos dentro de un rango de fechas de entrega.
     * @param fechaInicial la fecha de inicio del rango.
     * @param fechaFinal la fecha de fin del rango.
     * @return una lista de {@link Envio} que se encuentran dentro del rango de fechas.
     */
    public List<Envio> buscarEnvioPorRangoDeFecha(LocalDate fechaInicial, LocalDate fechaFinal) {
        List<Envio> enviosEncontrados = envioRepository.buscarPorRangoDeFecha(fechaInicial, fechaFinal);
        return enviosEncontrados;
    }

    /**
     * Elimina un envío por su ID.
     * @param id el ID del envío a eliminar.
     * @throws RuntimeException si no se encuentra un envío con el ID proporcionado.
     */
    public void eliminarEnvio(Integer id) {
        Optional<Envio> envioExistente = envioRepository.findById(id);
        if (envioExistente.isPresent()) {
            envioRepository.deleteById(id);
        } else {
            throw new RuntimeException("No existe envio con esa id");
        }
    }

}
