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

public class EditArc extends JDialog {
	private JTextField textField_arcOption;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditArc dialog = new EditArc();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditArc() {
		setTitle("Edit Arc");
		setBounds(100, 100, 410, 244);
		getContentPane().setLayout(null);
		{
			JLabel lblNewLabel_arcOption = new JLabel("Arc Option:");
			lblNewLabel_arcOption.setFont(new Font("宋体", Font.PLAIN, 14));
			lblNewLabel_arcOption.setBounds(73, 63, 83, 15);
			getContentPane().add(lblNewLabel_arcOption);
		}
		{
			textField_arcOption = new JTextField();
			textField_arcOption.setBounds(166, 60, 151, 21);
			getContentPane().add(textField_arcOption);
			textField_arcOption.setColumns(10);
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
