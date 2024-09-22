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

    //Obtener un recurso en particular por ID
    public RecursoDTO getRecursoById(Long id) throws NotFoundException{
        Optional<Recurso> recurso = recursoRepository.findById(id);
        if(recurso.isEmpty()){
            throw new NotFoundException("No se encontró el recurso solicitado");
        } else {
            return RecursoMapper.toDTO(recurso.get());
        }
    }

    //Método POST para Recursos
    @Transactional
    public RecursoDTO create(RecursoDTO body) throws BadRequestException{

        if(body.getNombre().isEmpty()){
            throw new BadRequestException("Ingrese nombre del recurso");
        }
        //Ojo, capaz conviene separarlo
        if(body.getNombre().length() > 50 || body.getDescripcion().length() > 200) {
            throw new BadRequestException("Se superó el límite de carácteres aceptados");
        }

        //VALIDACIÓN MOMENTÁNEA
        if(body.getActivo() == null){
            throw new BadRequestException("Ingrese el estado del recurso");
        }

        // Mapeo del DTO a la entidad
        Recurso recurso = RecursoMapper.toEntity(body);

        // Guardar la entidad en la base de datos
        Recurso recursoGuardado = recursoRepository.save(recurso);

        // Retornar el DTO mapeado desde la entidad guardada
        return RecursoMapper.toDTO(recursoGuardado);
    }

    // Método DELETE para modificar algún recurso de la BBDD
    @Transactional
    public Boolean logicDelete(Long id) throws NotFoundException {
        Optional<Recurso> recursoOptional = recursoRepository.findById(id);
        if(recursoOptional.isPresent()) {
            Recurso recurso = recursoOptional.get();
            recurso.setActivo(false);
            recursoRepository.save(recurso);
            return true;
        } else {
            throw new NotFoundException("No se encontró el recurso");
        }
    }

    //Métodos PUT para actualizar recursos de la BBDD
    @Transactional
    public RecursoDTO update(Long id, RecursoDTO recursoDTO) throws NotFoundException {
        if(recursoRepository.existsById(id)) {
            Recurso recursoNuevo = RecursoMapper.toEntity(recursoDTO);
            recursoNuevo.setId(id);
            Recurso recursoGuardado = recursoRepository.save(recursoNuevo);
            return RecursoMapper.toDTO(recursoGuardado);
        } else {
            throw new NotFoundException("No se encontró el recurso deseado");
        }
    }
}
