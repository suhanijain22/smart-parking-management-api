package com.example.parking.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class BillingService {
    private static final long BLOCK_MINUTES = 180;
    private static final BigDecimal BLOCK_FEE = new BigDecimal("50.00");
    public BigDecimal calculateFee(LocalDateTime entryTime, LocalDateTime endTime) {
        if (entryTime == null || endTime == null) throw new IllegalArgumentException("Entry and end timestamps are required");
        if (endTime.isBefore(entryTime)) throw new IllegalArgumentException("End time cannot be before entry time");
        long seconds = Duration.between(entryTime, endTime).getSeconds();
        if (seconds <= 0) return BigDecimal.ZERO.setScale(2);
        long blockSeconds = BLOCK_MINUTES * 60;
        long blocks = (seconds + blockSeconds - 1) / blockSeconds;
        return BLOCK_FEE.multiply(BigDecimal.valueOf(blocks)).setScale(2);
    }
}
