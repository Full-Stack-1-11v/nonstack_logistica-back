package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.VehiculoDespachoControllerV2;
import com.perfulandia.cl.logistica.model.VehiculoDespacho;
/**
 * Assembler para convertir un {@link VehiculoDespacho} a un {@link EntityModel}.
 * Agrega enlaces HATEOAS para la navegación de la API REST.
 */

@Component
public class VehiculoDespachoAssembler implements RepresentationModelAssembler<VehiculoDespacho, EntityModel<VehiculoDespacho>> {

    /**
     * Convierte un objeto {@link VehiculoDespacho} a un {@link EntityModel} con enlaces HATEOAS.
     *
     * @param vehiculoDespacho el objeto {@link VehiculoDespacho} a convertir.
     * @return un {@link EntityModel} que contiene el {@link VehiculoDespacho} y los enlaces HATEOAS.
     */

    @Override
    public EntityModel<VehiculoDespacho> toModel(VehiculoDespacho vehiculoDespacho) {
        return EntityModel.of(vehiculoDespacho,
                linkTo(methodOn(VehiculoDespachoControllerV2.class).actualizarVehiculoDespacho(null, vehiculoDespacho.getPatente())).withRel("update"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).parcharVehiculoDespacho(null, vehiculoDespacho.getPatente())).withRel("patch"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).borrarVehiculoDespacho(vehiculoDespacho.getPatente())).withRel("delete"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).getVehiculosDespacho()).withRel("vehiculos"));
    }
}
