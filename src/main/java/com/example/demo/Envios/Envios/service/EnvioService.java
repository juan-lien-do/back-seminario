package com.example.demo.Envios.Envios.service;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.repository.ComputadoraRepository;
import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Empleados.repository.EmpleadoRepository;
import com.example.demo.Empleados.service.EmpleadoService;
import com.example.demo.Envios.CambiosEstadoEnvio.domain.CambioEstadoEnvio;
import com.example.demo.Envios.CambiosEstadoEnvio.repository.CambiosEstadoEnvioRepository;
import com.example.demo.Envios.DetallesEnvioComputadora.domain.DetalleEnvioComputadora;
import com.example.demo.Envios.DetallesEnvioComputadora.dto.DetalleEnvioComputadoraPostDTO;
import com.example.demo.Envios.DetallesEnvioComputadora.repository.DetallesEnvioComputadoraRepository;
import com.example.demo.Envios.DetallesEnvioRecurso.domain.DetalleEnvioRecurso;
import com.example.demo.Envios.DetallesEnvioRecurso.dto.DetalleRecursoPostDTO;
import com.example.demo.Envios.DetallesEnvioRecurso.repository.DetalleEnvioRecursoRepository;
import com.example.demo.Envios.Envios.domain.Envio;
import com.example.demo.Envios.Envios.dto.EnvioDTO;
import com.example.demo.Envios.Envios.dto.EnvioPostDTO;
import com.example.demo.Envios.Envios.dto.EnvioResponseDTO;
import com.example.demo.Envios.Envios.mapper.EnvioMapper;
import com.example.demo.Envios.Envios.repository.EnvioRepository;
import com.example.demo.Existencias.domain.Existencia;
import com.example.demo.Existencias.dto.ExistenciaRequestDTO;
import com.example.demo.Existencias.repository.ExistenciasRepository;
import com.example.demo.Existencias.service.ExistenciasService;
import com.example.demo.Usuarios.domain.Usuario;
import com.example.demo.Usuarios.repository.UsuarioRepository;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
//@NoArgsConstructor
@Service

public class EnvioService {

    @Autowired
    private final EnvioRepository envioRepository;
    @Autowired
    private final EmpleadoRepository empleadoRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final ExistenciasRepository existenciasRepository;
    @Autowired
    private final DetallesEnvioComputadoraRepository detallesEnvioComputadoraRepository;
    @Autowired
    private final DetalleEnvioRecursoRepository detalleEnvioRecursoRepository;
    @Autowired
    private final CambiosEstadoEnvioRepository cambiosEstadoEnvioRepository;
    @Autowired
    private final ExistenciasService existenciasService;
    @Autowired
    private final ComputadoraRepository computadoraRepository;

    // ------------------------------------ MÉTODOS GET ------------------------------------------------------//

    //Obtener todos los Envios de la BBDD
    public List<EnvioResponseDTO> getAllEnvios() {
        List<Envio> listEnvios = envioRepository.findAll();
        return listEnvios.stream()
                .map(Envio::toResponseDTO)
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

    // TODO: VALIDAR QUE SOLO EXISTA UN ENVIO CON ESTADO CREADO A LA VEZ
    @Transactional
    public EnvioResponseDTO create(EnvioPostDTO body) throws BadRequestException, NotFoundException {
        Optional<Empleado> empleado = empleadoRepository.findById(body.getIdEmpleado());
        if (empleado.isEmpty()) throw new NotFoundException("No se encontró empleado");
        Optional<Usuario> usuario = usuarioRepository.findByNombre(body.getNombreUsuario());
        if (usuario.isEmpty()) throw new NotFoundException("No se encontró usuario");

        LocalDate fechaActual = LocalDate.now();
        CambioEstadoEnvio cev = CambioEstadoEnvio.builder()
                .fechaInicio(fechaActual)
                .idEstadoEnvio(1l)
                .build();

        List<DetalleEnvioComputadora> decs = new ArrayList<>();

        List<DetalleEnvioRecurso> ders = new ArrayList<>();

        List<Existencia> existencias = new ArrayList<>();

        for (DetalleRecursoPostDTO derp : body.getDetallesEnvioRecurso()) {
            Optional<Existencia> ex = existenciasRepository.findById(derp.getIdExistencia());
            if (ex.isEmpty()) throw new NotFoundException("No se encontró existencia.");

            DetalleEnvioRecurso der = DetalleEnvioRecurso.builder()
                    .cantidad(derp.getCantidad())
                    .existencia(ex.get())
                    .recurso(ex.get().getRecurso())
                    .build();

            existencias.add(ex.get());
            ders.add(der);
        }

        for (DetalleEnvioComputadoraPostDTO decp :
                body.getDetallesEnvioComputadora()) {
            Optional<Computadora> compu = computadoraRepository.findById(decp.getIdComputadora());
            if (compu.isEmpty()) throw new NotFoundException("No se encontró computadora.");

            DetalleEnvioComputadora dec = new DetalleEnvioComputadora();
            dec.setComputadora(compu.get());

            decs.add(dec);
        }

        Envio env = Envio.builder()
                .fechaPreparacion(fechaActual)
                .empleado(empleado.get())
                .usuario(usuario.get())
                .detallesEnvioRecurso(ders)
                .detallesEnvioComputadora(decs)
                .listaCambiosEstado(List.of(cev))
                .build();


        Envio envPersist = envioRepository.save(env);

        ders.forEach(d -> {
            d.setEnvio(envPersist);
        });
        decs.forEach(d -> {
            d.setEnvio(envPersist);
        });
        cev.setEnvio(envPersist);

        detalleEnvioRecursoRepository.saveAll(ders);
        cambiosEstadoEnvioRepository.save(cev);
        detallesEnvioComputadoraRepository.saveAll(decs);
        // TODO
        //detallesEnvioComputadoraRepository.save(xxxx);

        // disminuir existencias

        List<ExistenciaRequestDTO> exReqDto = new ArrayList<>();

        ders.forEach(x -> {
            ExistenciaRequestDTO er = new ExistenciaRequestDTO(
                    x.getCantidad(), x.getRecurso().getId(), x.getExistencia().getDeposito().getIdDeposito()
            );
            exReqDto.add(er);
        });

        for (ExistenciaRequestDTO er : exReqDto) {
            existenciasService.disminuirExistencias(er);
        }

        return envPersist.toResponseDTO();
    }

}
