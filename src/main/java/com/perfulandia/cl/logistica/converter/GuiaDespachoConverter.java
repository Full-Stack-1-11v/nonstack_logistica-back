package com.perfulandia.cl.logistica.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.perfulandia.cl.logistica.dto.GuiaDespachoDTO;
import com.perfulandia.cl.logistica.model.GuiaDespacho;

public class GuiaDespachoConverter {

    public static GuiaDespachoDTO convertToDTO(GuiaDespacho guiaDespacho){
        GuiaDespachoDTO dto = new GuiaDespachoDTO();
        dto.setIdEnvio(guiaDespacho.getIdEnvio());
        dto.setIdOrden(guiaDespacho.getIdOrden());
        dto.setIdDespacho(guiaDespacho.getIdDespacho());
        if(guiaDespacho.getEnvios() != null){
           List<Integer> envioIds = guiaDespacho.getEnvios().stream()
                .map(envio -> envio.getIdEnvio())
                .collect(Collectors.toList());
            dto.setIdEnvios(envioIds);
        }
        return dto;
    }

}
