package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.dto.UsuarioDTO;
import com.quintoimpacto.ecosistema.dto.UsuarioUpdateRequest;
import com.quintoimpacto.ecosistema.model.Usuario;
import com.quintoimpacto.ecosistema.service.abstraction.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long usuarioId){
        return new ResponseEntity<>(usuarioService.getUsuario(usuarioId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios(){
        return new ResponseEntity<>(usuarioService.getAllUsuariosActivos(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioUpdateRequest usuario){
        return new ResponseEntity<>(usuarioService.createUsuario(usuario), HttpStatus.CREATED);
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody UsuarioUpdateRequest usuario){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(usuarioService.updateUsuario(usuario, authUser), HttpStatus.OK);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long usuarioId){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        usuarioService.deleteUsuario(authUser);
        return ResponseEntity.ok().build();
    }
}
