package by.belstu.lab02.services;

import by.belstu.lab02.models.User;
import by.belstu.lab02.models.Worker;
import by.belstu.lab02.repositories.UserRepository;
import by.belstu.lab02.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerServices {
    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    UserRepository userRepository;

    public List<Worker> findAll() {
        return Streamable.of(workerRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).toList();
    }

    public Worker findById(int id) {
        return workerRepository.findById(id).orElse(null);
    }


    public Worker save(Worker worker) {
        return workerRepository.save(worker);
    }

    public void delete(int id) {
        workerRepository.deleteById(id);
    }

}
