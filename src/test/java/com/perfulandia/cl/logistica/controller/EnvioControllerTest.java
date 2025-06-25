package com.perfulandia.cl.logistica.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.RutaRepository;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;
import com.perfulandia.cl.logistica.service.EnvioService;

@WebMvcTest(EnvioController.class)
public class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    EnvioService envioService;

    @MockitoBean
    private GuiaDespachoRepository guiaDespachoRepository;

    @MockitoBean
    private RutaRepository rutaRepository;

    @MockitoBean
    private OrdenFeignClient ordenFeignClient;

    @MockitoBean
    private VehiculoDespachoRepository vehiculoDespachoRepository;

    private Envio envioMock;
    private List<Envio> enviosMock;

    @BeforeEach
    void setUp() {
        GuiaDespacho guiaDespacho = new GuiaDespacho();
        guiaDespacho.setIdDespacho(1);

        VehiculoDespacho vehiculoDespacho = new VehiculoDespacho();
        vehiculoDespacho.setIdVehiculoDespacho(1);

        Ruta ruta = new Ruta();
        ruta.setIdRuta(1);

        envioMock = new Envio(1, 101, 1, LocalDate.of(2025, 6, 25), false, "Observacion de prueba", guiaDespacho, vehiculoDespacho, ruta);

        enviosMock = new ArrayList<>();
        enviosMock.add(envioMock);
    }

    @Test
    void getEnvios_Success() throws Exception {
        when(envioService.obtenerEnvios()).thenReturn(enviosMock);

        mockMvc.perform(get("/api/v1/logistica/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idEnvio", is(1)))
                .andExpect(jsonPath("$[0].observacion", is("Observacion de prueba")));

        verify(envioService, times(1)).obtenerEnvios();
    }

    @Test
    void getEnvios_NoContent() throws Exception {
        when(envioService.obtenerEnvios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/logistica/envios"))
                .andExpect(status().isNoContent());

        verify(envioService, times(1)).obtenerEnvios();
    }

    @Test
    void getEnvioPorId_Success() throws Exception {
        when(envioService.obtenerEnvioPorId(1)).thenReturn(Optional.of(envioMock));

        mockMvc.perform(get("/api/v1/logistica/envios/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnvio", is(1)))
                .andExpect(jsonPath("$.idCliente", is(101)));

        verify(envioService, times(1)).obtenerEnvioPorId(1);
    }

    @Test
    void getEnvioPorId_NotFound() throws Exception {
        when(envioService.obtenerEnvioPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/logistica/envios/{id}", 99))
                .andExpect(status().isNotFound());

        verify(envioService, times(1)).obtenerEnvioPorId(99);
    }

    @Test
    void crearEnvio_Created() throws Exception {
        when(envioService.crearEnvio(any(Envio.class))).thenReturn(envioMock);

        mockMvc.perform(post("/api/v1/logistica/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envioMock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEnvio", is(1)));

        verify(envioService, times(1)).crearEnvio(any(Envio.class));
    }

    @Test
    void crearEnvio_InternalServerError() throws Exception {
        when(envioService.crearEnvio(any(Envio.class))).thenThrow(new RuntimeException("Error en la base de datos"));

        mockMvc.perform(post("/api/v1/logistica/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envioMock)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void actualizarEnvio_Success() throws Exception {
        Envio envioActualizado = new Envio();
        envioActualizado.setIdEnvio(1);
        envioActualizado.setObservacion("Observacion actualizada");

        when(envioService.actualizarEnvio(eq(1), any(Envio.class))).thenReturn(envioActualizado);

        mockMvc.perform(put("/api/v1/logistica/envios/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observacion", is("Observacion actualizada")));

        verify(envioService, times(1)).actualizarEnvio(eq(1), any(Envio.class));
    }

    @Test
    void actualizarEnvio_NotFound() throws Exception {
        when(envioService.actualizarEnvio(eq(99), any(Envio.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/logistica/envios/{id}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Envio())))
                .andExpect(status().isNotFound());
    }

    @Test
    void parcharEnvio_Success() throws Exception {
        Envio envioParchado = new Envio();
        envioParchado.setObservacion("Envio parchado");

        when(envioService.parcharEnvio(eq(1), any(Envio.class))).thenReturn(envioParchado);

        mockMvc.perform(patch("/api/v1/logistica/envios/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"observacion\":\"Envio parchado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observacion", is("Envio parchado")));
    }

    @Test
    void parcharEnvio_BadRequest() throws Exception {
        when(envioService.parcharEnvio(eq(1), any(Envio.class))).thenThrow(new RuntimeException("Error de parcheo"));

        mockMvc.perform(patch("/api/v1/logistica/envios/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"observacion\":\"Envio parchado\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarEnvio_NoContent() throws Exception {
        doNothing().when(envioService).eliminarEnvio(1);

        mockMvc.perform(delete("/api/v1/logistica/envios/{id}", 1))
                .andExpect(status().isNoContent());

        verify(envioService, times(1)).eliminarEnvio(1);
    }

    @Test
    void eliminarEnvio_NotFound() throws Exception {
        doThrow(new RuntimeException("No existe envio con esa id")).when(envioService).eliminarEnvio(99);

        mockMvc.perform(delete("/api/v1/logistica/envios/{id}", 99))
                .andExpect(status().isNotFound());

        verify(envioService, times(1)).eliminarEnvio(99);
    }
}
