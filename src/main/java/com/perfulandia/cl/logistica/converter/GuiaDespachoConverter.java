package com.perfulandia.cl.logistica.converter;

import java.util.List;
import java.util.stream.Collectors;



import com.perfulandia.cl.logistica.client.OrdenFeignClient;
import com.perfulandia.cl.logistica.dto.GuiaDespachoDTO;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
/**
 * Clase de utilidad para convertir entidades {@link GuiaDespacho} a objetos de transferencia de datos {@link GuiaDespachoDTO}.
 * Esta clase proporciona métodos estáticos para realizar la conversión, incluyendo la obtención de datos
 * de otros microservicios a través de un cliente Feign.
 */
public class GuiaDespachoConverter {
    /**
     * Convierte un objeto de entidad {@link GuiaDespacho} a un objeto {@link GuiaDespachoDTO}.
     * Mapea los campos de la entidad a los campos correspondientes del DTO.
     * Utiliza el {@link OrdenFeignClient} para obtener y adjuntar los detalles de la orden
     * desde el microservicio de órdenes.
     *
     * @param guiaDespacho el objeto de entidad {@link GuiaDespacho} a convertir.
     * @param ordenClient el cliente Feign para comunicarse con el microservicio de órdenes.
     * @return el objeto {@link GuiaDespachoDTO} resultante, enriquecido con los datos de la orden.
     */

    public static GuiaDespachoDTO convertToDTO(GuiaDespacho guiaDespacho,OrdenFeignClient ordenClient){
        GuiaDespachoDTO dto = new GuiaDespachoDTO();
        dto.setIdEnvio(guiaDespacho.getIdEnvio());
        dto.setIdOrden(guiaDespacho.getIdOrden());
        dto.setIdDespacho(guiaDespacho.getIdDespacho());
        dto.setDatosOrden(ordenClient.obtenerOrdenPorId(guiaDespacho.getIdOrden()));
        if(guiaDespacho.getEnvios() != null){
           List<Integer> envioIds = guiaDespacho.getEnvios().stream()
                .map(envio -> envio.getIdEnvio())
                .collect(Collectors.toList());
            dto.setIdEnvios(envioIds);
        }
        return dto;
    }

}
