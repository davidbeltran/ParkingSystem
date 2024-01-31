/////////////////////////////
// File: ServerClient.java
// Author: Instructor, modified by David Beltran
/////////////////////////////
package parkingsystem.clients;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import parkingsystem.communications.CommandRequest;
import parkingsystem.communications.CommandResponse;

public class ServerClient {

  // The displayCommands will be sorted by key order in the GUI, so alphebetizing is good.
  // First 2 characters are stripped from description to button label
  public static final String[][] COMMANDS = new String[][]{
    {"a. Register Customer", "CUSTOMER", "Name", "Phone", "StreetAddress", "Apt#", "City", 
      "State", "ZipCode"},
    {"b. Register Vehicle", "CAR", "License", "Customer", "Type"}, 
    {"c. Stop server", "STOP"}/*,
    {"c. Parking", "PARK", "Lot", "Permit Id", "Time"},
    {"d. Get Charges", "CHARGES", "Customer", "Car"}*/
  };

  // This must match the server's selected port and host
  private static final int PORT = 7777;
  private static final String SERVER = "localhost";

  private ServerClient() {
  }

  public static CommandRequest parseCommand(String[] request) {    
    // There must be a command
    if ( request.length == 0 ) {
      System.err.println("No command present");
      return null;
    }
    
    // First parameter is special: It must match a valid in the commands table
    String commandName = null;
    String[] parameters = null;
    for ( String[] command: COMMANDS ) {
      if (command[1].equalsIgnoreCase(request[0].trim())) {
        commandName = command[1];
        parameters = new String[command.length - 2];
        for (int j=1; j < request.length; j++) {
          String[] parts = request[j].split("=");
          if ( parts.length == 2 ) {  
            for (int k=0; k<parameters.length; k++) {
              if ( command[k+2].equalsIgnoreCase(parts[0].trim())) {
                parameters[k] = parts[1].trim().toLowerCase();
              }
            }
          } else {
            System.err.println("Bad parameter "+request[j]);
          }
        }
        return new CommandRequest(commandName,parameters);
      }
    }
    System.err.println("Command "+request[0]+" not recognized");
    return null;
  }
  
  public static CommandResponse runCommand(CommandRequest commandRequest)
      throws IOException, ClassNotFoundException {

    InetAddress host = InetAddress.getByName(SERVER);
    CommandResponse response = null;
    try ( Socket link = new Socket(host, PORT);
         ObjectOutputStream osw = new ObjectOutputStream(link.getOutputStream());
         ObjectInputStream isw = new ObjectInputStream(link.getInputStream());
        ) {

      // connect to server
      System.out.println("You are now connected to: " + host.getHostAddress());

      Object o = commandRequest;
      System.out.println(" Sending a " + commandRequest.getCommandName() + " request...");
      osw.writeObject(o);
      
      Object obj = isw.readObject();
      System.out.println(" Receiving a " + commandRequest.getCommandName() + " response:");
      
      if (obj instanceof CommandResponse) {
        response = (CommandResponse)obj;
      }
      System.out.println(Arrays.toString(response.getCommandParameters()));
    }
    return response;

  }

  public static Map<String, CommandRequest> commands() {
    Map<String, CommandRequest> commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    for (String[] description : COMMANDS) {
      commands.put(description[1], new CommandRequest(description[1], new String[0]));
    }
    return commands;
  }
  
  /**
   * Run this as: $ java ict4315.client.Client COMMAND label1=value1 label2=value2 ... Use LIST to
   * get the list of commands and their labels.
   */
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    if (args.length == 0 || args[0].equals("LIST")) {
      System.out.println("Here are the commands we know about.");
      System.out.println(
          "Usage: $ java " + ServerClient.class.getName() + 
          " COMMAND label1=value1 label2=value2 ...");
      System.out.println();
      for (String[] description : COMMANDS) {
        System.out.format("%s: %s ", description[0], description[1]);
        for (int i = 2; i < description.length; ++i) {
          System.out.format("%s=\"value\" ", description[i].replaceAll(" ", "").toLowerCase());
        }
        System.out.println();
      }
      return;
    }

    CommandRequest command = commands().get(args[0]);
    if (command == null) {
      System.out.println("Unrecognised command: " + args[0]);
      System.out.print("Known commands: ");
      String comma = "";
      for (String[] description : COMMANDS) {
        System.out.print(comma + description[1]);
        comma = ", ";
      }
      System.out.println();
      return;
    }
    
    CommandRequest request = parseCommand(args);
    System.out.println("Request:\n " + request.getCommandName());
    if (request != null) {
      @SuppressWarnings("unused")
      CommandResponse response = ServerClient.runCommand(request);
    }
  }
}
