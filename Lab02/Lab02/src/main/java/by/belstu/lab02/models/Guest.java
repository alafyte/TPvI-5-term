package by.belstu.lab02.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "GUEST")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String secondName;

    @Column
    private String email;

    @Column
    private LocalDate birthday;

    public Guest(String firstName, String lastName, String secondName, String email, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondName = secondName;
        this.email = email;
        this.birthday = birthday;
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + secondName;
    }

    public String getBirthdayFormatted() {
        if (birthday != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formatter.format(birthday);
            return birthday.format(formatter);
        }
        return null;
    }

}
