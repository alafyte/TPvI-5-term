package by.belstu.lab02.repositories;

import by.belstu.lab02.models.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
}