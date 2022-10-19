package uz.pdp.simpletelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.simpletelegrambot.entity.BasketEntity;
import uz.pdp.simpletelegrambot.entity.ProductEntity;
import uz.pdp.simpletelegrambot.entity.UserEntity;

import javax.jws.soap.SOAPBinding;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<BasketEntity, Integer> {

// Optional<UserEntity> findByBasketUser(UserEntity entity);

    @Query(
            value = "select basket_id from product_basket where products_id = :products_id"
            , nativeQuery = true
    )
    List<Integer> findAllByIdNative(Integer products_id);

    @Transactional
    @Modifying
    @Query(
            value = "delete from basket_products where products_id = :products_id", nativeQuery = true
    )
    void deleteByIdNative(Integer products_id);

//    List<Credential> findAllByIdUserFK(Long idUserFK);


//    BasketEntity findById(Integer id);


}
