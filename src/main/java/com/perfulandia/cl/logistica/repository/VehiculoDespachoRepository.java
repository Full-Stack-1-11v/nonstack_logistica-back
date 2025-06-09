package com.perfulandia.cl.logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.VehiculoDespacho;

import feign.Param;

@Repository
public interface VehiculoDespachoRepository extends JpaRepository<VehiculoDespacho,Integer>{

    VehiculoDespacho findByPatente(String patente);
    boolean existsByPatente(String patente);
    void deleteByPatente(String patente);

    @Query(value = "SELECT * FROM vehiculo_despacho WHERE patente LIKE :patron%" , nativeQuery = true)
    List<VehiculoDespacho> buscarPorPatronPatente(@Param("patron") String patron);
}
