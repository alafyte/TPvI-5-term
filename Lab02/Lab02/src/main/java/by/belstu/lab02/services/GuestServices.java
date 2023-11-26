package by.belstu.lab02.services;


import by.belstu.lab02.models.Guest;
import by.belstu.lab02.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestServices {
    @Autowired
    GuestRepository guestRepository;
    public void saveGuest(Guest guest){
        guestRepository.save(guest);
    }

    public void deleteGuest(int id){
        guestRepository.deleteById(id);
    }

    public Guest findGuest(int id){
        return guestRepository.findById(id).orElse(null);
    }

    public void updateGuest(Guest guest){
        guestRepository.save(guest);
    }

    public List<Guest> getGuests(){
        return Streamable.of(guestRepository.findAll()).toList();
    }

}
