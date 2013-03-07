package rmitester;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import rmitester.RMIStartup.RegistryFactory;

public class RMITestClient extends UnicastRemoteObject implements ClientInterface {

	protected RMITestClient() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = -6639952078940474123L;
	
	protected static RegistryFactory factory = new RegistryFactory();
	
	@Override
	public void notifyAdded(String msg) throws RemoteException {
		System.out.println("Client got response message: " + msg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Usage: java rmitester.RMITestClient server_host");
			return;
		}
		
		RMIStartup.configureRmi(null, "/allow_all.policy");
		
		Registry registry = null;
		try {
			registry = factory.getRegistry(args[0]);
		} catch (RemoteException e) {
			System.out.println("Failed to find registry at host: " + args[0]);
			System.out.println(e.toString());
			return;
		}
		
		ServerInterface server = null;
		try {
			server = (ServerInterface) registry.lookup(ServerInterface.SERVER_NAME);
		} catch (Exception e) {
			System.out.println("Failed to lookup server at host: " + args[0]);
			System.out.println(e.toString());
			return;
		}
		
		RMITestClient client = null;
		try {
			client = new RMITestClient();
		} catch (RemoteException e) {
			System.out.println("Failed to create new client.");
			System.out.println(e.toString());
			return;
		}
		
		try {
			server.addClient(client);
		} catch (Exception e) {
			System.out.println("Failed to call method on server.");
			System.out.println(e.toString());
			return;
		}
		
		Scanner sysin = new Scanner(System.in);
		while(!sysin.hasNextLine() || !sysin.nextLine().equals("quit")) {}
		
		try {
			UnicastRemoteObject.unexportObject(client, true);
		} catch (NoSuchObjectException e) {
			System.out.println("Failed to unexport client.");
			System.out.println(e.toString());
		}
	}

}
