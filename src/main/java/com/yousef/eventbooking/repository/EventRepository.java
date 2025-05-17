package com.yousef.eventbooking.repository;

import com.yousef.eventbooking.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitle(String title);

    Optional<Event> findEventByTitle(String title);


    @Query("""
    SELECT e FROM Event e
    WHERE (:name IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:venueId IS NULL OR e.venue.id = :venueId)
      AND (:startDate IS NULL OR e.startDatetime >= :startDate)
      AND (:endDate IS NULL OR e.endDatetime <= :endDate)
      AND (:categoryId IS NULL OR e.category.category_id = :categoryId)
      AND (:tag IS NULL OR EXISTS (
            SELECT t FROM e.tags t WHERE LOWER(t.name) = LOWER(:tag)
      ))
""")
    Page<Event> findEventWithFilters(
            @Param("name") String name,
            @Param("venueId") Long venueId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("categoryId") Long categoryId,
            @Param("tag") String tag,
            Pageable pageable);








}
