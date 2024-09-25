package com.quintoimpacto.ecosistema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quintoimpacto.ecosistema.model.enums.EstadoPublicacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicacionDTO {

    private Long id;

    private String titulo;

    private String descripcion;

    private List<ImagenResponse> imagenes;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime fechaAlta;

}
