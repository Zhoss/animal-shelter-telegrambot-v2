package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.Cat;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.PetRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Pet;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    DogRepository dogRepositoryMock;

    @Mock
    CatRepository catRepositoryMock;

    @Mock
    ModelMapper modelMapperMock;

    @InjectMocks
    PetService petServiceOut;

    private long id1 = 1;
    private long id2 = 2;
    private long id3 = 3;
    private boolean taken1 = true;
    private boolean taken2 = false;
    private boolean onProbation1 = true;
    private boolean onProbation2 = false;

    private Pet pet1 = new Pet();
    private Pet pet2 = new Pet();
    private Pet pet3 = new Pet();
    private Cat cat1 = new Cat();
    private Cat cat2 = new Cat();
    private Cat cat3 = new Cat();
    private Dog dog1 = new Dog();
    private Dog dog2 = new Dog();
    private Dog dog3 = new Dog();

    private PetRecord petRecord1 = new PetRecord();
    private PetRecord petRecord2 = new PetRecord();
    private PetRecord petRecord3 = new PetRecord();
    private PetRecord petRecord4 = new PetRecord();

    List<Dog> dogs = new ArrayList<>();
    List<Cat> cats = new ArrayList<>();
    List<PetRecord> petRecords = new ArrayList<>();


    @BeforeEach
    void setUp() {

        petRecord4.setPetType(RET);

        pet1.setId(id1);
        pet2.setId(id2);
        pet3.setId(id3);
        dog1.setId(id1);
        dog2.setId(id2);
        dog3.setId(id3);
        cat1.setId(id1);
        cat2.setId(id2);
        cat3.setId(id3);
        dog1.setTaken(taken1);
        cat1.setTaken(taken1);
        dog1.setOnProbation(onProbation1);
        cat1.setOnProbation(onProbation1);

        petRecord1.setId(pet1.getId());
        petRecord1.setPetType(PetType.CAT);
        petRecord2.setId(pet2.getId());
        petRecord3.setId(pet3.getId());

        dogs.add(dog1);
        dogs.add(dog2);
        dogs.add(dog3);

        cats.add(cat1);
        cats.add(cat2);
        cats.add(cat3);

        petRecords.add(petRecord1);
        petRecords.add(petRecord2);
        petRecords.add(petRecord3);
    }

    @Test
    void shouldReturnPetRecordWhenAddPet() {

        when(modelMapperMock.mapToPetEntity(petRecord1)).thenReturn(cat1);
        when(catRepositoryMock.save(cat1)).thenReturn(cat1);
        when(modelMapperMock.mapToPetRecord(cat1)).thenReturn(petRecord1);

        Assertions.assertEquals(petRecord1, petServiceOut.addPet(petRecord1));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddPetWithNullPetRecord() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.addPet(null));
    }

    @Test
    void shouldReturnPetRecordWhenFindPetWithDog() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));

        when(modelMapperMock.mapToPetRecord(dog1)).thenReturn(petRecord1);

        Assertions.assertEquals(petRecord1, petServiceOut.findPet(dog1.getId(), DOG));
    }

    @Test
    void shouldReturnPetRecordWhenFindPetWithCAt() {

        when(catRepositoryMock.findById(cat1.getId())).thenReturn(Optional.of(cat1));

        when(modelMapperMock.mapToPetRecord(cat1)).thenReturn(petRecord1);

        Assertions.assertEquals(petRecord1, petServiceOut.findPet(pet1.getId(), CAT));
    }

   @Test
    void shouldReturnIllegalArgumentExceptionWhenFindPetWithWrongPetType() {

       Assertions.assertThrows(IllegalArgumentException.class,
               () -> petServiceOut.findPet(cat1.getId(), RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindPetWithWrongPetId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.findPet(-1L, DOG));
    }
    @Test
    void shouldReturnIllegalArgumentExceptionWhenEditPetWithNullPetRecord() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.editPet(null));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenEditPetWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.editPet(petRecord4));
    }


    @Test
    void shouldInvokeDeletePetWithDog() {

        petServiceOut.deletePet(dog1.getId(), DOG);
        verify(dogRepositoryMock, times(1)).deleteById(anyLong());
    }

   @Test
    void shouldInvokeDeletePetWithCat() {

        petServiceOut.deletePet(cat1.getId(), CAT);
        verify(catRepositoryMock, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeletePetWithWrongId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.deletePet(-1, DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeletePetWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.deletePet(cat1.getId(), RET));
    }

    @Test
    void shouldReturnPetRecordListWhenFindAllPetsWithDogType() {

        when(dogRepositoryMock.findAll()).thenReturn(dogs);
        when(modelMapperMock.mapToPetRecord(dog1)).thenReturn(petRecord1);
        when(modelMapperMock.mapToPetRecord(dog2)).thenReturn(petRecord2);
        when(modelMapperMock.mapToPetRecord(dog3)).thenReturn(petRecord3);

        Assertions.assertIterableEquals(petRecords, petServiceOut.findAllPets(DOG));
    }

       @Test
    void shouldReturnPetRecordListWhenFindAllPetsWithCAtType() {

        when(catRepositoryMock.findAll()).thenReturn(cats);
        when(modelMapperMock.mapToPetRecord(cat1)).thenReturn(petRecord1);
        when(modelMapperMock.mapToPetRecord(cat2)).thenReturn(petRecord2);
        when(modelMapperMock.mapToPetRecord(cat3)).thenReturn(petRecord3);

        Assertions.assertIterableEquals(petRecords, petServiceOut.findAllPets(CAT));
    }

    @Test
    void shouldReturnNewArrayListWhenFindAllPetsWithAbsenceDogRecords() {

        when(dogRepositoryMock.findAll()).thenReturn(List.of());
        Assertions.assertEquals(new ArrayList<>(),
                petServiceOut.findAllPets(DOG));
    }

     @Test
    void shouldReturnNewArrayListWhenFindAllPetsWithAbsenceCatRecords() {

        when(catRepositoryMock.findAll()).thenReturn(List.of());
        Assertions.assertEquals(new ArrayList<>(),
                petServiceOut.findAllPets(CAT));
    }

     @Test
    void shouldReturnIllegalArgumentExceptionWhenFindAllPetsWithWrongPetType() {

         Assertions.assertThrows(IllegalArgumentException.class,
                 () -> petServiceOut.findAllPets(RET));
    }


    @Test
    void shouldInvokeSaveDogWhenChangeIsTakenStatusWhenIsTakenTrue() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        petServiceOut.changeIsTakenStatus(dog1.getId(), DOG,true);
        verify(dogRepositoryMock, times(1)).save(dog1);
    }

    @Test
    void shouldInvokeSaveDogWhenChangeIsTakenStatusWhenIsTakenFalse() {

        when(dogRepositoryMock.findById(dog2.getId())).thenReturn(Optional.of(dog2));
        petServiceOut.changeIsTakenStatus(dog2.getId(), DOG,false);
        verify(dogRepositoryMock, times(1)).save(dog2);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeIsTakenStatusWhenIsTakenFalseAndOnProbationTrueOfDog() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeIsTakenStatus(dog1.getId(),DOG, false));
    }

    @Test
    void shouldInvokeSaveCatWhenChangeIsTakenStatusWhenIsTakenTrue() {

        when(catRepositoryMock.findById(cat1.getId())).thenReturn(Optional.of(cat1));
        petServiceOut.changeIsTakenStatus(cat1.getId(), CAT,true);
        verify(catRepositoryMock, times(1)).save(cat1);
    }

    @Test
    void shouldInvokeSaveCatWhenChangeIsTakenStatusWhenIsTakenFalse() {

        when(catRepositoryMock.findById(cat2.getId())).thenReturn(Optional.of(cat2));
        petServiceOut.changeIsTakenStatus(cat2.getId(), CAT,false);
        verify(catRepositoryMock, times(1)).save(cat2);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeIsTakenStatusWhenIsTakenFalseAndOnProbationTrueOfCat() {

        when(catRepositoryMock.findById(cat1.getId())).thenReturn(Optional.of(cat1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeIsTakenStatus(cat1.getId(),CAT, false));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenChangeIsTakenStatusWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeIsTakenStatus(cat1.getId(),RET, false));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenChangeIsTakenStatusWithWrongPetId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeIsTakenStatus(-1,CAT, false));
    }


    @Test
    void shouldInvokeSaveDogWhenChangeOnProbationStatusWhenIsTakenTrueAndOnProbationTrue() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        petServiceOut.changeOnProbationStatus(dog1.getId(), DOG,true);
        verify(dogRepositoryMock, times(1)).save(dog1);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeOnProbationStatusWhenIsTakenFalseAndOnProbationTrueOfDog() {

        when(dogRepositoryMock.findById(dog3.getId())).thenReturn(Optional.of(dog3));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeOnProbationStatus(dog3.getId(), DOG,true));
    }

    @Test
    void shouldInvokeSaveDogWhenChangeOnProbationStatusWhenOnProbationSetToFalse() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        petServiceOut.changeOnProbationStatus(dog1.getId(), DOG,false);
        verify(dogRepositoryMock, times(1)).save(dog1);
    }

    @Test
    void shouldInvokeSaveCatWhenChangeOnProbationStatusWhenIsTakenTrueAndOnProbationTrue() {

        when(catRepositoryMock.findById(cat1.getId())).thenReturn(Optional.of(cat1));
        petServiceOut.changeOnProbationStatus(cat1.getId(), CAT,true);
        verify(catRepositoryMock, times(1)).save(cat1);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeOnProbationStatusWhenIsTakenFalseAndOnProbationTrueOfCat() {

        when(dogRepositoryMock.findById(dog3.getId())).thenReturn(Optional.of(dog3));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeOnProbationStatus(dog3.getId(), DOG,true));
    }

    @Test
    void shouldInvokeSaveCatWhenChangeOnProbationStatusWhenOnProbationSetToFalse() {

        when(catRepositoryMock.findById(cat1.getId())).thenReturn(Optional.of(cat1));
        petServiceOut.changeOnProbationStatus(cat1.getId(), CAT,false);
        verify(catRepositoryMock, times(1)).save(cat1);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeOnProbationStatusWhenIsTakenFalseAndOnProbationTrueOfCAt() {

        when(catRepositoryMock.findById(cat3.getId())).thenReturn(Optional.of(cat3));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeOnProbationStatus(cat3.getId(), CAT,true));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenChangeOnProbationStatusWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeOnProbationStatus(cat1.getId(),RET, false));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenChangeOnProbationStatusWithWrongPetId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> petServiceOut.changeOnProbationStatus(-1,CAT, false));
    }
}