package com.example.demo.Empleados.service;

import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Empleados.dto.EmpleadoPostRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoPutRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoResponseDTO;
import com.example.demo.Empleados.mapper.EmpleadoMapper;
import com.example.demo.Empleados.repository.EmpleadoRepository;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmpleadoService {
    @Autowired
    private final EmpleadoRepository empleadoRepository;


    public List<EmpleadoResponseDTO> getAll(Map<String, String> allParams) {
        System.out.println(allParams);
        System.out.println(allParams.get("activo"));

        List<Empleado> empleados1;

        if (allParams.get("activo").isEmpty()){
            empleados1 = empleadoRepository.findByNombreContaining(allParams.get("nombre"));
        } else {
            empleados1 = empleadoRepository.findByNombreContainingAndActivo(allParams.get("nombre"), Objects.equals(allParams.get("activo"), "true"));
        }


        return empleados1.stream().map(EmpleadoMapper::toResponseDTO).toList();
    }

    public String saveEmpleado(EmpleadoPostRequestDTO empleadoPostRequestDTO) throws BadRequestException, Exception{
        List<Empleado> emps = empleadoRepository.findByCuil(empleadoPostRequestDTO.getCuil());
        if (!emps.isEmpty()) throw new BadRequestException("Ya existe un usuario con ese CUIl");
        Empleado res = empleadoRepository.save(EmpleadoMapper.toEntity(empleadoPostRequestDTO));
        return res.getIdEmpleado().toString();
    }

    public String updateEmpleado(EmpleadoPutRequestDTO empleadoDTO) throws NotFoundException, BadRequestException {
        List<Empleado> emps = empleadoRepository.findByCuil(empleadoDTO.getCuil());
        if (!emps.isEmpty() && !Objects.equals(emps.get(0).getIdEmpleado(), empleadoDTO.getIdEmpleado()))
            throw new BadRequestException("Ya existe un usuario con ese CUIl");

        Optional<Empleado> empleadoOpt = empleadoRepository.findById(empleadoDTO.getIdEmpleado());
        if (empleadoOpt.isEmpty()) throw new NotFoundException("No se encontró el empleado");

        empleadoRepository.save(EmpleadoMapper.toEntity(empleadoDTO));

        return empleadoDTO.getIdEmpleado().toString();
    }

    public String activarEmpleado(Long id)throws NotFoundException{
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(id);
        if (empleadoOpt.isEmpty()) throw new NotFoundException("No se encontró el empleado");

        empleadoOpt.get().setActivo(true);

        empleadoRepository.save(empleadoOpt.get());
        return empleadoOpt.get().getIdEmpleado().toString();
    }

    public String desactivarEmpleado(Long id) throws NotFoundException{
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(id);
        if (empleadoOpt.isEmpty()) throw new NotFoundException("No se encontró el empleado");

        empleadoOpt.get().setActivo(false);
        empleadoOpt.get().setDeleteDate(LocalDate.now());

        empleadoRepository.save(empleadoOpt.get());
        return empleadoOpt.get().getIdEmpleado().toString();
    }
}
