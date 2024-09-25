package com.quintoimpacto.ecosistema.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ImagenProductoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String publicId;

    private int orden;

    @ManyToOne
    @JoinColumn(name = "productoyservicio_id")
    private ProductoServicio productoServicio;

    public ImagenProductoServicio(String url, int orden, String publicId) {
        this.url = url;
        this.orden = orden;
        this.publicId = publicId;
    }
}
