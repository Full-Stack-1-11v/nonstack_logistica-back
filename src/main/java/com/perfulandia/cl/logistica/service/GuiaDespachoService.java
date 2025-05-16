package com.perfulandia.cl.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;

@Service
public class GuiaDespachoService {

    @Autowired
    private GuiaDespachoRepository guiaDespachoRepository;

    public List<GuiaDespacho> verGuiaDespachos(){

        return guiaDespachoRepository.findAll();
    }

}
