package com.example.demo.controller;

import com.example.demo.domain.Usuario;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.dto.UsuarioDTOAfterLogin;
import com.example.demo.exceptions.WrongCredentialsException;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.service.JWTService;
import com.example.demo.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTOAfterLogin> login(@RequestBody UsuarioDTO user) {

        try {
            return ResponseEntity.status(201).body(usuarioService.verify(user));
        } catch (WrongCredentialsException e){
            return ResponseEntity.notFound().build();
        }

    }



}
