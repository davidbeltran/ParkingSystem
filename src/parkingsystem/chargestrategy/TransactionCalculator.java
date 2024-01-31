/*
Programmer: David Beltran
File: TransactionCalculator.java 
*/

/*
 This file contains the TransactionCalculator interface. This 
 interface contains methods that will be implemented by
 different strategy classes. 
*/

package parkingsystem.chargestrategy;

import parkingsystem.ParkingCharge;
import parkingsystem.observer.ParkingEvent;

public interface TransactionCalculator {
  final double FLATRATE = 1000.0;//regular rate for flat fee lots
  final double TIMEDRATE = 200.0 / 3600;//$2 per hour charge for timing charge lots
  
  ParkingCharge getCharge(ParkingEvent pe);
}
