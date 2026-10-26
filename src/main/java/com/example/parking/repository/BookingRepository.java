package com.example.parking.repository;
import com.example.parking.model.Booking;
import com.example.parking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatusOrderByEntryTimeAsc(BookingStatus status);
    boolean existsByVehicleNumberAndStatus(String vehicleNumber, BookingStatus status);
    List<Booking> findByStatusAndExitTimeGreaterThanEqualAndExitTimeLessThan(BookingStatus status, LocalDateTime start, LocalDateTime end);
}
