package PetriTool;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import PetriTool.ControlTreePanel.User;

/**
 * @author Lighters_c
 * This class contains methods that being used to control.
 */
public class ControlMethod implements ActionListener,TreeSelectionListener{
	
	
	PetriTool petriTool_;
	JTree tree;
	DesignPanel designPanel_;
	Vector<Place> placeVector_;
	Vector<Transition>transitionVector_;
	Vector<Token>tokenVector_;
	Vector<Arc> arcVector_;
	
	
	
	@SuppressWarnings("unchecked")
	public ControlMethod(PetriTool petriTool) {
		this.petriTool_=petriTool;
		this.designPanel_=petriTool.designPanel_;
		placeVector_=designPanel_.placeVector_;
		transitionVector_=designPanel_.transitionVector_;
		tokenVector_=designPanel_.tokenVector_;
		arcVector_=designPanel_.arcVector_;
		setUI();
	}
	
	@SuppressWarnings("unchecked")
	public ControlMethod(PetriTool petriTool,JTree tree) {
		this(petriTool);
		this.tree=tree;
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
 		if (node == null)
 			return;
 		Object object = node.getUserObject();
 		if (node.isLeaf()) 
 		{
 			User user = (User) object;
 			switch (user.toString()) {
 			case "siphon control": siphonControl(); break;
			case "blank1": blank(); break;
			case "blank2": blank(); break;
			case "GMEC": GMECcontrol(); break;
			default: break;
			}
 		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		MenuItem source=(MenuItem)(e.getSource());
		String menuItemName=source.getLabel();
		switch(menuItemName)
		{
			case "Siphon Control": siphonControl(); break;
			case "blank": blank(); break;
			case "GMEC Control": GMECcontrol(); break;
			default: break;
		}
	}
	
	public void siphonControl()
	{
//		JOptionPane.showMessageDialog(null, "Siphon Control method is invoked!");
//		outputInterface();
		if(!transitionVector_.isEmpty())
		{
			Random random=new Random();
			int xCoordinate;
			int yCoordinate;
			do {
				xCoordinate=random.nextInt(petriTool_.gridWidth_);
				
				yCoordinate=random.nextInt(petriTool_.gridHeight_);
			}while(designPanel_.gridSpaceOccupied(xCoordinate*petriTool_.gridStep_, yCoordinate*petriTool_.gridStep_));
			Place newPlace=new Place(xCoordinate,yCoordinate,petriTool_.getPlaceLabel());
			placeVector_.add(newPlace);
			Arc newArc=new Arc(xCoordinate,yCoordinate);
			newArc.setStartComponent("Place");
			newArc.setEndComponent("Transition");
			newArc.addCoordinates(transitionVector_.firstElement().getXCoordinate(),
					transitionVector_.firstElement().getYCoordinate());
			newArc.setTokensToEnable(petriTool_.componentPanel_.
                    getTokensToEnable());
			newArc.calculateSlopes();
			newArc.setArcDrawCoordinates();
			arcVector_.add(newArc);
			
			designPanel_.repaint();
		}
	}
	
	public void blank()
	{
		JOptionPane.showMessageDialog(null, "Blank method is invoked!");
		output();
	}
	
	public void GMECcontrol()
	{
		JOptionPane.showMessageDialog(null, "GMEC control method is invoked!");
		output();
	}

	public void output()
	{
		JOptionPane.showMessageDialog(null, "Output is ...");
	}

	
	public void setUI()
	{
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();  
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}  
	}
}
