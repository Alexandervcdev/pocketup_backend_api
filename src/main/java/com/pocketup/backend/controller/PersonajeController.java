package com.pocketup.backend.controller;

import com.pocketup.backend.model.Personaje;
import com.pocketup.backend.repository.IPersonajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personaje")
public class PersonajeController {
    @Autowired
    private IPersonajeRepository personaje_repository;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Personaje> obtenerPersonaje(@PathVariable Long usuarioId) {
        return personaje_repository.findByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
