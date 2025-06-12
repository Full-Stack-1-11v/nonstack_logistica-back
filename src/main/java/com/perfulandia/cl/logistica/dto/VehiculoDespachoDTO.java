package com.perfulandia.cl.logistica.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDespachoDTO {
    @Schema(description = "Id del vehiculo de despacho",example = "1")
    private Integer idVehiculoDespacho;
    @Schema(description = "Patente del vehiculo de despacho",example = "AA-11")
    private String patente;
    @Schema(description = "Año de fabricacion del vehiculo de despacho",example = "2025")
    private Integer ano;
    @Schema(description = "Lista de envios en la que el vehiculo ha participado")
    private List<Integer> idEnvios;

}
