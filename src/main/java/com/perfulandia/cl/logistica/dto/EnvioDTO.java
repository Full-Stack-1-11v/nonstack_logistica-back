package com.perfulandia.cl.logistica.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un envio de producto(s) DTO.")
public class EnvioDTO {
    @Schema(description = "Id del envio",example = "1")
    private Integer idEnvio;
    @Schema(description = "Id del cliente",example = "1")
    private Integer idCliente;
    @Schema(description = "Id de la orden",example = "1")
    private Integer idOrden;
    @Schema(description = "Fecha de entrega del envio",example = "30/10/1991")
    private LocalDate fechaEntrega;
    @Schema(description = "Booleano que identifica si el envio fue entregado o on",example = "false")
    private Boolean entregado;
    @Schema(description = "Observacion sobre el envio",example = "Ataque de perro , no se pudo entregar")
    private String observacion;
    @Schema(description = "Id de la  guia de despacho",example = "1")
    private Integer guiaDespachoId; // Solo la ID, eso es lo que quiero traerme!!
    @Schema(description = "Id del vehiculo de despacho",example = "1")
    private Integer vehiculoDespachoId; 
    @Schema(description = "Id de la ruta del envio",example = "1")
    private Integer rutaId;
}