package com.mujakjung.domain.attraction.accommodation;

import com.mujakjung.domain.attraction.Attraction;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("ACCOMMODATION")
public class Accommodation extends Attraction {
    private String address;
    private String websiteLink;
}
