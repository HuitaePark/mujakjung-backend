package com.mujakjung.domain.attraction.restaurant;

import com.mujakjung.domain.attraction.restaurant.entity.Restaurant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    @Query(value = "select r.name from Restaurant r where r.id = :id")
    String findNameById(@Param("id")Long id);

    @Query("SELECT r FROM Restaurant r WHERE r.region LIKE CONCAT(:region, '%')")
    List<Restaurant> findRegionRestaurants(@Param("region") String region, Pageable pageable);

}
