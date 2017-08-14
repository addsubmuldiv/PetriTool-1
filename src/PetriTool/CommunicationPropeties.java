package PetriTool;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Window;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.IntrospectionException;
import java.awt.event.ActionEvent;
import static java.util.stream.Collectors.*;


public class CommunicationPropeties extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldStartBits;
	private JTextField textFieldEndBits;
	public static String dataToDecorate="";
	public static String startBits="";
	public static String endBits="";
	public static String otherBits="";
	public static String dataDecorated="";
	private static CommunicationPropeties communicationPropeties;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CommunicationPropeties frame = new CommunicationPropeties();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private int propetiesYCoordinate=69;
	
	private final int ROWSPACE=31;
	private ButtonGroup propetiesGroup=new ButtonGroup();
	
	class Propeties
	{
		protected JRadioButton jButton;
		protected JTextField propetiesName;
		protected JTextField propetiesValue;
		protected int yCoordinate;
		
		
	
		
		public int getyCoordinate() {
			return yCoordinate;
		}

		public void setLocation(int y)
		{
			this.jButton.setLocation(jButton.getX(), y);
			this.propetiesName.setLocation(propetiesName.getX(), y);
			this.propetiesValue.setLocation(propetiesValue.getX(), y);
			yCoordinate=y;
		}


		public Propeties()
		{
			yCoordinate=propetiesYCoordinate;
			jButton=new JRadioButton("");
			jButton.setBounds(18, propetiesYCoordinate, 21, 21);
			
			propetiesName=new JTextField();
			propetiesName.setBounds(45, propetiesYCoordinate, 104, 21);
			
			propetiesValue=new JTextField();
			propetiesValue.setBounds(159, propetiesYCoordinate, 138, 21);
			
			propetiesGroup.add(jButton);
			
			
			propetiesPanel.add(jButton);
			propetiesPanel.add(propetiesName);
			propetiesPanel.add(propetiesValue);
			propetiesYCoordinate+=ROWSPACE;			
			if(propetiesYCoordinate>scrollPanePropeties.getHeight())
			{
				propetiesPanel.setPreferredSize(new Dimension(scrollPanePropeties.getWidth(), propetiesYCoordinate));
				propetiesPanel.revalidate();
			}
			repaint();

		}
	}
	
	private ArrayList<Propeties> propetiesList;
	
	private JScrollPane scrollPanePropeties; 
	
	ButtonGroup rdButtonGroup;
	JRadioButton rdbtnHEX; 
	JRadioButton rdbtnDEC; 
	
	public static boolean isHEX=false;
	
	private JPanel propetiesPanel;
	/**
	 * Create the frame.
	 */
	public CommunicationPropeties() {
		setResizable(false);
		setTitle("Communication Propeties");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 366, 298);
		communicationPropeties=this;
        this.addWindowListener(new Win());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		propetiesList=new ArrayList<>();
		JButton btn_Up = new JButton("Up");
		btn_Up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<propetiesList.size();i++)
				{
					if(propetiesList.get(i).jButton.isSelected())
					{	
						if(i==0)
						{
							return;
						}
						Propeties tempModule=propetiesList.get(i);
						tempModule.setLocation(tempModule.getyCoordinate()-ROWSPACE);
						Propeties lastModule=propetiesList.get(i-1);
						lastModule.setLocation(lastModule.getyCoordinate()+ROWSPACE);
						
						propetiesList.set(i, lastModule);
						propetiesList.set(i-1, tempModule);
						break;
					}
				}
				repaint();
			}
		});
		btn_Up.setFont(UIManager.getFont("Button.font"));
		btn_Up.setToolTipText("Up");
		btn_Up.setBounds(10, 236, 60, 23);
		contentPane.add(btn_Up);
		
		JButton btn_Down = new JButton("Down");
		btn_Down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<propetiesList.size();i++)
				{
					if(propetiesList.get(i).jButton.isSelected())
					{	
						if(i==propetiesList.size()-1)
						{
							return;
						}
						Propeties tempModule=propetiesList.get(i);
						tempModule.setLocation(tempModule.getyCoordinate()+ROWSPACE);
						Propeties nextModule=propetiesList.get(i+1);
						nextModule.setLocation(nextModule.getyCoordinate()-ROWSPACE);
						
						propetiesList.set(i, nextModule);
						propetiesList.set(i+1, tempModule);
						break;
					}
				}
				repaint();
			}
		});
		btn_Down.setFont(UIManager.getFont("Button.font"));
		btn_Down.setToolTipText("Down");
		btn_Down.setBounds(80, 236, 60, 23);
		contentPane.add(btn_Down);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnHEX.isSelected())
				{
					String start=textFieldStartBits.getText();
					String end=textFieldEndBits.getText();
					String other="";
					if(start.length()==0||end.length()==0)
						JOptionPane.showMessageDialog(null, "Start and end bits can't be empty!");
					startBits=start;
					endBits=end;
					if(propetiesList.size()!=0)
					{
						other=propetiesList.stream().
								map(p->p.propetiesValue.getText()).
								collect(joining(" "));
					}
					otherBits=other+" ";
					isHEX=true;
					communicationPropeties.setVisible(false);
					return;
				}
				isHEX=false;
				communicationPropeties.setVisible(false);
			}
		});
		btnOk.setFont(UIManager.getFont("Button.font"));
		btnOk.setToolTipText("OK");
		btnOk.setBounds(290, 236, 60, 23);
		contentPane.add(btnOk);
		
		scrollPanePropeties = new JScrollPane();
		scrollPanePropeties.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanePropeties.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanePropeties.setBounds(10, 10, 340, 194);
		contentPane.add(scrollPanePropeties);
		
		propetiesPanel= new JPanel();
		propetiesPanel.setLayout(null);
		scrollPanePropeties.setViewportView(propetiesPanel);
		
		JLabel lblStartBits = new JLabel("Start bits");
		lblStartBits.setBounds(61, 7, 72, 21);
		propetiesPanel.add(lblStartBits);
		
		textFieldStartBits = new JTextField();
		textFieldStartBits.setBounds(159, 7, 138, 21);
		propetiesPanel.add(textFieldStartBits);
		textFieldStartBits.setColumns(10);
		
		JLabel lblEndBits = new JLabel("End bits");
		lblEndBits.setBounds(71, 38, 60, 21);
		propetiesPanel.add(lblEndBits);
		
		textFieldEndBits = new JTextField();
		textFieldEndBits.setColumns(10);
		textFieldEndBits.setBounds(159, 38, 138, 21);
		propetiesPanel.add(textFieldEndBits);
		
		JButton btn_Add = new JButton("Add");
		btn_Add.setFont(UIManager.getFont("Button.font"));
		btn_Add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				propetiesList.add(new Propeties());
			}
		});
		btn_Add.setToolTipText("Add");
		btn_Add.setBounds(150, 236, 60, 23);
		contentPane.add(btn_Add);
		
		JButton btn_Delete = new JButton("Drop");
		btn_Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<propetiesList.size();i++)
				{
					if(propetiesList.get(i).jButton.isSelected())
					{
						propetiesPanel.remove(propetiesList.get(i).jButton);
						propetiesPanel.remove(propetiesList.get(i).propetiesName);
						propetiesPanel.remove(propetiesList.get(i).propetiesValue);
						repaint();
						propetiesList.remove(i);
						propetiesList.stream().skip(i).forEach(m->
						{
//							m.jButton.setLocation(m.jButton.getX(), m.jButton.getY()-ROW_SPACE);
//							m.moduleName.setLocation(m.moduleName.getX(), m.moduleName.getY()-ROW_SPACE);
//							m.placeDistinction.setLocation(m.placeDistinction.getX(), m.placeDistinction.getY()-ROW_SPACE);
							m.setLocation(m.getyCoordinate()-ROWSPACE);
						});
					}
				}
				propetiesYCoordinate-=ROWSPACE;
				repaint();
			}
		});
		btn_Delete.setFont(UIManager.getFont("Button.font"));
		btn_Delete.setToolTipText("Delete");
		btn_Delete.setBounds(220, 236, 60, 23);
		contentPane.add(btn_Delete);
		
		rdButtonGroup=new ButtonGroup();
		rdbtnHEX= new JRadioButton("hexadecimal");
		rdbtnHEX.setBounds(59, 210, 121, 23);
		contentPane.add(rdbtnHEX);
		
		rdbtnDEC = new JRadioButton("Decimal");
		rdbtnDEC.setBounds(206, 210, 121, 23);
		contentPane.add(rdbtnDEC);
		
		rdButtonGroup.add(rdbtnDEC);
		rdButtonGroup.add(rdbtnHEX);
		
		setUI();
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
	
	public String decorateData(String data)
	{
		String[] dataArray=data.split(" ");
		ArrayList<String> dataList=new ArrayList<>();
		for(int i=0;i<dataArray.length;i++)
		{
			dataArray[i]="0"+dataArray[i];
			dataList.add(dataArray[i]);
		}
		String dataBits=dataList.stream().collect(joining(" "));
		return startBits+" "+otherBits+dataBits+" "+endBits+" 0D 0A";
	}
	
	
	
	public String parseData(String hexData)
	{
		String data=hexData.substring(startBits.length()).trim();
		int endBitsPosition=data.indexOf(endBits);
		String dataToParse=data.substring(0, endBitsPosition).trim();
		String[] dataArray=dataToParse.split(" ");
		StringBuilder sBuilder=new StringBuilder();
		for(int i=0;i<dataArray.length;i++)
		{
			dataArray[i]=dataArray[i].substring(1);
			sBuilder.append(dataArray[i]);
			sBuilder.append(" ");
		}
		return sBuilder.toString().trim();
	}
	
	
	
	
	
	class Win extends WindowAdapter {
		/**
		 * The key to fix the can't close bug
		 * **/
	    public void windowClosing(WindowEvent e) 
	    {
	        e.getWindow().setVisible(false);
	        ((Window) e.getComponent()).dispose();
	    }
	}
	
	
}
