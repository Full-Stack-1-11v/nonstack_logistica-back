package com.perfulandia.cl.logistica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
    public void getRutasByCoordenadas_RetornaLista() {
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

        try {
            List<Ruta> rutasEncontradas = rutaService.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2);
            assertNotNull(rutasEncontradas);
            assertEquals(1, rutasEncontradas.size());
        } catch (Exception e) {
            System.out.println("Error que no debiese ocurrir : " + e.getMessage());
        }

    }

    @Test
    public void getRutasByCoordenadas_CoordenadaNula_ReturnsNull() {
        Float x_1 = -71.0F;
        Float x_2 = -70.0F;
        Float y_1 = null;
        Float y_2 = null;

        when(rutaRepository.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2)).thenReturn(new ArrayList<>());

        try {
            List<Ruta> rutasEncontradas = rutaService.buscarRutasPorCoordenadas(x_1, x_2, y_1, y_2);
            assertNotNull(rutasEncontradas);
            assertEquals(0, rutasEncontradas.size());
        } catch (Exception e) {
            System.out.println("Error que no debiese ocurrir : " + e.getMessage());
        }

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
    public void putRuta_InvalidIdReturnsException(){
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
        when(rutaRepository.existsById(idRutaMock)).thenReturn(true);

        try {
            Ruta putRuta = rutaService.putRuta(rutaMock, idRutaMock);
            assertNotNull(putRuta);
            assertThat(putRuta)
                .hasFieldOrPropertyWithValue("idRuta", rutaMock.getIdRuta())
                .hasFieldOrPropertyWithValue("coordXInicio", rutaMock.getCoordXInicio())
                .hasFieldOrPropertyWithValue("coordYInicio", rutaMock.getCoordYInicio())
                .hasFieldOrPropertyWithValue("coordXFinal", rutaMock.getCoordXFinal())
                .hasFieldOrPropertyWithValue("coordYFinal", rutaMock.getCoordYFinal());
        } catch (Exception e) {
             System.out.println("Error que no debiese ocurrir : " + e.getMessage());
        }
    }

    @Test
    public void patchRuta_InvalidIdReturnsException(){
        Integer idRutaMock = null;
        Ruta patchRutaMock = new Ruta(1, 22F, 25F, 21F, 15F, null);
        when(rutaRepository.existsById(idRutaMock)).thenThrow(new IllegalArgumentException("Id no puede ser nulo"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            Ruta patchRuta = rutaService.parcharRuta(patchRutaMock, idRutaMock);
        });

        assertEquals("Id no puede ser nulo", exception.getMessage());
    }

    @Test
    public void patchRuta_IdNotFoundReturnsException(){
        Integer idRutaMock = 33;
        when(rutaRepository.existsById(idRutaMock)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, ()->{
            Ruta patchRuta = rutaService.parcharRuta(rutaMock, idRutaMock);
        });

        assertEquals("No existe la ruta con id: " + idRutaMock, exception.getMessage());
    }




}
