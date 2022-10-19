package uz.pdp.simpletelegrambot.entity;

import lombok.Data;
import uz.pdp.simpletelegrambot.enums.UserBotState;

import javax.persistence.*;

@Data
@Entity(name = "users")
public class UserEntity {

    @Id
    @Column(unique = true, nullable = false)
    private Long chatId;

    private String firstName;

    private String lastName;

//    @Enumerated(EnumType.STRING)
//    private UserBotState state;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "basketId")
    private BasketEntity basket;

}
