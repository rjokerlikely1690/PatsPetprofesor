package com.dogapp.controller;

import com.dogapp.model.Dog;
import com.dogapp.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST para la gestión de perros
 * 
 * Proporciona endpoints RESTful para operaciones CRUD y funcionalidades adicionales
 * relacionadas con los perros. Implementa HATEOAS para navegabilidad de la API.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/dogs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dogs", description = "API para gestión de perros")
public class DogController {

    private final DogService dogService;

    /**
     * Obtiene todos los perros del sistema
     * 
     * @return lista de perros con enlaces HATEOAS
     */
    @GetMapping
    @Operation(summary = "Obtener todos los perros", description = "Devuelve una lista de todos los perros registrados en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de perros obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Dog>>> getAllDogs() {
        log.info("GET /api/dogs - Obteniendo todos los perros");
        
        List<Dog> dogs = dogService.getAllDogs();
        
        List<EntityModel<Dog>> dogModels = dogs.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Dog>> response = CollectionModel.of(dogModels);
        response.add(linkTo(methodOn(DogController.class).getAllDogs()).withSelfRel());
        response.add(linkTo(methodOn(DogController.class).getAvailableDogsForAdoption()).withRel("available-for-adoption"));
        response.add(linkTo(methodOn(DogController.class).getPuppies()).withRel("puppies"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un perro por su ID
     * 
     * @param id ID del perro
     * @return perro con enlaces HATEOAS
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener perro por ID", description = "Devuelve un perro específico basado en su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perro encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Perro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Dog>> getDogById(
            @Parameter(description = "ID del perro", required = true)
            @PathVariable Long id) {
        log.info("GET /api/dogs/{} - Obteniendo perro por ID", id);
        
        return dogService.getDogById(id)
            .map(dog -> ResponseEntity.ok(toEntityModel(dog)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo perro
     * 
     * @param dog datos del perro a crear
     * @return perro creado con enlaces HATEOAS
     */
    @PostMapping
    @Operation(summary = "Crear nuevo perro", description = "Crea un nuevo perro en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Perro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "Perro ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Dog>> createDog(
            @Parameter(description = "Datos del perro a crear", required = true)
            @Valid @RequestBody Dog dog) {
        log.info("POST /api/dogs - Creando nuevo perro: {}", dog.getName());
        
        try {
            Dog createdDog = dogService.createDog(dog);
            EntityModel<Dog> response = toEntityModel(createdDog);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear perro: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un perro existente
     * 
     * @param id ID del perro a actualizar
     * @param dog datos actualizados del perro
     * @return perro actualizado con enlaces HATEOAS
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar perro", description = "Actualiza un perro existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perro actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Perro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Dog>> updateDog(
            @Parameter(description = "ID del perro", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del perro", required = true)
            @Valid @RequestBody Dog dog) {
        log.info("PUT /api/dogs/{} - Actualizando perro", id);
        
        try {
            Dog updatedDog = dogService.updateDog(id, dog);
            EntityModel<Dog> response = toEntityModel(updatedDog);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar perro: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un perro del sistema
     * 
     * @param id ID del perro a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar perro", description = "Elimina un perro del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Perro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Perro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteDog(
            @Parameter(description = "ID del perro", required = true)
            @PathVariable Long id) {
        log.info("DELETE /api/dogs/{} - Eliminando perro", id);
        
        try {
            dogService.deleteDog(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar perro: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Asigna un propietario a un perro
     * 
     * @param dogId ID del perro
     * @param ownerId ID del propietario
     * @return perro con propietario asignado
     */
    @PutMapping("/{dogId}/assign-owner/{ownerId}")
    @Operation(summary = "Asignar propietario", description = "Asigna un propietario a un perro")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propietario asignado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Perro o propietario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Dog>> assignOwner(
            @Parameter(description = "ID del perro", required = true)
            @PathVariable Long dogId,
            @Parameter(description = "ID del propietario", required = true)
            @PathVariable Long ownerId) {
        log.info("PUT /api/dogs/{}/assign-owner/{} - Asignando propietario", dogId, ownerId);
        
        try {
            Dog updatedDog = dogService.assignOwnerToDog(dogId, ownerId);
            EntityModel<Dog> response = toEntityModel(updatedDog);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al asignar propietario: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remueve el propietario de un perro
     * 
     * @param dogId ID del perro
     * @return perro sin propietario
     */
    @PutMapping("/{dogId}/remove-owner")
    @Operation(summary = "Remover propietario", description = "Remueve el propietario de un perro")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propietario removido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Perro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Dog>> removeOwner(
            @Parameter(description = "ID del perro", required = true)
            @PathVariable Long dogId) {
        log.info("PUT /api/dogs/{}/remove-owner - Removiendo propietario", dogId);
        
        try {
            Dog updatedDog = dogService.removeOwnerFromDog(dogId);
            EntityModel<Dog> response = toEntityModel(updatedDog);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al remover propietario: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca perros por nombre
     * 
     * @param name nombre del perro
     * @return lista de perros que coinciden con el nombre
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar perros por nombre", description = "Busca perros que coincidan con el nombre proporcionado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Dog>>> searchDogsByName(
            @Parameter(description = "Nombre del perro a buscar", required = true)
            @RequestParam String name) {
        log.info("GET /api/dogs/search?name={} - Buscando perros por nombre", name);
        
        List<Dog> dogs = dogService.searchDogsByName(name);
        
        List<EntityModel<Dog>> dogModels = dogs.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Dog>> response = CollectionModel.of(dogModels);
        response.add(linkTo(methodOn(DogController.class).searchDogsByName(name)).withSelfRel());
        response.add(linkTo(methodOn(DogController.class).getAllDogs()).withRel("all-dogs"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Busca perros por raza
     * 
     * @param breed raza del perro
     * @return lista de perros de la raza especificada
     */
    @GetMapping("/breed/{breed}")
    @Operation(summary = "Buscar perros por raza", description = "Busca perros de una raza específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Dog>>> searchDogsByBreed(
            @Parameter(description = "Raza del perro", required = true)
            @PathVariable String breed) {
        log.info("GET /api/dogs/breed/{} - Buscando perros por raza", breed);
        
        List<Dog> dogs = dogService.searchDogsByBreed(breed);
        
        List<EntityModel<Dog>> dogModels = dogs.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Dog>> response = CollectionModel.of(dogModels);
        response.add(linkTo(methodOn(DogController.class).searchDogsByBreed(breed)).withSelfRel());
        response.add(linkTo(methodOn(DogController.class).getAllDogs()).withRel("all-dogs"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene perros disponibles para adopción
     * 
     * @return lista de perros disponibles para adopción
     */
    @GetMapping("/available-for-adoption")
    @Operation(summary = "Perros disponibles para adopción", description = "Obtiene perros que están disponibles para adopción")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Dog>>> getAvailableDogsForAdoption() {
        log.info("GET /api/dogs/available-for-adoption - Obteniendo perros disponibles para adopción");
        
        List<Dog> dogs = dogService.getAvailableDogsForAdoption();
        
        List<EntityModel<Dog>> dogModels = dogs.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Dog>> response = CollectionModel.of(dogModels);
        response.add(linkTo(methodOn(DogController.class).getAvailableDogsForAdoption()).withSelfRel());
        response.add(linkTo(methodOn(DogController.class).getAllDogs()).withRel("all-dogs"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene perros cachorros
     * 
     * @return lista de perros cachorros
     */
    @GetMapping("/puppies")
    @Operation(summary = "Perros cachorros", description = "Obtiene perros que son cachorros (menos de 1 año)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Dog>>> getPuppies() {
        log.info("GET /api/dogs/puppies - Obteniendo perros cachorros");
        
        List<Dog> dogs = dogService.getPuppies();
        
        List<EntityModel<Dog>> dogModels = dogs.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Dog>> response = CollectionModel.of(dogModels);
        response.add(linkTo(methodOn(DogController.class).getPuppies()).withSelfRel());
        response.add(linkTo(methodOn(DogController.class).getAllDogs()).withRel("all-dogs"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene estadísticas de perros por raza
     * 
     * @return estadísticas por raza
     */
    @GetMapping("/statistics/breed")
    @Operation(summary = "Estadísticas por raza", description = "Obtiene estadísticas de perros agrupadas por raza")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Object[]>> getBreedStatistics() {
        log.info("GET /api/dogs/statistics/breed - Obteniendo estadísticas por raza");
        
        List<Object[]> statistics = dogService.getBreedStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Convierte un Dog a EntityModel con enlaces HATEOAS
     * 
     * @param dog perro a convertir
     * @return EntityModel con enlaces HATEOAS
     */
    private EntityModel<Dog> toEntityModel(Dog dog) {
        EntityModel<Dog> dogModel = EntityModel.of(dog);
        
        // Enlaces básicos
        dogModel.add(linkTo(methodOn(DogController.class).getDogById(dog.getId())).withSelfRel());
        dogModel.add(linkTo(methodOn(DogController.class).getAllDogs()).withRel("all-dogs"));
        
        // Enlaces condicionales
        if (dog.getOwner() != null) {
            dogModel.add(linkTo(methodOn(OwnerController.class).getOwnerById(dog.getOwner().getId())).withRel("owner"));
            dogModel.add(linkTo(methodOn(DogController.class).removeOwner(dog.getId())).withRel("remove-owner"));
        } else {
            dogModel.add(linkTo(methodOn(DogController.class).getAvailableDogsForAdoption()).withRel("available-for-adoption"));
        }
        
        // Enlaces de acciones
        dogModel.add(linkTo(methodOn(DogController.class).updateDog(dog.getId(), dog)).withRel("update"));
        dogModel.add(linkTo(methodOn(DogController.class).deleteDog(dog.getId())).withRel("delete"));
        
        return dogModel;
    }
} 