import java.io.Serializable;
/**
 * This is the neighbour class that stores information about other neighbours
 * 
 * 
 * @version $Id: 1
 * 
 * 
 * @author Simran Jaising
 * 
 * 
 */

public class Neighbour implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double UpperRightY;
	public double UpperRightX;
	public double LowerLeftX;
	public double LowerLeftY;
	public double centreX;
	public double centreY;

	public Neighbour(double upperRightY, double upperRightX, double lowerLeftY,
			double lowerLeftX, String ipAddress) {
		super();

		UpperRightY = upperRightY;
		UpperRightX = upperRightX;
		LowerLeftX = lowerLeftX;
		LowerLeftY = lowerLeftY;

		this.centreX = centreX;
		this.centreY = centreY;

		this.ipAddress = ipAddress;
	}

	//
	// public double getUpperLeftX() {
	// return UpperLeftX;
	// }

	// public void setUpperLeftX(double upperLeftX) {
	// UpperLeftX = upperLeftX;
	// }

	// public double getUpperLeftY() {
	// return UpperLeftY;
	// }
	//
	// public void setUpperLeftY(double upperLeftY) {
	// UpperLeftY = upperLeftY;
	// }
	
	
	public double getCentreY() {
		return (this.LowerLeftY+this.UpperRightY)/2;
	}

	public double getCentreX() {
		return (this.LowerLeftX+this.UpperRightX)/2;
	}

	public Neighbour() {
		// TODO Auto-generated constructor stub
	}

	public double getUpperRightY() {
		return UpperRightY;
	}

	public void setUpperRightY(double upperRightY) {
		UpperRightY = upperRightY;
	}

	public double getUpperRightX() {
		return UpperRightX;
	}

	public void setUpperRightX(double upperRightX) {
		UpperRightX = upperRightX;
	}

	public double getLowerLeftX() {
		return LowerLeftX;
	}

	public void setLowerLeftX(double lowerLeftX) {
		LowerLeftX = lowerLeftX;
	}

	public double getLowerLeftY() {
		return LowerLeftY;
	}

	public void setLowerLeftY(double lowerLeftY) {
		LowerLeftY = lowerLeftY;
	}

	// public double getLowerRightX() {
	// return LowerRightX;
	// }
	//
	// public void setLowerRightX(double lowerRightX) {
	// LowerRightX = lowerRightX;
	// }
	//
	// public double getLowerRightY() {
	// return LowerRightY;
	// }
	//
	// public void setLowerRightY(double lowerRightY) {
	// LowerRightY = lowerRightY;
	// }

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String ipAddress;
	
	public void printMe(){
		System.out.println(" IP: " + this.getIpAddress());
		System.out.println(" UpperRightY: " + this.getUpperRightY());
		System.out.println(" UpperRightX: " + this.getUpperRightX());
		System.out.println(" LowerLeftY: " + this.getLowerLeftY());
		System.out.println(" LowerLeftX: " + this.getLowerLeftX());
		System.out.println("-------------------------------");
	}

}
