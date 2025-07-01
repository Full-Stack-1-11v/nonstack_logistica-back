package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

import com.perfulandia.cl.logistica.controller.RutaControllerV2;
import com.perfulandia.cl.logistica.model.Ruta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Assembler para convertir un objeto {@link Ruta} a un {@link EntityModel}.
 * Agrega enlaces HATEOAS para la navegación de la API REST.
 */
@Component
public class RutaAssembler implements RepresentationModelAssembler<Ruta, EntityModel<Ruta>> {

    private static final Logger log = LoggerFactory.getLogger(RutaAssembler.class);

    /**
     * Convierte un objeto {@link Ruta} a un {@link EntityModel} con enlaces
     * HATEOAS.
     *
     * @param ruta el objeto {@link Ruta} a convertir.
     * @return un {@link EntityModel} que contiene la {@link Ruta} y los enlaces
     *         HATEOAS.
     */
    @Override
    @NonNull
    public EntityModel<Ruta> toModel(@NonNull Ruta ruta) {
        log.info(
                "[RutaAssembler.toModel]Assembling Ruta to EntityModel for Ruta with start coords ({}, {}) and end coords ({}, {})",
                ruta.getCoordXInicio(), ruta.getCoordYInicio(), ruta.getCoordXFinal(), ruta.getCoordYFinal());
        EntityModel<Ruta> rutaModel = EntityModel.of(ruta,
                WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(RutaControllerV2.class).putRuta(null, ruta.getIdRuta()))
                        .withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaControllerV2.class).getRutas())
                        .withRel("rutas"));

        // PUT
        rutaModel.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(RutaControllerV2.class).putRuta(null, ruta.getIdRuta()))
                .withRel("update"));

        // DELETE
        rutaModel.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(RutaControllerV2.class).deleteRuta(ruta.getIdRuta()))
                .withRel("delete"));

        return rutaModel;
    }

}
