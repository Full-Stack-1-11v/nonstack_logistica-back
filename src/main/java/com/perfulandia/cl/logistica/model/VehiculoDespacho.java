package com.perfulandia.cl.logistica.model;

import java.util.List;

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

/**
 * Entidad que representa un vehículo de despacho que será usado para envíos.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un vehículo de despacho que sera usado para evnios.")
public class VehiculoDespacho {

    /**
     * ID único del vehículo, generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id del vehiculo", example = "1")
    private Integer idVehiculoDespacho;

    /**
     * Patente única del vehículo.
     */
    @Column(name = "patente", nullable = false, length = 13, unique = true)
    @Schema(description = "Patente del vehiculo", example = "AA-11")
    private String patente;

    /**
     * Año de manufacturación del vehículo.
     */
    @Column(name = "ano", nullable = false, length = 4)
    @Schema(description = "Año de manufacturación del vehículo", example = "2025")
    private Integer ano;

    /**
     * Lista de envíos asignados a este vehículo. La propiedad se ignora en la serialización JSON.
     */
    @OneToMany(mappedBy = "vehiculoDespacho", cascade = CascadeType.ALL, orphanRemoval = true)
    @ArraySchema(schema = @Schema(implementation = Envio.class))
    @JsonIgnore
    private List<Envio> envios;
}