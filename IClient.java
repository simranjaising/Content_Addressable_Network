import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is the client interface that helps connect to other peers
 * 
 * 
 * @version $Id: 1
 * 
 * 
 * @author Simran Jaising
 * 
 * 
 */
public interface IClient extends java.rmi.Remote {
	// public double doubleRandomInclusive(double max, double min);

	public boolean checkIfPointInZone(double x, double y, double upperRightY,
			double upperRightX, double lowerLeftY, double lowerLeftX)
			throws RemoteException;

	// public double getUpperLeftX();

	public double getUpperRightY() throws RemoteException;

	public void setUpperRightY(double upperRightY) throws RemoteException;

	public double getUpperRightX() throws RemoteException;

	public void setUpperRightX(double upperRightX) throws RemoteException;

	// public void setUpperLeftY();
	//
	// public double getUpperLeftY();
	//
	// public void setUpperLeftX(double upperLeftX);

	public double getLowerLeftX() throws RemoteException;

	public void setLowerLeftX(double lowerLeftX) throws RemoteException;

	public double getLowerLeftY() throws RemoteException;

	public void setLowerLeftY(double lowerLeftY) throws RemoteException;

	// public double getLowerRightX();
	//
	// public void setLowerRightX();
	//
	// public double getLowerRightY();
	//
	// public void setLowerRightY();

	public void updateNeighbour(Neighbour c) throws RemoteException;

	public void removeNeighbour(String n) throws RemoteException;

	public ArrayList<Neighbour> getNeighbour() throws RemoteException;

	public void setNeighbour(ArrayList<Neighbour> neighbour)
			throws RemoteException;

	public String getIpAddress() throws RemoteException;

	public void setIpAddress(String ipAddress) throws RemoteException;

	public void printNeighbours() throws RemoteException;

	public HashMap<String, byte[]> getFileList() throws RemoteException;

	public void setFileList(HashMap<String, byte[]> fileList)
			throws RemoteException;

	public void updateFileList(String keyword, byte[] fileContent)
			throws RemoteException;

	public byte[] getFileContent(String keyword) throws RemoteException;

	public boolean searchFile(String keyword) throws RemoteException;

	public void removeFile(String keyword) throws RemoteException;

	public double getCentreY() throws RemoteException;

	public double getCentreX() throws RemoteException;

	public void printFileNames() throws RemoteException;
	
	public void viewPeer(String ip) throws IOException, NotBoundException;

}