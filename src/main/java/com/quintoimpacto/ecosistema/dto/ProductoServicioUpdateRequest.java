package com.quintoimpacto.ecosistema.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoServicioUpdateRequest {

    //La id se recibe como query param
    @NotNull
    private String nombre;

    @NotNull
    private String descripcionLarga;

    @NotNull
    private String descripcionCorta;

    @NotNull
    private String telefono;

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

    /*private List<ImagenesRequestDTO> imagenes;*/



    /*@NotNull
    private List<String> imagenesEliminadas;

    @NotNull
    private List<MultipartFile> imagenesCreadas;*/

}
