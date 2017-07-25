package PetriTool.serialException;

public class PortInUse extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PortInUse() {}

	@Override
	public String toString() {
		return "Port is occupied!Failed to open serial port operation!";
	}
	
}
