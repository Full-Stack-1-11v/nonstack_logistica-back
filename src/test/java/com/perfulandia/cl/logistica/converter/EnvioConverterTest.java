package com.perfulandia.cl.logistica.converter;

import com.perfulandia.cl.logistica.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.perfulandia.cl.logistica.dto.EnvioDTO;
import com.perfulandia.cl.logistica.model.Envio;

public class EnvioConverterTest {

    // Ojo, com el metodo es estatico, no es necesario injectmock, pero lo dejo para
    // recordarme

    @InjectMocks
    EnvioConverter envioConverter;

    Envio envio = new Envio();

    @BeforeEach
    public void setUp() {

        envio.setIdEnvio(1);
        envio.setIdCliente(123);
        envio.setIdOrden(456);
        envio.setFechaEntrega(LocalDate.of(2025, 7, 15));
        envio.setEntregado(true);
        envio.setObservacion("Entrega exitosa en la puerta principal.");

        // mock GuiaDespacho
        GuiaDespacho guiaDespacho = new GuiaDespacho();
        guiaDespacho.setIdDespacho(1);
        guiaDespacho.setIdEnvio(1); 
        guiaDespacho.setIdOrden(789); 
        envio.setGuiaDespacho(guiaDespacho);

        // mock VehiculoDespacho
        VehiculoDespacho vehiculoDespacho = new VehiculoDespacho();
        vehiculoDespacho.setIdVehiculoDespacho(1);
        vehiculoDespacho.setPatente("AA-11"); 
        vehiculoDespacho.setAno(2023); 
        envio.setVehiculoDespacho(vehiculoDespacho);

        // mock Ruta
        Ruta ruta = new Ruta();
        ruta.setIdRuta(1);
        ruta.setCoordXInicio(25.03F); 
        ruta.setCoordYInicio(15.03F); 
        ruta.setCoordXFinal(-25.03F); 
        ruta.setCoordYFinal(-15.03F); 
        envio.setRuta(ruta);
    }

    @Test
    public void convertToDTOSuccessful() {

        EnvioDTO envioDTO = EnvioConverter.convertToDTO(envio);

        assertNotNull(envioDTO);
        assertEquals(envio.getIdEnvio(), envioDTO.getIdEnvio());
        assertEquals(envio.getIdCliente(), envioDTO.getIdCliente());
        assertEquals(envio.getIdOrden(), envioDTO.getIdOrden());
        assertEquals(envio.getFechaEntrega(), envioDTO.getFechaEntrega());
        assertEquals(envio.getEntregado(), envioDTO.getEntregado());
        assertEquals(envio.getObservacion(), envioDTO.getObservacion());
        assertEquals(envio.getGuiaDespacho().getIdDespacho(), envioDTO.getGuiaDespachoId());
        assertEquals(envio.getVehiculoDespacho().getIdVehiculoDespacho(), envioDTO.getVehiculoDespachoId());
        assertEquals(envio.getRuta().getIdRuta(), envioDTO.getRutaId());
    }

    @Test
    public void converToDTO_GuiaDespachoIsNull() {
        envio.setGuiaDespacho(null);

        EnvioDTO envioDTO = EnvioConverter.convertToDTO(envio);

        assertNotNull(envioDTO);
        assertEquals(envio.getIdEnvio(), envioDTO.getIdEnvio());
        assertEquals(envio.getIdCliente(), envioDTO.getIdCliente());
        assertEquals(envio.getIdOrden(), envioDTO.getIdOrden());
        assertEquals(envio.getFechaEntrega(), envioDTO.getFechaEntrega());
        assertEquals(envio.getEntregado(), envioDTO.getEntregado());
        assertEquals(envio.getObservacion(), envioDTO.getObservacion());
        assertNull(envioDTO.getGuiaDespachoId());
        assertEquals(envio.getVehiculoDespacho().getIdVehiculoDespacho(), envioDTO.getVehiculoDespachoId());
        assertEquals(envio.getRuta().getIdRuta(), envioDTO.getRutaId());
    }

    @Test
    public void converToDTO_VehiculoDespachoIsNull() {
        envio.setVehiculoDespacho(null);

        EnvioDTO envioDTO = EnvioConverter.convertToDTO(envio);

        assertNotNull(envioDTO);
        assertEquals(envio.getIdEnvio(), envioDTO.getIdEnvio());
        assertEquals(envio.getIdCliente(), envioDTO.getIdCliente());
        assertEquals(envio.getIdOrden(), envioDTO.getIdOrden());
        assertEquals(envio.getFechaEntrega(), envioDTO.getFechaEntrega());
        assertEquals(envio.getEntregado(), envioDTO.getEntregado());
        assertEquals(envio.getObservacion(), envioDTO.getObservacion());
        assertEquals(envio.getGuiaDespacho().getIdDespacho(), envioDTO.getGuiaDespachoId());
        assertNull(envioDTO.getVehiculoDespachoId());
        assertEquals(envio.getRuta().getIdRuta(), envioDTO.getRutaId());
    }

    @Test
    public void converToDTO_RutaIsNull() {
        envio.setRuta(null);

        EnvioDTO envioDTO = EnvioConverter.convertToDTO(envio);

        assertNotNull(envioDTO);
        assertEquals(envio.getIdEnvio(), envioDTO.getIdEnvio());
        assertEquals(envio.getIdCliente(), envioDTO.getIdCliente());
        assertEquals(envio.getIdOrden(), envioDTO.getIdOrden());
        assertEquals(envio.getFechaEntrega(), envioDTO.getFechaEntrega());
        assertEquals(envio.getEntregado(), envioDTO.getEntregado());
        assertEquals(envio.getObservacion(), envioDTO.getObservacion());
        assertEquals(envio.getGuiaDespacho().getIdDespacho(), envioDTO.getGuiaDespachoId());
        assertEquals(envio.getVehiculoDespacho().getIdVehiculoDespacho(), envioDTO.getVehiculoDespachoId());
        assertNull(envioDTO.getRutaId());
    }

    @Test
    public void converToDTOAllNull() {

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            EnvioDTO envioDTO = EnvioConverter.convertToDTO(null);
        });
    }

}
