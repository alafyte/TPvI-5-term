package by.belstu.lab02.contollers;

import by.belstu.lab02.dto.GuestRequest;
import by.belstu.lab02.models.Guest;
import by.belstu.lab02.models.User;
import by.belstu.lab02.repositories.UserRepository;
import by.belstu.lab02.services.GuestServices;
import by.belstu.lab02.services.UserDetailsImpl;
import by.belstu.lab02.services.UserServices;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class GuestController {
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private GuestServices guestServices;

    @Autowired
    private UserServices userServices;

    @GetMapping("/view-guests")
    public ModelAndView GuestList(Model model) {
        UserDetailsImpl auth_user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServices.findByLogin(auth_user.getUsername()).get();
        ModelAndView modelAndView = new ModelAndView("ViewGuest");
        List<Guest> guests = guestServices.getGuests();
        model.addAttribute("guests", guests);
        modelAndView.addObject("user", user);
        log.info("/view-guests GET");
        return modelAndView;
    }


    @GetMapping("/create-guest")
    public ModelAndView SaveGuest(Model model) {
        UserDetailsImpl auth_user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServices.findByLogin(auth_user.getUsername()).get();
        ModelAndView modelAndView = new ModelAndView("CreateGuest");
        modelAndView.addObject("user", user);
        log.info("/create-guest GET");
        return modelAndView;
    }

    @PostMapping("/create-guest")
    public ResponseEntity<?> SaveGuest(@RequestBody GuestRequest guestRequest) {
        try {
            ResponseEntity<?> errorMessages = validateGuestRequest(guestRequest);
            if (errorMessages != null) return errorMessages;

            Guest newGuest = new Guest(
                    guestRequest.getFirstname(),
                    guestRequest.getLastname(),
                    guestRequest.getSecondname(),
                    guestRequest.getEmail(),
                    guestRequest.getBirthday());
            guestServices.saveGuest(newGuest);
            log.info("/create-guest POST");
            return new ResponseEntity<>(newGuest, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при добавлении гостя");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    @GetMapping(value = {"/edit-guest/{id}"})
    public ModelAndView UpdateGuest(Model model, @PathVariable("id") int id) {
        UserDetailsImpl auth_user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServices.findByLogin(auth_user.getUsername()).get();
        ModelAndView modelAndView = new ModelAndView("EditGuest");
        Guest guest = guestServices.findGuest(id);
        model.addAttribute("guest", guest);
        modelAndView.addObject("user", user);
        log.info("/edit-guest GET");
        return modelAndView;

    }

    @PostMapping(value = {"/edit-guest"})
    public ResponseEntity<?> UpdateGuest(@RequestBody GuestRequest guestRequest) {
        try {
            ResponseEntity<?> errorMessages = validateGuestRequest(guestRequest);
            if (errorMessages != null) return errorMessages;

            int id = guestRequest.getId();
            String lastname = guestRequest.getLastname();
            String firstname = guestRequest.getFirstname();
            String secondname = guestRequest.getSecondname();
            String email = guestRequest.getEmail();
            Date birthday = guestRequest.getBirthday();
            Guest newGuest = new Guest(id, firstname, lastname, secondname, email, birthday);
            guestServices.updateGuest(newGuest);
            log.info("edit-guest POST");
            return new ResponseEntity<>(newGuest, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при изменении данных гостя");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    private ResponseEntity<?> validateGuestRequest(@RequestBody GuestRequest guestRequest) {
        Set<ConstraintViolation<GuestRequest>> violations = validator.validate(guestRequest);

        if (!violations.isEmpty()) {
            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
        return null;
    }


    @GetMapping(value = {"/delete-guest/{id}"})
    public String DeleteGuest(ModelAndView modelAndView, @PathVariable("id") int id) {
        try {
            guestServices.deleteGuest(id);
            log.info("/delete-guest GET");
            return "redirect:/view-guests";
        } catch (Exception e) {
            return "redirect:/view-guests";
        }
    }
}
