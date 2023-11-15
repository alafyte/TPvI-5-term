package by.belstu.lab02.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TYPE_ROOM")
public class TypeRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String nameType;

    @Column
    String info;

    @Column
    float price;

    public String toString(){
        return nameType;
    }
}
