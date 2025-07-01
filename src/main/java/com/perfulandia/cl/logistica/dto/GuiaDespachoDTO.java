package com.perfulandia.cl.logistica.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad GuiaDespacho.
 * Representa los datos de una guía de despacho que se exponen en la API,
 * incluyendo datos enriquecidos de otras fuentes como el microservicio de órdenes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la Guía de Despacho.")
public class GuiaDespachoDTO {

    /**
     * ID único de la guía de despacho.
     */
    @Schema(description = "Id de la guia de despacho", example = "1")
    private Integer idDespacho;

    /**
     * ID del envío principal asociado a esta guía.
     */
    @Schema(description = "Id del envio", example = "1")
    private Integer idEnvio;

    /**
     * ID de la orden asociada a esta guía.
     */
    @Schema(description = "Id de la orden", example = "1")
    private Integer idOrden;

    /**
     * Objeto DTO con los detalles de la orden, obtenido de un servicio externo.
     */
    @Schema(description = "Datos de la orden asociada a la guia de despacho")
    private OrdenDTO datosOrden;

    /**
     * Lista de IDs de todos los envíos asociados a esta guía de despacho.
     */
    @Schema(description = "Lista de Ids de los envios asociados a la guia de despacho")
    private List<Integer> idEnvios;
}
