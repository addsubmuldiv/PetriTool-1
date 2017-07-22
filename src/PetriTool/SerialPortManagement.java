package PetriTool;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import PetriTool.serialException.*;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class SerialPortManagement extends JFrame {

	private JPanel contentPane=null;
	private PetriTool petriTool_=null;
	private ArrayList<String> commList=null;
	private SerialPort serialPort;
	JTextArea textArea_Receive;
	/**
	 * Create the frame.
	 */
	public SerialPortManagement(PetriTool petriTool) {
		this.commList=SerialTool.findPort();
		initComponent();
		setUI();
        this.petriTool_=petriTool;
        this.addWindowListener(new Win());
	}





	private void initComponent() {
		setTitle("Connect to device");
		setBounds(100, 100, 600, 600);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_Control = new JPanel();
		panel_Control.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Control", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Control.setBounds(10, 10, 234, 541);
		contentPane.add(panel_Control);
		panel_Control.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Select port:");
		lblNewLabel.setBounds(28, 34, 118, 15);
		panel_Control.add(lblNewLabel);
		
		final JComboBox comboBox_Serial_Port = new JComboBox();
		if (commList == null || commList.size()<1) {
			JOptionPane.showMessageDialog(null, "There is no available serial port!", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			for (String s : commList) {
				comboBox_Serial_Port.addItem(s);
			}
		}
		comboBox_Serial_Port.setBounds(120, 31, 78, 21);
		panel_Control.add(comboBox_Serial_Port);
		
		JLabel lblBaudRate = new JLabel("Baud rate:");
		lblBaudRate.setBounds(40, 82, 70, 15);
		panel_Control.add(lblBaudRate);
		
		final JComboBox comboBox_Baud_Rate = new JComboBox();
		comboBox_Baud_Rate.setModel(new DefaultComboBoxModel(new String[] {"300", "600", "1200", "2400", "4800", "9600", "19200", "38400", "43000", "56000", "57600", "115200"}));
		comboBox_Baud_Rate.setBounds(120, 79, 78, 21);
		panel_Control.add(comboBox_Baud_Rate);
		
		JLabel lblStopBit = new JLabel("Stop bit:");
		lblStopBit.setBounds(46, 130, 54, 15);
		panel_Control.add(lblStopBit);
		
		JComboBox comboBox_Stop_Bit = new JComboBox();
		comboBox_Stop_Bit.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
		comboBox_Stop_Bit.setBounds(120, 127, 78, 21);
		panel_Control.add(comboBox_Stop_Bit);
		
		JLabel lblDataBits = new JLabel("Data bit:");
		lblDataBits.setBounds(46, 175, 70, 15);
		panel_Control.add(lblDataBits);
		
		JComboBox comboBox_Data_Bit = new JComboBox();
		comboBox_Data_Bit.setModel(new DefaultComboBoxModel(new String[] {"8", "7", "6", "5"}));
		comboBox_Data_Bit.setBounds(120, 172, 78, 21);
		panel_Control.add(comboBox_Data_Bit);
		
		JLabel lblOddevenCheck = new JLabel("Odd-even check:");
		lblOddevenCheck.setBounds(10, 221, 100, 15);
		panel_Control.add(lblOddevenCheck);
		
		JComboBox comboBox_Odd_Even_Check = new JComboBox();
		comboBox_Odd_Even_Check.setModel(new DefaultComboBoxModel(new String[] {"NONE", "ODD", "EVEN"}));
		comboBox_Odd_Even_Check.setBounds(120, 218, 78, 21);
		panel_Control.add(comboBox_Odd_Even_Check);
		
		JButton btn_Connect = new JButton("Connect");
		btn_Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String commNameStr=(String)comboBox_Serial_Port.getSelectedItem();
				String baudRateStr=(String)comboBox_Baud_Rate.getSelectedItem();
				//Check the serial port name
				if (commNameStr == null || commNameStr.equals("")) {
					JOptionPane.showMessageDialog(null, "There is no serial port", "Error", JOptionPane.INFORMATION_MESSAGE);			
				}
				else {
					//Check the baud rate 
					if (baudRateStr == null || baudRateStr.equals("")) {
						JOptionPane.showMessageDialog(null, "Getting baud rate occurs a problem", "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						//if both serial port name and baud rate are correct
						int bps = Integer.parseInt(baudRateStr);
						try {
							
							//Get the port that specified
							serialPort = SerialTool.openPort(commNameStr, bps);
							//Add listener to the serial port
							SerialTool.addListener(serialPort, new SerialListener());
							//if the listener is added correctly
							JOptionPane.showMessageDialog(null, "Listen succeed", "tip", JOptionPane.INFORMATION_MESSAGE);
							
						} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
							JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}	
				
			}
		});
		btn_Connect.setBounds(59, 281, 111, 36);
		panel_Control.add(btn_Connect);
		
		JPanel panel_Send = new JPanel();
		panel_Send.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data sent", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Send.setBounds(254, 10, 327, 195);
		contentPane.add(panel_Send);
		panel_Send.setLayout(null);
		
		final JTextArea textArea_Send=new JTextArea();
		textArea_Send.setLineWrap(true);
		JScrollPane jScrollPane_Send=new JScrollPane(textArea_Send);
		jScrollPane_Send.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane_Send.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane_Send.setBounds(10, 23, 307, 119);
		panel_Send.add(jScrollPane_Send);
		JButton btn_Send = new JButton("Send");
		btn_Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				/**Get the data you want to send to the port from textArea**/
				String dataSendStr=textArea_Send.getText();
				
				/**Encoding the data as byte[]**/
				byte[] dataSend=dataSendStr.getBytes();
				try {
					SerialTool.sendToPort(serialPort, dataSend);
				} catch (SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
		btn_Send.setBounds(108, 152, 111, 36);
		panel_Send.add(btn_Send);
		
		JPanel panel_Receive = new JPanel();
		panel_Receive.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data reception", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Receive.setBounds(254, 215, 327, 195);
		contentPane.add(panel_Receive);
		panel_Receive.setLayout(null);
		
		textArea_Receive = new JTextArea();
		textArea_Receive.setLineWrap(true);
		textArea_Receive.setEnabled(false);
		JScrollPane jScrollPane_Receive=new JScrollPane(textArea_Receive);
		jScrollPane_Receive.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane_Receive.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane_Receive.setBounds(10, 29, 310, 137);
		panel_Receive.add(jScrollPane_Receive);
		
		JPanel panel_Identify = new JPanel();
		panel_Identify.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Marking", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Identify.setBounds(254, 420, 327, 131);
		contentPane.add(panel_Identify);
		panel_Identify.setLayout(null);
		
		JTextArea textArea_Identify = new JTextArea();
		textArea_Identify.setLineWrap(true);
		JScrollPane jScrollPane_Identify=new JScrollPane(textArea_Identify);
		jScrollPane_Identify.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane_Identify.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane_Identify.setBounds(10, 28, 310, 93);
		panel_Identify.add(jScrollPane_Identify);
	}
	
	
	
	
	
	public void setUI()
	{
		/**Set the window style**/
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();  
            try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  	
	}
	
	private String dataValid = "";	//the data will be showed in the textArea_Receive
	
	
	
	class SerialListener implements SerialPortEventListener
	{

		@Override
		public void serialEvent(SerialPortEvent serialPortEvent) {
			// TODO Auto-generated method stub
			switch (serialPortEvent.getEventType()) {

            case SerialPortEvent.BI: // 10 Communication interruption
            	JOptionPane.showMessageDialog(null, "Communication interruption", "Error", JOptionPane.INFORMATION_MESSAGE);
            	break;

            case SerialPortEvent.OE: // 7 Overflow error

            case SerialPortEvent.FE: // 9 Frame Error

            case SerialPortEvent.PE: // 8 Parity check error

            case SerialPortEvent.CD: // 6 Carrier detect

            case SerialPortEvent.CTS: // 3 Remove pending delivery data

            case SerialPortEvent.DSR: // 4 Ready to send the data

            case SerialPortEvent.RI: // 5 Ringing indicating

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 The output buffer is empty
            	break;
            
            case SerialPortEvent.DATA_AVAILABLE: // 1 The serial port has available data
            	
            	//System.out.println("found data");
				byte[] data = null;
				
				try {
					if (serialPort == null) {
						JOptionPane.showMessageDialog(null, "Serial port object is empty! Monitor failed!", "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						data = SerialTool.readFromPort(serialPort);	//Read data into byte array
						//System.out.println(new String(data));
						
						//Customize the parsing process
						if (data == null || data.length < 1) {	//Check whether the data is read correctly
							JOptionPane.showMessageDialog(null, "No valid data is obtained from reading data! Please check the equipment or procedure!", "Error", JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						}else {
							String dataOriginal = new String(data);	//Converts the byte array data to a string that holds the raw data
							dataValid+=dataOriginal+"\n";
							textArea_Receive.setText(dataValid);
							repaint();
							}
						}	
				}catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
	
	
	class Win extends WindowAdapter {
		/**
		 * The key to fix the can't close bug
		 * **/
	    public void windowClosing(WindowEvent e) 
	    {
	        e.getWindow().setVisible(false);
	        SerialTool.closePort(serialPort);
	        ((Window) e.getComponent()).dispose();
	    }
	}
}




