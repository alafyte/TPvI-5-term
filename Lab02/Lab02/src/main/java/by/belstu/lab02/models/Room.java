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

    @Column
    String photo;

    @ManyToOne
    @JoinColumn(name = "type_of_rooms")
    TypeRoom typeOfRooms;

    @Column
    int countOfPlaces;

    public Room(int number, String photo, TypeRoom typeOfRooms, int countOfPlaces) {
        this.number = number;
        this.photo = photo;
        this.typeOfRooms = typeOfRooms;
        this.countOfPlaces = countOfPlaces;
    }

    public float getPrice() {
        return countOfPlaces * 10 + typeOfRooms.getPrice();
    }

}
