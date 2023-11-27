package by.belstu.lab02.contollers;

import by.belstu.lab02.dto.GuestRequest;
import by.belstu.lab02.dto.WorkerRequest;
import by.belstu.lab02.models.Worker;
import by.belstu.lab02.services.EmailSenderService;
import by.belstu.lab02.services.WorkerServices;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class WorkerController {
    @Autowired
    private LocalValidatorFactoryBean validator;

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
        try {
            ResponseEntity<?> errorMessages = validateWorkerRequest(createWorkerRequest);
            if (errorMessages != null) return errorMessages;

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
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при создании работника");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    private ResponseEntity<?> validateWorkerRequest(@RequestBody WorkerRequest createWorkerRequest) {
        Set<ConstraintViolation<WorkerRequest>> violations = validator.validate(createWorkerRequest);

        if (!violations.isEmpty()) {
            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
        return null;
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
        try {
            ResponseEntity<?> errorMessages = validateWorkerRequest(editWorkerRequest);
            if (errorMessages != null) return errorMessages;

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
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при изменении данных работника");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }


    @GetMapping("/delete-worker/{id}")
    public String deleteWorker(ModelAndView modelAndView, @PathVariable int id) {
        try {
            workerServices.delete(id);
            log.info("/delete-worker GET");
            return "redirect:/view-workers";
        } catch (Exception e) {
            return "redirect:/view-workers";
        }
    }
}
