package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.dto.ProvinciaDTO;
import com.quintoimpacto.ecosistema.model.Provincia;
import com.quintoimpacto.ecosistema.service.abstraction.ProvinciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/provincia")
public class ProvinciaController {

    private final ProvinciaService provinciaService;

    @GetMapping
    public ResponseEntity<List<ProvinciaDTO>> obtenerPorNombre(@RequestParam String nombrePais){

        return ResponseEntity.ok(provinciaService.getAllProvinciasByPais(nombrePais));
    }
}
