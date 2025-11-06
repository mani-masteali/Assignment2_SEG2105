package edu.seg2105.edu.server.backend;
import java.util.Scanner;
import edu.seg2105.client.common.ChatIF;

public class ServerConsole implements ChatIF {
	
	final public static int DEFAULT_PORT = 5555;
	private EchoServer server;
	private Scanner fromConsole;
	
	public ServerConsole(int port) {
		server = new EchoServer(port);
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
				String serverMsg = "Sercer MSG> "+ msg;
				display(serverMsg);
				server.sendToAllClients(serverMsg);
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
