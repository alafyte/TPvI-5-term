package by.belstu.lab02.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkerRequest {
    private int id;

    @NotBlank(message = "Фамилия обязательна")
    private String lastname;

    @NotBlank(message = "Имя обязательно")
    private String firstname;

    private String secondname;

    @NotBlank(message = "Должность обязательна")
    private String position;

    @NotBlank(message = "Номер телефона обязателен")
    @Pattern(regexp = "\\+375\\(\\d{2}\\)\\d{3}-\\d{2}-\\d{2}", message = "Неверный формат номера телефона")
    private String phone;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный email")
    private String email;

    @Min(value = 0, message = "Опыт работы не может быть меньше 0")
    @Max(value = 70, message = "Опыт работы не может превышать 70 лет")
    private int experience;
}
