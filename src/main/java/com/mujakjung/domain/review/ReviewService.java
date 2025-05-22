package com.mujakjung.domain.review;

import com.mujakjung.domain.attraction.course.repository.CourseDetailRepository;
import com.mujakjung.domain.review.dto.ReivewSaveDto;
import com.mujakjung.domain.review.dto.ReviewDto;
import com.mujakjung.domain.review.dto.ReviewUpdatedto;
import com.mujakjung.global.util.PageResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public void deleteReview(Long id){
        Review review = reviewRepository.findById(id).orElseThrow(()->new IllegalArgumentException("리뷰를 찾을수 없습니다."));
        reviewRepository.delete(review);
    }

    public void updateReview(Long id, ReviewUpdatedto request) {
        Review review = reviewRepository.findById(id).orElseThrow(()->new IllegalArgumentException("리뷰를 찾을수 없습니다."));
        review.updateContent(request.getContent(),request.getUpdate_time());
        reviewRepository.save(review);
    }

    public void passwordCheck(Long id, String password) {
        Review review = reviewRepository.findById(id).orElseThrow(()->new IllegalArgumentException("리뷰를 찾을수 없습니다."));
        String requestPassword = review.getPassword();
        if(!password.equals(requestPassword)){
            throw new IllegalArgumentException("비밀번호가 불일치 합니다.");
        }
    }
    public PageResponse<ReviewDto> findReview(Pageable pageable,Long detailId){
        Page<Review> pages = reviewRepository.findbyCourseDetailId(pageable,detailId);
        Page<ReviewDto> list = pages.map(reviewMapper::toDto);

        return new PageResponse<>(list);
    }
}
