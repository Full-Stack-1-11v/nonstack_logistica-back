package com.perfulandia.cl.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio,Integer>{

}
