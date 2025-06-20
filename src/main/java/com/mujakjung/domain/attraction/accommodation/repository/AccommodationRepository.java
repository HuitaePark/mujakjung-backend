package com.mujakjung.domain.attraction.accommodation.repository;

import com.mujakjung.domain.attraction.accommodation.entity.Accommodation;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationRepository extends JpaRepository<Accommodation,Long> {
    @Query(value = "select a.name from Accommodation a where a.id = :id")
    String findNameById(@Param("id")Long id);

    @Query("SELECT a FROM Accommodation a WHERE a.region LIKE CONCAT(:region, '%')")
    List<Accommodation> findRegionAccommodations(@Param("region") String region, Pageable pageable);

    @Modifying
    @Query("UPDATE Accommodation a SET a.likeCount = a.likeCount + 1 WHERE a.id = :accommodationId")
    void plusLikeCount(@Param("accommodationId") Long accommodationId);
}
