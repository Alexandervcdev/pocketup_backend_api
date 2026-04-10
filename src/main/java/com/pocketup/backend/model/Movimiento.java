package com.pocketup.backend.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "movimiento")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;
    @Column(nullable = false)
    private LocalDate fecha;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType tipo; // Será "INGRESO" o "GASTO"
//    @Column(nullable = false)
//    private String categoria; //"Salario", "Inversiones", "Comida"
    @Column(length = 500)
    private String nota; // Etiqueta o nota opcional
//    @Column(name = "archivo_url")
//    private String archivoUrl; // Ruta de la factura/imagen adjunta

    // RELACIÓN 1:N -> Muchos movimientos pertenecen a un Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "movimientos"})
    private Usuario usuario;
}
