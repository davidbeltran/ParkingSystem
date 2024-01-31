/*
Programmer: David Beltran
File: ParkingChargeStrategy.java 
*/

package parkingsystem.chargestrategy;

public class ParkingChargeStrategyFactory {
  
  //Factory method that returns strategy object
  public TransactionCalculator getCalc(String strategy) {
    strategy = strategy.toUpperCase();
    TransactionCalculator calc = null;
    switch (strategy) {
      case "TIMEDLOTSIZE":
        calc = new LotSizeDecorator(new CarTypeDecorator(new TimedFeeLotCalc()));
        break;
      case "FLATLOTSIZE":
        calc =  new LotSizeDecorator(new CarTypeDecorator(new FlatFeeLotCalc()));
        break;
      case "TIMEDOVERUSE":
        calc = new PrimeTimeDecorator(new CarTypeDecorator(new TimedFeeLotCalc()));
        break;
      case "FLATOVERUSE":
        calc = new OvernightDecorator(new CarTypeDecorator(new FlatFeeLotCalc()));
        break;
    }
    return calc;
  }
}
