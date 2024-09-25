package com.quintoimpacto.ecosistema.service.impl;

import com.quintoimpacto.ecosistema.dto.PaisDTO;
import com.quintoimpacto.ecosistema.model.Pais;
import com.quintoimpacto.ecosistema.repository.PaisRepository;
import com.quintoimpacto.ecosistema.service.abstraction.PaisService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PaisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PaisDTO> getAllPaises() {
        List<Pais> paises = paisRepository.findAll();
        if (paises.isEmpty()){throw new NoSuchElementException("no hay paises para mostrar");
        }
        return paises.stream().map(pais -> modelMapper.map(pais, PaisDTO.class)).toList();
    }

    @Override
    public Pais getByName(String nombrePais) {
         return paisRepository.findByNombre(nombrePais).orElseThrow(() -> new NoSuchElementException("El pais "+ nombrePais + " no se encuentra en la DB"));
    }


}
