package com.quintoimpacto.ecosistema.model;


import com.quintoimpacto.ecosistema.model.enums.EstadoProductoServicio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter

@Table(name = "productoyservicio")
public class ProductoServicio {

    //ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcionLarga;

    @Column(name = "descripcion_corta")
    private String descripcionCorta;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "instagram")
    private String instagram;

    private boolean isDeleted;

    private String feedback;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

    @Column(name = "ciudad")
    private String ciudad;

    @Enumerated(EnumType.STRING)
    private EstadoProductoServicio estado;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "productoServicio",cascade = CascadeType.ALL)
    private List<ImagenProductoServicio> imagenes = new ArrayList<>();
}
