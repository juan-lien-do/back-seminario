package com.example.demo.Depositos.serivce;

import com.example.demo.Depositos.domain.Deposito;
import com.example.demo.Depositos.dto.DepositoDTO;
import com.example.demo.Depositos.mapper.DepositoMapper;
import com.example.demo.Depositos.repository.DepositoRepository;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service

public class DepositoService {

    @Autowired
    private final DepositoRepository depositoRepository;

    //Obtener un depósito en particular
    public DepositoDTO getDepositoById(Long id) throws NotFoundException {
        Optional<Deposito> deposito = depositoRepository.findById(id);
        if (deposito.isEmpty()) {
            throw new NotFoundException("No es encontró el depósito");
        } else {
            return DepositoMapper.toDTO(deposito.get());
        }
    }
}
