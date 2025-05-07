package com.mujakjung.domain.attraction.restaurant;

import com.mujakjung.domain.attraction.Attraction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    @Query(value = "select r.name from Restaurant r where r.id = :id")
    String findNameById(@Param("id")Long id);

    @Query(value = "SELECT * FROM Attraction a WHERE a.type = 'RESTAURANT' AND region = :region ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Restaurant> findRegionRestaurants(@Param("region")String region);

}
