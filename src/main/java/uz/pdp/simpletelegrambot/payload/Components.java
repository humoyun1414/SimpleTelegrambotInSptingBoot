package uz.pdp.simpletelegrambot.payload;

import org.springframework.stereotype.Component;
import uz.pdp.simpletelegrambot.entity.ProductEntity;
import uz.pdp.simpletelegrambot.enums.ProductStatus;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.Map;


public interface Components {
    String ADMIN = "5646443541";
    String ADMIN2 = "689214434";
    Map<String, ProductStatus> PRODUCT_STATUS_MAP_UPDATE = new HashMap<>();
    Map<String, ProductStatus> PRODUCT_STATUS_MAP = new HashMap<>();
    Map<String, ProductEntity> PRODUCT_ENTITY_MAP_UPDATE = new HashMap<>();
    Map<String, ProductEntity> PRODUCT_ENTITY_MAP = new HashMap<>();
}
