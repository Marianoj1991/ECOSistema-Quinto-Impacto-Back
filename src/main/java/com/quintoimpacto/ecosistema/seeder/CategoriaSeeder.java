package com.quintoimpacto.ecosistema.seeder;

import com.quintoimpacto.ecosistema.model.Categoria;
import com.quintoimpacto.ecosistema.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;



@RequiredArgsConstructor
@Configuration
public class CategoriaSeeder {

    private final CategoriaRepository categoriaRepository;

    public void seed(){
        //Si la tabla categoria esta vacia (en BDD) agrega todas las categorias y salimos del metodo run
        if (categoriaRepository.count() == 0) {
            categoriaRepository.saveAll(getCategoriasList());
            return;
        }

        //Si alguna categoria en la base de datos no esta presente se agrega
        List<Categoria> categoriasFaltantes = new ArrayList<>();
        for (Categoria categoria : getCategoriasList()) {
            if (!categoriaRepository.existsByNombre(categoria.getNombre())) {
                categoriasFaltantes.add(categoria);
            }
        }
        categoriaRepository.saveAll(categoriasFaltantes);
    }


    //Lista de todas las categorias
    public List<Categoria> getCategoriasList() {
        List<Categoria> categorias = new ArrayList<>();

        categorias.add(new Categoria("Bienestar"));
        categorias.add(new Categoria("Capacitaciones"));
        categorias.add(new Categoria("Construcción"));
        categorias.add(new Categoria("Cultivos"));
        categorias.add(new Categoria("Gastronomía"));
        categorias.add(new Categoria("Indumentaría"));
        categorias.add(new Categoria("Merchandising"));
        categorias.add(new Categoria("Muebles/Deco"));
        categorias.add(new Categoria("Reciclaje"));
        categorias.add(new Categoria("Tecnología"));
        categorias.add(new Categoria("Transporte"));
        return categorias;
    }
}
