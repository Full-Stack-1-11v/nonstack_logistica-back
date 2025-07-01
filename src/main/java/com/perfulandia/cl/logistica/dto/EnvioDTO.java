package com.perfulandia.cl.logistica.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Envio.
 * Representa los datos de un envío que se exponen en la API.
 * Extiende {@link RepresentationModel} para soportar HATEOAS.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Entidad que representa un envio de producto(s) DTO.")
public class EnvioDTO extends RepresentationModel<EnvioDTO> {

    /**
     * ID único del envío.
     */
    @Schema(description = "Id del envio", example = "1")
    private Integer idEnvio;

    /**
     * ID del cliente asociado al envío.
     */
    @Schema(description = "Id del cliente", example = "1")
    private Integer idCliente;

    /**
     * ID de la orden asociada al envío.
     */
    @Schema(description = "Id de la orden", example = "1")
    private Integer idOrden;

    /**
     * Fecha programada para la entrega del envío.
     */
    @Schema(description = "Fecha de entrega del envio", example = "1991-10-30")
    private LocalDate fechaEntrega;

    /**
     * Estado de entrega del envío (true si fue entregado, false en caso contrario).
     */
    @Schema(description = "Booleano que identifica si el envio fue entregado o no", example = "false")
    private Boolean entregado;

    /**
     * Observaciones adicionales sobre el envío.
     */
    @Schema(description = "Observacion sobre el envio", example = "Ataque de perro, no se pudo entregar")
    private String observacion;

    /**
     * ID de la guía de despacho asociada al envío.
     */
    @Schema(description = "Id de la guia de despacho", example = "1")
    private Integer guiaDespachoId;

    /**
     * ID del vehículo de despacho asignado al envío.
     */
    @Schema(description = "Id del vehiculo de despacho", example = "1")
    private Integer vehiculoDespachoId;

    /**
     * ID de la ruta asignada al envío.
     */
    @Schema(description = "Id de la ruta del envio", example = "1")
    private Integer rutaId;
}