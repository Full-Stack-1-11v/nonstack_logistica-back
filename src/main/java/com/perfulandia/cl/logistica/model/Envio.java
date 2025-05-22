package com.perfulandia.cl.logistica.model;

import java.time.LocalDate;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEnvio;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Column(name = "id_orden", nullable = false)
    private Integer idOrden;

    @Column(name = "fecha_entrega", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaEntrega;

    @Column(name = "entregado", nullable = false)
    private boolean entregado;

    @Column(name = "observacion", nullable = false, length = 150)
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_despacho", nullable = false)
    private GuiaDespacho guiaDespacho;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private VehiculoDespacho vehiculoDespacho;

}
