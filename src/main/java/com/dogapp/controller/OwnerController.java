package com.dogapp.controller;

import com.dogapp.model.Owner;
import com.dogapp.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST para la gestión de propietarios
 * 
 * Proporciona endpoints RESTful para operaciones CRUD y funcionalidades adicionales
 * relacionadas con los propietarios de perros. Implementa HATEOAS para navegabilidad de la API.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Owners", description = "API para gestión de propietarios de perros")
public class OwnerController {

    private final OwnerService ownerService;

    /**
     * Obtiene todos los propietarios del sistema
     * 
     * @return lista de propietarios con enlaces HATEOAS
     */
    @GetMapping
    @Operation(summary = "Obtener todos los propietarios", description = "Devuelve una lista de todos los propietarios registrados en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de propietarios obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Owner>>> getAllOwners() {
        log.info("GET /api/owners - Obteniendo todos los propietarios");
        
        List<Owner> owners = ownerService.getAllOwners();
        
        List<EntityModel<Owner>> ownerModels = owners.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Owner>> response = CollectionModel.of(ownerModels);
        response.add(linkTo(methodOn(OwnerController.class).getAllOwners()).withSelfRel());
        response.add(linkTo(methodOn(OwnerController.class).getOwnersWithDogs()).withRel("owners-with-dogs"));
        response.add(linkTo(methodOn(OwnerController.class).getOwnersWithoutDogs()).withRel("owners-without-dogs"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un propietario por su ID
     * 
     * @param id ID del propietario
     * @return propietario con enlaces HATEOAS
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener propietario por ID", description = "Devuelve un propietario específico basado en su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propietario encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propietario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Owner>> getOwnerById(
            @Parameter(description = "ID del propietario", required = true)
            @PathVariable Long id) {
        log.info("GET /api/owners/{} - Obteniendo propietario por ID", id);
        
        return ownerService.getOwnerById(id)
            .map(owner -> ResponseEntity.ok(toEntityModel(owner)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo propietario
     * 
     * @param owner datos del propietario a crear
     * @return propietario creado con enlaces HATEOAS
     */
    @PostMapping
    @Operation(summary = "Crear nuevo propietario", description = "Crea un nuevo propietario en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Propietario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "Propietario ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Owner>> createOwner(
            @Parameter(description = "Datos del propietario a crear", required = true)
            @Valid @RequestBody Owner owner) {
        log.info("POST /api/owners - Creando nuevo propietario: {} {}", owner.getFirstName(), owner.getLastName());
        
        try {
            Owner createdOwner = ownerService.createOwner(owner);
            EntityModel<Owner> response = toEntityModel(createdOwner);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear propietario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un propietario existente
     * 
     * @param id ID del propietario a actualizar
     * @param owner datos actualizados del propietario
     * @return propietario actualizado con enlaces HATEOAS
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar propietario", description = "Actualiza un propietario existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propietario actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Propietario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Owner>> updateOwner(
            @Parameter(description = "ID del propietario", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del propietario", required = true)
            @Valid @RequestBody Owner owner) {
        log.info("PUT /api/owners/{} - Actualizando propietario", id);
        
        try {
            Owner updatedOwner = ownerService.updateOwner(id, owner);
            EntityModel<Owner> response = toEntityModel(updatedOwner);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar propietario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un propietario del sistema
     * 
     * @param id ID del propietario a eliminar
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar propietario", description = "Elimina un propietario del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Propietario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propietario no encontrado"),
        @ApiResponse(responseCode = "409", description = "Propietario tiene perros asignados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteOwner(
            @Parameter(description = "ID del propietario", required = true)
            @PathVariable Long id) {
        log.info("DELETE /api/owners/{} - Eliminando propietario", id);
        
        try {
            ownerService.deleteOwner(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar propietario: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Desactiva un propietario
     * 
     * @param id ID del propietario a desactivar
     * @return propietario desactivado con enlaces HATEOAS
     */
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Desactivar propietario", description = "Desactiva un propietario sin eliminarlo del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propietario desactivado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propietario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Owner>> deactivateOwner(
            @Parameter(description = "ID del propietario", required = true)
            @PathVariable Long id) {
        log.info("PUT /api/owners/{}/deactivate - Desactivando propietario", id);
        
        try {
            Owner deactivatedOwner = ownerService.deactivateOwner(id);
            EntityModel<Owner> response = toEntityModel(deactivatedOwner);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al desactivar propietario: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Activa un propietario
     * 
     * @param id ID del propietario a activar
     * @return propietario activado con enlaces HATEOAS
     */
    @PutMapping("/{id}/activate")
    @Operation(summary = "Activar propietario", description = "Activa un propietario desactivado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propietario activado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propietario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EntityModel<Owner>> activateOwner(
            @Parameter(description = "ID del propietario", required = true)
            @PathVariable Long id) {
        log.info("PUT /api/owners/{}/activate - Activando propietario", id);
        
        try {
            Owner activatedOwner = ownerService.activateOwner(id);
            EntityModel<Owner> response = toEntityModel(activatedOwner);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al activar propietario: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca propietarios por nombre
     * 
     * @param name nombre del propietario
     * @return lista de propietarios que coinciden con el nombre
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar propietarios por nombre", description = "Busca propietarios que coincidan con el nombre proporcionado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Owner>>> searchOwnersByName(
            @Parameter(description = "Nombre del propietario a buscar", required = true)
            @RequestParam String name) {
        log.info("GET /api/owners/search?name={} - Buscando propietarios por nombre", name);
        
        List<Owner> owners = ownerService.searchOwnersByName(name);
        
        List<EntityModel<Owner>> ownerModels = owners.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Owner>> response = CollectionModel.of(ownerModels);
        response.add(linkTo(methodOn(OwnerController.class).searchOwnersByName(name)).withSelfRel());
        response.add(linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("all-owners"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Busca propietarios por ciudad
     * 
     * @param city ciudad del propietario
     * @return lista de propietarios de la ciudad especificada
     */
    @GetMapping("/city/{city}")
    @Operation(summary = "Buscar propietarios por ciudad", description = "Busca propietarios de una ciudad específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Owner>>> searchOwnersByCity(
            @Parameter(description = "Ciudad del propietario", required = true)
            @PathVariable String city) {
        log.info("GET /api/owners/city/{} - Buscando propietarios por ciudad", city);
        
        List<Owner> owners = ownerService.searchOwnersByCity(city);
        
        List<EntityModel<Owner>> ownerModels = owners.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Owner>> response = CollectionModel.of(ownerModels);
        response.add(linkTo(methodOn(OwnerController.class).searchOwnersByCity(city)).withSelfRel());
        response.add(linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("all-owners"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene propietarios que tienen perros
     * 
     * @return lista de propietarios con perros
     */
    @GetMapping("/with-dogs")
    @Operation(summary = "Propietarios con perros", description = "Obtiene propietarios que tienen al menos un perro")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Owner>>> getOwnersWithDogs() {
        log.info("GET /api/owners/with-dogs - Obteniendo propietarios con perros");
        
        List<Owner> owners = ownerService.getOwnersWithDogs();
        
        List<EntityModel<Owner>> ownerModels = owners.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Owner>> response = CollectionModel.of(ownerModels);
        response.add(linkTo(methodOn(OwnerController.class).getOwnersWithDogs()).withSelfRel());
        response.add(linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("all-owners"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene propietarios que no tienen perros
     * 
     * @return lista de propietarios sin perros
     */
    @GetMapping("/without-dogs")
    @Operation(summary = "Propietarios sin perros", description = "Obtiene propietarios que no tienen perros")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Owner>>> getOwnersWithoutDogs() {
        log.info("GET /api/owners/without-dogs - Obteniendo propietarios sin perros");
        
        List<Owner> owners = ownerService.getOwnersWithoutDogs();
        
        List<EntityModel<Owner>> ownerModels = owners.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Owner>> response = CollectionModel.of(ownerModels);
        response.add(linkTo(methodOn(OwnerController.class).getOwnersWithoutDogs()).withSelfRel());
        response.add(linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("all-owners"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene propietarios activos
     * 
     * @return lista de propietarios activos
     */
    @GetMapping("/active")
    @Operation(summary = "Propietarios activos", description = "Obtiene propietarios que están activos en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<CollectionModel<EntityModel<Owner>>> getActiveOwners() {
        log.info("GET /api/owners/active - Obteniendo propietarios activos");
        
        List<Owner> owners = ownerService.getActiveOwners();
        
        List<EntityModel<Owner>> ownerModels = owners.stream()
            .map(this::toEntityModel)
            .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Owner>> response = CollectionModel.of(ownerModels);
        response.add(linkTo(methodOn(OwnerController.class).getActiveOwners()).withSelfRel());
        response.add(linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("all-owners"));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene estadísticas de propietarios por ciudad
     * 
     * @return estadísticas por ciudad
     */
    @GetMapping("/statistics/city")
    @Operation(summary = "Estadísticas por ciudad", description = "Obtiene estadísticas de propietarios agrupadas por ciudad")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Object[]>> getCityStatistics() {
        log.info("GET /api/owners/statistics/city - Obteniendo estadísticas por ciudad");
        
        List<Object[]> statistics = ownerService.getCityStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Obtiene estadísticas de propietarios por número de perros
     * 
     * @return estadísticas por número de perros
     */
    @GetMapping("/statistics/dog-ownership")
    @Operation(summary = "Estadísticas por número de perros", description = "Obtiene estadísticas de propietarios agrupadas por número de perros que poseen")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<Object[]>> getDogOwnershipStatistics() {
        log.info("GET /api/owners/statistics/dog-ownership - Obteniendo estadísticas por número de perros");
        
        List<Object[]> statistics = ownerService.getDogOwnershipStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Convierte un Owner a EntityModel con enlaces HATEOAS
     * 
     * @param owner propietario a convertir
     * @return EntityModel con enlaces HATEOAS
     */
    private EntityModel<Owner> toEntityModel(Owner owner) {
        EntityModel<Owner> ownerModel = EntityModel.of(owner);
        
        // Enlaces básicos
        ownerModel.add(linkTo(methodOn(OwnerController.class).getOwnerById(owner.getId())).withSelfRel());
        ownerModel.add(linkTo(methodOn(OwnerController.class).getAllOwners()).withRel("all-owners"));
        
        // Enlaces condicionales
        if (owner.getIsActive()) {
            ownerModel.add(linkTo(methodOn(OwnerController.class).deactivateOwner(owner.getId())).withRel("deactivate"));
        } else {
            ownerModel.add(linkTo(methodOn(OwnerController.class).activateOwner(owner.getId())).withRel("activate"));
        }
        
        // Enlaces de acciones
        ownerModel.add(linkTo(methodOn(OwnerController.class).updateOwner(owner.getId(), owner)).withRel("update"));
        ownerModel.add(linkTo(methodOn(OwnerController.class).deleteOwner(owner.getId())).withRel("delete"));
        
        // Enlaces relacionados con perros
        if (owner.hasDogs()) {
            ownerModel.add(linkTo(methodOn(OwnerController.class).getOwnersWithDogs()).withRel("owners-with-dogs"));
        } else {
            ownerModel.add(linkTo(methodOn(OwnerController.class).getOwnersWithoutDogs()).withRel("owners-without-dogs"));
        }
        
        return ownerModel;
    }
} 