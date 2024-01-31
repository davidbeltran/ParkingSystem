///////////////////////////////
// File: ParkingService.java
// Author: Instructor
///////////////////////////////
package parkingsystem.clients;

@SuppressWarnings("serial")
public class CommandException extends RuntimeException {
	public CommandException(String message) {
		super(message);
	}
}
