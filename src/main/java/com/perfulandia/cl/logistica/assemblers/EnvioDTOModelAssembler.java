package com.perfulandia.cl.logistica.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.EnvioControllerV2;
import com.perfulandia.cl.logistica.dto.EnvioDTO;

@Component
public class EnvioDTOModelAssembler implements RepresentationModelAssembler<EnvioDTO, EntityModel<EnvioDTO>> {

    @Override
    public EntityModel<EnvioDTO> toModel(EnvioDTO envioDTO) {
        return EntityModel.of(envioDTO,
                linkTo(methodOn(EnvioControllerV2.class).getEnvioPorId(envioDTO.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnvioControllerV2.class).actualizarEnvio(envioDTO.getIdEnvio(), null)).withRel("update"),
                linkTo(methodOn(EnvioControllerV2.class).parcharEnvio(envioDTO.getIdEnvio(), null)).withRel("patch"),
                linkTo(methodOn(EnvioControllerV2.class).eliminarEnvio(envioDTO.getIdEnvio())).withRel("delete"),
                linkTo(methodOn(EnvioControllerV2.class).getEnvios()).withRel("envios"));
    }
}
