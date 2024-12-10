package com.example.demo.Computadoras.service;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.dto.ComputadoraDTO;
import com.example.demo.Computadoras.mapper.ComputadoraMapper;
import com.example.demo.Computadoras.repository.ComputadoraRepository;
import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.Recursos.mapper.RecursoMapper;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class ComputadoraService {

    @Autowired
    private final ComputadoraRepository computadoraRepository;

        // ----------------------- MÉTODOS GET ----------------------- //

    //Post de computadora - TODOS LOS RECURSOS
    public List<ComputadoraDTO> getAllComputadoras() {
        List<Computadora> lista = computadoraRepository.findAll();
        return lista.stream()
                .map(ComputadoraMapper::toDTO)
                .toList();
    }

    //Obtener un computadora en particular por ID
    public ComputadoraDTO getComputadoraById(Long id) throws NotFoundException {
        Optional<Computadora> compuradora = computadoraRepository.findById(id);
        if (compuradora.isEmpty()) {
            throw new NotFoundException("No se encontró el compuradora solicitado");
        } else {
            return ComputadoraMapper.toDTO(compuradora.get());
        }
    }

    //Obtener todos los ACTIVOS
    public List<ComputadoraDTO> getAllComputadorasByActivo(Boolean esActivo){
        return computadoraRepository.findByEsActivo(esActivo).stream().map(ComputadoraMapper::toDTO).toList();
    }

    //Obtener por Filtro de TipoComputadora y esMaterizado
    public List<ComputadoraDTO> getAllComputadorasTipo(Long idTipo) {
        List<Computadora> lista = computadoraRepository.findByIdTipo(idTipo);
        return lista.stream()
                .map(ComputadoraMapper::toDTO)
                .toList();
    }
    //Obtener todos los Masterizados
    public List<ComputadoraDTO> getAllMasterizados(Boolean esMasterizado){
        return computadoraRepository.findByEsMasterizado(esMasterizado).stream().map(ComputadoraMapper::toDTO).toList();
    }

        // ----------------------- MÉTODOS POST ----------------------- //

    //Crear una computadora y registrarla en la BBDD
    @Transactional
    public ComputadoraDTO create(ComputadoraDTO body) throws BadRequestException {

        if (body.getNroSerie().isEmpty()) {
            throw new BadRequestException("Ingrese el NroSerie del Equipo");
        }
        if (body.getNroSerie().length() > 50 || body.getDescripcion().length() > 300) {
            throw new BadRequestException("Se superó el límite de carácteres aceptados");
        }

        if (body.getIdDeposito() < 1 || body.getIdDeposito() > 3) {
            throw new BadRequestException("El idDeposito debe estar entre 1 y 3.");
        }

        //Verfica que el nro de WS no haya sido cargado previamente
        if (body.getEsMasterizado() && computadoraRepository.existsByNroWs(body.getNroWs())) {
            throw new BadRequestException("El nroWs ya está en uso, debe ser único.");
        }

        //VALIDACIÓN MOMENTÁNEA
        if (body.getEsActivo() == null) {
            throw new BadRequestException("El campo 'esActivo' es obligatorio.");
        } else if (body.getEsMasterizado() == null) {
            throw new BadRequestException("El campo 'esMasterizado' es obligatorio.");
        } else if (body.getIdDeposito() == null) {
            throw new BadRequestException("El campo 'idDeposito' es obligatorio.");
        } else if (body.getIdTipo() == null) {
            throw new BadRequestException("El campo 'idTipo' es obligatorio.");
        }

        Computadora computadora = ComputadoraMapper.toEntity(body);
        Computadora computadoraGuardada = computadoraRepository.save(computadora);
        return ComputadoraMapper.toDTO(computadoraGuardada);
    }

        // ----------------------- MÉTODOS PUT/PATCH ----------------------- //

    //Modificaciones a objetos de tipo Computadora
    @Transactional
    public ComputadoraDTO update(Long id, ComputadoraDTO computadoraDTO) throws NotFoundException {
        if(computadoraRepository.existsById(id)) {
            Computadora computadoraNuevo = ComputadoraMapper.toEntity(computadoraDTO);
            computadoraNuevo.setIdComputadora(id);
            Computadora computadoraGuardado = computadoraRepository.save(computadoraNuevo);
            return ComputadoraMapper.toDTO(computadoraGuardado);
        } else {
            throw new NotFoundException("No se encontró el equipo deseado");
        }
    }

        // ----------------------- MÉTODOS DELETE ----------------------- //

    // Método DELETE para modificar algún recurso de la BBDD
    @Transactional
    public Boolean logicDelete(Long id) throws NotFoundException {
        Optional<Computadora> computadoraOptional = computadoraRepository.findById(id);
        if(computadoraOptional.isPresent()) {
            Computadora computadora = computadoraOptional.get();
            computadora.setEsActivo(false);
            computadoraRepository.save(computadora);
            return true;
        } else {
            throw new NotFoundException("No se encontró el equipo");
        }
    }

    @Transactional
    public Boolean logicUndelete(Long id) throws NotFoundException {
        Optional<Computadora> computadoraOptional = computadoraRepository.findById(id);
        if(computadoraOptional.isPresent()) {
            Computadora computadora = computadoraOptional.get();
            computadora.setEsActivo(true);
            computadoraRepository.save(computadora);
            return true;
        } else {
            throw new NotFoundException("No se encontró el equipo");
        }
    }

    @Transactional
    //TODO función que actualice el enUso de una computadora
    public void actualizarEnUso(Long idComputadora, Boolean enUso) throws NotFoundException, BadRequestException {
        Optional<Computadora> computadoraOptional = computadoraRepository.findById(idComputadora);
        if(computadoraOptional.isPresent()) {
            Computadora computadora = computadoraOptional.get();
            computadora.setEnUso(enUso);
            computadoraRepository.save(computadora);
            return;
        } else {
            throw new NotFoundException("No se encontró el equipo");
        }
    }

}
