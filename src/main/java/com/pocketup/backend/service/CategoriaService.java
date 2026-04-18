package com.pocketup.backend.service;

import com.pocketup.backend.model.Categoria;
import com.pocketup.backend.model.Movimiento;
import com.pocketup.backend.repository.ICategoriaRepository;
import com.pocketup.backend.repository.IMovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService implements ICategoriaService {
    //Dependencias
    @Autowired
    private ICategoriaRepository categoria_repository;
    @Autowired
    private IMovimientoRepository movimiento_repository;

    @Override
    public List<Categoria> listarPorUsuario(Long usuarioId) {
        return categoria_repository.findByUsuarioIdOrGlobal(usuarioId);
    }

    @Override
    public Categoria save(Categoria categoria) {
        return categoria_repository.save(categoria);
    }

    @Override
    public Categoria update(Long id, Categoria nueva) {
        return categoria_repository.findById(id).map(existente -> {
            existente.setNombre(nueva.getNombre());
            existente.setIcono(nueva.getIcono());
            existente.setColor(nueva.getColor());
            return categoria_repository.save(existente);
        }).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Transactional
    @Override
    public void delete(Long idABorrar) {
        // 1. Buscamos la categoría "General" del sistema para reasignar
        // Asumimos que existe una global llamada "General"
        Categoria general = categoria_repository.findByNombreAndUsuarioIsNull("General")
                .orElseThrow(() -> new RuntimeException("Error: Categoría 'General' no existe en el sistema"));
        if (general.getId().equals(idABorrar)) {
            throw new RuntimeException("No se puede eliminar la categoría base del sistema");
        }

        // 2. Buscamos movimientos que usen la categoría que queremos borrar
        List<Movimiento> movimientos = movimiento_repository.findByCategoriaId(idABorrar);

        // 3. Reasignamos a "General"
        for (Movimiento m : movimientos) {
            m.setCategoria(general);
        }
        movimiento_repository.saveAll(movimientos);
        categoria_repository.deleteById(idABorrar);
    }
}
