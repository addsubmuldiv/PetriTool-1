package PetriTool;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import java.awt.Font;

public class EditTransition extends JDialog {
	private JTextField textField_transitionName;
	
	private Transition transitionEdited_;
	
	public Transition getTransitionEdited_() {
		return transitionEdited_;
	}

	public void setTransitionEdited_(Transition transitionEdited_) {
		this.transitionEdited_ = transitionEdited_;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditTransition dialog = new EditTransition();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditTransition() {
		setTitle("Edit Transition");
		setBounds(100, 100, 410, 244);
		getContentPane().setLayout(null);
		{
			JLabel lblNewLabel_transitionName = new JLabel("Transition Name:");
			lblNewLabel_transitionName.setFont(new Font("宋体", Font.PLAIN, 14));
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
			btn_OK.setBounds(85, 143, 93, 23);
			getContentPane().add(btn_OK);
		}
		{
			JButton btn_Cancel = new JButton("Cancel");
			btn_Cancel.setBounds(215, 143, 93, 23);
			getContentPane().add(btn_Cancel);
		}
		/**Set the window style**/
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

}
