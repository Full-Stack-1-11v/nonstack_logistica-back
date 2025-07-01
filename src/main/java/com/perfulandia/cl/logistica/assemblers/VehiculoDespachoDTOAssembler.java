package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.VehiculoDespachoControllerV2;
import com.perfulandia.cl.logistica.dto.VehiculoDespachoDTO;
/**
 * Assembler para convertir un {@link VehiculoDespachoDTO} a un {@link EntityModel}.
 * Agrega enlaces HATEOAS para la navegación de la API REST.
 */
@Component
public class VehiculoDespachoDTOAssembler implements RepresentationModelAssembler<VehiculoDespachoDTO, EntityModel<VehiculoDespachoDTO>> {
    
    private static final Logger log = LoggerFactory.getLogger(VehiculoDespachoDTOAssembler.class);
    /**
     * Convierte un objeto {@link VehiculoDespachoDTO} a un {@link EntityModel} con enlaces HATEOAS.
     *
     * @param vehiculoDespachoDTO el objeto {@link VehiculoDespachoDTO} a convertir.
     * @return un {@link EntityModel} que contiene el {@link VehiculoDespachoDTO} y los enlaces HATEOAS.
     */
    @Override
    public EntityModel<VehiculoDespachoDTO> toModel(VehiculoDespachoDTO vehiculoDespachoDTO) {
        log.info("[VehiculoDespachoDTOAssembler.toModel] Assembling VehiculoDespacho to EntityModel for VehiculoDespacho with patente {}", vehiculoDespachoDTO.getPatente());
        return EntityModel.of(vehiculoDespachoDTO,
                linkTo(methodOn(VehiculoDespachoControllerV2.class).actualizarVehiculoDespacho(null, vehiculoDespachoDTO.getPatente())).withRel("update"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).parcharVehiculoDespacho(null, vehiculoDespachoDTO.getPatente())).withRel("patch"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).borrarVehiculoDespacho(vehiculoDespachoDTO.getPatente())).withRel("delete"),
                linkTo(methodOn(VehiculoDespachoControllerV2.class).getVehiculosDespacho()).withRel("vehiculos"));
    }
}