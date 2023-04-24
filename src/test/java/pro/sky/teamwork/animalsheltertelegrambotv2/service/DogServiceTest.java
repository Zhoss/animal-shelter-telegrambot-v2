package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.DogNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogServiceTest {

    @Mock
    DogRepository dogRepositoryMock;
    @Mock
    ModelMapper modelMapperMock;

    @InjectMocks
    DogService dogServiceOut;

    private long id1 = 1;
    private long id2 = 2;
    private long id3 = 3;
    private boolean taken1 = true;
    private boolean taken2 = false;
    private boolean onProbation1 = true;
    private boolean onProbation2 = false;

    private Dog dog1 = new Dog();
    private Dog dog2 = new Dog();
    private Dog dog3 = new Dog();

    private DogRecord dogRecord1 = new DogRecord();
    private DogRecord dogRecord2 = new DogRecord();
    private DogRecord dogRecord3 = new DogRecord();

    List<Dog> dogs = new ArrayList<>();
    List<DogRecord> dogRecords = new ArrayList<>();



    @BeforeEach
    void setUp() {

        dog1.setId(id1);
        dog2.setId(id2);
        dog3.setId(id3);
        dog1.setTaken(taken1);
        dog2.setTaken(taken1);
        dog3.setTaken(taken2);
        dog1.setOnProbation(onProbation1);
        dog2.setOnProbation(onProbation2);
        dog3.setOnProbation(onProbation2);

        dogRecord1.setId(dog1.getId());
        dogRecord2.setId(dog2.getId());
        dogRecord3.setId(dog3.getId());

        dogs.add(dog1);
        dogs.add(dog2);
        dogs.add(dog3);

        dogRecords.add(dogRecord1);
        dogRecords.add(dogRecord2);
        dogRecords.add(dogRecord3);
    }

    @Test
    void shouldReturnDogRecordWhenAddDog() {

        when(dogRepositoryMock.save(dog1)).thenReturn(dog1);
        when(modelMapperMock.mapToDogEntity(dogRecord1)).thenReturn(dog1);
        when(modelMapperMock.mapToDogRecord(dog1)).thenReturn(dogRecord1);

        Assertions.assertEquals(dogRecord1, dogServiceOut.addDog(dogRecord1));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddDogWithNullDogRecord() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.addDog(null));
    }

    @Test
    void shouldReturnDogRecordWhenFindDog() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        when(modelMapperMock.mapToDogRecord(dog1)).thenReturn(dogRecord1);

        Assertions.assertEquals(dogRecord1, dogServiceOut.findDog(dog1.getId()));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDogWithWrongDogId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.findDog(-1));
    }

    @Test
    void shouldReturnDogNotFoundExceptionWhenFindDogWithAbsentDog() {

        Assertions.assertThrows(DogNotFoundException.class,
                () -> dogServiceOut.findDog(4));
    }
    @Test
    void shouldReturnDogRecordWhenEditDog() {

        when(modelMapperMock.mapToDogEntity(dogRecord1)).thenReturn(dog1);
        when(dogRepositoryMock.save(dog1)).thenReturn(dog1);
        when(modelMapperMock.mapToDogRecord(dog1)).thenReturn(dogRecord1);

        Assertions.assertEquals(dogRecord1, dogServiceOut.editDog(dogRecord1));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenEditDogWithNullDogRecord() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.editDog(null));
    }

    @Test
    void shouldInvokeDeleteDog() {

        dogServiceOut.deleteDog(dog1.getId());
        verify(dogRepositoryMock, only()).deleteById(anyLong());
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteDogWithWrongId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.deleteDog(-1));
    }

    @Test
    void shouldReturnDogRecordListWhenFindAllDogs() {

        when(dogRepositoryMock.findAll()).thenReturn(dogs);
        when(modelMapperMock.mapToDogRecord(dog1)).thenReturn(dogRecord1);
        when(modelMapperMock.mapToDogRecord(dog2)).thenReturn(dogRecord2);
        when(modelMapperMock.mapToDogRecord(dog3)).thenReturn(dogRecord3);

        Assertions.assertIterableEquals(dogRecords, dogServiceOut.findAllDogs());
    }

    @Test
    void shouldReturnNewArrayListWhenFindAllDogsWithAbsenceDogRecords() {

        when(dogRepositoryMock.findAll()).thenReturn(List.of());
        Assertions.assertEquals(new ArrayList<>(),
                dogServiceOut.findAllDogs());
    }

    @Test
    void shouldInvokeSaveDogWhenChangeIsTakenStatusWhenIsTakenTrue() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        dogServiceOut.changeIsTakenStatus(dog1.getId(), true);
        verify(dogRepositoryMock,times(1)).save(dog1);
    }

    @Test
    void shouldInvokeSaveDogWhenChangeIsTakenStatusWhenIsTakenFalse() {

        when(dogRepositoryMock.findById(dog2.getId())).thenReturn(Optional.of(dog2));
        dogServiceOut.changeIsTakenStatus(dog2.getId(), false);
        verify(dogRepositoryMock,times(1)).save(dog2);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeIsTakenStatusWhenIsTakenFalseAndOnProbationTrue() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.changeIsTakenStatus(dog1.getId(), false));
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeIsTakenStatusWithWrongDogId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.changeIsTakenStatus(0, false));
    }

    @Test
    void shouldInvokeSaveDogWhenChangeOnProbationStatusWhenIsTakenTrueAndOnProbationTrue() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        dogServiceOut.changeOnProbationStatus(dog1.getId(), true);
        verify(dogRepositoryMock,times(1)).save(dog1);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeOnProbationStatusWhenIsTakenFalseAndOnProbationTrue() {

        when(dogRepositoryMock.findById(dog3.getId())).thenReturn(Optional.of(dog3));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.changeOnProbationStatus(dog3.getId(), true));
    }

    @Test
    void shouldInvokeSaveDogWhenChangeOnProbationStatusWhenOnProbationFalse() {

        when(dogRepositoryMock.findById(dog1.getId())).thenReturn(Optional.of(dog1));
        dogServiceOut.changeOnProbationStatus(dog1.getId(), false);
        verify(dogRepositoryMock,times(1)).save(dog1);
    }

    @Test
    void shouReturnIllegalArgumentExceptionWhenChangeOnProbationStatusWithWrongDogId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dogServiceOut.changeOnProbationStatus(0, false));
    }
}