package PetriTool.serialException;

public class NotASerialPort extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotASerialPort() {}

	@Override
	public String toString() {
		return "Port pointing device is not serial type!Failed to open serial port operation!";
	}
	
	
}
