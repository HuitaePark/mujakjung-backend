package com.mujakjung.domain.attraction.course;

import com.mujakjung.domain.attraction.course.Entity.Course;
import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.Entity.CourseDetailLike;
import com.mujakjung.domain.attraction.course.dto.CourseApiResponse;
import com.mujakjung.domain.attraction.course.dto.DetailCourseResponseDto;
import com.mujakjung.domain.attraction.course.repository.CourseDetailLikeRepository;
import com.mujakjung.domain.attraction.course.repository.CourseDetailRepository;
import com.mujakjung.domain.attraction.course.repository.CourseRepository;
import com.mujakjung.global.enums.MBTI;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final CourseMapper courseMapper;
    private final CourseDetailLikeRepository likeRepo;
    /*
        랜덤 코스 조회 카테고리 메서드
    */
    public CourseApiResponse findCourse() {
        //랜덤으로 아무 코스나 선택
        Long courseId = courseRepository.findRandomCourseId();
        log.info("랜덤 코스 선택 결과: {}", courseId);
        //dto들의 리스트로 변환후 json으로 만들어서 반환
        List<DetailCourseResponseDto> courseList = courseMapper.courseToDto(findDetailCourse(courseId));

        return makeResponse(courseId, courseList);
    }

    /*
        MBTI입력을 받아서 코스를 조회하는 메서드
     */
    public CourseApiResponse findMbtiCourse(String type) {

        MBTI mbti = MBTI.fromString(type);
        log.info("요청받은: {}", mbti.getValue());
        Long courseId = courseRepository.findMbtiCourseId(mbti.getValue());
        log.info("MBTI 코스 선택 결과: {}", courseId);

        //dto들의 리스트로 변환후 json으로 만들어서 반환
        List<DetailCourseResponseDto> courseList = courseMapper.courseToDto(findDetailCourse(courseId));

        return makeResponse(courseId, courseList);

    }

    /*
        Region입력을 받아서 코스를 조회하는 메서드
     */
    public CourseApiResponse findRegionCourse(String area) {

        Long courseId = courseRepository.findRegionCourseId(area);
        log.info("지역 코스 선택 결과: {}", courseId);

        //dto들의 리스트로 변환후 json으로 만들어서 반환
        List<DetailCourseResponseDto> courseList = courseMapper.courseToDto(findDetailCourse(courseId));

        return makeResponse(courseId, courseList);
    }

    /*
    계절별 코스를 조회하는 메서드
    */
    public CourseApiResponse findSummerCourse(String season) {

        Long courseId = courseRepository.findSeasonCourseId(season);
        log.info("{} 코스 선택 결과: {}",season,courseId);

        //dto들의 리스트로 변환후 json으로 만들어서 반환
        List<DetailCourseResponseDto> courseList = courseMapper.courseToDto(findDetailCourse(courseId));

        return makeResponse(courseId, courseList);
    }




    /*
        코스를 받아서 세부 코스를 조회하는 메서드
    */
    private List<CourseDetail> findDetailCourse(Long courseId) {
        return courseDetailRepository.findByCourseId(courseId);
    }

    /*
        받은 세부코스들로 Response를 생성하는 메서드
    */
    private CourseApiResponse makeResponse(Long id, List<DetailCourseResponseDto> list) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 코스"));

        return new CourseApiResponse(course.getName(), course.getRegion(), course.getLatitude(), course.getLongitude(),
                course.getImgPath(), list, list.size());
    }
    /*
    좋아요 버튼 기능
     */
    @Transactional
    public boolean likeCourse(Long detailId,String ip){
        //이미 좋아요 눌렀는지 체크
        if(likeRepo.existsByCourseDetailIdAndIp(detailId,ip)){
            return true;
        }
        // 로그 저장
        CourseDetailLike log = CourseDetailLike.builder()
                .courseDetailId(detailId)
                .ip(ip)
                .likedAt(LocalDateTime.now())
                .build();
        likeRepo.save(log);

        //좋아요 카운트
        courseDetailRepository.plusLikeCount(detailId);
        return false;
    }

    public boolean findLikeCourse(Long detailId,String ip){
        return likeRepo.existsByCourseDetailIdAndIp(detailId, ip);
    }


    public long getLikeCount(Long detailId){
        // 좋아요 집계는 detail 테이블의 likeCount 칼럼이나
        // 또는 로그 테이블을 직접 조회해도 되고, 둘 중 하나를 선택
        return courseDetailRepository.findById(detailId)
                .map(CourseDetail::getLikeCount)
                .orElse(0);
    }

}
