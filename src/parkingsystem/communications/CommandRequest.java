/*
Programmer: Michael Schwartz M.S., modified by David Beltran
File: CommandRequest.java 
*/

package parkingsystem.communications;

import java.io.Serializable;

public final class CommandRequest implements Serializable{
  private static final long serialVersionUID = -5353857348351607617L;
  private final String commandName;
  private final String[] commandParameters;
  
  public CommandRequest(String cmd, String[] param) {
    this.commandName = cmd;
    this.commandParameters = param;
  }
  
  public String getCommandName() {
    return this.commandName;
  }
  
  public String[] getCommandParameters() {
    return this.commandParameters;
  }
}
