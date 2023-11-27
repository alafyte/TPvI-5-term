package by.belstu.lab02.contollers;

import by.belstu.lab02.dto.GuestRequest;
import by.belstu.lab02.models.Guest;
import by.belstu.lab02.models.User;
import by.belstu.lab02.repositories.UserRepository;
import by.belstu.lab02.services.GuestServices;
import by.belstu.lab02.services.UserDetailsImpl;
import by.belstu.lab02.services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.util.List;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class GuestController {

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
        Guest newGuest = new Guest(
                guestRequest.getFirstname(),
                guestRequest.getLastname(),
                guestRequest.getSecondname(),
                guestRequest.getEmail(),
                guestRequest.getBirthday());
        guestServices.saveGuest(newGuest);
        log.info("/create-guest POST");
        return new ResponseEntity<>(newGuest, HttpStatus.OK);
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
    }


    @GetMapping(value = {"/delete-guest/{id}"})
    public String DeleteGuest(ModelAndView modelAndView, @PathVariable("id") int id) {
        guestServices.deleteGuest(id);
        return "redirect:/view-guests";
    }
}