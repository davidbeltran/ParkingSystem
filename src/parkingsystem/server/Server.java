//////////////////////////
// File: Server.java
// Author: R Judd, modified by M I Schwartz and David Beltran
// This file implements an Object message-oriented server to allow clients to send
// commands to the Parking Office server.
//////////////////////////
package parkingsystem.server;

import parkingsystem.Address;
import parkingsystem.ParkingOffice;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

public class Server {

  static {
    System.setProperty(
        "java.util.logging.SimpleFormatter.format", 
        "%1$tc %4$-7s (%2$s) %5$s %6$s%n");
  }

  private static final Logger logger = Logger.getLogger(Server.class.getName());

  // Pick a TCP/IP Port
  private final int PORT = 7777;

  private final ParkingService service;

  private Duration cumulativeDuration = Duration.ZERO;
  private int countConnections = 0;
  private static volatile boolean doContinue = true;
  
  public Server(ParkingService service) {
    this.service = service;
  }

  public void startServer() throws IOException, Exception {
    logger.info("Starting server: " + InetAddress.getLocalHost().getHostAddress());
    try ( ServerSocket serverSocket = new ServerSocket(PORT)) {
      serverSocket.setReuseAddress(true);
      while (doContinue) {
        Socket client = serverSocket.accept();
        Instant start = Instant.now();
        Runnable r = new HandleRemoteClient(client, this.service);
        new Thread(r).start();
        Instant done = Instant.now();
        cumulativeDuration = cumulativeDuration.plus(Duration.between(start, done));
        countConnections++;
      }

      System.out.println("Number of threads: " + Thread.activeCount());
      System.out.println("Handled "+countConnections+" connections in "+cumulativeDuration);
      if ( countConnections > 0 ) {
        System.out.println("    "+
          (cumulativeDuration.toNanos() / countConnections) +
          " ns. per connection");
      }
    }
  }

  public static void stopServer() {
    doContinue = false;
    Thread.currentThread().interrupt();
  }
  
  /**
   * Run this as: $ java ict4315.server.Server
   */
  public static void main(String[] args) throws Exception {
    Address address = new Address("2130 S. High St.", "", "Denver", "CO", "80210");
    ParkingOffice parkingOffice = new ParkingOffice("DU Parking Office -- Test", address);

    ParkingService service = new ParkingService(parkingOffice);

    new Server(service).startServer();
  }
}
