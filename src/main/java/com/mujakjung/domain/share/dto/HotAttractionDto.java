package com.mujakjung.domain.share.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,         // 이름 기반 식별
        include = JsonTypeInfo.As.PROPERTY, // JSON 속성으로 삽입
        property = "dtoType"                // 예: "dtoType":"course"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HotCourseDto.class,       name = "course"),
        @JsonSubTypes.Type(value = HotRestaurantDto.class,   name = "restaurant"),
        @JsonSubTypes.Type(value = HotAccommodationDto.class,name = "accommodation")
})
public interface HotAttractionDto {
}
