package com.yousef.eventbooking.repository;

import com.yousef.eventbooking.entity.EventTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EventTagRepository extends JpaRepository<EventTag, Long> {

    boolean existsByName(String name);
    Set<EventTag> findAllByNameIn(Set<String> tags);
}