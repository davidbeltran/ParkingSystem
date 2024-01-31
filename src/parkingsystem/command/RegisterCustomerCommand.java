/* 
Programmer: David Beltran
File: RegisterCustomerCommand.java
*/
/*
 This file contains the RegisterCustomerCommand class for the Parking System 
 Application. This class implements the Command interface. This class
 holds the methods used by the ParkingService class to register/store Customer objects
 into the ParkingOffice class's lists. 
*/
package parkingsystem.command;

import java.util.Properties;
import java.util.Set;
import parkingsystem.Address;
import parkingsystem.Customer;
import parkingsystem.ParkingOffice;

public class RegisterCustomerCommand implements Command {
  private ParkingOffice office;
  
  //RegisterCustomerCommand constructor
  public RegisterCustomerCommand(ParkingOffice office) {
    this.office = office;
  }

  //Retrieves the name of the command to register a customer
  public String getCommandName() {
    return "CUSTOMER";
  }

  //Retrieves message that will be seen by end user
  public String getDisplayName() {
    return "Register a customer";
  }

  //Private method used within the execute() method to throw exception
  // if a field was not entered. 
  private void checkParameters(Properties params) {
    Set<String> keys = params.stringPropertyNames();
    for (String key : keys) {
      if (params.getProperty(key).isBlank() || params.getProperty(key) == null) {
        throw new IllegalArgumentException("You are missing " + key);
      }
    } 
  }
  
  //This method performs the registerCust() method from the ParkingOffice class
  // using end user responses as the arguments. 
  public String execute(Properties params) {
    //params = this.params;
    checkParameters(params);
    Address address = new Address(params.getProperty("custAddress1"), 
        params.getProperty("custAddress2"), params.getProperty("custCity"), 
        params.getProperty("custState"), params.getProperty("custZip"));
    Customer cust = this.office.registerCust(params.getProperty("custName"), address, 
        params.getProperty("custPhoneNumber"));
    //return "The customer " + cust.getName() + " was registered!";
    return cust.getCustomerId();
  }
}
