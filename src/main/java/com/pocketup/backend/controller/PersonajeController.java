package com.pocketup.backend.controller;

import com.pocketup.backend.model.Personaje;
import com.pocketup.backend.repository.IPersonajeRepository;
import com.pocketup.backend.service.IPersonajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personaje")
public class PersonajeController {
    @Autowired
    private IPersonajeRepository personaje_repository;
    @Autowired
    private IPersonajeService personaje_Service;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Personaje> obtenerPersonaje(@PathVariable Long usuarioId) {
        return personaje_repository.findByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{usuarioId}/skin")
    public ResponseEntity<Personaje> cambiarSkin(@PathVariable Long usuarioId, @RequestParam Integer nuevaSkin) {
        // 1. Ejecutamos la lógica de negocio (validar nivel y guardar)
        personaje_Service.cambiarSkin(usuarioId, nuevaSkin);

        // 2. Devolvemos el personaje actualizado directamente con ResponseEntity::ok
        return personaje_repository.findByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
