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
		setBounds(100, 100, 550, 600);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_Control = new JPanel();
		panel_Control.setBorder(new TitledBorder(null, "\u63A7\u5236", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_Control.setBounds(10, 10, 177, 541);
		contentPane.add(panel_Control);
		panel_Control.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("串口选择：");
		lblNewLabel.setBounds(10, 31, 65, 15);
		panel_Control.add(lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9"}));
		comboBox.setBounds(74, 28, 78, 21);
		panel_Control.add(comboBox);
		
		JLabel label = new JLabel("波特率：");
		label.setBounds(21, 79, 54, 15);
		panel_Control.add(label);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"300", "600", "1200", "2400", "4800", "9600", "19200", "38400", "43000", "56000", "57600", "115200"}));
		comboBox_1.setBounds(74, 76, 78, 21);
		panel_Control.add(comboBox_1);
		
		JLabel label_1 = new JLabel("停止位：");
		label_1.setBounds(21, 127, 54, 15);
		panel_Control.add(label_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
		comboBox_2.setBounds(74, 124, 78, 21);
		panel_Control.add(comboBox_2);
		
		JLabel label_2 = new JLabel("数据位：");
		label_2.setBounds(21, 172, 54, 15);
		panel_Control.add(label_2);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"8", "7", "6", "5"}));
		comboBox_3.setBounds(74, 169, 78, 21);
		panel_Control.add(comboBox_3);
		
		JLabel label_3 = new JLabel("奇偶校验：");
		label_3.setBounds(10, 218, 65, 15);
		panel_Control.add(label_3);
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setModel(new DefaultComboBoxModel(new String[] {"NONE", "ODD", "EVEN"}));
		comboBox_4.setBounds(74, 215, 78, 21);
		panel_Control.add(comboBox_4);
		
		JButton btn_Connect = new JButton("连接");
		btn_Connect.setBounds(31, 276, 111, 36);
		panel_Control.add(btn_Connect);
		
		JPanel panel_Send = new JPanel();
		panel_Send.setBorder(new TitledBorder(null, "\u6570\u636E\u53D1\u9001", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_Send.setBounds(197, 10, 327, 195);
		contentPane.add(panel_Send);
		panel_Send.setLayout(null);
		
		JTextArea textArea_Send=new JTextArea();
		textArea_Send.setLineWrap(true);
		JScrollPane jScrollPane_Send=new JScrollPane(textArea_Send);
		jScrollPane_Send.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane_Send.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane_Send.setBounds(10, 23, 307, 119);
		panel_Send.add(jScrollPane_Send);
		JButton btn_Send = new JButton("发送");
		btn_Send.setBounds(108, 152, 111, 36);
		panel_Send.add(btn_Send);
		
		JPanel panel_Receive = new JPanel();
		panel_Receive.setBorder(new TitledBorder(null, "\u6570\u636E\u63A5\u6536", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_Receive.setBounds(197, 215, 327, 195);
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
		panel_Identify.setBorder(new TitledBorder(null, "\u6807\u8BC6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_Identify.setBounds(197, 420, 327, 131);
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
