package PetriTool;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;


public class MiningMethod implements ActionListener,Observer{

	private PetriTool petriTool_;
	public MiningMethod(PetriTool petriTool) {
		this.petriTool_=petriTool;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		MenuItem item=(MenuItem) e.getSource();
		String source=item.getLabel();
		selectMethod(source);
	}

	@Override
	public void update(Observable o, Object arg) {
		String source=(String)arg;
		selectMethod(source);
	}
	
	public void selectMethod(String source)
	{
		switch (source) {
		case "process mining":
			processMining();
			break;
		default:
			break;
		}
	}
	
	public void processMining()
	{
		//TODO
		String cmd = "cmd /c start /b resources\\ProM\\ProM.bat";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void output()
	{
		JOptionPane.showMessageDialog(null, "output is ...");
	}
}
