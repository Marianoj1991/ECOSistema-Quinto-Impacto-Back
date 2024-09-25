package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.dto.CategoriaDTO;
import com.quintoimpacto.ecosistema.model.Categoria;
import com.quintoimpacto.ecosistema.service.abstraction.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {


    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerCategorias(){
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }
}
