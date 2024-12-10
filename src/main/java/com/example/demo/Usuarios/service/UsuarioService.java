package com.example.demo.Usuarios.service;

import java.time.LocalDateTime;
import java.util.*;

import com.example.demo.Recursos.domain.Recurso;
import com.example.demo.Recursos.dto.RecursoDTO;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    public UsuarioDTO register(UsuarioDTO usuarioDTO) {
        String password = generarPassword();
        LocalDateTime creacion = LocalDateTime.now();
        usuarioDTO.setPassword(encoder.encode(password));
        //A modo debug
        System.out.println(usuarioDTO.getPassword());
        usuarioDTO.setPrimerLogin(true);
        usuarioDTO.setUltimaActualizacion(creacion);
        usuarioDTO.setEsActivo(true);
        usuarioDTO.setFechaCreacion(creacion);
        String username = usuarioDTO.getApellido_usr() + usuarioDTO.getNombre_usr().charAt(0);
        //En mayúsculas
        usuarioDTO.setNombre(username.toUpperCase());
        //A modo debug
        Usuario user = UsuarioMapper.toEntity(usuarioDTO);
        System.out.println(user);
        System.out.println("pass sin hashear:" + password);
        Usuario usuarioGuardardo = usuarioRepository.save(user);
        return UsuarioMapper.toDto(usuarioGuardardo);
    }

    public UsuarioDTOAfterLogin verify(UsuarioDTOBeforeLogin user) throws WrongCredentialsException {
        Optional<Usuario> usuario = usuarioRepository.findByNombre(user.getNombre());
        if (usuario.isEmpty()) throw new WrongCredentialsException("Wrong credentials");

        LocalDateTime ultimaAct = usuario.get().getUltimaActualizacion();
        if(usuario.get().getPrimerLogin() && ultimaAct.isAfter(ultimaAct.plusMinutes(30))) {
            throw new WrongCredentialsException("Contraseña expirada");
        }

        if(!usuario.get().getEsActivo()) {
            throw new WrongCredentialsException("El usuario no esta activo");
        }

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getNombre(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            UsuarioDTOAfterLogin ans = UsuarioDTOAfterLogin.builder()
                    .token(jwtService.generateToken(usuario.get().getNombre()))
                    .nombre(usuario.get().getNombre())
                    .primerLogin(usuario.get().getPrimerLogin())
                    .isAdmin(usuario.get().getIsAdmin()) // esto es un hardcodeo terriiiiible
                    .build();
            //Setear primerLogin del usuario como false -- NO, ESTA LÓGICA VA DESPUÉS QUE SE CAMBIE LA CONTRASEÑA
            //usuario.get().setPrimerLogin(false);
            //usuarioRepository.save(usuario.get());
            //String para debug ?)
            System.out.println(ans.toString());
            return ans;
        } else {
            throw new WrongCredentialsException("Wrong credentials");
        }
    }

    @Transactional
    public Boolean logicDelete(Long id) throws NotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if(usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setEsActivo(false);
            usuarioRepository.save(usuario);
            return true;
        } else {
            throw new NotFoundException("No se encontró el recurso");
        }
    }

    @Transactional
    public Boolean logicUndelete(Long id) throws NotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if(usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setEsActivo(true);
            usuarioRepository.save(usuario);
            return true;
        } else {
            throw new NotFoundException("No se encontró el recurso");
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

    //Funciones extra
    public String generarPassword() {
        Random random = new Random();

        // Generar 6 letras (mayúsculas y minúsculas)
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            password.append(letters.charAt(random.nextInt(letters.length())));
        }

        // Generar 3 números
        String numbers = "0123456789";
        for (int i = 0; i < 3; i++) {
            password.append(numbers.charAt(random.nextInt(numbers.length())));
        }

        // Generar 1 carácter especial
        String specialCharacters = "!@#$%^&*()-_+=<>?";
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        // Mezclar los caracteres para que el orden no sea predecible
        List<Character> passwordChars = new ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            passwordChars.add(c);
        }
        Collections.shuffle(passwordChars);

        // Convertir de nuevo a cadena
        StringBuilder finalPassword = new StringBuilder();
        for (char c : passwordChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }
}
