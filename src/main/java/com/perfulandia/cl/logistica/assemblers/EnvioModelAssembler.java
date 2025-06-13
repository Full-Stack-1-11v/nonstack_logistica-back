package com.perfulandia.cl.logistica.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.EnvioControllerV2;
import com.perfulandia.cl.logistica.model.Envio;

@Component
public class EnvioModelAssembler implements RepresentationModelAssembler<Envio, EntityModel<Envio>> {

    @Override
    public EntityModel<Envio> toModel(Envio envio) {
        return EntityModel.of(envio,
                linkTo(methodOn(EnvioControllerV2.class).getEnvioPorId(envio.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnvioControllerV2.class).getEnvios()).withRel("envios"),
                linkTo(methodOn(EnvioControllerV2.class).eliminarEnvio(envio.getIdEnvio())).withRel("delete"));
    }
}