package uz.pdp.simpletelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.simpletelegrambot.entity.ProductEntity;
import uz.pdp.simpletelegrambot.enums.ProductStatus;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {


    boolean existsByProductStatus(ProductStatus status);

}
