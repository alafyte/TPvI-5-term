package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.TypeRoomRequest;
import by.belstu.lab02.models.TypeRoom;
import by.belstu.lab02.services.TypeRoomServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping
public class TypeRoomsController {

    @Autowired
    public TypeRoomServices typeRoomsServices;

    @GetMapping("/view-type-rooms")
    public ModelAndView typeRooms(Model model) {
        ModelAndView modelAndView = new ModelAndView("ViewTypeRooms");
        List<TypeRoom> typerooms = typeRoomsServices.getTypeRooms();
        model.addAttribute("typerooms", typerooms);
        log.info("/view-type-rooms GET");
        return modelAndView;
    }

    @GetMapping("/view-type-rooms-worker")
    public ModelAndView typeRoomsWorker(Model model) {
        ModelAndView modelAndView = new ModelAndView("ViewTypeRoomsWorker");
        List<TypeRoom> typerooms = typeRoomsServices.getTypeRooms();
        model.addAttribute("typerooms", typerooms);
        log.info("/view-type-rooms-worker GET");
        return modelAndView;
    }

    @GetMapping("/create-type-room")
    public ModelAndView createTypeRoom(Model model) {
        log.info("/create-type-room GET");
        return new ModelAndView("CreateTypeRoom");
    }

    @PostMapping("/create-type-room")
    public ResponseEntity<?> createTypeRoom(@RequestBody TypeRoomRequest typeRoomRequest) {
        String name = typeRoomRequest.getName_type();
        String description = typeRoomRequest.getInfo();
        float price = typeRoomRequest.getPrice();
        TypeRoom newTypeRoom = new TypeRoom(name, description, price);
        typeRoomsServices.saveTypeRooms(newTypeRoom);
        log.info("/create-type-room POST");
        return new ResponseEntity<>(newTypeRoom, HttpStatus.OK);
    }


    @GetMapping("/edit-type-room/{id}")
    public ModelAndView editTypeRoom(Model model, @PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("EditTypeRoom");
        TypeRoom typeRoom = typeRoomsServices.findTypeRooms(id);
        model.addAttribute("typeroom", typeRoom);
        log.info("/edit-type-room GET");
        return modelAndView;
    }

    @PostMapping("/edit-type-room")
    public ResponseEntity<?> editTypeRoom(@RequestBody TypeRoomRequest typeRoomRequest) {
        Integer idnew = typeRoomRequest.getId();
        String name = typeRoomRequest.getName_type();
        String description = typeRoomRequest.getInfo();
        float price = typeRoomRequest.getPrice();
        TypeRoom newTypeRoom = new TypeRoom(idnew, name, description, price);
        typeRoomsServices.updateTypeRooms(idnew, newTypeRoom);
        log.info("/edit-type-room POST");
        return new ResponseEntity<>(newTypeRoom, HttpStatus.OK);
    }


    @GetMapping("/delete-type-room/{id}")
    public String deleteTypeRoom(ModelAndView modelAndView, @PathVariable int id) {
        typeRoomsServices.deleteTypeRooms(id);
        log.info("/delete-type-room GET");
        return "redirect:/view-type-rooms";
    }

}
