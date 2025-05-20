package com.perfulandia.cl.logistica.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {

    private int idOrden;
    private int idCliente;
    private int idProducto;
}
