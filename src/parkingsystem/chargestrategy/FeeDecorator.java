/*
Programmer: David Beltran
File: FeeDecorator.java 
*/

package parkingsystem.chargestrategy;

import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

//Abstract class of decorator pattern
public abstract class FeeDecorator implements TransactionCalculator{
  public abstract ParkingCharge getCharge(ParkingEvent pe);
}
