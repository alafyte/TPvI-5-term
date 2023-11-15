package by.belstu.lab02.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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
    private Date birthday;

    public String getFullName() {
        return lastName + " " + firstName + " " + secondName;
    }

}
