package com.quintoimpacto.ecosistema.model;

import com.quintoimpacto.ecosistema.dto.ImagenDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagenPublicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private int orden;

    private String publicId;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;


    public ImagenPublicacion(String url, int orden, String publicId) {
        this.url = url;
        this.orden = orden;
        this.publicId = publicId;
    }
}
