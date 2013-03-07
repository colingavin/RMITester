package rmitester;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
	public static final String SERVER_NAME = "RMITestServer";
	
	public void addClient(ClientInterface client) throws RemoteException;
}
