package com.mujakjung.domain.attraction.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    @Query(value = "select r.name from Restaurant r where r.id = :id")
    String findNameById(@Param("id")Long id);
}
