package com.mujakjung.global.enums;

import lombok.Getter;

@Getter
public enum Category {
    MBTI("MBTI별 추천받기"),
    SUMMER("여름방학 여행지 추천받기"),
    WINTER("겨울방학 여행지 추천받기"),
    REGION("지역별 여행지 추천받기"),
    RANDOM("모두 랜덤으로 추천받기");

    private final String name;

    Category(String name) {
        this.name = name;
    }

}
