package by.belstu.lab02.services;

import by.belstu.lab02.models.User;
import by.belstu.lab02.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {
    @Autowired
    public UserRepository repository;

    public void saveUser(User user){
        repository.save(user);
    }

    public void deleteUser(int id){
        repository.deleteById(id);
    }

    public User findUser(int id){
        return repository.findById(id).orElse(null);
    }

    public void updateUser(User users){
        repository.save(users);
    }

    public List<User> getUsers(){
        return Streamable.of(repository.findAll()).toList();
    }

    public Optional<User> findByLogin(String login) {
        List<User> users = Streamable.of(repository.findAll()).toList();

        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst();
    }
    public Optional<User> findByWorkerId(int id) {
        List<User> users = Streamable.of(repository.findAll()).toList();
        users = users.stream().filter(user -> user.getWorker() != null).toList();
        return users.stream().filter(user -> user.getWorker().getId().equals(id)).findFirst();
    }
    public Boolean existsByLogin(String login) {
        List<User> users = Streamable.of(repository.findAll()).toList();

        return users.stream().anyMatch(user -> user.getLogin().equals(login));
    }
}
