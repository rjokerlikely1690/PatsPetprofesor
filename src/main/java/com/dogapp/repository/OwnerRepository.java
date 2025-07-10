package com.dogapp.repository;

import com.dogapp.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Owner
 * 
 * Proporciona operaciones CRUD básicas y consultas personalizadas para la gestión de propietarios.
 * Utiliza Spring Data JPA para simplificar las operaciones de base de datos.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    /**
     * Busca propietarios por nombre (coincidencia parcial, insensible a mayúsculas)
     * 
     * @param firstName nombre del propietario
     * @return lista de propietarios que coinciden con el nombre
     */
    List<Owner> findByFirstNameContainingIgnoreCase(String firstName);

    /**
     * Busca propietarios por apellido (coincidencia parcial, insensible a mayúsculas)
     * 
     * @param lastName apellido del propietario
     * @return lista de propietarios que coinciden con el apellido
     */
    List<Owner> findByLastNameContainingIgnoreCase(String lastName);

    /**
     * Busca propietarios por email (coincidencia exacta, insensible a mayúsculas)
     * 
     * @param email email del propietario
     * @return Optional con el propietario si existe
     */
    Optional<Owner> findByEmailIgnoreCase(String email);

    /**
     * Busca propietarios por teléfono
     * 
     * @param phone teléfono del propietario
     * @return Optional con el propietario si existe
     */
    Optional<Owner> findByPhone(String phone);

    /**
     * Busca propietarios por ciudad (coincidencia exacta, insensible a mayúsculas)
     * 
     * @param city ciudad del propietario
     * @return lista de propietarios de la ciudad especificada
     */
    List<Owner> findByCityIgnoreCase(String city);

    /**
     * Busca propietarios por código postal
     * 
     * @param postalCode código postal
     * @return lista de propietarios del código postal especificado
     */
    List<Owner> findByPostalCode(String postalCode);

    /**
     * Busca propietarios por rango de edad
     * 
     * @param minAge edad mínima
     * @param maxAge edad máxima
     * @return lista de propietarios en el rango de edad especificado
     */
    List<Owner> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * Busca propietarios por estado activo
     * 
     * @param isActive estado activo
     * @return lista de propietarios activos o inactivos
     */
    List<Owner> findByIsActive(Boolean isActive);

    /**
     * Verifica si existe un propietario con el email especificado
     * 
     * @param email email del propietario
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Verifica si existe un propietario con el teléfono especificado
     * 
     * @param phone teléfono del propietario
     * @return true si existe, false en caso contrario
     */
    boolean existsByPhone(String phone);

    /**
     * Busca propietarios por nombre completo (nombre o apellido)
     * 
     * @param name nombre o apellido a buscar
     * @return lista de propietarios que coinciden
     */
    @Query("SELECT o FROM Owner o WHERE " +
           "LOWER(o.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(o.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Owner> findByFullName(@Param("name") String name);

    /**
     * Busca propietarios con perros
     * 
     * @return lista de propietarios que tienen al menos un perro
     */
    @Query("SELECT DISTINCT o FROM Owner o JOIN o.dogs d WHERE d.id IS NOT NULL")
    List<Owner> findOwnersWithDogs();

    /**
     * Busca propietarios sin perros
     * 
     * @return lista de propietarios que no tienen perros
     */
    @Query("SELECT o FROM Owner o WHERE o.dogs IS EMPTY")
    List<Owner> findOwnersWithoutDogs();

    /**
     * Busca propietarios por número de perros
     * 
     * @param numberOfDogs número exacto de perros
     * @return lista de propietarios con el número especificado de perros
     */
    @Query("SELECT o FROM Owner o WHERE SIZE(o.dogs) = :numberOfDogs")
    List<Owner> findByNumberOfDogs(@Param("numberOfDogs") int numberOfDogs);

    /**
     * Busca propietarios con más de cierto número de perros
     * 
     * @param minNumberOfDogs número mínimo de perros
     * @return lista de propietarios con más del número especificado de perros
     */
    @Query("SELECT o FROM Owner o WHERE SIZE(o.dogs) > :minNumberOfDogs")
    List<Owner> findOwnersWithMoreThanDogs(@Param("minNumberOfDogs") int minNumberOfDogs);

    /**
     * Busca propietarios por múltiples criterios
     * 
     * @param city ciudad (opcional)
     * @param minAge edad mínima (opcional)
     * @param maxAge edad máxima (opcional)
     * @param isActive estado activo (opcional)
     * @return lista de propietarios que coinciden con los criterios
     */
    @Query("SELECT o FROM Owner o WHERE " +
           "(:city IS NULL OR LOWER(o.city) = LOWER(:city)) AND " +
           "(:minAge IS NULL OR o.age >= :minAge) AND " +
           "(:maxAge IS NULL OR o.age <= :maxAge) AND " +
           "(:isActive IS NULL OR o.isActive = :isActive)")
    List<Owner> findByMultipleCriteria(@Param("city") String city,
                                     @Param("minAge") Integer minAge,
                                     @Param("maxAge") Integer maxAge,
                                     @Param("isActive") Boolean isActive);

    /**
     * Obtiene estadísticas de propietarios por ciudad
     * 
     * @return lista de objetos con city y count
     */
    @Query("SELECT o.city, COUNT(o) FROM Owner o GROUP BY o.city ORDER BY COUNT(o) DESC")
    List<Object[]> getCityStatistics();

    /**
     * Obtiene estadísticas de propietarios por número de perros
     * 
     * @return lista de objetos con numberOfDogs y count
     */
    @Query("SELECT SIZE(o.dogs), COUNT(o) FROM Owner o GROUP BY SIZE(o.dogs) ORDER BY SIZE(o.dogs)")
    List<Object[]> getDogOwnershipStatistics();

    /**
     * Busca propietarios por dirección (coincidencia parcial)
     * 
     * @param address dirección a buscar
     * @return lista de propietarios que coinciden con la dirección
     */
    List<Owner> findByAddressContainingIgnoreCase(String address);

    /**
     * Busca propietarios activos en una ciudad específica
     * 
     * @param city ciudad
     * @return lista de propietarios activos en la ciudad especificada
     */
    @Query("SELECT o FROM Owner o WHERE LOWER(o.city) = LOWER(:city) AND o.isActive = true")
    List<Owner> findActiveOwnersByCity(@Param("city") String city);

    /**
     * Cuenta el número total de perros de todos los propietarios
     * 
     * @return número total de perros
     */
    @Query("SELECT COUNT(d) FROM Owner o JOIN o.dogs d")
    Long countTotalDogs();

    /**
     * Busca propietarios por raza de perro que poseen
     * 
     * @param breed raza del perro
     * @return lista de propietarios que tienen perros de la raza especificada
     */
    @Query("SELECT DISTINCT o FROM Owner o JOIN o.dogs d WHERE LOWER(d.breed) = LOWER(:breed)")
    List<Owner> findByDogBreed(@Param("breed") String breed);

    /**
     * Busca un propietario por ID si está activo
     * 
     * @param id ID del propietario
     * @return Optional con el propietario si existe y está activo
     */
    @Query("SELECT o FROM Owner o WHERE o.id = :id AND o.isActive = true")
    Optional<Owner> findByIdAndActive(@Param("id") Long id);
} 