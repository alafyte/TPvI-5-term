package by.belstu.lab02.models;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "USER_ORDER")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne
    @JoinColumn(name = "guest_id")
    Guest guest;

    @OneToOne
    @JoinColumn(name = "service_id")
    Service service;

    @Column
    Date date_orders;

    @Column
    Date date_service;

    @OneToOne
    @JoinColumn(name = "worker_id")
    Worker worker;

}
