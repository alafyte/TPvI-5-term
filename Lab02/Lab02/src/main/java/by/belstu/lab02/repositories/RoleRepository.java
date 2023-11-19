package by.belstu.lab02.repositories;

import by.belstu.lab02.models.Role;
import by.belstu.lab02.models.Roles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(Roles name);
}