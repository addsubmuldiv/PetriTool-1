package PetriTool;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.border.TitledBorder;
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

public class SerialPortManagement extends JFrame {

	private JPanel contentPane;
	private PetriTool petriTool_;
	

	/**
	 * Create the frame.
	 */
	public SerialPortManagement(PetriTool petriTool) {
		setTitle("Connect to device");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9"}));
		comboBox.setBounds(120, 31, 78, 21);
		panel_Control.add(comboBox);
		
		JLabel lblBaudRate = new JLabel("Baud rate:");
		lblBaudRate.setBounds(40, 82, 70, 15);
		panel_Control.add(lblBaudRate);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"300", "600", "1200", "2400", "4800", "9600", "19200", "38400", "43000", "56000", "57600", "115200"}));
		comboBox_1.setBounds(120, 79, 78, 21);
		panel_Control.add(comboBox_1);
		
		JLabel lblStopBit = new JLabel("Stop bit:");
		lblStopBit.setBounds(46, 130, 54, 15);
		panel_Control.add(lblStopBit);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
		comboBox_2.setBounds(120, 127, 78, 21);
		panel_Control.add(comboBox_2);
		
		JLabel lblDataBits = new JLabel("Data bit:");
		lblDataBits.setBounds(46, 175, 70, 15);
		panel_Control.add(lblDataBits);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"8", "7", "6", "5"}));
		comboBox_3.setBounds(120, 172, 78, 21);
		panel_Control.add(comboBox_3);
		
		JLabel lblOddevenCheck = new JLabel("Odd-even check:");
		lblOddevenCheck.setBounds(10, 221, 100, 15);
		panel_Control.add(lblOddevenCheck);
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setModel(new DefaultComboBoxModel(new String[] {"NONE", "ODD", "EVEN"}));
		comboBox_4.setBounds(120, 218, 78, 21);
		panel_Control.add(comboBox_4);
		
		JButton btn_Connect = new JButton("Connect");
		btn_Connect.setBounds(59, 281, 111, 36);
		panel_Control.add(btn_Connect);
		
		JPanel panel_Send = new JPanel();
		panel_Send.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data sent", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Send.setBounds(254, 10, 327, 195);
		contentPane.add(panel_Send);
		panel_Send.setLayout(null);
		
		JTextArea textArea_Send=new JTextArea();
		textArea_Send.setLineWrap(true);
		JScrollPane jScrollPane_Send=new JScrollPane(textArea_Send);
		jScrollPane_Send.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane_Send.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane_Send.setBounds(10, 23, 307, 119);
		panel_Send.add(jScrollPane_Send);
		JButton btn_Send = new JButton("Send");
		btn_Send.setBounds(108, 152, 111, 36);
		panel_Send.add(btn_Send);
		
		JPanel panel_Receive = new JPanel();
		panel_Receive.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Data reception", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_Receive.setBounds(254, 215, 327, 195);
		contentPane.add(panel_Receive);
		panel_Receive.setLayout(null);
		
		JTextArea textArea_Receive = new JTextArea();
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
		
		/**Set the window style**/
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();  
            try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  	
        this.petriTool_=petriTool;
	}
}
