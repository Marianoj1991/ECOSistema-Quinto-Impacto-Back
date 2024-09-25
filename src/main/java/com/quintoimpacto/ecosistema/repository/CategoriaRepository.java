package com.quintoimpacto.ecosistema.repository;

import com.quintoimpacto.ecosistema.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Boolean existsByNombre(String nombre);
    Optional<Categoria> findByNombre(String nombre);
}
