package com.quintoimpacto.ecosistema.dto;

import com.quintoimpacto.ecosistema.model.enums.EstadoProductoServicio;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductoServicioCompleteResponse {

    @NotNull
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String descripcionCorta;
    @NotNull
    private String descripcionLarga;
    @NotNull
    private String telefono;
    @NotNull
    private String email;
    @NotNull
    private String facebook;
    @NotNull
    private String instagram;
    @NotNull
    private String pais;
    @NotNull
    private String provincia;
    @NotNull
    private String ciudad;
    @NotNull
    private EstadoProductoServicio estado;
    @NotNull
    private String categoria;
    @NotNull
    private String feedback;
    @NotNull
    private List<ImagenResponse> imagenes;

}
