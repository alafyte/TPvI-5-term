package by.belstu.lab02.services;

import by.belstu.lab02.models.Room;
import by.belstu.lab02.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.util.List;

@Service
public class RoomServices {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FileUploadService fileUploadService;

    public @ResponseBody List<Room> getRooms() {
        return Streamable.of(roomRepository.findAll()).toList();
    }

    @Transient
    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }

    public Room findRoom(int id) {
        return roomRepository.findById(id).orElse(null);
    }

    public void updateRoom(int id, Room room) {
        Room room1 = roomRepository.findById(id).orElse(null);
        assert room1 != null;
        room1.setNumber(room.getNumber());
        room1.setTypeOfRooms(room.getTypeOfRooms());
        room1.setCountOfPlaces(room.getCountOfPlaces());
        room1.setPhoto(room.getPhoto());
        roomRepository.save(room1);
    }

    public void updateRoom(Room room) {
        roomRepository.save(room);
    }

}

