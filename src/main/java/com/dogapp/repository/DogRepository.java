package com.dogapp.repository;

import com.dogapp.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Dog
 * 
 * Proporciona operaciones CRUD básicas y consultas personalizadas para la gestión de perros.
 * Utiliza Spring Data JPA para simplificar las operaciones de base de datos.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    /**
     * Busca perros por nombre (coincidencia parcial, insensible a mayúsculas)
     * 
     * @param name nombre del perro a buscar
     * @return lista de perros que coinciden con el nombre
     */
    List<Dog> findByNameContainingIgnoreCase(String name);

    /**
     * Busca perros por raza (coincidencia exacta, insensible a mayúsculas)
     * 
     * @param breed raza del perro
     * @return lista de perros de la raza especificada
     */
    List<Dog> findByBreedIgnoreCase(String breed);

    /**
     * Busca perros por color (coincidencia exacta, insensible a mayúsculas)
     * 
     * @param color color del perro
     * @return lista de perros del color especificado
     */
    List<Dog> findByColorIgnoreCase(String color);

    /**
     * Busca perros por rango de edad
     * 
     * @param minAge edad mínima
     * @param maxAge edad máxima
     * @return lista de perros en el rango de edad especificado
     */
    List<Dog> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * Busca perros por estado de disponibilidad
     * 
     * @param isAvailable estado de disponibilidad
     * @return lista de perros disponibles o no disponibles
     */
    List<Dog> findByIsAvailable(Boolean isAvailable);

    /**
     * Busca perros por estado de vacunación
     * 
     * @param isVaccinated estado de vacunación
     * @return lista de perros vacunados o no vacunados
     */
    List<Dog> findByIsVaccinated(Boolean isVaccinated);

    /**
     * Busca perros por tamaño
     * 
     * @param size tamaño del perro (PEQUEÑO, MEDIANO, GRANDE)
     * @return lista de perros del tamaño especificado
     */
    List<Dog> findBySize(String size);

    /**
     * Busca perros por propietario
     * 
     * @param ownerId ID del propietario
     * @return lista de perros del propietario especificado
     */
    List<Dog> findByOwnerId(Long ownerId);

    /**
     * Busca perros sin propietario
     * 
     * @return lista de perros sin propietario asignado
     */
    List<Dog> findByOwnerIsNull();

    /**
     * Busca perros por rango de peso
     * 
     * @param minWeight peso mínimo
     * @param maxWeight peso máximo
     * @return lista de perros en el rango de peso especificado
     */
    List<Dog> findByWeightBetween(Double minWeight, Double maxWeight);

    /**
     * Cuenta la cantidad de perros por raza
     * 
     * @param breed raza del perro
     * @return número de perros de la raza especificada
     */
    Long countByBreedIgnoreCase(String breed);

    /**
     * Verifica si existe un perro con el nombre especificado
     * 
     * @param name nombre del perro
     * @return true si existe, false en caso contrario
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Busca perros disponibles para adopción (sin propietario y disponibles)
     * 
     * @return lista de perros disponibles para adopción
     */
    @Query("SELECT d FROM Dog d WHERE d.owner IS NULL AND d.isAvailable = true")
    List<Dog> findAvailableForAdoption();

    /**
     * Busca perros por múltiples criterios
     * 
     * @param breed raza (opcional)
     * @param color color (opcional)
     * @param minAge edad mínima (opcional)
     * @param maxAge edad máxima (opcional)
     * @return lista de perros que coinciden con los criterios
     */
    @Query("SELECT d FROM Dog d WHERE " +
           "(:breed IS NULL OR LOWER(d.breed) = LOWER(:breed)) AND " +
           "(:color IS NULL OR LOWER(d.color) = LOWER(:color)) AND " +
           "(:minAge IS NULL OR d.age >= :minAge) AND " +
           "(:maxAge IS NULL OR d.age <= :maxAge)")
    List<Dog> findByMultipleCriteria(@Param("breed") String breed,
                                   @Param("color") String color,
                                   @Param("minAge") Integer minAge,
                                   @Param("maxAge") Integer maxAge);

    /**
     * Busca perros cachorros (menos de 1 año)
     * 
     * @return lista de perros cachorros
     */
    @Query("SELECT d FROM Dog d WHERE d.age < 1")
    List<Dog> findPuppies();

    /**
     * Busca perros por nombre del propietario
     * 
     * @param ownerName nombre del propietario
     * @return lista de perros del propietario especificado
     */
    @Query("SELECT d FROM Dog d WHERE d.owner IS NOT NULL AND " +
           "(LOWER(d.owner.firstName) LIKE LOWER(CONCAT('%', :ownerName, '%')) OR " +
           "LOWER(d.owner.lastName) LIKE LOWER(CONCAT('%', :ownerName, '%')))")
    List<Dog> findByOwnerName(@Param("ownerName") String ownerName);

    /**
     * Obtiene estadísticas de perros por raza
     * 
     * @return lista de objetos con breed y count
     */
    @Query("SELECT d.breed, COUNT(d) FROM Dog d GROUP BY d.breed ORDER BY COUNT(d) DESC")
    List<Object[]> getBreedStatistics();

    /**
     * Busca un perro por ID si está disponible
     * 
     * @param id ID del perro
     * @return Optional con el perro si existe y está disponible
     */
    @Query("SELECT d FROM Dog d WHERE d.id = :id AND d.isAvailable = true")
    Optional<Dog> findByIdAndAvailable(@Param("id") Long id);
} 