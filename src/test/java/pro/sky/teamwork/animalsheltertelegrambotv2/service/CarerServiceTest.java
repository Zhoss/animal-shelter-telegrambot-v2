package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CarerNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.CarerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarerServiceTest {

    private final CarerRecord carerRecord1 = new CarerRecord();
    private final CarerRecord carerRecord2 = new CarerRecord();
    private final CarerRecord carerRecord3 = new CarerRecord();

    String fullName = "Иван Иванович Ивонов";
    int age = 21;
    String phoneNumber = "+7(111)1234567";
    long chatId = 111;


    private final Carer carer = new Carer();
    private final Carer carer2 = new Carer();
    private final Carer carer3 = new Carer();

    List<Carer> carers = List.of(carer, carer2, carer3);
    List<CarerRecord> carerRecords = List.of(carerRecord1, carerRecord2, carerRecord3);

    @Mock
    CarerRepository carerRepositoryMock;
    @Mock
    ModelMapper modelMapperMock;


    @InjectMocks
    CarerService carerServiceOut;

    @BeforeEach
    public void setUp() {

        carer.setFullName(fullName);
        carer.setBirthYear(LocalDate.now().getYear() - age);
        carer.setPhoneNumber(phoneNumber);
        carer.setChatId(chatId);
        carer.setId(111);
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

    }

    @Test
    void ShouldReturnCarerWhenAddCarer() {

        when(carerRepositoryMock.save(carer)).thenReturn(carer);
        carer.setId(0);

        assertEquals(carer, carerServiceOut.addCarer(fullName, age, phoneNumber, chatId));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenAddCarer() {

        assertThrows(IllegalArgumentException.class,
                ()->carerServiceOut.addCarer("", age, phoneNumber, chatId));
    }

    @Test
    void ShouldReturnCarerWhenSaveCarer() {

        when(carerRepositoryMock.save(carer)).thenReturn(carer);
        assertEquals(carer, carerServiceOut.saveCarer(carer));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenSaveCarer() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.saveCarer(null));
    }

    @Test
    void ShouldReturnCarerRecordWhenFindCarer() {

        when(carerRepositoryMock.findById(carer.getId())).thenReturn(Optional.of(carer));

        when(modelMapperMock.mapToCarerRecord(carer)).thenReturn(carerRecord1);

        assertEquals(carerRecord1,carerServiceOut.findCarer(carer.getId()));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindCarer() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarer(-1));
    }

    @Test
    void ShouldReturnCarerNotFoundExceptionWhenFindCarerWithWrongId() {

        when(carerRepositoryMock.findById(carer.getId())).thenReturn(Optional.empty());

        assertThrows(CarerNotFoundException.class,
                () -> carerServiceOut.findCarer(carer.getId()));
    }

    @Test
    void ShouldReturnCarerWhenFindCarerByChatId() {

        when(carerRepositoryMock.findCarerByChatId(carer.getChatId())).thenReturn(Optional.of(carer));

        assertEquals(carer,carerServiceOut.findCarerByChatId(carer.getChatId()));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindCarerByChatIdWithNullId() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarerByChatId(0));
    }

    @Test
    void editCarer() {

        when(carerRepositoryMock.findById(carer.getId())).thenReturn(Optional.of(carer));

        verify(modelMapperMock).updateCarer(carerRecord1, carer);

        when(modelMapperMock.mapToCarerRecord(carer)).thenReturn(carerRecord1);
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenEditCarer() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.editCarer(null));
    }

    @Test
    void deleteCarer() {

        verify(carerRepositoryMock).deleteById(any(long.class));
    }

    @Test
    void ShouldReturnCarerRecordWhenFindCarerByPhoneNumber() {

        when(carerRepositoryMock.findCarerByPhoneNumber(carer.getPhoneNumber()))
                .thenReturn(Optional.of(carer));

        when(modelMapperMock.mapToCarerRecord(carer))
                .thenReturn(carerRecord1);

        assertEquals(carerRecord1, carerServiceOut.findCarerByPhoneNumber(carer.getPhoneNumber()));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindCarerByPhoneNumberWithWrongPhoneNumber() {

        assertThrows(IllegalArgumentException.class,
                () -> carerServiceOut.findCarerByPhoneNumber("7(123)1234567"));
    }

    @Test
    void ShouldReturnCarerNotFoundExceptionWhenFindCarerByPhoneNumberWithNoSuchPhoneNumber() {

        when(carerRepositoryMock.findCarerByPhoneNumber(any(String.class)))
                .thenReturn(Optional.empty());
        assertThrows(CarerNotFoundException.class,
                () -> carerServiceOut.findCarerByPhoneNumber(carer.getPhoneNumber()));
    }

    @Test
    void ShouldReturnAllCarerRecordsWhenFindAllCarers() {

        when(carerRepositoryMock.findAll()).thenReturn(carers);

        when(modelMapperMock.mapToCarerRecord(carer)).thenReturn(carerRecord1);
        when(modelMapperMock.mapToCarerRecord(carer2)).thenReturn(carerRecord2);
        when(modelMapperMock.mapToCarerRecord(carer3)).thenReturn(carerRecord3);

        assertIterableEquals(carerRecords, carerServiceOut.findAllCarers());
    }

    @Test
    void ShouldReturnArrayListWhenFindAllCarersIsEmpty() {

        List<Carer> carers1 = List.of();

        when(carerRepositoryMock.findAll()).thenReturn(carers1);

        assertEquals(new ArrayList<>(), carerServiceOut.findAllCarers());
    }

//    @Test
//    void findCarerByDogId() {
//    }
}