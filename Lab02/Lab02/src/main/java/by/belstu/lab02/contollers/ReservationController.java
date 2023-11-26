package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.ReservationRequest;
import by.belstu.lab02.models.Guest;
import by.belstu.lab02.models.Reservation;
import by.belstu.lab02.models.Room;
import by.belstu.lab02.models.TypeRoom;
import by.belstu.lab02.services.GuestServices;
import by.belstu.lab02.services.ReservationServices;
import by.belstu.lab02.services.RoomServices;
import by.belstu.lab02.services.TypeRoomServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@RequestMapping
public class ReservationController {
    public final ReservationServices reservationServices;
    public final TypeRoomServices typeRoomServices;
    public final RoomServices roomServices;
    public final GuestServices guestServices;

    public ReservationController(ReservationServices reservationServices, TypeRoomServices typeRoomServices, RoomServices roomServices, GuestServices guestServices) {
        this.reservationServices = reservationServices;
        this.typeRoomServices = typeRoomServices;
        this.roomServices = roomServices;
        this.guestServices = guestServices;
    }

    @GetMapping(value = {"/view-reservation"})
    public ModelAndView ViewReservation(Model model) {
        ModelAndView modelAndView = new ModelAndView("ViewReservation");
        List<Reservation> reservations = reservationServices.findAll();
        modelAndView.addObject("reservations", reservations);
        log.info("/view-reservation GET");
        return modelAndView;
    }

    @GetMapping(value = {"/create-reservation"})
    public ModelAndView CreateReservation(ModelAndView model) {
        ModelAndView modelAndView = new ModelAndView("CreateReservation");
        List<TypeRoom> typeRooms = typeRoomServices.getTypeRooms();
        List<Guest> guests = guestServices.getGuests();
        modelAndView.addObject("typeroomList", typeRooms);
        modelAndView.addObject("guests", guests);
        log.info("/create-reservation GET");
        return modelAndView;
    }

    @PostMapping(value = {"/create-reservation"})
    public ResponseEntity<?> CreateReservation(@RequestBody ReservationRequest reservationRequest) {
        Room freeRoom = getFreeRoom(reservationRequest.getType_room_id(), reservationRequest.getDate_in(), reservationRequest.getDate_out(), reservationRequest.getGuests_count());
        if (freeRoom != null) {
            Reservation reservation = new Reservation();
            Guest newGuest = guestServices.findGuest(reservationRequest.getGuest());
            reservation.setGuest(newGuest);
            reservation.setDateIn(reservationRequest.getDate_in());
            reservation.setDateOut(reservationRequest.getDate_out());
            reservation.setGuestCount(reservationRequest.getGuests_count());
            reservation.setRoom(freeRoom);
            reservationServices.save(reservation);
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
        ModelAndView modelAndView = new ModelAndView("EditReservation");
        Reservation reservation = reservationServices.findById(id);
        model.addAttribute("reservation", reservation);
        List<Guest> guests = guestServices.getGuests();
        List<TypeRoom> typeRooms = typeRoomServices.getTypeRooms();
        modelAndView.addObject("typeroomList", typeRooms);
        modelAndView.addObject("guests", guests);
        log.info("/edit-reservation GET");
        return modelAndView;
    }

    @PostMapping(value = {"/edit-reservation"})
    public ResponseEntity<?> UpdateReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationRequest.getId());
        reservation.setRoom(getFreeRoom(reservationRequest.getType_room_id(), reservationRequest.getDate_in(), reservationRequest.getDate_out(), reservationRequest.getGuests_count()));
        reservation.setDateIn(reservationRequest.getDate_in());
        reservation.setDateOut(reservationRequest.getDate_out());
        reservation.setGuest(guestServices.findGuest(reservationRequest.getGuest()));
        reservation.setGuestCount(reservationRequest.getGuests_count());
        reservationServices.save(reservation);
        log.info("/edit-reservation POST");
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }


    @GetMapping(value = {"/delete-reservation/{id}"})
    public String DeleteReservation(Model model, @PathVariable("id") int id) {
        reservationServices.delete(reservationServices.findById(id));

        log.info("/delete-reservation GET");
        return "redirect:/view-reservation";
    }
}
