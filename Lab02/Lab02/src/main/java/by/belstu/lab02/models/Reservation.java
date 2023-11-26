package by.belstu.lab02.models;

import lombok.*;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "RESERVATION")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    Room room;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    Guest guest;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    @Column
    LocalDate dateIn;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    @Column
    LocalDate dateOut;

    @Column
    int guestCount;

    public String getDateInFormatted() {
        if (dateIn != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            formatter.format(dateIn);
            return dateIn.format(formatter);
        }
        return null;
    }

    public String getDateOutFormatted() {
        if (dateOut != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dateOut.format(formatter);
        }
        return null;
    }

}
