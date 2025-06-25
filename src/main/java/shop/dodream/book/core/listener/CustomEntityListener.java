package shop.dodream.book.core.listener;

import jakarta.persistence.PrePersist;
import shop.dodream.book.entity.BaseTimeEntity;

import java.time.ZonedDateTime;

public class CustomEntityListener {
    @PrePersist
    public void prePersist(Object o) {
        if(o instanceof BaseTimeEntity baseTimeEntity){
            baseTimeEntity.setCreatedAt(ZonedDateTime.now());
        }
    }
}
