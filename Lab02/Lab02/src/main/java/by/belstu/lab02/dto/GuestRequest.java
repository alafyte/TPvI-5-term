package by.belstu.lab02.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestRequest {
    private int id;
    private String lastname;
    private String firstname;
    private String secondname;
    private String email;
    private Date birthday;
}
