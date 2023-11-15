package by.belstu.lab02.models;


import lombok.*;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "ROOM")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    int number;

    @OneToOne
    @JoinColumn(name = "type_of_rooms")
    TypeRoom typeOfRooms;

    @Column
    int countOfPlaces;

    //    public void SetToServices(){
//        RoomServices roomServices = new RoomServices();
//        Room newRoom = roomServices.findRoom(this.id);
//        if (newRoom != null){
//            this.number = newRoom.number;
//            this.type_rooms = newRoom.type_rooms;
//            this.count_places = newRoom.count_places;
//        }
//    }


    public float getPrice() {
        return countOfPlaces * 10 + typeOfRooms.getPrice();
    }

}
