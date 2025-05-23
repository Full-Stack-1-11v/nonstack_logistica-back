package com.perfulandia.cl.logistica.converter;

import com.perfulandia.cl.logistica.dto.EnvioDTO;
import com.perfulandia.cl.logistica.model.Envio;

public class EnvioConverter {

    public static EnvioDTO convertToDTO(Envio envio) {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdEnvio(envio.getIdEnvio());
        dto.setIdCliente(envio.getIdCliente());
        dto.setIdOrden(envio.getIdOrden());
        dto.setFechaEntrega(envio.getFechaEntrega());
        dto.setEntregado(envio.getEntregado());
        dto.setObservacion(envio.getObservacion());
        if (envio.getGuiaDespacho() != null) {
            dto.setGuiaDespachoId(envio.getGuiaDespacho().getIdDespacho());
        }
        if (envio.getVehiculoDespacho() != null) {
            dto.setVehiculoDespachoId(envio.getVehiculoDespacho().getIdVehiculoDespacho());
        }
        if (envio.getRuta() != null) {
            dto.setRutaId(envio.getRuta().getIdRuta());
        }
        return dto;
    }
}