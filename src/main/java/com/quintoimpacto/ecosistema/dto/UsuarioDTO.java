package com.quintoimpacto.ecosistema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioDTO {

    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;
}
