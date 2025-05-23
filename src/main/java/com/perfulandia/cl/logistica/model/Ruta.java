package com.perfulandia.cl.logistica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruta {

    @Id
    @Column(name = "id_ruta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRuta;

    @Column(name = "coord_x_inicio", nullable = false)
    private float coordXInicio;
    @Column(name = "coord_y_inicio", nullable = false)
    private float coordYInicio;
    @Column(name = "coord_x_final", nullable = false)
    private float coordXFinal;
    @Column(name = "coord_y_final", nullable = false)
    private float coordYFinal;
}
