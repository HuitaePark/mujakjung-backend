package com.mujakjung.domain.attraction.accommodation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccmmodationRepository extends JpaRepository<Accommodation,Long> {
    @Query(value = "select a.name from Accommodation a where a.id = :id")
    String findNameById(@Param("id")Long id);
}
