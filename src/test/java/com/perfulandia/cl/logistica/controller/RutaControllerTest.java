package com.perfulandia.cl.logistica.controller;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.service.RutaService;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;
import com.perfulandia.cl.logistica.client.OrdenFeignClient;


@WebMvcTest(RutaController.class)
public class RutaControllerTest {

    @Autowired
    private MockMvc mockMvc;
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

    private Ruta rutaMock;
    private List<Ruta> rutasMock;

    @BeforeEach
    void setUp() {
        rutaMock = new Ruta(1, 10.5f, 20.5f, 30.5f, 40.5f, new ArrayList<>());
        rutasMock = new ArrayList<>();
        rutasMock.add(rutaMock);
    }

    @Test
    void getAllRutas_Success() throws Exception {
        when(rutaService.getAllRutas()).thenReturn(rutasMock);

        mockMvc.perform(get("/api/v1/logistica/envios/rutas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idRuta", is(1)));

        verify(rutaService, times(1)).getAllRutas();
    }

    @Test
    void getAllRutas_NoContent() throws Exception {
        when(rutaService.getAllRutas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/logistica/envios/rutas"))
                .andExpect(status().isNoContent());

        verify(rutaService, times(1)).getAllRutas();
    }

    @Test
    void getAllRutas_InternalServerError() throws Exception{
        when(rutaService.getAllRutas()).thenThrow(new RuntimeException());

         mockMvc.perform(get("/api/v1/logistica/envios/rutas"))
                .andExpect(status().isInternalServerError());

        verify(rutaService, times(1)).getAllRutas();
    }

    @Test
    void buscarRutasPorCoordenadas_Success() throws Exception {
        when(rutaService.buscarRutasPorCoordenadas(25.0F, 25.0F, 25.0F,25.0F)).thenReturn(rutasMock);

        mockMvc.perform(get("/api/v1/logistica/envios/rutas/{x_1}/{x_2}/{y_1}/{y_2}"
                ,25.0F,25.0F,25.0F,25.0F))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].coordXInicio", closeTo(10.5, 0.001)));

        verify(rutaService, times(1)).buscarRutasPorCoordenadas(25.0F, 25.0F, 25.0F,25.0F);
    }

    @Test
    void buscarRutasPorCoordenadas_NoContent() throws Exception {
        List<Ruta> rutasEncontradas = new ArrayList<>();
        when(rutaService.buscarRutasPorCoordenadas(25.0F, 25.0F, 25.0F,25.0F)).thenReturn(rutasEncontradas);

        mockMvc.perform(get("/api/v1/logistica/envios/rutas/{x_1}/{x_2}/{y_1}/{y_2}"
                ,25.0F,25.0F,25.0F,25.0F))
                .andExpect(status().isNoContent());
        verify(rutaService, times(1)).buscarRutasPorCoordenadas(25.0F, 25.0F, 25.0F,25.0F);
    }

    @Test
    void buscarRutasPorCoordenadas_InternalServerError() throws Exception {
        when(rutaService.buscarRutasPorCoordenadas(25.0F, 25.0F, 25.0F,25.0F)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/logistica/envios/rutas/{x_1}/{x_2}/{y_1}/{y_2}"
                ,25.0F,25.0F,25.0F,25.0F))
                .andExpect(status().isInternalServerError());
                
        verify(rutaService, times(1)).buscarRutasPorCoordenadas(25.0F, 25.0F, 25.0F,25.0F);
    }


    @Test
    void crearRuta_Created() throws Exception {
        when(rutaService.crearRuta(any(Ruta.class))).thenReturn(rutaMock);

        mockMvc.perform(post("/api/v1/logistica/envios/rutas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rutaMock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idRuta", is(1)));

        verify(rutaService, times(1)).crearRuta(any(Ruta.class));
    }

    @Test
    void putRuta_Success() throws Exception {
        when(rutaService.putRuta(any(Ruta.class), eq(1))).thenReturn(rutaMock);

        mockMvc.perform(put("/api/v1/logistica/envios/rutas/{idRuta}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rutaMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRuta", is(1)));

        verify(rutaService, times(1)).putRuta(any(Ruta.class), eq(1));
    }

    @Test
    void putRuta_NotFound() throws Exception {
        when(rutaService.putRuta(any(Ruta.class), eq(99))).thenThrow(new RuntimeException("No existe la ruta"));

        mockMvc.perform(put("/api/v1/logistica/envios/rutas/{idRuta}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rutaMock)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void parcharRuta_Success() throws Exception {
        when(rutaService.parcharRuta(any(Ruta.class), eq(1))).thenReturn(rutaMock);

        mockMvc.perform(patch("/api/v1/logistica/envios/rutas/{idRuta}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"coordXFinal\": 35.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRuta", is(1)));

        verify(rutaService, times(1)).parcharRuta(any(Ruta.class), eq(1));
    }

    @Test
    void parcharRuta_NotFound() throws Exception {
        when(rutaService.parcharRuta(any(Ruta.class), eq(99))).thenThrow(new RuntimeException("No existe la ruta"));

        mockMvc.perform(patch("/api/v1/logistica/envios/rutas/{idRuta}", 99)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"coordXFinal\": 35.5}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteRuta_NoContent() throws Exception {
        doNothing().when(rutaService).deleteRuta(1);

        mockMvc.perform(delete("/api/v1/logistica/envios/rutas/{idRuta}", 1))
                .andExpect(status().isNoContent());

        verify(rutaService, times(1)).deleteRuta(1);
    }

    @Test
    void deleteRuta_NotFound() throws Exception {
        doThrow(new RuntimeException("No existe la ruta")).when(rutaService).deleteRuta(99);

        mockMvc.perform(delete("/api/v1/logistica/envios/rutas/{idRuta}", 99))
                .andExpect(status().isInternalServerError());

        verify(rutaService, times(1)).deleteRuta(99);
    }
}
