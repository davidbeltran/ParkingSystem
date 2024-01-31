/*
Programmer: David Beltran
File: TransactionManager.java 
*/

/*
 This file contains the TransactionManager class. This class
 handles the calculations of when a car enters and exits a 
 parking lot. It helps keep track of how many spots are available
 and also produces a charge that is then given to the ParkingOffice
 object. 
*/

package parkingsystem;

import parkingsystem.chargestrategy.ParkingChargeStrategyFactory;
import parkingsystem.chargestrategy.TransactionCalculator;
import parkingsystem.observer.ParkingEvent;

public class TransactionManager {
  private ParkingOffice office;
  
  // TransactionManager constructor
  public TransactionManager(ParkingOffice office) {
    this.office = office;
  }

  //calculates and returns a parking charge.
  public ParkingCharge park(String strategy, ParkingEvent pe) {
    TransactionCalculator calc = new ParkingChargeStrategyFactory().getCalc(strategy);
    ParkingCharge charge = calc.getCharge(pe);
    this.office.addCharge(charge);//charge is added to ParkingOffice object charges list
    return charge;
  }
}
