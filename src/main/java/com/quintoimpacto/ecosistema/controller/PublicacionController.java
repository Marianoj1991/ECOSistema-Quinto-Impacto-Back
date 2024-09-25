package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.dto.*;
import com.quintoimpacto.ecosistema.model.Publicacion;
import com.quintoimpacto.ecosistema.service.abstraction.PublicacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService publicacionService;

    @GetMapping("/inicio/publicacion/{publicacionId}")
    public ResponseEntity<PublicacionDTO> obtenerPublicacionNoOcultaPorId(@PathVariable Long publicacionId) throws Exception {
        PublicacionDTO publicacion = publicacionService.getPublicacionNoOcultaById(publicacionId);
        return new ResponseEntity<>(publicacion, HttpStatus.OK);
    }

    @PatchMapping("/inicio/publicacion/{id}/incrementar-visualizacion")
    public ResponseEntity<Void> aumentarVisualizacion(@PathVariable Long id){
        publicacionService.aumentarVisualizacion(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inicio/publicacion")
    public ResponseEntity<List<PublicacionDTO>> obtenerPublicacionesNoOcultas() {
        List<PublicacionDTO> activePublicaciones = publicacionService.getAllPublicacionesNoOcultas();
        return ResponseEntity.ok(activePublicaciones);
    }

    @GetMapping("/admin/publicacion/dashboard")
    public ResponseEntity<List<PublicacionDashboardDTO>> dataDashboard(){
        return ResponseEntity.ok(publicacionService.dataDashboard());
    }

    @GetMapping("/admin/publicacion/{publicacionId}")
    public ResponseEntity<PublicacionAdminDTO> obtenerPublicacionPorId(@PathVariable Long publicacionId){
        return ResponseEntity.ok(publicacionService.getPublicacionById(publicacionId));
    }

    @GetMapping("/admin/publicacion")
    public ResponseEntity<List<PublicacionAdminDTO>> obtenerPublicaciones(){
        return ResponseEntity.ok(publicacionService.getAllPublicaciones());
    }
    @PostMapping("/admin/publicacion")
    public ResponseEntity<String> crearPublicacion(@ModelAttribute PublicacionRequest publicacionRequest, @RequestParam(required = false) MultipartFile imagen1,
                                                   @RequestParam(required = false) MultipartFile imagen2,
                                                   @RequestParam(required = false) MultipartFile imagen3){

        List<ImagenesRequestDTO> imagenes = new ArrayList<>();
        if (imagen1 != null && !imagen1.isEmpty()){
            imagenes.add(new ImagenesRequestDTO(imagen1,  1));
        }
        if (imagen2 != null && !imagen2.isEmpty()){
            imagenes.add(new ImagenesRequestDTO(imagen2,  2));
        }
        if (imagen3 != null && !imagen3.isEmpty()){
            imagenes.add(new ImagenesRequestDTO(imagen3, 3));
        }
        return ResponseEntity.ok(publicacionService.crearPublicacion(publicacionRequest, imagenes));
    }

    @PutMapping("/admin/publicacion/{publicacionId}")
    public ResponseEntity<String> actualizarPublicacion(@PathVariable Long publicacionId,@Valid  @ModelAttribute PublicacionRequest publicacion, @RequestParam(required = false) MultipartFile imagen1,
                                                        @RequestParam(required = false) MultipartFile imagen2,
                                                        @RequestParam(required = false) MultipartFile imagen3) throws Exception {
        List<ImagenesRequestDTO> imagenes = new ArrayList<>();
        if (imagen1 !=null && !imagen1.isEmpty()){
            imagenes.add(new ImagenesRequestDTO(imagen1, 1));
        }
        if (imagen2 !=null && !imagen2.isEmpty()){
            imagenes.add(new ImagenesRequestDTO(imagen1, 2));
        }
        if (imagen3 !=null && !imagen3.isEmpty()){
            imagenes.add(new ImagenesRequestDTO(imagen1, 3));
        }

        return new ResponseEntity<>(publicacionService.updatePublicacion(publicacionId, publicacion, imagenes), HttpStatus.OK);
    }

    @DeleteMapping("/admin/publicacion/{publicacionId}")
    public ResponseEntity<Void> desactivarPublicacion(@PathVariable Long publicacionId) throws Exception {
        publicacionService.bajaPublicacion(publicacionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin/publicacion/{publicacionId}/ocultar")
    public ResponseEntity<Void> ocultarPublicacion(@PathVariable Long publicacionId){
        publicacionService.ocultarPublicacion(publicacionId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/admin/publicacion/{publicacionId}/mostrar")
    public ResponseEntity<Void> activarPublicacion(@PathVariable Long publicacionId){
        publicacionService.desocultarPublicacion(publicacionId);
        return ResponseEntity.ok().build();
    }


}
