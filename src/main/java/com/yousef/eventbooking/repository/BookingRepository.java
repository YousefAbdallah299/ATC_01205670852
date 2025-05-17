package com.yousef.eventbooking.repository;

import com.yousef.eventbooking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByEvent_idAndUser_Email(Long eventId, String email);
    Optional<Booking> findByEvent_idAndUser_Email(Long eventId, String email);
    Page<Booking> findAllByUser_Email(String email, Pageable pageable);


}