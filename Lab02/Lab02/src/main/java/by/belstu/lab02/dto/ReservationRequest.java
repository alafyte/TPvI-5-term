package by.belstu.lab02.dto;

import by.belstu.lab02.models.Guest;
import by.belstu.lab02.models.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private int id;
    private int type_room_id;
    private LocalDate date_in;
    private LocalDate date_out;
    private int guest;
    private int guests_count;
}
