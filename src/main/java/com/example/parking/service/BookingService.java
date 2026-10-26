
package com.example.parking.service;

import com.example.parking.dto.BookingRequest;
import com.example.parking.exception.BadRequestException;
import com.example.parking.exception.ConflictException;
import com.example.parking.exception.ResourceNotFoundException;
import com.example.parking.model.*;
import com.example.parking.repository.BookingRepository;
import com.example.parking.repository.ParkingSlotRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookings;
    private final ParkingSlotRepository slots;
    private final BillingService billing;

    public BookingService(
            BookingRepository bookings,
            ParkingSlotRepository slots,
            BillingService billing) {
        this.bookings = bookings;
        this.slots = slots;
        this.billing = billing;
    }

    @Transactional
    public Booking create(BookingRequest request) {

        String vehicle = request.vehicleNumber()
                .replaceAll("[ -]", "")
                .toUpperCase();

        if (bookings.existsByVehicleNumberAndStatus(
                vehicle, BookingStatus.ACTIVE)) {
            throw new ConflictException(
                    "This vehicle already has an active booking");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime entry = request.entryTime() == null
                ? now
                : request.entryTime();

        if (entry.isAfter(now)) {
            throw new BadRequestException(
                    "Entry time cannot be in the future");
        }

        ParkingSlot slot;

        if (request.slotId() != null) {
            slot = slots.findByIdForUpdate(request.slotId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Parking slot " + request.slotId()
                                            + " was not found"));
        } else {
            List<ParkingSlot> available =
                    slots.findAvailableForUpdate();

            if (available.isEmpty()) {
                throw new ConflictException(
                        "No parking slots are currently available");
            }

            slot = available.get(0);
        }

        if (slot.isOccupied()) {
            throw new ConflictException(
                    "Parking slot " + slot.getSlotCode()
                            + " is already occupied");
        }

        slot.setOccupied(true);
        slots.save(slot);

        Booking booking = new Booking();
        booking.setVehicleNumber(vehicle);
        booking.setSlot(slot);
        booking.setEntryTime(entry);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setPaymentStatus(PaymentStatus.PENDING);

        return bookings.save(booking);
    }

    @Transactional(readOnly = true)
    public List<Booking> active() {
        return bookings.findByStatusOrderByEntryTimeAsc(
                BookingStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Booking get(Long id) {
        return bookings.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking " + id + " was not found"));
    }

    @Transactional(readOnly = true)
    public BigDecimal fare(Long id, LocalDateTime at) {

        Booking booking = get(id);

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            return booking.getFinalFee();
        }

        LocalDateTime end = at == null
                ? LocalDateTime.now()
                : at;

        if (end.isBefore(booking.getEntryTime())) {
            throw new BadRequestException(
                    "Calculation time cannot be before entry time");
        }

        return billing.calculateFee(
                booking.getEntryTime(), end);
    }

    @Transactional
    public Booking checkout(Long id, LocalDateTime requestedExit) {

        Booking booking = bookings.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking " + id + " was not found"));

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new ConflictException(
                    "Booking has already been checked out");
        }

        // Capture the server time once for consistent validation.
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime exit = requestedExit == null
                ? now
                : requestedExit;

        if (exit.isBefore(booking.getEntryTime())) {
            throw new BadRequestException(
                    "Exit time cannot be before entry time");
        }

        if (exit.isAfter(now)) {
            throw new BadRequestException(
                    "Exit time cannot be in the future");
        }

        ParkingSlot slot = slots.findByIdForUpdate(
                        booking.getSlot().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Allocated parking slot was not found"));

        BigDecimal finalFee = billing.calculateFee(
                booking.getEntryTime(), exit);

        booking.setExitTime(exit);
        booking.setFinalFee(finalFee);
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setStatus(BookingStatus.COMPLETED);

        slot.setOccupied(false);
        slots.save(slot);

        return bookings.save(booking);
    }

    @Transactional(readOnly = true)
    public BigDecimal revenue(LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return bookings
                .findByStatusAndExitTimeGreaterThanEqualAndExitTimeLessThan(
                        BookingStatus.COMPLETED, start, end)
                .stream()
                .map(Booking::getFinalFee)
                .filter(fee -> fee != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);
    }
}
