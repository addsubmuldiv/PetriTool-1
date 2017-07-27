package PetriTool.serialException;

public class ReadDataFromSerialPortFailure extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReadDataFromSerialPortFailure() {}

	@Override
	public String toString() {
		return "Error while reading data from serial port!";
	}
	
}
