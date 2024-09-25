package com.quintoimpacto.ecosistema.repository;

import com.quintoimpacto.ecosistema.model.ImagenProductoServicio;
import com.quintoimpacto.ecosistema.model.ProductoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImagenProductoServicioRepository extends JpaRepository<ImagenProductoServicio, Long> {
    Optional<ImagenProductoServicio> findByOrdenAndProductoServicio(int orden, ProductoServicio entidad);
    void deleteByOrdenAndProductoServicio(int orden, ProductoServicio entidad);

}
