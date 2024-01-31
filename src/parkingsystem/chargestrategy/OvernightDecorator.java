/*
Programmer: David Beltran
File: OvernightDecorator.java 
*/

package parkingsystem.chargestrategy;

import parkingsystem.Money;
import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

//concrete decorator class for decorator pattern
public class OvernightDecorator extends FeeDecorator{
  private TransactionCalculator calc;
  private double cents;
  
  public OvernightDecorator(TransactionCalculator calc) {
    this.calc = calc;
  }

  @Override
  public ParkingCharge getCharge(ParkingEvent pe) {
    ParkingCharge charge = this.calc.getCharge(pe);
    if (pe.getDaysBetween() > 2) {
      this.cents = charge.getMoney().getCents();
      this.cents += ((FLATRATE + (FLATRATE * pe.getDaysBetween())) * .25); 
    } else {
      this.cents = charge.getMoney().getCents();
    }
    return new ParkingCharge.Builder(pe.getCar(), new Money(this.cents), pe.getExitDay())
        .setLot(pe.getLot().getLotId())
        .setParkTime(pe.getTotalSec())
        .setOvernight(pe.getDaysBetween())
        .build();
  }
  
}
