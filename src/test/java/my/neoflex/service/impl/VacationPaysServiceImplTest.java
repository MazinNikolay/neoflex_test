package my.neoflex.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class VacationPaysServiceImplTest {

    private final VacationPaysServiceImpl service;

    public VacationPaysServiceImplTest() {
        service = new VacationPaysServiceImpl();
    }

    @Test
    @DisplayName("Тест корректного расчета отпускных")
    void calculateBySalaryCorrect() {

        LocalDate currentDate = LocalDate.now().plusYears(1);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String testYear = String.valueOf(currentDate.getYear());
        String testDateString = "10.01.".concat(testYear);

        System.out.println(testDateString);
        LocalDate testDate = LocalDate.parse(testDateString, dateFormatter);

        BigDecimal averageSalary = BigDecimal.valueOf(100000L);
        int vacationDays = 10;

        BigDecimal actualVacationPay = service.calculateBySalary(averageSalary, vacationDays, testDate)
                .getVacationPay();
        BigDecimal expectedVacationPay = BigDecimal.valueOf(34129.69);

        Assertions.assertEquals(expectedVacationPay, actualVacationPay);
    }

    @Test
    @DisplayName("Тест корректного расчета отпускных без даты начала отпуска")
    void calculateBySalaryCorrectWithoutStartDate() {

        BigDecimal averageSalary = BigDecimal.valueOf(100000L);
        int vacationDays = 10;

        BigDecimal actualVacationPay = service.calculateBySalary(averageSalary, vacationDays, null)
                .getVacationPay();
        BigDecimal expectedVacationPay = BigDecimal.valueOf(34129.69);

        Assertions.assertEquals(expectedVacationPay, actualVacationPay);
    }

    @Test
    @DisplayName("Тест расчета отпускных с отрицательной зарплатой")
    void calculateByInvalidSalary() {

        BigDecimal averageSalary = BigDecimal.valueOf(-100000L);
        int vacationDays = 10;

        String message = Assertions.assertThrows(RuntimeException.class, () ->
                        service.calculateBySalary(averageSalary, vacationDays, null))
                .getMessage();

        Assertions.assertEquals("Средняя зарплата должна быть положительной", message);
    }

    @Test
    @DisplayName("Тест расчета отпускных с некорректным количеством дней отпуска")
    void calculateByInvalidVacationDays() {

        BigDecimal averageSalary = BigDecimal.valueOf(100000L);
        int vacationDays = 0;

        String message = Assertions.assertThrows(RuntimeException.class, () ->
                        service.calculateBySalary(averageSalary, vacationDays, null))
                .getMessage();

        Assertions.assertEquals("Количество дней отпуска должно быть положительным", message);
    }

    @Test
    @DisplayName("Тест расчета отпускных с некорректной датой начала отпуска")
    void calculateByInvalidStartVacationDate() {

        LocalDate currentDate = LocalDate.now().minusYears(1);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String testYear = String.valueOf(currentDate.getYear());
        String testDateString = "10.01.".concat(testYear);

        System.out.println(testDateString);
        LocalDate testDate = LocalDate.parse(testDateString, dateFormatter);

        BigDecimal averageSalary = BigDecimal.valueOf(100000L);
        int vacationDays = 10;

        String message = Assertions.assertThrows(RuntimeException.class, () ->
                        service.calculateBySalary(averageSalary, vacationDays, testDate))
                .getMessage();

        Assertions.assertEquals("Дата начала отпуска не может быть в прошлом", message);
    }
}