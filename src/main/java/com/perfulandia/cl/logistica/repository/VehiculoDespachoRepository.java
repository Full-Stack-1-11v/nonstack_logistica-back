package com.perfulandia.cl.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.VehiculoDespacho;

@Repository
public interface VehiculoDespachoRepository extends JpaRepository<VehiculoDespacho,Integer>{

    VehiculoDespacho findByPatente(String patente);
    boolean existsByPatente(String patente);
    void deleteByPatente(String patente);
}
