package uz.pdp.simpletelegrambot.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Entity(name = "basket")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = "user")

public class BasketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


//    @ManyToMany(mappedBy = "basket", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
//    @ManyToMany(mappedBy = "basket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductEntity> products;


    @Column(name = "user_chat_id")
    private Long userChatId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_chat_id", insertable = false, updatable = false)
    private UserEntity user;


}
