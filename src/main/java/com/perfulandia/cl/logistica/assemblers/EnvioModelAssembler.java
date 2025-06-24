package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.EnvioControllerV2;
import com.perfulandia.cl.logistica.model.Envio;

/**
 * Assembler para convertir un {@link Envio} a un {@link EntityModel}.
 * Agrega enlaces HATEOAS para la navegación de la API REST.
 */
@Component


public class EnvioModelAssembler implements RepresentationModelAssembler<Envio, EntityModel<Envio>> {

    /**
     * Convierte un objeto {@link Envio} a un {@link EntityModel} con enlaces HATEOAS.
     *
     * @param envio el objeto {@link Envio} a convertir.
     * @return un {@link EntityModel} que contiene el {@link Envio} y los enlaces HATEOAS.
     */
    @Override
    public EntityModel<Envio> toModel(Envio envio) {
        return EntityModel.of(envio,
                linkTo(methodOn(EnvioControllerV2.class).getEnvioPorId(envio.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnvioControllerV2.class).getEnvios()).withRel("envios"),
                linkTo(methodOn(EnvioControllerV2.class).actualizarEnvio(null, envio)).withRel("put"),
                linkTo(methodOn(EnvioControllerV2.class).actualizarEnvio(null, envio)).withRel("patch"),
                linkTo(methodOn(EnvioControllerV2.class).eliminarEnvio(envio.getIdEnvio())).withRel("delete"));
    }
}