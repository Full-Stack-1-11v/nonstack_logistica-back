package com.perfulandia.cl.logistica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.converter.EnvioConverter;
import com.perfulandia.cl.logistica.dto.EnvioDTO;
import com.perfulandia.cl.logistica.dto.OrdenDTO;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.RutaRepository;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;
import com.perfulandia.cl.logistica.service.EnvioService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.Media;

@WebMvcTest(EnvioControllerV2.class)
public class EnvioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper; // Para transformar objetos a JSON

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

    private List<Envio> listEnviosMock = new ArrayList<>();
    private OrdenDTO ordenDTOMock = new OrdenDTO();
    private GuiaDespacho guiaDespacho = new GuiaDespacho();
    private VehiculoDespacho vehiculoDespacho = new VehiculoDespacho();
    Ruta ruta = new Ruta();

    @BeforeEach
    public void setUp() {

        // GuiaDespacho mock
        guiaDespacho.setIdDespacho(1);
        // VehiculoDespacho mock

        vehiculoDespacho.setIdVehiculoDespacho(1);
        // Ruta mock
        ruta.setIdRuta(1);
        // Envios
        Envio envio1 = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho,
                ruta);
        listEnviosMock.add(envio1);

        ordenDTOMock.setIdCliente(1);
        ordenDTOMock.setIdOrden(1);
        ordenDTOMock.setIdProducto(1);

    }

    @Test
    public void getEnviosSuccesful() throws Exception {
        when(rutaRepository.findById(any())).thenReturn(Optional.of(ruta));
        when(ordenFeignClient.obtenerOrdenPorId(anyInt())).thenReturn(ordenDTOMock);
        when(guiaDespachoRepository.findById(anyInt())).thenReturn(Optional.of(guiaDespacho));
        when(vehiculoDespachoRepository.findById(any())).thenReturn(Optional.of(vehiculoDespacho));
        when(envioService.obtenerEnvios()).thenReturn(listEnviosMock);

        mockMvc.perform(get("/api/v2/logistica/envios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json")) // Por que es HATEOAS
                .andExpect(jsonPath("$._embedded.envioDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].idEnvio", is(1)))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].idCliente", is(101)))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].fechaEntrega", is("2025-02-02")))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].entregado", is(false)))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].observacion", is("Observacion 1")))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].guiaDespachoId", is(1)))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].vehiculoDespachoId", is(1)))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].rutaId", is(1)));

        verify(envioService, times(1)).obtenerEnvios();

    }

    @Test
    public void getEnviosIsEmpty() throws Exception {
        listEnviosMock = new ArrayList<>();
        when(rutaRepository.findById(any())).thenReturn(Optional.of(ruta));
        when(ordenFeignClient.obtenerOrdenPorId(anyInt())).thenReturn(ordenDTOMock);
        when(guiaDespachoRepository.findById(anyInt())).thenReturn(Optional.of(guiaDespacho));
        when(vehiculoDespachoRepository.findById(any())).thenReturn(Optional.of(vehiculoDespacho));
        when(envioService.obtenerEnvios()).thenReturn(listEnviosMock);

        mockMvc.perform(get("/api/v2/logistica/envios"))
                .andExpect(status().isNoContent());
        verify(envioService, times(1)).obtenerEnvios();

    }

    @Test
    public void getEnviosByIdSuccesful() throws Exception {
        Integer idExistenteEnvio = 1;
        Envio envio1 = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho,
                ruta);
        Optional<Envio> envioOptional = Optional.of(envio1);
        when(rutaRepository.findById(any())).thenReturn(Optional.of(ruta));
        when(ordenFeignClient.obtenerOrdenPorId(anyInt())).thenReturn(ordenDTOMock);
        when(guiaDespachoRepository.findById(anyInt())).thenReturn(Optional.of(guiaDespacho));
        when(vehiculoDespachoRepository.findById(any())).thenReturn(Optional.of(vehiculoDespacho));
        when(envioService.obtenerEnvioPorId(idExistenteEnvio)).thenReturn(envioOptional);

        mockMvc.perform(get("/api/v2/logistica/envios/{id}", idExistenteEnvio))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json")) // Por que es HATEOAS
                .andExpect(jsonPath("$.idEnvio", is(1)))
                .andExpect(jsonPath("$.idCliente", is(101)))
                .andExpect(jsonPath("$.fechaEntrega", is("2025-02-02")))
                .andExpect(jsonPath("$.entregado", is(false)))
                .andExpect(jsonPath("$.observacion", is("Observacion 1")))
                .andExpect(jsonPath("$.guiaDespachoId", is(1)))
                .andExpect(jsonPath("$.vehiculoDespachoId", is(1)))
                .andExpect(jsonPath("$.rutaId", is(1)));
        verify(envioService, times(1)).obtenerEnvioPorId(idExistenteEnvio);

    }

    @Test
    public void getEnviosById_NoResult() throws Exception {
        Integer idNoExistenteEnvio = 2;
        Optional<Envio> envioOptional = Optional.empty();
        when(rutaRepository.findById(any())).thenReturn(Optional.of(ruta));
        when(ordenFeignClient.obtenerOrdenPorId(anyInt())).thenReturn(ordenDTOMock);
        when(guiaDespachoRepository.findById(anyInt())).thenReturn(Optional.of(guiaDespacho));
        when(vehiculoDespachoRepository.findById(any())).thenReturn(Optional.of(vehiculoDespacho));
        when(envioService.obtenerEnvioPorId(idNoExistenteEnvio)).thenReturn(envioOptional);
        mockMvc.perform(get("/api/v2/logistica/envios/{id}", idNoExistenteEnvio))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getEnviosFechasuccesful() throws Exception {
        LocalDate fechaInicialMock = LocalDate.of(2025, 1, 1);
        LocalDate fechaFinalMock = LocalDate.of(2025, 12, 31);
        when(rutaRepository.findById(any())).thenReturn(Optional.of(ruta));
        when(ordenFeignClient.obtenerOrdenPorId(anyInt())).thenReturn(ordenDTOMock);
        when(guiaDespachoRepository.findById(anyInt())).thenReturn(Optional.of(guiaDespacho));
        when(vehiculoDespachoRepository.findById(any())).thenReturn(Optional.of(vehiculoDespacho));
        when(envioService.buscarEnvioPorRangoDeFecha(fechaInicialMock, fechaFinalMock)).thenReturn(listEnviosMock);

        mockMvc.perform(get("/api/v2/logistica/envios/buscar-por-fecha/{fechaInicio}/{fechaFin}", fechaInicialMock,
                fechaFinalMock)
                .contentType("application/hal+json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.envioDTOList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.envioDTOList[0].idEnvio", is(1)));

        verify(envioService, times(1)).buscarEnvioPorRangoDeFecha(fechaInicialMock, fechaFinalMock);

    }

    @Test
    public void getEnviosReturnsNoContent() throws Exception {
        LocalDate fechaInicialMock = LocalDate.of(2024, 1, 1);
        LocalDate fechaFinalMock = LocalDate.of(2024, 12, 31);
        List<Envio> enviosVacio = new ArrayList<>();
        when(rutaRepository.findById(any())).thenReturn(Optional.of(ruta));
        when(ordenFeignClient.obtenerOrdenPorId(anyInt())).thenReturn(ordenDTOMock);
        when(guiaDespachoRepository.findById(anyInt())).thenReturn(Optional.of(guiaDespacho));
        when(vehiculoDespachoRepository.findById(any())).thenReturn(Optional.of(vehiculoDespacho));
        when(envioService.buscarEnvioPorRangoDeFecha(fechaInicialMock, fechaFinalMock)).thenReturn(enviosVacio);

        mockMvc.perform(get("/api/v2/logistica/envios/buscar-por-fecha/{fechaInicio}/{fechaFin}", fechaInicialMock,
                fechaFinalMock)
                .contentType("application/hal+json"))
                .andExpect(status().isNoContent());

        verify(envioService, times(1)).buscarEnvioPorRangoDeFecha(fechaInicialMock, fechaFinalMock);

    }

    @Test
    public void postEnvioReturnsCreated() throws Exception {

        Envio envio1 = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        // Hay que convertir el objeto en un JSON!
        String requestBody = objectMapper.writeValueAsString(envio1);
        when(envioService.crearEnvio(envio1)).thenReturn(envio1);

        mockMvc.perform(post("/api/v2/logistica/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

        verify(envioService, times(1)).crearEnvio(envio1);
    }

    @Test
    public void postEnvioReturnsInternalServerError() throws Exception {

        Envio envio1 = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        // Hay que convertir el objeto en un JSON!
        String requestBody = objectMapper.writeValueAsString(envio1);
        when(envioService.crearEnvio(envio1)).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(post("/api/v2/logistica/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void putEnvioSuccessful() throws Exception {
        Integer idExistente = 1;
        Envio envioPut = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        Envio envioExistente = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        Envio envioActualizado = putMock(envioExistente, envioPut);

        String requestBody = objectMapper.writeValueAsString(envioPut);

        EnvioDTO envioActualizadoDTO = EnvioConverter.convertToDTO(envioActualizado);

        when(envioService.actualizarEnvio(idExistente, envioPut)).thenReturn(envioActualizado);

        mockMvc.perform(put("/api/v2/logistica/envios/{id}", idExistente)
                .contentType("application/hal+json")
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnvio", is(envioActualizadoDTO.getIdEnvio())))
                .andExpect(jsonPath("$.idCliente", is(envioActualizadoDTO.getIdCliente())))
                .andExpect(jsonPath("$.idOrden", is(envioActualizadoDTO.getIdOrden())))
                .andExpect(jsonPath("$.fechaEntrega", is(envioActualizadoDTO.getFechaEntrega().toString())))
                .andExpect(jsonPath("$.entregado", is(envioActualizadoDTO.getEntregado())))
                .andExpect(jsonPath("$.observacion", is(envioActualizadoDTO.getObservacion())))
                .andExpect(jsonPath("$.guiaDespachoId", is(envioActualizadoDTO.getGuiaDespachoId())))
                .andExpect(jsonPath("$.vehiculoDespachoId", is(envioActualizadoDTO.getVehiculoDespachoId())))
                .andExpect(jsonPath("$.rutaId", is(envioActualizadoDTO.getRutaId())));

    }

    @Test
    public void putEnvioReturnsNotFound() throws Exception {
        Integer idNoExistente = 111;
        Envio envioPut = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        String requestBody = objectMapper.writeValueAsString(envioPut);
        when(envioService.actualizarEnvio(idNoExistente, envioPut)).thenThrow(RuntimeException.class);

        mockMvc.perform(put("/api/v2/logistica/envios/{id}", idNoExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());

        verify(envioService, times(1)).actualizarEnvio(idNoExistente, envioPut);

    }

    @Test
    public void putEnvioReturnsNotFoundDos() throws Exception {
        Integer idNoExistente = 111;
        Envio envioPut = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        String requestBody = objectMapper.writeValueAsString(envioPut);
        when(envioService.actualizarEnvio(idNoExistente, envioPut)).thenReturn(null);

        mockMvc.perform(put("/api/v2/logistica/envios/{id}", idNoExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());

        verify(envioService, times(1)).actualizarEnvio(idNoExistente, envioPut);

    }

    @Test
    public void patchEnvioSuccesful() throws Exception {
        Integer idExistente = 1;
        Envio envioPatch = new Envio(1, null, null, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        Envio envioExistente = new Envio(1, 101, 1, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);
        Envio envioActualizado = patchMock(envioExistente, envioPatch);

        String requestBody = objectMapper.writeValueAsString(envioPatch);

        EnvioDTO envioActualizadoDTO = EnvioConverter.convertToDTO(envioActualizado);
        EntityModel<EnvioDTO> envioEntity = EntityModel.of(envioActualizadoDTO);

        when(envioService.parcharEnvio(idExistente, envioPatch)).thenReturn(envioActualizado);

        mockMvc.perform(patch("/api/v2/logistica/envios/{id}", idExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"));

    }

    @Test
    public void patchEnvioReturnsBadRequest() throws Exception {
        Integer idNoExistente = 222;
        Envio envioPatch = new Envio(1, null, null, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);

        String requestBody = objectMapper.writeValueAsString(envioPatch);
        when(envioService.parcharEnvio(anyInt(), any(Envio.class))).thenThrow(RuntimeException.class);

        mockMvc.perform(patch("/api/v2/logistica/envios/{id}", idNoExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void patchEnvioReturnsNotFound() throws Exception {
        Integer idNoExistente = 333;
        Envio envioPatch = new Envio(1, null, null, LocalDate.of(2025, 2, 2), false, "Observacion 1", guiaDespacho,
                vehiculoDespacho, ruta);

        String requestBody = objectMapper.writeValueAsString(envioPatch);
        when(envioService.parcharEnvio(eq(idNoExistente), any(Envio.class))).thenReturn(null);

        mockMvc.perform(patch("/api/v2/logistica/envios/{id}", idNoExistente)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());

        verify(envioService, times(1)).parcharEnvio(eq(idNoExistente), any(Envio.class));
    }

    @Test
    public void deleteEnvioSuccesful() throws Exception {
        Integer idExistente = 1;
        doNothing().when(envioService).eliminarEnvio(idExistente);
        // Simular que el envio existe
        mockMvc.perform(delete("/api/v2/logistica/envios/{id}", idExistente))
                .andExpect(status().isNoContent());

        verify(envioService, times(1)).eliminarEnvio(idExistente);
    }

    @Test
    public void deleteEnvioNotFound() throws Exception {
        Integer idNoExistente = 55;
        doThrow(new RuntimeException("No existe envio con esa id")).when(envioService).eliminarEnvio(idNoExistente);
        mockMvc.perform(delete("/api/v2/logistica/envios/{id}", idNoExistente))
                .andExpect(status().isNotFound());

        verify(envioService, times(1)).eliminarEnvio(idNoExistente);
    }

    // Metodo que simula un put
    public Envio putMock(Envio envioExistente, Envio envioPut) {
        Envio envioActualizado = envioExistente;
        envioActualizado.setEntregado(envioPut.getEntregado());
        envioActualizado.setFechaEntrega(envioPut.getFechaEntrega());
        envioActualizado.setGuiaDespacho(envioPut.getGuiaDespacho());
        envioActualizado.setIdCliente(envioPut.getIdCliente());
        envioActualizado.setIdEnvio(envioPut.getIdEnvio());
        envioActualizado.setIdOrden(envioPut.getIdOrden());
        envioActualizado.setObservacion(envioPut.getObservacion());
        envioActualizado.setRuta(envioPut.getRuta());
        envioActualizado.setVehiculoDespacho(envioPut.getVehiculoDespacho());

        return envioActualizado;
    }

    // Metodo que simula un patch
    public Envio patchMock(Envio envioExistente, Envio envioPatch) {
        Envio envioParchado = envioExistente;

        if (envioPatch.getIdCliente() != null) {
            envioParchado.setIdCliente(envioPatch.getIdCliente());
        }
        if (envioPatch.getIdOrden() != null) {
            envioParchado.setIdOrden(envioPatch.getIdOrden());
        }
        if (envioPatch.getFechaEntrega() != null) {
            envioParchado.setFechaEntrega(envioPatch.getFechaEntrega());
        }
        if (envioPatch.getEntregado() != null) {
            envioParchado.setEntregado(envioPatch.getEntregado());
        }
        if (envioPatch.getObservacion() != null) {
            envioParchado.setObservacion(envioPatch.getObservacion());
        }
        if (envioPatch.getGuiaDespacho() != null) {
            envioParchado.setGuiaDespacho(envioPatch.getGuiaDespacho());
        }
        if (envioPatch.getVehiculoDespacho() != null) {
            envioParchado.setVehiculoDespacho(envioPatch.getVehiculoDespacho());
        }
        if (envioPatch.getRuta() != null) {
            envioParchado.setRuta(envioPatch.getRuta());
        }

        return envioParchado;
    }

}
