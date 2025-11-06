package edu.seg2105.edu.server.backend;
import java.io.IOException;
import java.util.Scanner;
import edu.seg2105.client.common.ChatIF;

public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 5555;
	private EchoServer server;
	private Scanner fromConsole;
	
	public ServerConsole(EchoServer server) {
		this.server = server;
		fromConsole= new Scanner(System.in);
	}
	
	@Override
	public void display(String message) {
		System.out.println("> "+message);
	}
	public void accept() {
		try {
			while(true) {
			
				String msg = fromConsole.nextLine();
				
				if(msg.startsWith("#")) {
					String[] line = msg.split(" ");
					String command = line[0];
					switch (command) {
					case "#quit":
						server.close();
						System.exit(0);
						break;
					case "#stop":
						server.stopListening();
						display("Server stopped listening for new clients.");
						break;
					case "#close":
						try {
							server.close();
							display("Server closed. All clients disconnected.");
						}
						catch (IOException e) {
							display("Error closing server: "+e.getMessage());
						}
						break;
					case "#setport":
						if (server.isListening() || server.getNumberOfClients()>0) {
							display("Error: Server must be closed to set port.");
						}
						else if (line.length<2)
						{
							display("Usage: #setport <port>");
						}
						else {
							int newPort = Integer.parseInt(line[1]);
							server.setPort(newPort);
							display("Port set to "+ newPort);
						}
						break;
					case "start":
						if(server.isListening()) {
							display("Error: Server already listening");
						}
						else {
							try {
							server.listen();
							display("Server started listening for new clients.");
							}
							catch(IOException e) {
								display("Error: Could not start server");
							}
						}
						break;
					case "getport":
						display("Current port: "+server.getPort());
						break;
					}
					
				}
				
				else {
				String serverMsg = "SERVER MSG> "+ msg;
				display(serverMsg);
				server.sendToAllClients(serverMsg);
				}
			}
		}
		catch (Exception ex){
			System.out.println("Unexecepted error while reading from console.");
		}
	}
	public EchoServer getServer() {
		return server;
	}
}
