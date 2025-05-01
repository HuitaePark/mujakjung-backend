package com.mujakjung.domain.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewRequest {
    private Long courseId;
    private String username;
    private String content;
    private String password;
    private LocalDateTime create_time;
}
