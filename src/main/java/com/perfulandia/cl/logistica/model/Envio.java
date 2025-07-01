package com.perfulandia.cl.logistica.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

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

/**
 * Entidad que representa un envío de producto(s).
 * Contiene toda la información relativa a un envío específico, incluyendo
 * cliente, orden, fechas, estado y relaciones con otras entidades como
 * Guía de Despacho, Vehículo y Ruta.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un envio de producto(s).")
public class Envio {

    /**
     * ID único del envío, generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id del envio", example = "1")
    private Integer idEnvio;

    /**
     * ID del cliente asociado al envío.
     */
    @Column(name = "id_cliente", nullable = false)
    @Schema(description = "Id del cliente", example = "1")
    private Integer idCliente;

    /**
     * ID de la orden de compra asociada al envío.
     */
    @Column(name = "id_orden", nullable = false)
    @Schema(description = "Id de la orden", example = "1")
    private Integer idOrden;

    /**
     * Fecha en la que se realizó o se programó la entrega.
     */
    @Column(name = "fecha_entrega", nullable = false)
    @Schema(description = "Fecha de cuando se realizo la entrega", example = "30-10-1991")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaEntrega;

    /**
     * Indicador booleano que determina si el envío fue entregado.
     */
    @Column(name = "entregado", nullable = false)
    @Schema(description = "Booleano que determine si el envio fue realizado", example = "true")
    private Boolean entregado;

    /**
     * Campo para anotaciones o comentarios relevantes sobre el envío.
     */
    @Column(name = "observacion", nullable = false, length = 150)
    @Schema(description = "Observacion adicional al envio", example = "Se entrego al hijo de la persona.")
    private String observacion;

    /**
     * La guía de despacho a la que pertenece este envío.
     */
    @ManyToOne
    @JoinColumn(name = "id_despacho", nullable = false)
    @Schema(description = "Guia de despacho relacionada", example = "1")
    @JsonProperty("guiaDespacho")
    private GuiaDespacho guiaDespacho;

    /**
     * El vehículo asignado para realizar este envío.
     */
    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    @Schema(description = "Vehiculo de despacho relacionado", example = "1")
    private VehiculoDespacho vehiculoDespacho;

    /**
     * La ruta asignada para la entrega de este envío.
     */
    @ManyToOne
    @JoinColumn(name = "id_ruta", nullable = false)
    @Schema(description = "Id de la ruta relacionada", example = "2")
    private Ruta ruta;

}
