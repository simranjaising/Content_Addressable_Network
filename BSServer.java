import java.util.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * 
 * @author Amit Nadkarni
 *
 */
public class BSServer extends UnicastRemoteObject implements IBootStrapServer,
		Serializable {

	String bootStrappingNodeIP = null;

	/**
	 * 
	 * @throws RemoteException
	 */
	protected BSServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * updates bootStrappingNodeIP
	 */
	public void updateBootStrapingNode(String ip) {

		System.out.println("BootStrapping Node IP is" + ip);
		bootStrappingNodeIP = ip;
	}

	/**
	 * returns the bootStrappingNodeIP
	 */
	public String giveBootStrapNode() {
		System.out.println("Peer Connecting");
		return bootStrappingNodeIP;
	}

	/**
	 * the main of server class
	 * @param args
	 */
	public static void main(String args[]) {

		try {
			// Creates AddServer object
			BSServer obj = new BSServer();
			Registry reg = LocateRegistry.createRegistry(13003);
			// Binding the object in the rmi registry
			reg.rebind("BootStrapServer", obj);
			System.out.println("BootStrap Server bound in registry");
		} catch (Exception e) {
			System.out.println("Binding err: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
