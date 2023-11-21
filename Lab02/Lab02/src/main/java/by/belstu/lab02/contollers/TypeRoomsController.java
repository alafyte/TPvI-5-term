package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.TypeRoomRequest;
import by.belstu.lab02.forms.TypeRoomForm;
import by.belstu.lab02.models.TypeRoom;
import by.belstu.lab02.services.TypeRoomServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
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

    public final TypeRoomServices typeRoomsServices;

    public TypeRoomsController(TypeRoomServices typeRoomsServices) {
        this.typeRoomsServices = typeRoomsServices;
    }

    @GetMapping("/view-type-rooms")
    public ModelAndView typeRooms(Model model) {
        ModelAndView modelAndView = new ModelAndView("ViewTypeRooms");
        List<TypeRoom> typerooms = typeRoomsServices.getTypeRooms();
        model.addAttribute("typerooms", typerooms);
        return modelAndView;
    }

    @GetMapping("/create-type-room")
    public ModelAndView createTypeRoom(Model model) {
        ModelAndView modelAndView = new ModelAndView("CreateTypeRoom");
        TypeRoomForm typeRoomForm = new TypeRoomForm();
        model.addAttribute("typeroomform", typeRoomForm);
        return modelAndView;
    }

    @PostMapping("/create-type-room")
    public ResponseEntity<?> createTypeRoom(@RequestBody TypeRoomRequest typeRoomRequest) {
        String name = typeRoomRequest.getName_type();
        String description = typeRoomRequest.getInfo();
        float price = typeRoomRequest.getPrice();
        TypeRoom newTypeRoom = new TypeRoom(name, description, price);
        typeRoomsServices.saveTypeRooms(newTypeRoom);

        return new ResponseEntity<>(newTypeRoom, HttpStatus.OK);
    }


    @GetMapping("/edit-type-room/{id}")
    public ModelAndView editTypeRoom(Model model, @PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView("EditTypeRoom");
        TypeRoom typeRoom = typeRoomsServices.findTypeRooms(Integer.parseInt(id));
        model.addAttribute("typeroom", typeRoom);
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

        return new ResponseEntity<>(newTypeRoom, HttpStatus.OK);
    }


    @GetMapping("/delete-type-room/{id}")
    public ModelAndView deleteTypeRoom(ModelAndView modelAndView, @PathVariable String id) {
        typeRoomsServices.deleteTypeRooms(Integer.parseInt(id));
        modelAndView.setViewName("ViewTypeRooms");
        List<TypeRoom> typerooms = typeRoomsServices.getTypeRooms();
        modelAndView.addObject("typerooms", typerooms);
        return modelAndView;
    }

}