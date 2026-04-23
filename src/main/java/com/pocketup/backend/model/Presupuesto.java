package com.pocketup.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@Table(name = "presupuesto")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoLimite;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuarioId;
    @Transient // Esto le dice a JPA: "No crees una columna para esto, es solo para el JSON"
    private BigDecimal montoGastado;

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
        if (usuarioId != null) {
            Usuario u = new Usuario();
            u.setId(usuarioId);
            this.usuario = u;
        }
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
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
