package com.perfulandia.cl.logistica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.perfulandia.cl.logistica.model.Envio;
import com.perfulandia.cl.logistica.repository.EnvioRepository;
import com.perfulandia.cl.logistica.service.EnvioService;

@SpringBootTest
public class EnvioServiceTest {

    @Autowired
    EnvioService envioService;

    @MockitoBean
    EnvioRepository envioRepository;

    List<Envio> enviosMock = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Envio envioMock1 = new Envio(1, 1, 1, LocalDate.now(), true, "En bodega", null, null, null);
        Envio envioMock2 = new Envio(2, 2, 2, LocalDate.now().plusDays(1), false, "En ruta", null, null, null);
        Envio envioMock3 = new Envio(3, 3, 3, LocalDate.now().plusDays(2), true, "Entregado", null, null, null);
        Envio envioMock4 = new Envio(4, 4, 4, LocalDate.now().plusDays(3), false, "Retrasado", null, null, null);
        enviosMock.add(envioMock1);
        enviosMock.add(envioMock2);
        enviosMock.add(envioMock3);
        enviosMock.add(envioMock4);
    }

    @Test
    public void getAllEnviosSuccesful() {

        when(envioRepository.findAll()).thenReturn(enviosMock);

        List<Envio> enviosEncontrados = envioService.obtenerEnvios();

        assertNotNull(enviosEncontrados);
        assertEquals(4, enviosEncontrados.size());
        verify(envioRepository, times(1)).findAll();
    }

    @Test
    public void getEnvioByIdSuccesful() {
        Envio envioMock = new Envio(1, 1, 1, LocalDate.now(), true, "En bodega", null, null, null);
        Optional<Envio> envioMockOptional = Optional.of(envioMock);
        Integer idExistente = 1;
        when(envioRepository.findById(idExistente)).thenReturn(envioMockOptional);

        Optional<Envio> envioEncontrado = envioService.obtenerEnvioPorId(idExistente);

        assertNotNull(envioEncontrado);
        assertThat(envioEncontrado.get())
                .hasFieldOrPropertyWithValue("idEnvio", envioMock.getIdEnvio())
                .hasFieldOrPropertyWithValue("idCliente", envioMock.getIdCliente())
                .hasFieldOrPropertyWithValue("idOrden", envioMock.getIdOrden())
                .hasFieldOrPropertyWithValue("fechaEntrega", envioMock.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioMock.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioMock.getObservacion())
                .hasFieldOrPropertyWithValue("guiaDespacho", envioMock.getGuiaDespacho())
                .hasFieldOrPropertyWithValue("vehiculoDespacho", envioMock.getVehiculoDespacho())
                .hasFieldOrPropertyWithValue("ruta", envioMock.getRuta());
        verify(envioRepository, times(1)).findById(idExistente);
    }

    @Test
    public void createEnvioSuccessful() {
        Envio envioMock = new Envio(1, 1, 1, LocalDate.now(), true, "En bodega", null, null, null);
        when(envioRepository.save(envioMock)).thenReturn(envioMock);

        Envio envioRegistrado = envioService.crearEnvio(envioMock);

        assertNotNull(envioRegistrado);
        assertThat(envioRegistrado)
                .hasFieldOrPropertyWithValue("idEnvio", envioMock.getIdEnvio())
                .hasFieldOrPropertyWithValue("idCliente", envioMock.getIdCliente())
                .hasFieldOrPropertyWithValue("idOrden", envioMock.getIdOrden())
                .hasFieldOrPropertyWithValue("fechaEntrega", envioMock.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioMock.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioMock.getObservacion())
                .hasFieldOrPropertyWithValue("guiaDespacho", envioMock.getGuiaDespacho())
                .hasFieldOrPropertyWithValue("vehiculoDespacho", envioMock.getVehiculoDespacho())
                .hasFieldOrPropertyWithValue("ruta", envioMock.getRuta());
        verify(envioRepository, times(1)).save(envioRegistrado);
    }

    @Test
    public void putEnvio_EnvioExistsReturnsSuccessful() {
        Integer idExistente = 1;
        Envio envioPut = new Envio(1, 1, 1, LocalDate.now(), true, "En bodega", null, null, null);
        Envio envioExistente = new Envio(1, 2, 3, LocalDate.now(), false, "En salon", null, null, null);
        Optional<Envio> envioExistenteOptional = Optional.of(envioExistente);
        Envio envioNuevo = envioMockPut(envioExistente, envioPut);
        when(envioRepository.findById(idExistente)).thenReturn(envioExistenteOptional);
        when(envioRepository.save(envioNuevo)).thenReturn(envioNuevo);

        Envio envioActualizado = envioService.actualizarEnvio(idExistente, envioNuevo);

        assertNotNull(envioActualizado);
        assertThat(envioActualizado)
                .hasFieldOrPropertyWithValue("idEnvio", envioNuevo.getIdEnvio())
                .hasFieldOrPropertyWithValue("idCliente", envioNuevo.getIdCliente())
                .hasFieldOrPropertyWithValue("idOrden", envioNuevo.getIdOrden())
                .hasFieldOrPropertyWithValue("fechaEntrega", envioNuevo.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioNuevo.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioNuevo.getObservacion())
                .hasFieldOrPropertyWithValue("guiaDespacho", envioNuevo.getGuiaDespacho())
                .hasFieldOrPropertyWithValue("vehiculoDespacho", envioNuevo.getVehiculoDespacho())
                .hasFieldOrPropertyWithValue("ruta", envioNuevo.getRuta());
        verify(envioRepository, times(1)).save(envioExistente);

    }

    @Test
    public void putEnvio_EnvioDoesntExists_ReturnsRunTimeException() {
        Integer idExistente = 555;
        Envio envioPut = new Envio(1, 1, 1, LocalDate.now(), true, "En bodega", null, null, null);
        when(envioRepository.findById(idExistente)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Envio envioActualizado = envioService.actualizarEnvio(idExistente, envioPut);
        });

        assertEquals("No existe envio con esa id", exception.getMessage());
    }

    @Test
    public void patchEnvio_ReturnsRuntimeException() {
        Integer idNoExistente = 555;
        Envio envioPatchMock = new Envio(1, 1, 1, LocalDate.now(), true, "En bodega", null, null, null);
        when(envioRepository.existsById(idNoExistente)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Envio envioActualizado = envioService.parcharEnvio(idNoExistente, envioPatchMock);
        });

        assertEquals("El envio no existe con esa id :" + idNoExistente, exception.getMessage());
    }

    @Test
    public void patchEnvio_allAtributesNull_ReturnsRunTimeException() {
        Integer idNoExistente = 555;
        Envio envioPatchMock = new Envio(null, null, null, null, null, null, null, null, null);
        when(envioRepository.existsById(idNoExistente)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Envio envioActualizado = envioService.parcharEnvio(idNoExistente, envioPatchMock);
        });

        assertEquals("El objeto PATCH no contiene ningún atributo para actualizar.", exception.getMessage());
    }

    @Test
    public void patchEnvioSuccessful(){
        Integer idExistente = 1;
        Envio envioPatch = new Envio(null, 2, 2, LocalDate.now(), true, "En bodega", null, null, null);
        Envio envioExistente = new Envio(1, 1, 1, LocalDate.now(), false, "Desconocido", null, null, null);
        Optional<Envio> envioExistenteOptional = Optional.of(envioExistente);
        Envio envioParchado = envioMockPatch(envioExistente, envioPatch);
        when(envioRepository.existsById(idExistente)).thenReturn(true);
        when(envioRepository.findById(idExistente)).thenReturn(envioExistenteOptional);
        when(envioRepository.save(any(Envio.class))).thenReturn(envioParchado);

        Envio envioActualizado = envioService.parcharEnvio(idExistente, envioPatch);

        assertNotNull(envioActualizado);
        assertThat(envioActualizado)
                .hasFieldOrPropertyWithValue("idCliente", envioParchado.getIdCliente())
                .hasFieldOrPropertyWithValue("idOrden", envioParchado.getIdOrden())
                .hasFieldOrPropertyWithValue("fechaEntrega", envioParchado.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioParchado.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioParchado.getObservacion());
        verify(envioRepository, times(1)).save(any(Envio.class));

    }

    @Test
    public void patchEnvio_OnlyIdCliente_UpdatesIdCliente() {
        Integer idExistente = 1;
        Envio envioPatch = new Envio(null, 2, null, null, null, null, null, null, null); // Solo idCliente no nulo
        Envio envioExistente = new Envio(1, 1, 1, LocalDate.now(), false, "Desconocido", null, null, null);
        Optional<Envio> envioExistenteOptional = Optional.of(envioExistente);
        Envio envioParchado = envioMockPatch(envioExistente, envioPatch);
        when(envioRepository.existsById(idExistente)).thenReturn(true);
        when(envioRepository.findById(idExistente)).thenReturn(envioExistenteOptional);
        when(envioRepository.save(any(Envio.class))).thenReturn(envioParchado);

        Envio envioActualizado = envioService.parcharEnvio(idExistente, envioPatch);

        assertNotNull(envioActualizado);
        assertThat(envioActualizado)
                .hasFieldOrPropertyWithValue("idCliente", envioParchado.getIdCliente()) // Verifica que este se parcho
                .hasFieldOrPropertyWithValue("idOrden", envioExistente.getIdOrden()) // Verifica que no se parcho
                .hasFieldOrPropertyWithValue("fechaEntrega", envioExistente.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioExistente.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioExistente.getObservacion());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    // Test solo idOrden
    @Test
    public void patchEnvio_OnlyIdOrden_UpdatesIdOrden() {
        Integer idExistente = 1;
        Envio envioPatch = new Envio(null, null, 2, null, null, null, null, null, null); // Solo idOrden no nulo
        Envio envioExistente = new Envio(1, 1, 1, LocalDate.now(), false, "Desconocido", null, null, null);
        Optional<Envio> envioExistenteOptional = Optional.of(envioExistente);
        Envio envioParchado = envioMockPatch(envioExistente, envioPatch);
        when(envioRepository.existsById(idExistente)).thenReturn(true);
        when(envioRepository.findById(idExistente)).thenReturn(envioExistenteOptional);
        when(envioRepository.save(any(Envio.class))).thenReturn(envioParchado);

        Envio envioActualizado = envioService.parcharEnvio(idExistente, envioPatch);

        assertNotNull(envioActualizado);
        assertThat(envioActualizado)
                .hasFieldOrPropertyWithValue("idCliente", envioExistente.getIdCliente()) // Verifica que no se parcho
                .hasFieldOrPropertyWithValue("idOrden", envioParchado.getIdOrden()) // Verifica que si se parcho
                .hasFieldOrPropertyWithValue("fechaEntrega", envioExistente.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioExistente.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioExistente.getObservacion());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    // Test solo fechaEntrega
    @Test
    public void patchEnvio_OnlyFechaEntrega_UpdatesFechaEntrega() {
        Integer idExistente = 1;
        Envio envioPatch = new Envio(null, null, null, LocalDate.now().plusDays(5), null, null, null, null, null); // Solo
                                                                                                                   // fechaEntrega
                                                                                                                   // no
                                                                                                                   // nulo
        Envio envioExistente = new Envio(1, 1, 1, LocalDate.now(), false, "Desconocido", null, null, null);
        Optional<Envio> envioExistenteOptional = Optional.of(envioExistente);
        Envio envioParchado = envioMockPatch(envioExistente, envioPatch);
        when(envioRepository.existsById(idExistente)).thenReturn(true);
        when(envioRepository.findById(idExistente)).thenReturn(envioExistenteOptional);
        when(envioRepository.save(any(Envio.class))).thenReturn(envioParchado);

        Envio envioActualizado = envioService.parcharEnvio(idExistente, envioPatch);

        assertNotNull(envioActualizado);
        assertThat(envioActualizado)
                .hasFieldOrPropertyWithValue("idCliente", envioExistente.getIdCliente()) // Verifica que no se parcho
                .hasFieldOrPropertyWithValue("idOrden", envioExistente.getIdOrden()) // Verifica que no se parcho
                .hasFieldOrPropertyWithValue("fechaEntrega", envioPatch.getFechaEntrega()) // Verifica que si se parcho
                .hasFieldOrPropertyWithValue("entregado", envioExistente.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioExistente.getObservacion());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    // Test solo entregado
    @Test
    public void patchEnvio_OnlyEntregado_UpdatesEntregado() {
        Integer idExistente = 1;
        Envio envioPatch = new Envio(null, null, null, null, true, null, null, null, null); // Solo entregado no nulo
        Envio envioExistente = new Envio(1, 1, 1, LocalDate.now(), false, "Desconocido", null, null, null);
        Optional<Envio> envioExistenteOptional = Optional.of(envioExistente);
        Envio envioParchado = envioMockPatch(envioExistente, envioPatch);
        when(envioRepository.existsById(idExistente)).thenReturn(true);
        when(envioRepository.findById(idExistente)).thenReturn(envioExistenteOptional);
        when(envioRepository.save(any(Envio.class))).thenReturn(envioParchado);

        Envio envioActualizado = envioService.parcharEnvio(idExistente, envioPatch);

        assertNotNull(envioActualizado);
        assertThat(envioActualizado)
                .hasFieldOrPropertyWithValue("idCliente", envioExistente.getIdCliente())
                .hasFieldOrPropertyWithValue("idOrden", envioExistente.getIdOrden())
                .hasFieldOrPropertyWithValue("fechaEntrega", envioExistente.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioPatch.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioExistente.getObservacion());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    // Test solo observacion
    @Test
    public void patchEnvio_OnlyObservacion_UpdatesObservacion() {
        Integer idExistente = 1;
        Envio envioPatch = new Envio(null, null, null, null, null, "Nueva Observacion", null, null, null);
        Envio envioExistente = new Envio(1, 1, 1, LocalDate.now(), false, "Desconocido", null, null, null);
        Optional<Envio> envioExistenteOptional = Optional.of(envioExistente);
        Envio envioParchado = envioMockPatch(envioExistente, envioPatch);
        when(envioRepository.existsById(idExistente)).thenReturn(true);
        when(envioRepository.findById(idExistente)).thenReturn(envioExistenteOptional);
        when(envioRepository.save(any(Envio.class))).thenReturn(envioParchado);

        Envio envioActualizado = envioService.parcharEnvio(idExistente, envioPatch);

        assertNotNull(envioActualizado);
        assertThat(envioActualizado)
                .hasFieldOrPropertyWithValue("idCliente", envioExistente.getIdCliente())
                .hasFieldOrPropertyWithValue("idOrden", envioExistente.getIdOrden())
                .hasFieldOrPropertyWithValue("fechaEntrega", envioExistente.getFechaEntrega())
                .hasFieldOrPropertyWithValue("entregado", envioExistente.getEntregado())
                .hasFieldOrPropertyWithValue("observacion", envioPatch.getObservacion());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }
    // Faltan todas las combinaciones.... pero por tema academico no las hare!!!

    @Test
    public void searchEnvioPorRangoDeFechaSuccessful() {

        List<Envio> enviosMock = new ArrayList<>();
        LocalDate fechaInicial = LocalDate.of(2025, 1, 1);
        LocalDate fechaFinal = LocalDate.of(2025, 12, 12);
        Envio envioMock1 = new Envio(1, 1, 1, fechaInicial.minusYears(15), true, "En bodega", null, null, null);
        Envio envioMock2 = new Envio(2, 2, 2, fechaInicial.minusYears(2), false, "En ruta", null, null, null);
        Envio envioMock3 = new Envio(3, 3, 3, fechaFinal.minusMonths(1), true, "Entregado", null, null, null);
        Envio envioMock4 = new Envio(4, 4, 4, fechaFinal.minusDays(7), false, "Retrasado", null, null, null);
        enviosMock = List.of(envioMock1, envioMock2, envioMock3, envioMock4);
        List<Envio> enviosFiltrados = enviosMock.stream()
                .filter(envio -> (envio.getFechaEntrega().isAfter(fechaInicial) || envio.getFechaEntrega().isEqual(fechaInicial)) &&
                                 (envio.getFechaEntrega().isBefore(fechaFinal) || envio.getFechaEntrega().isEqual(fechaFinal)))
                .collect(Collectors.toList());

        when(envioRepository.buscarPorRangoDeFecha(fechaInicial, fechaFinal)).thenReturn(enviosFiltrados);

        List<Envio> enviosEncontrados = envioService.buscarEnvioPorRangoDeFecha(fechaInicial, fechaFinal);

        assertNotNull(enviosEncontrados);
        assertEquals(2, enviosEncontrados.size());

    }

    @Test
    public void deleteEnvio_NonExistingId_ReturnsRuntimeException(){
        Integer idNoExistente = 919;

        when(envioRepository.findById(idNoExistente)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            envioService.eliminarEnvio(idNoExistente);
        });

        assertEquals("No existe envio con esa id", exception.getMessage());
    }

    @Test
    public void deleteEnvioSuccesful() {
        Integer idExistente = 1;
        Envio envioMock = new Envio(1, 1, 1, LocalDate.now(), true, "En bodega", null, null, null);
        Optional<Envio> envioMockOptional = Optional.of(envioMock);
        when(envioRepository.findById(idExistente)).thenReturn(envioMockOptional);

        envioService.eliminarEnvio(idExistente);

        verify(envioRepository,times(1)).deleteById(idExistente);

    }

    // Metodo que simula un put
    public Envio envioMockPut(Envio envioExistente, Envio envioPut) {
        Envio envioActualizado = envioExistente;
        envioActualizado.setIdEnvio(envioPut.getIdEnvio());
        envioActualizado.setIdOrden(envioPut.getIdOrden());

        return envioActualizado;
    }

    // Metodo que simula un patch
    public Envio envioMockPatch(Envio envioExistente, Envio envioPatch) {
        Envio envioActualizado = envioExistente;
        if (envioPatch.getIdCliente() == null &&
                envioPatch.getIdOrden() == null &&
                envioPatch.getFechaEntrega() == null &&
                envioPatch.getEntregado() == null &&
                envioPatch.getObservacion() == null) {
            System.out.println("Patch invalido");
            return null;
        }

        if (envioPatch.getIdCliente() != null) {
            envioActualizado.setIdCliente(envioPatch.getIdCliente());
        }

        if (envioPatch.getIdOrden() != null) {
            envioActualizado.setIdOrden(envioPatch.getIdOrden());
        }

        if (envioPatch.getFechaEntrega() != null) {
            envioActualizado.setFechaEntrega(envioPatch.getFechaEntrega());
        }

        if (envioPatch.getEntregado() != null) {
            envioActualizado.setEntregado(envioPatch.getEntregado());
        }

        if (envioPatch.getObservacion() != null) {
            envioActualizado.setObservacion(envioPatch.getObservacion());
        }

        return envioActualizado;

    }

}
