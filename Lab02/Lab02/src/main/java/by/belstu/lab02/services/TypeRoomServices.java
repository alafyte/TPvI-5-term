package by.belstu.lab02.services;


import by.belstu.lab02.models.TypeRoom;
import by.belstu.lab02.repositories.TypeRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class TypeRoomServices {
    @Autowired
    TypeRoomRepository typeRoomsRepository;

    public @ResponseBody List<TypeRoom> getTypeRooms() {
        return Streamable.of(typeRoomsRepository.findAll()).toList();
    }

    public void saveTypeRooms(TypeRoom typeRooms) {
        typeRoomsRepository.save(typeRooms);
    }

    public void deleteTypeRooms(int id) {
        typeRoomsRepository.deleteById(id);
    }

    public @ResponseBody TypeRoom findTypeRooms(int id) {
        return typeRoomsRepository.findById(id).orElse(null);
    }

    public void updateTypeRooms(int id, TypeRoom typeRooms) {
        TypeRoom typeRooms1 = typeRoomsRepository.findById(id).orElse(null);
        assert typeRooms1 != null;
        typeRooms1.setNameType(typeRooms.getNameType());
        typeRooms1.setInfo(typeRooms.getInfo());
        typeRooms1.setPrice(typeRooms.getPrice());
        typeRoomsRepository.save(typeRooms1);
    }

}
