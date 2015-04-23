import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This is the client class that helps connect to other peers
 * 
 * 
 * @version $Id: 1
 * 
 * 
 * @author Simran Jaising
 * 
 * 
 */

public class Client extends UnicastRemoteObject implements IClient,
		Serializable {

	private static final long serialVersionUID = 1L;
	public static double UpperLeftXLimit = 0;
	public static double UpperLeftYLimit = 10.0;
	public static double UpperRightYLimit = 10.0;
	public static double UpperRightXLimit = 10.0;
	public static double LowerLeftXLimit = 0.0;
	public static double LowerLeftYLimit = 0;
	public static double LowerRightXLimit = 0;
	public static double LowerRightYLimit = 0;
	public double UpperRightY;
	public double UpperRightX;
	public double LowerLeftX;
	HashMap<String, byte[]> fileList = new HashMap<String, byte[]>();

	public double getUpperRightY() throws RemoteException {
		return UpperRightY;
	}

	public void setUpperRightY(double upperRightY) throws RemoteException {
		UpperRightY = upperRightY;
	}

	public double getUpperRightX() throws RemoteException {
		return UpperRightX;
	}

	public void setUpperRightX(double upperRightX) throws RemoteException {
		UpperRightX = upperRightX;
	}

	public double getLowerLeftX() throws RemoteException {
		return LowerLeftX;
	}

	public void setLowerLeftX(double lowerLeftX) throws RemoteException {
		LowerLeftX = lowerLeftX;
	}

	public double getLowerLeftY() throws RemoteException {
		return LowerLeftY;
	}

	public void setLowerLeftY(double lowerLeftY) throws RemoteException {
		LowerLeftY = lowerLeftY;
	}

	public void setNeighbour(ArrayList<Neighbour> neighbour)
			throws RemoteException {
		this.neighbour = neighbour;
	}

	public String getIpAddress() throws RemoteException {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) throws RemoteException {
		this.ipAddress = ipAddress;
	}

	public double LowerLeftY;
	public static int totalPeers;
	public ArrayList<Neighbour> neighbour = new ArrayList<Neighbour>();
	public static IServer bsServer;
	public String ipAddress;

	public Client(double upperRightY, double upperRightX, double lowerLeftY,
			double lowerLeftX, ArrayList<Neighbour> neighbour, String ipAddress)
			throws RemoteException {
		super();

		UpperRightY = upperRightY;
		UpperRightX = upperRightX;
		LowerLeftX = lowerLeftX;
		LowerLeftY = lowerLeftY;
		this.neighbour = neighbour;
		this.ipAddress = ipAddress;
	}

	public Client() throws RemoteException, UnknownHostException,
			NotBoundException, AlreadyBoundException {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		try {

			System.out.println("Please enter the bootstrap server IP.");
			BufferedReader bufferRead = new BufferedReader(
					new InputStreamReader(System.in));

			String bootsrapIP = bufferRead.readLine();
			Registry registry;
			Client client = new Client();
			// System.setSecurityManager(new SecurityManager());
			registry = LocateRegistry.getRegistry(bootsrapIP, 1818);
			// System.out.println(registry.toString());
			bsServer = (IServer) registry.lookup("BootStrapServer");
			// System.out.println("Connexted" + bsServer);
			InetAddress addr = InetAddress.getLocalHost();
			Registry reg = LocateRegistry.createRegistry(1818);
			client.chooseAction();

		} catch (Exception e) {
			System.out.println("HelloC exception: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void chooseAction() throws NumberFormatException, IOException,
			NotBoundException, AlreadyBoundException {
		while (true) {
			System.out.println("Choose one of the following actions: ");
			System.out.println("1. Join");
			System.out.println("2. Insert a File");
			System.out.println("3. Leave");
			System.out.println("4. Search");
			System.out.println("5. View Peer by IP Address");
			System.out.println("6. Exit");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					System.in));
			int action = Integer.parseInt(buffer.readLine());
			switch (action) {
			case 1:
				join();
				break;
			case 2:
				insert();
				break;
			case 3:
				leave();
				break;
			case 4:
				search();
				break;
			case 5:
				System.out
						.println("Please enter the IP of the peer that you want to view ");

				BufferedReader bufferRead = new BufferedReader(
						new InputStreamReader(System.in));

				String ip = bufferRead.readLine();
				Registry reg8 = LocateRegistry.getRegistry(ip, 1818);
				IClient peer = (IClient) reg8.lookup("peer");
				peer.viewPeer(peer.getIpAddress());
				break;
			case 6:
				System.exit(0);
			default:
				System.out.println("Enter a correct command");
			}
		}

	}

	/**
	 * This method makes the given peer leave the system
	 * 
	 */
	private void leave() throws IOException, NotBoundException {

		System.out
				.println("Please enter the IP of the peer that you want to remove ");

		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));

		String leavingIP = bufferRead.readLine();
		String bootstrapIP = bsServer.giveBootStrapNode();

		Registry reg8 = LocateRegistry.getRegistry(leavingIP, 1818);
		IClient leavingPeer = (IClient) reg8.lookup("peer");
		// System.out.println("Leaving " + leavingPeer.getIpAddress());

		ArrayList<Neighbour> neighbours = leavingPeer.getNeighbour();
		ArrayList<Neighbour> neighboursToArrange = leavingPeer.getNeighbour();
		double topMidPtEdgeY = 0;
		double topMidPtEdgeX = 0;
		double bottomMidPtEdgeY = 0;
		double bottomMidPtEdgeX = 0;
		topMidPtEdgeX = (Client.UpperRightXLimit + Client.LowerLeftXLimit) / 2;
		topMidPtEdgeY = Client.UpperLeftYLimit;
		bottomMidPtEdgeY = Client.LowerLeftYLimit;
		bottomMidPtEdgeX = (Client.UpperRightXLimit + Client.LowerLeftXLimit) / 2;

		if (leavingPeer.getNeighbour().size() == 1) {
			Neighbour n1 = leavingPeer.getNeighbour().get(0);

			if (leavingIP.equals(bootstrapIP)) {
				bsServer.updateBootStrapingNode(n1.ipAddress);
				System.out.println("Updating bootstrap IP to :"
						+ bsServer.giveBootStrapNode());
			}

			Registry reg9 = LocateRegistry.getRegistry(n1.ipAddress, 1818);
			IClient neighbourPeer = (IClient) reg9.lookup("peer");
			// System.out.println("New n " + neighbourPeer.getIpAddress());

			neighbourPeer.setUpperRightY(UpperRightYLimit);
			neighbourPeer.setUpperRightX(UpperRightXLimit);
			neighbourPeer.setLowerLeftY(LowerLeftYLimit);
			neighbourPeer.setLowerLeftX(LowerLeftXLimit);
			neighbourPeer.removeNeighbour(leavingIP);

			for (String keyword : leavingPeer.getFileList().keySet()) {

				neighbourPeer.updateFileList(keyword,
						leavingPeer.getFileContent(keyword));

			}

			neighbourPeer.viewPeer(neighbourPeer.getIpAddress());

		}
		// System.out.print("size of neighbours" + neighbours.size());
		if (neighbours.size() > 1 && neighbours.size() <= 2) {
			// System.out.print("size of neighbours" + neighbours.size());
			double distance;
			double minDistance;
			boolean isBiggerPeer = false;
			Neighbour thirdNeighbour = new Neighbour();
			Neighbour neighbourFound = neighbours.get(0);
			distance = Math
					.sqrt((Math.pow(
							(neighbours.get(0).getCentreX() - leavingPeer
									.getCentreX()), 2) + (Math.pow((neighbours
							.get(0).getCentreY() - leavingPeer.getCentreY()), 2))));

			for (int i = 0; i < neighbours.size(); i++) {
				Neighbour neighbour = neighbours.get(i);
				if ((Math
						.sqrt((Math.pow((neighbour.getCentreX() - leavingPeer
								.getCentreX()), 2) + (Math.pow((neighbour
								.getCentreY() - leavingPeer.getCentreY()), 2))))) < distance) {
					neighbourFound = neighbour;
					minDistance = (Math
							.sqrt((Math.pow(
									(neighbour.getCentreX() - leavingPeer
											.getCentreX()), 2) + (Math.pow(
									(neighbour.getCentreY() - leavingPeer
											.getCentreY()), 2)))));

				}
				// System.out.println("neighbour found " + neighbourFound);
				if ((Math
						.sqrt((Math.pow((neighbour.getCentreX() - leavingPeer
								.getCentreX()), 2) + (Math.pow((neighbour
								.getCentreY() - leavingPeer.getCentreY()), 2))))) == distance) {
					// System.out.println("neighbour coming in ");
					isBiggerPeer = true;
					neighboursToArrange.add(neighbour);
				}

			}

			if (isBiggerPeer == true) {
				Registry reg9 = LocateRegistry.getRegistry(neighboursToArrange
						.get(0).getIpAddress(), 1818);
				IClient Peer1 = (IClient) reg9.lookup("peer");

				Registry reg10 = LocateRegistry.getRegistry(neighboursToArrange
						.get(1).getIpAddress(), 1818);
				IClient Peer2 = (IClient) reg10.lookup("peer");

				if (leavingIP.equals(bootstrapIP)) {
					bsServer.updateBootStrapingNode(Peer1.getIpAddress());
					System.out.println("Updating bootstrap IP to :"
							+ bsServer.giveBootStrapNode());
				}

				ArrayList<Neighbour> neighbours1 = new ArrayList<Neighbour>();
				ArrayList<Neighbour> neighbours2 = new ArrayList<Neighbour>();

				Neighbour newNeighbour1 = new Neighbour(topMidPtEdgeY,
						topMidPtEdgeX, Client.LowerLeftYLimit,
						Client.LowerRightXLimit, Peer1.getIpAddress());
				Neighbour newNeighbour2 = new Neighbour(
						Client.UpperRightYLimit, Client.UpperRightXLimit,
						bottomMidPtEdgeY, bottomMidPtEdgeX,
						Peer2.getIpAddress());

				Peer1.setUpperRightY(topMidPtEdgeY);
				Peer1.setUpperRightX(topMidPtEdgeX);
				Peer1.setLowerLeftY(Client.LowerLeftYLimit);
				Peer1.setLowerLeftX(Client.LowerRightXLimit);
				Peer1.removeNeighbour(leavingIP);
				neighbours1.add(newNeighbour2);
				Peer1.setNeighbour(neighbours1);

				Peer2.setUpperRightY(Client.UpperRightYLimit);
				Peer2.setUpperRightX(Client.UpperRightXLimit);
				Peer2.setLowerLeftY(bottomMidPtEdgeY);
				Peer2.setLowerLeftX(bottomMidPtEdgeX);
				Peer2.removeNeighbour(leavingIP);
				neighbours2.add(newNeighbour1);
				Peer2.setNeighbour(neighbours2);

				for (String keyword : leavingPeer.getFileList().keySet()) {

					int xFile = hashCodeX(keyword);
					int yFile = hashCodeX(keyword);

					if (checkIfPointInZone(xFile, yFile,
							Peer1.getUpperRightY(), Peer1.getUpperRightX(),
							Peer1.getLowerLeftY(), Peer1.getLowerLeftX()) == true) {
						Peer1.updateFileList(keyword,
								leavingPeer.getFileContent(keyword));
					} else {
						if (checkIfPointInZone(xFile, yFile,
								Peer2.getUpperRightY(), Peer2.getUpperRightX(),
								Peer2.getLowerLeftY(), Peer2.getLowerLeftX()) == true) {
							Peer2.updateFileList(keyword,
									leavingPeer.getFileContent(keyword));
						}
					}
				}

				Peer1.viewPeer(Peer1.getIpAddress());
				// System.out.print("Printing peer 2" + Peer2.getIpAddress());
				Peer2.viewPeer(Peer2.getIpAddress());

			} else {

				for (int j = 0; j < neighbours.size(); j++) {
					Neighbour neighbour1 = neighbours.get(j);
					if (neighbour1.getIpAddress().equals(
							neighbourFound.getIpAddress()) == false) {
						thirdNeighbour = neighbour1;
						System.out.println("Third n "
								+ thirdNeighbour.ipAddress);
					}
				}

				ArrayList<Neighbour> neighbours1 = new ArrayList<Neighbour>();
				ArrayList<Neighbour> neighbours2 = new ArrayList<Neighbour>();

				if (neighbourFound.LowerLeftY < leavingPeer.getLowerLeftY()) {

					Registry reg9 = LocateRegistry.getRegistry(
							neighbourFound.ipAddress, 1818);
					IClient neighbourPeer = (IClient) reg9.lookup("peer");
					// System.out.println("Neighbour found "
					// + neighbourPeer.getIpAddress());
					// neighbourPeer.printNeighbours();

					Registry reg10 = LocateRegistry.getRegistry(
							thirdNeighbour.ipAddress, 1818);
					IClient thirdNeighbourPeer = (IClient) reg10.lookup("peer");
					// System.out.println("Third Neighbour  "
					// + thirdNeighbourPeer.getIpAddress());
					// thirdNeighbourPeer.printNeighbours();

					if (leavingIP.equals(bootstrapIP)) {
						bsServer.updateBootStrapingNode(neighbourPeer
								.getIpAddress());
						System.out.println("Updating bootstrap IP to :"
								+ bsServer.giveBootStrapNode());
					}

					neighbourPeer.setUpperRightY(leavingPeer.getUpperRightY());
					neighbourPeer.setUpperRightX(leavingPeer.getUpperRightX());
					neighbourPeer.setLowerLeftY(neighbourPeer.getLowerLeftY());
					neighbourPeer.setLowerLeftX(neighbourPeer.getLowerLeftX());
					neighbourPeer.removeNeighbour(leavingIP);
					thirdNeighbourPeer.removeNeighbour(leavingIP);

					for (String keyword : leavingPeer.getFileList().keySet()) {

						neighbourPeer.updateFileList(keyword,
								leavingPeer.getFileContent(keyword));

					}

					neighbourPeer.viewPeer(neighbourPeer.getIpAddress());
					thirdNeighbourPeer.viewPeer(thirdNeighbourPeer
							.getIpAddress());

				} else {
					if (neighbourFound.LowerLeftY > leavingPeer.getLowerLeftY()) {

						Registry reg9 = LocateRegistry.getRegistry(
								neighbourFound.ipAddress, 1818);
						IClient neighbourPeer = (IClient) reg9.lookup("peer");
						// System.out.println("Neighbour found "
						// + neighbourPeer.getIpAddress());
						// neighbourPeer.printNeighbours();

						Registry reg10 = LocateRegistry.getRegistry(
								thirdNeighbour.ipAddress, 1818);
						IClient thirdNeighbourPeer = (IClient) reg10
								.lookup("peer");
						// System.out.println("Third Neighbour  "
						// + thirdNeighbourPeer.getIpAddress());
						// thirdNeighbourPeer.printNeighbours();

						if (leavingIP.equals(bootstrapIP)) {
							bsServer.updateBootStrapingNode(neighbourPeer
									.getIpAddress());
							System.out.println("Updating bootstrap IP to :"
									+ bsServer.giveBootStrapNode());
						}

						neighbourPeer.setUpperRightY(neighbourPeer
								.getUpperRightY());
						neighbourPeer.setUpperRightX(neighbourPeer
								.getUpperRightX());
						neighbourPeer
								.setLowerLeftY(leavingPeer.getLowerLeftY());
						neighbourPeer
								.setLowerLeftX(leavingPeer.getLowerLeftX());
						neighbourPeer.removeNeighbour(leavingIP);
						thirdNeighbourPeer.removeNeighbour(leavingIP);

						for (String keyword : leavingPeer.getFileList()
								.keySet()) {

							neighbourPeer.updateFileList(keyword,
									leavingPeer.getFileContent(keyword));

						}

						neighbourPeer.viewPeer(neighbourPeer.getIpAddress());
						thirdNeighbourPeer.viewPeer(thirdNeighbourPeer
								.getIpAddress());
					}
				}
			}

		}
	}

	public double getCentreY() throws RemoteException {
		return (this.LowerLeftY + this.UpperRightY) / 2;
	}

	public double getCentreX() throws RemoteException {
		return (this.LowerLeftX + this.UpperRightX) / 2;
	}

	public int hashCodeY(String keyword) {

		int sum = 0;
		int hashValueX = 0;
		for (int i = 0; i < keyword.length(); i = i + 2) {
			char c = keyword.charAt(i);
			sum = sum + ((int) c);
		}
		hashValueX = sum % 10;
		return hashValueX;

	}

	public int hashCodeX(String keyword) {
		int sum = 0;
		int hashValueX = 0;
		for (int i = 1; i < keyword.length(); i = i + 2) {
			char c = keyword.charAt(i);
			sum = sum + ((int) c);
		}
		hashValueX = sum % 10;
		return hashValueX;

	}

	/**
	 * This method inserts a file in the given peer
	 * 
	 */
	public void insert() throws IOException, NotBoundException {

		System.out
				.println("Please enter file name if in the same directory, else enter path");

		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String keyword = bufferRead.readLine();

		FileInputStream fileInputStream = null;

		File file = new File(keyword);

		byte[] fileContent = new byte[(int) file.length()];

		fileInputStream = new FileInputStream(file);
		fileInputStream.read(fileContent);
		fileInputStream.close();

		int locationX = hashCodeX(keyword);
		int locationY = hashCodeY(keyword);

		System.out.println("locationX " + locationX);
		System.out.println("locationY " + locationY);

		String destinationIP = findFileLocation(locationX, locationY,
				InetAddress.getLocalHost().getHostAddress().toString());

		Registry reg7 = LocateRegistry.getRegistry(destinationIP, 1818);
		IClient destinationPeer = (IClient) reg7.lookup("peer");

		// FileList newFile=new FileList(keyword,fileContent);
		destinationPeer.updateFileList(keyword, fileContent);

	}

	/**
	 * This method searches for a given file
	 * 
	 */

	public void search() throws IOException, NotBoundException {

		System.out
				.println("Please enter the name of the file you want to search for.");

		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));

		String keyword = bufferRead.readLine();
		int locationX = hashCodeX(keyword);
		int locationY = hashCodeY(keyword);

		System.out.println("locationX " + locationX);
		System.out.println("locationY " + locationY);

		String destinationIP = findFileLocation(locationX, locationY,
				InetAddress.getLocalHost().getHostAddress().toString());

		Registry reg7 = LocateRegistry.getRegistry(destinationIP, 1818);
		IClient destinationPeer = (IClient) reg7.lookup("peer");

		boolean isPresent = destinationPeer.searchFile(keyword);

		if (isPresent) {
			System.out.println("Destination Peer "
					+ destinationPeer.getIpAddress());
		} else {
			System.out.print("Failure");
		}

	}

	public HashMap<String, byte[]> getFileList() {
		return fileList;
	}

	public boolean searchFile(String keyword) {
		boolean isPresent = false;
		isPresent = getFileList().containsKey(keyword);
		return isPresent;
	}

	public void setFileList(HashMap<String, byte[]> fileList) {
		this.fileList = fileList;
	}

	public void updateFileList(String keyword, byte[] fileContent) {
		this.fileList.put(keyword, fileContent);
	}

	public static double doubleRandomInclusive(double max, double min)
			throws RemoteException {
		double r = Math.random();
		if (r < 0.5) {
			return ((1 - Math.random()) * (max - min) + min);
		}
		return (Math.random() * (max - min) + min);
	}

	/**
	 * This method makes a peer join the network
	 * 
	 */
	public void join() throws NotBoundException, AlreadyBoundException,
			IOException {
		try {
			// IServer bsServer = null;
			if (bsServer.giveBootStrapNode() == null) {
				Client firstPeer = new Client(UpperRightYLimit,
						UpperRightXLimit, LowerLeftYLimit, LowerLeftXLimit,
						null, InetAddress.getLocalHost().getHostAddress()
								.toString());
				Registry reg = LocateRegistry.getRegistry(1818);
				// Binding the object in the rmi registry
				reg.rebind("peer", firstPeer);
				bsServer.updateBootStrapingNode(InetAddress.getLocalHost()
						.getHostAddress().toString());
				System.out.println("First peer bound in regsitry");
				System.out.println("IP Address: " + firstPeer.getIpAddress());
				System.out.println("Peer Upper Right Y-Coordinate: "
						+ firstPeer.getUpperRightY());
				System.out.println("Peer Upper Right X-Coordinate: "
						+ firstPeer.getUpperRightX());
				System.out.println("Peer Lower Left Y-Coordinate: "
						+ firstPeer.getLowerLeftY());
				System.out.println("Peer Lower Left X-Coordinate: "
						+ firstPeer.getLowerLeftX());
				System.out.println("Peer Centre Y-Coordinate: "
						+ firstPeer.getCentreY());
				System.out.println("Peer Centre X-Coordinate: "
						+ firstPeer.getCentreX());
				System.out.println("Peer has no Neighbours");
				System.out.println();
				System.out.println("Peer has the no files ");

				// firstPeer.viewPeer(firstPeer.getIpAddress());
				// System.out.println(bsServer.giveBootStrapNode());
				return;
			} else {
				double randX = doubleRandomInclusive(10.0, 0.0);
				double randY = doubleRandomInclusive(10.0, 0.0);
				// System.out.println(randX + " , " + randY);
				if (bsServer.giveBootStrapNode() != null) {
					String bootstrapIP = bsServer.giveBootStrapNode();
					// System.out.println(bootstrapIP);
					Registry reg2 = LocateRegistry.getRegistry(bootstrapIP,
							1818);
					IClient peer = (IClient) reg2.lookup("peer");
					// System.out.println(peer.getIpAddress());
					if (peer.checkIfPointInZone(randX, randY,
							peer.getUpperRightY(), peer.getUpperRightX(),
							peer.getLowerLeftY(), peer.getLowerLeftX()) == true) {
						// System.out.println("peer.getUpperRightY() "
						// + peer.getUpperRightY() + " peer.getUpperRightX() "
						// + peer.getUpperRightX() + " peer.getLowerLeftY() "
						// + peer.getLowerLeftY() + "peer.getLowerLeftX() "
						// + peer.getLowerLeftX());
						// System.out.println("Second peer found in zone of P1");
						split(randX, randY, peer.getIpAddress());
						// System.out.println("peer.getUpperRightY() "
						// + peer.getUpperRightY() + " peer.getUpperRightX() "
						// + peer.getUpperRightX() + " peer.getLowerLeftY() "
						// + peer.getLowerLeftY() + "peer.getLowerLeftX() "
						// + peer.getLowerLeftX());
					} else {
						this.route(randX, randY, bsServer.giveBootStrapNode());
					}

				}

			}
		} catch (Exception e) {
			System.out.print("Failure");
		}

	}

	public boolean checkIfPointInZone(double x, double y, double upperRightY,
			double upperRightX, double lowerLeftY, double lowerLeftX)
			throws RemoteException {
		boolean isInZone = false;
		if ((x >= lowerLeftX && x <= upperRightX)
				&& (y >= lowerLeftY && y <= upperRightY)) {
			isInZone = true;
		}
		return isInZone;

	}

	/**
	 * This splits a peer into the original and new peer
	 * 
	 */
	public void split(double x, double y, String ipAddress)
			throws NotBoundException, AlreadyBoundException, IOException {
		String myIp = InetAddress.getLocalHost().getHostAddress().toString();
		Registry reg2 = LocateRegistry.getRegistry(ipAddress, 1818);
		IClient peer = (IClient) reg2.lookup("peer");
		double rightMidPtEdgeX = 0;
		double rightMidPtEdgeY = 0;
		double leftMidPtEdgeX = 0;
		double leftMidPtEdgeY = 0;
		double topMidPtEdgeY = 0;
		double topMidPtEdgeX = 0;
		double bottomMidPtEdgeY = 0;
		double bottomMidPtEdgeX = 0;
		rightMidPtEdgeX = peer.getUpperRightX();
		rightMidPtEdgeY = (peer.getUpperRightY() + peer.getLowerLeftY()) / 2;
		leftMidPtEdgeX = peer.getLowerLeftX();
		leftMidPtEdgeY = (peer.getUpperRightY() + peer.getLowerLeftY()) / 2;
		topMidPtEdgeX = (peer.getLowerLeftX() + peer.getUpperRightX()) / 2;
		topMidPtEdgeY = peer.getUpperRightY();
		bottomMidPtEdgeY = peer.getLowerLeftY();
		bottomMidPtEdgeX = (peer.getLowerLeftX() + peer.getUpperRightX()) / 2;
		double midX = 0.0;
		double midY = 0.0;

		Registry reg3 = LocateRegistry.getRegistry(1818);
		ArrayList<Neighbour> tempNeighbourPeerOne = new ArrayList<Neighbour>();
		ArrayList<Neighbour> tempNeighbourPeerTwo = new ArrayList<Neighbour>();

		// when there is only one peer in the table
		if (peer.getNeighbour() == null) {
			Client newPeer = new Client();

			// Setting new peer new coordinates to the lower peer
			newPeer.setUpperRightY(topMidPtEdgeY);
			newPeer.setUpperRightX(topMidPtEdgeX);
			newPeer.setLowerLeftY(peer.getLowerLeftY());
			newPeer.setLowerLeftX(peer.getLowerLeftX());
			newPeer.setIpAddress(myIp);

			Neighbour n2 = new Neighbour(peer.getUpperRightY(),
					peer.getUpperRightX(), bottomMidPtEdgeY, bottomMidPtEdgeX,
					ipAddress);

			tempNeighbourPeerTwo.add(n2);
			newPeer.setNeighbour(tempNeighbourPeerTwo);

			// Registry reg3 = LocateRegistry.getRegistry();
			reg3.rebind("peer", newPeer);

			// Setting orginal peer new coordinates to the upper peer

			Neighbour n1 = new Neighbour(topMidPtEdgeY, topMidPtEdgeX,
					peer.getLowerLeftY(), peer.getLowerLeftX(), myIp);

			peer.setUpperRightY(peer.getUpperRightY());
			peer.setUpperRightX(peer.getUpperRightY());
			peer.setLowerLeftY(bottomMidPtEdgeY);
			peer.setLowerLeftX(bottomMidPtEdgeX);

			tempNeighbourPeerOne.add(n1);
			peer.setNeighbour(tempNeighbourPeerOne);

			// peer.printNeighbours();
			newPeer.viewPeer(newPeer.getIpAddress());

		} else {
			Client newPeer = new Client();
			if (peer.getNeighbour() != null) {

				// System.out.println("Entering neighbour list not null");
				// System.out.println("peer ip " + ipAddress);

				if ((peer.getUpperRightY() - peer.getLowerLeftY()) > (peer
						.getUpperRightX() - peer.getLowerLeftX())) {

					midY = rightMidPtEdgeY;
					// setting new peer at the bottom and updating neighbours
					// and co-ords
					newPeer.setIpAddress(myIp);
					newPeer.setUpperRightY(rightMidPtEdgeY);
					newPeer.setUpperRightX(rightMidPtEdgeX);
					newPeer.setLowerLeftY(peer.getLowerLeftY());
					newPeer.setLowerLeftX(peer.getLowerLeftX());
					newPeer.setNeighbour(tempNeighbourPeerTwo);

					Neighbour n2 = new Neighbour(peer.getUpperRightY(),
							peer.getUpperRightX(), leftMidPtEdgeY,
							leftMidPtEdgeX, ipAddress);

					newPeer.updateNeighbour(n2);

					// Registry reg3 = LocateRegistry.getRegistry();
					reg3.rebind("peer", newPeer);

					// setting old peer at the top and updating neighbours and
					// co-ords

					Neighbour n1 = new Neighbour(rightMidPtEdgeY,
							rightMidPtEdgeX, peer.getLowerLeftY(),
							peer.getLowerLeftX(), myIp);

					peer.setUpperRightY(peer.getUpperRightY());
					peer.setUpperRightX(peer.getUpperRightX());
					peer.setLowerLeftY(leftMidPtEdgeY);
					peer.setLowerLeftX(leftMidPtEdgeX);

					peer.updateNeighbour(n1);

					// peer.printNeighbours();
					// newPeer.viewPeer(newPeer.getIpAddress());
					// IClient t = (IClient) reg3.lookup("peer");
					// t.printNeighbours();

				}

				else {
					if ((peer.getUpperRightX() - peer.getLowerLeftX()) >= (peer
							.getUpperRightY() - peer.getLowerLeftY())) {

						// setting new peer at the left and updating neighbours
						// and co-ords

						midX = topMidPtEdgeX;
						newPeer.setUpperRightY(topMidPtEdgeY);
						newPeer.setUpperRightX(topMidPtEdgeX);
						newPeer.setLowerLeftY(peer.getLowerLeftY());
						newPeer.setLowerLeftX(peer.getLowerLeftX());
						newPeer.setIpAddress(myIp);
						newPeer.setNeighbour(tempNeighbourPeerTwo);

						Neighbour n2 = new Neighbour(peer.getUpperRightY(),
								peer.getUpperRightX(), bottomMidPtEdgeY,
								bottomMidPtEdgeX, ipAddress);

						newPeer.updateNeighbour(n2);

						// Registry reg3 = LocateRegistry.getRegistry();
						reg3.rebind("peer", newPeer);

						// setting old peer at the right and updating neighbours
						// and co-ords

						Neighbour n1 = new Neighbour(topMidPtEdgeY,
								topMidPtEdgeX, peer.getLowerLeftY(),
								peer.getLowerLeftX(), myIp);

						peer.setUpperRightY(peer.getUpperRightY());
						peer.setUpperRightX(peer.getUpperRightX());
						peer.setLowerLeftY(bottomMidPtEdgeY);
						peer.setLowerLeftX(bottomMidPtEdgeX);

						peer.updateNeighbour(n1);

						// peer.printNeighbours();

						// IClient t = (IClient) reg3.lookup("peer");
						// t.printNeighbours();

					}
				}

				for (String keyword : peer.getFileList().keySet()) {

					int xFile = hashCodeX(keyword);
					int yFile = hashCodeX(keyword);

					if ((xFile < newPeer.getUpperRightX() && xFile > newPeer
							.getLowerLeftX())
							&& (yFile < newPeer.getUpperRightY() && yFile > newPeer
									.getLowerLeftX())) {
						newPeer.updateFileList(keyword,
								peer.getFileContent(keyword));
						peer.removeFile(keyword);

					}
				}

				// updating all neighbours
				for (int i = 0; i < peer.getNeighbour().size(); i++) {
					Neighbour tempNeighbour = peer.getNeighbour().get(i);
					Registry reg4 = LocateRegistry.getRegistry(
							tempNeighbour.ipAddress, 1818);
					// System.out.println("Size of Neighbour list: "
					// + peer.getNeighbour().size());

					IClient newPeerObj = (IClient) reg3.lookup("peer");

					if (tempNeighbour.ipAddress.equals(newPeerObj
							.getIpAddress()) == false) {

						// System.out.println("Now looking at Neighbour "
						// + tempNeighbour.ipAddress);

						// tempNeighbour.printMe();

						if (midY != 0.0) {

							// check if neighbour is on top
							if (tempNeighbour.UpperRightY >= midY
									&& tempNeighbour.LowerLeftY >= midY) {
								// peer.updateNeighbour(tempNeighbour);

							}
							// check if neighbour is on below
							if (tempNeighbour.UpperRightY <= midY
									&& tempNeighbour.LowerLeftY <= midY) {

								newPeerObj.updateNeighbour(tempNeighbour);

								Neighbour n3 = new Neighbour(
										newPeerObj.getUpperRightY(),
										newPeerObj.getUpperRightX(),
										newPeerObj.getLowerLeftY(),
										newPeerObj.getLowerLeftX(),
										newPeerObj.getIpAddress());
								IClient t1 = (IClient) reg4.lookup("peer");
								t1.updateNeighbour(n3);

								if (tempNeighbour.UpperRightY <= midY
										&& tempNeighbour.LowerLeftY < midY) {
									peer.removeNeighbour(tempNeighbour.ipAddress);
									// System.out
									// .println("Now removing at Neighbour "
									// + tempNeighbour.ipAddress);
								}

								if (tempNeighbour.UpperRightY < midY
										&& tempNeighbour.LowerLeftY < midY) {
									peer.removeNeighbour(tempNeighbour.ipAddress);
									// System.out
									// .println("Now removing at Neighbour "
									// + tempNeighbour.ipAddress);
								}

								if (tempNeighbour.UpperRightY < midY
										&& tempNeighbour.LowerLeftY <= midY) {
									peer.removeNeighbour(tempNeighbour.ipAddress);
									// System.out
									// .println("Now removing at Neighbour "
									// + tempNeighbour.ipAddress);
								}

							}

							// check if neighbour is a neighbour of both the
							// peers
							if (tempNeighbour.UpperRightY > midY
									&& tempNeighbour.LowerLeftY < midY) {
								// peer.updateNeighbour(tempNeighbour);
								newPeerObj.updateNeighbour(tempNeighbour);

								Neighbour n3 = new Neighbour(
										newPeerObj.getUpperRightY(),
										newPeerObj.getUpperRightX(),
										newPeerObj.getLowerLeftY(),
										newPeerObj.getLowerLeftX(),
										newPeerObj.getIpAddress());
								IClient t1 = (IClient) reg4.lookup("peer");
								t1.updateNeighbour(n3);

								t1.removeNeighbour(peer.getIpAddress());

								Neighbour n4 = new Neighbour(
										peer.getUpperRightY(),
										peer.getUpperRightX(),
										peer.getLowerLeftY(),
										peer.getLowerLeftX(),
										peer.getIpAddress());
								t1.updateNeighbour(n4);
							}

						}

						if (midX != 0.0) {

							// check if neighbour is on left side
							if (tempNeighbour.LowerLeftX <= midX
									&& tempNeighbour.UpperRightX <= midX) {

								newPeerObj.updateNeighbour(tempNeighbour);

								Neighbour n3 = new Neighbour(
										newPeerObj.getUpperRightY(),
										newPeerObj.getUpperRightX(),
										newPeerObj.getLowerLeftY(),
										newPeerObj.getLowerLeftX(),
										newPeerObj.getIpAddress());
								IClient t1 = (IClient) reg4.lookup("peer");
								t1.updateNeighbour(n3);

								if (tempNeighbour.LowerLeftX < midX
										&& tempNeighbour.UpperRightX < midX) {
									peer.removeNeighbour(tempNeighbour.ipAddress);
									// System.out
									// .println("Now removing at Neighbour "
									// + tempNeighbour.ipAddress);
								}

								if (tempNeighbour.LowerLeftX <= midX
										&& tempNeighbour.UpperRightX < midX) {
									peer.removeNeighbour(tempNeighbour.ipAddress);
									// System.out
									// .println("Now removing at Neighbour "
									// + tempNeighbour.ipAddress);
								}

								if (tempNeighbour.LowerLeftX < midX
										&& tempNeighbour.UpperRightX <= midX) {
									peer.removeNeighbour(tempNeighbour.ipAddress);
									// System.out
									// .println("Now removing at Neighbour "
									// + tempNeighbour.ipAddress);
								}
							}

							// check if neighbour is on right side
							if (tempNeighbour.LowerLeftX >= midX
									&& tempNeighbour.UpperRightX >= midX) {

							}

							// check is neighbur is to both
							if (tempNeighbour.LowerLeftX < midX
									&& tempNeighbour.UpperRightX > midX) {
								newPeerObj.updateNeighbour(tempNeighbour);

								Neighbour n3 = new Neighbour(
										newPeerObj.getUpperRightY(),
										newPeerObj.getUpperRightX(),
										newPeerObj.getLowerLeftY(),
										newPeerObj.getLowerLeftX(),
										newPeerObj.getIpAddress());
								IClient t1 = (IClient) reg4.lookup("peer");
								t1.updateNeighbour(n3);

								t1.removeNeighbour(peer.getIpAddress());

								Neighbour n4 = new Neighbour(
										peer.getUpperRightY(),
										peer.getUpperRightX(),
										peer.getLowerLeftY(),
										peer.getLowerLeftX(),
										peer.getIpAddress());
								t1.updateNeighbour(n4);
							}

						}
					}

				}

				// peer.printNeighbours();
				newPeer.viewPeer(newPeer.getIpAddress());
				// Registry reg5 = LocateRegistry.getRegistry();
				// reg3.rebind("peer", newPeer);

				// IClient t = (IClient) reg3.lookup("peer");
				// t.printNeighbours();

			}
		}
	}

	public String findFileLocation(int x, int y, String ipAddress)
			throws RemoteException, NotBoundException {
		String destinationIP = "";
		boolean found = false;
		Neighbour tempNeighbour = new Neighbour();
		tempNeighbour.ipAddress = ipAddress;

		System.out.println("Routing : " + tempNeighbour.ipAddress);

		Registry newRegistry = LocateRegistry.getRegistry(
				tempNeighbour.ipAddress, 1818);
		IClient peer = (IClient) newRegistry.lookup("peer");
		double minDistance;
		Neighbour neighbourFound = new Neighbour();
		List<Neighbour> neigbours = peer.getNeighbour();
		if (checkIfPointInZone(x, y, peer.getUpperRightY(),
				peer.getUpperRightX(), peer.getLowerLeftY(),
				peer.getLowerLeftX()) == true) {
			destinationIP = peer.getIpAddress();
			return destinationIP;
		}

		else {
			double distance = Math.sqrt((Math.pow((neigbours.get(0)
					.getCentreX() - x), 2) + (Math.pow((neigbours.get(0)
					.getCentreY() - y), 2))));

			for (int i = 0; i < neigbours.size(); i++) {
				Neighbour neighbour = neigbours.get(i);
				if (checkIfPointInZone(x, y, neighbour.UpperRightY,
						neighbour.UpperRightX, neighbour.LowerLeftY,
						neighbour.LowerLeftX) == true) {
					found = true;
					neighbourFound = neighbour;
					System.out.println("File desitination "
							+ neighbourFound.ipAddress);
					destinationIP = neighbourFound.ipAddress;
					return destinationIP;
				} else {
					if ((Math
							.sqrt((Math.pow((neighbour.getCentreX() - x), 2) + (Math
									.pow((neighbour.getCentreY() - y), 2))))) <= distance) {
						tempNeighbour = neighbour;
						minDistance = (Math.sqrt((Math.pow(
								(neighbour.getCentreX() - x), 2) + (Math.pow(
								(neighbour.getCentreY() - y), 2)))));
					}
				}
			}

			findFileLocation(x, y, tempNeighbour.ipAddress);

		}
		System.out.println("File desitination " + neighbourFound.ipAddress);
		return destinationIP;

	}

	/**
	 * This method implements the greedy routing to find the spot for the new
	 * peer
	 * 
	 */
	public void route(double x, double y, String ipAddress)
			throws NotBoundException, AlreadyBoundException, IOException {
		Neighbour tempNeighbour = new Neighbour();
		tempNeighbour.ipAddress = ipAddress;
		// System.out.println("routing neighbour" + tempNeighbour.ipAddress);
		boolean found = false;

		Registry reg2 = LocateRegistry.getRegistry(tempNeighbour.ipAddress,
				1818);
		IClient peer = (IClient) reg2.lookup("peer");
		double distance;
		double minDistance;
		Neighbour neighbourFound;
		ArrayList<Neighbour> neigbours = peer.getNeighbour();
		distance = Math
				.sqrt((Math.pow((neigbours.get(0).getCentreX() - x), 2) + (Math
						.pow((neigbours.get(0).getCentreY() - y), 2))));

		for (int i = 0; i < neigbours.size(); i++) {
			Neighbour neighbour = neigbours.get(i);
			if (checkIfPointInZone(x, y, neighbour.UpperRightY,
					neighbour.UpperRightX, neighbour.LowerLeftY,
					neighbour.LowerLeftX) == true) {
				found = true;
				neighbourFound = neighbour;
				// System.out
				// .println("neighbourFound " + neighbourFound.ipAddress);
				split(x, y, neighbourFound.getIpAddress());
				return;
			} else {
				if ((Math
						.sqrt((Math.pow((neighbour.getCentreX() - x), 2) + (Math
								.pow((neighbour.getCentreY() - y), 2))))) <= distance) {
					tempNeighbour = neighbour;
					minDistance = (Math.sqrt((Math.pow(
							(neighbour.getCentreX() - x), 2) + (Math.pow(
							(neighbour.getCentreY() - y), 2)))));
				}
			}
		}

		route(x, y, tempNeighbour.ipAddress);

		// split(x, y, tempNeighbour.getIpAddress());
		// return;
	}

	@Override
	public ArrayList<Neighbour> getNeighbour() throws RemoteException {
		// TODO Auto-generated method stub
		return this.neighbour;
	}

	/**
	 * This method prints all the neighbours of a given peer
	 * 
	 */
	@Override
	public void printNeighbours() throws RemoteException {

		if ((getNeighbour().size() != 0)) {

			for (int i = 0; i < this.getNeighbour().size(); i++) {
				Neighbour n = this.getNeighbour().get(i);

				System.out.println(" IP: " + n.getIpAddress());
				System.out.println(" UpperRightY: " + n.getUpperRightY());
				System.out.println(" UpperRightX: " + n.getUpperRightX());
				System.out.println(" LowerLeftY: " + n.getLowerLeftY());
				System.out.println(" LowerLeftX: " + n.getLowerLeftX());
				System.out.println("-------------------------------");

			}
		} else {
			System.out.println("This peer has no neighbours");
			System.out.println("-------------------------------");
		}

	}

	@Override
	public void updateNeighbour(Neighbour c) throws RemoteException {
		neighbour.add(c);
	}

	public void removeNeighbour(String ipAddress) throws RemoteException {
		String ip = ipAddress;
		for (int i = 0; i < neighbour.size(); i++) {
			if (neighbour.get(i).ipAddress.equals(ip))
				neighbour.remove(neighbour.get(i));
		}
	}

	public byte[] getFileContent(String keyword) throws RemoteException {

		byte[] content = fileList.get(keyword);
		return content;
	}

	/**
	 * This method prints all the files of a given peer
	 * 
	 */
	public void printFileNames() throws RemoteException {

		if (this.getFileList().size() != 0) {
			for (String keyword : this.getFileList().keySet()) {

				System.out.println(" Name of File: " + keyword);
				System.out.println("-------------------------------");

			}
		} else {
			System.out.println("This peer has no files");
			System.out.println("-------------------------------");
		}
	}

	public void removeFile(String keyword) throws RemoteException {

		fileList.remove(keyword);
	}

	/**
	 * This method prints all the details of a given peer
	 * 
	 */
	public void viewPeer(String ip) throws IOException, NotBoundException {

		Registry reg11 = LocateRegistry.getRegistry(ip, 1818);
		IClient peer = (IClient) reg11.lookup("peer");

		System.out.println("IP Address: " + peer.getIpAddress());
		System.out.println("Peer Upper Right Y-Coordinate: "
				+ peer.getUpperRightY());
		System.out.println("Peer Upper Right X-Coordinate: "
				+ peer.getUpperRightX());
		System.out.println("Peer Lower Left Y-Coordinate: "
				+ peer.getLowerLeftY());
		System.out.println("Peer Lower Left X-Coordinate: "
				+ peer.getLowerLeftX());
		System.out.println("Peer Centre Y-Coordinate: " + peer.getCentreY());
		System.out.println("Peer Centre X-Coordinate: " + peer.getCentreX());
		System.out.println("Peer has the following Neighbours :");
		peer.printNeighbours();
		System.out.println();
		System.out.println("Peer has the following files :");
		peer.printFileNames();
		System.out.println("-------------------------------");

	}
}