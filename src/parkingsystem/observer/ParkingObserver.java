/*
Programmer: David Beltran
File: ParkingObserver.java 
*/

/*
 This file contains the ParkingObserver interface. This 
 interface holds the uninitialized variables used by its
 concrete classes.    
*/
package parkingsystem.observer;

public interface ParkingObserver {
  void update(ParkingEvent pe);
  void setStrategy(String strategy);
}
