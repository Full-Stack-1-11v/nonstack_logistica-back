package com.perfulandia.cl.logistica.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.perfulandia.cl.logistica.dto.VehiculoDespachoDTO;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;

public class VehiculoDespachoConverterTest {

    private VehiculoDespacho vehiculoDespacho = new VehiculoDespacho();
    private List<Envio> envios = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        // Solo me interesan los Ids...
        envios.add(new Envio(1, null, null, null, null, null, null, null, null));
        envios.add(new Envio(2, null, null, null, null, null, null, null, null));
        envios.add(new Envio(3, null, null, null, null, null, null, null, null));

        vehiculoDespacho.setIdVehiculoDespacho(1);
        vehiculoDespacho.setPatente("AA-11");
        vehiculoDespacho.setAno(2025);
        vehiculoDespacho.setEnvios(envios);
    }

    @Test
    public void VehiculoDespachoConvertToDTOSuccessful() {

        VehiculoDespachoDTO vehiculoDespachoDTO = VehiculoDespachoConverter.convertDTOVehiculo(vehiculoDespacho);

        assertNotNull(vehiculoDespachoDTO);
        assertEquals(vehiculoDespacho.getAno(), vehiculoDespachoDTO.getAno());
        assertEquals(vehiculoDespacho.getEnvios().size(), vehiculoDespachoDTO.getIdEnvios().size());
        assertEquals(vehiculoDespacho.getIdVehiculoDespacho(), vehiculoDespachoDTO.getIdVehiculoDespacho());
        assertEquals(vehiculoDespacho.getPatente(), vehiculoDespachoDTO.getPatente());

        // Verificamos que todos los envios tienen la misma id
        for (int i = 0; i < vehiculoDespacho.getEnvios().size(); i++) {
            assertEquals(vehiculoDespacho.getEnvios().get(i).getIdEnvio(), vehiculoDespachoDTO.getIdEnvios().get(i));
        }
    }

    @Test
    public void VehiculoDespachoConvertToDTO_EnviosIsNull() {
        vehiculoDespacho.setEnvios(null);;
        VehiculoDespachoDTO vehiculoDespachoDTO = VehiculoDespachoConverter.convertDTOVehiculo(vehiculoDespacho);

        assertNotNull(vehiculoDespachoDTO);
        assertNull(vehiculoDespachoDTO.getIdEnvios());
        assertEquals(vehiculoDespacho.getAno(), vehiculoDespachoDTO.getAno());
        assertEquals(vehiculoDespacho.getIdVehiculoDespacho(), vehiculoDespachoDTO.getIdVehiculoDespacho());
        assertEquals(vehiculoDespacho.getPatente(), vehiculoDespachoDTO.getPatente());

    }

    @Test
    public void VehiculoDespachoConvertToDTO_VehiculoDespachoIsNull(){
        vehiculoDespacho = null;

        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            VehiculoDespachoDTO vehiculoDespachoDTO = VehiculoDespachoConverter.convertDTOVehiculo(vehiculoDespacho);
        });
    }

}
