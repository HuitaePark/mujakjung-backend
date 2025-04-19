package com.mujakjung.domain.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReivewSaveDto {
    private Long courseId;
    private String username;
    private String password;
    private String content;
    private LocalDateTime create_time;
}
