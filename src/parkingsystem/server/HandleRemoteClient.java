///////////////////////////////
// File: HandleRemoteClient.java
// Author: Instructor, modified by David Beltran
///////////////////////////////

package parkingsystem.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import parkingsystem.communications.CommandResponse;

public class HandleRemoteClient implements Runnable{
  private Socket client;
  private ParkingService service;
  private static final Logger logger = Logger.getLogger(Server.class.getName());
  
  public HandleRemoteClient(Socket s, ParkingService service) {
    this.client = s;
    this.service = service;
  }
  
  @Override
  public void run() {
    try (ObjectOutputStream osw = new ObjectOutputStream(client.getOutputStream());
        InputStream isw = client.getInputStream();
        ) {
        CommandResponse output;
        try {
          synchronized(service) {
            output = service.handleInput(client.getInputStream());
            System.out.println(Arrays.toString(output.getCommandParameters()));
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          output = new CommandResponse(401, "Exception");
          output.addMessage(ex.getMessage());
        }
        osw.writeObject(output);
    } catch (IOException e) {
        logger.log(Level.WARNING, "Failed to read from client.", e);
        e.printStackTrace();
    } finally {
      try {
        client.close();
      } catch (IOException e) {
        logger.log(Level.WARNING, "Failed to close client socket.", e);
      }
    }
  }
}
