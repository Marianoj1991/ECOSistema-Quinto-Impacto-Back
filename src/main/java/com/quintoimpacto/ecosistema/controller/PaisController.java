package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.dto.PaisDTO;
import com.quintoimpacto.ecosistema.model.Pais;
import com.quintoimpacto.ecosistema.service.abstraction.PaisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pais")
public class PaisController {

    private final PaisService paisService;

    @GetMapping
    public ResponseEntity<List<PaisDTO>> getAllPais(){
        return ResponseEntity.ok(paisService.getAllPaises());
    }

}
