package com.quintoimpacto.ecosistema.service.impl;

import com.quintoimpacto.ecosistema.dto.CategoriaDTO;
import com.quintoimpacto.ecosistema.model.Categoria;
import com.quintoimpacto.ecosistema.repository.CategoriaRepository;
import com.quintoimpacto.ecosistema.service.abstraction.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CategoriaServiceImpl implements CategoriaService {


    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoriaDTO> getAllCategorias() {
        List<Categoria> categorias =  categoriaRepository.findAll();
        if (categorias.isEmpty()){throw new NoSuchElementException("La lista categoria esta vacia");
        }
        return categorias.stream().map(categoria -> modelMapper.map(categoria, CategoriaDTO.class)).toList();
    }

    @Override
    public Categoria getCategoriaByNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre).orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada"));
    }
}
