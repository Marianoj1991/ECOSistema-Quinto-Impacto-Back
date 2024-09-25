package com.quintoimpacto.ecosistema.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.quintoimpacto.ecosistema.model.Usuario;
import com.quintoimpacto.ecosistema.repository.UsuarioRepository;
import com.quintoimpacto.ecosistema.service.abstraction.UsuarioService;
import com.quintoimpacto.ecosistema.service.impl.JwtService;
import com.quintoimpacto.ecosistema.service.impl.UsuarioServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//todo: manejo de errores, logger para ver correcto funcionamiento.
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    //Todo: nahuel: fix DI beans
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioServiceImpl usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        //Obtener jwt contenido en el header
        String authorizationHeader = request.getHeader("Authorization"); //bearer jwt
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            //Sacamos el bearer y nos quedamos solo con el jwt
            String jwt = authorizationHeader.split(" ")[1];
            Usuario usuario;


            Payload googleTokenPayload = jwtService.googleVerifierAndPayload(jwt);
            if (googleTokenPayload != null){

                 usuario = usuarioRepository.findByEmail(googleTokenPayload.getEmail()).orElse(null);

                if (usuario == null){
                   usuario = usuarioService.createUsuarioFromGoogleToken(googleTokenPayload);
                }
                String newJwt = jwtService.generateToken(usuario);
                response.setHeader("Authorization","Bearer " + newJwt);
            }
            else{
                Claims payloadJwt = jwtService.verifyAndGetJwtPayload(jwt);
                 usuario = usuarioRepository.findByEmail(payloadJwt.getSubject())
                        .orElseThrow(() -> new EntityNotFoundException("usuario no encontrado"));
                String newJwt = jwtService.generateToken(usuario);
                response.setHeader("Authorization","Bearer " + newJwt);
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    usuario,null,usuario.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
