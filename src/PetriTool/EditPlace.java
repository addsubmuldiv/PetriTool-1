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
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class EditPlace extends JDialog {
	private JTextField textField_placeName;
	private JTextField textField_tokenOption;
	
	/**
	 * before edit a place, we must get its reference
	 * **/
	private Place placeEdited_;
	
	private Token tokenEdited_;
	
	public Token getTokenEdited_() {
		return tokenEdited_;
	}

	public void setTokenEdited_(Token tokenEdited_) {
		this.tokenEdited_ = tokenEdited_;
	}

	private PetriTool petriTool_;
	
	private DesignPanel designPanel_;
	
	public Place getPlaceEdited_() {
		return placeEdited_;
	}

	public void setPlaceEdited_(Place placeEdited_) {
		this.placeEdited_ = placeEdited_;
	}
	
	
	public void setText() {
		this.textField_placeName.setText(placeEdited_.getplaceName_());
		if(tokenEdited_!=null)
			this.textField_tokenOption.setText(String.valueOf(tokenEdited_.getTokensRepresented()));
		else
			this.textField_tokenOption.setText("0");
	}
	
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			EditPlace dialog = new EditPlace(petriTool_);
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public EditPlace(PetriTool petriTool,DesignPanel designPanel) {
		setTitle("Edit Place");
		setBounds(100, 100, 410, 300);
		petriTool_=petriTool;
		designPanel_=designPanel;
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
					String placeName=textField_placeName.getText().trim().toString();
					if(placeName.length()==0)
					{
						JOptionPane.showMessageDialog(null, "Place name can't be empty!!");
						return;
					}
					placeEdited_.setplaceName_(placeName);
					placeEdited_.draw(designPanel_.getGraphics(), petriTool_.gridStep_,
					petriTool_.foregroundColor_, true);
					designPanel_.repaint();
					//TODO
					if(textField_tokenOption.getText().trim().length()!=0&&placeEdited_.getNumTokens()==0)
					{
						System.out.println(placeEdited_.getNumTokens());
						JOptionPane.showMessageDialog(null, "Sorry! You hava to add a token first!");
						dispose();
						return;
					}
					if(tokenEdited_!=null&&textField_tokenOption.getText().trim().length()!=0)
					{	
					/**use RegEx to test the text if it is actually an integer**/
						if(textField_tokenOption.getText().trim().matches("^[1-9]*[1-9][0-9]*$"))
						{
							tokenEdited_.setTokensRepresented(Integer.parseInt(textField_tokenOption.getText()));
							tokenEdited_.draw(designPanel_.getGraphics(), petriTool_.gridStep_, petriTool_.foregroundColor_);
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Token number should be a Integer");
							return;
						}
					}
					designPanel_.repaint();
					dispose();
				}
			});
			btn_OK.setBounds(85, 185, 93, 23);
			getContentPane().add(btn_OK);
		}
		{
			JButton btn_Cancel = new JButton("Cancel");
			btn_Cancel.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
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
				e.printStackTrace();
			}  
	}

}



