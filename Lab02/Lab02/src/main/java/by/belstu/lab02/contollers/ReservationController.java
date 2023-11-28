package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.ReservationRequest;
import by.belstu.lab02.models.*;
import by.belstu.lab02.services.*;
import jakarta.validation.ConstraintViolation;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping
public class ReservationController {
    @Autowired
    public ReservationServices reservationServices;

    @Autowired
    public TypeRoomServices typeRoomServices;

    @Autowired
    public RoomServices roomServices;

    @Autowired
    public GuestServices guestServices;

    @Autowired
    UserServices userServices;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    EmailSenderService emailSenderService;

    @GetMapping(value = {"/view-reservation"})
    public ModelAndView ViewReservation(Model model) {
        UserDetailsImpl auth_user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServices.findByLogin(auth_user.getUsername()).get();
        ModelAndView modelAndView = new ModelAndView("ViewReservation");
        List<Reservation> reservations = reservationServices.findAll();
        modelAndView.addObject("reservations", reservations);
        modelAndView.addObject("user", user);
        log.info("/view-reservation GET");
        return modelAndView;
    }

    @GetMapping(value = {"/create-reservation"})
    public ModelAndView CreateReservation(ModelAndView model) {
        UserDetailsImpl auth_user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServices.findByLogin(auth_user.getUsername()).get();
        ModelAndView modelAndView = new ModelAndView("CreateReservation");
        List<TypeRoom> typeRooms = typeRoomServices.getTypeRooms();
        List<Guest> guests = guestServices.getGuests();
        modelAndView.addObject("typeroomList", typeRooms);
        modelAndView.addObject("guests", guests);
        modelAndView.addObject("user", user);
        log.info("/create-reservation GET");
        return modelAndView;
    }

    @PostMapping(value = {"/create-reservation"})
    public ResponseEntity<?> CreateReservation(@RequestBody ReservationRequest reservationRequest) {
        try {
            Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(reservationRequest);

            if (!violations.isEmpty()) {
                List<String> errorMessages = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }

            if (reservationRequest.getDate_in().isAfter(reservationRequest.getDate_out())) {
                List<String> errorMessages = new ArrayList<String>();
                errorMessages.add("Неккоректные дата заезда/выезда");
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }

            Reservation reservation = new Reservation();
            Room freeRoom = getFreeRoom(reservationRequest.getType_room_id(), reservationRequest.getDate_in(), reservationRequest.getDate_out(), reservationRequest.getGuests_count());
            Guest newGuest = guestServices.findGuest(reservationRequest.getGuest());
            if (freeRoom != null) {
                reservation.setGuest(newGuest);
                reservation.setDateIn(reservationRequest.getDate_in());
                reservation.setDateOut(reservationRequest.getDate_out());
                reservation.setGuestCount(reservationRequest.getGuests_count());
                reservation.setRoom(freeRoom);
                reservationServices.save(reservation);
                emailSenderService.sendSimpleEmail(newGuest.getEmail(), "Бронирование",
                        "Здравствуйте, " + newGuest.getFirstName() + " " + newGuest.getSecondName() +
                                "!\nНа ваше имя был забронирован номер. Информация о бронировании:\n" +
                                "Номер: " + reservation.getRoom().getNumber() + "\nДата заезда: " + reservation.getDateInFormatted()
                                + "\nДата выезда: " + reservation.getDateOutFormatted() + "\nКол-во гостей: "
                                + reservation.getGuestCount() + "\nЦена (посуточно): " + reservation.getRoom().getPrice());
            } else {
                List<String> errorMessages = new ArrayList<String>();
                errorMessages.add("Нет свободных номеров на эти даты/тип номера/кол-во гостей");
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }
            log.info("/create-reservation POST");
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при бронировании");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    public Room getFreeRoom(int type, LocalDate date_in, LocalDate date_out, int guests_count) {
        List<Room> rooms = roomServices.getRooms();
        for (Room room : rooms) {
            if (room.getTypeOfRooms().getId() == type && room.getCountOfPlaces() >= guests_count && !reservationServices.doesReservationExist(room, date_in, date_out)) {
                return room;
            }
        }
        return null;
    }

    @GetMapping(value = {"/edit-reservation/{id}"})
    public ModelAndView UpdateReservation(Model model, @PathVariable("id") int id) {
        UserDetailsImpl auth_user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServices.findByLogin(auth_user.getUsername()).get();
        ModelAndView modelAndView = new ModelAndView("EditReservation");
        Reservation reservation = reservationServices.findById(id);
        model.addAttribute("reservation", reservation);
        List<Guest> guests = guestServices.getGuests();
        List<TypeRoom> typeRooms = typeRoomServices.getTypeRooms();
        modelAndView.addObject("typeroomList", typeRooms);
        modelAndView.addObject("guests", guests);
        modelAndView.addObject("user", user);
        log.info("/edit-reservation GET");
        return modelAndView;
    }

    @PostMapping(value = {"/edit-reservation"})
    public ResponseEntity<?> UpdateReservation(@RequestBody ReservationRequest reservationRequest) {
        try {
            Reservation oldReservation = reservationServices.findById(reservationRequest.getId());
            reservationRequest.setGuest(oldReservation.getGuest().getId());

            Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(reservationRequest);
            if (!violations.isEmpty()) {
                List<String> errorMessages = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }

            Reservation reservation = new Reservation();
            if (Objects.equals(oldReservation.getDateIn(), reservationRequest.getDate_in()) && Objects.equals(oldReservation.getDateOut(), reservationRequest.getDate_out())) {
                reservation.setRoom(oldReservation.getRoom());
            } else {
                reservation.setRoom(getFreeRoom(reservationRequest.getType_room_id(), reservationRequest.getDate_in(), reservationRequest.getDate_out(), reservationRequest.getGuests_count()));
            }
            if (reservationRequest.getDate_in().isAfter(reservationRequest.getDate_out())) {
                List<String> errorMessages = new ArrayList<String>();
                errorMessages.add("Неккоректные дата заезда/выезда");
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }
            if (reservationRequest.getGuests_count() > reservation.getRoom().getCountOfPlaces()) {
                List<String> errorMessages = new ArrayList<String>();
                errorMessages.add("Количество гостей не допустимо для данного типа номера");
                return new ResponseEntity<>(errorMessages, HttpStatus.OK);
            }


            reservation.setId(reservationRequest.getId());
            reservation.setDateIn(reservationRequest.getDate_in());
            reservation.setDateOut(reservationRequest.getDate_out());
            reservation.setGuest(oldReservation.getGuest());
            reservation.setGuestCount(reservationRequest.getGuests_count());
            reservationServices.save(reservation);

            Guest newGuest = reservation.getGuest();

            emailSenderService.sendSimpleEmail(newGuest.getEmail(), "Бронирование",
                    "Здравствуйте, " + newGuest.getFirstName() + " " + newGuest.getSecondName() +
                            "!\nВаша бронь была изменена. Информация о бронировании:\n" +
                            "Номер: " + reservation.getRoom().getNumber() + "\nДата заезда: " + reservation.getDateInFormatted()
                            + "\nДата выезда: " + reservation.getDateOutFormatted() + "\nКол-во гостей: "
                            + reservation.getGuestCount() + "\nЦена (посуточно): " + reservation.getRoom().getPrice());
            log.info("/edit-reservation POST");
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при изменении брони");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    @GetMapping(value = {"/delete-reservation/{id}"})
    public String DeleteReservation(Model model, @PathVariable("id") int id) {
        try {
            Reservation reservation = reservationServices.findById(id);
            Guest newGuest = reservation.getGuest();
            reservationServices.delete(reservation);

            emailSenderService.sendSimpleEmail(newGuest.getEmail(), "Бронирование",
                    "Здравствуйте, " + newGuest.getFirstName() + " " + newGuest.getSecondName() +
                            "!\nВаша бронь была отменена. Информация о бронировании:\n" +
                            "Номер: " + reservation.getRoom().getNumber() + "\nДата заезда: " + reservation.getDateInFormatted()
                            + "\nДата выезда: " + reservation.getDateOutFormatted() + "\nКол-во гостей: "
                            + reservation.getGuestCount() + "\nЦена (посуточно): " + reservation.getRoom().getPrice());
            log.info("/delete-reservation GET");
            return "redirect:/view-reservation";
        } catch (Exception e) {
            return "redirect:/view-reservation";
        }

    }
}
