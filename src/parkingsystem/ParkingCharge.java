/* 
Programmer: David Beltran
File: ParkingCharge.java
*/
/*
 This file contains the ParkingCharge class for the Parking System 
 that creates a charge for each time a Car object enters and exits
 a parking lot in the Parking System
*/
package parkingsystem;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingCharge {
  private final String lotId;
  private final long parkTime;// included to record total time in parking lot
  private final long overnight;// included to record number of nights parked overnight
  private final Money amount;
  private final LocalDateTime date; //added to track day 
  private final Car car;// added to help getParkingChargesC in ParkingOffice
  
  public static class Builder {
    private Car car;
    private Money amount;
    private LocalDateTime date;
    private String givenLot = "";
    private long parkTime = 0;
    private long overnight = 0;
    
    public Builder(Car car, Money amount, LocalDateTime date) {
      this.car = car;
      this.amount = amount;
      this.date = date;
    }
    
    public Builder setLot(String givenLot) {
      this.givenLot = givenLot;
      return this;
    }
    
    public Builder setParkTime(long parkTime) {
      this.parkTime = parkTime;
      return this;
    }
    
    public Builder setOvernight(long overnight) {
      this.overnight = overnight;
      return this;
    }
    
    public ParkingCharge build() {
      return new ParkingCharge(this);
    }
  }
  
  private ParkingCharge(Builder builder) {
    this.car = builder.car;
    this.amount = builder.amount;
    this.date = builder.date;
    this.lotId = builder.givenLot;
    this.parkTime = builder.parkTime;
    this.overnight = builder.overnight;
  }
  
  /*
  hashCode() and equals() overriden to allow comparison of object values and 
  not memory location
 */
  @Override
  public int hashCode() {
    return Objects.hash(amount, lotId, overnight, parkTime, car.getPermitId());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ParkingCharge)) {
      return false;
    }
    ParkingCharge other = (ParkingCharge) obj;
    return Objects.equals(amount, other.amount) && Objects.equals(lotId, other.lotId)
        && overnight == other.overnight && parkTime == other.parkTime
        && Objects.equals(car.getPermitId(), other.getPermitId());
  }

  public String toString() {
    return "Charge [car ID = " + this.car.getPermitId() + ", lot ID = " + lotId + 
        ", parking time = "+ parkTime + ", nights = " + overnight + ", amount due = " 
        + amount.toString() + "]" ;
  }
  
  // Retrieves Car object permit ID associated to instant of ParkingCharge
  public String getPermitId() {
    return this.car.getPermitId();
  }
  
  // Retrieves Money object associated to instant of ParkingCharge
  public Money getMoney() {
    return this.amount;
  }
  
  public LocalDateTime getDate() {
    return this.date;
  }
  
  public Car getCar() {
    return this.car;
  }
}
