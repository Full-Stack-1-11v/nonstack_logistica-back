package com.perfulandia.cl.logistica.controller;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.cl.logistica.assemblers.VehiculoDespachoAssembler;
import com.perfulandia.cl.logistica.assemblers.VehiculoDespachoDTOAssembler;
import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.converter.VehiculoDespachoConverter;
import com.perfulandia.cl.logistica.dto.VehiculoDespachoDTO;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.repository.RutaRepository;
import com.perfulandia.cl.logistica.service.VehiculoDespachoService;

@WebMvcTest(VehiculoDespachoControllerV2.class)
public class VehiculoDespachoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehiculoDespachoDTOAssembler vehiculoDTOAssembler;

    @Autowired
    private VehiculoDespachoAssembler vehiculoAssembler;

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

    private VehiculoDespacho vehiculoMock = new VehiculoDespacho();
    private List<VehiculoDespacho> vehiculosMock = new ArrayList();
    private List<Envio> envios = new ArrayList();

    @BeforeEach
    public void setUp() {

        Envio envio1 = new Envio();
        envio1.setIdEnvio(1);
        envios.add(envio1);
        vehiculoMock.setIdVehiculoDespacho(1);
        vehiculoMock.setAno(2025);
        vehiculoMock.setEnvios(envios);
        vehiculoMock.setPatente("AA-11");

        // Agregando vehiculos a vehiculosMock

        vehiculoMock.setIdVehiculoDespacho(1);
        vehiculoMock.setAno(2025);
        vehiculoMock.setEnvios(envios);
        vehiculoMock.setPatente("AA-11");

        VehiculoDespacho vehiculo2 = new VehiculoDespacho();
        vehiculo2.setIdVehiculoDespacho(2);
        vehiculo2.setAno(2024);
        vehiculo2.setEnvios(envios);
        vehiculo2.setPatente("BB-22");

        VehiculoDespacho vehiculo3 = new VehiculoDespacho();
        vehiculo3.setIdVehiculoDespacho(3);
        vehiculo3.setAno(2023);
        vehiculo3.setEnvios(envios);
        vehiculo3.setPatente("CC-33");

        vehiculosMock.add(vehiculoMock);
        vehiculosMock.add(vehiculo2);
        vehiculosMock.add(vehiculo3);

    }

    @Test
    public void getVehiculoDespachoSuccesful() throws Exception {
        when(vehiculoDespachoService.verVehiculosDespachos()).thenReturn(vehiculosMock);

        mockMvc.perform(get("/api/v2/logistica/vehiculos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json")) // Por que es HATEOAS
                .andExpect(jsonPath("$._embedded.vehiculoDespachoDTOList", hasSize(vehiculosMock.size())))
                .andExpect(jsonPath("$._embedded.vehiculoDespachoDTOList[0].idVehiculoDespacho",
                        is(vehiculosMock.get(0).getIdVehiculoDespacho())))
                .andExpect(jsonPath("$._embedded.vehiculoDespachoDTOList[0].patente",
                        is(vehiculosMock.get(0).getPatente())))
                .andExpect(jsonPath("$._embedded.vehiculoDespachoDTOList[0].ano", is(vehiculosMock.get(0).getAno())))
                .andExpect(
                        jsonPath("$._embedded.vehiculoDespachoDTOList[0].idEnvios[0]", is(envios.get(0).getIdEnvio())));

        verify(vehiculoDespachoService, times(1)).verVehiculosDespachos();

    }

    @Test
    public void getVehiculoDespachoReturnsNoContent() throws Exception {
        List<VehiculoDespacho> vehiculosMock = new ArrayList();
        when(vehiculoDespachoService.verVehiculosDespachos()).thenReturn(vehiculosMock);
        mockMvc.perform(get("/api/v2/logistica/vehiculos"))
                .andExpect(status().isNoContent());

        verify(vehiculoDespachoService, times(1)).verVehiculosDespachos();

    }

    @Test
    public void getVehiculoDespachoReturnsServerError() throws Exception {
        when(vehiculoDespachoService.verVehiculosDespachos()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v2/logistica/vehiculos"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getVehiculosDespachoByPatronPatenteSuccesful() throws Exception {
        String patronPatente = "AA";
        List<VehiculoDespacho> vehiculosEncontrados = vehiculosMock.stream()
                .filter(vehiculo -> vehiculo.getPatente().contains(patronPatente)).toList();
        List<VehiculoDespachoDTO> vehiculosDTO = vehiculosEncontrados.stream()
                .map(VehiculoDespachoConverter::convertDTOVehiculo).toList();
        List<EntityModel<VehiculoDespachoDTO>> vehiculosAssembled = vehiculosDTO.stream()
                .map(vehiculoDTOAssembler::toModel)
                .toList();
        CollectionModel<EntityModel<VehiculoDespachoDTO>> collectionModel = CollectionModel.of(vehiculosAssembled);
        String bodyResponse = objectMapper.writeValueAsString(collectionModel);

        when(vehiculoDespachoService.buscarVehiculoPorPatronPatente(patronPatente)).thenReturn(vehiculosEncontrados);

        mockMvc.perform(get("/api/v2/logistica/vehiculos/{patron_patente}", patronPatente)
                .contentType("application/hal+json")
                .content(bodyResponse))
                .andExpect(status().isOk());

        verify(vehiculoDespachoService, times(1)).buscarVehiculoPorPatronPatente(patronPatente);

    }

    @Test
    public void getVehiculoDespachoByPatronPatenteIncorrectLenght() throws Exception {
        String patronPatente = "AAAA";

        mockMvc.perform(get("/api/v2/logistica/vehiculos/{patron_patente}", patronPatente))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getVehiculoDespachoByPatronPatenteReturnsServerError() throws Exception {
        String patronPatente = "AA";
        when(vehiculoDespachoService.buscarVehiculoPorPatronPatente(patronPatente)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v2/logistica/vehiculos/{patron_patente}", patronPatente))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void postVehiculoDespachoSuccessful() throws Exception {
        VehiculoDespacho vehiculoCrear = new VehiculoDespacho(); // El body solo pide patente y anio!
        vehiculoCrear.setPatente("BB-11");
        vehiculoCrear.setAno(2024);
        String bodyRequest = objectMapper.writeValueAsString(vehiculoCrear);

        when(vehiculoDespachoService.registrarVehiculoDespacho(vehiculoCrear)).thenReturn(vehiculoCrear);

        mockMvc.perform(post("/api/v2/logistica/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.idVehiculoDespacho").hasJsonPath()) // Chequeamos si esta el json path, no asigna
                                                                           // id
                .andExpect(jsonPath("$.patente", is("BB-11")))
                .andExpect(jsonPath("$.ano", is(2024)))
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.patch.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists())
                .andExpect(jsonPath("$._links.vehiculos.href").exists());

    }

    @Test
    public void postVehiculoDespachoReturnsConflict() throws Exception {
        VehiculoDespacho vehiculoCrear = new VehiculoDespacho(); // El body solo pide patente y anio!
        vehiculoCrear.setPatente("BB-11");
        vehiculoCrear.setAno(2024);
        String bodyRequest = objectMapper.writeValueAsString(vehiculoCrear);

        when(vehiculoDespachoService.registrarVehiculoDespacho(vehiculoCrear)).thenReturn(null);

        mockMvc.perform(post("/api/v2/logistica/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isConflict());

    }

    @Test
    public void postVehiculoDespachoReturnsInternalServerError() throws Exception {
        VehiculoDespacho vehiculoCrear = new VehiculoDespacho(); // El body solo pide patente y anio!
        vehiculoCrear.setPatente("BB-11");
        vehiculoCrear.setAno(2024);
        String bodyRequest = objectMapper.writeValueAsString(vehiculoCrear);

        when(vehiculoDespachoService.registrarVehiculoDespacho(vehiculoCrear)).thenThrow(new RuntimeException());
        mockMvc.perform(post("/api/v2/logistica/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyRequest))
                .andExpect(status().isInternalServerError());

    }

}
