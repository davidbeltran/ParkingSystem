/*
Programmer: David Beltran
File: LotSizeDecorator.java 
*/

package parkingsystem.chargestrategy;

import parkingsystem.LotType;
import parkingsystem.Money;
import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

//concrete decorator class for decorator pattern
public class LotSizeDecorator extends FeeDecorator{
  private TransactionCalculator calc;
  private double cents;
  
  public LotSizeDecorator(TransactionCalculator calc) {
    this.calc = calc;
  }
  
  @Override
  public ParkingCharge getCharge(ParkingEvent pe) {
    ParkingCharge charge = this.calc.getCharge(pe);
    if (pe.getLot().getMaxCapacity() <= 50) {
      if (pe.getLot().getLotType().equals(LotType.TIMED)) {
        this.cents = charge.getMoney().getCents() + ((TIMEDRATE * pe.getTotalSec()) * .25);
      } else if (pe.getLot().getLotType().equals(LotType.FLAT)) {
        this.cents = (charge.getMoney().getCents());
        this.cents += ((FLATRATE + (FLATRATE * pe.getDaysBetween())) * .25);
      }
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