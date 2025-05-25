package com.perfulandia.cl.logistica.model;

import java.util.List;


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
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVehiculoDespacho;

    @Column(name = "patente" , nullable = false , length = 13, unique = true)
    private String patente;

    @Column(name = "ano" , nullable = false , length = 4)
    private Integer ano;

    @OneToMany(mappedBy = "vehiculoDespacho" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Envio> envios;
}