package by.belstu.lab02.dto;

import by.belstu.lab02.models.Guest;
import by.belstu.lab02.models.Reservation;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "Неверный ID типа номера")
    private int type_room_id;

    @FutureOrPresent(message = "Неверная дата заезда")
    private LocalDate date_in;

    @Future(message = "Неверная дата выезда")
    private LocalDate date_out;

    @Min(value = 1, message = "Неверный ID гостя")
    private int guest;

    @Min(value = 1, message = "Число гостей не может быть меньше 0")
    private int guests_count;
}
