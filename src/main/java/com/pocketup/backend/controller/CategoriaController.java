package com.pocketup.backend.controller;

import com.pocketup.backend.model.Categoria;
import com.pocketup.backend.service.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/categorias")
@RestController
public class CategoriaController {
    @Autowired
    private ICategoriaService categoria_service;

    // 1. Obtener todas las categorías (Base + las del Usuario)
    @GetMapping("/get/{usuarioId}")
    public ResponseEntity<List<Categoria>> listarCategorias(@PathVariable Long usuarioId) {
        List<Categoria> categorias = categoria_service.listarPorUsuario(usuarioId);
        // Si no hay categorías, devolverá una lista vacía con 200 OK
        return ResponseEntity.ok(categoria_service.listarPorUsuario(usuarioId));
    }

    // 2. Guardar una nueva categoría personalizada
    @PostMapping("/new")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        // Al no haber try-catch, cualquier error de validación o DB sube al GlobalHandler
        Categoria nueva_categoria = categoria_service.save(categoria);
        return new ResponseEntity<>(nueva_categoria, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoria_service.update(id, categoria));
    }

    // 3. Eliminar una categoría
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        // Si el Service lanza RuntimeException por intentar borrar "General",
        // el GlobalExceptionHandler lo captura y devuelve el 400 Bad Request.
        categoria_service.delete(id);
        return ResponseEntity.noContent().build(); // Devuelve un 204 No Content si todo fue bien
    }
}
