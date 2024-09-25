package com.quintoimpacto.ecosistema.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {

    private final CategoriaSeeder categoriaSeeder;
    private final PaisYProvinciaSeeder paisYProvinciaSeeder;
    private final UsuarioAdminSeeder usuarioAdminSeeder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        categoriaSeeder.seed();
        paisYProvinciaSeeder.seed();
        usuarioAdminSeeder.seed();


    }
}
