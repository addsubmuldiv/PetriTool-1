package PetriTool;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sun.istack.internal.FinalArrayList;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditArc extends JDialog {
	private JTextField textField_arcOption;

	private Arc arcEdited_;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			EditArc dialog = new EditArc();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void setArcEdited_(Arc arcEdited_) {
		this.arcEdited_ = arcEdited_;
	}

	/**
	 * Create the dialog.
	 */
	public EditArc(final PetriTool petriTool_,final DesignPanel designPanel_) {
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
			btn_OK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String num=textField_arcOption.getText().trim();
					if(num.matches("^[1-9]*[1-9][0-9]*$"))
					{
						int arcNum=Integer.parseInt(textField_arcOption.getText().trim());
						arcEdited_.setTokensToEnable(arcNum);
						arcEdited_.draw(designPanel_.getGraphics(), petriTool_.gridStep_, petriTool_.foregroundColor_);
						designPanel_.repaint();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please input a Integer");
						return;
					}
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
