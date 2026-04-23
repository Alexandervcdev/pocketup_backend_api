package com.pocketup.backend.controller;

import com.pocketup.backend.model.Meta;
import com.pocketup.backend.service.IMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metas")
public class MetaController {

    @Autowired
    private IMetaService meta_service;

    @GetMapping("/get/{usuarioId}")
    public ResponseEntity<List<Meta>> listarMetas(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(meta_service.listarPorUsuario(usuarioId));
    }

    @PostMapping("/new")
    public ResponseEntity<Meta> crearMeta(@RequestBody Meta meta) {
        return new ResponseEntity<>(meta_service.save(meta), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Meta> actualizarMeta(@PathVariable Long id, @RequestBody Meta meta) {
        return ResponseEntity.ok(meta_service.update(id, meta));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarMeta(@PathVariable Long id) {
        meta_service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ENDPOINT ESPECIAL: Solo recibe un JSON pequeño con {"cantidad": 50.00}
    @PutMapping("/{id}/add-fondos")
    public ResponseEntity<Meta> agregarFondosMeta(@PathVariable Long id, @RequestBody Map<String, BigDecimal> payload) {
        BigDecimal cantidad = payload.get("cantidad");
        if (cantidad == null) {
            throw new IllegalArgumentException("Debe proporcionar una cantidad");
        }
        return ResponseEntity.ok(meta_service.agregarFondos(id, cantidad));
    }
}