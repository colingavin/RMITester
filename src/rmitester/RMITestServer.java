package rmitester;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Scanner;

import rmitester.RMIStartup;
import rmitester.RMIStartup.RegistryFactory;

public class RMITestServer implements ServerInterface {

	protected static RegistryFactory factory = new RegistryFactory();
	
	public static void main(String[] args) {
		RMIStartup.configureRmi(RMITestServer.class, "/allow_all.policy");
		
		RMITestServer server = new RMITestServer();
		
		try {
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);
			
			Registry registry = factory.getRegistry();
			registry.rebind(ServerInterface.SERVER_NAME, stub);
			
			System.out.println("Server successfully started");
		} catch (Exception e) {
			System.out.println("Error publishing server.");
			System.out.println(e.toString());
		}
		
		Scanner sysin = new Scanner(System.in);
		while(!sysin.hasNextLine() || !sysin.nextLine().equals("quit")) {}
		
		try {
			UnicastRemoteObject.unexportObject(server, true);
		} catch (NoSuchObjectException e) {
			System.out.println("Failed to unexport server.");
			System.out.println(e.toString());
		}
	}

	@Override
	public void addClient(ClientInterface client) throws RemoteException {
		System.out.println("Got addClient.");
		try {
			client.notifyAdded("You were added at " + new Date().toString() + ".");
		} catch (Exception e) {
			System.out.println("Error calling back to client: " + client.toString());
			System.out.println(e.toString());
			return;
		}
		System.out.println("Responded successfully.");
	}

}
