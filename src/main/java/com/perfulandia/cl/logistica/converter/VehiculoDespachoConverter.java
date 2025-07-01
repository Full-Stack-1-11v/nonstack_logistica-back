package com.perfulandia.cl.logistica.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.perfulandia.cl.logistica.dto.VehiculoDespachoDTO;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Clase de utilidad para convertir entidades {@link VehiculoDespacho} a objetos de transferencia de datos {@link VehiculoDespachoDTO}.
 * Esta clase proporciona métodos estáticos para realizar la conversión.
 */
public class VehiculoDespachoConverter {

    private static final Logger log = LoggerFactory.getLogger(VehiculoDespachoConverter.class);

    /**
     * Convierte un objeto de entidad {@link VehiculoDespacho} a un objeto {@link VehiculoDespachoDTO}.
     * Mapea los campos de la entidad a los campos correspondientes del DTO, incluyendo la lista de IDs de envíos asociados.
     *
     * @param vehiculo el objeto de entidad {@link VehiculoDespacho} a convertir.
     * @return el objeto {@link VehiculoDespachoDTO} resultante.
     */
    public static VehiculoDespachoDTO convertDTOVehiculo(VehiculoDespacho vehiculo) {
        log.info("[VehiculoDepachoConverter.convertToDTO] Converting VehiculoDespacho with patente {} to VehiculoDespachoDTO", vehiculo.getPatente());
        VehiculoDespachoDTO dto = new VehiculoDespachoDTO();
        dto.setIdVehiculoDespacho(vehiculo.getIdVehiculoDespacho());
        dto.setAno(vehiculo.getAno());
        dto.setPatente(vehiculo.getPatente());
        if (vehiculo.getEnvios() != null) {
            List<Integer> idEnvios = vehiculo.getEnvios().stream()
                    .map(envio -> envio.getIdEnvio())
                    .collect(Collectors.toList());
            dto.setIdEnvios(idEnvios);
        }

        return dto;

    }
}
