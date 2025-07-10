package com.dogapp.service;

import com.dogapp.model.Owner;
import com.dogapp.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de propietarios
 * 
 * Esta clase contiene toda la lógica de negocio para las operaciones CRUD
 * y funcionalidades adicionales relacionadas con los propietarios de perros.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OwnerService {

    private final OwnerRepository ownerRepository;

    /**
     * Obtiene todos los propietarios del sistema
     * 
     * @return lista de todos los propietarios
     */
    @Transactional(readOnly = true)
    public List<Owner> getAllOwners() {
        log.info("Obteniendo todos los propietarios del sistema");
        return ownerRepository.findAll();
    }

    /**
     * Obtiene un propietario por su ID
     * 
     * @param id ID del propietario
     * @return Optional con el propietario si existe
     */
    @Transactional(readOnly = true)
    public Optional<Owner> getOwnerById(Long id) {
        log.info("Buscando propietario con ID: {}", id);
        return ownerRepository.findById(id);
    }

    /**
     * Crea un nuevo propietario en el sistema
     * 
     * @param owner datos del propietario a crear
     * @return propietario creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    public Owner createOwner(Owner owner) {
        log.info("Creando nuevo propietario: {} {}", owner.getFirstName(), owner.getLastName());
        
        // Validación de negocio
        validateOwnerData(owner);
        
        // Verificar si ya existe un propietario con el mismo email
        if (ownerRepository.existsByEmailIgnoreCase(owner.getEmail())) {
            throw new IllegalArgumentException("Ya existe un propietario con el email: " + owner.getEmail());
        }
        
        // Verificar si ya existe un propietario con el mismo teléfono
        if (ownerRepository.existsByPhone(owner.getPhone())) {
            throw new IllegalArgumentException("Ya existe un propietario con el teléfono: " + owner.getPhone());
        }
        
        Owner savedOwner = ownerRepository.save(owner);
        log.info("Propietario creado exitosamente con ID: {}", savedOwner.getId());
        return savedOwner;
    }

    /**
     * Actualiza un propietario existente
     * 
     * @param id ID del propietario a actualizar
     * @param updatedOwner datos actualizados del propietario
     * @return propietario actualizado
     * @throws IllegalArgumentException si el propietario no existe
     */
    public Owner updateOwner(Long id, Owner updatedOwner) {
        log.info("Actualizando propietario con ID: {}", id);
        
        Owner existingOwner = ownerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado con ID: " + id));
        
        // Validación de negocio
        validateOwnerData(updatedOwner);
        
        // Verificar si el email ya existe en otro propietario
        if (!existingOwner.getEmail().equalsIgnoreCase(updatedOwner.getEmail()) && 
            ownerRepository.existsByEmailIgnoreCase(updatedOwner.getEmail())) {
            throw new IllegalArgumentException("Ya existe otro propietario con el email: " + updatedOwner.getEmail());
        }
        
        // Verificar si el teléfono ya existe en otro propietario
        if (!existingOwner.getPhone().equals(updatedOwner.getPhone()) && 
            ownerRepository.existsByPhone(updatedOwner.getPhone())) {
            throw new IllegalArgumentException("Ya existe otro propietario con el teléfono: " + updatedOwner.getPhone());
        }
        
        // Actualizar campos
        existingOwner.setFirstName(updatedOwner.getFirstName());
        existingOwner.setLastName(updatedOwner.getLastName());
        existingOwner.setEmail(updatedOwner.getEmail());
        existingOwner.setPhone(updatedOwner.getPhone());
        existingOwner.setAddress(updatedOwner.getAddress());
        existingOwner.setCity(updatedOwner.getCity());
        existingOwner.setPostalCode(updatedOwner.getPostalCode());
        existingOwner.setAge(updatedOwner.getAge());
        existingOwner.setObservations(updatedOwner.getObservations());
        existingOwner.setIsActive(updatedOwner.getIsActive());
        
        Owner savedOwner = ownerRepository.save(existingOwner);
        log.info("Propietario actualizado exitosamente: {} {}", savedOwner.getFirstName(), savedOwner.getLastName());
        return savedOwner;
    }

    /**
     * Elimina un propietario del sistema
     * 
     * @param id ID del propietario a eliminar
     * @throws IllegalArgumentException si el propietario no existe o tiene perros asignados
     */
    public void deleteOwner(Long id) {
        log.info("Eliminando propietario con ID: {}", id);
        
        Owner owner = ownerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado con ID: " + id));
        
        // Verificar si el propietario tiene perros asignados
        if (owner.hasDogs()) {
            throw new IllegalArgumentException("No se puede eliminar el propietario porque tiene perros asignados");
        }
        
        ownerRepository.deleteById(id);
        log.info("Propietario eliminado exitosamente con ID: {}", id);
    }

    /**
     * Desactiva un propietario sin eliminarlo
     * 
     * @param id ID del propietario a desactivar
     * @return propietario desactivado
     */
    public Owner deactivateOwner(Long id) {
        log.info("Desactivando propietario con ID: {}", id);
        
        Owner owner = ownerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado con ID: " + id));
        
        owner.setIsActive(false);
        Owner savedOwner = ownerRepository.save(owner);
        log.info("Propietario desactivado exitosamente: {} {}", savedOwner.getFirstName(), savedOwner.getLastName());
        return savedOwner;
    }

    /**
     * Activa un propietario
     * 
     * @param id ID del propietario a activar
     * @return propietario activado
     */
    public Owner activateOwner(Long id) {
        log.info("Activando propietario con ID: {}", id);
        
        Owner owner = ownerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado con ID: " + id));
        
        owner.setIsActive(true);
        Owner savedOwner = ownerRepository.save(owner);
        log.info("Propietario activado exitosamente: {} {}", savedOwner.getFirstName(), savedOwner.getLastName());
        return savedOwner;
    }

    /**
     * Busca propietarios por nombre completo
     * 
     * @param name nombre o apellido del propietario
     * @return lista de propietarios que coinciden con el nombre
     */
    @Transactional(readOnly = true)
    public List<Owner> searchOwnersByName(String name) {
        log.info("Buscando propietarios por nombre: {}", name);
        return ownerRepository.findByFullName(name);
    }

    /**
     * Busca propietarios por email
     * 
     * @param email email del propietario
     * @return Optional con el propietario si existe
     */
    @Transactional(readOnly = true)
    public Optional<Owner> searchOwnerByEmail(String email) {
        log.info("Buscando propietario por email: {}", email);
        return ownerRepository.findByEmailIgnoreCase(email);
    }

    /**
     * Busca propietarios por ciudad
     * 
     * @param city ciudad del propietario
     * @return lista de propietarios de la ciudad especificada
     */
    @Transactional(readOnly = true)
    public List<Owner> searchOwnersByCity(String city) {
        log.info("Buscando propietarios por ciudad: {}", city);
        return ownerRepository.findByCityIgnoreCase(city);
    }

    /**
     * Busca propietarios que tienen perros
     * 
     * @return lista de propietarios con perros
     */
    @Transactional(readOnly = true)
    public List<Owner> getOwnersWithDogs() {
        log.info("Obteniendo propietarios con perros");
        return ownerRepository.findOwnersWithDogs();
    }

    /**
     * Busca propietarios que no tienen perros
     * 
     * @return lista de propietarios sin perros
     */
    @Transactional(readOnly = true)
    public List<Owner> getOwnersWithoutDogs() {
        log.info("Obteniendo propietarios sin perros");
        return ownerRepository.findOwnersWithoutDogs();
    }

    /**
     * Busca propietarios por múltiples criterios
     * 
     * @param city ciudad (opcional)
     * @param minAge edad mínima (opcional)
     * @param maxAge edad máxima (opcional)
     * @param isActive estado activo (opcional)
     * @return lista de propietarios que coinciden con los criterios
     */
    @Transactional(readOnly = true)
    public List<Owner> searchOwnersByMultipleCriteria(String city, Integer minAge, Integer maxAge, Boolean isActive) {
        log.info("Buscando propietarios por múltiples criterios - Ciudad: {}, Edad: {}-{}, Activo: {}", 
                city, minAge, maxAge, isActive);
        return ownerRepository.findByMultipleCriteria(city, minAge, maxAge, isActive);
    }

    /**
     * Obtiene estadísticas de propietarios por ciudad
     * 
     * @return lista de estadísticas por ciudad
     */
    @Transactional(readOnly = true)
    public List<Object[]> getCityStatistics() {
        log.info("Obteniendo estadísticas de propietarios por ciudad");
        return ownerRepository.getCityStatistics();
    }

    /**
     * Obtiene estadísticas de propietarios por número de perros
     * 
     * @return lista de estadísticas por número de perros
     */
    @Transactional(readOnly = true)
    public List<Object[]> getDogOwnershipStatistics() {
        log.info("Obteniendo estadísticas de propietarios por número de perros");
        return ownerRepository.getDogOwnershipStatistics();
    }

    /**
     * Busca propietarios por raza de perro que poseen
     * 
     * @param breed raza del perro
     * @return lista de propietarios con perros de la raza especificada
     */
    @Transactional(readOnly = true)
    public List<Owner> searchOwnersByDogBreed(String breed) {
        log.info("Buscando propietarios por raza de perro: {}", breed);
        return ownerRepository.findByDogBreed(breed);
    }

    /**
     * Obtiene propietarios activos
     * 
     * @return lista de propietarios activos
     */
    @Transactional(readOnly = true)
    public List<Owner> getActiveOwners() {
        log.info("Obteniendo propietarios activos");
        return ownerRepository.findByIsActive(true);
    }

    /**
     * Valida los datos del propietario
     * 
     * @param owner propietario a validar
     * @throws IllegalArgumentException si los datos son inválidos
     */
    private void validateOwnerData(Owner owner) {
        if (owner.getFirstName() == null || owner.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del propietario es obligatorio");
        }
        
        if (owner.getLastName() == null || owner.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del propietario es obligatorio");
        }
        
        if (owner.getEmail() == null || owner.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del propietario es obligatorio");
        }
        
        if (owner.getPhone() == null || owner.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del propietario es obligatorio");
        }
        
        if (owner.getAddress() == null || owner.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección del propietario es obligatoria");
        }
        
        if (owner.getCity() == null || owner.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad del propietario es obligatoria");
        }
        
        if (owner.getPostalCode() == null || owner.getPostalCode().trim().isEmpty()) {
            throw new IllegalArgumentException("El código postal del propietario es obligatorio");
        }
        
        if (owner.getAge() != null && owner.getAge() < 18) {
            throw new IllegalArgumentException("El propietario debe ser mayor de edad");
        }
        
        if (owner.getAge() != null && owner.getAge() > 120) {
            throw new IllegalArgumentException("La edad del propietario no puede ser mayor a 120 años");
        }
        
        // Validar formato de email
        if (!owner.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        
        // Validar formato de teléfono
        if (!owner.getPhone().matches("^[+]?[0-9]{8,15}$")) {
            throw new IllegalArgumentException("El formato del teléfono no es válido");
        }
        
        // Validar formato de código postal
        if (!owner.getPostalCode().matches("^[0-9]{5}$")) {
            throw new IllegalArgumentException("El código postal debe tener 5 dígitos");
        }
    }
} 