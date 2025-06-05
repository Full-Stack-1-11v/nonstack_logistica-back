package com.perfulandia.cl.logistica;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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

    // Debiese hacer los arrange dentro de cada test!, pero estoy aprendiendo...
    // proximos hago
    // arrange adentro de cada test
    private List<VehiculoDespacho> vehiculosMock = new ArrayList<>();
    private List<Envio> enviosMock = new ArrayList<>();
    private VehiculoDespacho vehiculoRegistrar = new VehiculoDespacho(1, "BB-11", 2023, enviosMock);
    private VehiculoDespacho vehiculoParaActualizar = new VehiculoDespacho(1, "BB-11", 2020, enviosMock);

    @BeforeEach
    void setUp() {
        vehiculosMock.add(new VehiculoDespacho(1, "AA-11", 2000, enviosMock));
        vehiculosMock.add(new VehiculoDespacho(2, "AA-22", 2023, enviosMock));
        vehiculosMock.add(new VehiculoDespacho(3, "BB-11", 2023, enviosMock));
        vehiculosMock.add(new VehiculoDespacho(4, "BB-11", 2023, enviosMock));
    }

    @Test
    @DisplayName("verVehiculosDespachos debe de devolver algo, desde 0 hasta infinito como tamaño de lista")
    public void findAll() {

        when(vehiculoDespachoRepository.findAll()).thenReturn(vehiculosMock);

        List<VehiculoDespacho> vehiculos = vehiculoDespachoService.verVehiculosDespachos();

        assertNotNull(vehiculos);
        assertEquals(4, vehiculos.size());

    }

    @Test
    @DisplayName("registrarVehiculoDespacho devuelve nulo si hay un vehiculo que ya existe")
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
    @DisplayName("registrarVehiculoDespacho no es nulo si no hay un vehiculo que ya existe")
    public void registerVehiculoReturnsNotNullIfNotExists() {
        when(vehiculoDespachoRepository.findByPatente(vehiculoRegistrar.getPatente())).thenReturn(null);

        VehiculoDespacho vehiculoRegistrado = vehiculoDespachoService.registrarVehiculoDespacho(vehiculoRegistrar);

        assertNotNull(vehiculoRegistrado);
        assertEquals(vehiculoRegistrado, vehiculoRegistrar);
    }

    @Test
    @DisplayName("actualizarVehiculoDespacho es nulo si no hay un vehiculo con esa patente")
    public void putVehiculoReturnsNullIfNotExists() {
        when(vehiculoDespachoRepository.findByPatente(vehiculoRegistrar.getPatente())).thenReturn(null);

        VehiculoDespacho vehiculoExistente = vehiculoDespachoService.actualizarVehiculoDespacho(vehiculoRegistrar,
                "BB-11");

        assertNull(vehiculoExistente);
    }

    @Test
    @DisplayName("actualizarVehiculoDespacho no es nulo y tiene mismo año si hay un vehiculo con esa patente")
    public void putVehiculoReturnsNotNullIfExists() {
        when(vehiculoDespachoRepository.findByPatente(vehiculoParaActualizar.getPatente()))
                .thenReturn(vehiculoRegistrar);

        VehiculoDespacho vehiculoExistente = vehiculoDespachoService.actualizarVehiculoDespacho(vehiculoParaActualizar,
                vehiculoParaActualizar.getPatente());
        vehiculoExistente.setAno(vehiculoParaActualizar.getAno());

        when(vehiculoDespachoRepository.save(vehiculoExistente)).thenReturn(vehiculoExistente);

        assertNotNull(vehiculoExistente);
        assertNotNull(vehiculoParaActualizar);
        assertEquals(vehiculoExistente.getAno(), vehiculoRegistrar.getAno());
    }

    @Test
    @DisplayName("parcharVehiculoDespacho arroja RuntimeException si no existe vehiculo con esa patente")
    public void patchVehiculoReturnsRutimeExceptionOne() {
        when(vehiculoDespachoRepository.existsByPatente(vehiculoParaActualizar.getPatente())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> { // Que linea me va a tirar un
                                                                                  // RunTimeException
            vehiculoDespachoService.parcharVehiculoDespacho(vehiculoParaActualizar,
                    vehiculoParaActualizar.getPatente());
        });

        // Assert
        assertEquals("Vehiculo con la patente : " + vehiculoParaActualizar.getPatente() + " no existe.",
                exception.getMessage());
    }

    @Test
    @DisplayName("parcharVehiculoDespacho arroja RuntimeException si el objeto nuevo no tiene ni patente ni año")
    public void patchVehiculoReturnsRunTimeExceptionTwo() {
        when(vehiculoDespachoRepository.existsByPatente("BB-11")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vehiculoParaActualizar.setPatente(null);
            vehiculoParaActualizar.setAno(null);
            vehiculoDespachoService.parcharVehiculoDespacho(vehiculoParaActualizar,
                    "BB-11");
        });

        // Assert
        assertEquals(exception.getMessage(), "El objeto debe tener al menos un atributo : patente y/o año");

    }

    @Test
    @DisplayName("parcharVehiculoDespacho devuelve objeto parchado con año actualizada")
    public void patchVehiculoSoloAno() {

        String patenteExistente = "BB-11";
        VehiculoDespacho vehiculoExistente = new VehiculoDespacho(1, patenteExistente, 2020, enviosMock);
        VehiculoDespacho vehiculoConNuevosDatos = new VehiculoDespacho();
        vehiculoConNuevosDatos.setAno(2023);

        when(vehiculoDespachoRepository.existsByPatente(patenteExistente)).thenReturn(true);
        when(vehiculoDespachoRepository.findByPatente(patenteExistente)).thenReturn(vehiculoExistente);
        when(vehiculoDespachoRepository.save(vehiculoExistente)).thenReturn(vehiculoExistente);

        try {
            VehiculoDespacho vehiculoParchado = vehiculoDespachoService.parcharVehiculoDespacho(vehiculoConNuevosDatos,
                    patenteExistente);

            // Assert
            assertNotNull(vehiculoParchado);
            assertEquals(2023, vehiculoParchado.getAno());
            assertEquals(patenteExistente, vehiculoParchado.getPatente());
        } catch (Exception e) {
            fail("No se esperaba una excepcion, pero igual ocurrio..." + e.getMessage());
        }

    }

    @Test
    @DisplayName("parcharVehiculoDespacho devuelve objeto parchado con patente actualizada")
    public void patchVehiculoSoloPatente() {

        String patenteExistente = "BB-11";
        VehiculoDespacho vehiculoExistente = new VehiculoDespacho(1, patenteExistente, 2020, enviosMock);
        VehiculoDespacho vehiculoConNuevosDatos = new VehiculoDespacho();
        vehiculoConNuevosDatos.setAno(null);
        vehiculoConNuevosDatos.setPatente("AA-11");

        when(vehiculoDespachoRepository.existsByPatente(patenteExistente)).thenReturn(true);
        when(vehiculoDespachoRepository.findByPatente(patenteExistente)).thenReturn(vehiculoExistente);
        when(vehiculoDespachoRepository.save(vehiculoExistente)).thenReturn(vehiculoExistente);

        try {
            VehiculoDespacho vehiculoParchado = vehiculoDespachoService.parcharVehiculoDespacho(vehiculoConNuevosDatos,
                    patenteExistente);

            // Assert
            assertNotNull(vehiculoParchado);
            assertEquals("AA-11", vehiculoParchado.getPatente());
        } catch (Exception e) {
            fail("No se esperaba una excepcion, pero igual ocurrio..." + e.getMessage());
        }

    }

    @Test
    @DisplayName("parcharVehiculoDespacho devuelve objeto parchado con año y patente actualizadas")
    public void patchVehiculoFull() { // Trae anio y patente

        String patenteExistente = "BB-11";
        VehiculoDespacho vehiculoExistente = new VehiculoDespacho(1, patenteExistente, 2020, enviosMock);
        VehiculoDespacho vehiculoConNuevosDatos = new VehiculoDespacho();
        vehiculoConNuevosDatos.setAno(2025);
        vehiculoConNuevosDatos.setPatente("AA-11");

        when(vehiculoDespachoRepository.existsByPatente(patenteExistente)).thenReturn(true);
        when(vehiculoDespachoRepository.findByPatente(patenteExistente)).thenReturn(vehiculoExistente);
        when(vehiculoDespachoRepository.save(vehiculoExistente)).thenReturn(vehiculoExistente);

        try {
            VehiculoDespacho vehiculoParchado = vehiculoDespachoService.parcharVehiculoDespacho(vehiculoConNuevosDatos,
                    patenteExistente);

            // Assert
            assertNotNull(vehiculoParchado);
            assertEquals("AA-11", vehiculoParchado.getPatente());
            assertEquals(2025, vehiculoParchado.getAno());
        } catch (Exception e) {
            fail("No se esperaba una excepcion, pero igual ocurrio..." + e.getMessage());
        }

    }

    @Test
    @DisplayName("buscarVehiculoPorPatronPatente arroja RuntimeException si el patron de patente no es de dos caracteres")
    public void searchVehiculoLenghtLessThanTwo() {
        // Arrange
        String patenteCorta = "A";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            List<VehiculoDespacho> vehiculosEncontrados = vehiculoDespachoService
                    .buscarVehiculoPorPatronPatente(patenteCorta);
        });

        assertEquals("Se deben de colocar dos valores alfanumericos como patron inicial", exception.getMessage());
    }

    @Test
    @DisplayName("buscarVehiculoPorPatronPatente devuelve una lista de elementos")
    public void searchVehiculoSuccessful(){
        // Arrange
        String patenteCorrecta = "BB";
        List<VehiculoDespacho> vehiculosResultantes = vehiculosMock.
                stream()
                .filter(vehiculo -> "BB-11".equals(vehiculo.getPatente()))
                .collect(Collectors.toList());
        
        when(vehiculoDespachoRepository.buscarPorPatronPatente(patenteCorrecta)).thenReturn(vehiculosResultantes);


        // Act
        try {
            List<VehiculoDespacho> vehiculosEncontrados = vehiculoDespachoService.buscarVehiculoPorPatronPatente(patenteCorrecta);

            assertNotNull(vehiculosEncontrados);
            assertEquals(2, vehiculosEncontrados.size());
        } catch (Exception e) {
           fail("No se esperaba una excepcion, pero igual ocurrio..." + e.getMessage());
        }
        

    }

    @Test
    @DisplayName("borrarVehiculoDespacho arroja RuntimeException si patente no existe")
    public void deleteVehiculoPatenteDoesntExist(){
        // Arrange
        String patenteNoExistente = "UGABUGA-69";
        when(vehiculoDespachoRepository.existsByPatente(patenteNoExistente)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vehiculoDespachoService.borrarVehiculoDespacho(patenteNoExistente);
        });

        assertEquals("Vehiculo con la patente + " + patenteNoExistente + " no existe", exception.getMessage());
    }

    @Test
    @DisplayName("borrarVehiculoDespacho se ejecuta si la patente existe")
    public void deleteVehiculoSuccessful(){
        // Arrange
        String patenteExistente = "BB-11";
        when(vehiculoDespachoRepository.existsByPatente(patenteExistente)).thenReturn(true);

        try {
            vehiculoDespachoService.borrarVehiculoDespacho(patenteExistente);
        } catch (Exception e) {
            fail("No se esperaba una excepcion, pero igual ocurrio..." + e.getMessage());
        }
    }
}
