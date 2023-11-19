package by.belstu.lab02.repositories;

import by.belstu.lab02.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByLogin(String login);
    Boolean existsByLogin(String login);
}