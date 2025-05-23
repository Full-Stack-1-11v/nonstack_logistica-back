package com.perfulandia.cl.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.Ruta;

@Repository
public interface RutaRepository extends JpaRepository<Ruta,Integer>{

}
