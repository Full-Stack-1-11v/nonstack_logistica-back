package com.perfulandia.cl.logistica.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.Envio;

import feign.Param;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Envio}.
 * Proporciona métodos CRUD estándar y consultas personalizadas para operaciones con envíos.
 */
@Repository
public interface EnvioRepository extends JpaRepository<Envio,Integer>{

    /**
     * Busca y devuelve una lista de envíos cuya fecha de entrega se encuentra dentro de un rango de fechas especificado.
     *
     * @param fechainicial La fecha de inicio del rango (inclusiva).
     * @param fechafinal La fecha de fin del rango (inclusiva).
     * @return Una lista de entidades {@link Envio} que coinciden con el criterio de búsqueda.
     */
    @Query(value = "SELECT * FROM envio WHERE fecha_entrega BETWEEN :fechainicial AND :fechafinal",nativeQuery = true)
    List<Envio> buscarPorRangoDeFecha(@Param("fechainicial") LocalDate fechainicial ,@Param("fechafinal") LocalDate fechafinal);
}
