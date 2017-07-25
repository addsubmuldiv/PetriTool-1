package PetriTool.serialException;

public class SerialPortInputStreamCloseFailure extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SerialPortInputStreamCloseFailure() {}

	@Override
	public String toString() {
		return "Error closing the serial object input stream (InputStream)!";
	}
	
	
}
