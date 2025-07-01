package com.perfulandia.cl.logistica.assemblers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.EnvioControllerV2;
import com.perfulandia.cl.logistica.dto.EnvioDTO;
/**
 * Assembler para convertir un {@link EnvioDTO} a un {@link EntityModel}.
 * Agrega enlaces HATEOAS para la navegación de la API REST.
 */
@Component


public class EnvioDTOModelAssembler implements RepresentationModelAssembler<EnvioDTO, EntityModel<EnvioDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(EnvioDTOModelAssembler.class);

    /**
     * Convierte un objeto {@link EnvioDTO} a un {@link EntityModel} con enlaces HATEOAS.
     *
     * @param envioDTO el objeto {@link EnvioDTO} a convertir.
     * @return un {@link EntityModel} que contiene el {@link EnvioDTO} y los enlaces HATEOAS.
     */
    @Override
    public EntityModel<EnvioDTO> toModel(EnvioDTO envioDTO) {
        logger.info("[EnvioDTOModelAssembler.toModel] Assembling links for EnvioDTO with id {}", envioDTO.getIdEnvio());
        return EntityModel.of(envioDTO,
                linkTo(methodOn(EnvioControllerV2.class).getEnvioPorId(envioDTO.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnvioControllerV2.class).actualizarEnvio(envioDTO.getIdEnvio(), null)).withRel("update"),
                linkTo(methodOn(EnvioControllerV2.class).parcharEnvio(envioDTO.getIdEnvio(), null)).withRel("patch"),
                linkTo(methodOn(EnvioControllerV2.class).eliminarEnvio(envioDTO.getIdEnvio())).withRel("delete"),
                linkTo(methodOn(EnvioControllerV2.class).getEnvios()).withRel("envios"));
    }
}
