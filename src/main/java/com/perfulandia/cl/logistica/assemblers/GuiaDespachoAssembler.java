package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.perfulandia.cl.logistica.controller.GuiaDespachoController;
import com.perfulandia.cl.logistica.model.GuiaDespacho;

@Component
public class GuiaDespachoAssembler implements RepresentationModelAssembler<GuiaDespacho, EntityModel<GuiaDespacho>>{

@Override
    public EntityModel<GuiaDespacho> toModel(GuiaDespacho guiaDespacho) {
        return EntityModel.of(guiaDespacho,
                linkTo(methodOn(GuiaDespachoController.class).getGuiaDespachoById(guiaDespacho.getIdDespacho())).withSelfRel(),
                linkTo(methodOn(GuiaDespachoController.class).putGuiaDespacho(guiaDespacho.getIdDespacho(), null)).withRel("update"),
                linkTo(methodOn(GuiaDespachoController.class).patchGuiaDespacho(guiaDespacho.getIdDespacho(), null)).withRel("patch"),
                linkTo(methodOn(GuiaDespachoController.class).deleteGuiaDespacho(guiaDespacho.getIdDespacho())).withRel("delete"));
    }

}
