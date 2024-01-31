/* 
Programmer: David Beltran
File: Money.java
*/
/*
 This file contains the Money class for the Parking System 
 that takes a number of cents and turns into a dollar amount.
*/
package parkingsystem;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Objects;

public class Money {
  private double cents;
  private static DecimalFormat df2 = new DecimalFormat("#.##");//formats money two decimal places
  private Currency currency;
  
  // Money constructor
  public Money(double cents) {
    this.cents = cents;
    this.currency = Currency.getInstance("USD");
  }
  
  //hashCode() and equals() overriden to override ParkingCharge hasCode() and equals()
  @Override
  public int hashCode() {
    return Objects.hash(cents);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Money)) {
      return false;
    }
    Money other = (Money) obj;
    return Double.doubleToLongBits(cents) == Double.doubleToLongBits(other.cents);
  }

  public double getCents() {// added for getParkingCharges() in ParkingOffice
    return this.cents;
  }
  
  // turns cents into dollar quantity
  public double getDollars() {
    return this.cents / 100.0;
  }

  public String toString() {
    String dollars = "" + df2.format(getDollars());
    return this.currency.getSymbol() + dollars;
  }
}
