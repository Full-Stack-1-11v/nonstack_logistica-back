package com.perfulandia.cl.logistica.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDespachoDTO {

    private Integer idVehiculoDespacho;
    private String patente;
    private Integer ano;
    private List<Integer> idEnvios;

}
