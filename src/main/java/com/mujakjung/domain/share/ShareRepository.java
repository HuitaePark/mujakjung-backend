package com.mujakjung.domain.share;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShareRepository extends JpaRepository<Share,Long> {
    @Query(value = """
      SELECT s.attractionId AS attractionId
      FROM share s
      WHERE s.attractionType = :type
      GROUP BY s.attractionId
      ORDER BY COUNT(s.id) DESC
      LIMIT 1
  """, nativeQuery = true)
    Long findTopAttractionIdByType(@Param("type") String type);
}
