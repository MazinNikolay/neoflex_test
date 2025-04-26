package my.neoflex.service.impl;

import my.neoflex.dto.VacationPayResponse;
import my.neoflex.service.VacationPaysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class VacationPaysServiceImpl implements VacationPaysService {

    Logger logger = LoggerFactory.getLogger(VacationPaysServiceImpl.class);
    private static final BigDecimal AVERAGE_DAYS_IN_MONTH = BigDecimal.valueOf(29.3);
    private static final Set<LocalDate> HOLIDAYS = Set.of(
            LocalDate.of(0, 1, 1),  // Новый год
            LocalDate.of(0, 1, 2),  // Новый год
            LocalDate.of(0, 1, 3),  // Новый год
            LocalDate.of(0, 1, 4),  // Новый год
            LocalDate.of(0, 1, 5),  // Новый год
            LocalDate.of(0, 1, 6),  // Новый год
            LocalDate.of(0, 1, 7),  // Новый год
            LocalDate.of(0, 1, 8),  // Новый год
            LocalDate.of(0, 2, 23), // День защитника Отечества
            LocalDate.of(0, 3, 8),  // Международный женский день
            LocalDate.of(0, 5, 1),  // Праздник Весны и Труда
            LocalDate.of(0, 5, 9),  // День Победы
            LocalDate.of(0, 6, 12), // День России
            LocalDate.of(0, 11, 4)  // День народного единства
    );

    @Override
    public VacationPayResponse calculateBySalary(BigDecimal averageSalary, int vacationDays, LocalDate startVacationDate) {

        logger.info("Метод расчета отпускных");
        validateRequest(averageSalary, vacationDays, startVacationDate);

        int workingDays = startVacationDate == null ?
                vacationDays : calculateWorkingDays(startVacationDate, vacationDays);

        BigDecimal dailySalary = averageSalary
                .divide(AVERAGE_DAYS_IN_MONTH, 10, RoundingMode.HALF_UP);

        BigDecimal vacationPay = dailySalary.multiply(BigDecimal.valueOf(workingDays))
                .setScale(2, RoundingMode.HALF_UP);

        VacationPayResponse response = new VacationPayResponse();
        response.setVacationPay(vacationPay);
        return response;
    }

    private void validateRequest(BigDecimal averageSalary, int vacationDays, LocalDate startVacationDate) {

        logger.info("Метод проверки валидности входных данных");
        if (averageSalary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Средняя зарплата должна быть положительной");
        }
        if (vacationDays <= 0) {
            throw new RuntimeException("Количество дней отпуска должно быть положительным");
        }
        if (startVacationDate != null) {
            if (startVacationDate.isBefore(LocalDate.now())) {
                throw new RuntimeException("Дата начала отпуска не может быть в прошлом");
            }
        }
    }

    private int calculateWorkingDays(LocalDate startDate, int vacationDays) {

        logger.info("Метод расчета дней отпуска с учетом праздничных");
        Set<LocalDate> vacationDates = new HashSet<>();
        LocalDate currentDate = startDate;

        for (int i = 0; i < vacationDays; i++) {
            vacationDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return (int) vacationDates.stream()
                .filter(date -> !isHoliday(date))
                .count();
    }

    private boolean isHoliday(LocalDate date) {

        logger.info("Метод проверки праздничного дня");
        return HOLIDAYS.contains(LocalDate.of(0, date.getMonth(), date.getDayOfMonth()));
    }
}
