package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatDailyReportRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogDailyReportRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportService.class);
    private final DogDailyReportRepository dogDailyReportRepository;
    private final CatDailyReportRepository catDailyReportRepository;
    private final ModelMapper modelMapper;

    public DailyReportService(DogDailyReportRepository dogDailyReportRepository, CatDailyReportRepository catDailyReportRepository, ModelMapper modelMapper) {
        this.dogDailyReportRepository = dogDailyReportRepository;
        this.catDailyReportRepository = catDailyReportRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод получения списка отчётов по опекуну.
     *
     * @param carerId id опекуна
     * @return возвращает отчет по опекуну (по его id)
     * @see DogDailyReportRepository#findDogDailyReportByDogCarerId(Long)
     */
    @Transactional(readOnly = true)
    public List<DailyReportRecord> findDailyReportsByCarer(Long carerId, PetType petType) {
        if (carerId > 0) {
            if (petType == PetType.CAT) {
                List<CatDailyReport> catDailyReports = catDailyReportRepository.findCatDailyReportByCatCarerId(carerId);
                if (!catDailyReports.isEmpty()) {
                    LOGGER.info("Was invoked method to find cat daily reports by cat carer id");
                    return catDailyReports.stream()
                            .map(this.modelMapper::mapToDailyRecordRecord)
                            .collect(Collectors.toList());
                } else {
                    LOGGER.info("Was invoked method to find all cat daily reports by cat carer id, but cat daily reports were not found");
                    return new ArrayList<>();
                }
            } else if (petType == PetType.DOG) {
                List<DogDailyReport> dogDailyReports = dogDailyReportRepository.findDogDailyReportByDogCarerId(carerId);
                if (!dogDailyReports.isEmpty()) {
                    LOGGER.info("Was invoked method to find dog daily reports by dog carer id");
                    return dogDailyReports.stream()
                            .map(this.modelMapper::mapToDailyRecordRecord)
                            .collect(Collectors.toList());
                } else {
                    LOGGER.info("Was invoked method to find all dog daily reports by dog carer id, but dog daily reports were not found");
                    return new ArrayList<>();
                }
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input carer id = " + carerId + " to find daily reports is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна");
        }
    }

    /**
     * Метод получения списка отчётов по опекуну и дате отчёта.
     *
     * @param carerId id опекуна
     * @return возвращает отчет по опекуну (по его id) и дате.
     * @see DogDailyReportRepository#findDogDailyReportByDogCarerIdAndReportDate(Long, LocalDate)
     */
    @Transactional(readOnly = true)
    public DailyReportRecord findDailyReportByCarerAndDate(Long carerId, LocalDate reportDate, PetType petType) {
        if (carerId > 0 && reportDate != null) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to find cat daily report by cat carer id and the specified date = " + reportDate);
                return this.modelMapper.mapToDailyRecordRecord(this.catDailyReportRepository
                        .findCatDailyReportByCatCarerIdAndReportDate(carerId, reportDate));
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to find dog daily report by dog carer id and the specified date = " + reportDate);
                return this.modelMapper.mapToDailyRecordRecord(this.dogDailyReportRepository
                        .findDogDailyReportByDogCarerIdAndReportDate(carerId, reportDate));
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input carer id = " + carerId + " is incorrect and/or input object 'reportDate' is null");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна и/или корректную дату");
        }
    }

    public List<DailyReportRecord> findDailyReportsByDate(LocalDate localDate, PetType petType) {
        if (petType == PetType.CAT) {
            List<CatDailyReport> catDailyReports = this.catDailyReportRepository.findCatDailyReportsByReportDate(localDate);
            return catDailyReports.stream()
                    .map(this.modelMapper::mapToDailyRecordRecord)
                    .collect(Collectors.toList());
        } else if (petType == PetType.DOG) {
            List<DogDailyReport> dogDailyReports = this.dogDailyReportRepository.findDogDailyReportsByReportDate(localDate);
            return dogDailyReports.stream()
                    .map(this.modelMapper::mapToDailyRecordRecord)
                    .collect(Collectors.toList());
        } else {
            LOGGER.error("Wrong pet type");
            throw new IllegalArgumentException("Тип животного указан не верно");
        }
    }

    @Transactional(readOnly = true)
    public DailyReport findDailyReportByCarerIdAndDate(Long carerId, LocalDate reportDate, PetType petType) {
        if (carerId > 0 && reportDate != null) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to find cat daily report by cat carer id = " + carerId + " and the specified date = " +
                        reportDate);
                return this.catDailyReportRepository.findCatDailyReportByCatCarerIdAndReportDate(carerId, reportDate);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to find dog daily report by dog carer id = " + carerId + " and the specified date = " +
                        reportDate);
                return this.dogDailyReportRepository.findDogDailyReportByDogCarerIdAndReportDate(carerId, reportDate);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input carer id = " + carerId + " is incorrect and/or input object 'reportDate' is null");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна и/или корректную дату");
        }
    }

    @Transactional
    public void addDailyReport(DailyReport dailyReport, PetType petType) {
        if (dailyReport != null) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method for adding cat daily report from Telegram bot");
                this.catDailyReportRepository.save((CatDailyReport) dailyReport);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method for adding dog daily report from Telegram bot");
                this.dogDailyReportRepository.save((DogDailyReport) dailyReport);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'dailyReport' is null");
            throw new IllegalArgumentException("Input object 'dailyReport' is null");
        }
    }

    @Transactional
    public void deleteDailyReport(long id, PetType petType) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to delete cat daily report");
                this.catDailyReportRepository.deleteById(id);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to delete dog daily report");
                this.dogDailyReportRepository.deleteById(id);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for deleting daily report is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id ежедневного отчета");
        }
    }
}
