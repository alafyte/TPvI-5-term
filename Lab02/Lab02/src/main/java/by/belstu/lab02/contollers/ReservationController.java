package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.ReservationRequest;
import by.belstu.lab02.models.*;
import by.belstu.lab02.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
        Room freeRoom = getFreeRoom(reservationRequest.getType_room_id(), reservationRequest.getDate_in(), reservationRequest.getDate_out(), reservationRequest.getGuests_count());
        Guest newGuest = guestServices.findGuest(reservationRequest.getGuest());
        if (freeRoom != null) {
            Reservation reservation = new Reservation();
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
        }
        log.info("/create-reservation POST");

        return new ResponseEntity<>(HttpStatus.OK);
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
        Reservation reservation = new Reservation();
        Reservation oldReservation = reservationServices.findById(reservationRequest.getId());
        if (Objects.equals(oldReservation.getDateIn(), reservationRequest.getDate_in()) && Objects.equals(oldReservation.getDateOut(), reservationRequest.getDate_out())) {
            reservation.setRoom(oldReservation.getRoom());
        } else {
            reservation.setRoom(getFreeRoom(reservationRequest.getType_room_id(), reservationRequest.getDate_in(), reservationRequest.getDate_out(), reservationRequest.getGuests_count()));
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
    }


    @GetMapping(value = {"/delete-reservation/{id}"})
    public String DeleteReservation(Model model, @PathVariable("id") int id) {
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
    }
}
