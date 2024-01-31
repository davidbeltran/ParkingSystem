/* 
Programmer: David Beltran
File: Address.java
*/
/*
 This file contains the Address class for the Parking System.
*/
package parkingsystem;

import java.util.Objects;

public class Address {
  private String streetAddress1;
  private String streetAddress2;
  private String city;
  private String state;
  private String zipCode;
  
  // Address constructor
  public Address(String streetAddress1, String streetAddress2, String city, 
      String state, String zipCode) {
    this.streetAddress1 = streetAddress1.toUpperCase();
    this.streetAddress2 = streetAddress2.toUpperCase();
    this.city = city.toUpperCase();
    this.state = state.toUpperCase();
    this.zipCode = zipCode.toUpperCase();
  }
  
  // hashCode() and equals() overriden to override Customer hasCode() and equals() 
  @Override
  public int hashCode() {
    return Objects.hash(city, state, streetAddress1, streetAddress2, zipCode);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Address)) {
      return false;
    }
    Address other = (Address) obj;
    return Objects.equals(city, other.city) && Objects.equals(state, other.state)
        && Objects.equals(streetAddress1, other.streetAddress1)
        && Objects.equals(streetAddress2, other.streetAddress2)
        && Objects.equals(zipCode, other.zipCode);
  }

  // Prints values placed in object variables
  public String getAddressInfo() {
    return streetAddress1 + ", " + streetAddress2 + ", " + city + ", " + state + ", " + zipCode;
  }
  
  // Retrieves Address object's streetAddress1 value
  public String getStreetAddress1() {
    return this.streetAddress1;
  }
  
  // Retrieves Address object's streetAddress2 value
  public String getStreetAddress2() {
    return this.streetAddress2;
  }
  
  // Retrieves Address object's city value
  public String getCity() {
    return this.city;
  }
  
  // Retrieves Address object's state value
  public String getState() {
    return this.state;
  }
  
  // Retrieves Address object's zipCode value
  public String getZipCode() {
    return this.zipCode;
  }
  
  // Sets Address object's streetAddress1 value
  public void setStreetAddress1(String givenStreet1) {
    this.streetAddress1 = givenStreet1;
  }
  
  // Sets Address object's streetAddress2 value
  public void setStreetAddress2(String givenStreet2) {
    this.streetAddress2 = givenStreet2;
  }
  
  // Sets Address object's city value
  public void setCity(String givenCity) {
    this.city = givenCity;
  }
  
  // Sets Address object's state value
  public void setState(String givenState) {
    this.state = givenState;
  }
  
  // Sets Address object's zipCode value
  public void setZipCode(String givenZip) {
    this.zipCode = givenZip;
  }
}
