package com.perfulandia.cl.logistica.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.VehiculoDespachoControllerV2;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;

@Component
public class VehiculoDespachoAssembler implements RepresentationModelAssembler<VehiculoDespacho, EntityModel<VehiculoDespacho>> {

    @Override
    public EntityModel<VehiculoDespacho> toModel(VehiculoDespacho vehiculoDespacho) {
        return EntityModel.of(vehiculoDespacho,
                linkTo(methodOn(VehiculoDespachoControllerV2.class).actualizarVehiculoDespacho(null, vehiculoDespacho.getPatente())).withRel("update"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).parcharVehiculoDespacho(null, vehiculoDespacho.getPatente())).withRel("patch"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).borrarVehiculoDespacho(vehiculoDespacho.getPatente())).withRel("delete"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).getVehiculosDespacho()).withRel("vehiculos"));
    }
}
