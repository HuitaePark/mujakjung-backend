package com.mujakjung.domain.attraction.accommodation;

import com.mujakjung.domain.attraction.restaurant.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationRepository extends JpaRepository<Accommodation,Long> {
    @Query(value = "select a.name from Accommodation a where a.id = :id")
    String findNameById(@Param("id")Long id);

    @Query(value = "SELECT * FROM Attraction a WHERE a.type = 'ACCOMMODATION' AND region = :region ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Accommodation> findRegionAccommodations(@Param("region")String region);
}
