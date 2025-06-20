package com.perfulandia.cl.logistica.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.service.VehiculoDespachoService;

@WebMvcTest(VehiculoDespachoControllerV2.class)
public class VehiculoDespachoControllerV2Test {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehiculoDespachoService vehiculoDespachoService;

    private VehiculoDespacho vehiculoMock = new VehiculoDespacho();
    

    @BeforeEach
    public void setUp(){
        List<Envio> envios = new ArrayList();
        vehiculoMock.setAno(2025);
        vehiculoMock.setEnvios(envios);

        // Terminar!!
    }





}
