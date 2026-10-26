package com.example.parking.controller;
import com.example.parking.model.ParkingSlot;
import com.example.parking.service.ParkingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/slots")
public class ParkingSlotController {
    private final ParkingService service; public ParkingSlotController(ParkingService service){this.service=service;}
    @GetMapping public List<ParkingSlot> all(){return service.all();}
    @GetMapping("/available") public List<ParkingSlot> available(){return service.available();}
    @GetMapping("/occupied") public List<ParkingSlot> occupied(){return service.occupied();}
    @GetMapping("/{id}") public ParkingSlot get(@PathVariable Long id){return service.get(id);}
}
