package com.pocketup.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter @Setter
@Table(name = "meta")
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre; // Ej: "Viaje a italia"

    @Column(nullable = false)
    private String icono; // Ej: "ic_flight"

    @Column(nullable = false)
    private String color;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoObjetivo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoActual = BigDecimal.ZERO;

    @Column(nullable = false)
    private String fechaLimite; // Ej: "2026-12-31" (Formato ISO)

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
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
