package com.mujakjung.domain.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReivewSaveDto {
    private String username;
    private String content;
    private LocalDateTime create_time;
}
