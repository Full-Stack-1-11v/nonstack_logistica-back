package com.perfulandia.cl.logistica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.perfulandia.cl.logistica.model.Ruta;
import com.perfulandia.cl.logistica.repository.RutaRepository;
import com.perfulandia.cl.logistica.service.RutaService;

@SpringBootTest
public class RutaServiceTest {

    @Autowired
    RutaService rutaService;

    @MockitoBean
    RutaRepository rutaRepository;

    private List<Ruta> rutasMock = new ArrayList<>();
    private Ruta rutaMock = new Ruta(1, 15F, 15F, 15F, 15F, null);

    @BeforeEach
    public void setUp() {
        rutasMock.add(new Ruta(1, 15.12F, 15.1F, 15.2F, 15.3F, null));
        rutasMock.add(new Ruta(2, 25F, 5F, 15F, 15F, null));
        rutasMock.add(new Ruta(3, 35F, -5F, 15F, 15F, null));
        rutasMock.add(new Ruta(4, -71.0F, -34.0F, -70.0F, -33.0F, null));
    }

    @Test
    public void getAllRutas() {

        when(rutaRepository.findAll()).thenReturn(rutasMock);

        List<Ruta> rutasEncontradas = rutaService.getAllRutas();

        assertNotNull(rutasEncontradas);
        assertEquals(4, rutasEncontradas.size());

    }

    @Test
    public void getRutasByCoordenadas_RetornaLista() throws Exception {
        Float x_1 = -71.0F;
        Float x_2 = -70.0F;
        Float y_1 = -34.0F;
        Float y_2 = -33.0F;

        double epsilon = 0.0001; // Margen de error para valores flotantes

        List<Ruta> rutasFiltradas = rutasMock.stream()
                .filter(ruta -> Math.abs(ruta.getCoordXInicio() - x_1) < epsilon &&
                        Math.abs(ruta.getCoordXFinal() - x_2) < epsilon &&
                        Math.abs(ruta.getCoordYInicio() - y_1) < epsilon &&
                        Math.abs(ruta.getCoordYFinal() - y_2) < epsilon)
                .collect(Collectors.toList());

        when(rutaRepository.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2)).thenReturn(rutasFiltradas);

        List<Ruta> rutasEncontradas = rutaService.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2);
        assertNotNull(rutasEncontradas);
        assertEquals(1, rutasEncontradas.size());

    }

    @Test
    public void getRutasByCoordenadas_CoordenadaNula_ReturnsNull() throws Exception {
        Float x_1 = -71.0F;
        Float x_2 = -70.0F;
        Float y_1 = null;
        Float y_2 = null;

        when(rutaRepository.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2)).thenReturn(new ArrayList<>());

        List<Ruta> rutasEncontradas = rutaService.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2);
        assertNotNull(rutasEncontradas);
        assertEquals(0, rutasEncontradas.size());

    }

    @Test
    public void createRuta_ReturnsRuta() {

        when(rutaRepository.save(rutaMock)).thenReturn(rutaMock);

        Ruta rutaCreada = rutaService.crearRuta(rutaMock);

        assertNotNull(rutaCreada);
        assertThat(rutaCreada)
                .hasFieldOrPropertyWithValue("idRuta", rutaMock.getIdRuta())
                .hasFieldOrPropertyWithValue("coordXInicio", rutaMock.getCoordXInicio())
                .hasFieldOrPropertyWithValue("coordYInicio", rutaMock.getCoordYInicio())
                .hasFieldOrPropertyWithValue("coordXFinal", rutaMock.getCoordXFinal())
                .hasFieldOrPropertyWithValue("coordYFinal", rutaMock.getCoordYFinal());

    }

    @Test
    public void putRuta_ReturnsException() {
        Integer idRutaMock = 1;
        when(rutaRepository.existsById(idRutaMock)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Ruta putRuta = rutaService.putRuta(rutaMock, idRutaMock); // Intentamos hacer un put
        });

        assertEquals("No existe la ruta con id: " + idRutaMock, exception.getMessage());
    }

    @Test
    public void putRuta_InvalidIdReturnsException() {
        Integer idRutaMock = null;
        when(rutaRepository.existsById(idRutaMock)).thenThrow(new IllegalArgumentException("Id no puede ser nulo"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Ruta putRuta = rutaService.putRuta(rutaMock, idRutaMock); // Intentamos hacer un put
        });

        assertEquals("Id no puede ser nulo", exception.getMessage());
    }

    @Test
    public void putRutaSuccessful() {
        Integer idRutaMock = 1;
        Ruta putRutaMock = new Ruta(idRutaMock, -25F, -30F, -35F, -40F, null); // id no importa para el put
        Optional<Ruta> optionalRutaMock = Optional.of(rutaMock);
        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);

        // Aqui debo de invocar al argumento de save, asi accedo al argumento y simulo
        // que le entrego los datos!!!!
        // Es necesario hacer esto?
        when(rutaRepository.save(any(Ruta.class))).thenAnswer(invocation -> {
            Ruta rutaActualizada = invocation.getArgument(0);
            rutaActualizada.setCoordXInicio(putRutaMock.getCoordXInicio());
            rutaActualizada.setCoordYInicio(putRutaMock.getCoordYInicio());
            rutaActualizada.setCoordXFinal(putRutaMock.getCoordXFinal());
            rutaActualizada.setCoordYFinal(putRutaMock.getCoordYFinal());
            return rutaActualizada;
        });

        Ruta putRuta = rutaService.putRuta(putRutaMock, idRutaMock);
        assertNotNull(putRuta);
        assertThat(putRuta)
                .hasFieldOrPropertyWithValue("coordXInicio", putRutaMock.getCoordXInicio())
                .hasFieldOrPropertyWithValue("coordYInicio", putRutaMock.getCoordYInicio())
                .hasFieldOrPropertyWithValue("coordXFinal", putRutaMock.getCoordXFinal())
                .hasFieldOrPropertyWithValue("coordYFinal", putRutaMock.getCoordYFinal());

    }

    @Test
    public void patchRuta_InvalidIdReturnsException() {
        Integer idRutaMock = null;
        Ruta patchRutaMock = new Ruta(1, 22F, 25F, 21F, 15F, null);
        when(rutaRepository.existsById(idRutaMock)).thenThrow(new IllegalArgumentException("Id no puede ser nulo"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Ruta patchRuta = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        });

        assertEquals("Id no puede ser nulo", exception.getMessage());
    }

    @Test
    public void patchRuta_IdNotFoundReturnsException() {
        Integer idRutaMock = 33;
        when(rutaRepository.existsById(idRutaMock)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Ruta patchRuta = rutaService.parcharRuta(rutaMock, idRutaMock);
        });

        assertEquals("No existe la ruta con id: " + idRutaMock, exception.getMessage());
    }

    @Test
    public void patchRutSuccessful() {
        Integer idRutaMock = 1;
        Ruta rutaExistente = new Ruta(1, 22F, 33F, 21F, 15F, null);
        Ruta patchRutaMock = new Ruta(1, null, null, 21F, 15F, null);
        Optional<Ruta> optionalRutaMock = Optional.of(rutaExistente);

        // Simulo un parchamiento
        Ruta patchedRutaMock = patchMock(rutaExistente, patchRutaMock); // la combinacion de ambos para pasarlo en el
                                                                        // thenAnswer

        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);
        when(rutaRepository.save(any(Ruta.class))).thenAnswer(invocation -> {
            Ruta rutaActualizada = patchedRutaMock;
            return rutaActualizada; // Devolvemos la simulacion del patch

        });

        Ruta rutaParchada = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        assertNotNull(rutaParchada);
        assertThat(rutaParchada)
                .hasFieldOrPropertyWithValue("coordXInicio", patchedRutaMock.getCoordXInicio())
                .hasFieldOrPropertyWithValue("coordXFinal", patchedRutaMock.getCoordXFinal())
                .hasFieldOrPropertyWithValue("coordYInicio", patchedRutaMock.getCoordYInicio())
                .hasFieldOrPropertyWithValue("coordYFinal", patchedRutaMock.getCoordYFinal());
        assertEquals(rutaParchada, patchedRutaMock);

    }

    @Test
    public void patchRutSuccessful_coordXInicio() {
        Integer idRutaMock = 1;
        Ruta rutaExistente = new Ruta(1, 22F, 33F, 21F, 15F, null);
        Ruta patchRutaMock = new Ruta(1, 44F, null, null, null, null); // Solo coordXInicio no nulo
        Optional<Ruta> optionalRutaMock = Optional.of(rutaExistente);

        // Simulo un parchamiento
        Ruta patchedRutaMock = patchMock(rutaExistente, patchRutaMock);

        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);
        when(rutaRepository.save(any(Ruta.class))).thenReturn(patchedRutaMock);

        Ruta rutaParchada = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        assertNotNull(rutaParchada);
        assertThat(rutaParchada)
                .hasFieldOrPropertyWithValue("coordXInicio", patchedRutaMock.getCoordXInicio());
        assertEquals(rutaParchada, patchedRutaMock);
    }

    @Test
    public void patchRutSuccessful_coordYInicio() {
        Integer idRutaMock = 1;
        Ruta rutaExistente = new Ruta(1, 22F, 33F, 21F, 15F, null);
        Ruta patchRutaMock = new Ruta(1, null, 44F, null, null, null); // Solo coordYInicio no nulo
        Optional<Ruta> optionalRutaMock = Optional.of(rutaExistente);

        // Simulo un parchamiento
        Ruta patchedRutaMock = patchMock(rutaExistente, patchRutaMock);

        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);
        when(rutaRepository.save(any(Ruta.class))).thenReturn(patchedRutaMock);

        Ruta rutaParchada = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        assertNotNull(rutaParchada);
        assertThat(rutaParchada)
                .hasFieldOrPropertyWithValue("coordYInicio", patchedRutaMock.getCoordYInicio());
        assertEquals(rutaParchada, patchedRutaMock);
    }

    @Test
    public void patchRutSuccessful_coordXFinal() {
        Integer idRutaMock = 1;
        Ruta rutaExistente = new Ruta(1, 22F, 33F, 21F, 15F, null);
        Ruta patchRutaMock = new Ruta(1, null, null, 44F, null, null); // Solo coordXFinal no nulo
        Optional<Ruta> optionalRutaMock = Optional.of(rutaExistente);

        // Simulo un parchamiento
        Ruta patchedRutaMock = patchMock(rutaExistente, patchRutaMock);

        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);
        when(rutaRepository.save(any(Ruta.class))).thenReturn(patchedRutaMock);

        Ruta rutaParchada = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        assertNotNull(rutaParchada);
        assertThat(rutaParchada)
                .hasFieldOrPropertyWithValue("coordXFinal", patchedRutaMock.getCoordXFinal());
        assertEquals(rutaParchada, patchedRutaMock);

    }

    @Test
    public void patchRutSuccessful_coordYFinal() {
        Integer idRutaMock = 1;
        Ruta rutaExistente = new Ruta(1, 22F, 33F, 21F, 15F, null);
        Ruta patchRutaMock = new Ruta(1, null, null, null, 44F, null); // Solo coordYFinal no nulo
        Optional<Ruta> optionalRutaMock = Optional.of(rutaExistente);

        // Simulo un parchamiento
        Ruta patchedRutaMock = patchMock(rutaExistente, patchRutaMock);

        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);
        when(rutaRepository.save(any(Ruta.class))).thenReturn(patchedRutaMock);

        Ruta rutaParchada = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        assertNotNull(rutaParchada);
        assertThat(rutaParchada)
                .hasFieldOrPropertyWithValue("coordYFinal", patchedRutaMock.getCoordYFinal());
        assertEquals(rutaParchada, patchedRutaMock);
    }

    @Test
    public void patchRutSuccessful_allCoords() {
        Integer idRutaMock = 1;
        Ruta rutaExistente = new Ruta(1, 22F, 33F, 21F, 15F, null);
        Ruta patchRutaMock = new Ruta(1, 44F, 55F, 66F, 77F, null); // Todas las coordenadas no nulas
        Optional<Ruta> optionalRutaMock = Optional.of(rutaExistente);

        // Simulo un parchamiento
        Ruta patchedRutaMock = patchMock(rutaExistente, patchRutaMock);

        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);
        when(rutaRepository.save(any(Ruta.class))).thenReturn(patchedRutaMock);

        Ruta rutaParchada = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        assertNotNull(rutaParchada);
        assertThat(rutaParchada)
                .hasFieldOrPropertyWithValue("coordXInicio", patchedRutaMock.getCoordXInicio())
                .hasFieldOrPropertyWithValue("coordYInicio", patchedRutaMock.getCoordYInicio())
                .hasFieldOrPropertyWithValue("coordXFinal", patchedRutaMock.getCoordXFinal())
                .hasFieldOrPropertyWithValue("coordYFinal", patchedRutaMock.getCoordYFinal());
        assertEquals(rutaParchada, patchedRutaMock);
    }

    @Test
    public void patchRutSuccessful_noCoords() {
        Integer idRutaMock = 1;
        Ruta rutaExistente = new Ruta(1, 22F, 33F, 21F, 15F, null);
        Ruta patchRutaMock = new Ruta(1, null, null, null, null, null); // Todas las coordenadas nulas
        Optional<Ruta> optionalRutaMock = Optional.of(rutaExistente);

        // Simulo un parchamiento
        Ruta patchedRutaMock = patchMock(rutaExistente, patchRutaMock);

        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);
        when(rutaRepository.findById(idRutaMock)).thenReturn(optionalRutaMock);
        when(rutaRepository.save(any(Ruta.class))).thenReturn(patchedRutaMock);

        Ruta rutaParchada = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        assertNotNull(rutaParchada);
        // Add assertions to verify that no coordinates were changed
        assertThat(rutaParchada)
                .hasFieldOrPropertyWithValue("coordXInicio", patchedRutaMock.getCoordXInicio())
                .hasFieldOrPropertyWithValue("coordYInicio", patchedRutaMock.getCoordYInicio())
                .hasFieldOrPropertyWithValue("coordXFinal", patchedRutaMock.getCoordXFinal())
                .hasFieldOrPropertyWithValue("coordYFinal", patchedRutaMock.getCoordYFinal());
        assertEquals(rutaParchada, patchedRutaMock);
    }

    @Test
    public void deleteRutaInvalidIdReturnsRuntimeException() {
        Integer idInvalidoMock = 444;
        when(rutaRepository.existsById(idInvalidoMock)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rutaService.deleteRuta(idInvalidoMock);
        });

        assertEquals("No existe la ruta con id: " + idInvalidoMock, exception.getMessage());

    }

    @Test
    public void deleteRuta_InvalidIdArgument_ReturnsRunTimeException() {

        when(rutaRepository.existsById(null)).thenThrow(new IllegalArgumentException("Id invalido"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rutaService.deleteRuta(null);
        });

        assertEquals("Id invalido", exception.getMessage());
    }

    @Test
    public void deleteRutaSuccessfull() {
        Integer idValido = 1;
        when(rutaRepository.existsById(idValido)).thenReturn(true);

        rutaService.deleteRuta(idValido);
        verify(rutaRepository, times(1)).deleteById(idValido);

    }

    // Metodos que me ayudan a simular patches //
    public Ruta patchMock(Ruta rutaExistente, Ruta patchRutaMock) {
        Ruta patchedRutaMock = rutaExistente;
        if (patchRutaMock.getCoordXFinal() != null) {
            patchedRutaMock.setCoordXFinal(patchRutaMock.getCoordXFinal());
        }
        if (patchRutaMock.getCoordXInicio() != null) {
            patchedRutaMock.setCoordXInicio(patchRutaMock.getCoordXInicio());
        }
        if (patchRutaMock.getCoordYFinal() != null) {
            patchedRutaMock.setCoordYFinal(patchRutaMock.getCoordYFinal());
        }
        if (patchRutaMock.getCoordYInicio() != null) {
            patchedRutaMock.setCoordYInicio(patchRutaMock.getCoordYInicio());
        }

        return patchedRutaMock;

    }
}
