package com.perfulandia.cl.logistica.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.service.VehiculoDespachoService;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.RutaRepository;
import com.perfulandia.cl.logistica.client.OrdenFeignClient;

@WebMvcTest(VehiculoDespachoController.class)
public class VehiculoDespachoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private VehiculoDespachoService vehiculoDespachoService;

    @Autowired
    private ObjectMapper objectMapper; // Para transformar objetos a JSON

    // Necesitamos moqckear todos los repostiries ya que obtener envios activa al
    // lazy loading de todos...
    @MockitoBean
    private EnvioRepository envioRepository;

    @MockitoBean
    private GuiaDespachoRepository guiaDespachoRepository;

    @MockitoBean
    private RutaRepository rutaRepository;

    @MockitoBean
    private OrdenFeignClient ordenFeignClient;

    private VehiculoDespacho vehiculoMock;
    private List<VehiculoDespacho> vehiculosMock;

    @BeforeEach
    void setUp() {
        vehiculoMock = new VehiculoDespacho(1, "AA-11", 2025, new ArrayList<>());
        vehiculosMock = new ArrayList<>();
        vehiculosMock.add(vehiculoMock);
    }

    @Test
    void getVehiculosDespacho_Success() throws Exception {
        when(vehiculoDespachoService.verVehiculosDespachos()).thenReturn(vehiculosMock);

        mockMvc.perform(get("/api/v1/logistica/vehiculos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patente", is("AA-11")));

        verify(vehiculoDespachoService, times(1)).verVehiculosDespachos();
    }

    @Test
    void getVehiculosDespacho_NoContent() throws Exception {
        when(vehiculoDespachoService.verVehiculosDespachos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/logistica/vehiculos"))
                .andExpect(status().isNoContent());

        verify(vehiculoDespachoService, times(1)).verVehiculosDespachos();
    }

    @Test
    void buscarPorPatronPatente_Success() throws Exception {
        String patron = "AA";
        when(vehiculoDespachoService.buscarVehiculoPorPatronPatente(patron)).thenReturn(vehiculosMock);

        mockMvc.perform(get("/api/v1/logistica/vehiculos/{patron_patente}", patron))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patente", is("AA-11")));

        verify(vehiculoDespachoService, times(1)).buscarVehiculoPorPatronPatente(patron);
    }

    @Test
    void buscarPorPatronPatente_BadRequest() throws Exception {
        String patronInvalido = "A";
        when(vehiculoDespachoService.buscarVehiculoPorPatronPatente(patronInvalido))
                .thenThrow(new RuntimeException("Se deben de colocar dos valores"));

        mockMvc.perform(get("/api/v1/logistica/vehiculos/{patron_patente}", patronInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createVehiculoDespacho_Created() throws Exception {
        when(vehiculoDespachoService.registrarVehiculoDespacho(any(VehiculoDespacho.class))).thenReturn(vehiculoMock);

        mockMvc.perform(post("/api/v1/logistica/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculoMock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patente", is("AA-11")));

        verify(vehiculoDespachoService, times(1)).registrarVehiculoDespacho(any(VehiculoDespacho.class));
    }

    @Test
    void createVehiculoDespacho_Conflict() throws Exception {
        when(vehiculoDespachoService.registrarVehiculoDespacho(any(VehiculoDespacho.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/logistica/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculoMock)))
                .andExpect(status().isConflict());
    }

    @Test
    void actualizarVehiculoDespacho_Success() throws Exception {
        when(vehiculoDespachoService.actualizarVehiculoDespacho(any(VehiculoDespacho.class), eq("AA-11")))
                .thenReturn(vehiculoMock);

        mockMvc.perform(put("/api/v1/logistica/vehiculos/{patente}", "AA-11")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculoMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patente", is("AA-11")));
    }

    @Test
    void actualizarVehiculoDespacho_NotFound() throws Exception {
        when(vehiculoDespachoService.actualizarVehiculoDespacho(any(VehiculoDespacho.class), anyString()))
                .thenReturn(null);

        mockMvc.perform(put("/api/v1/logistica/vehiculos/{patente}", "XX-99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehiculoMock)))
                .andExpect(status().isNotFound());
    }

    @Test
    void parcharVehiculoDespacho_Success() throws Exception {
        when(vehiculoDespachoService.parcharVehiculoDespacho(any(VehiculoDespacho.class), eq("AA-11")))
                .thenReturn(vehiculoMock);

        mockMvc.perform(patch("/api/v1/logistica/vehiculos/{patente}", "AA-11")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"ano\": 2026}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patente", is("AA-11")));
    }

    @Test
    void parcharVehiculoDespacho_BadRequest() throws Exception {
        when(vehiculoDespachoService.parcharVehiculoDespacho(any(VehiculoDespacho.class), eq("XX-99")))
                .thenThrow(new RuntimeException("Vehiculo no existe"));

        mockMvc.perform(patch("/api/v1/logistica/vehiculos/{patente}", "XX-99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"ano\": 2026}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void borrarVehiculoDespacho_NoContent() throws Exception {
        doNothing().when(vehiculoDespachoService).borrarVehiculoDespacho("AA-11");

        mockMvc.perform(delete("/api/v1/logistica/vehiculos/{patente}", "AA-11"))
                .andExpect(status().isNoContent());

        verify(vehiculoDespachoService, times(1)).borrarVehiculoDespacho("AA-11");
    }

    @Test
    void borrarVehiculoDespacho_BadRequest() throws Exception {
        doThrow(new RuntimeException("Vehiculo no existe")).when(vehiculoDespachoService)
                .borrarVehiculoDespacho("XX-99");

        mockMvc.perform(delete("/api/v1/logistica/vehiculos/{patente}", "XX-99"))
                .andExpect(status().isBadRequest());

        verify(vehiculoDespachoService, times(1)).borrarVehiculoDespacho("XX-99");
    }
}
