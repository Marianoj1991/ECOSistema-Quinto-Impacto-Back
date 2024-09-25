package com.quintoimpacto.ecosistema.service.abstraction;

import com.quintoimpacto.ecosistema.dto.PaisDTO;
import com.quintoimpacto.ecosistema.model.Pais;

import java.util.List;

public interface PaisService {
    List<PaisDTO> getAllPaises();

    Pais getByName(String nombrePais);


}
