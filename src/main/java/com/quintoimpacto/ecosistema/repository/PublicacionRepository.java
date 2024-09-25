package com.quintoimpacto.ecosistema.repository;

import com.quintoimpacto.ecosistema.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long>{

    List<Publicacion> findByIsDeletedFalseAndEstaOcultoFalse();
    Optional<Publicacion> findByIdAndIsDeletedFalseAndEstaOcultoFalse(Long id);

    List<Publicacion> findByIsDeletedFalse();

    @Query("SELECT p FROM Publicacion p WHERE p.isDeleted = false ORDER BY p.fechaAlta DESC")
    List<Publicacion> findByisDeletedFalseAndFechaOrdenDesc();

    Optional<Publicacion> findByIdAndIsDeletedFalse(Long id);


}
