package by.belstu.lab02.contollers;


import by.belstu.lab02.models.Room;
import by.belstu.lab02.models.TypeRoom;
import by.belstu.lab02.services.FileUploadService;
import by.belstu.lab02.services.RoomServices;
import by.belstu.lab02.services.TypeRoomServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class RoomController {
    private static final String STATIC_PATH = "/upload/images/";

    @Autowired
    private RoomServices roomServices;
    @Autowired
    private TypeRoomServices typeRoomsServices;
    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping(value = {"/view-rooms"})
    public ModelAndView ViewRooms(Model model) {
        ModelAndView modelAndView = new ModelAndView("ViewRooms");
        List<Room> rooms = roomServices.getRooms();
        model.addAttribute("rooms", rooms);
        log.info("/view-rooms GET");
        return modelAndView;
    }

    @GetMapping("view-rooms-worker")
    public ModelAndView ViewRoomsWorker(Model model) {
        ModelAndView modelAndView = new ModelAndView("ViewRoomsWorker");
        List<Room> rooms = roomServices.getRooms();
        model.addAttribute("rooms", rooms);
        log.info("/view-rooms-worker GET");
        return modelAndView;
    }

    @GetMapping("/create-room")
    public ModelAndView GetRoomForm(Model model) {
        ModelAndView modelAndView = new ModelAndView("CreateRoom");
        List<TypeRoom> typerooms = typeRoomsServices.getTypeRooms();
        modelAndView.addObject("typeroomList", typerooms);
        log.info("/create-room GET");
        return modelAndView;
    }

    @PostMapping(path = "/create-room", consumes = "multipart/form-data")
    public ResponseEntity<?> SaveRoom(@RequestParam("photo") MultipartFile file,
                                      @RequestParam("number") int number,
                                      @RequestParam("count_places") int count_places,
                                      @RequestParam("id_type_rooms") int id_type_rooms
    ) {

        ResponseEntity<?> errorMessages = validateRoomRequest(number, count_places, id_type_rooms);
        if (errorMessages != null) return errorMessages;

        String fileName = STATIC_PATH + fileUploadService.storeFile(file);
        TypeRoom typeRooms = typeRoomsServices.findTypeRooms(id_type_rooms);
        Room newRoom = new Room(number, fileName, typeRooms, count_places);
        roomServices.saveRoom(newRoom);

        log.info("/create-room POST");
        return new ResponseEntity<>(newRoom, HttpStatus.OK);
    }


    @GetMapping(value = {"/edit-room/{id}"})
    public ModelAndView EditRoom(Model model, @PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("EditRoom");
        Room room = roomServices.findRoom(id);
        List<TypeRoom> typerooms = typeRoomsServices.getTypeRooms();
        modelAndView.addObject("typeroomList", typerooms);
        model.addAttribute("room", room);
        log.info("/edit-room GET");
        return modelAndView;
    }

    @PostMapping(value = {"/edit-room"})
    public ResponseEntity<?> EditRoom(@RequestParam("photo") MultipartFile file,
                                      @RequestParam("id") int id,
                                      @RequestParam("number") int number,
                                      @RequestParam("count_places") int count_places,
                                      @RequestParam("id_type_rooms") int id_type_rooms
    ) {
        ResponseEntity<?> errorMessages = validateRoomRequest(number, count_places, id_type_rooms);
        if (errorMessages != null) return errorMessages;

        Room oldRoom = roomServices.findRoom(id);
        TypeRoom typeRooms = typeRoomsServices.findTypeRooms(id_type_rooms);
        String fileName = "";

        if (Objects.equals(file.getOriginalFilename(), "")) {
            fileName = oldRoom.getPhoto();
        } else {
            fileUploadService.deleteFileByName("." + oldRoom.getPhoto());
            fileName = STATIC_PATH + fileUploadService.storeFile(file);
        }

        Room newRoom = new Room(id, number, fileName, typeRooms, count_places);
        roomServices.updateRoom(id, newRoom);

        log.info("/edit-room POST");
        return new ResponseEntity<>(newRoom, HttpStatus.OK);
    }

    private ResponseEntity<?> validateRoomRequest(@RequestParam("number") int number, @RequestParam("count_places") int count_places, @RequestParam("id_type_rooms") int id_type_rooms) {
        if (number <= 0 || count_places <= 0 || id_type_rooms <= 0) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Числовые значения не могут быть меньше 0");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
        return null;
    }


    @GetMapping(value = {"/delete-room/{id}"})
    public String DeleteRoom(Model model, @PathVariable int id) {
        try {
            roomServices.deleteRoom(id);
            log.info("/delete-room GET");
            return "redirect:/view-rooms";
        } catch (Exception e) {
            return "redirect:/view-rooms";
        }
    }

}
