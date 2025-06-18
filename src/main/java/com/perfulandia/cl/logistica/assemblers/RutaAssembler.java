package com.perfulandia.cl.logistica.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.perfulandia.cl.logistica.controller.RutaControllerV2;
import com.perfulandia.cl.logistica.model.Ruta;

@Component
public class RutaAssembler implements RepresentationModelAssembler<Ruta, EntityModel<Ruta>> {

    @Override
    public EntityModel<Ruta> toModel(Ruta ruta) {
        return EntityModel.of(ruta,
                linkTo(methodOn(RutaControllerV2.class).getRutaByCoords(ruta.getCoordXInicio(), ruta.getCoordXFinal(),
                        ruta.getCoordYInicio(), ruta.getCoordYFinal())).withRel("rutasPorCoordenadas"),
                linkTo(methodOn(RutaControllerV2.class).putRuta(null, ruta.getIdRuta())).withRel("update"),
                linkTo(methodOn(RutaControllerV2.class).patchRuta(null, ruta.getIdRuta())).withRel("patch"),
                linkTo(methodOn(RutaControllerV2.class).deleteRuta(ruta.getIdRuta())).withRel("delete"),
                linkTo(methodOn(RutaControllerV2.class).getRutas()).withSelfRel());
    }
}
