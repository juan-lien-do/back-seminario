package com.example.demo.Usuarios.controller;

import com.example.demo.Recursos.dto.RecursoDTO;
import com.example.demo.Usuarios.dto.*;
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

    @PostMapping("/usuarios/register")
    public ResponseEntity<String> register(@RequestBody UsuarioRegisterDTO user) {
        System.out.println(user.toString());
        try {
            String msg = usuarioService.register(user);
            return ResponseEntity.ok(msg);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/usuarios/desactivar/{id}")
    public ResponseEntity<RecursoDTO> deactivateById(@PathVariable Long id){
        try {
            if (usuarioService.logicDelete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
        }
    }

    @PatchMapping("/usuarios/activar/{id}")
    public ResponseEntity<RecursoDTO> activateById(@PathVariable Long id){
        try {
            if (usuarioService.logicUndelete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header("ERROR_MSG", e.getMessage()).build();
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
    public ResponseEntity<UsuarioDTOAfterLogin> login(@RequestBody UsuarioDTOBeforeLogin user) {

        try {
            return ResponseEntity.status(201).body(usuarioService.verify(user));
        } catch (WrongCredentialsException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/usuarios/enviar-mail/")
    public ResponseEntity<String> enviarMail(){
        String email = "3mmanuelch@gmail.com", body = "Prueba";
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.enviarMail(email, body, "prueba email"));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody UsuarioModificacionDTO user) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.update(id, user);
            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/usuarios/actualizar_password")
    public ResponseEntity<String> changePassword(@RequestBody UsuarioPassDTO user) {
        try {
            String msg = usuarioService.changePassword(user);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/usuarios/blanquear_password/{id}")
    public ResponseEntity<String> resetarPassword(@PathVariable Long id) {
        try {
            String msg = usuarioService.resetPassword(id);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
