package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType.*;

@ExtendWith(MockitoExtension.class)
class CarerServiceTest {

    private final CarerRecord carerRecord1 = new CarerRecord();
    private final CarerRecord carerRecord2 = new CarerRecord();
    private final CarerRecord carerRecord3 = new CarerRecord();
    private final CarerRecord carerRecord4 = new CarerRecord();

    String fullName = "Иван Иванович Ивонов";
    int age = 21;
    String phoneNumber = "+7(111)1234567";
    long chatId = 111;


    private final Carer carer = new Carer();
    private final Carer carer2 = new Carer();
    private final Carer carer3 = new Carer();
    private final CatCarer catCarer = new CatCarer();
    private final CatCarer catCarer2 = new CatCarer();
    private final CatCarer catCarer3 = new CatCarer();
    private final CatCarer catCarer4 = new CatCarer();
    private final DogCarer dogCarer = new DogCarer();
    private final DogCarer dogCarer2 = new DogCarer();
    private final DogCarer dogCarer3 = new DogCarer();
    private final DogCarer dogCarer4 = new DogCarer();

    List<CatCarer> catCarers = List.of(catCarer, catCarer2, catCarer3);
    List<DogCarer> dogCarers = List.of(dogCarer, dogCarer2, dogCarer3);
    List<CarerRecord> carerRecords = List.of(carerRecord1, carerRecord2, carerRecord3);

    @Mock
    DogCarerRepository dogCarerRepositoryMock;

    @Mock
    CatCarerRepository catCarerRepositoryMock;

    @Mock
    ModelMapper modelMapperMock;
    @Mock
    CarerRecord carerRecordMock;


    @InjectMocks
    CarerService carerServiceOut;

    @BeforeEach
    public void setUp() {

        carer.setFullName(fullName);
        carer.setBirthYear(LocalDate.now().getYear() - age);
        carer.setPhoneNumber(phoneNumber);
        carer.setChatId(chatId);
        carer.setId(111);
//        carer.set
        carer2.setId(222);
        carer3.setId(333);

        carer2.setFullName("Петров Пётр Петрович");
        carer2.setBirthYear(LocalDate.now().getYear() - 22);
        carer2.setPhoneNumber("+7(222)1234567");
        carer2.setChatId(222);

        carer3.setFullName("Сидоров Сидор Сидорович");
        carer3.setBirthYear(LocalDate.now().getYear() - 23);
        carer3.setPhoneNumber("+7(333)1234567");
        carer3.setChatId(333);

        carerRecord1.setId(carer.getId());
        carerRecord2.setId(carer2.getId());
        carerRecord3.setId(carer3.getId());

        catCarer.setId(11);
        catCarer2.setId(12);
        catCarer3.setId(13);
        catCarer4.setId(0);
        dogCarer.setId(22);
        dogCarer2.setId(23);
        dogCarer3.setId(24);
        dogCarer4.setId(0);

        catCarer.setChatId(chatId);
        catCarer.setPhoneNumber(phoneNumber);
        dogCarer.setChatId(chatId);
        dogCarer.setPhoneNumber(phoneNumber);

        carerRecord1.setPetType(CAT);
        carerRecord2.setPetType(DOG);
        carerRecord4.setPetType(RET);//неверный тип животного для теста

    }

    @Test
    void shouldReturnCatCarerWhenAddCarer() {

        when(catCarerRepositoryMock.save(catCarer4)).thenReturn(catCarer4);
        assertEquals(catCarer4, carerServiceOut.addCarer(fullName, age, phoneNumber, chatId, CAT));
    }

    @Test
    void shouldReturnDogCarerWhenAddCarer() {

        when(dogCarerRepositoryMock.save(dogCarer4)).thenReturn(dogCarer4);
        assertEquals(dogCarer4, carerServiceOut.addCarer(fullName, age, phoneNumber, chatId, DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddCarerWithWrongPetType() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.addCarer(fullName, age, phoneNumber, chatId, RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddCarerWithWrongData() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.addCarer(" ", age, phoneNumber, chatId, CAT));
    }

    @Test
    void shouldReturnCarerWhenSaveCarerOfCat() {

        when(catCarerRepositoryMock.save(catCarer)).thenReturn(catCarer);
        assertEquals(catCarer, carerServiceOut.saveCarer(catCarer, CAT));
    }

    @Test
    void shouldReturnCarerWhenSaveCarerOfDog() {

        when(dogCarerRepositoryMock.save(dogCarer)).thenReturn(dogCarer);
        assertEquals(dogCarer, carerServiceOut.saveCarer(dogCarer, DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenSaveCarerWithWrongPetType() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.saveCarer(carer, RET));
    }


    @Test
    void shouldReturnIllegalArgumentExceptionWhenSaveCarerWithNull() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.saveCarer(null, CAT));
    }

    @Test
    void shouldReturnCarerRecordWhenFindCarerWithCat() {

        when(catCarerRepositoryMock.findById(catCarer.getId())).thenReturn(Optional.of(catCarer));
        when(modelMapperMock.mapToCarerRecord(catCarer)).thenReturn(carerRecord1);

        assertEquals(carerRecord1, carerServiceOut.findCarer(catCarer.getId(), CAT));
    }

    @Test
    void shouldReturnCarerRecordWhenFindCarerWithDog() {

        when(dogCarerRepositoryMock.findById(dogCarer.getId())).thenReturn(Optional.of(dogCarer));
        when(modelMapperMock.mapToCarerRecord(dogCarer)).thenReturn(carerRecord1);

        assertEquals(carerRecord1, carerServiceOut.findCarer(dogCarer.getId(), DOG));
    }


    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindCarerWithWrongPetType() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarer(dogCarer.getId(), RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindCarerWithWrongId() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarer(-1, CAT));
    }

    @Test
    void shouldReturnCarerWhenFindCarerByChatIdWithCAt() {

        when(catCarerRepositoryMock.findCatCarerByChatId(catCarer.getChatId()))
                .thenReturn(Optional.of(catCarer));

        assertEquals(catCarer, carerServiceOut.findCarerByChatId(catCarer.getChatId(), CAT));
    }

    @Test
    void shouldReturnCarerWhenFindCarerByChatIdWithDog() {

        when(dogCarerRepositoryMock.findDogCarerByChatId(dogCarer.getChatId()))
                .thenReturn(Optional.of(dogCarer));

        assertEquals(dogCarer, carerServiceOut.findCarerByChatId(dogCarer.getChatId(), DOG));
    }

    @Test
    void shouldReturnNullWhenFindCarerByChatIdWithWrongPetType() {

        assertNull(carerServiceOut.findCarerByChatId(dogCarer.getChatId(), RET));
    }


    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindCarerByChatIdWithNullId() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarerByChatId(0, DOG));
    }

    @Test
    void shouldReturnCarerRecordWhenEditCarer() {

        when(catCarerRepositoryMock.findById(carerRecord1.getId()))
                .thenReturn(Optional.of(catCarer));

        modelMapperMock.updateCarer(carerRecord1, catCarer);

        when(catCarerRepositoryMock.save(catCarer)).thenReturn(catCarer);

        when(modelMapperMock.mapToCarerRecord(catCarer)).thenReturn(carerRecord1);

        assertEquals(carerRecord1, carerServiceOut.editCarer(carerRecord1));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenEditCarerWithNullCarerRecord() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.editCarer(null));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenEditCarerWithWrongPetType() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.editCarer(carerRecord4));
    }

    @Test
    void shouldInvokeDeleteCarerOfCat() {

        carerServiceOut.deleteCarer(catCarer.getId(), CAT);
        verify(catCarerRepositoryMock).deleteById(catCarer.getId());
    }

    @Test
    void shouldInvokeDeleteCarerOfDog() {

        carerServiceOut.deleteCarer(dogCarer.getId(), DOG);
        verify(dogCarerRepositoryMock).deleteById(dogCarer.getId());
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteCarerOfWrongPetType() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.deleteCarer(carer.getId(), RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteCarerWithWrongId() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.deleteCarer(0, CAT));
    }

    @Test
    void ShouldReturnCarerRecordWhenFindCarerByPhoneNumberOfCat() {

        when(catCarerRepositoryMock.findCatCarerByPhoneNumber(catCarer.getPhoneNumber()))
                .thenReturn(Optional.of(catCarer));

        when(modelMapperMock.mapToCarerRecord(catCarer))
                .thenReturn(carerRecord1);

        assertEquals(carerRecord1, carerServiceOut
                .findCarerByPhoneNumber(catCarer.getPhoneNumber(), CAT));
    }

    @Test
    void ShouldReturnCarerRecordWhenFindCarerByPhoneNumberOfDog() {

        when(dogCarerRepositoryMock.findDogCarerByPhoneNumber(dogCarer.getPhoneNumber()))
                .thenReturn(Optional.of(dogCarer));

        when(modelMapperMock.mapToCarerRecord(dogCarer))
                .thenReturn(carerRecord1);

        assertEquals(carerRecord1, carerServiceOut
                .findCarerByPhoneNumber(dogCarer.getPhoneNumber(), DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindCarerByPhoneNumberWithWrongPetType() {
        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarerByPhoneNumber(dogCarer.getPhoneNumber(), RET));
    }


    @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindCarerByPhoneNumberWithWrongPhoneNumber() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarerByPhoneNumber("7(123)1234567", DOG));
    }

    @Test
    void ShouldReturnAllCarerRecordsWhenFindAllCarersOfCat() {

        when(catCarerRepositoryMock.findAll()).thenReturn(catCarers);

        when(modelMapperMock.mapToCarerRecord(catCarer)).thenReturn(carerRecord1);
        when(modelMapperMock.mapToCarerRecord(catCarer2)).thenReturn(carerRecord2);
        when(modelMapperMock.mapToCarerRecord(catCarer3)).thenReturn(carerRecord3);

        assertIterableEquals(carerRecords, carerServiceOut.findAllCarers(CAT));
    }

    @Test
    void ShouldReturnNewArrayListWhenFindAllCarersOfCatIsEmpty() {

        List<CatCarer> carers1 = List.of();

        when(catCarerRepositoryMock.findAll()).thenReturn(carers1);

        assertEquals(new ArrayList<>(), carerServiceOut.findAllCarers(CAT));
    }
   @Test
    void ShouldReturnAllCarerRecordsWhenFindAllCarersOfDog() {

        when(dogCarerRepositoryMock.findAll()).thenReturn(dogCarers);

        when(modelMapperMock.mapToCarerRecord(dogCarer)).thenReturn(carerRecord1);
        when(modelMapperMock.mapToCarerRecord(dogCarer2)).thenReturn(carerRecord2);
        when(modelMapperMock.mapToCarerRecord(dogCarer3)).thenReturn(carerRecord3);

        assertIterableEquals(carerRecords, carerServiceOut.findAllCarers(DOG));
    }

    @Test
    void ShouldReturnNewArrayListWhenFindAllCarersOfDogIsEmpty() {

        List<DogCarer> carers1 = List.of();

        when(dogCarerRepositoryMock.findAll()).thenReturn(carers1);

        assertEquals(new ArrayList<>(), carerServiceOut.findAllCarers(DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindAllCarersWithWrongPetType() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findAllCarers(RET));
    }
}