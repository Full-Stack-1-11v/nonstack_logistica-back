package com.perfulandia.cl.logistica.converter;

import com.perfulandia.cl.logistica.dto.EnvioDTO;
import com.perfulandia.cl.logistica.model.Envio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de utilidad para convertir entidades {@link Envio} a objetos de transferencia de datos {@link EnvioDTO}.
 * Esta clase proporciona métodos estáticos para realizar la conversión.
 */
public class EnvioConverter {

    private static final Logger log = LoggerFactory.getLogger(EnvioConverter.class);

    /**
     * Convierte un objeto de entidad {@link Envio} a un objeto {@link EnvioDTO}.
     * Mapea los campos de la entidad a los campos correspondientes del DTO.
     *
     * @param envio el objeto de entidad {@link Envio} a convertir.
     * @return el objeto {@link EnvioDTO} resultante.
     */
    public static EnvioDTO convertToDTO(Envio envio) {
        log.info("[EnvioConverter.convertToDTO] Converting Envio with id {} to EnvioDTO", envio.getIdEnvio());
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