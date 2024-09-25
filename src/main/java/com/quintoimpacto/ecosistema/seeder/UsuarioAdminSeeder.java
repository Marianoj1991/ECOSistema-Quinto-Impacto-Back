package com.quintoimpacto.ecosistema.seeder;

import com.quintoimpacto.ecosistema.model.Usuario;
import com.quintoimpacto.ecosistema.model.enums.RolesUsuario;
import com.quintoimpacto.ecosistema.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UsuarioAdminSeeder {

    private final UsuarioRepository usuarioRepository;

    public void seed(){
        boolean adminExiste = usuarioRepository.findByEmail("ecosistema.dev@gmail.com").isPresent();
        if (!adminExiste) {
            usuarioRepository.save(Usuario.builder()
                    .email("ecosistema.dev@gmail.com")
                    .nombre("eco")
                    .telefono("123456789")
                    .apellido("eco")
                    .productoyservicioCantidad(0)
                    .deleted(false)
                    .rol(RolesUsuario.ADMIN)
                    .build());
        }
    }

}
