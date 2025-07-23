package shop.dodream.book.core.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import shop.dodream.book.entity.BaseTimeEntity;

import java.time.ZonedDateTime;

public class CustomEntityListener {
    @PrePersist
    public void prePersist(Object o) {
        if(o instanceof BaseTimeEntity baseTimeEntity){
            baseTimeEntity.setCreatedAt(ZonedDateTime.now());
            baseTimeEntity.setUpdatedAt(ZonedDateTime.now());
        }
    }

    @PreUpdate
    public void preUpdate(Object o){
        if (o instanceof BaseTimeEntity baseTimeEntity){
            baseTimeEntity.setUpdatedAt(ZonedDateTime.now());
        }
    }
}
