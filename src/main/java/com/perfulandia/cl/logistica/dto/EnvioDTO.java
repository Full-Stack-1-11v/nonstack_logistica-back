package com.perfulandia.cl.logistica.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioDTO {

    private Integer idEnvio;
    private Integer idCliente;
    private Integer idOrden;
    private LocalDate fechaEntrega;
    private boolean entregado;
    private String observacion;
    private Integer guiaDespachoId; // Solo la ID, eso es lo que quiero traerme!!
    private Integer vehiculoDespachoId; // Solo la ID, eso es lo que quiero trarme nomas!!
}