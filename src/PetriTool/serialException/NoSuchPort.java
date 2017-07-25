package PetriTool.serialException;

public class NoSuchPort extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchPort() {}

	@Override
	public String toString() {
		return "No serial port device matching the port name is found!Failed to open serial port operation!";
	}
	
}
