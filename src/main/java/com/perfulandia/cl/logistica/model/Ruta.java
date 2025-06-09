package com.perfulandia.cl.logistica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private Float coordXInicio;

    @Column(name = "coord_y_inicio", nullable = false)
    private Float coordYInicio;

    @Column(name = "coord_x_final", nullable = false)
    private Float coordXFinal;

    @Column(name = "coord_y_final", nullable = false)
    private Float coordYFinal;

    @OneToMany(mappedBy = "ruta" , cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonIgnore
    private List<Envio> envios;
}
