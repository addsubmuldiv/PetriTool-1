package PetriTool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class CommunicationMethod implements ActionListener,Observer{

	PetriTool petriTool_;
	public CommunicationMethod(PetriTool petriTool) {
		// TODO Auto-generated constructor stub
		this.petriTool_=petriTool;
		setUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		connectToDevice();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		connectToDevice();
	}
	
	
	
	
	
	
	
	public void connectToDevice()
	{
		SerialPortManagement serialPortManagement=new SerialPortManagement(petriTool_);
		serialPortManagement.setVisible(true);
	}
	
	
	public void output()
	{
		JOptionPane.showMessageDialog(null, "output is ...");
	}
	
	
	public void setUI()
	{
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();  
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}


}
