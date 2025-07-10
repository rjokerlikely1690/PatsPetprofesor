package com.dogapp.service;

import com.dogapp.model.Dog;
import com.dogapp.model.Owner;
import com.dogapp.repository.DogRepository;
import com.dogapp.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de perros
 * 
 * Esta clase contiene toda la lógica de negocio para las operaciones CRUD
 * y funcionalidades adicionales relacionadas con los perros.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DogService {

    private final DogRepository dogRepository;
    private final OwnerRepository ownerRepository;

    /**
     * Obtiene todos los perros del sistema
     * 
     * @return lista de todos los perros
     */
    @Transactional(readOnly = true)
    public List<Dog> getAllDogs() {
        log.info("Obteniendo todos los perros del sistema");
        return dogRepository.findAll();
    }

    /**
     * Obtiene un perro por su ID
     * 
     * @param id ID del perro
     * @return Optional con el perro si existe
     */
    @Transactional(readOnly = true)
    public Optional<Dog> getDogById(Long id) {
        log.info("Buscando perro con ID: {}", id);
        return dogRepository.findById(id);
    }

    /**
     * Crea un nuevo perro en el sistema
     * 
     * @param dog datos del perro a crear
     * @return perro creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    public Dog createDog(Dog dog) {
        log.info("Creando nuevo perro: {}", dog.getName());
        
        // Validación de negocio
        validateDogData(dog);
        
        // Verificar si ya existe un perro con el mismo nombre
        if (dogRepository.existsByNameIgnoreCase(dog.getName())) {
            throw new IllegalArgumentException("Ya existe un perro con el nombre: " + dog.getName());
        }
        
        // Calcular edad automáticamente si no está establecida
        if (dog.getAge() == null && dog.getBirthDate() != null) {
            dog.setAge(dog.calculateAge());
        }
        
        // Determinar tamaño basado en el peso si no está establecido
        if (dog.getSize() == null && dog.getWeight() != null) {
            dog.setSize(determineSizeByWeight(dog.getWeight()));
        }
        
        Dog savedDog = dogRepository.save(dog);
        log.info("Perro creado exitosamente con ID: {}", savedDog.getId());
        return savedDog;
    }

    /**
     * Actualiza un perro existente
     * 
     * @param id ID del perro a actualizar
     * @param updatedDog datos actualizados del perro
     * @return perro actualizado
     * @throws IllegalArgumentException si el perro no existe
     */
    public Dog updateDog(Long id, Dog updatedDog) {
        log.info("Actualizando perro con ID: {}", id);
        
        Dog existingDog = dogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Perro no encontrado con ID: " + id));
        
        // Validación de negocio
        validateDogData(updatedDog);
        
        // Verificar si el nombre ya existe en otro perro
        if (!existingDog.getName().equalsIgnoreCase(updatedDog.getName()) && 
            dogRepository.existsByNameIgnoreCase(updatedDog.getName())) {
            throw new IllegalArgumentException("Ya existe otro perro con el nombre: " + updatedDog.getName());
        }
        
        // Actualizar campos
        existingDog.setName(updatedDog.getName());
        existingDog.setBreed(updatedDog.getBreed());
        existingDog.setAge(updatedDog.getAge());
        existingDog.setColor(updatedDog.getColor());
        existingDog.setWeight(updatedDog.getWeight());
        existingDog.setBirthDate(updatedDog.getBirthDate());
        existingDog.setDescription(updatedDog.getDescription());
        existingDog.setIsVaccinated(updatedDog.getIsVaccinated());
        existingDog.setIsAvailable(updatedDog.getIsAvailable());
        
        // Actualizar tamaño basado en peso si no está establecido
        if (updatedDog.getSize() == null && updatedDog.getWeight() != null) {
            existingDog.setSize(determineSizeByWeight(updatedDog.getWeight()));
        } else {
            existingDog.setSize(updatedDog.getSize());
        }
        
        Dog savedDog = dogRepository.save(existingDog);
        log.info("Perro actualizado exitosamente: {}", savedDog.getName());
        return savedDog;
    }

    /**
     * Elimina un perro del sistema
     * 
     * @param id ID del perro a eliminar
     * @throws IllegalArgumentException si el perro no existe
     */
    public void deleteDog(Long id) {
        log.info("Eliminando perro con ID: {}", id);
        
        if (!dogRepository.existsById(id)) {
            throw new IllegalArgumentException("Perro no encontrado con ID: " + id);
        }
        
        dogRepository.deleteById(id);
        log.info("Perro eliminado exitosamente con ID: {}", id);
    }

    /**
     * Asigna un propietario a un perro
     * 
     * @param dogId ID del perro
     * @param ownerId ID del propietario
     * @return perro con propietario asignado
     */
    public Dog assignOwnerToDog(Long dogId, Long ownerId) {
        log.info("Asignando propietario {} al perro {}", ownerId, dogId);
        
        Dog dog = dogRepository.findById(dogId)
            .orElseThrow(() -> new IllegalArgumentException("Perro no encontrado con ID: " + dogId));
        
        Owner owner = ownerRepository.findById(ownerId)
            .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado con ID: " + ownerId));
        
        dog.setOwner(owner);
        dog.setIsAvailable(false); // El perro ya no está disponible
        
        Dog savedDog = dogRepository.save(dog);
        log.info("Propietario asignado exitosamente al perro: {}", savedDog.getName());
        return savedDog;
    }

    /**
     * Remueve el propietario de un perro
     * 
     * @param dogId ID del perro
     * @return perro sin propietario
     */
    public Dog removeOwnerFromDog(Long dogId) {
        log.info("Removiendo propietario del perro con ID: {}", dogId);
        
        Dog dog = dogRepository.findById(dogId)
            .orElseThrow(() -> new IllegalArgumentException("Perro no encontrado con ID: " + dogId));
        
        dog.setOwner(null);
        dog.setIsAvailable(true); // El perro está disponible nuevamente
        
        Dog savedDog = dogRepository.save(dog);
        log.info("Propietario removido exitosamente del perro: {}", savedDog.getName());
        return savedDog;
    }

    /**
     * Busca perros por nombre
     * 
     * @param name nombre del perro
     * @return lista de perros que coinciden con el nombre
     */
    @Transactional(readOnly = true)
    public List<Dog> searchDogsByName(String name) {
        log.info("Buscando perros por nombre: {}", name);
        return dogRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Busca perros por raza
     * 
     * @param breed raza del perro
     * @return lista de perros de la raza especificada
     */
    @Transactional(readOnly = true)
    public List<Dog> searchDogsByBreed(String breed) {
        log.info("Buscando perros por raza: {}", breed);
        return dogRepository.findByBreedIgnoreCase(breed);
    }

    /**
     * Busca perros disponibles para adopción
     * 
     * @return lista de perros disponibles para adopción
     */
    @Transactional(readOnly = true)
    public List<Dog> getAvailableDogsForAdoption() {
        log.info("Obteniendo perros disponibles para adopción");
        return dogRepository.findAvailableForAdoption();
    }

    /**
     * Busca perros cachorros
     * 
     * @return lista de perros cachorros
     */
    @Transactional(readOnly = true)
    public List<Dog> getPuppies() {
        log.info("Obteniendo perros cachorros");
        return dogRepository.findPuppies();
    }

    /**
     * Busca perros por múltiples criterios
     * 
     * @param breed raza (opcional)
     * @param color color (opcional)
     * @param minAge edad mínima (opcional)
     * @param maxAge edad máxima (opcional)
     * @return lista de perros que coinciden con los criterios
     */
    @Transactional(readOnly = true)
    public List<Dog> searchDogsByMultipleCriteria(String breed, String color, Integer minAge, Integer maxAge) {
        log.info("Buscando perros por múltiples criterios - Raza: {}, Color: {}, Edad: {}-{}", 
                breed, color, minAge, maxAge);
        return dogRepository.findByMultipleCriteria(breed, color, minAge, maxAge);
    }

    /**
     * Obtiene estadísticas de perros por raza
     * 
     * @return lista de estadísticas por raza
     */
    @Transactional(readOnly = true)
    public List<Object[]> getBreedStatistics() {
        log.info("Obteniendo estadísticas de perros por raza");
        return dogRepository.getBreedStatistics();
    }

    /**
     * Valida los datos del perro
     * 
     * @param dog perro a validar
     * @throws IllegalArgumentException si los datos son inválidos
     */
    private void validateDogData(Dog dog) {
        if (dog.getName() == null || dog.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del perro es obligatorio");
        }
        
        if (dog.getBreed() == null || dog.getBreed().trim().isEmpty()) {
            throw new IllegalArgumentException("La raza del perro es obligatoria");
        }
        
        if (dog.getAge() != null && dog.getAge() < 0) {
            throw new IllegalArgumentException("La edad del perro no puede ser negativa");
        }
        
        if (dog.getWeight() != null && dog.getWeight() <= 0) {
            throw new IllegalArgumentException("El peso del perro debe ser mayor a 0");
        }
        
        if (dog.getSize() != null && 
            !dog.getSize().matches("^(PEQUEÑO|MEDIANO|GRANDE)$")) {
            throw new IllegalArgumentException("El tamaño debe ser PEQUEÑO, MEDIANO o GRANDE");
        }
    }

    /**
     * Determina el tamaño del perro basado en su peso
     * 
     * @param weight peso del perro
     * @return tamaño del perro
     */
    private String determineSizeByWeight(Double weight) {
        if (weight <= 10.0) {
            return "PEQUEÑO";
        } else if (weight <= 25.0) {
            return "MEDIANO";
        } else {
            return "GRANDE";
        }
    }
} 