package com.example.parking.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
public record BookingRequest(
    @NotBlank @Pattern(regexp="(?i)^[A-Z]{2}[- ]?[0-9]{1,2}[- ]?[A-Z]{0,3}[- ]?[0-9]{1,4}$", message="Vehicle number must resemble MH12AB1234") String vehicleNumber,
    @Positive Long slotId,
    LocalDateTime entryTime
) {}
