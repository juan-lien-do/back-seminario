package com.example.demo.Envios.Envios.service;

import com.example.demo.Computadoras.domain.Computadora;
import com.example.demo.Computadoras.dto.ComputadoraDTO;
import com.example.demo.Computadoras.repository.ComputadoraRepository;
import com.example.demo.Computadoras.service.ComputadoraService;
import com.example.demo.Empleados.domain.Empleado;
import com.example.demo.Empleados.repository.EmpleadoRepository;
import com.example.demo.Empleados.service.EmpleadoService;
import com.example.demo.Envios.CambiosEstadoEnvio.domain.CambioEstadoEnvio;
import com.example.demo.Envios.CambiosEstadoEnvio.repository.CambiosEstadoEnvioRepository;
import com.example.demo.Envios.DetallesEnvioComputadora.domain.DetalleEnvioComputadora;
import com.example.demo.Envios.DetallesEnvioComputadora.dto.DetalleEnvioComputadoraPostDTO;
import com.example.demo.Envios.DetallesEnvioComputadora.dto.DetalleEnvioComputadoraResponseDTO;
import com.example.demo.Envios.DetallesEnvioComputadora.repository.DetallesEnvioComputadoraRepository;
import com.example.demo.Envios.DetallesEnvioRecurso.domain.DetalleEnvioRecurso;
import com.example.demo.Envios.DetallesEnvioRecurso.dto.DetalleEnvioRecursoResponseDTO;
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
import com.example.demo.Usuarios.service.UsuarioService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.notificaciones.services.EMailServiceImpl;
import com.example.demo.notificaciones.services.TwilioNotificationService;
import com.twilio.base.bearertoken.Resource;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    private final TwilioNotificationService twilioNotificationService;
    @Autowired
    private ComputadoraService computadoraService;
    @Autowired
    private UsuarioService usuarioService;

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
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        CambioEstadoEnvio cev = CambioEstadoEnvio.builder()
                .fechaInicio(fechaHoraActual)
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
                    //TODO MOCKEADO PARA QUE NO SE ROMPA NADA XD
                    .esDevuelto(false)
                    .build();

            existencias.add(ex.get());
            ders.add(der);
        }

        for (DetalleEnvioComputadoraPostDTO decp :
                body.getDetallesEnvioComputadora()) {
            Optional<Computadora> compu = computadoraRepository.findById(decp.getIdComputadora());
            if (compu.isEmpty()) throw new NotFoundException("No se encontró computadora.");
            //actualizar el "Estado" enUso del equipo
            computadoraService.actualizarEnUso(compu.get().getIdComputadora(), true);

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

    public void cambiarEstado(Long idEnvio, Long idEstado) throws Exception {
        Optional<Envio> envioOptional = envioRepository.findEnvioByIdEnvio(idEnvio);
        if (envioOptional.isEmpty()) throw new NotFoundException("No se encontró el envío.");

        // Buscar el último cambio de estado con fechaFin == null
        CambioEstadoEnvio cev = envioOptional.get().getListaCambiosEstado().stream()
                .filter(x -> x.getFechaFin() == null)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No hay cambio de estado sin fecha de finalización."));

        LocalDate actual = LocalDate.now();
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        // Actualizar fechaFin del último cambio de estado
        cev.setFechaFin(fechaHoraActual);

        // Crear un nuevo cambio de estado
        CambioEstadoEnvio cambioEstadoEnvio = CambioEstadoEnvio.builder()
                .envio(envioOptional.get())
                .fechaInicio(fechaHoraActual)
                .idEstadoEnvio(idEstado)
                .build();

        String[] estadosPosibles = {"Pendiente", "En preparación",
                "Enviado", "Entregado/Pendiente de devolucion",
                "Devolucion parcial", "Devolucion completa", "Cancelado", "Para retiro", "En reparación"};
        String destinatario = envioOptional.get().getEmpleado().getNombre();
        String nuevoEstado = estadosPosibles[(int)(idEstado - 1L)];

        // Guardar ambos cambios
        cambiosEstadoEnvioRepository.save(cev);
        cambiosEstadoEnvioRepository.save(cambioEstadoEnvio);

        // Enviar notificación
            twilioNotificationService.notificarUsuario("+5493525413678", nuevoEstado, destinatario);
            twilioNotificationService.notificarUsuario("+5493586022582", nuevoEstado, destinatario);

        EnvioResponseDTO envioDTO = envioOptional.get().toResponseDTO();
        String nombreEmpleado = envioDTO.getNombreEmpleado();
        String email = envioOptional.get().getEmpleado().getMail();
        String bodyTemplate = "";
        String body = "";
        //Traer detalles del envío para asociar al email
        StringBuilder detallerBuilder = new StringBuilder();
        for(DetalleEnvioComputadoraResponseDTO det : envioDTO.getDetallesEnvioComputadora()) {
            detallerBuilder.append("- Computadora: ")
                    .append(det.getComputadoraDTO().getDescripcion())
                    .append("\n");
        }
        for (DetalleEnvioRecursoResponseDTO det : envioDTO.getDetallesEnvioRecurso()) {
            detallerBuilder.append("- Cantidad: ").append(det.getCantidad())
                    .append(", Recurso: ").append(det.getExistenciaDTO().getNombreRecurso())
                    .append("\n");
        }
        //Manejo de envios de email según estado
        switch (idEstado.intValue()) {
            case 4:
                bodyTemplate = loadTemplate("Enviado-bodyemail.txt");
                body = String.format(bodyTemplate,
                        nombreEmpleado,
                        detallerBuilder);
                usuarioService.enviarMail(email, body, "El envío fue retirado por logística");
                break;
            case 2,3,8:
                bodyTemplate = loadTemplate("ParaRetiro-bodyemail.txt");
                body = String.format(bodyTemplate, nombreEmpleado,
                        idEnvio,
                        estadosPosibles[idEstado.intValue()-1],
                        detallerBuilder);
                //System.out.println(bodyTemplate);
                usuarioService.enviarMail(email, body, "Actualización estado envío " + idEnvio);
                break;
        }
    }

    public String loadTemplate(String fileName) throws Exception {
        ClassPathResource resource = new ClassPathResource("/emailsTemplates/" + fileName);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}