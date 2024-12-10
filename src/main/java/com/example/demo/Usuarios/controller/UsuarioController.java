package com.example.demo.Usuarios.controller;

import com.example.demo.Usuarios.dto.UsuarioDTO;
import com.example.demo.Usuarios.dto.UsuarioDTOAfterLogin;
import com.example.demo.Usuarios.domain.Usuario;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.WrongCredentialsException;
import com.example.demo.Usuarios.mapper.UsuarioMapper;
import com.example.demo.Usuarios.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*") // esta solución la hice probando por probar y funcionó. replicar en todos los controllers
//@RequestMapping("/usuarios/")
public class UsuarioController {
    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> getAll(HttpServletRequest request) {
        System.out.println(request.getHeader("Authorization"));
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody UsuarioDTO user) {
        System.out.println(user);
        try {
            usuarioService.register(UsuarioMapper.toEntity(user));
            return ResponseEntity.status(201).build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/usuarios/notify/{id}") // el id del usuario
    public ResponseEntity<String> notificar(@PathVariable Long id){
        try{
            String telefono = usuarioService.notificar(id);
            return ResponseEntity.status(201).body(telefono);
        } catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            return ResponseEntity.notFound().header("ERROR", "No hay telefono").build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTOAfterLogin> login(@RequestBody UsuarioDTO user) {

        try {
            return ResponseEntity.status(201).body(usuarioService.verify(user));
        } catch (WrongCredentialsException e){
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/usuarios/enviar-mail/")
    public ResponseEntity<String> enviarMail(){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.enviarMail());
    }

}
