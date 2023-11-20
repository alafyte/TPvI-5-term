package by.belstu.lab02.contollers;

import by.belstu.lab02.dto.CreateWorkerRequest;
import by.belstu.lab02.dto.EditWorkerRequest;
import by.belstu.lab02.models.Worker;
import by.belstu.lab02.services.EmailSenderService;
import by.belstu.lab02.services.WorkerServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class WorkerController {
    public final WorkerServices workerServices;
    public final EmailSenderService emailSenderService;

    public WorkerController(WorkerServices workerServices, EmailSenderService emailSenderService) {
        this.workerServices = workerServices;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/view-workers")
    public ModelAndView workers(ModelAndView modelAndView) {
        List<Worker> workers = workerServices.findAll();
        modelAndView.addObject("workers", workers);
        modelAndView.setViewName("ViewWorkers");
        return modelAndView;
    }

    @GetMapping("/create-worker")
    public ModelAndView createWorker(ModelAndView modelAndView) {
        modelAndView.setViewName("CreateWorker");
        return modelAndView;
    }

    @PostMapping("/create-worker")
    public ResponseEntity<?> createWorker(@RequestBody CreateWorkerRequest createWorkerRequest) {
        Worker newWorker = new Worker(
                createWorkerRequest.getLastname(),
                createWorkerRequest.getFirstname(),
                createWorkerRequest.getSecondname(),
                createWorkerRequest.getPosition(),
                createWorkerRequest.getPhone(),
                createWorkerRequest.getEmail(),
                createWorkerRequest.getExperience());
        workerServices.save(newWorker);
        //emailSenderService.sendSimpleEmail(workerForm.getEmail(),"New worker", "Вы были добавленны как новый сотрудник");

        return new ResponseEntity<>(newWorker, HttpStatus.OK);
    }

    @GetMapping("/edit-worker/{id}")
    public ModelAndView editWorker(ModelAndView modelAndView, @PathVariable String id) {
        modelAndView.setViewName("EditWorker");
        Worker worker = workerServices.findById(Integer.parseInt(id));
        modelAndView.addObject("worker", worker);
        return modelAndView;
    }

    @PostMapping("/edit-worker")
    public ResponseEntity<?> editWorker(@RequestBody EditWorkerRequest editWorkerRequest) {
        Worker newWorker = new Worker(
                editWorkerRequest.getId(),
                editWorkerRequest.getLastname(),
                editWorkerRequest.getFirstname(),
                editWorkerRequest.getSecondname(),
                editWorkerRequest.getPosition(),
                editWorkerRequest.getPhone(),
                editWorkerRequest.getEmail(),
                editWorkerRequest.getExperience()
                );
        workerServices.save(newWorker);

        return new ResponseEntity<>(newWorker, HttpStatus.OK);
    }


    @GetMapping("/delete-worker/{id}")
    public ModelAndView deleteWorker(ModelAndView modelAndView, @PathVariable String id) {

        workerServices.delete(Integer.parseInt(id));
        modelAndView.setViewName("ViewWorkers");
        modelAndView.addObject("workers", workerServices.findAll());
        return modelAndView;
    }


}
