package com.mujakjung.domain.attraction.restaurant;


import com.mujakjung.domain.attraction.Attraction;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("RESTAURANT")
public class Restaurant extends Attraction {
    @Column(name = "address")
    private String address;
    private String websiteLink;
}