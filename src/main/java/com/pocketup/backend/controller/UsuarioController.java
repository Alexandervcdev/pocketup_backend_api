package com.pocketup.backend.controller;
import com.pocketup.backend.dto.UsuarioRegistroRequest;
import com.pocketup.backend.dto.UsuarioRequest;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UsuarioController {

    @Autowired
    private IUsuarioService usuario_service;

    @PostMapping("/user/register")
    public ResponseEntity<?> saveUser(@RequestBody UsuarioRegistroRequest userReq){
        Usuario nuevoUsuario = usuario_service.saveUser(userReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
    @PostMapping("/user/google-auth")
    public ResponseEntity<?> googleAuth(@RequestBody UsuarioRegistroRequest userReq){
        // Este método no lanza excepción si el email existe
        Usuario usuario = usuario_service.registerOrLoginSocial(userReq);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/user/get")
    public ResponseEntity<?> getUser(@RequestBody UsuarioRequest userReq){
        // Llamamos al servicio
        Optional<Usuario> user_optional = usuario_service.findUser(userReq);
        // Si el usuario está dentro Optional
        if (user_optional.isPresent()) {
            return ResponseEntity.ok(user_optional.get());
        }
        // Si la optional esta vacio.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con los criterios proporcionados");
    }

}
