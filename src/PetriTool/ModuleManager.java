package PetriTool;

import java.awt.Component;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;



public class ModuleManager
{

   
    private JTree moduleTree;
    private DefaultTreeModel treeModel;
    private DefaultMutableTreeNode load_modules;

    public void ModuleManager(Graphics g) {
		// TODO 自动生成的方法存根
    	}
   
    public JTree getModuleTree( )
{
    // get the names of all the classes that are confirmed to be modules
    Vector names = new Vector();
    Vector classes = new Vector();

    
    // create the root node
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Analysis Module Manager");

    // create root children
    load_modules = new DefaultMutableTreeNode("Available Modules");

   
    root.add(load_modules);
   

    treeModel = new DefaultTreeModel(root);

    moduleTree = new JTree(treeModel);
    moduleTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION);

 //   moduleTree.addMouseListener(new TreeHandler());

    moduleTree.setFocusable(false);

    // expand the modules path
    moduleTree.expandPath(moduleTree.getPathForRow(1));
    return moduleTree;
}

	
		
	

}
