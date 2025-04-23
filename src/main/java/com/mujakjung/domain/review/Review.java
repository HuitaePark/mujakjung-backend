package com.mujakjung.domain.review;


import com.mujakjung.domain.course.Entity.CourseDetail;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String content;

    private LocalDateTime create_date;

    @ManyToOne
    @JoinColumn(name = "course_detail_id")
    private CourseDetail courseDetail;

    public void updateContent(String content,LocalDateTime update_date){
        this.content = content;
        this.create_date = update_date;
    }

}
