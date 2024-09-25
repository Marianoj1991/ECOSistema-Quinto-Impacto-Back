package com.quintoimpacto.ecosistema.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
 @AllArgsConstructor
@NoArgsConstructor
public class ImagenesRequestDTO {
    @NotNull
    private MultipartFile imagen;
    @NotNull
    @Min(0)
    @Max(3)
    private int orden;
}
