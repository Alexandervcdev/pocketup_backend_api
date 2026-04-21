package com.pocketup.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*; // <-- Esto importa @Entity y el @Id correcto
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false)
    private String icono; // Guardaremos el nombre del icono (ej: "ic_food")

    @Column(nullable = false)
    private String color; // Guardaremos el Hex (ej: "#FF5733")

    // Si es NULL, es una categoría predefinida del sistema
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = true)
    private Usuario usuario;

    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuarioId;

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
        if (usuarioId != null) {
            Usuario u = new Usuario();
            u.setId(usuarioId);
            this.usuario = u;
        }
    }
}