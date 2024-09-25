package com.quintoimpacto.ecosistema.config;

import com.quintoimpacto.ecosistema.dto.UsuarioUpdateRequest;
import com.quintoimpacto.ecosistema.service.abstraction.UsuarioService;
import com.quintoimpacto.ecosistema.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final JwtFilter jwtFilter;
    private final UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authConfig -> {

                    //Login-Sign in
                    authConfig.requestMatchers(HttpMethod.GET,"/login").permitAll();

                    //usuario
                    authConfig.requestMatchers(HttpMethod.GET, "/usuarios/**").permitAll();
                    authConfig.requestMatchers(HttpMethod.PUT, "/usuarios/**").authenticated();
                    authConfig.requestMatchers(HttpMethod.DELETE,"/usuarios/**").authenticated();
                    authConfig.requestMatchers(HttpMethod.GET,"/usuarios").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST,"/usuarios").hasRole("ADMIN");

                    //Publicacion
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/publicacion/dashboard").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.POST,"/admin/publicacion").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.PUT,"/admin/publicacion/*/mostrar").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.PUT,"/admin/publicacion/*/ocultar").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.PUT, "/admin/publicacion/*").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.DELETE,"/admin/publicacion/*").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/publicacion").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/publicacion/*").hasRole("ADMIN");

                    authConfig.requestMatchers(HttpMethod.GET,"/inicio/publicacion/*").permitAll();
                    authConfig.requestMatchers(HttpMethod.PATCH,"/inicio/publicacion/*/incrementar-visualizacion").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET,"/inicio/publicacion").permitAll();
                    // ProductoServicio
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/dashboard").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/inicio/producto-servicio/*").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET,"/inicio/producto-servicio").permitAll();
                    authConfig.requestMatchers(HttpMethod.GET,"/perfil/producto-servicio/mis-productos-y-servicios").authenticated();
                    authConfig.requestMatchers(HttpMethod.GET, "/perfil/producto-servicio/mis-productos-y-servicios/*").authenticated();
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/*").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/obtener-todos").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.PUT,"/admin/producto-servicio/revision/*").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/estado/aceptado").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/estado/denegado").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/estado/revision-inicial").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/estado/requiere-cambios").hasRole("ADMIN");
                    authConfig.requestMatchers(HttpMethod.GET,"/admin/producto-servicio/estado/cambios-realizados").hasRole("ADMIN");

                    authConfig.requestMatchers(HttpMethod.POST, "/perfil/producto-servicio").authenticated();
                    authConfig.requestMatchers(HttpMethod.DELETE,"/perfil/producto-servicio/*").authenticated();
                    authConfig.requestMatchers(HttpMethod.PUT, "/perfil/producto-servicio/*").authenticated();
                    //Provincia
                    authConfig.requestMatchers(HttpMethod.GET, "/provincia/**").permitAll();

                    //Pais
                    authConfig.requestMatchers(HttpMethod.GET, "/pais").permitAll();

                    //Categorias
                    authConfig.requestMatchers(HttpMethod.GET,"/categorias").permitAll();
                    //TODO: las dos de abajo con rol de dev
                    authConfig.requestMatchers("/api-docs/**").permitAll();
                    authConfig.requestMatchers("/swagger-ui/**").permitAll();
                    authConfig.requestMatchers("/v3/api-docs/**").permitAll();

                    //terminar



                    //usuario



                });
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }



}
