package com.perfulandia.cl.logistica;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import com.perfulandia.cl.logistica.repository.VehiculoDespachoRepository;
import com.perfulandia.cl.logistica.service.VehiculoDespachoService;

@SpringBootTest
public class VehiculoDespachoTest {

    @Autowired
    VehiculoDespachoService vehiculoDespachoService;

    @MockitoBean
    VehiculoDespachoRepository vehiculoDespachoRepository;

    private List<VehiculoDespacho> vehiculosMock = new ArrayList<>();
    private List<Envio> enviosMock = new ArrayList<>();
    private VehiculoDespacho vehiculoRegistrar = new VehiculoDespacho(1, "BB-11", 2023, enviosMock);
    private VehiculoDespacho vehiculoParaActualizar = new VehiculoDespacho(1,"BB-11",2020,enviosMock);

    @BeforeEach
    void setUp() {
        vehiculosMock.add(new VehiculoDespacho(1, "AA-11", 2000, enviosMock));
        vehiculosMock.add(new VehiculoDespacho(2, "AA-22", 2023, enviosMock));
    }

    @Test
    public void findAll() {

        when(vehiculoDespachoRepository.findAll()).thenReturn(vehiculosMock);

        List<VehiculoDespacho> vehiculos = vehiculoDespachoService.verVehiculosDespachos();

        assertNotNull(vehiculos);
        assertEquals(2, vehiculos.size());

    }

    @Test
    public void registerVehiculoReturnsNullIfAlreadyExists() {
        // Mockeo que cuando busco por la patente del vehiculo a registrar me devuelve
        // como si existiese!
        // Por lo tanto el metodo registrarvehiculo hara que el vehiculo no sea nulo y
        // el metodo devolvera nulo!
        when(vehiculoDespachoRepository.findByPatente(vehiculoRegistrar.getPatente())).thenReturn(vehiculoRegistrar);

        VehiculoDespacho vehiculoRegistrado = vehiculoDespachoService.registrarVehiculoDespacho(vehiculoRegistrar);

        assertNull(vehiculoRegistrado);
    }

    @Test
    public void registerVehiculoReturnsNotNullIfNotExists() {
        when(vehiculoDespachoRepository.findByPatente(vehiculoRegistrar.getPatente())).thenReturn(null);

        VehiculoDespacho vehiculoRegistrado = vehiculoDespachoService.registrarVehiculoDespacho(vehiculoRegistrar);

        assertNotNull(vehiculoRegistrado);
        assertEquals(vehiculoRegistrado, vehiculoRegistrar);
    }

    @Test
    public void putVehiculoReturnsNullIfNotExists() {
        when(vehiculoDespachoRepository.findByPatente(vehiculoRegistrar.getPatente())).thenReturn(null);

        VehiculoDespacho vehiculoExistente = vehiculoDespachoService.actualizarVehiculoDespacho(vehiculoRegistrar,
                "BB-11");

        assertNull(vehiculoExistente);
    }

    @Test
    public void putVehiculoReturnsNotNullIfExists(){
        when(vehiculoDespachoRepository.findByPatente(vehiculoParaActualizar.getPatente())).thenReturn(vehiculoRegistrar);

        VehiculoDespacho vehiculoExistente = vehiculoDespachoService.actualizarVehiculoDespacho(vehiculoParaActualizar,vehiculoParaActualizar.getPatente());
        vehiculoExistente.setAno(vehiculoParaActualizar.getAno());

        when(vehiculoDespachoRepository.save(vehiculoExistente)).thenReturn(vehiculoExistente);

        assertNotNull(vehiculoExistente);
        assertNotNull(vehiculoParaActualizar);
        assertEquals(vehiculoExistente.getAno(), vehiculoRegistrar.getAno());
    }
}
