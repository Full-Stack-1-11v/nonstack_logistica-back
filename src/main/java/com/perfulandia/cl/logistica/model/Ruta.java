package com.perfulandia.cl.logistica.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una ruta de un envio.")
public class Ruta {

    @Id
    @Column(name = "id_ruta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id la ruta",example = "1")
    private Integer idRuta;

    @Column(name = "coord_x_inicio", nullable = false)
    @Schema(description = "Coordenada X inicial (donde parte el transporte)",example = "25.03F")
    private Float coordXInicio;

    @Column(name = "coord_y_inicio", nullable = false)
    @Schema(description = "Coordenada Y inicial (donde parte el transporte)",example = "15.03F")
    private Float coordYInicio;

    @Column(name = "coord_x_final", nullable = false)
    @Schema(description = "Coordenada X final (donde termina el transporte)",example = "-25.03F")
    private Float coordXFinal;

    @Column(name = "coord_y_final", nullable = false)
    @Schema(description = "Coordenada Y final (donde parte el transporte)",example = "-15.03F")
    private Float coordYFinal;

    @OneToMany(mappedBy = "ruta" , cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonIgnore
    @ArraySchema(schema = @Schema(implementation = Envio.class))
    @JsonBackReference
    private List<Envio> envios;
}
