package by.belstu.lab02.repositories;

import by.belstu.lab02.models.TypeRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRoomRepository extends CrudRepository<TypeRoom, Integer> {

}