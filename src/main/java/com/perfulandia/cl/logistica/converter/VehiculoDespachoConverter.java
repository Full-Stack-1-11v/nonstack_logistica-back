package com.perfulandia.cl.logistica.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.perfulandia.cl.logistica.dto.VehiculoDespachoDTO;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;

public class VehiculoDespachoConverter {

    public static VehiculoDespachoDTO convertDTOVehiculo(VehiculoDespacho vehiculo) {
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
