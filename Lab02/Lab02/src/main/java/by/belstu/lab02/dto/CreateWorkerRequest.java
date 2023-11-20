package by.belstu.lab02.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateWorkerRequest {
    private String lastname;
    private String firstname;
    private String secondname;
    private String position;
    private String phone;
    private String email;
    private int experience;
}
