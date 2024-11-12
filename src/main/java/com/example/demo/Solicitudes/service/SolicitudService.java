package com.example.demo.Solicitudes.service;

import com.example.demo.Solicitudes.domain.DetalleSolicitud;
import com.example.demo.Solicitudes.domain.Solicitud;
import com.example.demo.Solicitudes.dtos.SolicitudDTOGet;
import com.example.demo.Solicitudes.dtos.SolicitudDTOPost;
import com.example.demo.Solicitudes.mapper.SolicitudMapper;
import com.example.demo.Solicitudes.repository.DetalleSolicitudRepository;
import com.example.demo.Solicitudes.repository.SolicitudRepository;
import com.example.demo.Usuarios.domain.Usuario;
import com.example.demo.Usuarios.repository.UsuarioRepository;
import com.example.demo.Usuarios.service.UsuarioService;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SolicitudService {
    @Autowired
    private final SolicitudRepository solicitudRepository;
    @Autowired
    private final DetalleSolicitudRepository detalleSolicitudRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;

    public List<SolicitudDTOGet> getAll() {
        return solicitudRepository.findAll().stream().map(SolicitudMapper::toDTOGet).toList();
    }

    @Transactional
    public Long create(SolicitudDTOPost solicitudDTOPost) throws NotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByNombre(solicitudDTOPost.getNombreUsuario());
        if (usuario.isEmpty()) throw new NotFoundException("No se encontró usuario");

        Solicitud soli = Solicitud.builder()
                .fechaSolicitud(LocalDate.now())
                .usuario(usuario.get())
                .fechaIncorporacion(null)
                .build();

        Solicitud persistSolicitud = solicitudRepository.save(soli);
        System.out.println(persistSolicitud);

        List<DetalleSolicitud> detalleSolicituds = solicitudDTOPost.getDetallesSolicitud()
                .stream().map((det) -> SolicitudMapper.fromDTOPost(det, persistSolicitud)).toList();

        detalleSolicitudRepository.saveAll(detalleSolicituds);

        return persistSolicitud.getIdSolicitud();
    }

    public Long incorporar(Long id) throws NotFoundException, BadRequestException{
        Optional<Solicitud> soli = solicitudRepository.findById(id);
        if (soli.isEmpty()) throw new NotFoundException("No se encontró la solicitud");
        LocalDate fechaInc = LocalDate.now();

        if (soli.get().getFechaIncorporacion() != null) throw new BadRequestException("Ya esta incorporada.");


        soli.get().setFechaIncorporacion(fechaInc);

        solicitudRepository.save(soli.get());
        return soli.get().getIdSolicitud();
    }
}
