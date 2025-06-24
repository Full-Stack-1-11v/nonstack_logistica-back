package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.perfulandia.cl.logistica.controller.RutaControllerV2;
import com.perfulandia.cl.logistica.model.Ruta;

/**
 * Assembler para convertir un objeto {@link Ruta} a un {@link EntityModel}.
 * Agrega enlaces HATEOAS para la navegación de la API REST.
 */
@Component


public class RutaAssembler implements RepresentationModelAssembler<Ruta, EntityModel<Ruta>> {
    /**
     * Convierte un objeto {@link Ruta} a un {@link EntityModel} con enlaces HATEOAS.
     *
     * @param ruta el objeto {@link Ruta} a convertir.
     * @return un {@link EntityModel} que contiene la {@link Ruta} y los enlaces HATEOAS.
     */
    @Override
    public EntityModel<Ruta> toModel(Ruta ruta) {
        return EntityModel.of(ruta,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaControllerV2.class).getRutas()).withRel("rutas"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaControllerV2.class).getRutaByCoords(ruta.getCoordXInicio(), ruta.getCoordXFinal(), ruta.getCoordYInicio(), ruta.getCoordYFinal())).withSelfRel()
        );
    }
    
}
