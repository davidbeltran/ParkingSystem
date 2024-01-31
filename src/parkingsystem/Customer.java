/* 
Programmer: David Beltran
File: Customer.java
*/
/*
 This file contains the Customer class for the Parking System 
 that creates a customer object. This class has a method to register
 a Car class object associated to corresponding customer object.
*/

package parkingsystem;

import java.util.Objects;

public class Customer {
  private String customerId;
  private String name;
  transient private Address address;
  private String phoneNumber;
  transient private int holdId;
  
  // Customer constructor 
  public Customer(String customerId, String name, Address address, String phoneNumber) {
    this.customerId = customerId;
    this.name = name.toUpperCase();
    this.address = address;
    this.phoneNumber = phoneNumber;
  }
  
  //Prints values placed in object variables
  public String toString() {
    return "Customer [customer ID = " + customerId + ", customer name = " + name +
        ", customer address = [" + address.getAddressInfo() + "]"
        + ", customer phone number = " + phoneNumber + "]";
  }
  
  /*
  hashCode() and equals() overriden to allow comparison of object values and 
  not memory location. The customer object will be the same even if customerId is different
 */
  @Override
  public int hashCode() {
    return Objects.hash(address, holdId, name, phoneNumber);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Customer)) {
      return false;
    }
    Customer other = (Customer) obj;
    return Objects.equals(address, other.address) 
        && holdId == other.holdId && Objects.equals(name, other.name)
        && Objects.equals(phoneNumber, other.phoneNumber);
  }
  
  // Retrieves Customer object's customerId value
  public String getCustomerId() {
    return this.customerId;
  }
  
  // Retrieves Customer object's name value
  public String getName() {
    return this.name;
  }
  
  // Retrieves Customer object's address string value
  public String getAddress() {
    return this.address.getAddressInfo();  
  }
  
  // Retrieves Customer object's phoneNumber value
  public String getPhoneNumber() {
    return this.phoneNumber;
  }
  
  // Sets Customer object's customerId value
  public void setCustomerId(String givenId) {
    this.customerId = givenId;
  }
  
  // Sets Customer object's Address object value
  public void setAddress(Address givenAddress) {    
    this.address = givenAddress;
  }
  
  // Sets Customer object's name value
  public void setName(String givenName) {
    this.name = givenName.toUpperCase();
  }
  
  // Sets Customer object's phoneNumber value
  public void setPhoneNumber(String givenPhone) {
    this.phoneNumber = givenPhone;
  }
}
