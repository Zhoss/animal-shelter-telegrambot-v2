package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatDailyReportRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogDailyReportRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyReportServiceTest {

    @Mock
    public DogDailyReportRepository dogDailyReportRepositoryMock;

    @Mock
    public CatDailyReportRepository catDailyReportRepositoryMock;

    @Mock
    private ModelMapper modelMapperMock;

    @InjectMocks
    private DailyReportService dailyReportServiceOut;

    private final DailyReportRecord dailyReportRecord1 = new DailyReportRecord();
    private final DailyReportRecord dailyReportRecord2 = new DailyReportRecord();
    private final DailyReportRecord dailyReportRecord3 = new DailyReportRecord();

    private long id1 = 1;
    private long id2 = 2;
    private long id3 = 3;

    private final LocalDate reportDate1 = LocalDate.now().minusDays(1);
    private final LocalDate reportDate2 = LocalDate.now().minusDays(2);
    private final LocalDate reportDate3 = LocalDate.now().minusDays(3);

    private final Carer carer1 = new Carer();
    private final Carer carer2 = new Carer();
    private final Carer carer3 = new Carer();

    private final CatDailyReport catDailyReport1 = new CatDailyReport();
    private final CatDailyReport catDailyReport2 = new CatDailyReport();
    private final CatDailyReport catDailyReport3 = new CatDailyReport();
    private final DogDailyReport dogDailyReport1 = new DogDailyReport();
    private final DogDailyReport dogDailyReport2 = new DogDailyReport();
    private final DogDailyReport dogDailyReport3 = new DogDailyReport();

    private final List<DailyReportRecord> dailyReportRecordList = new ArrayList<>();

    private final List<CatDailyReport> catDailyReportList = new ArrayList<>();
    private final List<DogDailyReport> dogDailyReportList = new ArrayList<>();


    @BeforeEach
    void setUp() {

        dailyReportRecord1.setId(id1);
        dailyReportRecord2.setId(id2);
        dailyReportRecord3.setId(id3);

        dailyReportRecord1.setReportDate(reportDate1);
        dailyReportRecord2.setReportDate(reportDate2);
        dailyReportRecord3.setReportDate(reportDate3);

        carer1.setId(11);
        carer2.setId(22);
        carer3.setId(33);

        dailyReportRecord1.setCarerId(carer1.getId());
        dailyReportRecord2.setCarerId(carer2.getId());
        dailyReportRecord3.setCarerId(carer3.getId());

        dailyReportRecordList.add(dailyReportRecord1);
        dailyReportRecordList.add(dailyReportRecord2);
        dailyReportRecordList.add(dailyReportRecord3);

        catDailyReport1.setId(id1);
        catDailyReport2.setId(id2);
        catDailyReport3.setId(id3);

        catDailyReport1.setCarer(carer1);
        catDailyReport2.setCarer(carer2);
        catDailyReport3.setCarer(carer3);

        catDailyReport1.setReportDate(LocalDate.now().minusDays(1));
        catDailyReport2.setReportDate(LocalDate.now().minusDays(2));
        catDailyReport3.setReportDate(LocalDate.now().minusDays(3));

        dogDailyReport1.setId(id1);
        dogDailyReport2.setId(id2);
        dogDailyReport3.setId(id3);

        dogDailyReport1.setCarer(carer1);
        dogDailyReport2.setCarer(carer2);
        dogDailyReport3.setCarer(carer3);

        dogDailyReport1.setReportDate(LocalDate.now().minusDays(1));
        dogDailyReport2.setReportDate(LocalDate.now().minusDays(2));
        dogDailyReport3.setReportDate(LocalDate.now().minusDays(3));

        catDailyReportList.add(catDailyReport1);
        catDailyReportList.add(catDailyReport2);
        catDailyReportList.add(catDailyReport3);

        dogDailyReportList.add(dogDailyReport1);
        dogDailyReportList.add(dogDailyReport2);
        dogDailyReportList.add(dogDailyReport3);

    }

    @Test
    void shouldReturnDailyReportRecordListWhenFindDailyReportsByCarerOfCat() {

        when(catDailyReportRepositoryMock.findCatDailyReportByCatCarerId(carer1.getId()))
                .thenReturn(catDailyReportList);

        when(modelMapperMock.mapToDailyRecordRecord(catDailyReport1)).thenReturn(dailyReportRecord1);
        when(modelMapperMock.mapToDailyRecordRecord(catDailyReport2)).thenReturn(dailyReportRecord2);
        when(modelMapperMock.mapToDailyRecordRecord(catDailyReport3)).thenReturn(dailyReportRecord3);

        Assertions.assertIterableEquals(dailyReportRecordList,
                dailyReportServiceOut.findDailyReportsByCarer(carer1.getId(), PetType.CAT));
    }

    @Test
    void shouldReturnNewArrayListWhenFindDailyReportsByCarerWithEmptyDailyReportListOfCat() {

        when(catDailyReportRepositoryMock.findCatDailyReportByCatCarerId(any(long.class)))
                .thenReturn(List.of());

        Assertions.assertEquals(new ArrayList<>(),
                dailyReportServiceOut.findDailyReportsByCarer(carer1.getId(), PetType.CAT));
    }

    @Test
    void shouldReturnDailyReportRecordListWhenFindDailyReportsByCarerOfDog() {

        when(dogDailyReportRepositoryMock.findDogDailyReportByDogCarerId(carer1.getId()))
                .thenReturn(dogDailyReportList);

        when(modelMapperMock.mapToDailyRecordRecord(dogDailyReport1)).thenReturn(dailyReportRecord1);
        when(modelMapperMock.mapToDailyRecordRecord(dogDailyReport2)).thenReturn(dailyReportRecord2);
        when(modelMapperMock.mapToDailyRecordRecord(dogDailyReport3)).thenReturn(dailyReportRecord3);

        Assertions.assertIterableEquals(dailyReportRecordList,
                dailyReportServiceOut.findDailyReportsByCarer(carer1.getId(), PetType.DOG));
    }

    @Test
    void shouldReturnNewArrayListWhenFindDailyReportsByCarerWithEmptyDailyReportListOfDog() {

        when(dogDailyReportRepositoryMock.findDogDailyReportByDogCarerId(any(long.class)))
                .thenReturn(List.of());

        Assertions.assertEquals(new ArrayList<>(),
                dailyReportServiceOut.findDailyReportsByCarer(carer1.getId(), PetType.DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportsByCarerWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportsByCarer(carer1.getId(), PetType.RET));
    }



    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportsByCarerWithWrongCarerId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportsByCarer(0L, PetType.CAT));
    }

    @Test
    void shouldReturnDailyReportRecordWhenFindDailyReportByCarerAndDateOfCat() {

        when(catDailyReportRepositoryMock.findCatDailyReportByCatCarerIdAndReportDate(carer1.getId(),
                catDailyReport1.getReportDate())).thenReturn(catDailyReport1);
        when(modelMapperMock.mapToDailyRecordRecord(catDailyReport1)).thenReturn(dailyReportRecord1);

        Assertions.assertEquals(dailyReportRecord1,
                dailyReportServiceOut.findDailyReportByCarerAndDate(carer1.getId(),
                        catDailyReport1.getReportDate(), PetType.CAT));
    }

    @Test
    void shouldReturnDailyReportRecordWhenFindDailyReportByCarerAndDateOfDog() {

        when(dogDailyReportRepositoryMock.findDogDailyReportByDogCarerIdAndReportDate(carer1.getId(),
                dogDailyReport1.getReportDate())).thenReturn(dogDailyReport1);
        when(modelMapperMock.mapToDailyRecordRecord(dogDailyReport1)).thenReturn(dailyReportRecord1);

        Assertions.assertEquals(dailyReportRecord1,
                dailyReportServiceOut.findDailyReportByCarerAndDate(carer1.getId(),
                        dogDailyReport1.getReportDate(), PetType.DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerAndDateWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerAndDate(carer1.getId(),
                        dogDailyReport1.getReportDate(), PetType.RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerAndDateWithWrongCarerTd() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerAndDate(-1L,
                        dogDailyReport1.getReportDate(), PetType.RET));
    }

    @Test
    void shouldReturnDailyReportWhenFindDailyReportByCarerIdAndDateOfCat() {

        when(catDailyReportRepositoryMock.findCatDailyReportByCatCarerIdAndReportDate(carer1.getId(),
                catDailyReport1.getReportDate())).thenReturn(catDailyReport1);

        Assertions.assertEquals(catDailyReport1,
                dailyReportServiceOut.findDailyReportByCarerIdAndDate(carer1.getId(),
                        catDailyReport1.getReportDate(), PetType.CAT));
    }

    @Test
    void shouldReturnDailyReportWhenFindDailyReportByCarerIdAndDateOfDog() {

        when(dogDailyReportRepositoryMock.findDogDailyReportByDogCarerIdAndReportDate(carer1.getId(),
                dogDailyReport1.getReportDate())).thenReturn(dogDailyReport1);

        Assertions.assertEquals(dogDailyReport1,
                dailyReportServiceOut.findDailyReportByCarerIdAndDate(carer1.getId(),
                        dogDailyReport1.getReportDate(), PetType.DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerIdAndDateWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerIdAndDate(carer1.getId(),
                        dogDailyReport1.getReportDate(), PetType.RET));
    }


    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerIdAndDateWithWrongCarerId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerIdAndDate(0L,
                        dogDailyReport1.getReportDate(), PetType.DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerIdAndDateWithNullDate() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerIdAndDate(carer1.getId(),
                        null, PetType.DOG));
    }

    @Test
    void shouldInvokeAddDailyReportOfCat() {

        dailyReportServiceOut.addDailyReport(catDailyReport1, PetType.CAT);

        verify(catDailyReportRepositoryMock).save(catDailyReport1);
    }

    @Test
    void shouldInvokeAddDailyReportOfDog() {

        dailyReportServiceOut.addDailyReport(dogDailyReport1, PetType.DOG);

        verify(dogDailyReportRepositoryMock).save(dogDailyReport1);
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddDailyReportWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.addDailyReport(dogDailyReport1, PetType.RET));
    }


    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddDailyReportWithNullDailyReport() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.addDailyReport(null, PetType.DOG));
    }

    @Test
    void shouldInvokeDeleteDailyReportOfCat() {

        dailyReportServiceOut.deleteDailyReport(catDailyReport1.getId(), PetType.CAT);

        verify(catDailyReportRepositoryMock).deleteById(any(long.class));
    }

    @Test
    void shouldInvokeDeleteDailyReportOfDog() {

        dailyReportServiceOut.deleteDailyReport(dogDailyReport1.getId(), PetType.DOG);

        verify(dogDailyReportRepositoryMock).deleteById(any(long.class));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteDailyReportWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.deleteDailyReport(dogDailyReport1.getId(), PetType.RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteDailyReportWithNullDailyReportId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.deleteDailyReport(0L, PetType.DOG));
    }

    @Test
    void shouldReturnListDailyReportRecordWhenFindDailyReportsByDateOfCat() {

        catDailyReport1.setReportDate(LocalDate.now());
        catDailyReport2.setReportDate(LocalDate.now());
        catDailyReport3.setReportDate(LocalDate.now());

        when(catDailyReportRepositoryMock.findCatDailyReportsByReportDate(LocalDate.now()))
                .thenReturn(catDailyReportList);
        when(modelMapperMock.mapToDailyRecordRecord(catDailyReport1)).thenReturn(dailyReportRecord1);
        when(modelMapperMock.mapToDailyRecordRecord(catDailyReport2)).thenReturn(dailyReportRecord2);
        when(modelMapperMock.mapToDailyRecordRecord(catDailyReport3)).thenReturn(dailyReportRecord3);

        Assertions.assertIterableEquals(dailyReportRecordList,
                dailyReportServiceOut.findDailyReportsByDate(LocalDate.now(), PetType.CAT));

    }
    @Test
    void shouldReturnListDailyReportRecordWhenFindDailyReportsByDateOfDog() {

        dogDailyReport1.setReportDate(LocalDate.now());
        dogDailyReport2.setReportDate(LocalDate.now());
        dogDailyReport3.setReportDate(LocalDate.now());

        when(dogDailyReportRepositoryMock.findDogDailyReportsByReportDate(LocalDate.now()))
                .thenReturn(dogDailyReportList);
        when(modelMapperMock.mapToDailyRecordRecord(dogDailyReport1)).thenReturn(dailyReportRecord1);
        when(modelMapperMock.mapToDailyRecordRecord(dogDailyReport2)).thenReturn(dailyReportRecord2);
        when(modelMapperMock.mapToDailyRecordRecord(dogDailyReport3)).thenReturn(dailyReportRecord3);

        Assertions.assertIterableEquals(dailyReportRecordList,
                dailyReportServiceOut.findDailyReportsByDate(LocalDate.now(), PetType.DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportsByDateWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportsByDate(LocalDate.now(), PetType.RET));
    }
}