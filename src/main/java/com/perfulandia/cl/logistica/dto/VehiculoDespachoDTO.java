package com.perfulandia.cl.logistica.dto;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad VehiculoDespacho.
 * Representa los datos de un vehículo de despacho que se exponen en la API.
 * Extiende {@link RepresentationModel} para soportar HATEOAS.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "DTO para el Vehículo de Despacho.")
public class VehiculoDespachoDTO extends RepresentationModel<VehiculoDespachoDTO> {

    /**
     * ID único del vehículo de despacho.
     */
    @Schema(description = "Id del vehiculo de despacho", example = "1")
    private Integer idVehiculoDespacho;

    /**
     * Patente del vehículo.
     */
    @Schema(description = "Patente del vehiculo de despacho", example = "AA-11-BB")
    private String patente;

    /**
     * Año de fabricación del vehículo.
     */
    @Schema(description = "Año de fabricacion del vehiculo de despacho", example = "2025")
    private Integer ano;

    /**
     * Lista de IDs de los envíos asociados a este vehículo.
     */
    @Schema(description = "Lista de envios en la que el vehiculo ha participado")
    private List<Integer> idEnvios;
}
