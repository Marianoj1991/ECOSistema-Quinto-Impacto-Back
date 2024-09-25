package com.quintoimpacto.ecosistema.repository;

import com.quintoimpacto.ecosistema.model.ImagenPublicacion;
import com.quintoimpacto.ecosistema.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImagenPublicacionRepository extends JpaRepository<ImagenPublicacion, Long> {
    Optional<ImagenPublicacion> findByPublicacionAndOrden(Publicacion publicacion, int orden);

    void deleteByPublicacionAndOrden(Publicacion publicacion, int orden);
}
