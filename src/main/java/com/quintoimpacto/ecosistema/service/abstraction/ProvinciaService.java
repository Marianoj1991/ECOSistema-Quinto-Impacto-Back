package com.quintoimpacto.ecosistema.service.abstraction;

import com.quintoimpacto.ecosistema.dto.ProvinciaDTO;
import com.quintoimpacto.ecosistema.model.Pais;
import com.quintoimpacto.ecosistema.model.Provincia;

import java.util.List;
import java.util.Optional;

public interface ProvinciaService {

    List<ProvinciaDTO> getAllProvinciasByPais(String paisNombre);

    Provincia getProvinciaByNombreYPais(String provinciaNombre, Pais pais);

}
