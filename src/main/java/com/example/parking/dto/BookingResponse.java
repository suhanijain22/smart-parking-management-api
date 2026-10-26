package com.example.parking.dto;
import com.example.parking.model.Booking;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record BookingResponse(Long id, String vehicleNumber, Long slotId, String slotCode, LocalDateTime entryTime, LocalDateTime exitTime, String status, String paymentStatus, BigDecimal finalFee) {
    public static BookingResponse from(Booking b) { return new BookingResponse(b.getId(), b.getVehicleNumber(), b.getSlot().getId(), b.getSlot().getSlotCode(), b.getEntryTime(), b.getExitTime(), b.getStatus().name(), b.getPaymentStatus().name(), b.getFinalFee()); }
}
