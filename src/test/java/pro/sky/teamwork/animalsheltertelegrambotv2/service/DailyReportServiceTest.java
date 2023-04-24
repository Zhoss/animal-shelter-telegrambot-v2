package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyReportServiceTest {

    @Mock
    public DailyReportRepository dailyReportRepositoryMock;

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

    private final DailyReport dailyReport1 = new DailyReport();
    private final DailyReport dailyReport2 = new DailyReport();
    private final DailyReport dailyReport3 = new DailyReport();

    private final List<DailyReportRecord> dailyReportRecordList = new ArrayList<>();

    private final List<DailyReport> dailyReportList = new ArrayList<>();


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

        dailyReport1.setId(id1);
        dailyReport2.setId(id2);
        dailyReport3.setId(id3);

        dailyReport1.setCarer(carer1);
        dailyReport2.setCarer(carer2);
        dailyReport3.setCarer(carer3);

        dailyReport1.setReportDate(LocalDate.now().minusDays(1));
        dailyReport2.setReportDate(LocalDate.now().minusDays(2));
        dailyReport3.setReportDate(LocalDate.now().minusDays(3));

        dailyReportList.add(dailyReport1);
        dailyReportList.add(dailyReport2);
        dailyReportList.add(dailyReport3);

        carer1.setDailyReports(dailyReportList);
    }

    @Test
    void shouldReturnDailyReportRecordListWhenFindDailyReportsByCarer() {

        when(dailyReportRepositoryMock.findDailyReportByCarerId(carer1.getId()))
                .thenReturn(dailyReportList);

        when(modelMapperMock.mapToDailyRecordRecord(dailyReport1)).thenReturn(dailyReportRecord1);
        when(modelMapperMock.mapToDailyRecordRecord(dailyReport2)).thenReturn(dailyReportRecord2);
        when(modelMapperMock.mapToDailyRecordRecord(dailyReport3)).thenReturn(dailyReportRecord3);

        Assertions.assertIterableEquals(dailyReportRecordList,
                dailyReportServiceOut.findDailyReportsByCarer(carer1.getId()));
    }

    @Test
    void shouldReturnNewArrayListWhenFindDailyReportsByCarerWithEmptyDailyReportList() {

        when(dailyReportRepositoryMock.findDailyReportByCarerId(any(long.class)))
                .thenReturn(List.of());

        Assertions.assertEquals(new ArrayList<>(),
                dailyReportServiceOut.findDailyReportsByCarer(carer1.getId()));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportsByCarerWithWrongCarerId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                ()->dailyReportServiceOut.findDailyReportsByCarer(0L));
    }

    @Test
    void shouldReturnDailyReportRecordWhenFindDailyReportByCarerAndDate() {

        when(modelMapperMock.mapToDailyRecordRecord(dailyReport1)).thenReturn(dailyReportRecord1);
        when(dailyReportRepositoryMock.findDailyReportByCarerIdAndReportDate(carer1.getId(),
                dailyReport1.getReportDate())).thenReturn(dailyReport1);

        Assertions.assertEquals(dailyReportRecord1,
                dailyReportServiceOut.findDailyReportByCarerAndDate(carer1.getId(),
                        dailyReport1.getReportDate()));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerAndDateWithWrongCarerTd() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerAndDate(-1L,
                        dailyReport1.getReportDate()));
    }

      @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerAndDateWithNullDate() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerAndDate(carer1.getId(),
                        null));
    }

    @Test
    void shouldReturnDailyReportWhenFindDailyReportByCarerIdAndDate() {

        when(dailyReportRepositoryMock.findDailyReportByCarerIdAndReportDate(carer1.getId(),
                dailyReport1.getReportDate())).thenReturn(dailyReport1);

        Assertions.assertEquals(dailyReport1,
                dailyReportServiceOut.findDailyReportByCarerIdAndDate(carer1.getId(),
                        dailyReport1.getReportDate()));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerIdAndDateWithWrongCarerId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerIdAndDate(0L,
                        dailyReport1.getReportDate()));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindDailyReportByCarerIdAndDateWithNullDate() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.findDailyReportByCarerIdAndDate(carer1.getId(),
                        null));
    }

    @Test
    void shouldInvokeAddDailyReport() {

        dailyReportServiceOut.addDailyReport(dailyReport1);

        verify(dailyReportRepositoryMock).save(dailyReport1);
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddDailyReportWithNullDailyReport() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.addDailyReport(null));
    }

    @Test
    void shouldInvokeDeleteDailyReport() {

        dailyReportServiceOut.deleteDailyReport(dailyReport1.getId());

        verify(dailyReportRepositoryMock).deleteById(any(long.class));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteDailyReportWithNullDailyReportId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> dailyReportServiceOut.deleteDailyReport(0L));
    }

    @Test
    void shouldReturnListDailyReportRecordWhenFindDailyReportsByDate() {

        when(dailyReportRepositoryMock.findDailyReportsByReportDate(dailyReport1.getReportDate()))
                .thenReturn(dailyReportList);
        when(modelMapperMock.mapToDailyRecordRecord(dailyReport1)).thenReturn(dailyReportRecord1);
        when(modelMapperMock.mapToDailyRecordRecord(dailyReport2)).thenReturn(dailyReportRecord2);
        when(modelMapperMock.mapToDailyRecordRecord(dailyReport3)).thenReturn(dailyReportRecord3);

        Assertions.assertIterableEquals(dailyReportRecordList,
                dailyReportServiceOut.findDailyReportsByDate(dailyReport1.getReportDate()));
    }
}