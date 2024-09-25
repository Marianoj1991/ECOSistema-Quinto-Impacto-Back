package com.quintoimpacto.ecosistema.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioUpdateRequest {

    @NotBlank
    @NotNull

    private String nombre;
    @NotBlank
    @NotNull
    private String apellido;
    @NotBlank
    @NotNull
    private String telefono;



}
