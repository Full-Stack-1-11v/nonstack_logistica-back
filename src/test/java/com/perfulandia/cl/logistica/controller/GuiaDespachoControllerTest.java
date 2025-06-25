package com.perfulandia.cl.logistica.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.dto.OrdenDTO;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.RutaRepository;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;
import com.perfulandia.cl.logistica.service.GuiaDespachoService;
import com.perfulandia.cl.logistica.service.OrdenDTOService;

@WebMvcTest(GuiaDespachoController.class)
public class GuiaDespachoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OrdenDTOService ordenDTOService;
    @MockitoBean
    private GuiaDespachoService guiaDespachoService;
    @MockitoBean
    private GuiaDespachoRepository guiaDespachoRepository;
    @MockitoBean
    private EnvioRepository envioRepository;
    @MockitoBean
    private RutaRepository rutaRepository;
    @MockitoBean
    private VehiculoDespachoRepository vehiculoDespachoRepository;
    @MockitoBean
    private OrdenFeignClient ordenFeignClient;

    private GuiaDespacho guiaDespachoMock = new GuiaDespacho();;
    private List<GuiaDespacho> guiasDespachoMock;

    @BeforeEach
    public void setUp() {

        guiaDespachoMock.setIdDespacho(1);
        guiaDespachoMock.setIdEnvio(100);
        guiaDespachoMock.setIdOrden(200);
        guiaDespachoMock.setEnvios(new ArrayList<>());

        guiasDespachoMock = new ArrayList<>();
        guiasDespachoMock.add(guiaDespachoMock);
    }

    @Test
    void getDespachos_Success() throws Exception {
        when(guiaDespachoService.verGuiaDespachos()).thenReturn(guiasDespachoMock);
        when(ordenFeignClient.obtenerOrdenPorId(anyInt())).thenReturn(new OrdenDTO());

        mockMvc.perform(get("/api/v1/logistica/despachos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idDespacho", is(1)));

        verify(guiaDespachoService, times(1)).verGuiaDespachos();
    }

    @Test
    void getDespachos_NoContent() throws Exception {
        when(guiaDespachoService.verGuiaDespachos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/logistica/despachos"))
                .andExpect(status().isNoContent());

        verify(guiaDespachoService, times(1)).verGuiaDespachos();
    }

    @Test
    void getDespachos_InternalServerError() throws Exception {
        when(guiaDespachoService.verGuiaDespachos()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/logistica/despachos"))
                .andExpect(status().isInternalServerError());

        verify(guiaDespachoService, times(1)).verGuiaDespachos();
    }

    @Test
    void getGuiaDespachoByIdSuccessful() throws Exception {
        when(guiaDespachoService.obtenerGuiaDespachoPorId(1)).thenReturn(Optional.of(guiaDespachoMock));

        mockMvc.perform(get("/api/v1/logistica/despachos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idDespacho", is(1)))
                .andExpect(jsonPath("$.idOrden", is(200)));

        verify(guiaDespachoService, times(1)).obtenerGuiaDespachoPorId(1);
    }

    @Test
    void getGuiaDespachoById_NotFound() throws Exception {
        when(guiaDespachoService.obtenerGuiaDespachoPorId(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/logistica/despachos/{id}", 99))
                .andExpect(status().isNotFound());

        verify(guiaDespachoService, times(1)).obtenerGuiaDespachoPorId(99);
    }

    @Test
    void createDespacho_Created() throws Exception {
        when(guiaDespachoService.crearGuiaDespacho(any(GuiaDespacho.class))).thenReturn(guiaDespachoMock);

        mockMvc.perform(post("/api/v1/logistica/despachos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guiaDespachoMock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDespacho", is(1)));

        verify(guiaDespachoService, times(1)).crearGuiaDespacho(any(GuiaDespacho.class));
    }

    @Test
    void createDespacho_InternalServerError() throws Exception {
        when(guiaDespachoService.crearGuiaDespacho(any(GuiaDespacho.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/v1/logistica/despachos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guiaDespachoMock)))
                .andExpect(status().isInternalServerError());

        verify(guiaDespachoService, times(1)).crearGuiaDespacho(any(GuiaDespacho.class));
    }

    @Test
    void putGuiaDespacho_Success() throws Exception {
        when(guiaDespachoService.putGuiaDespacho(any(GuiaDespacho.class), eq(1))).thenReturn(guiaDespachoMock);

        mockMvc.perform(put("/api/v1/logistica/despachos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guiaDespachoMock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDespacho", is(1)));

        verify(guiaDespachoService, times(1)).putGuiaDespacho(any(GuiaDespacho.class), eq(1));
    }

        @Test
    void putGuiaDespacho_InternalServerError() throws Exception {
        when(guiaDespachoService.putGuiaDespacho(any(GuiaDespacho.class), eq(1))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/v1/logistica/despachos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guiaDespachoMock)))
                .andExpect(status().isInternalServerError());

        verify(guiaDespachoService, times(1)).putGuiaDespacho(any(GuiaDespacho.class), eq(1));
    }

    @Test
    void patchGuiaDespacho_Success() throws Exception {
        when(guiaDespachoService.parcharGuiaDespacho(any(GuiaDespacho.class), eq(1))).thenReturn(guiaDespachoMock);

        mockMvc.perform(patch("/api/v1/logistica/despachos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idEnvio\": 101}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDespacho", is(1)));

        verify(guiaDespachoService, times(1)).parcharGuiaDespacho(any(GuiaDespacho.class), eq(1));
    }

    @Test
    void patchGuiaDespacho_InternalServerError() throws Exception {
        when(guiaDespachoService.parcharGuiaDespacho(any(GuiaDespacho.class), eq(1))).thenThrow(new RuntimeException());

        mockMvc.perform(patch("/api/v1/logistica/despachos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idEnvio\": 101}"))
                .andExpect(status().isInternalServerError());

        verify(guiaDespachoService, times(1)).parcharGuiaDespacho(any(GuiaDespacho.class), eq(1));
    }

    @Test
    void deleteGuiaDespacho_NoContent() throws Exception {
        doNothing().when(guiaDespachoService).borrarGuiaDespacho(1);

        mockMvc.perform(delete("/api/v1/logistica/despachos/{id}", 1))
                .andExpect(status().isNoContent());

        verify(guiaDespachoService, times(1)).borrarGuiaDespacho(1);
    }

    @Test
    void deleteGuiaDespacho_NotFound() throws Exception {
        doThrow(new RuntimeException("No existe la guia")).when(guiaDespachoService).borrarGuiaDespacho(99);

        mockMvc.perform(delete("/api/v1/logistica/despachos/{id}", 99))
                .andExpect(status().isNotFound());

        verify(guiaDespachoService, times(1)).borrarGuiaDespacho(99);
    }
}
