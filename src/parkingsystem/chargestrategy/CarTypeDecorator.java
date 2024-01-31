/*
Programmer: David Beltran
File: CarTypeDecorator.java 
*/

package parkingsystem.chargestrategy;

import parkingsystem.CarType;
import parkingsystem.Money;
import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

public class CarTypeDecorator extends FeeDecorator{
  private TransactionCalculator calc;
  private double cents;
  
  public CarTypeDecorator(TransactionCalculator calc) {
    this.calc = calc;
  }
  
  //Gives a 25% surcharge if vehicle is SUV and 25% discount if COMPACT
  @Override
  public ParkingCharge getCharge(ParkingEvent pe) {
    ParkingCharge charge = this.calc.getCharge(pe);
    if (pe.getCar().getType().equals(CarType.SUV)) {
      this.cents = (charge.getMoney().getCents());
      this.cents += (this.cents * .25);
    } else if (pe.getCar().getType().equals(CarType.COMPACT)) {
      this.cents = (charge.getMoney().getCents());
      this.cents -= (this.cents * .25);
    }
    return new ParkingCharge.Builder(pe.getCar(), new Money(this.cents), pe.getExitDay())
        .setLot(pe.getLot().getLotId())
        .setParkTime(pe.getTotalSec())
        .setOvernight(pe.getDaysBetween())
        .build();
  }
}
