package by.belstu.lab02.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomRequest {
    private Integer id;

    @NotBlank(message = "Название типа обязательно")
    private String name_type;

    @NotBlank(message = "Описание обязательно")
    private String info;

    @PositiveOrZero(message = "Цена не может быть меньше 0")
    private float price;

}
