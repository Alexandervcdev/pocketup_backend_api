package com.pocketup.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*; // <-- Esto importa @Entity y el @Id correcto
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "categoria")
public class Categoria {

    @Id // (Asegúrate de que no haya import org.springframework.data.annotation.Id; arriba)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false)
    private String icono; // Guardaremos el nombre del icono (ej: "ic_food")

    @Column(nullable = false)
    private String color; // Guardaremos el Hex (ej: "#FF5733")

    // Si es NULL, es una categoría predefinida del sistema
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;
}