package com.quintoimpacto.ecosistema.service.impl;



import com.quintoimpacto.ecosistema.dto.ProvinciaDTO;
import com.quintoimpacto.ecosistema.model.Pais;
import com.quintoimpacto.ecosistema.model.Provincia;
import com.quintoimpacto.ecosistema.repository.PaisRepository;
import com.quintoimpacto.ecosistema.repository.ProvinciaRepository;
import com.quintoimpacto.ecosistema.service.abstraction.ProvinciaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;




@Service
@RequiredArgsConstructor
public class ProvinciaServiceImpl implements ProvinciaService {

    private final ProvinciaRepository provinciaRepository;
    private final PaisRepository paisRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProvinciaDTO> getAllProvinciasByPais(String paisNombre){
        Pais pais = paisRepository.findByNombre(paisNombre)
                .orElseThrow(() -> new NoSuchElementException("Pais: "+ paisNombre +" no encontrado"));

        List<Provincia> provincias = provinciaRepository.findByPais(pais)
                .orElseThrow(() -> new NoSuchElementException("No se encontraron provincias para el pais: " + paisNombre));

        if (provincias.isEmpty()){throw new EntityNotFoundException("La lista provincias esta vacia");}

        return provincias.stream().map(provincia -> modelMapper.map(provincia, ProvinciaDTO.class)).toList();

    }

    @Override
    public Provincia getProvinciaByNombreYPais(String provinciaNombre, Pais pais) {
        return provinciaRepository.findByNombreAndPais(provinciaNombre, pais).orElseThrow(
                () -> new EntityNotFoundException("La provincia "+ provinciaNombre + " no pertenece a " + pais.getNombre() + " no existe o no se encontro"));
    }
}
