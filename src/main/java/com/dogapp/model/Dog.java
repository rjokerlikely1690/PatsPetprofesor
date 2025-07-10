package com.dogapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa un perro en el sistema
 * 
 * Esta clase define la estructura de datos para un perro con todas sus características
 * y relaciones con otras entidades del sistema.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Entity
@Table(name = "dogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre del perro es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank(message = "La raza es obligatoria")
    @Size(min = 3, max = 100, message = "La raza debe tener entre 3 y 100 caracteres")
    @Column(name = "breed", nullable = false, length = 100)
    private String breed;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 30, message = "La edad no puede ser mayor a 30 años")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotBlank(message = "El color es obligatorio")
    @Size(min = 3, max = 50, message = "El color debe tener entre 3 y 50 caracteres")
    @Column(name = "color", nullable = false, length = 50)
    private String color;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0.1 kg")
    @DecimalMax(value = "200.0", message = "El peso no puede ser mayor a 200 kg")
    @Column(name = "weight", nullable = false)
    private Double weight;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(name = "description", length = 500)
    private String description;

    @NotNull(message = "El estado de vacunación es obligatorio")
    @Column(name = "is_vaccinated", nullable = false)
    private Boolean isVaccinated;

    @Pattern(regexp = "^(PEQUEÑO|MEDIANO|GRANDE)$", message = "El tamaño debe ser PEQUEÑO, MEDIANO o GRANDE")
    @Column(name = "size", length = 20)
    private String size;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    // Relación con Owner (Propietario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    // Campos de auditoría
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Método para calcular la edad automáticamente basada en la fecha de nacimiento
     * 
     * @return edad calculada en años
     */
    public int calculateAge() {
        if (birthDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    /**
     * Método para determinar si el perro es cachorro
     * 
     * @return true si es cachorro (menos de 1 año), false en caso contrario
     */
    public boolean isPuppy() {
        return calculateAge() < 1;
    }

    /**
     * Método para obtener información completa del perro
     * 
     * @return descripción completa del perro
     */
    public String getFullInfo() {
        return String.format("%s - %s de %d años, color %s, peso %.2f kg", 
                name, breed, age, color, weight);
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                ", weight=" + weight +
                ", isAvailable=" + isAvailable +
                '}';
    }
} 