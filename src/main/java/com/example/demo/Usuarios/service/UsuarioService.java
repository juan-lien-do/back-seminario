package com.example.demo.Usuarios.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.Usuarios.dto.UsuarioDTO;
import com.example.demo.Usuarios.dto.UsuarioDTOAfterLogin;
import com.example.demo.Usuarios.dto.UsuarioDTOBeforeLogin;
import com.example.demo.Usuarios.mapper.UsuarioMapper;
import com.example.demo.Usuarios.repository.UsuarioRepository;
import com.example.demo.Usuarios.domain.Usuario;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.WrongCredentialsException;
import com.example.demo.notificaciones.services.EMailDetails;
import com.example.demo.notificaciones.services.EMailServiceImpl;
import com.example.demo.notificaciones.services.TwilioNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private final AuthenticationManager authManager;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final TwilioNotificationService twilioNotificationService;
    @Autowired
    private final JWTService jwtService;
    @Autowired
    private final EMailServiceImpl eMailService;

    public UsuarioService(AuthenticationManager authManager, UsuarioRepository usuarioRepository, JWTService jwtService, TwilioNotificationService twilioNotificationService, EMailServiceImpl eMailService) {
        this.authManager = authManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.twilioNotificationService = twilioNotificationService;
        this.eMailService = eMailService;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll().stream().map(UsuarioMapper::toDto).toList();
    }

    public Usuario register(Usuario user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user);
        usuarioRepository.save(user);
        return user;
    }

    public UsuarioDTOAfterLogin verify(UsuarioDTOBeforeLogin user) throws WrongCredentialsException {
        Optional<Usuario> usuario = usuarioRepository.findByNombre(user.getNombre());
        if (usuario.isEmpty()) throw new WrongCredentialsException("Wrong credentials");


        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getNombre(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            UsuarioDTOAfterLogin ans = UsuarioDTOAfterLogin.builder()
                    .token(jwtService.generateToken(usuario.get().getNombre()))
                    .nombre(usuario.get().getNombre())
                    .isAdmin(usuario.get().getIsAdmin()) // esto es un hardcodeo terriiiiible
                    .build();
            System.out.println(ans.toString());
            return ans;
        } else {
            throw new WrongCredentialsException("Wrong credentials");
        }
    }

    public String notificar(Long id) throws NotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) throw new NotFoundException("No se encontró el usuario");
        else {
            String numero = usuario.get().getTelefono();

            twilioNotificationService.notificarUsuario(numero, "", "");

            return numero;
        }
    }

    public String enviarMail(){
        // TODO Modifiquen esto de la forma en la que vean que es necesario
        // esto es solo un ejemplo de como se utiliza
        EMailDetails eMailDetails = new EMailDetails();
        eMailDetails.setRecipient("emmanuelricardo.chaile@cba.gov.ar");
        eMailDetails.setMsgBody("prueba");
        eMailDetails.setSubject("prueba de mail");
        return eMailService.enviarMailConAdjunto(eMailDetails);
    }
}
