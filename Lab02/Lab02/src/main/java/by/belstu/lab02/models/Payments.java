package by.belstu.lab02.models;

import lombok.*;

import jakarta.persistence.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PAYMENT")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int Id;

    @OneToOne
    @JoinColumn(name = "worker_id")
    Worker worker;

    @Column
    float bonus;

    @Column
    float salary;

    @Column
    float work_hour;

    Date date;
}
