/* 
Programmer: David Beltran
File: ParkingOffice.java
*/
/*
 This file contains the ParkingOffice class for the Parking System 
 that runs most functionality of storing which customers and cars are
 registered within the Parking System. ParkingOffice class also stores
 charges collected by all parking lots.
*/
package parkingsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingOffice {
  private String name;
  private Address address; // changed to Address type
  private volatile List<Customer> customers = new ArrayList<>();
  private volatile List<Car> cars = new ArrayList<>();
  private volatile List<ParkingLot> lots = new ArrayList<>();
  private volatile List<ParkingCharge> charges = new ArrayList<>();
  private volatile List<ParkingPermit> permits = new ArrayList<>();
  private int holdId;//
  private int permIdHold;
  private int lotIdHold;
  
  /*
    ParkingOffice constructor. Requires only a name and address
    to instentiate a ParkingOffice. Constructor automatically 
    creates ParkingLot objects and adds them to the 
    List<ParkingLot>   
  */
  public ParkingOffice(String name, Address address) {
    this.name = name;
    this.address = address;
  }
  
  // creates an instance of a Customer and adds that object to the List<Customer>
  public synchronized Customer registerCust(String name, Address address, String phone) {
    this.holdId++;
    String custId = "" + this.holdId;
    Customer customer = new Customer(custId, name, address, phone);
    this.customers.add(customer);
    return customer;
  }
  
  // Retrieves list of all customer IDs from ParkingOffice customer list
  public List<String> getCustomerIds() {
    List<String> custIds = new ArrayList<>();
    for (Customer customer : this.customers) {
      custIds.add(customer.getCustomerId());
    }
    return custIds;
  }
  
  /*
    creates an instance of a Car by using the register() method from
    the Customer class. Car instance is added to List<Car>
  */
  public synchronized Car registerCar(Customer c, String license, CarType t) {
    this.permIdHold++;
    String carPermit = c.getCustomerId() + this.permIdHold;
    LocalDate carExp = LocalDate.now();
    carExp = carExp.plusDays(180);
    Car car = new Car(carPermit, carExp, license, t, c.getCustomerId());
    this.cars.add(car);
    return car;
  }
  
  // adds the permit to the ParkingOffice list of permits
  public ParkingPermit registerCarPerm(Car givenCar) {
    ParkingPermit permit = new ParkingPermit(givenCar);
    permits.add(permit);
    return permit;
  }
  
  // Retrieves list of permit IDs from ParkingOffice car list
  public List<String> getPermitIds() {
    List<String> permitIds = new ArrayList<>();
    for (Car car : this.cars) {
      permitIds.add(car.getPermitId());
    }
    return permitIds;
  }
  
  // Retrieves list of car permit IDs from a specific customer
  public List<String> getCustPermitIds(Customer givenCustomer) {
    List<String> custPermitIds = new ArrayList<>();
    for (Car car : this.cars) {
      if (givenCustomer.getCustomerId().equals(car.getCustomerId())) {
        custPermitIds.add(car.getPermitId());
      }
    }
    return custPermitIds;
  }
  
  // Adds instance of ParkingCharge to List<ParkingCharge>
  public Money addCharge(ParkingCharge givenCharge) {
    this.charges.add(givenCharge);
    return givenCharge.getMoney();
  }
  
  // Retrieves Customer objects using Customer object name
  public Customer getCustomer(String givenName) {
    givenName = givenName.toUpperCase();
    Customer custHold = null;
    for (Customer customer : this.customers) {
      if (givenName.equals(customer.getName())) {
        custHold = customer;
      }
    }
    if (custHold == null) {
      throw new IllegalArgumentException("No customer with the name " + givenName);
    }
    return custHold;
  }
  
  // Retrieves ParkingLot objects using ParkingLot object ID
  public ParkingLot getLot(String givenLotId) {
    givenLotId = givenLotId.toUpperCase();
    ParkingLot lotHold = null;
    for (ParkingLot lot : this.lots) {
      if (givenLotId.equals(lot.getLotId())) {
        lotHold = lot;
      }
    }
    if (lotHold == null) {
      throw new IllegalArgumentException("No parking lot with the ID " + givenLotId);
    }
    return lotHold;
  }
  
  // Retrieves Car objects using Car object permit ID
  public Car getCar(String givenCarId) {
    Car carHold = null;
    for (Car car : this.cars) {
      if (givenCarId.equals(car.getPermitId())) {
        carHold = car;
      }
    }
    if (carHold == null) {
      throw new IllegalArgumentException("No car with permit ID " + givenCarId);
    }
    return carHold;
  }
  
  // Retrieves all ParkingCharge objects of one Car object with Car object permit ID
  public List<ParkingCharge> getBill(String givenCarId) {
    List<ParkingCharge> bills = new ArrayList<>();
    for (ParkingCharge charge : this.charges) {
      if (givenCarId.equals(charge.getPermitId())) {
        bills.add(charge);
      }
    }
    if (bills.isEmpty()) {
      throw new IllegalArgumentException("No charge with car permit ID " + givenCarId);
    }
    return bills;
  }
  
  public Money getParkingCharges(ParkingPermit givenPermit) {
    double cents = 0;
    for (ParkingCharge charge : this.charges) {
      if(givenPermit.getPermitId().equals(charge.getPermitId())) {
        cents = cents + charge.getMoney().getCents();
      }
    }
    Money cash = new Money(cents);
    return cash;
  }
  
  public Money getParkingCharges(Customer givenCustomer) {
    double cents = 0;
    for (ParkingCharge charge: this.charges) {
      if(givenCustomer.getCustomerId().equals(charge.getCar().getCustomerId())) {
        cents = cents + charge.getMoney().getCents();
      }
    }
    Money cash = new Money(cents);
    return cash;
  }
  
  public Money getParkingCharges(Car car) {
    double cents = 0;
    for (ParkingCharge charge : this.charges) {
      if(car.getPermitId().equals(charge.getPermitId())) {
        cents = cents + charge.getMoney().getCents();
      }
    }
    Money cash = new Money(cents);
    return cash;
  }
  
  //method to inject ParkingLot objects into ParkingOffice object
  public void addLot(String address, int capacity, LotType type) {
    if (this.lots.size() == 4) {
      throw new IllegalArgumentException("No more lots can be added at this time");
    }
    this.lotIdHold++;
    if (this.lots.isEmpty()) {
      if (type.equals(LotType.TIMED)) {
        this.lots.add(new ParkingLot("a" + this.lotIdHold, address, capacity, type, this));
      } else if (type.equals(LotType.FLAT)) {
        this.lots.add(new ParkingLot("b" + this.lotIdHold, address, capacity, type, this));
      }
    } else {
      List<ParkingLot> lots = this.lots;
      for (int i = 0; i < lots.size(); i++) {
        if (type.equals(LotType.TIMED)) {
          this.lots.add(new ParkingLot("a" + this.lotIdHold, address, capacity, type, this));
          break;
        } else if (type.equals(LotType.FLAT)) {
          this.lots.add(new ParkingLot("b" + this.lotIdHold, address, capacity, type, this));
          break;
        }
      }
    }
  }
  
  // Retrieves ParkingLot objects stored in lots list
  public List<ParkingLot> getLots() {
    return this.lots;
  }
  
  // Retrieves Customer objects stored in customers list
  public List<Customer> getCustomers() {
    return this.customers;
  }

  // Retrieves Car objects stored in List<Car>
  public List<Car> getCars() {
    return this.cars;
  }
  
  // Retrieves ParkingCharge objects stored in charges list
  public List<ParkingCharge> getCharges() {
    return this.charges;
  }
  
  // Retrieves ParkingOffice object name
  public String getParkingOfficeName() {
    return this.name;
  }
  
  // Retrieves ParkingOffice object address
  public Address getAddress() {
    return this.address;
  }
  
  /*
  hashCode() and equals() overriden to allow comparison of object values and 
  not memory location. The ParkingOffice is the same even if
  cars, charges, customers, and permit lists and holdId are different.
 */
  @Override
  public int hashCode() {
    return Objects.hash(address, lots, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ParkingOffice)) {
      return false;
    }
    ParkingOffice other = (ParkingOffice) obj;
    return Objects.equals(address, other.address) 
        && Objects.equals(lots, other.lots) && Objects.equals(name, other.name);
  }
}
