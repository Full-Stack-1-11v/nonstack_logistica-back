package com.perfulandia.cl.logistica.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Entidad que representa un envio de producto(s).")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id del envio",example = "1")
    private Integer idEnvio;

    @Column(name = "id_cliente", nullable = false)
    @Schema(description = "Id del cliente",example = "1")
    private Integer idCliente;

    @Column(name = "id_orden", nullable = false)
    @Schema(description = "Id de la orden",example = "1")
    private Integer idOrden;

    @Column(name = "fecha_entrega", nullable = false)
    @Schema(description = "Fecha de cuando se realizo la entrega",example = "30-10-1991")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaEntrega;

    @Column(name = "entregado", nullable = false)
    @Schema(description = "Booleano que determine si el envio fue realizado",example = "true")
    private Boolean entregado;

    @Column(name = "observacion", nullable = false, length = 150)
    @Schema(description = "Observacion adicional al envio",example = "Se entrego al hijo de la persona.")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_despacho", nullable = false)
    @JsonIgnore
    @Schema(description = "Guia de despacho relacionada",example = "1")
    private GuiaDespacho guiaDespacho;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    @JsonIgnore
    @Schema(description = "Vehiculo de despacho relacionado",example = "1")
    private VehiculoDespacho vehiculoDespacho;

    @ManyToOne
    @JoinColumn(name = "id_ruta",nullable = false)
    @JsonIgnore
    @Schema(description = "Id de la ruta relacionada",example = "02")
    private Ruta ruta;

}
