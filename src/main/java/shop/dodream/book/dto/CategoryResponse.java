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
    private Long category_id;
    private String category_name;
    private Long depth;
    private Long parent_id;
}
