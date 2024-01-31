///////////////////////////////
// File: ParkingService.java
// Author: Instructor, modified by David Beltran
// This file is the seed for how the ParkingOffice server handles remote commands.
///////////////////////////////
package parkingsystem.server;

import parkingsystem.Address;
import parkingsystem.Car;
import parkingsystem.CarType;
import parkingsystem.Customer;
import parkingsystem.communications.CommandRequest;
import parkingsystem.communications.CommandResponse;
import parkingsystem.ParkingOffice;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParkingService {

  protected final ParkingOffice parkingOffice;

  private static final Logger logger = Logger.getLogger(ParkingService.class.getName());

  public ParkingService(ParkingOffice parkingOffice) {
    this.parkingOffice = parkingOffice;
  }

  protected CommandResponse handleInput(InputStream in) throws IOException,
  ClassNotFoundException {
    BufferedInputStream bis = new BufferedInputStream(in);
    if (bis.markSupported()) {
      bis.mark(4);
    }
    ObjectInputStream ois = new ObjectInputStream(bis);
    Object obj = ois.readObject();
    
    CommandRequest command = (CommandRequest) obj;
    return performCommand(command);
  }

  private CommandResponse performCommand(CommandRequest request) {
    logger.log(Level.INFO, "Performing {0} command", request.getCommandName());
    CommandResponse result = null;
    switch (request.getCommandName().toUpperCase()) {
      case "CUSTOMER":
        result = new CommandResponse(200, "Success");
        if (checkNumberOfParameters(7, request.getCommandParameters().length)) {
          Address address = new Address("", "", "", "", "");
          address.setStreetAddress1(request.getCommandParameters()[2]);
          address.setStreetAddress2(request.getCommandParameters()[3]);
          address.setCity(request.getCommandParameters()[4]);
          address.setState(request.getCommandParameters()[5]);
          address.setZipCode(request.getCommandParameters()[6]);
          Customer customer = new Customer("", "", address, "");
          customer.setName(checkName(request.getCommandParameters()[0]));
          customer.setPhoneNumber(checkPhoneNumber(request.getCommandParameters()[1]));
          customer = this.parkingOffice.registerCust(customer.getName(), address, 
              customer.getPhoneNumber());
          result.addMessage("Added " + customer.toString());
        }
        break;
      case "CAR":
        result = new CommandResponse(200, "Success");
        if (checkNumberOfParameters(3, request.getCommandParameters().length)) {
          Car car = new Car();
          car.setLicense(checkLicensePlate(request.getCommandParameters()[0]));
          car.setOwner(checkCustomer(request.getCommandParameters()[1]));
          car.setCarType(checkCarType(request.getCommandParameters()[2]));
          car = parkingOffice.registerCar(parkingOffice
              .getCustomer(request.getCommandParameters()[1]),
              car.getLicense(), car.getType());
          result.addMessage("Added " + car.toString());
        }
        break;
      case "STOP":
        result = new CommandResponse(200, "Success");
        Server.stopServer();
        result.addMessage("Server has been stopped.");
        break;
      default:
        result = new CommandResponse(400, "Unknown");
        result.addMessage("Request " + request.getCommandName() + " not supported");
    }
    return result;
  }
  
  private static boolean checkNumberOfParameters(int expected, int provided) {
    boolean result = true;
    if (provided < expected) {
      logger.log(Level.SEVERE, "Not enough parameters! Expected {0} received {1}",
          new Object[]{expected, provided});
      result = false;
    }
    return result;
  }

  private static String checkName(String name) {
    String result = name;
    if (name == null) {
      result = "Unknown";
    }
    return result;
  }

  private Customer checkCustomer(String customerName) {
    Customer customer = parkingOffice.getCustomer(customerName);
    if (customer == null) {
      Address address = new Address("", "", "", "", "");
      customer = new Customer("", "", address, "");
      customer.setName(customerName);
      parkingOffice.registerCust(customer.getName(), address, "");
    }
    return customer;
  }

  private static String checkLicensePlate(String plate) {
    String result = plate;
    if (result == null) {
      plate = "Unknown";
    }
    return result;
  }
  
  private static String checkPhoneNumber(String number) {
    String result = number;
    if (result == null) {
      number = "Unknown";
    }
    return result;
  }
  
  private static CarType checkCarType(String type) {
    return CarType.valueOf(type.toUpperCase());
  }
}
