package com.mujakjung.domain.review;

import com.mujakjung.domain.course.repository.CourseDetailRepository;
import com.mujakjung.domain.review.dto.ReivewSaveDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReviewService {

    private final CourseDetailRepository courseDetailRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public void saveReview(ReivewSaveDto dto){
        Review review = reviewMapper.toEntity(dto,courseDetailRepository);
        reviewRepository.save(review);
    }

}
