package com.quintoimpacto.ecosistema.repository;

import com.quintoimpacto.ecosistema.model.Pais;
import com.quintoimpacto.ecosistema.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    Optional<List<Provincia>> findByPais(Pais pais);
    Optional<Provincia> findByNombreAndPais(String nombre, Pais pais);



}
