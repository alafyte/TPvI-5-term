package by.belstu.lab02.services;

import by.belstu.lab02.models.Reservation;
import by.belstu.lab02.models.Room;
import by.belstu.lab02.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationServices {
    @Autowired
    public ReservationRepository reservationRepository;


    public void save(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public Reservation findById(int id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> findAll() {
        return Streamable.of(reservationRepository.findAll()).toList();
    }

    public Boolean doesReservationExist(Room room, LocalDate date_in, LocalDate date_out) {
        List<Reservation> reservations = Streamable.of(reservationRepository.findAll()).toList();
        boolean isFound = false;
        for (Reservation reservation : reservations) {
            if (Objects.equals(reservation.getRoom().getId(), room.getId())) {
                LocalDate reservationCheckIn = reservation.getDateIn();
                LocalDate reservationCheckOut = reservation.getDateOut();

                isFound = !((date_in.isAfter(reservationCheckOut) || date_in.isEqual(reservationCheckOut))
                        || (date_out.isBefore(reservationCheckIn) || date_out.isEqual(reservationCheckIn)));
            }
        }
        return isFound;
    }

}
