package com.perfulandia.cl.logistica.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.VehiculoDespachoControllerV2;
import com.perfulandia.cl.logistica.dto.VehiculoDespachoDTO;

@Component
public class VehiculoDespachoDTOAssembler implements RepresentationModelAssembler<VehiculoDespachoDTO, EntityModel<VehiculoDespachoDTO>> {

    @Override
    public EntityModel<VehiculoDespachoDTO> toModel(VehiculoDespachoDTO vehiculoDespachoDTO) {
        return EntityModel.of(vehiculoDespachoDTO,
                linkTo(methodOn(VehiculoDespachoControllerV2.class).actualizarVehiculoDespacho(null, vehiculoDespachoDTO.getPatente())).withRel("update"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).parcharVehiculoDespacho(null, vehiculoDespachoDTO.getPatente())).withRel("patch"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).borrarVehiculoDespacho(vehiculoDespachoDTO.getPatente())).withRel("delete"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).getVehiculosDespacho()).withRel("vehiculos"));
    }
}