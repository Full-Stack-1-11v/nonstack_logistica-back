package com.perfulandia.cl.logistica.model;

import java.util.List;

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
@Schema(description = "Entidad que representa una guía de despacho.")
public class GuiaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id de la guia de despacho",example = "1")
    private Integer idDespacho;

    @Column(name="id_envio")
    @Schema(description = "Id del envio relacionado",example = "1")
    private Integer idEnvio;

    @Column(name="id_orden")
    @Schema(description = "Id de la orden relacionada",example = "1")
    private Integer idOrden;

    @OneToMany(mappedBy = "guiaDespacho",cascade = CascadeType.ALL , orphanRemoval = true)
    @ArraySchema(schema = @Schema(implementation = Envio.class))
    private List<Envio> envios;

}
