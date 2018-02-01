package PetriTool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.TextComponent;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.lang.invoke.ConstantCallSite;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class GmecControl extends JFrame {

	private JPanel contentPane;
	private JTextField textField_4;
	private JPanel constraintPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GmecControl frame = new GmecControl();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private Vector<GmecModule> gmecModuleVector = new Vector<>();
	
	private int labelYCoordinate = 13;
	private int textYCoordinate = 10;
	private int count = 0;
	
	class GmecModule {
		private JLabel labelL;
		private JLabel labelB;
		private static final int distance = 28; 
		private JTextField textL;
		private JTextField textB;
		public GmecModule() {
			labelL = new JLabel("L"+count+":");
			labelB = new JLabel("B"+count+":");
			textL = new JTextField();
			textB = new JTextField();
			labelL.setBounds(10,labelYCoordinate,24,15);
			textL.setBounds(37,textYCoordinate,210,21);
			labelB.setBounds(270,labelYCoordinate,28,15);
			textB.setBounds(298,textYCoordinate,82,21);
			
			constraintPanel.add(labelL);
			constraintPanel.add(labelB);
			constraintPanel.add(textB);
			constraintPanel.add(textL);
			
			labelYCoordinate+=distance;
			textYCoordinate+=distance;
			count++;
			if(textYCoordinate>constraintPanel.getHeight())
			{
				constraintPanel.setPreferredSize(new Dimension(constraintPanel.getWidth(), textYCoordinate));
				constraintPanel.revalidate();
			}
			repaint();
		}
	}
	
	
	
	
	
	

	/**
	 * Create the frame.
	 */
	public GmecControl() {
		setTitle("GMEC Control");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 463, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("L¦Ì¡Üb");
		label.setBounds(10, 79, 54, 15);
		label.setFont(new Font("Cambria Math", Font.PLAIN, 13));
		contentPane.add(label);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 97, 427, 150);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		constraintPanel = new JPanel();
		scrollPane.setViewportView(constraintPanel);
		constraintPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gmecModuleVector.add(new GmecModule());
			}
		});
		btnNewButton.setBounds(250, 268, 74, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Ok");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				
				
				
				
			}
		});
		btnNewButton_1.setBounds(334, 268, 74, 23);
		contentPane.add(btnNewButton_1);
		
		JLabel lblL_1 = new JLabel("The number of element of L\u03BC is equal to the number of place");
		lblL_1.setBounds(60, 78, 372, 15);
		contentPane.add(lblL_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(10, 29, 427, 40);
		contentPane.add(scrollPane_1);
		
		textField_4 = new JTextField();
		scrollPane_1.setViewportView(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblPlaceOrder = new JLabel("Place order");
		lblPlaceOrder.setBounds(10, 10, 80, 15);
		contentPane.add(lblPlaceOrder);
	}
}
