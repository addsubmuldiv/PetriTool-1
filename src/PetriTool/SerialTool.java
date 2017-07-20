package PetriTool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import PetriTool.serialException.*;

/**
 * Serial port service class, provide open, close serial port, 
 * read, send serial port data and other services (use single case design mode)
 * 
 *
 */
public class SerialTool {
    
    private static SerialTool serialTool = null;
    
    static {
        //A SerialTool object is initialized when the class is loaded by a ClassLoader
        if (serialTool == null) {
            serialTool = new SerialTool();
        }
    }
    
    //Set the constructor of SerialTool private to use singleton pattern
    private SerialTool() {}    
    
    /**
     * Gets the SerialTool object that provides the service
     * @return serialTool
     */
    public static SerialTool getSerialTool() {
        if (serialTool == null) {
            serialTool = new SerialTool();
        }
        return serialTool;
    }


    /**
     * Search the available ports
     * @return A list of available ports 
     */
    public static final ArrayList<String> findPort() {

        //Obtain all available ports now
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();    
        
        ArrayList<String> portNameList = new ArrayList<>();

        //Add the available port name to the list and return the list
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }

        return portNameList;

    }
    
    /**
     * open port 
     * @param portName 
     * @param baudrate 
     * @return 
     * @throws SerialPortParameterFailure 
     * @throws NotASerialPort 
     * @throws NoSuchPort 
     * @throws PortInUse 
     */
    public static final SerialPort openPort(String portName, int baudrate) throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse {

        try {

            //Identify port by port name
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            //Open port and give it a name and a timeout(the timeout of the open port operation)
            CommPort commPort = portIdentifier.open(portName, 2000);

            if (commPort instanceof SerialPort) {
                
                SerialPort serialPort = (SerialPort) commPort;
                
                try {                        
                    //set the baud rate and other parameters
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);                              
                } catch (UnsupportedCommOperationException e) {  
                    throw new SerialPortParameterFailure();
                }
                
                //System.out.println("Open " + portName + " sucessfully !");
                return serialPort;
            
            }        
            else {
                throw new NotASerialPort();
            }
        } catch (NoSuchPortException e1) {
          throw new NoSuchPort();
        } catch (PortInUseException e2) {
            throw new PortInUse();
        }
    }
    
    /**
     * close serial port
     * @param serialport waiting to be closed
     */
    public static void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }
    
    /**
     * Send data to the port
     * @param serialPort    The destination of the data
     * @param order    The data waiting to be send
     * @throws SendDataToSerialPortFailure 	 data sent failed
     * @throws SerialPortOutputStreamCloseFailure    Closing port occurs an exception
     */
    public static void sendToPort(SerialPort serialPort, byte[] order) throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {

        OutputStream out = null;
        
        try {
            
            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
            
        } catch (IOException e) {
            throw new SendDataToSerialPortFailure();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }                
            } catch (IOException e) {
                throw new SerialPortOutputStreamCloseFailure();
            }
        }
        
    }
    
    /**
     * Read data from port
     * @param serialPort   The serial port that has connected
     * @return data
     * @throws ReadDataFromSerialPortFailure 
     * @throws SerialPortInputStreamCloseFailure 
     */
    public static byte[] readFromPort(SerialPort serialPort) throws ReadDataFromSerialPortFailure, SerialPortInputStreamCloseFailure {

        InputStream in = null;
        byte[] bytes = null;

        try {
            
            in = serialPort.getInputStream();
            int bufflenth = in.available();        //obtain the length of the data in the input stream
            
            while (bufflenth != 0) {                             
                bytes = new byte[bufflenth];
                in.read(bytes);
                bufflenth = in.available();
            } 
        } catch (IOException e) {
            throw new ReadDataFromSerialPortFailure();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch(IOException e) {
                throw new SerialPortInputStreamCloseFailure();
            }
        }
        return bytes;
    }
    
    /**
     * add listener
     * @param port     
     * @param listener 
     * @throws TooManyListeners 
     */
    public static void addListener(SerialPort port, SerialPortEventListener listener) throws TooManyListeners {

        try {
            
            port.addEventListener(listener);
            //  Set when there is data coming rouse the reception thread 
            port.notifyOnDataAvailable(true);
          //Set up interrupt threads when communication interrupts	
            port.notifyOnBreakInterrupt(true);

        } catch (TooManyListenersException e) {
            throw new TooManyListeners();
        }
    }
    
    
}