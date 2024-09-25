package com.quintoimpacto.ecosistema.repository;

import com.quintoimpacto.ecosistema.model.Pais;
import com.quintoimpacto.ecosistema.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {

    Optional<Pais> findByNombre(String nombrePais);


}
