/*
Programmer: David Beltran
File: ParkingEvent.java 
*/

/*
 This file contains the ParkingEvent class. This class holds
 the values needed to create a ParkingCharge object. ParkingEvent
 object is instantiated at ParkingLot's exitLot() method.   
*/
package parkingsystem.observer;

import java.time.LocalDateTime;
import parkingsystem.Car;
import parkingsystem.ParkingLot;

public class ParkingEvent {
  private final long totalSec;
  private final long daysBetween;
  private final ParkingLot lot;
  private final Car car;
  private final LocalDateTime in;
  private final LocalDateTime out;
  
  public static class Builder {
    private long totalSec = 0;
    private long daysBetween = 0;
    private ParkingLot lot;
    private Car car;
    private LocalDateTime in;
    private LocalDateTime out;
    
    public Builder(ParkingLot lot, Car car) {
      this.lot = lot;
      this.car = car;
    }
    
    public Builder setTotalSec(long totalSec) {
      this.totalSec = totalSec;
      return this;
    }
    
    public Builder setDaysBetween(long daysBetween) {
      this.daysBetween = daysBetween;
      return this;
    }
    
    public Builder setEntry(LocalDateTime in) {
      this.in = in;
      return this;
    }
    
    public Builder setExit(LocalDateTime out) {
      this.out = out;
      return this;
    }
    
    public ParkingEvent build() {
      return new ParkingEvent(this);
    }
  }
  
  private ParkingEvent(Builder builder) {
    this.lot = builder.lot;
    this.car = builder.car;
    this.totalSec = builder.totalSec;
    this.daysBetween = builder.daysBetween;
    this.in = builder.in;
    this.out = builder.out;
  }
  
  public ParkingLot getLot() {
    return this.lot;
  }
  
  public Car getCar() {
    return this.car;
  }
  
  public long getTotalSec() {
    return this.totalSec;
  }
  
  public long getDaysBetween() {
    return this.daysBetween;
  }
  
  public LocalDateTime getEntryDay() {
    return this.in;
  }
  
  public LocalDateTime getExitDay() {
    return this.out;
  }
}
