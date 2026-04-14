package com.pocketup.backend.controller;
import com.pocketup.backend.dto.UsuarioLoginRequest;
import com.pocketup.backend.dto.UsuarioRegistroRequest;
import com.pocketup.backend.dto.UsuarioRequest;
import com.pocketup.backend.dto.UsuarioUpdateRequest;
import com.pocketup.backend.model.ApiError;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
        Usuario usuario = usuario_service.registerOrLoginSocial(userReq);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/user/get")
    public ResponseEntity<?> getUser(@RequestBody UsuarioRequest userReq){
        Optional<Usuario> user_optional = usuario_service.findUser(userReq);
        if (user_optional.isPresent()) {
            return ResponseEntity.ok(user_optional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con los criterios proporcionados");
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody UsuarioLoginRequest login_req) {
        Usuario usuario = usuario_service.login(login_req);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long id,
                                              @RequestBody UsuarioUpdateRequest user_update_request) {
        Usuario update_user = usuario_service.updateUser(id,user_update_request);
        Map<String, Object> response = new HashMap<>();
        response.put("id", update_user.getId());
        response.put("mensaje", "Perfil actualizado correctamente");
        return ResponseEntity.ok(response);
    }
}
