package com.pocketup.backend.controller;
import com.pocketup.backend.dto.UsuarioRegistroRequest;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

    @Autowired
    private IUsuarioService usuario_service;

    @PostMapping("/user/register")
    public ResponseEntity<?> saveUser(@RequestBody UsuarioRegistroRequest userReq){
        Usuario nuevoUsuario = usuario_service.saveUser(userReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

}
