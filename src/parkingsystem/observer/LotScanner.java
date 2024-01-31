/*
Programmer: David Beltran
File: LotScanner.java 
*/

/*
 This file contains the LotScanner class. This class
 implements the ParkingObserver interface and serves as
 an observer in the observer pattern to send a message
 to the application that then creates a charge. The 
 LotScanner class helps with providing the entry and
 exit times. 
*/

package parkingsystem.observer;

import parkingsystem.ParkingLot;
import parkingsystem.TransactionManager;

public class LotScanner implements ParkingObserver {
  private ParkingLot lot;
  private String strategy;
  private TransactionManager tm;
  
  //LotScanner constructor
  public LotScanner(ParkingLot lot) {
    this.lot = lot;
    this.lot.addObserver(this);//adds the LotScanner to ParkingLot's observer list
    this.tm = new TransactionManager(this.lot.getOffice());
  }
  
  //sends ParkingEvent object and strategy choice to TransactionManager object's park() method
  @Override
  public void update(ParkingEvent pe) {
    this.tm.park(strategy, pe);
  }
  
  //method to allow the LotScanner be given the strategy object
  @Override
  public void setStrategy(String strategy) {
    this.strategy = strategy;
  }
}
