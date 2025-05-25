package com.perfulandia.cl.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.Ruta;

import feign.Param;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    
    @Query(value = "SELECT * FROM ruta WHERE coord_x_final BETWEEN :x_1 AND :x_2 AND coord_y_final BETWEEN y_1 AND y_2", nativeQuery = true)
    List<Ruta> buscarPorRangoDeFecha(
            @Param("x_1") Float x_1,
            @Param("x_2") Float x_2,
            @Param("y_1") Float y_1,
            @Param("y_2") Float y_2);

}
