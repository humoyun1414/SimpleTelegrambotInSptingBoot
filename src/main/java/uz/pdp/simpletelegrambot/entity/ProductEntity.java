package uz.pdp.simpletelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.MoreObjects;
import lombok.*;
import uz.pdp.simpletelegrambot.enums.ProductStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@RequiredArgsConstructor
@Entity(name = "product")
@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(exclude = "basket")
public class ProductEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @OneToOne
    private PhotoEntity photo;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<BasketEntity> basket;

    private LocalDateTime createdDate = LocalDateTime.now();

//
//    @Override
//    public String toString() {
//        return "Mahsulot :" +
//                "\nid=" + id +
//                ",\nnomi=" + name +
//                ",\nnarxi=" + price +
//                ",\nbo'limi=" + category +
//                ",\nstatus=" + status +
//                ",\ncreated=" + createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//    }
}