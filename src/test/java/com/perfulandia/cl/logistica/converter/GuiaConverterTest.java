package com.perfulandia.cl.logistica.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.dto.GuiaDespachoDTO;
import com.perfulandia.cl.logistica.dto.OrdenDTO;
import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.model.GuiaDespacho;


@ExtendWith(MockitoExtension.class)
public class GuiaConverterTest {

    private GuiaDespacho guiaDespacho = new GuiaDespacho(); // Guia despacho para convertir
    private OrdenDTO ordenDTO = new OrdenDTO(); // ordenDTO mockeada para entregar al DTO de GuiaDespacho
    Envio envio1 = new Envio();
    Envio envio2 = new Envio();
    List<Envio> envios = new ArrayList<>();

    @Mock
    OrdenFeignClient feignClient;

    @InjectMocks
    private GuiaDespachoConverter guiaConverter;

    @BeforeEach
    public void setUp() {

        // Seteando Guia Despacho
        guiaDespacho.setIdDespacho(1);
        guiaDespacho.setIdEnvio(123);
        guiaDespacho.setIdOrden(456);
        guiaDespacho.setEnvios(null);

        envio1.setIdEnvio(1);

        envio2.setIdEnvio(2);

        envios.add(envio1);
        envios.add(envio2);
        guiaDespacho.setEnvios(envios);

        // Seteando ordenDTO
        ordenDTO.setIdOrden(1);
        ordenDTO.setIdCliente(123);
        ordenDTO.setIdProducto(456);

    }

    @Test
    public void convertToDTOSuccessful() {
        when(feignClient.obtenerOrdenPorId(guiaDespacho.getIdOrden())).thenReturn(ordenDTO);

        GuiaDespachoDTO guiaDTO = GuiaDespachoConverter.convertToDTO(guiaDespacho, feignClient);

        assertNotNull(guiaDTO);
        assertThat(guiaDTO)
                .hasFieldOrPropertyWithValue("idDespacho", guiaDespacho.getIdDespacho())
                .hasFieldOrPropertyWithValue("idEnvio", guiaDespacho.getIdEnvio())
                .hasFieldOrPropertyWithValue("idOrden", guiaDespacho.getIdOrden())
                .hasFieldOrPropertyWithValue("datosOrden",ordenDTO)
                .hasFieldOrPropertyWithValue("idEnvios", guiaDTO.getIdEnvios());
    }

    @Test
    public void convertToDTO_EnvioIsNull(){
        guiaDespacho.setEnvios(null);
        when(feignClient.obtenerOrdenPorId(guiaDespacho.getIdOrden())).thenReturn(ordenDTO);

        GuiaDespachoDTO guiaDTO = GuiaDespachoConverter.convertToDTO(guiaDespacho, feignClient);

        assertNotNull(guiaDTO);
        assertThat(guiaDTO)
                .hasFieldOrPropertyWithValue("idDespacho", guiaDespacho.getIdDespacho())
                .hasFieldOrPropertyWithValue("idEnvio", guiaDespacho.getIdEnvio())
                .hasFieldOrPropertyWithValue("idOrden", guiaDespacho.getIdOrden())
                .hasFieldOrPropertyWithValue("datosOrden",ordenDTO)
                .hasFieldOrPropertyWithValue("idEnvios", null);
    }

    @Test
    public void convertToDTO_NullGuiaDespacho(){
        guiaDespacho = null;

        NullPointerException exception = assertThrows(NullPointerException.class, ()->{
            GuiaDespachoDTO guiaDTO = GuiaDespachoConverter.convertToDTO(guiaDespacho, feignClient);
        });


    }
}
