package com.mujakjung.domain.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewUpdatedto {
    private String content;
    private LocalDateTime update_time;
}
