package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.AgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementServiceTest {

    private final AgreementRecord agreementRecord = new AgreementRecord();
    private final AgreementRecord agreementRecord2 = new AgreementRecord();
    private final AgreementRecord agreementRecord3 = new AgreementRecord();

    private final Dog dog = new Dog();
    private final Dog dog2 = new Dog();
    private final Dog dog3 = new Dog();

    private final Carer carer = new Carer();
    private final Carer carer2 = new Carer();
    private final Carer carer3 = new Carer();

    private  final Agreement agreement = new Agreement();
    private  final Agreement agreement2 = new Agreement();
    private  final Agreement agreement3 = new Agreement();


    @Mock
    AgreementRepository agreementRepositoryMock;

    @Mock
    ModelMapper modelMapperMock;

    @Mock
    DogRepository dogRepositoryMock;

    @InjectMocks
    AgreementService agreementServiceOut;



    @BeforeEach
    public void setup() {
        agreementRecord.setId(101);
        agreementRecord2.setId(102);
        agreementRecord3.setId(103);
        agreementRecord.setNumber("111");
        agreementRecord2.setNumber("222");
        agreementRecord3.setNumber("333");
        agreementRecord.setConclusionDate(LocalDate.now());
        agreementRecord2.setConclusionDate(LocalDate.now());
        agreementRecord3.setConclusionDate(LocalDate.now());
        agreementRecord.setCarerId(1);
        agreementRecord2.setCarerId(2);
        agreementRecord3.setCarerId(3);

        dog.setId(1);
        dog2.setId(2);
        dog3.setId(3);
        dog.setTaken(true);
        dog2.setTaken(true);
        dog3.setTaken(true);
        dog.setName("Dog1");
        dog2.setName("Dog2");
        dog3.setName("Dog3");
        dog.setAge(11);
        dog2.setAge(12);
        dog3.setAge(13);
        dog.setBreed("No breed");
        dog2.setBreed("No breed");
        dog3.setBreed("No breed");
        dog.setCoatColor("black");
        dog2.setCoatColor("orange");
        dog3.setCoatColor("green");

        carer.setDog(dog);
        carer2.setDog(dog2);
        carer3.setDog(dog3);

        agreement.setId(agreementRecord.getId());
        agreement2.setId(agreementRecord2.getId());
        agreement3.setId(agreementRecord3.getId());
        agreement.setNumber(agreementRecord.getNumber());
        agreement2.setNumber(agreementRecord2.getNumber());
        agreement3.setNumber(agreementRecord3.getNumber());
        agreement.setConclusionDate(agreementRecord.getConclusionDate());
        agreement2.setConclusionDate(agreementRecord2.getConclusionDate());
        agreement3.setConclusionDate(agreementRecord3.getConclusionDate());
        agreement.setCarer(carer);
        agreement2.setCarer(carer2);
        agreement3.setCarer(carer3);
        agreement.setProbationEndData(agreementRecord.getConclusionDate().plusDays(30));
        agreement2.setProbationEndData(agreementRecord2.getConclusionDate().plusDays(30));
        agreement3.setProbationEndData(agreementRecord3.getConclusionDate().plusDays(30));

    }

    @Test
    void ShouldReturnAgreementRecordWhenAddAgreement() {

        when(modelMapperMock.mapToAgreementEntity(eq(agreementRecord))).thenReturn(agreement);

        when(agreementRepositoryMock.save(agreement)).thenReturn(agreement);

        when(modelMapperMock.mapToAgreementRecord(agreement)).thenReturn(agreementRecord);

        when(dogRepositoryMock.save(dog)).thenReturn(dog);

        Assertions.assertEquals(agreementRecord,agreementServiceOut.addAgreement(agreementRecord));
    }

    @Test
    void ShouldReturnNullPointerExceptionWhenAddAgreement() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.addAgreement(null));
    }

    @Test
    void ShouldReturnAgreementRecordWhenFindAgreementById() {

        when(agreementRepositoryMock.findById(100L)).thenReturn(Optional.of(agreement));

        when(modelMapperMock.mapToAgreementRecord(agreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord,agreementServiceOut.findAgreementById(100));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindAgreementById() {
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->agreementServiceOut.findAgreementById(0));
    }
   @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindAgreementById2() {
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->agreementServiceOut.findAgreementById(-1));
    }

    @Test
    void ShouldReturnAgreementRecordWhenEditAgreement() {
        when(modelMapperMock.mapToAgreementEntity(eq(agreementRecord))).thenReturn(agreement);

        when(agreementRepositoryMock.save(agreement)).thenReturn(agreement);

        when(modelMapperMock.mapToAgreementRecord(agreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord,agreementServiceOut.editAgreement(agreementRecord));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenEditAgreement() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.editAgreement(null));
    }

    @Test
    @Disabled
    void deleteAgreement() {
//        when(agreementRepositoryMock.existsById(any(long.class))).thenReturn(true);
        Mockito.verify(agreementRepositoryMock).deleteById(agreement.getId());
    }

    @Test
    void ShouldReturnAllAgreementRecordsWhenFindAllAgreements() {
        List<Agreement> agreements = List.of(agreement, agreement2, agreement3);
        when(agreementRepositoryMock.findAll()).thenReturn(agreements);
        when(modelMapperMock.mapToAgreementRecord(agreement)).thenReturn(agreementRecord);
        when(modelMapperMock.mapToAgreementRecord(agreement2)).thenReturn(agreementRecord2);
        when(modelMapperMock.mapToAgreementRecord(agreement3)).thenReturn(agreementRecord3);
        Assertions.assertIterableEquals(List.of(agreementRecord, agreementRecord2, agreementRecord3),
                agreementServiceOut.findAllAgreements());
    }

    @Test
    void ShouldReturnNewArrayListWhenFindAllAgreements() {
        when(agreementRepositoryMock.findAll()).thenReturn(List.of());
        Assertions.assertIterableEquals(new ArrayList<>(), agreementServiceOut.findAllAgreements());

    }

    @Test
    void ShouldReturnAllAgreementsOnThisDateWhenFindAllAgreementsWithProbationByDate() {
        when(agreementRepositoryMock.findAll())
                .thenReturn(List.of(agreement, agreement2, agreement3));
        Assertions.assertIterableEquals(List.of(agreement, agreement2, agreement3),
                agreementServiceOut.findAllAgreementsWithProbationByDate(LocalDate.now()));
    }

    @Test
    void ShouldReturnNewArrayListOnDateWhenFindAllAgreementsWithProbationByDate() {
        when(agreementRepositoryMock.findAll())
                .thenReturn(List.of(agreement, agreement2, agreement3));
        Assertions.assertIterableEquals(new ArrayList<>(),
                agreementServiceOut.findAllAgreementsWithProbationByDate(
                        LocalDate.now().plusDays(31)));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindAllAgreementsWithNoDate() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.findAllAgreementsWithProbationByDate(null));
    }

    @Test
    void findAgreements() {
        when(agreementRepositoryMock.findAll())
                .thenReturn(List.of(agreement, agreement2, agreement3));
        Assertions.assertIterableEquals(List.of(agreement, agreement2, agreement3),
                agreementServiceOut.findAgreements());
    }
}