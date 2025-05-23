package com.perfulandia.cl.logistica.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuiaDespachoDTO {

    private Integer idDespacho;
    private Integer idEnvio;
    private Integer idOrden;
    private List<Integer> idEnvios;
    
}
