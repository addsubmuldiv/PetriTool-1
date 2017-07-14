package PetriTool;
import java.awt.FlowLayout;

import javafx.geometry.Dimension2DBuilder;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.JScrollPane;


public class TreePanel extends JTree {
    
	 /**
      * Used to access the system variables that are
      * changed by the user.
    **/
    protected PetriTool petriTool_;
	
	public TreePanel(PetriTool app) {
        
		petriTool_ = app;
		
		// ---------------------------------------------------------
        // Create a control tree
        // ---------------------------------------------------------
        
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Deadlock Control");
        
        node1.add(new DefaultMutableTreeNode(new User("siphon control")));
        node1.add(new DefaultMutableTreeNode(new User("blank1")));
        node1.add(new DefaultMutableTreeNode(new User("blank2")));
 
        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Operation Control");
        node2.add(new DefaultMutableTreeNode(new User("GMEC")));
        node2.add(new DefaultMutableTreeNode(new User("blank1")));
        node2.add(new DefaultMutableTreeNode(new User("blank2")));
 
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Control");
 
        top.add(new DefaultMutableTreeNode(new User("Single Control method")));
        top.add(node1);
        top.add(node2);
        
        final JTree tree = new JTree(top);
       
        this.add(tree);  
                      
//      tree.setSize(200,200);
        
        repaint();
       
        
        
        
      
        
        // ADD the response events
        tree.addTreeSelectionListener(new TreeSelectionListener() {
 
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();
 
                if (node == null)
                    return;
 
                Object object = node.getUserObject();
                if (node.isLeaf()) {
                    User user = (User) object;
                    System.out.println("Your Choice is " + user.toString());
                }
 
            }
        });
    }

 class User {
    private String name;
 
    public User(String n) {
        name = n;
    }
 
    // The important is in "toString", the text shown is the string in "toString"
    public String toString() {
        return name;
    }
}
}