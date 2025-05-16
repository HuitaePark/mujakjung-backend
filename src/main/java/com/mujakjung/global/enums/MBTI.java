package com.mujakjung.global.enums;

import lombok.Getter;

@Getter
public enum MBTI {
    ISTJ("ISTJ"),
    ISFJ("ISFJ"),
    INFJ("INFJ"),
    INTJ("INTJ"),
    ISTP("ISTP"),
    ISFP("ISFP"),
    INFP("INFP"),
    INTP("INTP"),
    ESTP("ESTP"),
    ESFP("ESFP"),
    ENFP("ENFP"),
    ENTP("ENTP"),
    ESTJ("ESTJ"),
    ESFJ("ESFJ"),
    ENFJ("ENFJ"),
    ENTJ("ENTJ");

    private final String value;

    MBTI(String value) {
        this.value = value;
    }

    public static MBTI fromString(String value) {
        for(MBTI mbti : MBTI.values()){
            if (mbti.value.equalsIgnoreCase(value)) {
                return mbti;
            }
        }
        throw new IllegalArgumentException("Unknown MBTI type: " + value);
    }
}