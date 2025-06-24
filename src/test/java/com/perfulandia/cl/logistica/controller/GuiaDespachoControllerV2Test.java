package com.perfulandia.cl.logistica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.cl.logistica.assemblers.GuiaDespachoAssembler;
import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.converter.GuiaDespachoConverter;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.RutaRepository;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;
import com.perfulandia.cl.logistica.service.GuiaDespachoService;

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
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GuiaDespachoControllerV2.class)
public class GuiaDespachoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GuiaDespachoAssembler guiaDespachoAssembler;
    @Autowired
    private ObjectMapper objectMapper;
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

    private GuiaDespacho guiaDespachoMock = new GuiaDespacho();
    private List<GuiaDespacho> guiasDespachoMock = new ArrayList<>();

    @BeforeEach

    public void setUp() {

        guiaDespachoMock.setIdDespacho(1);
        guiaDespachoMock.setIdEnvio(123);
        guiaDespachoMock.setIdOrden(456);
        guiaDespachoMock.setEnvios(new ArrayList<>()); // Initialize the list of envios

        GuiaDespacho guiaDespacho2 = new GuiaDespacho();
        guiaDespacho2.setIdDespacho(2);
        guiaDespacho2.setIdEnvio(789);
        guiaDespacho2.setIdOrden(101);
        guiaDespacho2.setEnvios(new ArrayList<>());

        guiasDespachoMock.add(guiaDespachoMock);
        guiasDespachoMock.add(guiaDespacho2);

    }

    @Test
    public void getGuiasDespachoSuccesful() throws Exception {

        when(guiaDespachoService.verGuiaDespachos()).thenReturn(guiasDespachoMock);

        mockMvc.perform(get("/api/v2/logistica/despachos"))
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.guiaDespachoList").exists())
                .andExpect(jsonPath("$._embedded.guiaDespachoList", hasSize(guiasDespachoMock.size())))
                .andExpect(jsonPath("$._embedded.guiaDespachoList[0].idDespacho",
                        is(guiasDespachoMock.get(0).getIdDespacho())))
                .andExpect(
                        jsonPath("$._embedded.guiaDespachoList[0].idEnvio", is(guiasDespachoMock.get(0).getIdEnvio())))
                .andExpect(
                        jsonPath("$._embedded.guiaDespachoList[0].idOrden", is(guiasDespachoMock.get(0).getIdOrden())))
                .andExpect(
                        jsonPath("$._embedded.guiaDespachoList[0].envios", is(guiasDespachoMock.get(0).getEnvios())));

        verify(guiaDespachoService, times(1)).verGuiaDespachos();

    }

    @Test
    public void getGuiasDespachoReturnsNoContent() throws Exception {
        List<GuiaDespacho> guiasDespachoMock = new ArrayList<>();

        when(guiaDespachoService.verGuiaDespachos()).thenReturn(guiasDespachoMock);

        mockMvc.perform(get("/api/v2/logistica/despachos"))
                .andExpect(status().isNoContent());

        verify(guiaDespachoService, times(1)).verGuiaDespachos();
    }

    @Test
    public void getGuiasDespachoReturnsInternalServerError() throws Exception {
        when(guiaDespachoService.verGuiaDespachos()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v2/logistica/despachos"))
                .andExpect(status().isInternalServerError());

        verify(guiaDespachoService, times(1)).verGuiaDespachos();
    }

    @Test
    public void getGuiaDespachoByIdSuccesful() throws Exception {
        Integer idExistente = 1;
        Optional<GuiaDespacho> guiaOpt = Optional.of(guiaDespachoMock);

        when(guiaDespachoService.obtenerGuiaDespachoPorId(idExistente)).thenReturn(guiaOpt);

        mockMvc.perform(get("/api/v2/logistica/despachos/{id}", idExistente))
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDespacho",
                        is(guiaDespachoMock.getIdDespacho())))
                .andExpect(
                        jsonPath("$.idEnvio", is(guiaDespachoMock.getIdEnvio())))
                .andExpect(
                        jsonPath("$.idOrden", is(guiaDespachoMock.getIdOrden())))
                .andExpect(
                        jsonPath("$.envios", is(guiaDespachoMock.getEnvios())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/api/v2/logistica/despachos/1")));
        verify(guiaDespachoService, times(1)).obtenerGuiaDespachoPorId(idExistente);

    }

    @Test
    public void getGuiaDespachoByIdReturnsNotFound() throws Exception {
        Integer idExistente = 1;

        when(guiaDespachoService.obtenerGuiaDespachoPorId(idExistente)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v2/logistica/despachos/{id}", idExistente))
                .andExpect(status().isNotFound());
        verify(guiaDespachoService, times(1)).obtenerGuiaDespachoPorId(idExistente);
    }

    @Test
    public void postGuiaDespachoReturnsCreated() throws Exception {
        String bodyRequest = objectMapper.writeValueAsString(guiaDespachoMock);
        when(guiaDespachoService.crearGuiaDespacho(guiaDespachoMock)).thenReturn(guiaDespachoMock);

        mockMvc.perform(post("/api/v2/logistica/despachos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.idDespacho",
                        is(guiaDespachoMock.getIdDespacho())))
                .andExpect(
                        jsonPath("$.idEnvio", is(guiaDespachoMock.getIdEnvio())))
                .andExpect(
                        jsonPath("$.idOrden", is(guiaDespachoMock.getIdOrden())))
                .andExpect(
                        jsonPath("$.envios", is(guiaDespachoMock.getEnvios())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/api/v2/logistica/despachos/1")));

        verify(guiaDespachoService, times(1)).crearGuiaDespacho(any(GuiaDespacho.class));
    }

    @Test
    public void postGuiaDespachoReturnsInternalServerError() throws Exception {
        String bodyRequest = objectMapper.writeValueAsString(guiaDespachoMock);
        when(guiaDespachoService.crearGuiaDespacho(guiaDespachoMock)).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/v2/logistica/despachos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isInternalServerError());

        verify(guiaDespachoService, times(1)).crearGuiaDespacho(any(GuiaDespacho.class));

    }

    @Test
    public void putGuiaDespachoSuccesful() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaPut = new GuiaDespacho();
        guiaPut.setIdEnvio(33);
        guiaPut.setIdOrden(22);
        String bodyRequest = objectMapper.writeValueAsString(guiaPut);
        GuiaDespacho guiaFinal = mockPut(guiaDespachoMock, guiaPut);

        when(guiaDespachoService.putGuiaDespacho(guiaPut, idExistente)).thenReturn(guiaFinal);

        mockMvc.perform(put("/api/v2/logistica/despachos/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.idDespacho").hasJsonPath())
                .andExpect(
                        jsonPath("$.idEnvio", is(guiaFinal.getIdEnvio())))
                .andExpect(
                        jsonPath("$.idOrden", is(guiaFinal.getIdOrden())))
                .andExpect(
                        jsonPath("$.envios", is(guiaFinal.getEnvios())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/api/v2/logistica/despachos/1")));

        verify(guiaDespachoService, times(1)).putGuiaDespacho(guiaPut, idExistente);
    }

    @Test
    public void putGuiaDespachoReturnsInternalServerError() throws Exception {
        Integer idRandom = 1;
        GuiaDespacho guiaPut = new GuiaDespacho();
        guiaPut.setIdEnvio(33);
        guiaPut.setIdOrden(22);
        String bodyRequest = objectMapper.writeValueAsString(guiaPut);
        when(guiaDespachoService.putGuiaDespacho(any(GuiaDespacho.class), eq(idRandom)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/v2/logistica/despachos/{id}", idRandom)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isInternalServerError());

        verify(guiaDespachoService, times(1)).putGuiaDespacho(any(GuiaDespacho.class), eq(idRandom));
    }

    @Test
    public void patchGuiaDespachoSuccesful() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaPatch = new GuiaDespacho();
        guiaPatch.setIdEnvio(33);
        guiaPatch.setIdOrden(22);
        String bodyRequest = objectMapper.writeValueAsString(guiaPatch);
        GuiaDespacho guiaFinal = mockPatch(guiaDespachoMock, guiaPatch);

        when(guiaDespachoService.parcharGuiaDespacho(guiaPatch, idExistente)).thenReturn(guiaFinal);

        mockMvc.perform(patch("/api/v2/logistica/despachos/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.idDespacho").hasJsonPath())
                .andExpect(
                        jsonPath("$.idEnvio", is(guiaFinal.getIdEnvio())))
                .andExpect(
                        jsonPath("$.idOrden", is(guiaFinal.getIdOrden())))
                .andExpect(
                        jsonPath("$.envios", is(guiaFinal.getEnvios())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/api/v2/logistica/despachos/1")));
    }

    @Test
    public void patchGuiaDespachoIdEnvioNullSuccesful() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaPatch = new GuiaDespacho();
        guiaPatch.setIdEnvio(null);
        guiaPatch.setIdOrden(22);
        String bodyRequest = objectMapper.writeValueAsString(guiaPatch);
        GuiaDespacho guiaFinal = mockPatch(guiaDespachoMock, guiaPatch);

        when(guiaDespachoService.parcharGuiaDespacho(guiaPatch, idExistente)).thenReturn(guiaFinal);

        mockMvc.perform(patch("/api/v2/logistica/despachos/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.idDespacho").hasJsonPath())
                .andExpect(
                        jsonPath("$.idEnvio", is(guiaFinal.getIdEnvio())))
                .andExpect(
                        jsonPath("$.idOrden", is(guiaFinal.getIdOrden())))
                .andExpect(
                        jsonPath("$.envios", is(guiaFinal.getEnvios())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/api/v2/logistica/despachos/1")));
    }

    @Test
    public void patchGuiaDespachoIdOrdenNullSuccesful() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaPatch = new GuiaDespacho();
        guiaPatch.setIdEnvio(33);
        guiaPatch.setIdOrden(null);
        String bodyRequest = objectMapper.writeValueAsString(guiaPatch);
        GuiaDespacho guiaFinal = mockPatch(guiaDespachoMock, guiaPatch);

        when(guiaDespachoService.parcharGuiaDespacho(guiaPatch, idExistente)).thenReturn(guiaFinal);

        mockMvc.perform(patch("/api/v2/logistica/despachos/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.idDespacho").hasJsonPath())
                .andExpect(
                        jsonPath("$.idEnvio", is(guiaFinal.getIdEnvio())))
                .andExpect(
                        jsonPath("$.idOrden", is(guiaFinal.getIdOrden())))
                .andExpect(
                        jsonPath("$.envios", is(guiaFinal.getEnvios())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/api/v2/logistica/despachos/1")));
    }

    @Test
    public void patchGuiaDespachoBothNullReturnsInternalServerError() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaPatch = new GuiaDespacho();
        guiaPatch.setIdEnvio(null);
        guiaPatch.setIdOrden(null);
        String bodyRequest = objectMapper.writeValueAsString(guiaPatch);
        GuiaDespacho guiaFinal = mockPatch(guiaDespachoMock, guiaPatch);

        when(guiaDespachoService.parcharGuiaDespacho(guiaPatch, idExistente)).thenThrow(new RuntimeException());

        mockMvc.perform(patch("/api/v2/logistica/despachos/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteGuiaDespachoSuccesful() throws Exception {
        Integer idExistente = 1;

        doNothing().when(guiaDespachoService).borrarGuiaDespacho(idExistente);

        mockMvc.perform(delete("/api/v2/logistica/despachos/{id}", idExistente))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteGuiaDespachoReturnsNotFound() throws Exception {
        Integer idExistente = 1;

        doThrow(new RuntimeException()).when(guiaDespachoService).borrarGuiaDespacho(idExistente);

        mockMvc.perform(delete("/api/v2/logistica/despachos/{id}", idExistente))
                .andExpect(status().isNotFound());
    }

    // metodo que simula un put

    public GuiaDespacho mockPut(GuiaDespacho guiaExistente, GuiaDespacho guiaPut) {
        GuiaDespacho guiaActualizada = guiaExistente;
        guiaActualizada.setIdEnvio(guiaPut.getIdEnvio());
        guiaActualizada.setIdOrden(guiaPut.getIdOrden());

        return guiaActualizada;
    }

    // metodo que simula un patch

    public GuiaDespacho mockPatch(GuiaDespacho guiaExistente, GuiaDespacho guiaPatch) {
        GuiaDespacho guiaActualizada = guiaExistente;
        if (guiaPatch.getIdEnvio() != null) {
            guiaActualizada.setIdEnvio(guiaPatch.getIdEnvio());
        }
        if (guiaPatch.getIdOrden() != null) {
            guiaActualizada.setIdOrden(guiaPatch.getIdOrden());
        }

        return guiaActualizada;

    }

}
