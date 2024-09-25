package com.quintoimpacto.ecosistema.model;

import com.quintoimpacto.ecosistema.model.enums.RolesUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private boolean deleted;

    private String telefono;

    @Enumerated(value = EnumType.STRING)
    private RolesUsuario rol;
    //TODO: agregar imagen de perfil

    private Integer productoyservicioCantidad;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ProductoServicio> productoServicios = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Publicacion> publicaciones = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", deleted=" + deleted +
                ", telefono='" + telefono + '\'' +
                ", rol=" + rol +
                ", productoServicioCantidad=" + productoyservicioCantidad +
                '}';
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return getNombre();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isDeleted();
    }
}

