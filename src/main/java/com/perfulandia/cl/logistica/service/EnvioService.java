package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.repository.EnvioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnvioService {

    @Autowired
    EnvioRepository envioRepository;

    public List<Envio> obtenerEnvios(){
        
        List<Envio> envios = envioRepository.findAll();

        return envios;
    }

}
