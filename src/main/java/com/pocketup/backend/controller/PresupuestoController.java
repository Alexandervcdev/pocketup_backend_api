package com.pocketup.backend.controller;

import com.pocketup.backend.model.Presupuesto;
import com.pocketup.backend.service.IPresupuestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presupuestos")
public class PresupuestoController {

    @Autowired
    private IPresupuestoService presupuesto_service;

    @GetMapping("/get/{usuarioId}")
    public ResponseEntity<List<Presupuesto>> listarPresupuestos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(presupuesto_service.listarPorUsuario(usuarioId));
    }

    @PostMapping("/new")
    public ResponseEntity<Presupuesto> crearPresupuesto(@RequestBody Presupuesto presupuesto) {
        return new ResponseEntity<>(presupuesto_service.save(presupuesto), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Presupuesto> actualizarPresupuesto(@PathVariable Long id, @RequestBody Presupuesto presupuesto) {
        return ResponseEntity.ok(presupuesto_service.update(id, presupuesto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarPresupuesto(@PathVariable Long id) {
        presupuesto_service.delete(id);
        return ResponseEntity.noContent().build();
    }
}