package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.perfulandia.cl.logistica.controller.GuiaDespachoController;
import com.perfulandia.cl.logistica.controller.GuiaDespachoControllerV2;
import com.perfulandia.cl.logistica.model.GuiaDespacho;
/**
 * Assembler para convertir un {@link GuiaDespacho} a un {@link EntityModel}.
 * Agrega enlaces HATEOAS para la navegación de la API REST.
 */
@Component

public class GuiaDespachoAssembler implements RepresentationModelAssembler<GuiaDespacho, EntityModel<GuiaDespacho>>{

    /**
     * Convierte un objeto {@link GuiaDespacho} a un {@link EntityModel} con enlaces HATEOAS.
     *
     * @param guiaDespacho el objeto {@link GuiaDespacho} a convertir.
     * @return un {@link EntityModel} que contiene el {@link GuiaDespacho} y los enlaces HATEOAS.
     */
@Override
    public EntityModel<GuiaDespacho> toModel(GuiaDespacho guiaDespacho) {

        GuiaDespacho dummyGuiaDespacho = new GuiaDespacho();

        return EntityModel.of(guiaDespacho,
                linkTo(methodOn(GuiaDespachoControllerV2.class).getGuiaDespachoById(guiaDespacho.getIdDespacho())).withSelfRel(),
                linkTo(methodOn(GuiaDespachoControllerV2.class).putGuiaDespacho(guiaDespacho.getIdDespacho(), dummyGuiaDespacho)).withRel("update"),
                linkTo(methodOn(GuiaDespachoControllerV2.class).patchGuiaDespacho(guiaDespacho.getIdDespacho(), dummyGuiaDespacho)).withRel("patch"),
                linkTo(methodOn(GuiaDespachoControllerV2.class).deleteGuiaDespacho(guiaDespacho.getIdDespacho())).withRel("delete"));
    }

}
