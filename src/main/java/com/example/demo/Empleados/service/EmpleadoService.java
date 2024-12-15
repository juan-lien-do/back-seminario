package com.example.demo.Empleados.service;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.repository.ComputadoraRepository;
import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Empleados.dto.EmpleadoPostRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoPutRequestDTO;
import com.example.demo.Empleados.dto.EmpleadoResponseDTO;
import com.example.demo.Empleados.mapper.EmpleadoMapper;
import com.example.demo.Empleados.repository.EmpleadoRepository;
import com.example.demo.Envios.DetallesEnvioComputadora.domain.DetalleEnvioComputadora;
import com.example.demo.Envios.DetallesEnvioComputadora.repository.DetallesEnvioComputadoraRepository;
import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.repository.EnvioRepository;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class EmpleadoService {
    @Autowired
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    private final EnvioRepository envioRepository;

    @Autowired
    private final ComputadoraRepository computadoraRepository;

    @Autowired
    private final DetallesEnvioComputadoraRepository detallesEnvioComputadoraRepository;


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

    public String desactivarEmpleado(Long id) throws NotFoundException, BadRequestException{
        Optional<Empleado> empleadoOpt = empleadoRepository.findById(id);
        if (empleadoOpt.isEmpty()) throw new NotFoundException("No se encontró el empleado");

        // validacion de que no se pueda borrar si tiene envíos pendientes de devolver.
        List<Envio> envios = envioRepository.findByEmpleado(empleadoOpt.get());

        long cantidadEnviosSinFinalizar = envios.stream().filter(cev -> !cev.sosFinalizado()).count();

        if (cantidadEnviosSinFinalizar > 0){
            throw new BadRequestException("El empleado "+empleadoOpt.get().getNombre() + " tiene "
                    + cantidadEnviosSinFinalizar + " envíos sin devolver o cancelar.");
        }

        empleadoOpt.get().setActivo(false);
        empleadoOpt.get().setDeleteDate(LocalDate.now());

        empleadoRepository.save(empleadoOpt.get());
        return empleadoOpt.get().getIdEmpleado().toString();
    }

    public List<String> quienTieneComputadora(Long id) throws NotFoundException{
        Optional<Computadora> compuOpt = computadoraRepository.findById(id);
        if (compuOpt.isEmpty()) throw new NotFoundException("No se encontró la computadora");

        List<DetalleEnvioComputadora> detalleEnvioComputadoras = detallesEnvioComputadoraRepository.findByComputadora(compuOpt.get());

        List<Envio> enviosConCompuNoFinalizados = detalleEnvioComputadoras.stream()
                .filter(det -> !det.getEsDevuelto())
                .map(DetalleEnvioComputadora::getEnvio)
                .toList();

        List<String> nombres = enviosConCompuNoFinalizados.stream()
                .map(env -> env.getEmpleado().getNombre()+"-"+env.getEmpleado().getCuil().toString())
                .toList();
        Set<String> nombreSinRepetir = new HashSet<>();
        nombreSinRepetir.addAll(nombres);

        return nombreSinRepetir.stream().toList();
    }
}
