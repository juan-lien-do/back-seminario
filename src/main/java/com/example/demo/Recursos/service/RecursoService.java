package com.example.demo.Recursos.service;

import com.example.demo.Depositos.domain.Deposito;
import com.example.demo.Depositos.repository.DepositoRepository;
import com.example.demo.Depositos.serivce.DepositoService;
import com.example.demo.Existencias.domain.Existencia;
import com.example.demo.Existencias.dto.ExistenciaDTO;
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

    @Autowired
    private final DepositoService depositoService;

    //Obtener todos los recursos de la BBDD
    public List<RecursoDTO> getAllRecursos() {
        List<Recurso> lista = recursoRepository.findAll();
        return lista.stream()
                .map(RecursoMapper::toDTO)
                .toList();
    }

    //Obtener todos los recursos de la BBDD filtrados por categoria
    public List<RecursoDTO> getAllRecursosCat(Long categoria) {
        List<Recurso> lista = recursoRepository.findByCategoria(categoria);
        return lista.stream()
                .map(RecursoMapper::toDTO)
                .toList();
    }

    //Obtener un recurso en particular por ID
    public RecursoDTO getRecursoById(Long id) throws NotFoundException {
        Optional<Recurso> recurso = recursoRepository.findById(id);
        if (recurso.isEmpty()) {
            throw new NotFoundException("No se encontró el recurso solicitado");
        } else {
            return RecursoMapper.toDTO(recurso.get());
        }
    }

    /* Método que por el momento funcionaba
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
    } */

    /* Este me tiro para resolver chatGPT pero no puede resolver la verificacion de existencias
    @Transactional
    public RecursoDTO create(RecursoDTO body) throws BadRequestException, NotFoundException {

        if (body.getNombre().isEmpty()) {
            throw new BadRequestException("Ingrese nombre del recurso");
        }
        if (body.getNombre().length() > 50 || body.getDescripcion().length() > 200) {
            throw new BadRequestException("Se superó el límite de carácteres aceptados");
        }

        if (body.getActivo() == null) {
            throw new BadRequestException("Ingrese el estado del recurso");
        }

        // Mapeo del DTO a la entidad
        Recurso recurso = RecursoMapper.toEntity(body);

        // Guardar el recurso en la base de datos
        Recurso recursoGuardado = recursoRepository.save(recurso);

        // Verificar si se pasaron parámetros para crear existencias
        if (body.getExistencias() != null && !body.getExistencias().isEmpty()) {
            for (ExistenciaDTO existenciaDTO : body.getExistencias()) {

                // Verificar que el idDepo sea 1, 2 o 3
                Long idDeposito = existenciaDTO.getDeposito().getId();
                if (idDeposito != 1 && idDeposito != 2 && idDeposito != 3) {
                    throw new BadRequestException("El depósito debe ser 1, 2 o 3");
                }

                // Traer el objeto Deposito desde la base de datos
                Deposito deposito = depositoService.getDepositoById(idDeposito);

                // Crear la nueva instancia de Existencia
                Existencia nuevaExistencia = new Existencia();
                nuevaExistencia.setCantidad(existenciaDTO.getCantidad());
                nuevaExistencia.setRecurso(recursoGuardado);  // Asocia el recurso guardado
                nuevaExistencia.setDeposito(deposito);  // Asocia el depósito

                // Guardar la existencia en la base de datos
                existenciaRepository.save(nuevaExistencia);
            }
        }

        // Retornar el DTO mapeado desde la entidad guardada
        return RecursoMapper.toDTO(recursoGuardado);
    } */

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
