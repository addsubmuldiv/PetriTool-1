package PetriTool;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

public class MiningMethod implements ActionListener,Observer{

	private PetriTool petriTool_;
	public MiningMethod(PetriTool petriTool) {
		// TODO Auto-generated constructor stub
		this.petriTool_=petriTool;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		MenuItem item=(MenuItem) e.getSource();
		String source=item.getLabel();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		String source=(String)arg;
	}
	
	public void selectMethod(String source)
	{
		switch (source) {
		case "alpah mining":
			alphaMining();
			break;
		case "delta mining":
			deltaMining();
			break;
		default:
			break;
		}
	}
	
	public void alphaMining()
	{
		JOptionPane.showMessageDialog(null, "alpha mining");
	}
	
	public void deltaMining()
	{
		
	}
	
	
}
