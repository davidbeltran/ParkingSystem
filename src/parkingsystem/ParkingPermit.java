/* 
Programmer: David Beltran
File: ParkingPermit.java
*/
/*
 File that creates an object of a parking permit containing
 important information which includes the customer ID number, 
 permit number, and the type of vehicle. 
*/

package parkingsystem;

import java.util.Objects;

public class ParkingPermit {
  private String permitNum;
  private CarType type;
  private String customerId;
  
  public ParkingPermit(Car givenCar) {
    this.permitNum = givenCar.getPermitId();
    this.type = givenCar.getType();
    this.customerId = givenCar.getCustomerId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(permitNum);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ParkingPermit)) {
      return false;
    }
    ParkingPermit other = (ParkingPermit) obj;
    return Objects.equals(permitNum, other.permitNum);
  }
  
  public String getPermitId() {
    return this.permitNum;
  }
  
  public String getCustomerId() {
    return this.customerId;
  }
  
  public CarType getCarType() {
    return this.type;
  }
}
