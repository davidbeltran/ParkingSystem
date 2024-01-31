/*
Programmer: David Beltran
File: LotSizeDecorator.java 
*/

package parkingsystem.chargestrategy;

import parkingsystem.CarType;
import parkingsystem.Money;
import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

//Concrete component class of decorator pattern
public class TimedFeeLotCalc implements TransactionCalculator{
  private double cents = 0.0;
  
  @Override 
  public ParkingCharge getCharge(ParkingEvent pe) {
    this.cents = TIMEDRATE * pe.getTotalSec();
    if (pe.getCar().getType().equals(CarType.COMPACT)) {
      this.cents -= (cents * .2);
    }
    return new ParkingCharge.Builder(pe.getCar(), new Money(cents), pe.getExitDay())
        .setLot(pe.getLot().getLotId())
        .setParkTime(pe.getTotalSec())
        .build();
  }
}
