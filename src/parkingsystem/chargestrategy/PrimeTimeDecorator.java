/*
Programmer: David Beltran
File: LotSizeDecorator.java 
*/

package parkingsystem.chargestrategy;

import parkingsystem.Money;
import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

//concrete decorator class for decorator pattern
public class PrimeTimeDecorator extends FeeDecorator{
  private TransactionCalculator calc;
  private double cents;
  
  public PrimeTimeDecorator(TransactionCalculator calc) {
    this.calc = calc;
  }

  @Override
  public ParkingCharge getCharge(ParkingEvent pe) {
    ParkingCharge charge = this.calc.getCharge(pe);
    if (pe.getEntryDay().getHour() >= 11 && pe.getEntryDay().getHour() < 15) {
      this.cents = charge.getMoney().getCents() + ((TIMEDRATE * pe.getTotalSec()) * .25);
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
