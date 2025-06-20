package com.mujakjung.domain.share.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,        // 1. 타입을 이름("course") 대신 전체 클래스 경로로 저장
        include = JsonTypeInfo.As.PROPERTY, // 2. 타입을 JSON 속성으로 포함
        property = "@class"                 // 3. 속성 이름을 '@class'로 지정 (핵심)
)
@JsonSubTypes({
        // Id.CLASS를 사용하면 Jackson이 클래스를 직접 알 수 있어 name 속성이 필요 없습니다.
        @JsonSubTypes.Type(value = HotCourseDto.class),
        @JsonSubTypes.Type(value = HotRestaurantDto.class),
        @JsonSubTypes.Type(value = HotAccommodationDto.class)
})
public interface HotAttractionDto extends Serializable {
}
