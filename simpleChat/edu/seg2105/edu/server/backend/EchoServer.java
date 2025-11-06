package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  String message = msg.toString();
	  if (message.startsWith("#login")) {
		  String[] line = message.split(" ");
		  if(line.length<2) {
			  try {
				  client.sendToClient("ERROR: Login ID not specified. Connection closing.");
				  client.close();
			  }
			  catch(Exception e){
				  System.out.println("Error closing client after invalid login");
			  }
			  return;
		  }
		  if (client.getInfo("loginId")!=null) {
			  try {
				  client.sendToClient("ERROR: Already logged in. Connection closing.");
				  client.close();
			  }
			  catch(Exception e) {
				  System.out.println("Error closing duplicate login client.");
			  }
			  return;
		  }
		  String loginId = line[1];
		  client.setInfo("loginId", loginId);
		  System.out.println("Client logged in as "+loginId);
		  return;
	  }
	  Object id = client.getInfo("loginId");
	  if (id == null) {
		  try {
			  client.sendToClient("ERROR: Must login before sending messages. Connection closing.");
			  client.close();
		  }
		  catch (Exception e) {
			  System.out.println("Error closing unlogged client.");
		  }
		  return;
	  }
	String taggedMessage = id + ": " + message;
    System.out.println("Message received: " + message + " from " + id);
    this.sendToAllClients(taggedMessage);
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("Client "+client+" has connected");
  }
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  Object loginId = client.getInfo("loginId");
	  System.out.println("Client "+(loginId!=null ? loginId: "unknown")+" has disconnected");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    
    EchoServer sv = new EchoServer(port);
    ServerConsole serverConsole = new ServerConsole(sv);
    
    try 
    {
      sv.listen(); //Start listening for connections
      serverConsole.accept();
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
