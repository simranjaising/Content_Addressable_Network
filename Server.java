import java.util.*;
import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
/**
 * This is the Server class that serves as an entry point to peers
 * 
 * 
 * @version $Id: 1
 * 
 * 
 * @author Simran Jaising
 * 
 * 
 */
public class Server extends UnicastRemoteObject implements IServer,
		Serializable {

	String bootStrappingNodeIP = null;
	static int noOfPeersConnected;

	/**
	 * 
	 * @throws RemoteException
	 */
	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void updateNumberOfPeers() throws RemoteException {
		noOfPeersConnected++;
	}

	public int getNumberOfPeersConnected() throws RemoteException {
		return noOfPeersConnected;
	}

	public void updateBootStrapingNode(String ip) throws RemoteException {

		System.out.println("BootStrapping Node IP is" + ip);
		bootStrappingNodeIP = ip;
	}

	public String giveBootStrapNode() throws RemoteException {
		//System.out.println("Peer Connecting");
		return bootStrappingNodeIP;
	}

	public static void main(String args[]) {

		try {
			// Creates AddServer object
			Server obj = new Server();
			//System.out.print(InetAddress.getLocalHost());
			Registry reg = LocateRegistry.createRegistry(1818);
			// Binding the object in the rmi registry
			reg.rebind("BootStrapServer", obj);
			System.out.println("BootStrap Server bound in registry");
		} catch (Exception e) {
			System.out.println("Binding err: " + e.getMessage());
			e.printStackTrace();
		}
	}

}