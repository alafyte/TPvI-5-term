package by.belstu.lab02.models;


import lombok.*;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "EVENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String event;

    @Column
    private Date date_create;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
