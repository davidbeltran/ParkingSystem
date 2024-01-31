/* 
Programmer: David Beltran
File: ParkingLot.java
*/
/*
 This file contains the ParkingLot class for the Parking System 
 that creates a parking lot object. The parking lot object takes 
 a car object into two of its methods in order to keep track of 
 its capacity variable. 
*/

package parkingsystem;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import parkingsystem.observer.ParkingEvent;
import parkingsystem.observer.ParkingObserver;

public class ParkingLot {
  private String lotId;
  private String address;
  private int openSpots;
  private final int MAXCAPACITY;
  private LotType type;
  private ParkingOffice office;
  private List<ParkingObserver> parkingObservers = new LinkedList<>();//holds list of observers
  
  // ParkingLot constructor
  public ParkingLot(String lotId, String address, int capacity, LotType type, 
      ParkingOffice office) {
    if (capacity < 1) {
      throw new IllegalArgumentException("Parking lot must contain at least one spot.");
    }
    lotId = lotId.toUpperCase();
    this.lotId = lotId;
    this.address = address;
    this.openSpots = capacity;
    this.MAXCAPACITY = capacity;
    this.type = type;
    this.office = office;
  }
  //adds observer object to list of ParkingLot's oberver list. 
  //observer is added when observer object is instantiated.
  public void addObserver(ParkingObserver po) {
    parkingObservers.add(po);
  }
  
  //removes observer from parkingObservers list
  public void removeObserver(ParkingObserver po) {
    parkingObservers.remove(po);
  }
  
  //returns timestamp of when vehicle entered the parking lot
  public LocalDateTime enterLot(String carPermitId) {
    //checks to ensure the permit is registered in the ParkingOffice
    if (!(this.office.getPermitIds().contains(carPermitId))) {
      throw new IllegalArgumentException("Permit " + carPermitId + " is not registered");
    }
    checkIfFull();
    this.openSpots--;
    return LocalDateTime.now();
  }
  
  //captures timestamp of when vehicle exited a parking lot.
  //instantiates ParkingEvent object then sends to observers to create a parking charge
  public void exitLot(LocalDateTime in, LocalDateTime out, String carPermitId) {
    this.openSpots++;
    long totalSec = Math.abs(ChronoUnit.SECONDS.between(in, out));
    long daysBetween = Math.abs(ChronoUnit.DAYS.between(in, out));
    ParkingEvent pe = new ParkingEvent.Builder(this, this.office.getCar(carPermitId))
        .setTotalSec(totalSec)
        .setDaysBetween(daysBetween)
        .setEntry(in)
        .setExit(out)
        .build();
    notifyParkingObservers(pe);
  }
  
  //method that updates every observer in observer list.
  //this update creates a parking charge.
  private void notifyParkingObservers(ParkingEvent pe) {
    for (ParkingObserver observer : this.parkingObservers) {
      observer.update(pe);
    }
  }
  
  /*
  hashCode() and equals() overriden to allow comparison of object values and 
  not memory location. ParkingLot object is the same even if capacity is different.
 */
  @Override
  public int hashCode() {
    return Objects.hash(address, lotId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ParkingLot)) {
      return false;
    }
    ParkingLot other = (ParkingLot) obj;
    return Objects.equals(address, other.address)
        && Objects.equals(lotId, other.lotId);
  }

  // Prints values placed in object variables
  public String toString() {
    return "[lotID = " + this.lotId + ", address = " + this.address 
        + ", capacity = " + this.openSpots + "]";
  }
  
  private void checkIfFull() {
    if (this.openSpots <= 0) {
      throw new IllegalArgumentException("Lot " + this.lotId + " is full.");
    }
  }
  
  // Retrieves ParkingLot object's lotId value
  public String getLotId() {
    return this.lotId;
  }
  
  //Retrieves ParkingLot object's address value
  public String getAddress() {
    return this.address;
  }
  
  //Retrieves ParkingLot object's capacity value
  public int getAvailableSpots() {
    return this.openSpots;
  }
  
  public int getMaxCapacity() {
    return this.MAXCAPACITY;
  }
  
  public ParkingOffice getOffice() {
    return this.office;
  }
  
  //Sets ParkingLot object's lotId value
  public void setLotId(String givenId) {
    this.lotId = givenId;
  }
  
  //Sets ParkingLot object's address value
  public void setAddress(String givenAddress) {
    this.address = givenAddress;
  }
  
  //Sets ParkingLot object's capacity value
  public void setCapacity(int givenCapacity) {
    this.openSpots = givenCapacity;
  }
  
  public LotType getLotType() {
    return this.type;
  }
}
