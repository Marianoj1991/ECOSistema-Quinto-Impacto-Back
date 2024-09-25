package com.quintoimpacto.ecosistema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class PublicacionAdminDTO {

    private Long id;

    private String titulo;

    private String descripcion;

    private List<ImagenResponse> imagenes;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime fechaAlta;
    private int cantidadVistos;
    private Boolean estaOculto;
}
