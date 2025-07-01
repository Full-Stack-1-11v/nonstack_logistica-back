package com.perfulandia.cl.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para una Orden.
 * Representa los datos de una orden, generalmente obtenidos de un microservicio externo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa una Orden de un servicio externo.")
public class OrdenDTO {

    /**
     * ID único de la orden.
     */
    @Schema(description = "Id de la orden", example = "1")
    private int idOrden;

    /**
     * ID del cliente que realizó la orden.
     */
    @Schema(description = "Id del cliente", example = "1")
    private int idCliente;

    /**
     * ID del producto incluido en la orden.
     */
    @Schema(description = "Id del producto", example = "1")
    private int idProducto;
}
