
import java.util.*;

import java.rmi.RemoteException;

public interface IServer extends java.rmi.Remote {

	void updateBootStrapingNode(String ip) throws RemoteException;
	String giveBootStrapNode() throws RemoteException;
	void updateNumberOfPeers() throws RemoteException;	
	int getNumberOfPeersConnected() throws RemoteException;

}