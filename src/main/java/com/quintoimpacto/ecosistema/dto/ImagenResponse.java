package com.quintoimpacto.ecosistema.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagenResponse {

    @NotBlank
    private String url;
    @NotNull
    @Min(0)
    @Max(3)
    private int orden;
}
