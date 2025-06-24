package com.perfulandia.cl.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.Ruta;

import feign.Param;

/**
 * Repositorio de Spring Data JPA para la entidad {@link Ruta}.
 * Proporciona métodos CRUD estándar y consultas personalizadas para operaciones con rutas.
 */
@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    
    /**
     * Busca y devuelve una lista de rutas cuyas coordenadas finales se encuentran dentro de un área rectangular definida.
     *
     * @param x_1 La coordenada X mínima del área de búsqueda.
     * @param x_2 La coordenada X máxima del área de búsqueda.
     * @param y_1 La coordenada Y mínima del área de búsqueda.
     * @param y_2 La coordenada Y máxima del área de búsqueda.
     * @return Una lista de entidades {@link Ruta} que coinciden con el criterio de búsqueda.
     */
    @Query(value = "SELECT * FROM ruta WHERE coord_x_final BETWEEN :x_1 AND :x_2 AND coord_y_final BETWEEN :y_1 AND :y_2", nativeQuery = true)
    List<Ruta> buscarRutasPorCoordenadas(
            @Param("x_1") Float x_1,
            @Param("x_2") Float x_2,
            @Param("y_1") Float y_1,
            @Param("y_2") Float y_2);

}
