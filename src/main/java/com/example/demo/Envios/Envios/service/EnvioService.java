package com.example.demo.Envios.Envios.service;

import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.dto.EnvioDTO;
import com.example.demo.Envios.Envios.mapper.EnvioMapper;
import com.example.demo.Envios.Envios.repository.EnvioRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
//@NoArgsConstructor
@Service

public class EnvioService {

    @Autowired
    private final EnvioRepository envioRepository;

    //Obtener todos los Envios de la BBDD
    public List<EnvioDTO> getAllEnvios(){
        List<Envio> listEnvios = envioRepository.findAll();
        return listEnvios.stream()
                .map(EnvioMapper::toDTO)
                .toList();
    }

}
