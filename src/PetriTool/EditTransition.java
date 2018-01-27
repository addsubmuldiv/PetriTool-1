package PetriTool;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.PRIVATE_MEMBER;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditTransition extends JDialog {
	private JTextField textField_transitionName;
	
	private Transition transitionEdited_;
	private JTextField textField_speed;
	
	public Transition getTransitionEdited_() {
		return transitionEdited_;
	}

	public void setTransitionEdited_(Transition transitionEdited_) {
		this.transitionEdited_ = transitionEdited_;
	}

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			EditTransition dialog = new EditTransition();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public EditTransition(final PetriTool petriTool_,final DesignPanel designPanel_) {
		
		
		setTitle("Edit Transition");
		setBounds(100, 100, 410, 244);
		getContentPane().setLayout(null);
		{
			JLabel lblNewLabel_transitionName = new JLabel("Transition Name:");
			lblNewLabel_transitionName.setFont(new Font("Dialog", Font.PLAIN, 14));
			lblNewLabel_transitionName.setBounds(49, 63, 124, 15);
			getContentPane().add(lblNewLabel_transitionName);
		}
		{
			textField_transitionName = new JTextField();
			textField_transitionName.setBounds(171, 60, 151, 21);
			getContentPane().add(textField_transitionName);
			textField_transitionName.setColumns(10);
		}
		{
			JButton btn_OK = new JButton("OK");
			btn_OK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String transitionName=textField_transitionName.getText().trim().toString();
					if(transitionName.length()==0)
					{
						JOptionPane.showMessageDialog(null, "Transition name can't be empty!!");
						return;
					}
					transitionEdited_.setTransitionName_(transitionName);
					transitionEdited_.draw(designPanel_.getGraphics(), petriTool_.gridStep_,
							petriTool_.foregroundColor_, petriTool_.transitionLabels_);
							designPanel_.repaint();
					String speedText = textField_speed.getText().trim();
					//use regex to ensure the speed is an int
					if(!speedText.matches("^\\+?[1-9][0-9]*$")) {
						JOptionPane.showMessageDialog(null, "Invalid input");
						return;
					}
					int transitionSpeed = Integer.parseInt(speedText);
					if(transitionSpeed>10) {
						JOptionPane.showMessageDialog(null, "Please ensure the speed is in [1~10]");
						return;
					}
					transitionEdited_.setSpeed(transitionSpeed);
					dispose();
				}
			});
			btn_OK.setBounds(85, 143, 93, 23);
			getContentPane().add(btn_OK);
		}
		{
			JButton btn_Cancel = new JButton("Cancel");
			btn_Cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btn_Cancel.setBounds(215, 143, 93, 23);
			getContentPane().add(btn_Cancel);
		}
		
		JLabel lblNewLabel_speed = new JLabel("Speed:");
		lblNewLabel_speed.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNewLabel_speed.setBounds(111, 94, 53, 15);
		getContentPane().add(lblNewLabel_speed);
		
		textField_speed = new JTextField();
		textField_speed.setColumns(10);
		textField_speed.setBounds(171, 91, 151, 21);
		getContentPane().add(textField_speed);
		/**Set the window style**/
		String lookAndFeel =   
                UIManager.getSystemLookAndFeelClassName();  
            try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}  
	}
}
