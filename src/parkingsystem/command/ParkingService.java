/* 
Programmer: David Beltran
File: ParkingService.java
*/
/*
 This file contains the ParkingService class for the Parking System
 Application. This class creates the object in which an end user will 
 interact with. The end user will be able to run commands into the 
 Parking System Application through this class's methods.  
*/

package parkingsystem.command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import parkingsystem.ParkingOffice;

public class ParkingService {
  @SuppressWarnings("unused")
  private ParkingOffice office;
  private Map<String, Command> commands = new LinkedHashMap<>();
  private String request;
  
  //ParkingLot constructor
  public ParkingService(ParkingOffice office) {
    this.office = office;
    //populates commands map with commands
    register(new RegisterCustomerCommand(office));
    register(new RegisterCarCommand(office));
  }
  
  //Private method used within the performCommand() method.
  //This runs the execute() method from either the RegisterCustomerCommand()
  // class or RegisterCarCommand class depending on user end command.
  private void register(Command command) {
    this.commands.put(command.getCommandName(), command);
  }
  
  //Method takes user end command and array of user end responses from the 
  // getAnswers() method. 
  public void performCommand(String request, String[] params) {
    request = request.toUpperCase();
    Command command = commands.get(request);
    Properties properties = new Properties();
    String[] paramNames = {};
    if (request.equals("CUSTOMER")) {
      paramNames = new String[] {"custName", "custPhoneNumber", "custAddress1", 
          "custAddress2", "custCity", "custState", "custZip"};
      for (int i = 0; i < params.length; i++) {
        properties.setProperty(paramNames[i], params[i]);
      }
    } else if (request.equals("CAR")) {
      paramNames = new String [] {"custName", "licensePlateNum", "carType"};
      for (int i = 0; i < params.length; i++) {
        properties.setProperty(paramNames[i], params[i]);
      }
    }
    command.execute(properties);
  }
  
  //Method created to obtain command from end user. 
  public String getRequest() {
    return this.request;
  }
}
