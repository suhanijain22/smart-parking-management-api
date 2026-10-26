package com.example.parking.service;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
class BillingServiceTest {
 private final BillingService billing=new BillingService();
 private final LocalDateTime start=LocalDateTime.of(2026,1,1,10,0);
 @Test void zeroDurationIsFree(){assertEquals("0.00",billing.calculateFee(start,start).toPlainString());}
 @Test void twoHoursFifteenMinutesCostsFifty(){assertEquals("50.00",billing.calculateFee(start,start.plusMinutes(135)).toPlainString());}
 @Test void exactThreeHoursCostsFifty(){assertEquals("50.00",billing.calculateFee(start,start.plusHours(3)).toPlainString());}
 @Test void oneSecondOverThreeHoursCostsOneHundred(){assertEquals("100.00",billing.calculateFee(start,start.plusHours(3).plusSeconds(1)).toPlainString());}
 @Test void sixHoursTwentyMinutesCostsOneHundredFifty(){assertEquals("150.00",billing.calculateFee(start,start.plusHours(6).plusMinutes(20)).toPlainString());}
 @Test void endBeforeStartRejected(){assertThrows(IllegalArgumentException.class,()->billing.calculateFee(start,start.minusSeconds(1)));}
 @Test void nullTimestampsRejected(){assertThrows(IllegalArgumentException.class,()->billing.calculateFee(null,start));}
}
