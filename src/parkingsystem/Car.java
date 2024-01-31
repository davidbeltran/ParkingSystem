/* 
Programmer: David Beltran
File: Car.java
*/
/*
 This file contains the Car class for the Parking System that
 creates an car object.
*/

package parkingsystem;

import java.time.LocalDate;
import java.util.Objects;

public class Car {
  private String permit;
  private LocalDate permitExp;
  private String license;
  private CarType type;
  private String ownerId;
  
  // Car constructor
  public Car(String permit, LocalDate permitExp, String license, CarType type, 
      String customerId) {
    this.permit = permit;
    this.permitExp = permitExp;
    this.license = license;
    this.type = type;
    this.ownerId = customerId;
  }
  
  public Car() {
  }
  
  /*
   hashCode() and equals() overriden to allow comparison of object values and 
   not memory location. The Car object will be the same even if CarType is different.
  */
  @Override
  public int hashCode() {
    return Objects.hash(license, ownerId, permit, permitExp);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Car)) {
      return false;
    }
    Car other = (Car) obj;
    return Objects.equals(license, other.license) && Objects.equals(ownerId, other.ownerId)
        && Objects.equals(permit, other.permit) && Objects.equals(permitExp, other.permitExp);
  }

  //Prints values placed in object variables
  public String toString() {
    return "Car [permit = " + permit + ", permitExp = " + permitExp + ", license = " + license +
        ", car type = " + type + ", customer id = " + ownerId + "]";
  }
  
  // Retrieves Car object's permit value
  public String getPermitId() {
    return this.permit;
  }
  
  // Retrieves Car object's permitExpiration value
  public LocalDate getPermitExp() {
    return this.permitExp;
  }
  
  // Retrieves Car object's license value
  public String getLicense() {
    return this.license;
  }
  
  // Retrieves Car object's type value
  public CarType getType() {
    return this.type;
  }
  
  // Retrieves Car object's customerId value
  public String getCustomerId() {
    return this.ownerId;
  }
  
  // Sets Car object's permit value
  public void setPermit(String givenPermit) {
    this.permit = givenPermit;
  }
  
  // Sets Car object's permitExpiration value
  public void setPermitExp(LocalDate givenExp) {
    this.permitExp = givenExp;
  }
  
  // Sets Car object's license value
  public void setLicense(String givenLicense) {
    this.license = givenLicense;
  }
  
  // Sets Car object's type value
  public void setCarType(CarType givenType) {
    this.type = givenType;
  }
  
  // Sets Car object's customerId value
  public void setOwner(Customer givenOwner) {
    this.ownerId = givenOwner.getCustomerId();
  }
}
