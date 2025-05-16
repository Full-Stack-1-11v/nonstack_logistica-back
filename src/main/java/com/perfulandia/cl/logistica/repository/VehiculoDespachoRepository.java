package com.perfulandia.cl.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.VehiculosDespacho;

@Repository
public interface VehiculoDespachoRepository extends JpaRepository<VehiculosDespacho,Integer>{

    public VehiculosDespacho findByPatente(String patente);
}
