package com.example.parking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="bookings", indexes={@Index(name="idx_booking_status", columnList="status"), @Index(name="idx_booking_exit_time", columnList="exit_time")})
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="vehicle_number", nullable=false, length=20) private String vehicleNumber;
    @ManyToOne(fetch=FetchType.EAGER, optional=false) @JoinColumn(name="slot_id", nullable=false) private ParkingSlot slot;
    @Column(name="entry_time", nullable=false) private LocalDateTime entryTime;
    @Column(name="exit_time") private LocalDateTime exitTime;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20) private BookingStatus status = BookingStatus.ACTIVE;
    @Enumerated(EnumType.STRING) @Column(name="payment_status", nullable=false, length=20) private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    @Column(name="final_fee", precision=10, scale=2) private BigDecimal finalFee;
    public Booking() {}
    public Long getId(){return id;} public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){vehicleNumber=v;}
    public ParkingSlot getSlot(){return slot;} public void setSlot(ParkingSlot s){slot=s;}
    public LocalDateTime getEntryTime(){return entryTime;} public void setEntryTime(LocalDateTime t){entryTime=t;}
    public LocalDateTime getExitTime(){return exitTime;} public void setExitTime(LocalDateTime t){exitTime=t;}
    public BookingStatus getStatus(){return status;} public void setStatus(BookingStatus s){status=s;}
    public PaymentStatus getPaymentStatus(){return paymentStatus;} public void setPaymentStatus(PaymentStatus p){paymentStatus=p;}
    public BigDecimal getFinalFee(){return finalFee;} public void setFinalFee(BigDecimal f){finalFee=f;}
}
