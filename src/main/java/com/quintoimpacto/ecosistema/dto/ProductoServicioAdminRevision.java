package com.quintoimpacto.ecosistema.dto;

import com.quintoimpacto.ecosistema.model.enums.EstadoProductoServicio;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoServicioAdminRevision {
    @NotBlank
    private String feedback;
    @NotBlank
    private EstadoProductoServicio estado;
}
