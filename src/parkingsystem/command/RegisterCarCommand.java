/* 
Programmer: David Beltran
File: RegisterCarCommand.java
*/
/*
 This file contains the RegisterCarCommand class for the Parking System 
 Application. This class implements the Command interface. This class
 holds the methods used by the ParkingService class to register/store Car objects
 into the ParkingOffice class's lists. 
*/

package parkingsystem.command;

import java.util.Properties;
import java.util.Set;
import parkingsystem.Car;
import parkingsystem.CarType;
import parkingsystem.ParkingOffice;

public class RegisterCarCommand implements Command {
  private ParkingOffice office;
  
  //RegisterCarCommand constructor
  public RegisterCarCommand(ParkingOffice office) {
    this.office = office;
  }
  
  //Retrieves the name of the command to register a car
  public String getCommandName() {
    return "CAR";
  }
  
  //Retrieves message that will be seen by end user
  public String getDisplayName() {
    return "Register a car";
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
  
  //This method performs the registerCar() method from the ParkingOffice class
  // using end user responses as the arguments. 
  public String execute(Properties params) {
    checkParameters(params);
    Car car = this.office.registerCar(this.office.getCustomer(params.getProperty("custName")),
        params.getProperty("licensePlateNum"), CarType.valueOf(params.getProperty("carType")));
    return car.getPermitId();
  }
}
