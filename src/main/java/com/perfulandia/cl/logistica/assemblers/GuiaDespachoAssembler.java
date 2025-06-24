package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.perfulandia.cl.logistica.controller.GuiaDespachoController;
import com.perfulandia.cl.logistica.controller.GuiaDespachoControllerV2;
import com.perfulandia.cl.logistica.model.GuiaDespacho;

@Component
public class GuiaDespachoAssembler implements RepresentationModelAssembler<GuiaDespacho, EntityModel<GuiaDespacho>>{

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
