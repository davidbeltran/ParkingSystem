/*
Programmer: David Beltran
File: FlatFeeLotCalc.java 
*/

package parkingsystem.chargestrategy;

import parkingsystem.CarType;
import parkingsystem.Money;
import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

//Concrete component class of decorator pattern
public class FlatFeeLotCalc implements TransactionCalculator {
  private double cents = 0.0;

  @Override
  public ParkingCharge getCharge(ParkingEvent pe) {
    this.cents = FLATRATE + (FLATRATE * pe.getDaysBetween());
    if (pe.getCar().getType().equals(CarType.COMPACT)) {
      this.cents -= (cents * .2);
    }
    return new ParkingCharge.Builder(pe.getCar(), new Money(this.cents), pe.getExitDay())
        .setLot(pe.getLot().getLotId())
        .setOvernight(pe.getDaysBetween())
        .build();
  }
}
