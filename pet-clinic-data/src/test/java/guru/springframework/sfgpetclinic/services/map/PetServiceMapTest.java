package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PetServiceMapTest {

    private PetService petMapService;

    private final Long petId = 1L;

    @BeforeEach
    void setUp() {

        petMapService = new PetServiceMap();

        petMapService.save(Pet.builder().id(petId).build());
    }

    @Test
    void findAll() {

        Set<Pet> pets = petMapService.findAll();

        assertEquals(1, pets.size());

    }

    @Test
    void findByExistingId() {

        Pet pet = petMapService.findByID(petId);

        assertEquals(petId, pet.getId());
    }

    @Test
    void findByIdNullId() {

        Pet pet = petMapService.findByID(null);

        assertNull(pet);
    }

    @Test
    void saveExistingId() {

        Long id = 2L;

        Pet secondPet = Pet.builder().id(id).build();

        Pet savedPet = petMapService.save(secondPet);

        assertEquals(id, savedPet.getId());

    }

    @Test
    void saveDuplicateId() {

        Long id = 1L;

        Pet secondPet = Pet.builder().id(id).build();

        Pet savedPet = petMapService.save(secondPet);

        assertEquals(id, savedPet.getId());
        assertEquals(1, petMapService.findAll().size());

    }

    @Test
    void saveNoId() {

        Pet savedPet = petMapService.save(Pet.builder().build());

        assertNotNull(savedPet);
        assertNotNull(savedPet.getId());

        assertEquals(2, petMapService.findAll().size());
    }

    @Test
    void deletePet() {

        petMapService.deleteById(petId);

        assertEquals(0, petMapService.findAll().size());
    }

    @Test
    void deleteWithWrongId() {

        Pet pet = Pet.builder().id(5L).build();

        petMapService.delete(pet);

        assertEquals(1, petMapService.findAll().size());

    }


    @Test
    void deleteWithNullId() {

        Pet pet = Pet.builder().build();

        petMapService.delete(pet);

        assertEquals(1, petMapService.findAll().size());

    }

    @Test
    void deleteNull() {

        petMapService.delete(null);

        assertEquals(1, petMapService.findAll().size());

    }

    @Test
    void deleteByIdCorrectId() {

        petMapService.deleteById(petId);

        assertEquals(0, petMapService.findAll().size());
    }

    @Test
    void deleteByIdWrongId() {

        petMapService.deleteById(5L);

        assertEquals(1, petMapService.findAll().size());
    }

    @Test
    void deleteByIdNullId() {

        petMapService.deleteById(null);

        assertEquals(1, petMapService.findAll().size());
    }
}