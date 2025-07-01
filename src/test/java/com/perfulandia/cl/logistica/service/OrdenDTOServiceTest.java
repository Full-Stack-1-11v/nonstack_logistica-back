package com.perfulandia.cl.logistica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.dto.OrdenDTO;
import com.perfulandia.cl.logistica.service.OrdenDTOService;

@SpringBootTest
public class OrdenDTOServiceTest {

    @Autowired
    OrdenDTOService ordenDTOService;

    @MockitoBean
    OrdenFeignClient ordenFeignClient;

    List<OrdenDTO> ordenDTOsMock = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        ordenDTOsMock.add(new OrdenDTO(1, 101, 201));
        ordenDTOsMock.add(new OrdenDTO(2, 102, 202));
        ordenDTOsMock.add(new OrdenDTO(3, 103, 203));
        ordenDTOsMock.add(new OrdenDTO(4, 104, 204));
    }

    @Test
    public void verOrdenesSuccessful() {
        when(ordenFeignClient.getOrdenes()).thenReturn(ordenDTOsMock);

        List<OrdenDTO> ordenesDTO = ordenDTOService.verOrdenes();

        assertNotNull(ordenesDTO);
        assertEquals(4, ordenesDTO.size());
        for (int i = 0; i < ordenesDTO.size(); i++) {
            assertThat(ordenesDTO.get(i))
            .hasFieldOrPropertyWithValue("idOrden", ordenDTOsMock.get(i).getIdOrden())
            .hasFieldOrPropertyWithValue("idCliente", ordenDTOsMock.get(i).getIdCliente())
            .hasFieldOrPropertyWithValue("idProducto", ordenDTOsMock.get(i).getIdProducto());
        }
    }

    @Test
    public void obtenerOrdenPorIdSuccessful() {
        Integer idExistente = 1;
        OrdenDTO ordenDTOMock = new OrdenDTO(1, 101, 201);
        when(ordenFeignClient.obtenerOrdenPorId(idExistente)).thenReturn(ordenDTOMock);

        OrdenDTO ordenDTO = ordenDTOService.obtenerOrdenPorId(idExistente);

        assertNotNull(ordenDTO);
        assertThat(ordenDTO)
                .hasFieldOrPropertyWithValue("idOrden", ordenDTOMock.getIdOrden())
                .hasFieldOrPropertyWithValue("idCliente", ordenDTOMock.getIdCliente())
                .hasFieldOrPropertyWithValue("idProducto", ordenDTOMock.getIdProducto());

    }

}
