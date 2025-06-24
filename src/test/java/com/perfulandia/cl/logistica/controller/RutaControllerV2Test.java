package com.perfulandia.cl.logistica.controller;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.cl.logistica.assemblers.RutaAssembler;
import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;
import com.perfulandia.cl.logistica.service.RutaService;

@WebMvcTest(RutaControllerV2.class)
public class RutaControllerV2Test {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RutaAssembler rutaAssembler;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RutaService rutaService;
    @MockitoBean
    private GuiaDespachoRepository guiaDespachoRepository;
    @MockitoBean
    private EnvioRepository envioRepository;
    @MockitoBean
    private VehiculoDespachoRepository vehiculoDespachoRepository;
    @MockitoBean
    private OrdenFeignClient ordenFeignClient;

    private Ruta rutaMock = new Ruta(1, 25.02F, 30.02F, 35.02F, 40.02F, new ArrayList<>());
    private List<Ruta> rutasMock = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        // Rutas par la lista
        Ruta ruta1 = new Ruta(1, 25.02F, 30.02F, 35.02F, 40.02F, new ArrayList<>());
        Ruta ruta2 = new Ruta(2, 15.02F, 20.02F, 25.02F, 30.02F, new ArrayList<>());
        Ruta ruta3 = new Ruta(3, 5.02F, 10.02F, 15.02F, 20.02F, new ArrayList<>());

        // Envios simples
        Envio envio1 = new Envio();
        envio1.setIdEnvio(1);
        Envio envio2 = new Envio();
        envio2.setIdEnvio(2);
        Envio envio3 = new Envio();
        envio3.setIdEnvio(3);

        // Agregando envios a las rutas
        ruta1.setEnvios(List.of(envio1));
        ruta2.setEnvios(List.of(envio2));
        ruta3.setEnvios(List.of(envio3));

        // Agregando rutas a rutasMock
        rutasMock.add(ruta1);
        rutasMock.add(ruta2);
        rutasMock.add(ruta3);

    }

    @Test
    public void getRutasSuccesful() throws Exception {
        when(rutaService.getAllRutas()).thenReturn(rutasMock);

        mockMvc.perform(get("/api/v2/logistica/envios/rutas"))
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.rutaList", hasSize(rutasMock.size())))
                .andExpect(jsonPath("$._embedded.rutaList[0].idRuta", is(1)))
                .andExpect(jsonPath("$._embedded.rutaList[0].idRuta", is(1)))
                .andExpect(jsonPath("$._embedded.rutaList[0].coordXInicio", closeTo(25.02, 0.001)))
                .andExpect(jsonPath("$._embedded.rutaList[0].coordYInicio", closeTo(30.02, 0.001)))
                .andExpect(jsonPath("$._embedded.rutaList[0].coordXFinal", closeTo(35.02, 0.001)))
                .andExpect(jsonPath("$._embedded.rutaList[0].coordYFinal", closeTo(40.02, 0.001)));

        verify(rutaService, times(1)).getAllRutas();
    }

    @Test
    public void getRutasSuccesfulReturnsNoContent() throws Exception {
        rutasMock = new ArrayList<>();
        when(rutaService.getAllRutas()).thenReturn(rutasMock);

        mockMvc.perform(get("/api/v2/logistica/envios/rutas"))
                .andExpect(status().isNoContent());

        verify(rutaService, times(1)).getAllRutas();
    }

    @Test
    public void getRutasSuccesfulReturnsInternalServerError() throws Exception {
        when(rutaService.getAllRutas()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v2/logistica/envios/rutas"))
                .andExpect(status().isInternalServerError());

        verify(rutaService, times(1)).getAllRutas();
    }

}
