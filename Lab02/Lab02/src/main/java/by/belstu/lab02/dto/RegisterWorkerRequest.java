package by.belstu.lab02.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterWorkerRequest {
    private String login;
    private String password;
    private int id;
}
