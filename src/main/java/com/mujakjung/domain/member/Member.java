package com.mujakjung.domain.member;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.Entity.CourseDetailLike;
import com.mujakjung.domain.attraction.course.Entity.MemberDetailLike;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Table(name = "member", catalog = "q")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;
    private String password;

    private String name;

    private String role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MemberDetailLike> likes = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime create_At;

    @UpdateTimestamp
    private LocalDateTime update_At;

}
