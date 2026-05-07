package com.pocketup.backend.controller;
import com.pocketup.backend.model.Categoria;
import com.pocketup.backend.model.ConfiguracionApp;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.repository.ICategoriaRepository;
import com.pocketup.backend.repository.IConfiguracionAppRepository;
import com.pocketup.backend.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IConfiguracionAppRepository configRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    // Listar solo las categorías base del sistema
    @GetMapping("/categorias-base")
    public ResponseEntity<List<Map<String, Object>>> obtenerCategoriasBase() {
        List<Categoria> categorias = categoriaRepository.findByUsuarioIsNull();
        List<Map<String, Object>> listaLimpia = new ArrayList<>();
        for (Categoria c : categorias) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("nombre", c.getNombre());
            map.put("icono", c.getIcono());
            map.put("color", c.getColor());
            listaLimpia.add(map);
        }
        return ResponseEntity.ok(listaLimpia);
    }

    // Crear una nueva categoría base
    @PostMapping("/categorias-base")
    public ResponseEntity<Map<String, String>> crearCategoriaBase(@RequestBody Map<String, String> payload) {
        Categoria nuevaCat = new Categoria();
        nuevaCat.setNombre(payload.get("nombre"));
        nuevaCat.setIcono(payload.get("icono"));
        nuevaCat.setColor(payload.get("color"));
        nuevaCat.setUsuario(null); // ¡MAGIA! Esto la convierte en categoría del sistema

        categoriaRepository.save(nuevaCat);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Categoría del sistema creada con éxito");
        return ResponseEntity.ok(respuesta);
    }

    // Obtener configuración completa (para el panel de escritorio)
    @GetMapping("/configuracion")
    public ResponseEntity<ConfiguracionApp> obtenerConfiguracion() {
        ConfiguracionApp config = configRepository.findById(1L).orElseGet(() -> {
            return configRepository.save(new ConfiguracionApp());
        });
        return ResponseEntity.ok(config);
    }

    // Guardar/Actualizar configuración (incluyendo el interruptor de mantenimiento)
    @PutMapping("/configuracion")
    public ResponseEntity<ConfiguracionApp> guardarConfiguracion(@RequestBody ConfiguracionApp nuevaConfig) {
        ConfiguracionApp config = configRepository.findById(1L).orElse(new ConfiguracionApp());
        // Actualizamos todos los campos
        config.setXpPorNivel(nuevaConfig.getXpPorNivel());
        config.setMantenimientoActivo(nuevaConfig.isMantenimientoActivo());
        config.setTituloMantenimiento(nuevaConfig.getTituloMantenimiento());
        config.setMensajeMantenimiento(nuevaConfig.getMensajeMantenimiento());

        configRepository.save(config);
        return ResponseEntity.ok(config);
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        // MODO HACKATHON: Datos fijos para probar la conexión con la app de escritorio.
        // Más adelante, aquí llamarás a tu usuarioRepository.count(), etc.
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsuarios", 1245);
        stats.put("movimientosHoy", 342);
        stats.put("nivelPromedio", "Nvl. 15");
        stats.put("estado", "ONLINE");

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Map<String, Object>>> obtenerListaUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Map<String, Object>> usuariosLimpios = new ArrayList<>();
        for (Usuario u : usuarios) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("nombre", u.getNombre());
            map.put("email", u.getEmail());
            // Leemos el estado real. Si es null (usuarios viejos), lo consideramos ACTIVO
            map.put("estado", u.getEstado() != null ? u.getEstado() : "ACTIVO");
            if (u.getFechaRegistro() != null) {
                map.put("fechaRegistro", u.getFechaRegistro().toLocalDate().toString());
            } else {
                map.put("fechaRegistro", "Desconocida");
            }
            usuariosLimpios.add(map);
        }
        return ResponseEntity.ok(usuariosLimpios);
    }

    @PutMapping("/usuarios/{id}/toggle-estado")
    public ResponseEntity<Map<String, String>> alternarEstadoUsuario(@PathVariable Long id) {
        // 1. Buscamos al usuario en la base de datos
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return ResponseEntity.notFound().build(); // Devuelve error 404 si no existe
        }

        // 2. Comprobamos su estado actual y lo cambiamos (Interruptor)
        if ("ACTIVO".equals(usuario.getEstado()) || usuario.getEstado() == null) {
            usuario.setEstado("SUSPENDIDO");
        } else {
            usuario.setEstado("ACTIVO");
        }

        // 3. Guardamos los cambios
        usuarioRepository.save(usuario);

        // 4. Respondemos con el nuevo estado para que la app de escritorio lo sepa
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Operación exitosa");
        respuesta.put("nuevoEstado", usuario.getEstado());
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@PathVariable Long id) {
        // MODO HACKATHON: Lo borramos directamente.
        // Si tuvieras que borrar sus movimientos antes, lo harías aquí.
        usuarioRepository.deleteById(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuario eliminado con éxito");
        return ResponseEntity.ok(respuesta);
    }

}