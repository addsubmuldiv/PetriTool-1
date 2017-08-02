package PetriTool;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextField;
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
import javafx.scene.control.ComboBox;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;
import static java.util.stream.Collectors.*;




public class SerialPortManagement extends JFrame {

	private JPanel contentPane=null;
	private PetriTool petriTool_=null;
	private ArrayList<String> commList=null;
	private SerialPort serialPort;
	JTextArea textArea_Receive;
	JTextArea textArea_Send;
	
	public final int ROW_SPACE=27;
	
	ArrayList<Module> moduleListC2P;
	
	
	ButtonGroup radioGroupC2P=new ButtonGroup();
	
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
	/**
	 * Module set class
	 * **/
	int moduleYCoordinateC2P=9;
	class ModuleC2P extends Module
	{			
		public ModuleC2P()
		{
			yCoordinate=moduleYCoordinateC2P;
			jButton=new JRadioButton("");
			jButton.setBounds(18, moduleYCoordinateC2P, 21, 21);
			
			moduleName=new JTextField();
			moduleName.setBounds(51, moduleYCoordinateC2P, 88, 21);
			
			placeDistinction=new JTextField();
			placeDistinction.setBounds(149, moduleYCoordinateC2P, 144, 21);
			
			modulePanelC2P.add(jButton);
			modulePanelC2P.add(moduleName);
			modulePanelC2P.add(placeDistinction);
			
			radioGroupC2P.add(jButton);

			moduleYCoordinateC2P+=ROW_SPACE;
			if(moduleYCoordinateC2P>scrollPaneC2P.getHeight())
			{
				modulePanelC2P.setPreferredSize(new Dimension(scrollPaneC2P.getWidth(), moduleYCoordinateC2P));
				modulePanelC2P.revalidate();
			}
			repaint();
		}
	}
	
	
	
	abstract class Module 
	{
		protected JRadioButton jButton;
		
		protected JTextField moduleName;
		
		protected JTextField placeDistinction;

		protected int yCoordinate;
		
		
		
		public int getyCoordinate() {
			return yCoordinate;
		}



		public void setLocation(int y)
		{
			this.jButton.setLocation(jButton.getX(),y);
			this.moduleName.setLocation(moduleName.getX(),y);
			this.placeDistinction.setLocation(placeDistinction.getX(),y);
			yCoordinate=y;
		}
	}
	

	private JComboBox comboBox_Baud_Rate;
	private JComboBox comboBox_Stop_Bit; 
	private JComboBox comboBox_Data_Bit;
	private JComboBox comboBox_Odd_Even_Check;
	JButton btn_SendAuto;
	JButton btn_Connect;
	
	
	JScrollPane scrollPaneC2P;
	JPanel modulePanelC2P;
	boolean isConnected=false;
	
	private AutoSendListener autoSendListener=null;
	
	private void initComponent() {
		setTitle("Connect to device");
		setBounds(100, 100, 900, 600);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_Control = new JPanel();
		panel_Control.setBounds(5, 10, 234, 541);
		panel_Control.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Control", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
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
		
		comboBox_Baud_Rate = new JComboBox();
		comboBox_Baud_Rate.setModel(new DefaultComboBoxModel(new String[] {"300", "600", "1200", "2400", "4800", "9600", "19200", "38400", "43000", "56000", "57600", "115200"}));
		comboBox_Baud_Rate.setBounds(120, 79, 78, 21);
		panel_Control.add(comboBox_Baud_Rate);
		
		JLabel lblStopBit = new JLabel("Stop bit:");
		lblStopBit.setBounds(46, 130, 54, 15);
		panel_Control.add(lblStopBit);
		
		comboBox_Stop_Bit = new JComboBox();
		comboBox_Stop_Bit.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
		comboBox_Stop_Bit.setBounds(120, 127, 78, 21);
		panel_Control.add(comboBox_Stop_Bit);
		
		JLabel lblDataBits = new JLabel("Data bit:");
		lblDataBits.setBounds(46, 175, 70, 15);
		panel_Control.add(lblDataBits);
		
		comboBox_Data_Bit = new JComboBox();
		comboBox_Data_Bit.setModel(new DefaultComboBoxModel(new String[] {"8", "7", "6", "5"}));
		comboBox_Data_Bit.setBounds(120, 172, 78, 21);
		panel_Control.add(comboBox_Data_Bit);
		
		JLabel lblOddevenCheck = new JLabel("Odd-even check:");
		lblOddevenCheck.setBounds(10, 221, 100, 15);
		panel_Control.add(lblOddevenCheck);
		
		comboBox_Odd_Even_Check = new JComboBox();
		comboBox_Odd_Even_Check.setModel(new DefaultComboBoxModel(new String[] {"NONE", "ODD", "EVEN"}));
		comboBox_Odd_Even_Check.setBounds(120, 218, 78, 21);
		panel_Control.add(comboBox_Odd_Even_Check);
		
		// Add a button to connect with device
		btn_Connect= new JButton("Connect");
		btn_Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String commNameStr=(String)comboBox_Serial_Port.getSelectedItem();
				String baudRateStr=(String)comboBox_Baud_Rate.getSelectedItem();
//				int dataBits=
				//Check the serial port name
				switch(btn_Connect.getText())
				{
					case "Connect":
						isConnected=true;
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
									serialPort = SerialTool.openPort(commNameStr, bps,getDataBits(),getStopBits(),getOddEvenCheck());
									//Add listener to the serial port
									SerialTool.addListener(serialPort, new SerialListener());
									//if the listener is added correctly
									JOptionPane.showMessageDialog(null, "Listen succeed", "tip", JOptionPane.INFORMATION_MESSAGE);
									btn_Connect.setText("Disconnect");
								} catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort | PortInUse | TooManyListeners e1) {
									JOptionPane.showMessageDialog(null, e1, "Error", JOptionPane.INFORMATION_MESSAGE);
								}
							}
						}
						break;
					case "Disconnect":
						isConnected=false;
						SerialTool.closePort(serialPort);
						isAutoSendPressed=false;
						btn_Connect.setText("Connect");
						btn_SendAuto.setEnabled(true);
						JOptionPane.showMessageDialog(null, "The serial port has been closed");
						break;
					default:
						break;
				}
			}
		});
		btn_Connect.setBounds(59, 281, 111, 36);
		panel_Control.add(btn_Connect);
		
		
		JPanel panel_Send = new JPanel();
		panel_Send.setBounds(610, 10, 274, 353);
		panel_Send.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data sent", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panel_Send);
		panel_Send.setLayout(null);
		
		textArea_Send=new JTextArea();
		textArea_Send.setLineWrap(true);
		JScrollPane jScrollPane_Send=new JScrollPane(textArea_Send);
		jScrollPane_Send.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane_Send.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane_Send.setBounds(10, 22, 254, 258);
		panel_Send.add(jScrollPane_Send);
		
		JButton btn_Send = new JButton("Manual Test");
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
		btn_Send.setBounds(20, 290, 100, 36);
		panel_Send.add(btn_Send);
		
		// Add a button to get and send the data from the model automatically
		
        autoSendListener=new AutoSendListener();
		btn_SendAuto = new JButton("Auto Send");
		btn_SendAuto.addActionListener(autoSendListener);
		//Todo 
		btn_SendAuto.setBounds(151, 290, 100, 36);
		panel_Send.add(btn_SendAuto);
		
		
	
		
				
		//Receive data from device
		
		JPanel panel_Receive = new JPanel();
		panel_Receive.setBounds(610, 373, 274, 178);
		panel_Receive.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data reception", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		contentPane.add(panel_Receive);
		panel_Receive.setLayout(null);
		
		textArea_Receive = new JTextArea();
		textArea_Receive.setLineWrap(true);
		textArea_Receive.setEnabled(false);
		JScrollPane jScrollPane_Receive=new JScrollPane(textArea_Receive);
		jScrollPane_Receive.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane_Receive.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane_Receive.setBounds(10, 29, 254, 137);
		panel_Receive.add(jScrollPane_Receive);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Computer to PLC Singals", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(249, 10, 351, 281);
		contentPane.add(panel);
		panel.setLayout(null);
		
		scrollPaneC2P = new JScrollPane();
		scrollPaneC2P.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneC2P.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneC2P.setBounds(10, 22, 331, 205);
		panel.add(scrollPaneC2P);
		
		modulePanelC2P = new JPanel();
		scrollPaneC2P.setViewportView(modulePanelC2P);
		modulePanelC2P.setLayout(null);
		
		
		moduleListC2P=new ArrayList<>();
		moduleListC2P.add(new ModuleC2P());
		moduleListC2P.add(new ModuleC2P());
		moduleListC2P.add(new ModuleC2P());
		
		
		
		
		
		
		
		
		
		JButton btnUpC2P = new JButton("Up");
		btnUpC2P.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<moduleListC2P.size();i++)
				{
					if(moduleListC2P.get(i).jButton.isSelected())
					{	
						if(i==0)
						{
							return;
						}
						Module tempModule=moduleListC2P.get(i);
						tempModule.setLocation(tempModule.getyCoordinate()-ROW_SPACE);
						Module lastModule=moduleListC2P.get(i-1);
						lastModule.setLocation(lastModule.getyCoordinate()+ROW_SPACE);
						
						moduleListC2P.set(i, lastModule);
						moduleListC2P.set(i-1, tempModule);
						break;
					}
				}
				repaint();
			}
		});
		btnUpC2P.setBounds(10, 237, 57, 23);
		panel.add(btnUpC2P);
		
		JButton btnDownC2P = new JButton("Down");
		btnDownC2P.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<moduleListC2P.size();i++)
				{
					if(moduleListC2P.get(i).jButton.isSelected())
					{	
						if(i==moduleListC2P.size()-1)
						{
							return;
						}
						Module tempModule=moduleListC2P.get(i);
						tempModule.setLocation(tempModule.getyCoordinate()+ROW_SPACE);
						Module nextModule=moduleListC2P.get(i+1);
						nextModule.setLocation(nextModule.getyCoordinate()-ROW_SPACE);
						
						moduleListC2P.set(i, nextModule);
						moduleListC2P.set(i+1, tempModule);
						break;
					}
				}
				repaint();
			}
		});
		btnDownC2P.setBounds(77, 237, 62, 23);
		panel.add(btnDownC2P);
		
		JButton btnAddC2P = new JButton("Add");
		btnAddC2P.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moduleListC2P.add(new ModuleC2P());
			}
		});
		btnAddC2P.setBounds(149, 237, 51, 23);
		panel.add(btnAddC2P);
		
		JButton btnDeleteC2P = new JButton("Delete");
		btnDeleteC2P.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<moduleListC2P.size();i++)
				{
					if(moduleListC2P.get(i).jButton.isSelected())
					{
						modulePanelC2P.remove(moduleListC2P.get(i).jButton);
						modulePanelC2P.remove(moduleListC2P.get(i).moduleName);
						modulePanelC2P.remove(moduleListC2P.get(i).placeDistinction);
						repaint();
						moduleListC2P.remove(i);
						moduleListC2P.stream().skip(i).forEach(m->
						{
//							m.jButton.setLocation(m.jButton.getX(), m.jButton.getY()-ROW_SPACE);
//							m.moduleName.setLocation(m.moduleName.getX(), m.moduleName.getY()-ROW_SPACE);
//							m.placeDistinction.setLocation(m.placeDistinction.getX(), m.placeDistinction.getY()-ROW_SPACE);
							m.setLocation(m.getyCoordinate()-ROW_SPACE);
						});
					}
				}
				moduleYCoordinateC2P-=ROW_SPACE;
				repaint();
			}
		});
		btnDeleteC2P.setBounds(210, 237, 70, 23);
		panel.add(btnDeleteC2P);
		
		JButton btnOkC2P = new JButton("OK");
		btnOkC2P.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnOkC2P.setBounds(290, 237, 51, 23);
		panel.add(btnOkC2P);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "PLC to Computer Singals", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(249, 301, 351, 250);
		contentPane.add(panel_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(10, 22, 331, 178);
		panel_1.add(scrollPane_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		scrollPane_1.setViewportView(panel_2);
		
		JButton button = new JButton("Up");
		button.setBounds(10, 210, 57, 23);
		panel_1.add(button);
		
		JButton button_2 = new JButton("Down");
		button_2.setBounds(77, 210, 62, 23);
		panel_1.add(button_2);
		
		JButton button_3 = new JButton("Add");
		button_3.setBounds(149, 210, 51, 23);
		panel_1.add(button_3);
		
		JButton button_4 = new JButton("Delete");
		button_4.setBounds(210, 210, 70, 23);
		panel_1.add(button_4);
		
		JButton button_5 = new JButton("OK");
		button_5.setBounds(290, 210, 51, 23);
		panel_1.add(button_5);
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
	
	
	private int getDataBits()
	{
		int dataBits=Integer.parseInt((String)comboBox_Data_Bit.getSelectedItem());
		switch (dataBits) {
		case 5: return SerialPort.DATABITS_5;
		case 6: return SerialPort.DATABITS_6;
		case 7: return SerialPort.DATABITS_7;
		case 8: return SerialPort.DATABITS_8;
		default:break;
		}
		return 0;
	}
	
	private int getStopBits()
	{
		int stopBits=Integer.parseInt((String)comboBox_Stop_Bit.getSelectedItem());
		switch (stopBits) {
		case 1: return SerialPort.STOPBITS_1;
		case 2: return SerialPort.STOPBITS_2;
		default:break;
		}
		return 0;
	}
	
	private int getOddEvenCheck()
	{
		String oddEvenCheck=(String)comboBox_Odd_Even_Check.getSelectedItem();
		switch (oddEvenCheck) {
		case "NONE": return serialPort.PARITY_NONE;
		case "ODD": return serialPort.PARITY_ODD;
		case "EVEN": return serialPort.PARITY_EVEN;
		default:
			break;
		}
		return 0;
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
							
							if(isAutoSendPressed)
							{
								autoDataReceiveHandle(dataOriginal);
							}
							
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
		
		
		public void autoDataReceiveHandle(String dataOriginal)
		{
			Vector<Place> pVector=petriTool_.designPanel_.placeVector_;
			Vector<Token> tVector=petriTool_.designPanel_.tokenVector_;
			String[] dataArray=dataOriginal.split(" ");
			for(int i=0;i<dataArray.length;i++)
			{	
				int tokenNum=Integer.parseInt(dataArray[i]);
				if(tokenNum!=0)
				{
					Place place_=pVector.elementAt(i);
					if(place_.token_==null)
					{
						Token newToken=new Token(place_.getXCoordinate(),
								place_.getYCoordinate(), tokenNum);
						place_.setToken(newToken);
						tVector.addElement(newToken);
					}
					else
					{
						place_.token_.setTokensRepresented(tokenNum);
					}
					petriTool_.designPanel_.repaint();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					petriTool_.controlPanel_.userWantsRun();
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
	        isAutoSendPressed=false;
	        SerialTool.closePort(serialPort);
	        ((Window) e.getComponent()).dispose();
	    }
	}
	
	
	private static boolean isAutoSendPressed=false;
	
	public static void setAutoSendPressed(boolean isAutoSendPressed) {
		SerialPortManagement.isAutoSendPressed = isAutoSendPressed;
	}

	public static boolean isAutoSendPressed() {
		return isAutoSendPressed;
	}

	
	/**
	 * Inner class that listen the AutoSend button has been pressed
	 * **/
	class AutoSendListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(isConnected)
			{
				petriTool_.controlPanel_.userWantsRun();
				isAutoSendPressed=true;
				btn_SendAuto.setEnabled(false);
			}
		}
		
	}
	
	
	/**The string that shows what you have sent**/
	String dataToSend="";
	
	/**Get string and send it to the serial port**/
	public void getDataAndSend(Object dataObject)
	{
		placeMatching(dataObject);
		dataToSend+=(String)dataObject+'\n';
		textArea_Send.setText(dataToSend);
		/**Encoding the data as byte[]**/
		byte[] dataSend=((String)dataObject).getBytes();
		try {
			SerialTool.sendToPort(serialPort, dataSend);
		} catch (SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void placeMatching(Object dataObject)
	{
		String data=(String)dataObject;
		String[] dataArray=data.split(" ");
		Vector<Place> placeVector=petriTool_.designPanel_.placeVector_;
		for(int i=0;i<dataArray.length;i++)
		{
			if(dataArray[i]!="0")
			{
				
			}
		}
		Map<String, java.util.List<Place>> placeGroup = 
				placeVector.stream().collect(groupingBy(place->{
					for(int i=0;i<moduleListC2P.size();i++)
					{
						if(place.getplaceName_().contains(
								(moduleListC2P.get(i).placeDistinction.getText())))
							return moduleListC2P.get(i).placeDistinction.getText();
						
					}
					return "";
				}));
		
		if(placeGroup.size()<moduleListC2P.size())
		{
			JOptionPane.showMessageDialog(null, "Please assure your module "
					+ "is matching with your petri net");
			return;
		}
		StringBuilder dataToSendBuilder=new StringBuilder();
		for(int i=0;i<moduleListC2P.size();i++)
		{
			java.util.List<Place> pList=placeGroup.get(moduleListC2P.get(i).placeDistinction.getText());
			int res=pList.stream().map(p->p.getNumTokens()).reduce(0, (a,b)->a|b);
			dataToSendBuilder.append(res);
			dataToSendBuilder.append(" ");
		}
		String matchingStr=dataToSendBuilder.toString().trim();
		
		System.out.println(matchingStr);
		
//	for(String key:placeGroup.keySet())
//		{
//			System.out.print(key+"= [");
//			for(Place place:placeGroup.get(key))
//			{
//				System.out.print(place.getplaceName_()+" ");
//			}
//			System.out.println("]");
//		}
	}
}



