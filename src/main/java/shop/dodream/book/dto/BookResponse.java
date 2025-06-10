package com.nhnacademy.springbootjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CommentRequest {
    private Long book_id;
    private String title;
    private String description;
    private String author;
    private String publisher;
    private LocalDateTime pushlished_at;
    private String isbn;
    private Long regular_price;
    private STATUS status;
    private Long sale_price;
    private Boolean is_giftable;
    private LocalDateTime created_at;
    private Long search_count;
    private Long view_count;
}
