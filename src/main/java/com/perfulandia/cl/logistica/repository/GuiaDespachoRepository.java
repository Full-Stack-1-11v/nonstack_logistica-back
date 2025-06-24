package com.perfulandia.cl.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.perfulandia.cl.logistica.model.GuiaDespacho;

/**
 * Repositorio de Spring Data JPA para la entidad {@link GuiaDespacho}.
 * Hereda los métodos CRUD estándar para la gestión de guías de despacho.
 */
@Repository
public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho,Integer>{

}
