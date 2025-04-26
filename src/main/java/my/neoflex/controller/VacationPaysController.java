package my.neoflex.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import my.neoflex.dto.VacationPayResponse;
import my.neoflex.service.VacationPaysService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/calculacte")
@Tag(name = "Контроллер для вычисления отпускных")
@RequiredArgsConstructor
public class VacationPaysController {

    private final VacationPaysService vacationPaysService;

    @Operation(summary = "Вычисление отпускных",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Вычисление прошло успешно"),
                    @ApiResponse(responseCode = "400",
                            description = "Ошибка ввода данных")
            })
    @GetMapping
    public VacationPayResponse calculateAverageVacationPays(@RequestParam BigDecimal averageSalary,
                                                            @RequestParam int vacationDays,
                                                            @RequestParam(required = false)
                                                            @DateTimeFormat(pattern = "dd.MM.yyyy")
                                                            LocalDate startVacationDate) {
        return vacationPaysService.calculateBySalary(averageSalary, vacationDays, startVacationDate);
    }
}

