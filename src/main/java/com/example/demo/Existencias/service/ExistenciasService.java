package com.example.demo.Existencias.service;

import com.example.demo.Depositos.domain.Deposito;
import com.example.demo.Depositos.repository.DepositoRepository;
import com.example.demo.Existencias.domain.Existencia;
import com.example.demo.Existencias.dto.ExistenciaRequestDTO;
import com.example.demo.Existencias.repository.ExistenciasRepository;
import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Recursos.repository.RecursoRepository;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExistenciasService {
    @Autowired
    private final ExistenciasRepository existenciasRepository;
    @Autowired
    private final RecursoRepository recursoRepository;
    @Autowired
    private final DepositoRepository depositoRepository;

    public void incorporarExistencias(ExistenciaRequestDTO existenciaRequestDTO) throws NotFoundException {
        Long idRecurso = existenciaRequestDTO.getIdRecurso();
        List<Existencia> existencias = existenciasRepository.findByIdrecurso(idRecurso);

        List<Existencia> existenciasFiltradas = existencias.stream()
                .filter(x -> Objects.equals(x.getDeposito().getIdDeposito(), existenciaRequestDTO.getIdDeposito())).toList();
        if (existenciasFiltradas.isEmpty()) {
            Optional<Recurso> rec = recursoRepository.findById(existenciaRequestDTO.getIdRecurso());
            if (rec.isEmpty()) throw new NotFoundException("Recurso no encontrado.");
            Optional<Deposito> dep = depositoRepository.findById(existenciaRequestDTO.getIdDeposito());
            if (dep.isEmpty()) throw new NotFoundException("Deposito no encontrado");

            Existencia ex = Existencia.builder()
                    .recurso(rec.get())
                    .deposito(dep.get())
                    .cantidad(existenciaRequestDTO.getCantidad())
                    .build();
            existenciasRepository.save(ex);
        } else {
            Existencia ex = existenciasFiltradas.stream().findFirst().get();
            ex.setCantidad(ex.getCantidad() + existenciaRequestDTO.getCantidad());
            existenciasRepository.save(ex);
        }
    }

    // TODO !!!!!!!!!!!!
    public void disminuirExistencias(ExistenciaRequestDTO existenciaRequestDTO) throws NotFoundException, BadRequestException {


        Long idRecurso = existenciaRequestDTO.getIdRecurso();
        List<Existencia> existencias = existenciasRepository.findByIdrecurso(idRecurso);

        List<Existencia> existenciasFiltradas = existencias.stream()
                .filter(x -> Objects.equals(x.getDeposito().getIdDeposito(), existenciaRequestDTO.getIdDeposito())).toList();
        if (existenciasFiltradas.isEmpty()) {
            throw new NotFoundException("Existencia no encontrada.");
        } else {
            Existencia ex = existenciasFiltradas.stream().findFirst().get();
            Long cantidadFinal = ex.getCantidad() - existenciaRequestDTO.getCantidad();
            if (cantidadFinal < 0)
                throw new BadRequestException("La cantidad de la existencia no puede quedar por debajo de 0.");
            ex.setCantidad(cantidadFinal);
            existenciasRepository.save(ex);
        }
    }

    public void aumentarExistencias(Long idExistencia, Long cantidad) throws NotFoundException, BadRequestException {

        Optional<Existencia> existenciaOptional = existenciasRepository.findById(idExistencia);

        if (existenciaOptional.isEmpty()){
            throw new NotFoundException("Existencia no encontrada.");
        } else {
            Existencia ex = existenciaOptional.get();
            Long cantidadFinal = ex.getCantidad() + cantidad;
            ex.setCantidad(cantidadFinal);
            existenciasRepository.save(ex);
        }
    }
}
