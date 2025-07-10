package com.dogapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un propietario de perros en el sistema
 * 
 * Esta clase define la estructura de datos para un propietario con todas sus características
 * y relaciones con los perros que posee.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Entity
@Table(name = "owners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^[+]?[0-9]{8,15}$", message = "El número de teléfono debe tener entre 8 y 15 dígitos")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 10, max = 200, message = "La dirección debe tener entre 10 y 200 caracteres")
    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(min = 2, max = 100, message = "La ciudad debe tener entre 2 y 100 caracteres")
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^[0-9]{5}$", message = "El código postal debe tener 5 dígitos")
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "El propietario debe ser mayor de edad")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    @Column(name = "age", nullable = false)
    private Integer age;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(name = "observations", length = 500)
    private String observations;

    @NotNull(message = "El estado activo es obligatorio")
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // Relación con Dogs (Perros)
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Dog> dogs = new ArrayList<>();

    // Campos de auditoría
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Método para obtener el nombre completo del propietario
     * 
     * @return nombre completo (nombre + apellido)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Método para obtener la cantidad de perros que posee el propietario
     * 
     * @return número de perros
     */
    public int getNumberOfDogs() {
        return dogs != null ? dogs.size() : 0;
    }

    /**
     * Método para agregar un perro al propietario
     * 
     * @param dog el perro a agregar
     */
    public void addDog(Dog dog) {
        if (dogs == null) {
            dogs = new ArrayList<>();
        }
        dogs.add(dog);
        dog.setOwner(this);
    }

    /**
     * Método para remover un perro del propietario
     * 
     * @param dog el perro a remover
     */
    public void removeDog(Dog dog) {
        if (dogs != null) {
            dogs.remove(dog);
            dog.setOwner(null);
        }
    }

    /**
     * Método para obtener la información de contacto completa
     * 
     * @return información de contacto
     */
    public String getContactInfo() {
        return String.format("%s - Email: %s, Teléfono: %s, Dirección: %s, %s %s", 
                getFullName(), email, phone, address, city, postalCode);
    }

    /**
     * Método para verificar si el propietario tiene perros
     * 
     * @return true si tiene perros, false en caso contrario
     */
    public boolean hasDogs() {
        return dogs != null && !dogs.isEmpty();
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", numberOfDogs=" + getNumberOfDogs() +
                ", isActive=" + isActive +
                '}';
    }
} 