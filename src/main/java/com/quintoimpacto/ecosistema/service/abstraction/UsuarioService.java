package com.quintoimpacto.ecosistema.service.abstraction;

import com.quintoimpacto.ecosistema.dto.UsuarioDTO;
import com.quintoimpacto.ecosistema.dto.UsuarioUpdateRequest;
import com.quintoimpacto.ecosistema.model.Usuario;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO getUsuario(Long usuarioId);
    List<UsuarioDTO> getAllUsuariosActivos();
    UsuarioDTO createUsuario(UsuarioUpdateRequest usuario);
    UsuarioDTO updateUsuario(UsuarioUpdateRequest usuario, Authentication authUser);
    void deleteUsuario(Authentication authUser);
}
