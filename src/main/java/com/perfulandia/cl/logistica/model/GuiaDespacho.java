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

/**
 * Entidad que representa una guía de despacho.
 * Agrupa uno o más envíos que se gestionan conjuntamente.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una guía de despacho.")
public class GuiaDespacho {

    /**
     * ID único de la guía de despacho, generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id de la guia de despacho", example = "1")
    private Integer idDespacho;

    /**
     * ID del envío principal o de referencia para esta guía.
     */
    @Column(name = "id_envio")
    @Schema(description = "Id del envio relacionado", example = "1")
    private Integer idEnvio;

    /**
     * ID de la orden de compra asociada a esta guía de despacho.
     */
    @Column(name = "id_orden")
    @Schema(description = "Id de la orden relacionada", example = "1")
    private Integer idOrden;

    /**
     * Lista de todos los envíos que están incluidos en esta guía de despacho.
     */
    @OneToMany(mappedBy = "guiaDespacho", cascade = CascadeType.ALL, orphanRemoval = true)
    @ArraySchema(schema = @Schema(implementation = Envio.class))
    private List<Envio> envios;

}
