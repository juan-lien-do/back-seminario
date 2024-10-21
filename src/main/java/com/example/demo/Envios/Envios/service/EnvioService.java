package com.example.demo.Envios.Envios.service;

import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.dto.EnvioDTO;
import com.example.demo.Envios.Envios.mapper.EnvioMapper;
import com.example.demo.Envios.Envios.repository.EnvioRepository;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
//@NoArgsConstructor
@Service

public class EnvioService {

    @Autowired
    private final EnvioRepository envioRepository;

    // ------------------------------------ MÉTODOS GET ------------------------------------------------------//

    //Obtener todos los Envios de la BBDD
    public List<EnvioDTO> getAllEnvios(){
        List<Envio> listEnvios = envioRepository.findAll();
        return listEnvios.stream()
                .map(EnvioMapper::toDTO)
                .toList();
    }

    //Obtener un envío en particular
    public EnvioDTO getEnvioById(Long id) throws NotFoundException {
        Optional<Envio> envio = envioRepository.findById(id);
        if (envio.isEmpty()) {
            throw new NotFoundException("No se encontró el recurso solicitado");
        } else {
            return EnvioMapper.toDTO(envio.get());
        }
    }

    // ------------------------------------ MÉTODOS POST ------------------------------------------------------//
    @Transactional
    public EnvioDTO create(EnvioDTO body) throws BadRequestException {

        Envio envio = EnvioMapper.toEntity(body);
        Envio envioGuardado = envioRepository.save(envio);

        return EnvioMapper.toDTO(envioGuardado);
    }

}
