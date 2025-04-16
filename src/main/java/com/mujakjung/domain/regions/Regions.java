package com.mujakjung.domain.regions;


import com.mujakjung.domain.course.Entity.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class Regions {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
}
