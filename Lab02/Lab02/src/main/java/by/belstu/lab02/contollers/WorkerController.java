package by.belstu.lab02.contollers;

import by.belstu.lab02.dto.WorkerRequest;
import by.belstu.lab02.models.Worker;
import by.belstu.lab02.services.EmailSenderService;
import by.belstu.lab02.services.WorkerServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public WorkerServices workerServices;

    @Autowired
    public EmailSenderService emailSenderService;


    @GetMapping("/view-workers")
    public ModelAndView workers(ModelAndView modelAndView) {
        List<Worker> workers = workerServices.findAll();
        modelAndView.addObject("workers", workers);
        modelAndView.setViewName("ViewWorkers");

        log.info("/view-workers GET");
        return modelAndView;
    }

    @GetMapping("/create-worker")
    public ModelAndView createWorker(ModelAndView modelAndView) {
        modelAndView.setViewName("CreateWorker");

        log.info("/create-worker GET");
        return modelAndView;
    }

    @PostMapping("/create-worker")
    public ResponseEntity<?> createWorker(@RequestBody WorkerRequest createWorkerRequest) {
        Worker newWorker = new Worker(
                createWorkerRequest.getLastname(),
                createWorkerRequest.getFirstname(),
                createWorkerRequest.getSecondname(),
                createWorkerRequest.getPosition(),
                createWorkerRequest.getPhone(),
                createWorkerRequest.getEmail(),
                createWorkerRequest.getExperience());
        workerServices.save(newWorker);
        log.info("/create-worker POST");
        return new ResponseEntity<>(newWorker, HttpStatus.OK);
    }

    @GetMapping("/edit-worker/{id}")
    public ModelAndView editWorker(ModelAndView modelAndView, @PathVariable int id) {
        modelAndView.setViewName("EditWorker");
        Worker worker = workerServices.findById(id);
        modelAndView.addObject("worker", worker);

        log.info("/edit-worker GET");
        return modelAndView;
    }

    @PostMapping("/edit-worker")
    public ResponseEntity<?> editWorker(@RequestBody WorkerRequest editWorkerRequest) {
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

        log.info("/edit-worker POST");
        return new ResponseEntity<>(newWorker, HttpStatus.OK);
    }


    @GetMapping("/delete-worker/{id}")
    public String deleteWorker(ModelAndView modelAndView, @PathVariable int id) {
        workerServices.delete(id);

        log.info("/delete-worker GET");
        return "redirect:/view-workers";
    }


}
