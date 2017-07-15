package PetriTool;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ControlMethod implements ActionListener{
	
	
	PetriTool petriTool_;
	
	public ControlMethod(PetriTool petriTool) {
		// TODO Auto-generated constructor stub
		this.petriTool_=petriTool;
		String lookAndFeel =   
	            UIManager.getSystemLookAndFeelClassName();  
	        try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
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
		JOptionPane.showMessageDialog(null, "Siphon Control method is invoked!");
	}
	
	public void blank()
	{
		JOptionPane.showMessageDialog(null, "Blank method is invoked!");
	}
	
	public void GMECcontrol()
	{
		JOptionPane.showMessageDialog(null, "GMEC control method is invoked!");
	}

	
}
