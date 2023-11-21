package by.belstu.lab02.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRoomForm {
        private Integer id;
        private String nameType;
        private String info;
        private float price;
        public String toString() {
            return nameType;
        }

}
