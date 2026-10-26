package com.example.parking.service;
import com.example.parking.exception.ResourceNotFoundException;
import com.example.parking.model.ParkingSlot;
import com.example.parking.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ParkingService {
    private final ParkingSlotRepository slots;
    public ParkingService(ParkingSlotRepository slots){this.slots=slots;}
    public List<ParkingSlot> all(){return slots.findAll();}
    public List<ParkingSlot> available(){return slots.findByOccupiedFalseOrderBySlotCodeAsc();}
    public List<ParkingSlot> occupied(){return slots.findByOccupiedTrueOrderBySlotCodeAsc();}
    public ParkingSlot get(Long id){return slots.findById(id).orElseThrow(()->new ResourceNotFoundException("Parking slot "+id+" was not found"));}
}
