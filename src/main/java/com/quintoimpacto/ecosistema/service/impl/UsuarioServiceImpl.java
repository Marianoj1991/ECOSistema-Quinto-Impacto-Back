package com.quintoimpacto.ecosistema.service.impl;

import com.quintoimpacto.ecosistema.dto.UsuarioDTO;
import com.quintoimpacto.ecosistema.dto.UsuarioUpdateRequest;
import com.quintoimpacto.ecosistema.model.Usuario;
import com.quintoimpacto.ecosistema.model.enums.RolesUsuario;
import com.quintoimpacto.ecosistema.repository.UsuarioRepository;
import com.quintoimpacto.ecosistema.service.abstraction.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService { //TODO: Implementar modelMapper
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public UsuarioDTO getUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findByIdAndDeletedFalse(usuarioId).orElseThrow(() -> new EntityNotFoundException("El usuario con id: " + usuarioId + " no existe en base de datos"));
        return modelMapper.map(usuario, UsuarioDTO.class);

    }


    @Override
    public List<UsuarioDTO> getAllUsuariosActivos() {
        List<Usuario> usuarios = usuarioRepository.findByDeletedFalse();
        if (usuarios.isEmpty()){throw new EntityNotFoundException("La tabla usuarios esta vacia");}

        return usuarios.stream().map(usuario -> modelMapper.map(usuario, UsuarioDTO.class)).toList();
    }

    public Usuario createUsuarioFromGoogleToken(Payload payload){

        String nombre = payload.get("given_name") != null ? payload.get("given_name").toString() : "";
        String apellido = payload.get("family_name") != null ? payload.get("family_name").toString() : "";
        String email = payload.getEmail() != null ? payload.getEmail() : "";
        //String fotoPerfil =  payload.get("picture") != null ? payload.get("given_name").toString() : "";
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .rol(RolesUsuario.USUARIO)
                .telefono("")
                .deleted(false)
                .productoyservicioCantidad(0)
                .build();
        usuarioRepository.save(usuario);
        return usuario;

    }

    @Override
    public UsuarioDTO createUsuario(UsuarioUpdateRequest usuario) {
        Usuario usuarioSave = new Usuario();
        usuarioSave.setNombre(usuario.getNombre());
        usuarioSave.setApellido(usuario.getApellido());
        usuarioSave.setTelefono(usuario.getTelefono());
        usuarioSave.setDeleted(false);
        usuarioSave.setProductoyservicioCantidad(0);
        usuarioSave.setRol(RolesUsuario.USUARIO);
        usuarioRepository.save(usuarioSave);
        return modelMapper.map(usuarioSave, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO updateUsuario(UsuarioUpdateRequest usuario, Authentication authUser) {
        Usuario actualUser = (Usuario) authUser.getPrincipal();
        Usuario usuarioUpdate = usuarioRepository.findByEmail(actualUser.getEmail()).orElseThrow(() -> new EntityNotFoundException("El usuario con email: " + actualUser.getEmail() + " no existe en base de datos"));
        //TODO: simplificar esto
        if (usuarioUpdate != null) {
            if (usuario.getNombre() != null) {
                usuarioUpdate.setNombre(usuario.getNombre());
            }
            if (usuario.getApellido() != null) {
                usuarioUpdate.setApellido(usuario.getApellido());
            }
            if (usuario.getTelefono() != null) {
                usuarioUpdate.setTelefono(usuario.getTelefono());
            }
            usuarioRepository.save(usuarioUpdate);
        }
        //retorna dto
        return modelMapper.map(usuarioUpdate, UsuarioDTO.class);

    }

    @Override
    public void deleteUsuario(Authentication authUser) {
        Usuario actualUser = (Usuario) authUser.getPrincipal();
        Usuario usuarioDelete = usuarioRepository.findByEmail(actualUser.getEmail()).orElseThrow(() -> new EntityNotFoundException("El usuario con email: " + actualUser.getEmail() + " no existe en base de datos"));
        usuarioDelete.setDeleted(true);
        usuarioRepository.save(usuarioDelete);
    }

    //TODO: Borrar cualquier usuario si tenes el rol de ADMIN ??
}
