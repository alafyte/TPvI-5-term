package by.belstu.lab02.contollers;


import by.belstu.lab02.dto.TypeRoomRequest;
import by.belstu.lab02.models.TypeRoom;
import by.belstu.lab02.services.TypeRoomServices;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping
public class TypeRoomsController {
    @Autowired
    private LocalValidatorFactoryBean validator;

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
        try {
            ResponseEntity<?> errorMessages = validateTypeRoomRequest(typeRoomRequest);
            if (errorMessages != null) return errorMessages;

            String name = typeRoomRequest.getName_type();
            String description = typeRoomRequest.getInfo();
            float price = typeRoomRequest.getPrice();
            TypeRoom newTypeRoom = new TypeRoom(name, description, price);
            typeRoomsServices.saveTypeRooms(newTypeRoom);
            log.info("/create-type-room POST");
            return new ResponseEntity<>(newTypeRoom, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при создании типа номера");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
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
        try {
            ResponseEntity<?> errorMessages = validateTypeRoomRequest(typeRoomRequest);
            if (errorMessages != null) return errorMessages;

            Integer idnew = typeRoomRequest.getId();
            String name = typeRoomRequest.getName_type();
            String description = typeRoomRequest.getInfo();
            float price = typeRoomRequest.getPrice();
            TypeRoom newTypeRoom = new TypeRoom(idnew, name, description, price);
            typeRoomsServices.updateTypeRooms(idnew, newTypeRoom);
            log.info("/edit-type-room POST");
            return new ResponseEntity<>(newTypeRoom, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("Произошла ошибка при изменении типа номера");
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
    }

    private ResponseEntity<?> validateTypeRoomRequest(@RequestBody TypeRoomRequest typeRoomRequest) {
        Set<ConstraintViolation<TypeRoomRequest>> violations = validator.validate(typeRoomRequest);

        if (!violations.isEmpty()) {
            List<String> errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errorMessages, HttpStatus.OK);
        }
        return null;
    }


    @GetMapping("/delete-type-room/{id}")
    public String deleteTypeRoom(ModelAndView modelAndView, @PathVariable int id) {
        try {
            typeRoomsServices.deleteTypeRooms(id);
            log.info("/delete-type-room GET");
            return "redirect:/view-type-rooms";
        } catch (Exception e) {
            return "redirect:/view-type-rooms";
        }
    }
}
