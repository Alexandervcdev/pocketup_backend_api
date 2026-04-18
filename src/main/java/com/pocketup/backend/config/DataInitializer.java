package com.pocketup.backend.config;

import com.pocketup.backend.model.Categoria;
import com.pocketup.backend.repository.ICategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ICategoriaRepository categoria_repository;

    public DataInitializer(ICategoriaRepository categoriaRepository) {
        this.categoria_repository = categoriaRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (categoria_repository.findByNombreAndUsuarioIsNull("General").isEmpty()) {
            //System.out.println("Creando categorías base del sistema...");
            List<Categoria> categoriasBase = Arrays.asList(
                    crearBase("General", "ic_all_inclusive", "#9E9E9E"), // Gris
                    crearBase("Comida", "ic_restaurant", "#FF7043"),    // Naranja
                    crearBase("Transporte", "ic_directions_car", "#42A5F5"), // Azul
                    crearBase("Salud", "ic_medical_services", "#66BB6A"),   // Verde
                    crearBase("Ocio", "ic_celebration", "#AB47BC"),     // Morado
                    crearBase("Nómina", "ic_payments", "#FFCA28")       // Dorado/Amarillo
            );

            categoria_repository.saveAll(categoriasBase);
            //System.out.println("Categorías base creadas con éxito.");
        }
    }

    // Método auxiliar para limpiar el código
    private Categoria crearBase(String nombre, String icono, String color) {
        Categoria cat = new Categoria();
        cat.setNombre(nombre);
        cat.setIcono(icono);
        cat.setColor(color);
        cat.setUsuario(null); // Esencial: NULL significa que es del sistema
        return cat;
    }

}
