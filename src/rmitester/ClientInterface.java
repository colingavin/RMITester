package rmitester;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
	public void notifyAdded(String msg) throws RemoteException;
}
