package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.Cat;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatAgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogAgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType.*;

@ExtendWith(MockitoExtension.class)
class AgreementServiceTest {

    private final AgreementRecord agreementRecord = new AgreementRecord();
    private final AgreementRecord agreementRecord2 = new AgreementRecord();
    private final AgreementRecord agreementRecord3 = new AgreementRecord();
    private final AgreementRecord agreementRecord4 = new AgreementRecord();

    private final Dog dog = new Dog();
    private final Cat cat = new Cat();
    private final Dog dog2 = new Dog();
    private final Dog dog3 = new Dog();



    private final Agreement agreement = new Agreement();
    private final CatAgreement catAgreement = new CatAgreement();
    private final CatAgreement catAgreement2 = new CatAgreement();
    private final CatAgreement catAgreement3 = new CatAgreement();
    private final DogAgreement dogAgreement = new DogAgreement();
    private final DogAgreement dogAgreement2 = new DogAgreement();
    private final DogAgreement dogAgreement3 = new DogAgreement();
    private final Agreement agreement2 = new Agreement();
    private final Agreement agreement3 = new Agreement();


    @Mock
    CatAgreementRepository catAgreementRepositoryMock;

    @Mock
    DogAgreementRepository dogAgreementRepositoryMock;

    @Mock
    ModelMapper modelMapperMock;


    @Mock
    CatRepository catRepositoryMock;

    @InjectMocks
    AgreementService agreementServiceOut;

    @AfterEach



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
        agreementRecord.setPetType(CAT);
        agreementRecord2.setCarerId(2);
        agreementRecord2.setPetType(CAT);
        agreementRecord3.setCarerId(3);
        agreementRecord3.setPetType(CAT);

        agreementRecord4.setPetType(RET);//неверный тип питомца для теста


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




        agreement.setId(agreementRecord.getId());
        agreement2.setId(agreementRecord2.getId());
        agreement3.setId(agreementRecord3.getId());
        agreement.setNumber(agreementRecord.getNumber());
        agreement2.setNumber(agreementRecord2.getNumber());
        agreement3.setNumber(agreementRecord3.getNumber());
        agreement.setConclusionDate(agreementRecord.getConclusionDate());
        agreement2.setConclusionDate(agreementRecord2.getConclusionDate());
        agreement3.setConclusionDate(agreementRecord3.getConclusionDate());

        agreement.setProbationEndData(agreementRecord.getConclusionDate().plusDays(30));
        agreement2.setProbationEndData(agreementRecord2.getConclusionDate().plusDays(30));
        agreement3.setProbationEndData(agreementRecord3.getConclusionDate().plusDays(30));

        catAgreement.setId(10);

        CatCarer catCarer = new CatCarer();
        catCarer.setCat(cat);
        catAgreement.setProbationEndData(agreement.getProbationEndData());
        catAgreement.setCarer(catCarer);

        catAgreement2.setProbationEndData(agreement2.getProbationEndData());
        catAgreement3.setProbationEndData(agreement3.getProbationEndData());

        dogAgreement.setId(20);
        dogAgreement.setProbationEndData(agreement.getProbationEndData());
        dogAgreement2.setProbationEndData(agreement2.getProbationEndData());
        dogAgreement3.setProbationEndData(agreement3.getProbationEndData());

    }

    @Test
    void shouldReturnAgreementRecordWhenAddAgreementOfCat() {

        when(modelMapperMock.mapToAgreementEntity(eq(agreementRecord))).thenReturn(catAgreement);
        when(catAgreementRepositoryMock.save(catAgreement)).thenReturn(catAgreement);
        when(catRepositoryMock.save(cat)).thenReturn(cat);

        when(modelMapperMock.mapToAgreementRecord(catAgreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord,
                agreementServiceOut.addAgreement(agreementRecord));
    }



    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddAgreementWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.addAgreement(agreementRecord4));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenAddAgreementWithNullAgreementRecord() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.addAgreement(null));
    }

    @Test
    void shouldReturnAgreementRecordWhenFindAgreementByIdOfCat() {

        when(catAgreementRepositoryMock.findById(catAgreement.getId()))
                .thenReturn(Optional.of(catAgreement));
        when(modelMapperMock.mapToAgreementRecord(catAgreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord,
                agreementServiceOut.findAgreementById(catAgreement.getId(), CAT));
    }

    @Test
    void shouldReturnAgreementRecordWhenFindAgreementByIdOfDog() {

        when(dogAgreementRepositoryMock.findById(dogAgreement.getId()))
                .thenReturn(Optional.of(dogAgreement));
        when(modelMapperMock.mapToAgreementRecord(dogAgreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord,
                agreementServiceOut.findAgreementById(dogAgreement.getId(), DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindAgreementByIdWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.findAgreementById(dogAgreement.getId(), RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindAgreementByIdWithWrongId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.findAgreementById(-1, DOG));
    }


    @Test
    void shouldReturnAgreementRecordWhenEditAgreementOfCat() {

        when(modelMapperMock.mapToAgreementEntity(eq(agreementRecord))).thenReturn(catAgreement);
        when(catAgreementRepositoryMock.save(catAgreement)).thenReturn(catAgreement);
        when(modelMapperMock.mapToAgreementRecord(catAgreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord, agreementServiceOut
                .editAgreement(agreementRecord));
    }

    @Test
    void shouldReturnAgreementRecordWhenEditAgreementOfDog() {

        agreementRecord.setPetType(DOG);
        when(modelMapperMock.mapToAgreementEntity(eq(agreementRecord))).thenReturn(dogAgreement);
        when(dogAgreementRepositoryMock.save(dogAgreement)).thenReturn(dogAgreement);
        when(modelMapperMock.mapToAgreementRecord(dogAgreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord, agreementServiceOut
                .editAgreement(agreementRecord));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenEditAgreementWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.editAgreement(agreementRecord4));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenEditAgreementWithNull() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.editAgreement(null));
    }

    @Test
    void shouldInvokeDeleteAgreementOfDog() {

        agreementServiceOut.deleteAgreement(dogAgreement.getId(), DOG);
        Mockito.verify(dogAgreementRepositoryMock, times(1))
                .deleteById(dogAgreement.getId());
    }

    @Test
    void shouldInvokeDeleteAgreementOfCat() {

        agreementServiceOut.deleteAgreement(catAgreement.getId(), CAT);
        Mockito.verify(catAgreementRepositoryMock, times(1))
                .deleteById(catAgreement.getId());
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteAgreementWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.deleteAgreement(catAgreement.getId(), RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenDeleteAgreementWithWrongId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.deleteAgreement(-1, DOG));
    }

    @Test
    void shouldReturnAllAgreementRecordsWhenFindAllAgreementsOfCat() {

        List<CatAgreement> catAgreements = List.of(catAgreement, catAgreement3);
        when(catAgreementRepositoryMock.findAll()).thenReturn(catAgreements);
        when(modelMapperMock.mapToAgreementRecord(catAgreement)).thenReturn(agreementRecord);
        when(modelMapperMock.mapToAgreementRecord(catAgreement3)).thenReturn(agreementRecord3);

        Assertions.assertIterableEquals(List.of(agreementRecord,agreementRecord3),
                agreementServiceOut.findAllAgreements(CAT));

    }

    @Test
    void shouldReturnNewArrayListWhenFindAllAgreementsWithNoAgreementsOfCat() {

        when(catAgreementRepositoryMock.findAll()).thenReturn(List.of());
        Assertions.assertIterableEquals(new ArrayList<>(), agreementServiceOut.findAllAgreements(CAT));

    }

    @Test
    void shouldReturnAllAgreementRecordsWhenFindAllAgreementsOfDog() {

        List<DogAgreement> dogAgreements = List.of(dogAgreement, dogAgreement3);
        when(dogAgreementRepositoryMock.findAll()).thenReturn(dogAgreements);
        when(modelMapperMock.mapToAgreementRecord(dogAgreement)).thenReturn(agreementRecord);
        when(modelMapperMock.mapToAgreementRecord(dogAgreement3)).thenReturn(agreementRecord3);

        Assertions.assertIterableEquals(List.of(agreementRecord, agreementRecord3),
                agreementServiceOut.findAllAgreements(DOG));
    }

    @Test
    void shouldReturnNewArrayListWhenFindAllAgreementsWithNoAgreementsOfDog() {

        when(dogAgreementRepositoryMock.findAll()).thenReturn(List.of());
        Assertions.assertIterableEquals(new ArrayList<>(), agreementServiceOut.findAllAgreements(DOG));

    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindAllAgreementsWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.findAllAgreements(RET));
    }

    @Test
    void ShouldReturnAllAgreementsOnThisDateWhenFindAllAgreementsWithProbationByDateOfCat() {
        when(catAgreementRepositoryMock.findAll())
                .thenReturn(List.of(catAgreement, catAgreement2, catAgreement3));
        Assertions.assertIterableEquals(List.of(catAgreement, catAgreement2, catAgreement3),
                agreementServiceOut.findAllAgreementsWithProbationByDate(LocalDate.now(), CAT));
    }

    @Test
    void ShouldReturnNewArrayListOnDateWhenFindAllAgreementsWithProbationByDateOfCat() {
        when(catAgreementRepositoryMock.findAll())
                .thenReturn(List.of(catAgreement, catAgreement2, catAgreement3));
        Assertions.assertIterableEquals(new ArrayList<>(),
                agreementServiceOut.findAllAgreementsWithProbationByDate(
                        LocalDate.now().plusDays(31), CAT));
    }

    @Test
    void ShouldReturnAllAgreementsOnThisDateWhenFindAllAgreementsWithProbationByDateOfDog() {
        when(dogAgreementRepositoryMock.findAll())
                .thenReturn(List.of(dogAgreement, dogAgreement2, dogAgreement3));
        Assertions.assertIterableEquals(List.of(dogAgreement, dogAgreement2, dogAgreement3),
                agreementServiceOut.findAllAgreementsWithProbationByDate(LocalDate.now(), DOG));
    }

    @Test
    void ShouldReturnNewArrayListOnDateWhenFindAllAgreementsWithProbationByDateOfDog() {
        when(dogAgreementRepositoryMock.findAll())
                .thenReturn(List.of(dogAgreement, dogAgreement2, dogAgreement3));
        Assertions.assertIterableEquals(new ArrayList<>(),
                agreementServiceOut.findAllAgreementsWithProbationByDate(
                        LocalDate.now().plusDays(31), DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindAllAgreementsWithProbationByDateWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.findAllAgreementsWithProbationByDate(LocalDate.now(), RET));
    }

    @Test
    void ShouldReturnIllegalArgumentExceptionWhenFindAllAgreementsWithNoDate() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.findAllAgreementsWithProbationByDate(null, CAT));
    }

    @Test
    void shouldReturnAgreementsListWhenFindAgreementsWithEndingProbationOfCat() {

        catAgreement.setProbationEndData(LocalDate.now().plusDays(2));
        catAgreement2.setProbationEndData(LocalDate.now().plusDays(2));
        catAgreement3.setProbationEndData(LocalDate.now().plusDays(2));

        when(catAgreementRepositoryMock.findAll())
                .thenReturn(List.of(catAgreement, catAgreement2, catAgreement3));

        Assertions.assertIterableEquals(List.of(catAgreement, catAgreement2, catAgreement3),
                agreementServiceOut.findAgreementsWithEndingProbation(CAT));
    }

    @Test
    void shouldReturnAgreementsListWhenFindAgreementsWithEndingProbationOfDog() {

        dogAgreement.setProbationEndData(LocalDate.now().plusDays(2));
        dogAgreement2.setProbationEndData(LocalDate.now().plusDays(2));
        dogAgreement3.setProbationEndData(LocalDate.now().plusDays(2));

        when(dogAgreementRepositoryMock.findAll())
                .thenReturn(List.of(dogAgreement, dogAgreement2, dogAgreement3));

        Assertions.assertIterableEquals(List.of(dogAgreement, dogAgreement2, dogAgreement3),
                agreementServiceOut.findAgreementsWithEndingProbation(DOG));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenFindAllAgreementsWithEndingProbationWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.findAgreementsWithEndingProbation(RET));
    }

    @Test
    void shouldReturnAgreementRecordWhenChangeProbationEndDataOfCat() {

        when(catAgreementRepositoryMock.findById(catAgreement.getId()))
                .thenReturn(Optional.of(catAgreement));
        when(catAgreementRepositoryMock.save(catAgreement)).thenReturn(catAgreement);
        when(modelMapperMock.mapToAgreementRecord(catAgreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord,
                agreementServiceOut
                        .changeProbationEndData(catAgreement.getId(), LocalDate.now(), CAT));

    }

    @Test
    void shouldReturnAgreementRecordWhenChangeProbationEndDataOfDog() {

        when(dogAgreementRepositoryMock.findById(dogAgreement.getId()))
                .thenReturn(Optional.of(dogAgreement));
        when(dogAgreementRepositoryMock.save(dogAgreement)).thenReturn(dogAgreement);
        when(modelMapperMock.mapToAgreementRecord(dogAgreement)).thenReturn(agreementRecord);

        Assertions.assertEquals(agreementRecord, agreementServiceOut
                        .changeProbationEndData(dogAgreement.getId(), LocalDate.now(), DOG));

    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenChangeProbationEndDataWithWrongPetType() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.changeProbationEndData(dogAgreement.getId(), LocalDate.now(), RET));
    }

    @Test
    void shouldReturnIllegalArgumentExceptionWhenChangeProbationEndDataWithWrongId() {

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> agreementServiceOut.changeProbationEndData(-1, LocalDate.now(), DOG));
    }
}