package com.mujakjung.domain.member;

import com.mujakjung.domain.attraction.accommodation.entity.AccommodationLike;
import com.mujakjung.domain.attraction.course.Entity.CourseDetailLike;
import com.mujakjung.domain.attraction.restaurant.entity.RestaurantLike;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Table(name = "member")
public class Member implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nickname;

    private String role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseDetailLike> courseDetailLikes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantLike> restaurantLikes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccommodationLike> accommodationLikes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    public void encryptionPassword(PasswordEncoder encoder){
        this.password = encoder.encode(this.password);
    }
    public void updatePassword(String password,PasswordEncoder encoder){
        this.password = encoder.encode(password);
    }
    public boolean checkPassword(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.password);
    }
}
