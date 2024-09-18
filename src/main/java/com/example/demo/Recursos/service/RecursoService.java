package com.example.demo.Recursos.service;

import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.Recursos.mapper.*;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.Recursos.repository.RecursoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class RecursoService {

    @Autowired
    private final RecursoRepository recursoRepository;

    //Obtener todos los recursos de la BBDD
    public List<RecursoDTO> getAllRecursos(){
        List<Recurso> lista = recursoRepository.findAll();
        return lista.stream()
                .map(RecursoMapper::toDTO)
                .toList();
    }

    @Transactional
    //método POST para Recursos
    public RecursoDTO create(RecursoDTO body) throws BadRequestException{

        if(body.getNombre().isEmpty()){
            throw new BadRequestException("Ingrese nombre del recurso");
        }

        // Mapeo del DTO a la entidad
        Recurso recurso = RecursoMapper.toEntity(body);

        // Guardar la entidad en la base de datos
        Recurso recursoGuardado = recursoRepository.save(recurso);

        // Retornar el DTO mapeado desde la entidad guardada
        return RecursoMapper.toDTO(recursoGuardado);
    }
}
