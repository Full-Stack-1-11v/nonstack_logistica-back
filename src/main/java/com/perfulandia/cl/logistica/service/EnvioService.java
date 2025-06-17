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

@Service
@Transactional
public class EnvioService {

    @Autowired
    EnvioRepository envioRepository;
    @Autowired
    GuiaDespachoRepository guiaDespachoRepository;

    public List<Envio> obtenerEnvios() {

        List<Envio> envios = envioRepository.findAll();

        return envios;

    }

    public Optional<Envio> obtenerEnvioPorId(Integer id) {
        return envioRepository.findById(id);
    }

    public Envio crearEnvio(Envio envio) {
        // Obtener guiadespacho de la DB
        GuiaDespacho guiaDespacho = guiaDespachoRepository.findById(envio.getGuiaDespacho().getIdDespacho())
                .orElseThrow(() -> new IllegalArgumentException(
                        "GuiaDespacho with id " + envio.getGuiaDespacho().getIdDespacho() + " no encontrado"));

        // Asociar guia despacho al envio
        envio.setGuiaDespacho(guiaDespacho);

        // Guardar el envio
        return envioRepository.save(envio);
    }

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

    public List<Envio> buscarEnvioPorRangoDeFecha(LocalDate fechaInicial, LocalDate fechaFinal) {
        List<Envio> enviosEncontrados = envioRepository.buscarPorRangoDeFecha(fechaInicial, fechaFinal);
        return enviosEncontrados;
    }

    public void eliminarEnvio(Integer id) {
        Optional<Envio> envioExistente = envioRepository.findById(id);
        if (envioExistente.isPresent()) {
            envioRepository.deleteById(id);
        } else {
            throw new RuntimeException("No existe envio con esa id");
        }
    }

}
