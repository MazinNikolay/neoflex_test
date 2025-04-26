package my.neoflex.service;

import my.neoflex.dto.VacationPayResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VacationPaysService {
    VacationPayResponse calculateBySalary(BigDecimal averageSalary, int vacationDays, LocalDate startVacationDate);
}
