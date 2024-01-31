/*
Programmer: David Beltran
File: ParkingSystemTest.java 
*/

/*
 This file contains the ParkingSystemTest class. This class
 is used to test implementations added through the ICT 4315 course. 
*/
package test;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;
import org.junit.Test;
import parkingsystem.Address;
import parkingsystem.Car;
import parkingsystem.CarType;
import parkingsystem.Customer;
import parkingsystem.LotType;
import parkingsystem.Money;
import parkingsystem.ParkingCharge;
import parkingsystem.ParkingLot;
import parkingsystem.ParkingOffice;
import parkingsystem.command.ParkingService;
import parkingsystem.command.RegisterCarCommand;
import parkingsystem.command.RegisterCustomerCommand;
import parkingsystem.communications.CommandRequest;
import parkingsystem.communications.CommandResponse;
import parkingsystem.observer.LotScanner;
import parkingsystem.observer.ParkingObserver;

public class ParkingSystemTest {
  private Address officeAddress;
  private ParkingOffice mainOffice;
  private Customer cust;
  private Car car1, car2, car3, car4, car5;

  public void createObjects() {
    this.officeAddress = new Address("123 circle dr.", "unit 4", "las vegas", "nv", "12345");
    this.mainOffice = new ParkingOffice("office", officeAddress);
    this.cust = mainOffice.registerCust("jon doe", officeAddress, "123-456-7890");
    this.car1 = mainOffice.registerCar(cust, "123aew", CarType.SUV);
    this.car2 = mainOffice.registerCar(cust, "456aew", CarType.SUV);
    this.car3 = mainOffice.registerCar(cust, "789aew", CarType.SUV);
    this.car4 = mainOffice.registerCar(cust, "101aew", CarType.COMPACT);
    this.car5 = mainOffice.registerCar(cust, "112aew", CarType.COMPACT);
    this.mainOffice.addLot("123 square dr.", 200, LotType.TIMED);//a1
    this.mainOffice.addLot("123 Triangle dr.", 40, LotType.TIMED);//a2
    this.mainOffice.addLot("123 cube dr.", 200, LotType.FLAT);//b3
    this.mainOffice.addLot("123 pyramid dr.", 40, LotType.FLAT);//b4
  }
  
  @Test
  public void testRegisterCustomerCommand() {
    Address officeAddress = new Address("123 circle dr.", "unit 4", "las vegas", "nv", "12345");
    ParkingOffice mainOffice = new ParkingOffice("office", officeAddress);
    RegisterCustomerCommand custCommand = new RegisterCustomerCommand(mainOffice);
    Properties props = new Properties();
    props.setProperty("custName", "jon doe");
    props.setProperty("custPhoneNumber", "123-456-7890");
    props.setProperty("custAddress1", "123 circle dr.");
    props.setProperty("custAddress2", "unit 4");
    props.setProperty("custCity", "las vegas");
    props.setProperty("custState", "nv");
    props.setProperty("custZip", "12345");
    custCommand.execute(props);
    ParkingOffice mainOffice2 = new ParkingOffice("office2", officeAddress);
    mainOffice2.registerCust("jon doe", officeAddress, "123-456-7890");
    assertEquals(mainOffice.getCustomer("jon doe"), mainOffice2.getCustomer("jon doe"));
  }
  
  @Test
  public void testRegisterCarCommand() {
    Address officeAddress = new Address("123 circle dr.", "unit 4", "las vegas", "nv", "12345");
    ParkingOffice mainOffice = new ParkingOffice("office", officeAddress);
    Customer cust1 = mainOffice.registerCust("jon doe", officeAddress, "123-456-7890");
    Properties props = new Properties();
    props.setProperty("custName", cust1.getName());
    props.setProperty("licensePlateNum", "123aew");
    props.setProperty("carType", "SUV");
    ParkingOffice mainOffice2 = new ParkingOffice("office2", officeAddress);
    RegisterCarCommand carCommand = new RegisterCarCommand(mainOffice2);
    mainOffice2.registerCust("jon doe", officeAddress, "123-456-7890");
    carCommand.execute(props);
    assertEquals(mainOffice.getCars(), mainOffice.getCars());
  }
  
  @Test
  public void testParkingService() {
    Address officeAddress = new Address("123 circle dr.", "unit 4", "las vegas", "nv", "12345");
    ParkingOffice mainOffice = new ParkingOffice("office", officeAddress);
    Customer cust = mainOffice.registerCust("jon doe", officeAddress, "123-456-7890");
    mainOffice.registerCar(cust, "123aew", CarType.SUV);
    ParkingOffice mainOffice2 = new ParkingOffice("office2", officeAddress);
    ParkingService service = new ParkingService(mainOffice2);
    String[] custAnswers = {"jon doe", "123-456-7890", "123 circle dr.", "unit 4", "las vegas",
        "nv", "12345"};
    service.performCommand("CUSTOMER", custAnswers);
    assertEquals(mainOffice.getCustomer("jon doe").getPhoneNumber(), 
        mainOffice2.getCustomer("jon doe").getPhoneNumber());
    
    String[] carAnswers = {"jon doe", "123aew", "SUV"};
    service.performCommand("CAR", carAnswers);
    assertEquals(mainOffice.getCars(), mainOffice2.getCars());
  }
  
  //Tests the TransactionManager class
  @Test
  public void testTransactionManager() {
    createObjects();
    
    //checks that lot spot availability is decreasing or increasing
    //as cars enter and exit a lot, respectively. 
    ParkingLot lot = mainOffice.getLot("a1");
    lot.enterLot(car1.getPermitId());
    assertEquals(lot.getAvailableSpots(), 199);
    lot.exitLot(LocalDateTime.now(), LocalDateTime.now(), car1.getPermitId());
    assertEquals(lot.getAvailableSpots(), 200);
  }
  
  
  //Tests the "overuse" strategy
  @Test
  public void testOverUseStrategy() {
    createObjects();
    
    //checks fee when SUV enters a hour rate lot during prime time
    ParkingLot lot = mainOffice.getLot("a1");
    ParkingObserver scanner = new LotScanner(lot);
    scanner.setStrategy("timedoveruse");
    LocalDateTime in = LocalDateTime.of(2022, 1, 20, 11, 0, 0);
    LocalDateTime out = LocalDateTime.of(2022, 1, 20, 11, 30, 0);
    lot.exitLot(in, out, car1.getPermitId());
    ParkingCharge dummyCharge1 = new ParkingCharge.Builder(car1, new Money(150), 
        in).setLot("A1").setParkTime(1800).setOvernight(0).build();
    assertEquals(mainOffice.getParkingCharges(car1), dummyCharge1.getMoney());
    
    //checks fee when SUV enters a hour rate lot outside prime time
    in = LocalDateTime.of(2022, 1, 20, 9, 0, 0);
    out = LocalDateTime.of(2022, 1, 20, 9, 30, 0);
    lot.exitLot(in, out, car2.getPermitId());
    ParkingCharge dummyCharge2 = new ParkingCharge.Builder(car2, new Money(125), 
        in).setLot("A1").setParkTime(1800).setOvernight(0).build();
    assertEquals(mainOffice.getParkingCharges(car2), dummyCharge2.getMoney());
    
    //checks fee when SUV stays overnight once
    lot = mainOffice.getLot("b3");
    scanner = new LotScanner(lot);
    scanner.setStrategy("flatoveruse");
    in = LocalDateTime.of(2022, 1, 20, 9, 0, 0);
    out = LocalDateTime.of(2022, 1, 21, 9, 30, 0);
    lot.exitLot(in, out, car3.getPermitId());
    ParkingCharge dummyCharge3 = new ParkingCharge.Builder(car3, new Money(2500), 
        in).setLot("b3").setParkTime(0).setOvernight(1).build();
    assertEquals(mainOffice.getParkingCharges(car3), dummyCharge3.getMoney());
    
    //checks fee when COMPACT stays overnight more than twice in a row 
    in = LocalDateTime.of(2022, 1, 20, 9, 0, 0);
    out = LocalDateTime.of(2022, 1, 23, 9, 30, 0);
    lot.exitLot(in, out, car4.getPermitId());
    ParkingCharge dummyCharge4 = new ParkingCharge.Builder(car4, new Money(3400), 
        in).setLot("b3").setParkTime(0).setOvernight(3).build();
    assertEquals(mainOffice.getParkingCharges(car4), dummyCharge4.getMoney());
    
  }
  
  
  //test LotSize strategy
  @Test
  public void testLotSizeStrategy () {
    createObjects();
    
    //checks fee when a SUV vehicle parks in a small hour rate lot
    ParkingLot lot = mainOffice.getLot("a2");
    ParkingObserver scanner = new LotScanner(lot);
    scanner.setStrategy("timedlotsize");
    LocalDateTime in = LocalDateTime.of(2022, 1, 20, 11, 0, 0);
    LocalDateTime out = LocalDateTime.of(2022, 1, 20, 12, 0, 0);
    lot.exitLot(in, out, car1.getPermitId());
    ParkingCharge dummyCharge1 = new ParkingCharge.Builder(car1, new Money(300), 
        in).setLot("a2").setParkTime(1800).setOvernight(0).build();
    assertEquals(mainOffice.getParkingCharges(car1), dummyCharge1.getMoney());
    
    //checks fee when a COMPACT vehicle parks in a small hour rate lot
    lot.exitLot(in, out, car4.getPermitId());
    ParkingCharge dummyCharge4 = new ParkingCharge.Builder(car4, new Money(170), 
        in).setLot("a2").setParkTime(1800).setOvernight(0).build();
    assertEquals(mainOffice.getParkingCharges(car4), dummyCharge4.getMoney());
    
    //checks fee when a SUV vehicle parks in a small sized flat fee lot
    lot = mainOffice.getLot("b4");
    scanner = new LotScanner(lot);
    scanner.setStrategy("flatlotsize");
    in = LocalDateTime.of(2022, 1, 20, 9, 0, 0);
    out = LocalDateTime.of(2022, 1, 21, 9, 30, 0);
    lot.exitLot(in, out, car3.getPermitId());
    ParkingCharge dummyCharge3 = new ParkingCharge.Builder(car3, new Money(3000), 
        in).setLot("b4").setParkTime(0).setOvernight(1).build();
    assertEquals(mainOffice.getParkingCharges(car3), dummyCharge3.getMoney());
    
    //checks fee when a COMPACT vehicle parks in a small sized flat fee lot
    lot.exitLot(in, out, car5.getPermitId());
    ParkingCharge dummyCharge5 = new ParkingCharge.Builder(car5, new Money(1700), 
        in).setLot("b4").setParkTime(0).setOvernight(1).build();
    assertEquals(mainOffice.getParkingCharges(car5), dummyCharge5.getMoney());
    
    //checks fee when a SUV vehicle parks in a regular sized flat fee lot
    lot = mainOffice.getLot("b3");
    scanner = new LotScanner(lot);
    scanner.setStrategy("flatlotsize");
    lot.exitLot(in, out, car2.getPermitId());
    ParkingCharge dummyCharge2 = new ParkingCharge.Builder(car2, new Money(2500), 
        in).setLot("b3").setParkTime(0).setOvernight(1).build();
    assertEquals(mainOffice.getParkingCharges(car2), dummyCharge2.getMoney());
  }
  
  @Test
  public void testServerCommunication() {
    createObjects();
    String[] params = {"customer ID = 1", "customer name = JON DOE",
        "customer address = [123 CIRCLE DR., UNIT 4, LAS VEGAS, NV, 12345]",
        "customer phone number = 123-456-7890"};
    CommandRequest req = new CommandRequest("CUSTOMER", params);
    assertEquals(cust.toString(), "Customer " + Arrays.toString(req.getCommandParameters()));
    
    CommandResponse res = new CommandResponse(200, "Success");
    res.addMessage(cust.toString());
    assertEquals(Arrays.toString(res.getCommandParameters()), 
        "[Customer " + Arrays.toString(req.getCommandParameters()) + "]");
  }
}