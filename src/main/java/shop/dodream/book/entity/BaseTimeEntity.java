package shop.dodream.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import shop.dodream.book.core.listener.CustomEntityListener;

import java.time.ZonedDateTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(CustomEntityListener.class)
public class BaseTimeEntity {
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private ZonedDateTime updatedAt;
}