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
@NoArgsConstructor
@AllArgsConstructor
public class GuiaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDespacho;

    @Column(name="id_envio")
    private Integer idEnvio;

    @Column(name="id_orden")
    private Integer idOrden;

    @OneToMany(mappedBy = "guiaDespacho",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Envio> envios;

}
