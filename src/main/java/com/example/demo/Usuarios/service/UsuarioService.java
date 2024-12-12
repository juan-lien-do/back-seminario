package com.example.demo.Usuarios.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.example.demo.Usuarios.dto.*;
import com.example.demo.Usuarios.mapper.UsuarioMapper;
import com.example.demo.Usuarios.repository.UsuarioRepository;
import com.example.demo.Usuarios.domain.Usuario;
import com.example.demo.exceptions.BadRequestException;
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
import org.springframework.web.bind.annotation.RequestBody;

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

    public String register(UsuarioRegisterDTO usuarioDTO) throws BadRequestException {

        if(usuarioRepository.existsByCuil(usuarioDTO.getCuil())){
            throw new BadRequestException("El cuil ya se encuentra registrado");
        }

        String password = generarPassword();
        Usuario user = new Usuario();
        LocalDateTime creacion = LocalDateTime.now();
        user.setPassword(encoder.encode(password));
        //A modo debug
        System.out.println(user.getPassword());
        user.setNombreUsuario(usuarioDTO.getNombre_usr());
        user.setApellidoUsuario(usuarioDTO.getApellido_usr());
        user.setObservaciones(usuarioDTO.getObservaciones());
        user.setMail(usuarioDTO.getMail());
        user.setTelefono(usuarioDTO.getTelefono());
        user.setCuil(usuarioDTO.getCuil());
        user.setPrimerLogin(true);
        user.setUltimaActualizacion(creacion);
        user.setEsActivo(true);
        user.setIsAdmin(false);
        user.setFechaCreacion(creacion);
        String username = (usuarioDTO.getApellido_usr() + usuarioDTO.getNombre_usr().charAt(0)).toUpperCase();
        //En mayúsculas
        user.setNombre(username);
        //A modo debug
        System.out.println(user);
        System.out.println("pass sin hashear:" + password);
        usuarioRepository.save(user);
        String body = "Usuario: " + username + " - " + "Contraseña temporal: " + password;
        String subject = "Usuario nuevo, bienvenido a Almacen IT";
        enviarMail(user.getMail(), body, subject);
        return ("El usuario " + user.getNombre() + " se creó con éxito");
    }

    public UsuarioDTOAfterLogin verify(UsuarioDTOBeforeLogin user) throws WrongCredentialsException {
        Optional<Usuario> usuario = usuarioRepository.findByNombre(user.getNombre());
        if (usuario.isEmpty()) throw new WrongCredentialsException("Wrong credentials");

        LocalDateTime ultimaAct = usuario.get().getUltimaActualizacion();
        //Verificación para el primer logueo
        if(usuario.get().getPrimerLogin() && LocalDateTime.now().isAfter(ultimaAct.plusMinutes(15))) {
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

    public UsuarioDTO update(Long id, UsuarioModificacionDTO usuarioDTO) throws NotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombreUsuario(usuarioDTO.getNombre_usr());
            usuario.setCuil(usuarioDTO.getCuil());
            usuario.setApellidoUsuario(usuarioDTO.getApellido_usr());
            usuario.setObservaciones(usuarioDTO.getObservaciones());
            usuario.setMail(usuarioDTO.getMail());
            usuario.setTelefono(usuarioDTO.getTelefono());
            usuario.setIsAdmin(usuarioDTO.getIsAdmin());
            usuario.setIsDriver(usuarioDTO.getIsDriver());
            usuario.setUltimaActualizacion(LocalDateTime.now());
            usuarioRepository.save(usuario);
            return UsuarioMapper.toDto(usuario);
        } else {
            throw new NotFoundException("No existe el usuario");
        }
    }

    public String changePassword(@RequestBody UsuarioPassDTO usuarioPassDTO) throws NotFoundException {

        if(!validarPassword(usuarioPassDTO.getNuevaPassword())){
            return "Contraseña inválida, verifique que tenga al menos 6 caracteres, una mayúscula" +
                    ", un carácter especial y un dígito";
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findByNombre(usuarioPassDTO.getNombre());
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            //Seteo de nueva clave hasheada
            usuario.setPassword(encoder.encode(usuarioPassDTO.getNuevaPassword()));
            //Ultima modificacion del user
            usuario.setUltimaActualizacion(LocalDateTime.now());
            //al loguerse y garantizar el cambio de la clave, deja de ser primer logueo
            usuario.setPrimerLogin(false);
            //Modo debug
            System.out.println("Nueva pass: " + usuarioPassDTO.getNuevaPassword());
            //Guardar
            usuarioRepository.save(usuario);
            //Enviar email de cambio exitoso
            enviarMail(usuario.getMail(), "Su clave fue modificada con éxito el " + LocalDateTime.now(), "Modificacion de Clave");
            //Retorno de mensaje para modificacion exitosa
            return "Clave modificada con éxito";
        } else {
            throw new NotFoundException("No existe el usuario");
        }
    }

    @Transactional
    public String resetPassword(Long id) throws NotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            String password = generarPassword();
            usuario.setPassword(encoder.encode(password));
            usuario.setUltimaActualizacion(LocalDateTime.now());
            usuario.setPrimerLogin(true);
            usuarioRepository.save(usuario);
            String body = "Usuario: " + usuario.getNombre() + " - " + "Contraseña temporal: " + password + "\n" +
                    "Dispone de 15 minutos para ingresar y cambiar su contraseña, en caso contrario comunicarse con " +
                    "Administración nuevamente";
            String subject = "Reseteo de contraseña exitoso";
            enviarMail(usuario.getMail(), body, subject);
            return ("El usuario " + usuario.getNombre() + " se creó con éxito");
        } else {
            throw new NotFoundException("No existe el usuario");
        }
    }

    //Funciones extra

    public String enviarMail(String email, String body, String subject){
        // TODO Modifiquen esto de la forma en la que vean que es necesario
        // esto es solo un ejemplo de como se utiliza
        EMailDetails eMailDetails = new EMailDetails();
        eMailDetails.setRecipient(email);
        eMailDetails.setMsgBody(body);
        eMailDetails.setSubject(subject);
        return eMailService.enviarMailConAdjunto(eMailDetails);
    }


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

    public Boolean validarPassword(String password) {
        if(password.length() < 6) {
            return false;
        }
        Boolean poseeMayus = password.chars().anyMatch(Character::isUpperCase);
        Boolean poseeNumeros = password.chars().anyMatch(Character::isDigit);
        Boolean poseeSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
        return poseeMayus && poseeNumeros && poseeSpecial;
    }
}
