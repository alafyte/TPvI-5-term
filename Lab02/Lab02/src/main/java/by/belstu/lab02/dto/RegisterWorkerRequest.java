package by.belstu.lab02.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterWorkerRequest {

    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    @Size(min = 8, message = "Пароль должен содержать как минимум 8 символов")
    private String password;

    private int id;
}
