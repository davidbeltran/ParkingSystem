/*
Programmer: Michael Schwartz M.S., modified by David Beltran
File: CommandResponse.java 
*/

package parkingsystem.communications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class CommandResponse implements Serializable{
  private static final long serialVersionUID = 5953764915431919727L;
  private int responseCode;
  private String responseText;
  private List<String> messages;
  
  public CommandResponse(int code, String cmd) {
    this.responseCode = code;
    this.responseText = cmd;
    this.messages = new ArrayList<>();
  }
  
  public int getResponseCode() {
    return this.responseCode;
  }
  
  public String getResponseText() {
    return this.responseText;
  }
  
  public void addMessage(String msg) {
    this.messages.add(msg);
  }
  
  public String[] getCommandParameters() {
    return messages.toArray(new String[0]);
  }
}
