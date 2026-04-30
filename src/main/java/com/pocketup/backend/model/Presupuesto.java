package com.pocketup.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "presupuesto")
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoLimite;

    // Con esta etiqueta, el servidor entiende el JSON de Android y no lanza Error 400
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @Transient // No se crea en la DB, se calcula al vuelo para el JSON
    private BigDecimal montoGastado;

    // --- RELACIÓN CON USUARIO ---
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

    // --- RELACIÓN CON CATEGORÍA ---
    // Quitamos el CascadeType.REMOVE por seguridad
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    private Categoria categoria;

    @Column(name = "categoria_id", insertable = false, updatable = false)
    private Long categoriaId;

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
        if (categoriaId != null) {
            Categoria c = new Categoria();
            c.setId(categoriaId);
            this.categoria = c;
        }
    }
}