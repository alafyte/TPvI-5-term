package by.belstu.lab02.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestRequest {
    private int id;
    @NotBlank(message = "Фамилия обязательна")
    private String lastname;
    @NotBlank(message = "Имя обязательно")
    private String firstname;
    private String secondname;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @Past(message = "Неверная дата рождения")
    private LocalDate birthday;
}
