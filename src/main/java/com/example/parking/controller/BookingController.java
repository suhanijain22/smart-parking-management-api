package com.example.parking.controller;
import com.example.parking.dto.BookingRequest;
import com.example.parking.dto.BookingResponse;
import com.example.parking.dto.CheckoutRequest;
import com.example.parking.model.Booking;
import com.example.parking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@RestController @RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service; public BookingController(BookingService service){this.service=service;}
    @PostMapping("/entry") @ResponseStatus(HttpStatus.CREATED) public BookingResponse entry(@Valid @RequestBody BookingRequest request){return BookingResponse.from(service.create(request));}
    @GetMapping("/active") public List<BookingResponse> active(){return service.active().stream().map(BookingResponse::from).toList();}
    @GetMapping("/{id}") public BookingResponse get(@PathVariable Long id){return BookingResponse.from(service.get(id));}
    @GetMapping("/{id}/fare") public FareResponse fare(@PathVariable Long id,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime at){return new FareResponse(id,at==null?LocalDateTime.now():at,service.fare(id,at));}
    @PostMapping("/{id}/checkout") public BookingResponse checkout(@PathVariable Long id,@Valid @RequestBody(required=false) CheckoutRequest request){return BookingResponse.from(service.checkout(id,request==null?null:request.exitTime()));}
    public record FareResponse(Long bookingId, LocalDateTime calculatedAt, BigDecimal amount){}
}
