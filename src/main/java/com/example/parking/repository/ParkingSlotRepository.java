package com.example.parking.repository;
import com.example.parking.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByOccupiedFalseOrderBySlotCodeAsc();
    List<ParkingSlot> findByOccupiedTrueOrderBySlotCodeAsc();
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select s from ParkingSlot s where s.id = :id") Optional<ParkingSlot> findByIdForUpdate(@Param("id") Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select s from ParkingSlot s where s.occupied = false order by s.slotCode asc") List<ParkingSlot> findAvailableForUpdate();
}
