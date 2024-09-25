package com.quintoimpacto.ecosistema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PublicacionDashboardDTO {

    private Long id;

    private String titulo;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime fecha;

    private int vistas;
}
