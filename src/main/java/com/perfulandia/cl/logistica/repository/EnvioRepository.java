package com.perfulandia.cl.logistica.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.Envio;

import feign.Param;

@Repository
public interface EnvioRepository extends JpaRepository<Envio,Integer>{

    @Query(value = "SELECT * FROM envio WHERE fecha_entrega BETWEEN :fechainicial AND :fechafinal",nativeQuery = true)
    List<Envio> buscarPorRangoDeFecha(@Param("fechainicial") LocalDate fechainicial ,@Param("fechafinal") LocalDate fechafinal);
}
