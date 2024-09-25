package com.quintoimpacto.ecosistema.seeder;

import com.quintoimpacto.ecosistema.model.Pais;
import com.quintoimpacto.ecosistema.model.Provincia;
import com.quintoimpacto.ecosistema.repository.PaisRepository;
import com.quintoimpacto.ecosistema.repository.ProvinciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Configuration
public class PaisYProvinciaSeeder {


    private final PaisRepository paisRepository;
    private final ProvinciaRepository provinciaRepository;


    // Todo: agregar mas paises , mejorar el codigo, ver si deberia de verificarse si algun pais o provincia falta en su tabla y persistirse
    public void seed(){

        //si la tabla pais y provincia estan vacias en la DB se llenan con su respectivas relaciones entre pais y provincia
        if (paisRepository.count() == 0 && provinciaRepository.count() == 0){

            Pais argentina = new Pais("Argentina");
            Pais uruguay = new Pais("Uruguay");
            Pais colombia = new Pais("Colombia");

            argentina.setProvincias(listaProvinciasArgentina(argentina));
            uruguay.setProvincias(listaProvinciasUruguay(uruguay));
            colombia.setProvincias(listaProvinciasColombia(colombia));

            paisRepository.saveAll(Arrays.asList(argentina,uruguay, colombia));

        }

    }

    public List<Provincia> listaProvinciasArgentina(Pais argentina) {

        List<String> provinciasArgentina = Arrays.asList(
                "Córdoba", "San Luis", "Misiones", "San Juan", "Entre Ríos",
                "Santa Cruz", "Río Negro", "Chubut", "Mendoza", "La Rioja",
                "Catamarca", "La Pampa", "Santiago del Estero", "Corrientes",
                "Santa Fe", "Tucumán", "Neuquén", "Salta", "Chaco", "Formosa",
                "Jujuy", "Buenos Aires", "Ciudad Autónoma de Buenos Aires",
                "Tierra del Fuego"
        );

        return provinciasArgentina.stream().map(nombreProvincia-> new Provincia(nombreProvincia,argentina)).collect(Collectors.toList());
    }

    public List<Provincia> listaProvinciasUruguay(Pais uruguay) {

        List<String> provinciasUruguay = Arrays.asList(
                "Artigas", "Canelones", "Cerro Largo", "Colonia", "Durazno",
                "Flores", "Florida", "Lavalleja", "Maldonado", "Montevideo",
                "Paysandú", "Río Negro", "Rivera", "Rocha", "Salto",
                "San José", "Soriano", "Tacuarembó", "Treinta y Tres");

        return provinciasUruguay.stream().map(nombreProvincia-> new Provincia(nombreProvincia,uruguay)).collect(Collectors.toList());
    }

    public List<Provincia> listaProvinciasColombia(Pais colombia) {

        List<String> provinciasColombia = Arrays.asList(
                "Amazonas", "Antioquia", "Arauca", "Atlántico", "Bolívar",
                "Boyacá", "Caldas", "Caquetá", "Casanare", "Cauca",
                "Cesar", "Chocó", "Córdoba", "Cundinamarca", "Guainía",
                "Guaviare", "Huila", "La Guajira", "Magdalena", "Meta",
                "Nariño", "Norte de Santander", "Putumayo", "Quindío",
                "Risaralda", "San Andrés y Providencia", "Santander",
                "Sucre", "Tolima", "Valle del Cauca", "Vaupés", "Vichada"
        );


        return provinciasColombia.stream().map(nombreProvincia-> new Provincia(nombreProvincia,colombia)).collect(Collectors.toList());
    }
}
