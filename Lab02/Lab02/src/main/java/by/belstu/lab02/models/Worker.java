package by.belstu.lab02.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "WORKER")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String lastName;

    @Column
    private String firstName;

    @Column
    private String secondName;

    @Column
    private String position;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private int experience;

    public Worker(String lastName, String firstName, String secondName, String position, String phone, String email, int experience) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.experience = experience;
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + secondName + " ";
    }
}
