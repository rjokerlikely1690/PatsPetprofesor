package com.dogapp.service;

import com.dogapp.model.Dog;
import com.dogapp.model.Owner;
import com.dogapp.repository.DogRepository;
import com.dogapp.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para DogService
 * 
 * Prueba todas las funcionalidades del servicio de perros utilizando mocks
 * para aislar las dependencias y verificar el comportamiento esperado.
 * 
 * @author Tu Nombre
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class DogServiceTest {

    @Mock
    private DogRepository dogRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private DogService dogService;

    private Dog testDog;
    private Owner testOwner;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        testDog = Dog.builder()
            .id(1L)
            .name("Max")
            .breed("Golden Retriever")
            .age(3)
            .color("Dorado")
            .weight(25.5)
            .birthDate(LocalDate.now().minusYears(3))
            .description("Perro muy amigable")
            .isVaccinated(true)
            .isAvailable(true)
            .size("MEDIANO")
            .build();

        testOwner = Owner.builder()
            .id(1L)
            .firstName("Juan")
            .lastName("Pérez")
            .email("juan@email.com")
            .phone("123456789")
            .address("Calle 123")
            .city("Bogotá")
            .postalCode("11111")
            .age(30)
            .isActive(true)
            .build();
    }

    @Test
    void getAllDogs_ShouldReturnListOfDogs() {
        // Given
        List<Dog> expectedDogs = Arrays.asList(testDog);
        given(dogRepository.findAll()).willReturn(expectedDogs);

        // When
        List<Dog> actualDogs = dogService.getAllDogs();

        // Then
        assertThat(actualDogs).isNotNull();
        assertThat(actualDogs).hasSize(1);
        assertThat(actualDogs.get(0).getName()).isEqualTo("Max");
        verify(dogRepository).findAll();
    }

    @Test
    void getDogById_WhenDogExists_ShouldReturnDog() {
        // Given
        given(dogRepository.findById(1L)).willReturn(Optional.of(testDog));

        // When
        Optional<Dog> result = dogService.getDogById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Max");
        verify(dogRepository).findById(1L);
    }

    @Test
    void getDogById_WhenDogDoesNotExist_ShouldReturnEmpty() {
        // Given
        given(dogRepository.findById(1L)).willReturn(Optional.empty());

        // When
        Optional<Dog> result = dogService.getDogById(1L);

        // Then
        assertThat(result).isEmpty();
        verify(dogRepository).findById(1L);
    }

    @Test
    void createDog_WithValidData_ShouldReturnSavedDog() {
        // Given
        given(dogRepository.existsByNameIgnoreCase("Max")).willReturn(false);
        given(dogRepository.save(any(Dog.class))).willReturn(testDog);

        // When
        Dog result = dogService.createDog(testDog);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max");
        verify(dogRepository).existsByNameIgnoreCase("Max");
        verify(dogRepository).save(any(Dog.class));
    }

    @Test
    void createDog_WithDuplicateName_ShouldThrowException() {
        // Given
        given(dogRepository.existsByNameIgnoreCase("Max")).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> dogService.createDog(testDog))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Ya existe un perro con el nombre: Max");
        
        verify(dogRepository).existsByNameIgnoreCase("Max");
        verify(dogRepository, never()).save(any(Dog.class));
    }

    @Test
    void createDog_WithNullName_ShouldThrowException() {
        // Given
        testDog.setName(null);

        // When & Then
        assertThatThrownBy(() -> dogService.createDog(testDog))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("El nombre del perro es obligatorio");
        
        verify(dogRepository, never()).save(any(Dog.class));
    }

    @Test
    void createDog_WithEmptyBreed_ShouldThrowException() {
        // Given
        testDog.setBreed("");

        // When & Then
        assertThatThrownBy(() -> dogService.createDog(testDog))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("La raza del perro es obligatoria");
        
        verify(dogRepository, never()).save(any(Dog.class));
    }

    @Test
    void updateDog_WithValidData_ShouldReturnUpdatedDog() {
        // Given
        Dog updatedDog = Dog.builder()
            .name("Max Updated")
            .breed("Labrador")
            .age(4)
            .color("Negro")
            .weight(30.0)
            .birthDate(LocalDate.now().minusYears(4))
            .isVaccinated(true)
            .isAvailable(false)
            .size("GRANDE")
            .build();

        given(dogRepository.findById(1L)).willReturn(Optional.of(testDog));
        given(dogRepository.existsByNameIgnoreCase("Max Updated")).willReturn(false);
        given(dogRepository.save(any(Dog.class))).willReturn(testDog);

        // When
        Dog result = dogService.updateDog(1L, updatedDog);

        // Then
        assertThat(result).isNotNull();
        verify(dogRepository).findById(1L);
        verify(dogRepository).save(any(Dog.class));
    }

    @Test
    void updateDog_WithNonExistentId_ShouldThrowException() {
        // Given
        given(dogRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> dogService.updateDog(1L, testDog))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Perro no encontrado con ID: 1");
        
        verify(dogRepository).findById(1L);
        verify(dogRepository, never()).save(any(Dog.class));
    }

    @Test
    void deleteDog_WithExistentId_ShouldDeleteSuccessfully() {
        // Given
        given(dogRepository.existsById(1L)).willReturn(true);

        // When
        dogService.deleteDog(1L);

        // Then
        verify(dogRepository).existsById(1L);
        verify(dogRepository).deleteById(1L);
    }

    @Test
    void deleteDog_WithNonExistentId_ShouldThrowException() {
        // Given
        given(dogRepository.existsById(1L)).willReturn(false);

        // When & Then
        assertThatThrownBy(() -> dogService.deleteDog(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Perro no encontrado con ID: 1");
        
        verify(dogRepository).existsById(1L);
        verify(dogRepository, never()).deleteById(1L);
    }

    @Test
    void assignOwnerToDog_WithValidIds_ShouldAssignOwner() {
        // Given
        given(dogRepository.findById(1L)).willReturn(Optional.of(testDog));
        given(ownerRepository.findById(1L)).willReturn(Optional.of(testOwner));
        given(dogRepository.save(any(Dog.class))).willReturn(testDog);

        // When
        Dog result = dogService.assignOwnerToDog(1L, 1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOwner()).isEqualTo(testOwner);
        assertThat(result.getIsAvailable()).isFalse();
        verify(dogRepository).findById(1L);
        verify(ownerRepository).findById(1L);
        verify(dogRepository).save(any(Dog.class));
    }

    @Test
    void assignOwnerToDog_WithNonExistentDog_ShouldThrowException() {
        // Given
        given(dogRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> dogService.assignOwnerToDog(1L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Perro no encontrado con ID: 1");
        
        verify(dogRepository).findById(1L);
        verify(ownerRepository, never()).findById(any());
    }

    @Test
    void assignOwnerToDog_WithNonExistentOwner_ShouldThrowException() {
        // Given
        given(dogRepository.findById(1L)).willReturn(Optional.of(testDog));
        given(ownerRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> dogService.assignOwnerToDog(1L, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Propietario no encontrado con ID: 1");
        
        verify(dogRepository).findById(1L);
        verify(ownerRepository).findById(1L);
        verify(dogRepository, never()).save(any(Dog.class));
    }

    @Test
    void removeOwnerFromDog_WithValidId_ShouldRemoveOwner() {
        // Given
        testDog.setOwner(testOwner);
        testDog.setIsAvailable(false);
        given(dogRepository.findById(1L)).willReturn(Optional.of(testDog));
        given(dogRepository.save(any(Dog.class))).willReturn(testDog);

        // When
        Dog result = dogService.removeOwnerFromDog(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOwner()).isNull();
        assertThat(result.getIsAvailable()).isTrue();
        verify(dogRepository).findById(1L);
        verify(dogRepository).save(any(Dog.class));
    }

    @Test
    void searchDogsByName_ShouldReturnMatchingDogs() {
        // Given
        List<Dog> expectedDogs = Arrays.asList(testDog);
        given(dogRepository.findByNameContainingIgnoreCase("Max")).willReturn(expectedDogs);

        // When
        List<Dog> result = dogService.searchDogsByName("Max");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Max");
        verify(dogRepository).findByNameContainingIgnoreCase("Max");
    }

    @Test
    void searchDogsByBreed_ShouldReturnMatchingDogs() {
        // Given
        List<Dog> expectedDogs = Arrays.asList(testDog);
        given(dogRepository.findByBreedIgnoreCase("Golden Retriever")).willReturn(expectedDogs);

        // When
        List<Dog> result = dogService.searchDogsByBreed("Golden Retriever");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBreed()).isEqualTo("Golden Retriever");
        verify(dogRepository).findByBreedIgnoreCase("Golden Retriever");
    }

    @Test
    void getAvailableDogsForAdoption_ShouldReturnAvailableDogs() {
        // Given
        List<Dog> expectedDogs = Arrays.asList(testDog);
        given(dogRepository.findAvailableForAdoption()).willReturn(expectedDogs);

        // When
        List<Dog> result = dogService.getAvailableDogsForAdoption();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsAvailable()).isTrue();
        verify(dogRepository).findAvailableForAdoption();
    }

    @Test
    void getPuppies_ShouldReturnPuppies() {
        // Given
        Dog puppy = Dog.builder()
            .id(2L)
            .name("Buddy")
            .breed("Bulldog")
            .age(0)
            .color("Blanco")
            .weight(5.0)
            .birthDate(LocalDate.now().minusMonths(6))
            .isVaccinated(false)
            .isAvailable(true)
            .size("PEQUEÑO")
            .build();

        List<Dog> expectedPuppies = Arrays.asList(puppy);
        given(dogRepository.findPuppies()).willReturn(expectedPuppies);

        // When
        List<Dog> result = dogService.getPuppies();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Buddy");
        verify(dogRepository).findPuppies();
    }

    @Test
    void getBreedStatistics_ShouldReturnStatistics() {
        // Given
        Object[] statistic = {"Golden Retriever", 3L};
        List<Object[]> expectedStatistics = new ArrayList<>();
        expectedStatistics.add(statistic);
        given(dogRepository.getBreedStatistics()).willReturn(expectedStatistics);

        // When
        List<Object[]> result = dogService.getBreedStatistics();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("Golden Retriever");
        assertThat(result.get(0)[1]).isEqualTo(3L);
        verify(dogRepository).getBreedStatistics();
    }

    @Test
    void createDog_WithWeightButNoSize_ShouldSetSizeAutomatically() {
        // Given
        Dog dogWithoutSize = Dog.builder()
            .name("TestDog")
            .breed("Test Breed")
            .age(2)
            .color("Test Color")
            .weight(15.0) // Should be MEDIANO
            .birthDate(LocalDate.now().minusYears(2))
            .isVaccinated(true)
            .isAvailable(true)
            .build();

        given(dogRepository.existsByNameIgnoreCase("TestDog")).willReturn(false);
        given(dogRepository.save(any(Dog.class))).willReturn(dogWithoutSize);

        // When
        Dog result = dogService.createDog(dogWithoutSize);

        // Then
        verify(dogRepository).save(argThat(dog -> "MEDIANO".equals(dog.getSize())));
    }

    @Test
    void createDog_WithSmallWeight_ShouldSetSizeToPequeno() {
        // Given
        Dog smallDog = Dog.builder()
            .name("SmallDog")
            .breed("Chihuahua")
            .age(1)
            .color("Marrón")
            .weight(5.0) // Should be PEQUEÑO
            .birthDate(LocalDate.now().minusYears(1))
            .isVaccinated(true)
            .isAvailable(true)
            .build();

        given(dogRepository.existsByNameIgnoreCase("SmallDog")).willReturn(false);
        given(dogRepository.save(any(Dog.class))).willReturn(smallDog);

        // When
        dogService.createDog(smallDog);

        // Then
        verify(dogRepository).save(argThat(dog -> "PEQUEÑO".equals(dog.getSize())));
    }

    @Test
    void createDog_WithLargeWeight_ShouldSetSizeToGrande() {
        // Given
        Dog largeDog = Dog.builder()
            .name("LargeDog")
            .breed("Great Dane")
            .age(2)
            .color("Negro")
            .weight(40.0) // Should be GRANDE
            .birthDate(LocalDate.now().minusYears(2))
            .isVaccinated(true)
            .isAvailable(true)
            .build();

        given(dogRepository.existsByNameIgnoreCase("LargeDog")).willReturn(false);
        given(dogRepository.save(any(Dog.class))).willReturn(largeDog);

        // When
        dogService.createDog(largeDog);

        // Then
        verify(dogRepository).save(argThat(dog -> "GRANDE".equals(dog.getSize())));
    }

    @Test
    void createDog_WithNegativeWeight_ShouldThrowException() {
        // Given
        testDog.setWeight(-5.0);

        // When & Then
        assertThatThrownBy(() -> dogService.createDog(testDog))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("El peso del perro debe ser mayor a 0");
        
        verify(dogRepository, never()).save(any(Dog.class));
    }

    @Test
    void createDog_WithInvalidSize_ShouldThrowException() {
        // Given
        testDog.setSize("GIGANTE");

        // When & Then
        assertThatThrownBy(() -> dogService.createDog(testDog))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("El tamaño debe ser PEQUEÑO, MEDIANO o GRANDE");
        
        verify(dogRepository, never()).save(any(Dog.class));
    }
} 