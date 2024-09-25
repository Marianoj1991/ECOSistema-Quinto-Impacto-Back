package com.quintoimpacto.ecosistema.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImagenDTO {

    @NotBlank
    private String url;

    @NotBlank
    private String publicId;

    @Min(0)
    @Max(3)
    private int orden;
}
