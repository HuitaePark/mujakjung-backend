package com.mujakjung.domain.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewUpdateRequest {
    private String content;
    private String password;
    private LocalDateTime update_time;
}
