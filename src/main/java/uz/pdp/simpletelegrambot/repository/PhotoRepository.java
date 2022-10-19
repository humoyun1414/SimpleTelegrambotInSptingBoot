package uz.pdp.simpletelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.simpletelegrambot.entity.PhotoEntity;
import uz.pdp.simpletelegrambot.entity.ProductEntity;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity,Integer> {


}
