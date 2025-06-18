package com.perfulandia.cl.logistica.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.perfulandia.cl.logistica.client.OrdenFeignClient;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(EnvioControllerV2.class)
public class EnvioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

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

    private List<Envio> listEnviosMock = new ArrayList<>();;
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
        Envio envio1 = new Envio(1, 101, 1, LocalDate.now(), false, "Observacion 1", guiaDespacho, vehiculoDespacho,
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
                .andExpect(status().isOk());
    }

}
