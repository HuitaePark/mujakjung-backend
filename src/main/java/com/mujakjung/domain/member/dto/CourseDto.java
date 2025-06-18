package com.mujakjung.domain.member.dto;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class CourseDto{
    private Long id;
    private String name;
    private String description;
    private String imgPath;
    private Integer likeCount;

    public static CourseDto from(CourseDetail course) {
        return new CourseDto(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getImgPath(),
                course.getLikeCount()
        );
    }
}
