package com.mujakjung.domain.share;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attractionType; //'ATTRACTION', 'ACCOMMODATION', 'RESTAURANT' 값을 가짐
    private Long attractionId;
    private String attractionName;
    private LocalDateTime shareDate;

    @PrePersist
    protected void onCreate() {
        this.shareDate = LocalDateTime.now();
    }
}
