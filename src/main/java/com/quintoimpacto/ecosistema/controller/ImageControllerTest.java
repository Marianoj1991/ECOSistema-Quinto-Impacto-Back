package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.service.impl.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageControllerTest {

    private final ImagenService imagenService;


   /* @PostMapping("/image-test")
    public ResponseEntity<String> uploadImageTest(@RequestParam MultipartFile images, Authentication authentication){
        authentication.
        imagenService.uploadProveedorImage(images);
        return ResponseEntity.ok().build();
    }*/
}

