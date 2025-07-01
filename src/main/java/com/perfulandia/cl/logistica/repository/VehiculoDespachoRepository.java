package com.perfulandia.cl.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.VehiculoDespacho;

import feign.Param;

/**
 * Repositorio de Spring Data JPA para la entidad {@link VehiculoDespacho}.
 * Proporciona métodos CRUD estándar y consultas personalizadas para operaciones con vehículos de despacho.
 */
@Repository
public interface VehiculoDespachoRepository extends JpaRepository<VehiculoDespacho,Integer>{

    /**
     * Busca un vehículo de despacho por su patente.
     * @param patente La patente del vehículo a buscar.
     * @return El {@link VehiculoDespacho} encontrado, o null si no existe.
     */
    VehiculoDespacho findByPatente(String patente);

    /**
     * Verifica si existe un vehículo de despacho con la patente especificada.
     * @param patente La patente a verificar.
     * @return true si el vehículo existe, false en caso contrario.
     */
    boolean existsByPatente(String patente);

    /**
     * Elimina un vehículo de despacho por su patente.
     * @param patente La patente del vehículo a eliminar.
     */
    void deleteByPatente(String patente);

    /**
     * Busca vehículos cuya patente comience con un patrón específico.
     * @param patron El patrón de inicio de la patente a buscar.
     * @return Una lista de entidades {@link VehiculoDespacho} que coinciden con el patrón.
     */
    @Query(value = "SELECT * FROM vehiculo_despacho WHERE patente LIKE :patron%" , nativeQuery = true)
    List<VehiculoDespacho> buscarPorPatronPatente(@Param("patron") String patron);
}
