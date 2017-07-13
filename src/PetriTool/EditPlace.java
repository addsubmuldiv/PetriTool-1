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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditPlace extends JDialog {
	private JTextField textField_placeName;
	private JTextField textField_tokenOption;
	
	/**
	 * before edit a place, we must get its reference
	 * **/
	private Place placeEdited_;
	
	public Place getPlaceEdited_() {
		return placeEdited_;
	}

	public void setPlaceEdited_(Place placeEdited_) {
		this.placeEdited_ = placeEdited_;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditPlace dialog = new EditPlace();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditPlace() {
		setTitle("Edit Place");
		setBounds(100, 100, 410, 300);
		getContentPane().setLayout(null);
		{
			JLabel lblNewLabel_placeName = new JLabel("Place Name:");
			lblNewLabel_placeName.setFont(new Font("宋体", Font.PLAIN, 14));
			lblNewLabel_placeName.setBounds(85, 63, 93, 15);
			getContentPane().add(lblNewLabel_placeName);
		}
		{
			textField_placeName = new JTextField();
			textField_placeName.setBounds(171, 60, 151, 21);
			getContentPane().add(textField_placeName);
			textField_placeName.setColumns(10);
		}
		{
			JLabel lblNewLabel_tokenOption = new JLabel("Token Option:");
			lblNewLabel_tokenOption.setFont(new Font("宋体", Font.PLAIN, 14));
			lblNewLabel_tokenOption.setBounds(71, 120, 93, 15);
			getContentPane().add(lblNewLabel_tokenOption);
		}
		{
			textField_tokenOption = new JTextField();
			textField_tokenOption.setBounds(171, 117, 151, 21);
			getContentPane().add(textField_tokenOption);
			textField_tokenOption.setColumns(10);
		}
		{
			JButton btn_OK = new JButton("OK");
			btn_OK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
				}
			});
			btn_OK.setBounds(85, 185, 93, 23);
			getContentPane().add(btn_OK);
		}
		{
			JButton btn_Cancel = new JButton("Cancel");
			btn_Cancel.setBounds(215, 185, 93, 23);
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
