package com.example.parking.controller;
import com.example.parking.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@RestController @RequestMapping("/api/revenue")
public class RevenueController {
    private final BookingService service; public RevenueController(BookingService service){this.service=service;}
    @GetMapping public RevenueResponse byDate(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){return new RevenueResponse(date,service.revenue(date));}
    public record RevenueResponse(LocalDate date, BigDecimal totalRevenue){}
}
