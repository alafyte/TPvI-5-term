package by.belstu.lab02.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomRequest {
    private Integer id;
    private String name_type;
    private String info;
    private float price;

}
