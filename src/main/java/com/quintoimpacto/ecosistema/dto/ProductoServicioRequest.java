package com.quintoimpacto.ecosistema.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductoServicioRequest {

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
    private String categoria;

    /*@Size(max = 3, message = "La lista de imagenes no puede ser mayor a 3")
    private List<ImagenesRequestDTO> imagenes;*/
}
