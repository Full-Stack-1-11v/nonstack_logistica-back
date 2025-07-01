package com.perfulandia.cl.logistica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
import com.perfulandia.cl.logistica.repository.GuiaDespachoRepository;
import com.perfulandia.cl.logistica.service.GuiaDespachoService;

@SpringBootTest
public class GuiaDespachoServiceTest {

    @Autowired
    GuiaDespachoService guiaDespachoService;

    @MockitoBean
    GuiaDespachoRepository guiaDespachoRepository;

    // Despacho individual
    GuiaDespacho despachoMock = new GuiaDespacho(1, 1, 1, null);
    // Despacho para put
    GuiaDespacho despachoPutMock = new GuiaDespacho(1, 2, 2, null);

    // Lista de despachos
    List<GuiaDespacho> despachosMock = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        despachosMock.add(new GuiaDespacho(1, 1, 1, null));
        despachosMock.add(new GuiaDespacho(2, 2, 2, null));
        despachosMock.add(new GuiaDespacho(3, 3, 3, null));
        despachosMock.add(new GuiaDespacho(4, 4, 4, null));
    }

    @Test
    public void findAllGuiaDespacho() {
        when(guiaDespachoRepository.findAll()).thenReturn(despachosMock);

        List<GuiaDespacho> despachosEncontrados = guiaDespachoService.verGuiaDespachos();

        assertNotNull(despachosEncontrados);
        assertEquals(4, despachosEncontrados.size());
        for (GuiaDespacho guiaDespacho : despachosEncontrados) {
            assertThat(guiaDespacho)
                    .hasFieldOrProperty("idDespacho")
                    .hasFieldOrProperty("idEnvio")
                    .hasFieldOrProperty("idOrden");
        }
    }

    @Test
    public void saveGuiaDespacho_ReturnsIllegalArgumentException() {
        when(guiaDespachoRepository.save(null)).thenThrow(new IllegalArgumentException("No puede ser nulo"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            GuiaDespacho despachoGuardado = guiaDespachoService.crearGuiaDespacho(null);
        });

        assertEquals("No puede ser nulo", exception.getMessage());
        verify(guiaDespachoRepository, times(1)).save(null);
    }

    @Test
    public void saveGuiaDespachoSuccessfull() throws Exception {
        when(guiaDespachoRepository.save(despachoMock)).thenReturn(despachoMock);

        GuiaDespacho despachoGuardado = guiaDespachoService.crearGuiaDespacho(despachoMock);
        assertNotNull(despachoGuardado);
        assertEquals(despachoGuardado, despachoGuardado);
        verify(guiaDespachoRepository, times(1)).save(despachoMock);

    }

    @Test
    public void putGuiaDespacho_NonExistingId_ThrowsRunTimeException() {
        Integer idNoExistente = 555;
        when(guiaDespachoRepository.existsById(idNoExistente)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            GuiaDespacho guiaDespachoActualizada = guiaDespachoService.putGuiaDespacho(despachoPutMock, idNoExistente);
        });

        assertEquals("El despacho no existe", exception.getMessage());
    }

    @Test
    public void putGuiaDespacho_InvalidId_ThrowsIllegalArgumentException() {
        Integer idNoValido = null;
        when(guiaDespachoRepository.existsById(null)).thenThrow(new IllegalArgumentException("Id invalido"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            GuiaDespacho guiaDespachoActualizada = guiaDespachoService.putGuiaDespacho(despachoPutMock, idNoValido);
        });

        assertEquals("Id invalido", exception.getMessage());
    }

    @Test
    public void putGuiaDespachoSuccessful() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaDespachoPut = new GuiaDespacho(idExistente, 2, 2, null);
        GuiaDespacho guiaDespachoExistente = new GuiaDespacho(idExistente, 1, 1, null);
        Optional<GuiaDespacho> guiaDespachoExistenteOptional = Optional.of(guiaDespachoExistente);

        when(guiaDespachoRepository.existsById(idExistente)).thenReturn(true);
        when(guiaDespachoRepository.findById(idExistente)).thenReturn(guiaDespachoExistenteOptional);

        // Esta la uso para comparar
        GuiaDespacho guiaDespachoActualizada = guiaDespachoMockPut(guiaDespachoExistente, guiaDespachoPut);

        GuiaDespacho guiaDespachoParchada = guiaDespachoService.putGuiaDespacho(guiaDespachoPut, idExistente);

        assertNotNull(guiaDespachoParchada);
        assertThat(guiaDespachoParchada)
                .hasFieldOrPropertyWithValue("idEnvio", guiaDespachoActualizada.getIdEnvio())
                .hasFieldOrPropertyWithValue("idOrden", guiaDespachoActualizada.getIdOrden());
        verify(guiaDespachoRepository, times(1)).save(guiaDespachoExistente);

    }

    @Test
    public void patchGuiaDespacho_NonExistingId_ThrowsRuntimeException() {
        Integer idNoExistente = 111;
        when(guiaDespachoRepository.existsById(idNoExistente)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.parcharGuiaDespacho(despachoMock, idNoExistente);
        });

        assertEquals("El despacho no existe", exception.getMessage());
    }

    @Test
    public void patchGuiaDespachoSuccessful() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaDespachoExistente = new GuiaDespacho(1, 1, 1, null);
        GuiaDespacho guiaDespachoPatch = new GuiaDespacho(1, 2, 2, null);
        Optional<GuiaDespacho> guiaDespachoExistenteOptional = Optional.of(guiaDespachoExistente);
        GuiaDespacho guiaDespachoParchada = guiaDespachoMockPatch(guiaDespachoExistente, guiaDespachoPatch);

        when(guiaDespachoRepository.existsById(idExistente)).thenReturn(true);
        when(guiaDespachoRepository.findById(idExistente)).thenReturn(guiaDespachoExistenteOptional);
        when(guiaDespachoRepository.save(any(GuiaDespacho.class))).thenReturn(guiaDespachoParchada);

        GuiaDespacho guiaDespachoActualizada = guiaDespachoService.parcharGuiaDespacho(guiaDespachoPatch,
                idExistente);
        assertNotNull(guiaDespachoActualizada);
        assertThat(guiaDespachoActualizada)
                .hasFieldOrPropertyWithValue("idEnvio", guiaDespachoParchada.getIdEnvio())
                .hasFieldOrPropertyWithValue("idOrden", guiaDespachoParchada.getIdOrden());
        verify(guiaDespachoRepository, times(1)).save(any(GuiaDespacho.class));

    }

    @Test
    public void patchGuiaDespacho_InvalidId_ThrowsIllegalArgumentException() {
        Integer idInvalido = null;
        when(guiaDespachoRepository.existsById(idInvalido)).thenThrow(new IllegalArgumentException("Id invalido"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            GuiaDespacho guiaDespachoParchada = guiaDespachoService.parcharGuiaDespacho(despachoMock, idInvalido);
        });

        assertEquals("Id invalido", exception.getMessage());
    }

    @Test
    public void patchGuiaDespacho_AllNull_ThrowsRuntimeException() {
        Integer idExistente = 1;
        GuiaDespacho guiaDespachoExistente = new GuiaDespacho(1, 1, 1, null);
        GuiaDespacho guiaDespachoPatch = new GuiaDespacho(1, null, null, null);
        Optional<GuiaDespacho> guiaDespachoExistenteOptional = Optional.of(guiaDespachoExistente);

        when(guiaDespachoRepository.existsById(idExistente)).thenReturn(true);
        when(guiaDespachoRepository.findById(idExistente)).thenReturn(guiaDespachoExistenteOptional);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            GuiaDespacho guiaDespachoActualizada = guiaDespachoService.parcharGuiaDespacho(guiaDespachoPatch,
                    idExistente);
        });

        assertEquals("Debe de haber almenos un atributo para parchar", exception.getMessage());

    }

    @Test
    public void patchGuiaDespacho_OnlyIdOrden() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaDespachoExistente = new GuiaDespacho(1, 1, 1, null);
        GuiaDespacho guiaDespachoPatch = new GuiaDespacho(1, null, 2, null);
        Optional<GuiaDespacho> guiaDespachoExistenteOptional = Optional.of(guiaDespachoExistente);
        GuiaDespacho guiaDespachoParchada = guiaDespachoMockPatch(guiaDespachoExistente, guiaDespachoPatch);

        when(guiaDespachoRepository.existsById(idExistente)).thenReturn(true);
        when(guiaDespachoRepository.findById(idExistente)).thenReturn(guiaDespachoExistenteOptional);
        when(guiaDespachoRepository.save(any(GuiaDespacho.class))).thenReturn(guiaDespachoParchada);

        GuiaDespacho guiaDespachoActualizada = guiaDespachoService.parcharGuiaDespacho(guiaDespachoPatch,
                idExistente);
        assertNotNull(guiaDespachoActualizada);
        assertThat(guiaDespachoActualizada)
                .hasFieldOrPropertyWithValue("idEnvio", guiaDespachoParchada.getIdEnvio())
                .hasFieldOrPropertyWithValue("idOrden", guiaDespachoParchada.getIdOrden());
        verify(guiaDespachoRepository, times(1)).save(any(GuiaDespacho.class));

    }

    @Test
    public void patchGuiaDespacho_OnlyIdEnvio() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaDespachoExistente = new GuiaDespacho(1, 1, 1, null);
        GuiaDespacho guiaDespachoPatch = new GuiaDespacho(1, 2, null, null);
        Optional<GuiaDespacho> guiaDespachoExistenteOptional = Optional.of(guiaDespachoExistente);
        GuiaDespacho guiaDespachoParchada = guiaDespachoMockPatch(guiaDespachoExistente, guiaDespachoPatch);

        when(guiaDespachoRepository.existsById(idExistente)).thenReturn(true);
        when(guiaDespachoRepository.findById(idExistente)).thenReturn(guiaDespachoExistenteOptional);
        when(guiaDespachoRepository.save(any(GuiaDespacho.class))).thenReturn(guiaDespachoParchada);

        GuiaDespacho guiaDespachoActualizada = guiaDespachoService.parcharGuiaDespacho(guiaDespachoPatch,
                idExistente);
        assertNotNull(guiaDespachoActualizada);
        assertThat(guiaDespachoActualizada)
                .hasFieldOrPropertyWithValue("idEnvio", guiaDespachoParchada.getIdEnvio())
                .hasFieldOrPropertyWithValue("idOrden", guiaDespachoParchada.getIdOrden());
        verify(guiaDespachoRepository, times(1)).save(any(GuiaDespacho.class));

    }

    @Test
    public void deleteGuiaDespachoSuccessful() throws Exception {
        Integer idExistente = 1;
        GuiaDespacho guiaDespachoExistente = new GuiaDespacho(1, 1, 1, null);
        Optional<GuiaDespacho> guiaDespachoExistenteOptional = Optional.of(guiaDespachoExistente);
        when(guiaDespachoRepository.existsById(idExistente)).thenReturn(true);
        when(guiaDespachoRepository.findById(idExistente)).thenReturn(guiaDespachoExistenteOptional);

        guiaDespachoService.borrarGuiaDespacho(idExistente);

        verify(guiaDespachoRepository, times(1)).delete(guiaDespachoExistente);

    }

    @Test
    public void deleteGuiaDespacho_NonExistingId_ReturnsRunTimeException() {
        Integer idNoExistente = 555;
        when(guiaDespachoRepository.existsById(idNoExistente)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            guiaDespachoService.borrarGuiaDespacho(idNoExistente);
        });

        assertEquals("No existe la guia de despacho con esa id.", exception.getMessage());
    }

    

    // Metodo que simula un put
    public GuiaDespacho guiaDespachoMockPut(GuiaDespacho guiaDespachoExistente, GuiaDespacho guiaPut) {
        GuiaDespacho guiaDespachoActualizada = guiaDespachoExistente;
        guiaDespachoActualizada.setIdEnvio(guiaPut.getIdEnvio());
        guiaDespachoActualizada.setIdOrden(guiaPut.getIdOrden());

        return guiaDespachoActualizada;
    }

    // Metodo que simula un patch
    public GuiaDespacho guiaDespachoMockPatch(GuiaDespacho guiaDespachoExistente, GuiaDespacho guiaPatch) {
        GuiaDespacho guiaDespachoActualizada = guiaDespachoExistente;
        if (guiaPatch.getIdEnvio() != null) {
            guiaDespachoActualizada.setIdEnvio(guiaPatch.getIdEnvio());
        }
        if (guiaPatch.getIdOrden() != null) {
            guiaDespachoActualizada.setIdOrden(guiaPatch.getIdOrden());
        }

        return guiaDespachoActualizada;
    }

}
