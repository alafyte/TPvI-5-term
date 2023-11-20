package by.belstu.lab02.models;

import lombok.*;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "APP_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne
    @JoinColumn(name = "worker_id")
    Worker worker;

    @Column
    String login;

    @Column
    String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role roles;

    public User(String login, String encode) {
        this.login = login;
        this.password = encode;
    }

}
