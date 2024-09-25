package com.quintoimpacto.ecosistema.service.abstraction;

import com.quintoimpacto.ecosistema.dto.CategoriaDTO;
import com.quintoimpacto.ecosistema.model.Categoria;

import java.util.List;

public interface CategoriaService {

    List<CategoriaDTO> getAllCategorias();

    Categoria getCategoriaByNombre(String nombre);
}
