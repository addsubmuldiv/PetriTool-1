package PetriTool;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import javax.swing.JLabel;
import java.awt.Font;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import static java.util.stream.Collectors.*;
public class GmecControl extends JFrame {

	private JPanel contentPane;
	private JTextArea tipField;
	private JPanel constraintPanel;

	private Vector<GmecModule> gmecModuleVector = new Vector<>();
	
	private int labelYCoordinate = 13;
	private int textYCoordinate = 10;
	private int count = 0;
	private static final int distance = 28; 
	class GmecModule {
		private JLabel labelL;
		private JLabel labelB;
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
	
	
	public void setTip() {
		Vector<Place> places = PetriTool.designPanel_.placeVector_;
		String tip = places.stream().map((place)->place.placeName_).collect(joining("  "));
		tipField.setText(tip);
	}
	
	
	private static int[] gmecL;
	
	/**
	 * get the token number of each place
	 * @return
	 */
	public int[] getUp() {
		Vector<Place> places = PetriTool.designPanel_.placeVector_;
		int[] token = new int[places.size()];
		for (int i = 0; i < places.size(); i++) {
			token[i] = places.get(i).getNumTokens();
		}
		return token;
	}

	
	public int[] getTheNewPlaceArc(int[] gmecL,int[][] dp) {
		int[] res = new int[dp[0].length];
		
		for(int i=0;i<dp[0].length;i++) {
			for(int j=0;j<gmecL.length;j++) {
				res[i]-=gmecL[j]*dp[j][i];
			}
		}
		
		return res;
	}
	
	
	public int getTheNewPlaceToken(int[] gmecL,int[] up,int b) {
		int res = 0;
		for(int i=0;i<gmecL.length;i++) {
			res+=gmecL[i]*up[i];
		}
		return b-res;
	}
	
	
	
	public static int getRandomCoordinate(int bound,int border) {
		Random random = new Random();
		int x = random.nextInt(bound);
		while(x>bound-border||x<border) {
			x=random.nextInt(bound);
		}
		return x;
	}
	
	private static int placeCount = 0;
	
	/**
	 * Add the control place and set its arc and token
	 * @param placeToTransition
	 * @param token
	 */
	public void addNewPlace(int[] placeToTransition,int token) {
		//TODO
		Vector<Arc> arcVector = PetriTool.designPanel_.arcVector_;
		Vector<Place> placeVector = PetriTool.designPanel_.placeVector_;
		Vector<Transition> transitionVector = PetriTool.designPanel_.transitionVector_;
		Vector<Token> tokenVector = PetriTool.designPanel_.tokenVector_;
		int x = getRandomCoordinate(PetriTool.gridWidth_, 5);
		int y = getRandomCoordinate(PetriTool.gridHeight_, 5);
		
		while(PetriTool.designPanel_.gridSpaceOccupied(x*PetriTool.gridStep_, 
				y*PetriTool.gridStep_)||
				!PetriTool.designPanel_.validDimension(x*PetriTool.gridStep_, 
						y*PetriTool.gridStep_)) {
			x = getRandomCoordinate(PetriTool.gridWidth_, 5);
			y = getRandomCoordinate(PetriTool.gridHeight_, 5);
		}
		
		Place place= new Place(x,y,PetriTool.getPlaceLabel());
		place.setplaceName_("C" + placeCount++);
		//set token
		Token tempToken = new Token(x, y, token);
		tokenVector.add(tempToken);
		place.setToken(tempToken);
		//set arc
		for(int i=0;i<placeToTransition.length;i++) {
			if(placeToTransition[i]<0) {
				Arc tempArc = new Arc(place,transitionVector.get(i));
				tempArc.setTokensToEnable(Math.abs(placeToTransition[i]));
				arcVector.add(tempArc);
			} else if(placeToTransition[i]>0) {
				Arc tempArc = new Arc(transitionVector.get(i),place);
				tempArc.setTokensToEnable(Math.abs(placeToTransition[i]));
				arcVector.add(tempArc);
			} else {
				continue;
			}
		}
		PetriTool.designPanel_.placeVector_.add(place);
		//set global environment
		PetriTool.destroySimulation();
		PetriTool.designPanel_.setInitialMarking();
		PetriTool.designPanel_.repaint();
	}
	
	public void calculatePlace(IncidenceMatrix incidenceMatrix) {
		for(int i=0;i<gmecModuleVector.size();i++) {
			GmecModule gmecModule = gmecModuleVector.get(i);
			if(StringUtils.isNotBlank(gmecModule.textB.getText())
					&&StringUtils.isNotBlank(gmecModule.textL.getText())) {
				//we use space to split the array
				String[] gmecLstr = gmecModule.textL.getText().split(" ");
				if(gmecLstr.length==PetriTool.designPanel_.placeVector_.size()) {
					gmecL = new int[gmecLstr.length];
					for (int j = 0; j < gmecL.length; j++) {
						gmecL[j]=Integer.parseInt(gmecLstr[j]);
					}				
					int[] newPlaceArc = getTheNewPlaceArc(gmecL, incidenceMatrix.incidenceMatrix);
					int token = getTheNewPlaceToken(gmecL, getUp(), Integer.parseInt(gmecModule.textB.getText()));
					addNewPlace(newPlaceArc, token);
				} else {
					continue;
				}
			}
		}
	}
	
	
	
	/**
	 * Create the frame.
	 */
	public GmecControl() {
		setTitle("GMEC Control");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 463, 479);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("L¦Ì¡Üb");
		label.setBounds(10, 218, 54, 15);
		label.setFont(new Font("Cambria Math", Font.PLAIN, 13));
		contentPane.add(label);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 236, 427, 150);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		constraintPanel = new JPanel();
		scrollPane.setViewportView(constraintPanel);
		constraintPanel.setLayout(null);
		
		JButton btn_add = new JButton("Add");
		btn_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				gmecModuleVector.add(new GmecModule());
			}
		});
		btn_add.setBounds(190, 407, 74, 23);
		contentPane.add(btn_add);
		
		JButton btnNewButton_1 = new JButton("Ok");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IncidenceMatrix incidenceMatrix = new IncidenceMatrix();
				calculatePlace(incidenceMatrix);
			}
		});
		btnNewButton_1.setBounds(358, 407, 74, 23);
		contentPane.add(btnNewButton_1);
		
		JLabel lblL_1 = new JLabel("The number of element of L\u03BC is equal to the number of place");
		lblL_1.setBounds(60, 217, 372, 15);
		contentPane.add(lblL_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(10, 29, 427, 179);
		contentPane.add(scrollPane_1);
		
		tipField = new JTextArea();
		tipField.setEditable(false);
		scrollPane_1.setViewportView(tipField);
		tipField.setColumns(10);
		tipField.setLineWrap(true);
		tipField.setBackground(getBackground());
		setTip();
		
		
		
		JLabel lblPlaceOrder = new JLabel("Place order");
		lblPlaceOrder.setBounds(10, 10, 80, 15);
		contentPane.add(lblPlaceOrder);
		
		JButton btn_delete = new JButton("Delete");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GmecModule temp = gmecModuleVector.lastElement();
				constraintPanel.remove(temp.labelB);
				constraintPanel.remove(temp.labelL);
				constraintPanel.remove(temp.textB);
				constraintPanel.remove(temp.textL);
				gmecModuleVector.remove(temp);
				textYCoordinate-=distance;
				labelYCoordinate-=distance;
				count--;
				constraintPanel.setPreferredSize(new Dimension(constraintPanel.getWidth(), textYCoordinate));
				constraintPanel.revalidate();
				repaint();
			}
		});
		btn_delete.setBounds(274, 407, 74, 23);
		contentPane.add(btn_delete);
		//this is used to close this frame only
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
