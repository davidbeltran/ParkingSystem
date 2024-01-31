/* 
Programmer: David Beltran
File: Command.java
*/
/*
 This file contains the Command Interface used to 
 organize the methods for the RegisterCustomerCommand()
 and RegisterCarCommand() classes of the Parking System
 Application 
*/

package parkingsystem.command;

import java.util.Properties;

// Command interface that organizes non-initialized methods
public interface Command {
  String getCommandName();
  String getDisplayName();
  String execute(Properties params);
}
