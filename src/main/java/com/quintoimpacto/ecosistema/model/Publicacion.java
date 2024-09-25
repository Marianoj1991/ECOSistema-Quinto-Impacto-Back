package com.quintoimpacto.ecosistema.model;

import com.quintoimpacto.ecosistema.model.enums.EstadoPublicacion;
import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "publicacion")
public class Publicacion{

    //ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //Atributos
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion", length = 2500)
    private String descripcion;
    /*@Enumerated(value = EnumType.STRING)
    private EstadoPublicacion estado;*/
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL)
    private List<ImagenPublicacion> imagenes = new ArrayList<>();
    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;
    @Column(name = "cant_vis")
    private int cantVis;
    private boolean estaOculto;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;




}
