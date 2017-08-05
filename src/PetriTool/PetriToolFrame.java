
//****************************************************************************
// CLASS NAME:	PetriToolFrame.java
//
// AUTHOR:	Rick Brink
//		    rick@mail.csh.rit.edu
//		    http://www.csh.rit.edu/~rick
//
// VERSION:	1.0
//
// HISTORY:	4/16/96		Initial Version
//
// COPYRIGHT INFORMATION:
//
// This program and the Java source is in the public domain.
// Permission to use, copy, modify, and distribute this software
// and its documentation for NON-COMMERCIAL purposes and
// without fee is hereby granted.
//
//    Copyright 1996
//
//    Rick Brink
//    1266 Brighton-Henrietta Townline Rd.
//    Rochester, NY 14623
//
// DISCLAIMER:
//
// The author claims no responsibility for any damage, direct or indirect,
// to any harware or software as a result of using this program.
//****************************************************************************

package PetriTool;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Event;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.FileDialog;
import java.awt.CheckboxMenuItem;
import java.awt.Panel;
import java.util.Vector;
import java.lang.StringIndexOutOfBoundsException;
import java.io.IOException;

import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.javafx.geom.transform.BaseTransform.Degree;

import javafx.scene.control.Control;

/**
  * A class representing the Frame containing the PetriTool
  * components including the MenuBar, ControlPanel, DesignPanel,
  * and StatusPanel.
  *
  * @see PetriTool
  * @see ControlPanel
  * @see DesignPanel
  * @see StatusPanel
  * @see java.awt.Frame
  * @see java.awt.MenuItem
  * @see java.awt.MenuBar
  * @see java.awt.Men5
  * @see java.awt.BorderLayout
  * @see java.awt.Font
  * @see java.awt.Event
  * @see java.awt.Dimension
  * @see java.awt.Toolkit
  * @see java.awt.FileDialog
  * @see java.awt.CheckboxMenuItem
  * @see java.awt.Panel
  * @see java.util.Vector
  * @see java.lang.StringIndexOutOfBoundsException
  * @see java.io.IOException
  *
  * @version 1.0 July 3, 1996
  *
  * @author  Rick Brink
**/
class PetriToolFrame extends Frame {
    /** Parent PetriTool object that instantiated this Frame **/
    PetriTool petriTool_;

    /** MenuItem for the Save option **/
    MenuItem saveMenuItem_;

    MenuItem saveAsPnt_;
    /**
      * Generic MenuItem for options that a specific handle is not
      * required for
    **/
    MenuItem menuItem_;

    /** MenuItem for the Cut option **/
    MenuItem cutMenuItem_;

    /** MenuItem for the Copyoption **/
    MenuItem copyMenuItem_;

    /** MenuItem for the Paste option **/
    MenuItem pasteMenuItem_;

    /** MenuItem for the Delete option **/
    MenuItem deleteMenuItem_;

    /** TextDialog for accepting user input of Captions **/
    protected TextDialog textDialog_ = null;

    /** Vector of MenuItems which need to be disabled during simulation **/
    protected Vector menuItemsToDisable_;

    /** MenuBar for holding all of the Menus in the PetriToolFrame **/
    MenuBar menubar;

    /** Menu for File operations **/
    Menu file;

    /** Menu for Edit operations **/
    Menu edit;

    /** Menu for View operations **/
    Menu view;

    /** Menu for Draw operations **/
    Menu draw;

    /** Menu for Text operations **/
    Menu text;

    /** Menu for selecting Options **/
    Menu options;

    /** Menu for Simulation operations **/
    Menu simulation;

    /** Menu for Analysis operations **/
    Menu analysis;

    /** Menu for Help operations **/
    Menu help;
    
    /** Menu for Control operations **/
    Menu control;
    
    
    /** Menu for Communication operations **/
    Menu communication;
    
    /** Menu for process mining operations **/
    Menu mining;

    /** List of ImageButton names in the ControlPanel **/
    String[] buttonList = {"New", "Open", "Save", "Save_as", "Print", "Zoom_in", "Zoom_out","Pointer", "Place", "Token",
                           "Transition", "Arc", "Text",
                           "Reset", "RevStep", "ForStep",
                           "Run", "Pause", "Stop", "Calc", "Show",
                           "Prop", "Help", "ConnectToDevice", "alpha_mining", "delta_mining"};

    /** Directory where the current design will to be saved **/
    String saveFileDirectory_ = null;

    /** Filename for the current design **/
    String saveFileName_ = null;

    /** Dialog for File Open operations **/
    FileDialog openFileDialog_;

    /** Dialog for File Save operations **/
    FileDialog saveFileDialog_;

    /** Used to select Regular style fonts **/
    protected CheckboxMenuItem regularCheckboxMenuItem_;

    /** Used to select Bold style fonts **/
    protected CheckboxMenuItem boldCheckboxMenuItem_;

    /** Used to select Italic style fonts **/
    protected CheckboxMenuItem italicCheckboxMenuItem_;

    /** Vector to store Font names available on the current system **/
    protected Vector fontVector_;

    /** Vector of available font sizes **/
    protected Vector fontSizeVector_;

    /** Generic CheckboxMenuItem for selecting the current font **/
    protected CheckboxMenuItem checkbox_;

    /** Vector of font names available on the current system **/
    protected String[] fontList_;

    /** Temporary CheckBoxMenuItem used for seting the current font **/
    protected CheckboxMenuItem tempCheckboxMenuItem_;

    /** Used for viewing the contents of help files **/
    protected FileViewer helpDialog_;

    /**
      * Used to distinguish between TextDialogEvents, indicating
      * the last text entered was for a search
    **/
    protected boolean userWantsSearch_ = false;

	/**
      * Construct a new PetriToolFrame, instantiating a ControlPanel
      * placed North, a StatusPanel placed in the South, and a
      * DesignPanel placed in teh center.  A MenuBar is also created
      * for the frame with all available options created and placed
      * in the MenuBar.
    **/
    public PetriToolFrame(PetriTool petriTool) {
        super("PetriTool");
        menuItemsToDisable_ = new Vector();
        petriTool_ = petriTool;

        Panel middlePanel_ = new Panel();
        middlePanel_.setLayout(new BorderLayout());
        this.setLayout(new BorderLayout());

        petriTool_.controlPanel_ = new ControlPanel(buttonList, petriTool);
        middlePanel_.add("North", petriTool_.controlPanel_);

        petriTool_.designPanel_ = new DesignPanel(petriTool_);
        middlePanel_.add("Center", petriTool_.designPanel_);

        petriTool_.componentPanel_ = new ComponentPanel(petriTool_);
        middlePanel_.add("South", petriTool_.componentPanel_);
        
        petriTool_.controlTreePanel_ = new ControlTreePanel(petriTool_);
        middlePanel_.add("West", petriTool_.controlTreePanel_);

        this.add("Center", middlePanel_);
        petriTool_.statusPanel_ = new StatusPanel(petriTool_);
        this.add("South", petriTool_.statusPanel_);

        setMenuBar(PetriToolMenuBar());
        setSize(1220, 700);
        setVisible(true);
        
        this.addWindowListener(new Win());

    }

    /**
      * Display a status message in the status panel
    **/
    public void StatusMessage(String msg) {
        petriTool_.statusPanel_.StatusMessage(msg);
    }

    /**
      * Disable all menus in the MenuItemsToDisable vector
      * when simulation is in progress
    **/
    public void DisableAllMenus() {
        MenuItem TempMenuItem;

        for (int ix = 0; ix < menuItemsToDisable_.size(); ix++) {
            TempMenuItem = (MenuItem)menuItemsToDisable_.elementAt(ix);
            TempMenuItem.setEnabled(false);
        }
    }

    /**
      * Set up all the MenuBar menus
    **/
    public MenuBar PetriToolMenuBar() {


        // Create the menubar.  Tell the frame about it.
        menubar = new MenuBar();
        this.setMenuBar(menubar);

       
        
        
        // Create the file menu.  Add items to it.  Add to menubar.
               
        file = new Menu("File");
        menuItemsToDisable_.addElement (menuItem_ = new MenuItem ("New") );
        file.add(menuItem_);

        menuItemsToDisable_.addElement (menuItem_ = new MenuItem ("Open") );
        file.add(menuItem_);

        menuItemsToDisable_.addElement (menuItem_ = new MenuItem ("Close") );
        file.add(menuItem_);

        file.addSeparator();
        
        saveMenuItem_ = new MenuItem ("Save");

        saveMenuItem_.setEnabled(true); 

        menuItemsToDisable_.addElement (saveMenuItem_);

        file.add(saveMenuItem_);

        menuItemsToDisable_.addElement (menuItem_ = new MenuItem ("Save As"));
            
        file.add(menuItem_);
        
        saveAsPnt_=new MenuItem("Save as pnt");
        menuItemsToDisable_.addElement(saveAsPnt_);
        file.add(saveAsPnt_);
        saveAsPnt_.addActionListener((e)->{
        	saveFileDialog_ = new FileDialog(this, "Save As", FileDialog.SAVE);
            saveFileDialog_.setDirectory(".");
            saveFileDialog_.setFile("*.pnt");
            
            saveFileDialog_.setVisible(true);  // blocks until user selects a file
            if (saveFileDialog_.getFile() != null) {
            	
                saveFileName_ = saveFileDialog_.getFile();
                saveFileDirectory_ = saveFileDialog_.getDirectory();
                petriTool_.designPanel_.saveDesign (saveFileDirectory_ +
        				saveFileName_);
                
            }
        	
        	
        });
        
        
        file.addSeparator();
        
        menuItemsToDisable_.addElement (menuItem_ = new MenuItem ("Print"));
        file.add(menuItem_);

        file.addSeparator();
        menuItemsToDisable_.addElement (menuItem_ = new MenuItem ("Exit"));
        file.add(menuItem_);
        menubar.add(file);

        // Create the Edit menu.  Add items to it.  Add to menubar.
        edit = new Menu("Edit");
        menuItemsToDisable_.addElement (cutMenuItem_ = new MenuItem ("Cut \tCtrl+X"));
        edit.add(cutMenuItem_);
        //cutMenuItem_.setEnabled(false);
        menuItemsToDisable_.addElement (copyMenuItem_ = new MenuItem ("Copy \tCtrl+C"));
        edit.add(copyMenuItem_);
        //copyMenuItem_.setEnabled(false);
        menuItemsToDisable_.addElement (pasteMenuItem_ = new MenuItem ("Paste \tCtrl+V"));
        edit.add(pasteMenuItem_);
        //pasteMenuItem_.setEnabled(false);
        menuItemsToDisable_.addElement (deleteMenuItem_ = new MenuItem ("Delete \tDel"));
        edit.add(deleteMenuItem_);
        //deleteMenuItem_.setEnabled(false);

        edit.addSeparator();
        edit.add(new MenuItem("Select"));
        menubar.add(edit);

        // Create the View menu.  Add items to it.  Add to menubar.
        view = new Menu("View");
        view.add(new MenuItem("Refresh"));
        view.add(new MenuItem("Fit To Window"));
        view.add(new MenuItem("View Entire Grid"));
        view.addSeparator();
        view.add(new MenuItem("Zoom In\tCtrl-Z"));
        view.add(new MenuItem("Zoom Out\tCtrl-Q"));
        menubar.add(view);

        // Create the Draw menu.  Add items to it.  Add to menubar.
        draw = new Menu("Draw");
        draw.add(new MenuItem("Place"));
        draw.add(new MenuItem("Token"));
        draw.add(new MenuItem("Transition"));
        draw.add(new MenuItem("Arc"));
        draw.add(new MenuItem("Text"));
        menubar.add(draw);

        // Create the Text menu.  Add items to it.  Add to menubar.
        text = new Menu("Text");

        // Get a list of available fonts
        Toolkit toolkit_ = Toolkit.getDefaultToolkit();
        fontList_ = toolkit_.getFontList();

        // Choices for font style
        Menu fontMenu_ = new Menu("Font Style");
        fontVector_ = new Vector();

        for (int i = 0; i < fontList_.length; i++) {
            checkbox_ = new CheckboxMenuItem(fontList_[i]);

            if (fontList_[i].equals("Helvetica")) {
                checkbox_.setState(true);
            }

            fontMenu_.add(checkbox_);
            fontVector_.addElement(checkbox_);
        }
        text.add(fontMenu_);

        // Choices for font size
        Menu fontSizeMenu_ = new Menu("Font Size");
        fontSizeVector_ = new Vector();

        for (int i = 0; i < petriTool_.fontSizeList_.length; i++) {
            checkbox_ = new CheckboxMenuItem
                            (String.valueOf(petriTool_.fontSizeList_[i]));

            if (petriTool_.fontSizeList_[i] == 10) {
                checkbox_.setState(true);
            }

            fontSizeMenu_.add(checkbox_);
            fontSizeVector_.addElement(checkbox_);
        }
        text.add(fontSizeMenu_);

        regularCheckboxMenuItem_ = new CheckboxMenuItem("Regular");
        regularCheckboxMenuItem_.setState(true);
        text.add(regularCheckboxMenuItem_);

        boldCheckboxMenuItem_ = new CheckboxMenuItem("Bold");
        text.add(boldCheckboxMenuItem_);

        italicCheckboxMenuItem_ = new CheckboxMenuItem("Italic");
        text.add(italicCheckboxMenuItem_);

        menubar.add(text);

        // Create the Options menu.  Add items to it.  Add to menubar.
        options = new Menu("Options");
        options.add(new MenuItem("Preferences"));
        menubar.add(options);

        // Create the Simulation menu.  Add items to it.  Add to menubar.
        simulation = new Menu("Simulation");
        simulation.add(new MenuItem("Reset"));
        simulation.add(new MenuItem("Forward Step"));
        simulation.add(new MenuItem("Reverse Step"));
        simulation.add(new MenuItem("Run"));
        simulation.add(new MenuItem("Stop"));
        menubar.add(simulation);

        // Create the Analysis menu.  Add items to it.  Add to menubar.
        analysis = new Menu("Analysis");
        analysis.add(new MenuItem("Calculate Reachability Tree"));
        analysis.add(new MenuItem("Show Reachability Tree"));
        analysis.add(new MenuItem("Net Properties"));
        analysis.add(new MenuItem("Search Reachability Tree"));
        menubar.add(analysis);

       
        
        
        // Create the Control menu.  Add items to it.  Add to menubar.
       control = new Menu("Control");
       control.add(new MenuItem("Deadlock Control"));
        
        Menu controlmenu = new Menu("Control");
        Menu deadlockcontrol = new Menu("Deadlock Control");
        
        Menu operationcontrol = new Menu ("Operation Control");
        controlmenu.add(deadlockcontrol);
        controlmenu.add(operationcontrol);
        
        MenuItem siphoncontrol = new MenuItem("Siphon Control");

        siphoncontrol.addActionListener(new ControlMethod(petriTool_));
        MenuItem blankcontrol = new MenuItem("blank");
        blankcontrol.addActionListener(new ControlMethod(petriTool_));

        deadlockcontrol.add(siphoncontrol);
        deadlockcontrol.add(blankcontrol);
        
        MenuItem gemccontrol= new MenuItem("GMEC Control");

        gemccontrol.addActionListener(new ControlMethod(petriTool_));
        operationcontrol.add(gemccontrol);		
        
        menubar.add(controlmenu);


  
        
        
        
     // Create the Communication menu.  Add items to it.  Add to menubar.
        communication = new Menu("Communication");
        MenuItem connectToDevice=new MenuItem("Connect to device");
        connectToDevice.addActionListener(new CommunicationMethod(petriTool_));
        communication.add(connectToDevice);
        menubar.add(communication);
        
        // Create the Processing menu.  Add items to it.  Add to menubar.
        mining = new Menu("Mining");
        MenuItem alpahMining=new MenuItem("alpha mining");
        MenuItem deltaMining=new MenuItem("delta mining");
        alpahMining.addActionListener(new MiningMethod(petriTool_));
        deltaMining.addActionListener(new MiningMethod(petriTool_));
        mining.add(alpahMining);
        mining.add(deltaMining);
        menubar.add(mining);

        
        // Create Help menu; add an item; add to menubar
        help = new Menu("Help");
        help.add(new MenuItem("About"));
        help.add(new MenuItem("Select Item"));
        menubar.add(help);

         // Display the help menu in a special reserved place.
        menubar.setHelpMenu(help);
        
        return(menubar);
    }

    /**
      * Handle all events in this frame by calling super.handleEvent()
    **/
    /* 
     * This method doesn't work after you click your right button,
     * only if you close your window before your clicking right button will it works.
     * Reason: Don't know yet, but the bug has been fixed
     */
    public boolean handleEvent(Event ev) {
        if (ev.id == Event.WINDOW_DESTROY) {
            System.exit(0);
            return true;
        }
        return super.handleEvent(ev);
    }

    /**
      * Repaint the elements in the frame
    **/
    public void repaint() {
        petriTool_.controlPanel_.repaint();
        petriTool_.designPanel_.repaint();
        petriTool_.statusPanel_.repaint();
    }

    /**
      * Set either the Font style or Font Size, based on the
      * String label received in the event.
    **/
    public void setFont (String label) {
        int selectedIndex__ = -1;
        boolean foundMatch__ = false;
        for (int i = 0; i < fontList_.length; i++) {
            if (!label.equals(fontList_[i])) {
                tempCheckboxMenuItem_ = (CheckboxMenuItem) fontVector_.elementAt(i);
                if (tempCheckboxMenuItem_.getState()) {
                    selectedIndex__ = i;
                    tempCheckboxMenuItem_.setState(false);
                }
            }
            else {
                petriTool_.fontStyle_ = fontList_[i];
                foundMatch__ = true;
            }
        }
        // If the label is that of a Font Size, we have incorrectly
        // deselected the Font Style, so reactivate it.
        if ((!(selectedIndex__ == -1)) && (!foundMatch__)) {
            tempCheckboxMenuItem_ = (CheckboxMenuItem)
                                    fontVector_.elementAt(selectedIndex__);
            tempCheckboxMenuItem_.setState(true);
        }

        // If we didn't fin a match in font style, must be a size
        if (!foundMatch__) {
            for (int i = 0; i < petriTool_.fontSizeList_.length; i++) {
                if (!label.equals(String.valueOf(petriTool_.fontSizeList_[i]))) {
                    tempCheckboxMenuItem_ = (CheckboxMenuItem) fontSizeVector_.elementAt(i);
                    if (tempCheckboxMenuItem_.getState()) {
                        tempCheckboxMenuItem_.setState(false);
                    }
                }
                else {
                    petriTool_.fontSize_ = petriTool_.fontSizeList_[i];
                }
            }
        }

    }

    /**
      * Query ControlPanel to see if the help button is active
    **/
    private boolean helpWanted() {
        return (petriTool_.controlPanel_.helpWanted());
    }

    /**
      * When a new design is desired, disable the Save menu item,
      * and make saveFileName_ and saveFileDirectory_ = null.
    **/
    public void newDesign() {
        saveMenuItem_.setEnabled(false);
        saveFileName_ = null;
        saveFileDirectory_ = null;
    }

    /**
      * Handle all events in the TextDialogs.  When the user inputs
      * text for a caption, set the Caption Font, set a boolean
      * variable in PetriTool (captionInProgress_) to true,
      * and select the current button to pointer so that the
      * user can select a location for the caption, handled in
      * DesignPanel object.
    **/
    public boolean TextDialogHandler(String label) {
        if (userWantsSearch_) {
            if (petriTool_.reachabilityTree_ != null) {
                petriTool_.reachabilityTree_.displayTree(
                    label.substring("TEXTDIALOG_OK_".length()));
            }
            else {
                StatusMessage("No reachability tree currently " +
                          "constructed, try Calculate " +
                          "Reachability Tree first.");
            }
            userWantsSearch_ = false;
        }
        else {
            petriTool_.captionText_ =
                label.substring("TEXTDIALOG_OK_".length());

            petriTool_.controlPanel_.updateButtons("Pointer");
            petriTool_.controlPanel_.userWantsPointer();
            petriTool_.captionInProgress_ = true;
            if (petriTool_.fontBold_) {
                if (petriTool_.fontItalic_) {
                    petriTool_.captionFont_ = new Font
                        (petriTool_.fontStyle_, Font.BOLD+Font.ITALIC,
                        petriTool_.fontSize_);
                }
                else {
                    petriTool_.captionFont_ = new Font
                        (petriTool_.fontStyle_, Font.BOLD, petriTool_.fontSize_);
                }
            }
            else if (petriTool_.fontItalic_) {
                petriTool_.captionFont_ = new Font
                    (petriTool_.fontStyle_, Font.ITALIC,
                    petriTool_.fontSize_);
            }
            else {
                petriTool_.captionFont_ = new Font
                    (petriTool_.fontStyle_, Font.PLAIN,
                    petriTool_.fontSize_);
            }
        }
        return false;
    }

    /**
      * This routine is responsible for capturing the keyboard
      * shortcut events, and taking the appropriate action.
    **/
    public boolean keyDown(Event e, int key)  {
        int flags = e.modifiers;
        char c = (char) e.key;

        System.out.println("in keyDown(), location 1");
        System.out.println("character = " + c);

        if (e.id == Event.KEY_PRESS) {
            // Delete key is delete
            if (c == '\177') {
                boolean done__ = petriTool_.designPanel_.deleteSelected();
                if (!done__) {
                    StatusMessage("There were no components selected to delete.");
                }
                repaint();
                return (true);
            }

            // Detect Ctrl+Some_Key
            if (e.controlDown()) {
                if (c < ' ') {
                    c += '@';
                    System.out.println("New character = " + c);

                    // Ctrl-X is Cut
                    if ((c == 'x') || (c == 'X')) {
                        boolean done__ = petriTool_.designPanel_.cutSelected();

                        if (!done__) {
                            StatusMessage("There were no components selected to cut.");
                        }

                        repaint();
                    }

                    // Ctrl-C is Copy
                    if ((c == 'c') || (c == 'C')) {
                        boolean done__ = petriTool_.designPanel_.copySelected();

                        if (!done__) {
                            StatusMessage("There were no components selected to copy.");
                        }

                        repaint();
                    }

                    // Ctrl-V is Paste
                    if ((c == 'v') || (c == 'V')) {
                        petriTool_.pasteInProgress_ = true;
                    }

                    // Ctrl-Z is Zoom In
                    if ((c == 'z') || (c == 'Z')) {
                        if (petriTool_.gridStep_ / 2 >= petriTool_.MAX_GRID_SIZE) {
                            StatusMessage("Cannot zoom in any further.");
                        }
                        else {
                            StatusMessage("Zoom In");
                            petriTool_.setGridStep(petriTool_.gridStep_ * 2);
                            petriTool_.designPanel_.adjustFonts(2);
                            petriTool_.designPanel_.repaint();
                        }
                    }

                    // Ctrl-Q is Zoom Out
                    if ((c == 'q') || (c == 'Q')) {
                        if (petriTool_.gridStep_ / 2 <= petriTool_.MIN_GRID_SIZE) {
                            StatusMessage("Cannot zoom out any further.");
                        }
                        else {
                            StatusMessage("Zoom Out");
                            petriTool_.setGridStep(petriTool_.gridStep_ / 2);
                            petriTool_.designPanel_.adjustFonts(-2);
                            petriTool_.designPanel_.repaint();
                        }
                    }

                }
            }
        }

        return (false);
    }

    /**
      * Handle actions in this frame involving the selection of
      * MenuBar items, as well as TextDialog events that are
      * propagated up to this frame.
    **/
    public boolean action(Event ev, Object arg) {
        String label = (String)arg;

        if (label.startsWith("TEXTDIALOG_")) {
            return TextDialogHandler(label);
        }
        else if (ev.target instanceof CheckboxMenuItem) {
            if (label.equals("Regular")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Regular.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                boldCheckboxMenuItem_.setState(false);
                italicCheckboxMenuItem_.setState(false);
                regularCheckboxMenuItem_.setState(true);
                petriTool_.fontBold_ = false;
                petriTool_.fontItalic_ = false;
            }
            else if(label.equals("Bold")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Bold.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                if (boldCheckboxMenuItem_.getState()) {
                    regularCheckboxMenuItem_.setState(false);
                    petriTool_.fontBold_ = true;
                }
                else {
                    petriTool_.fontBold_ = false;
                    if(!italicCheckboxMenuItem_.getState()) {
                        regularCheckboxMenuItem_.setState(true);
                    }
                }
            }
            else if(label.equals("Italic")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Italic.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                if (italicCheckboxMenuItem_.getState()) {
                    regularCheckboxMenuItem_.setState(false);
                    petriTool_.fontItalic_ = true;
                }
                else {
                    petriTool_.fontItalic_ = false;
                    if(!boldCheckboxMenuItem_.getState()) {
                        regularCheckboxMenuItem_.setState(true);
                    }
                }
            }
            else {
                setFont(label);
            }
        }
        else if(ev.target instanceof MenuItem) {
            if (label.equals("Exit")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Exit.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                YesNoDialog d = new ReallyQuitDialog(this);
                d.setVisible(true);
            }
            else if (label.equals("New")) {
//                // If the help button is active, display help file
//                if (helpWanted()) {
//                    try {
//                        helpDialog_ = new FileViewer("help/New.help");
//                    }
//                    catch (IOException e) {
//                        StatusMessage("Error opening help file.");
//                    }
//                    return (true);
//                }
//
//                // If help button not active, carry out action
//                YesNoDialog d = new NewDesignDialog(this);
//                d.setVisible(true);
            	selectNew();
            }
            else if (label.equals("Open")) {
//                // If the help button is active, display help file
//                if (helpWanted()) {
//                    try {
//                        helpDialog_ = new FileViewer("help/Open.help");
//                    }
//                    catch (IOException e) {
//                        StatusMessage("Error opening help file.");
//                    }
//                    return (true);
//                }
//
//                // If help button not active, carry out action
//                // Create a file selection dialog box
//                openFileDialog_ = new FileDialog(this, "Open", FileDialog.LOAD);
//                openFileDialog_.setDirectory(".");
//                openFileDialog_.setFile("*.pnt");
//                openFileDialog_.setVisible(true);  // blocks until user selects a file
//                if (openFileDialog_.getFile() != null) {
//                    saveFileName_ = openFileDialog_.getFile();
//                    saveFileDirectory_ = openFileDialog_.getDirectory();
//                    petriTool_.newDesign();
//                    petriTool_.designPanel_.openDesign (saveFileDirectory_ +
//                            saveFileName_);
//
//                    // Enable saveMenuItem_ now that we have a name
//                    saveMenuItem_.setEnabled(true);
//                }
            	selectOpen();
            }
            else if (label.equals("Close")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Close.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // Create a file selection dialog box
                saveFileDialog_ = new FileDialog(this, "Save As", FileDialog.SAVE);

                if ((saveFileDirectory_== null) || (saveFileName_ == null)) {
                    saveFileDialog_.setDirectory(".");
                    saveFileDialog_.setFile("*.pnt");
                }
                else {
                    saveFileDialog_.setDirectory(saveFileDirectory_);
                    saveFileDialog_.setFile(saveFileName_);
                }

                saveFileDialog_.setVisible(true);  // blocks until user selects a file
                if (saveFileDialog_.getFile() != null) {
                    saveFileName_ = saveFileDialog_.getFile();
                    saveFileDirectory_ = saveFileDialog_.getDirectory();

                    // Due to annomilies within the FileDialog class
                    // which tacks on an extra *.* to getFile(), and
                    // FilenameFilter not being implemented...
                    String tempString__ = saveFileName_;
                    int index__ = tempString__.lastIndexOf('*');
                    index__ -= 3;
                    try {
                        saveFileName_ = tempString__.substring(0, index__);
                        petriTool_.designPanel_.saveDesign (saveFileDirectory_ +
                            saveFileName_);
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        StatusMessage("Error saving file, bad file name " +
                                        tempString__);
                    }
                    // Perform a New Operation
                    petriTool_.setDefaults();
	                petriTool_.designPanel_.newDesign();

	                // Clear the current file name
	                saveFileName_ = null;
	                saveFileDirectory_ = null;

	                saveMenuItem_.setEnabled(false);
                }
            }
            else if (label.equals("Save")) {
//                // If the help button is active, display help file
//                if (helpWanted()) {
//                    try {
//                        helpDialog_ = new FileViewer("help/Save.help");
//                    }
//                    catch (IOException e) {
//                        StatusMessage("Error opening help file.");
//                    }
//                    return (true);
//                }
//
//                // If help button not active, carry out action
//                petriTool_.designPanel_.saveDesign
//                        (saveFileDirectory_ + saveFileName_);
            	selectSave();
            }
            else if (label.equals("Save As")) {
//                // If the help button is active, display help file
//                if (helpWanted()) {
//                    try {
//                        helpDialog_ = new FileViewer("help/Save_As.help");
//                    }
//                    catch (IOException e) {
//                        StatusMessage("Error opening help file.");
//                    }
//                    return (true);
//                }
//
//                // If help button not active, carry out action
//                // Create a file selection dialog box
//                saveFileDialog_ = new FileDialog(this, "Save As", FileDialog.SAVE);
//                saveFileDialog_.setDirectory(".");
//                saveFileDialog_.setFile("*.pnt");
//                saveFileDialog_.setVisible(true);  // blocks until user selects a file
//                if (saveFileDialog_.getFile() != null) {
//                    saveFileName_ = saveFileDialog_.getFile();
//                    saveFileDirectory_ = saveFileDialog_.getDirectory();
//
//                    // Due to annomilies within the FileDialog class
//                    // which tacks on an extra *.* to getFile(), and
//                    // FilenameFilter not being implemented...
//                    String tempString__ = saveFileName_;
//                    int index__ = tempString__.lastIndexOf('*');
//                    index__ -= 3;
//                    try {
//                        saveFileName_ = tempString__.substring(0, index__);
//                        petriTool_.designPanel_.saveDesign (saveFileDirectory_ +
//                            saveFileName_);
//
//                        // Enable saveMenuItem_ now that we have a name
//                        saveMenuItem_.setEnabled(true);
//                    }
//                    catch (StringIndexOutOfBoundsException e) {
//                        StatusMessage("Error saving file, bad file name " +
//                                      tempString__);
//                    }
//                }
            	selectSaveAs();
            }
            else if (label.equals("Print")) {
//                // If the help button is active, display help file
//                if (helpWanted()) {
//                    try {
//                        helpDialog_ = new FileViewer("help/Print.help");
//                    }
//                    catch (IOException e) {
//                        StatusMessage("Error opening help file.");
//                    }
//                    return (true);
//                }
//                // If help button is not active, carry out action
//                StatusMessage("Print option not yet implemented.");
            	selectPrint();
            }
            else if (label.equals("Cut\tCtrl+X")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Cut.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                boolean done__ = petriTool_.designPanel_.cutSelected();

                if (!done__) {
                    StatusMessage("There were no components selected to cut.");
                }

                repaint();
            }
            else if (label.equals("Copy\tCtrl+C")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Copy.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                boolean done__ = petriTool_.designPanel_.copySelected();

                if (!done__) {
                    StatusMessage("There were no components selected to copy.");
                }

                repaint();
            }
            else if (label.equals("Paste\tCtrl+V")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Paste.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                petriTool_.pasteInProgress_ = true;
            }
            else if (label.equals("Delete\tDel")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Delete.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                boolean done__ = petriTool_.designPanel_.deleteSelected();

                if (!done__) {
                    StatusMessage("There were no components selected to delete.");
                }

                repaint();
            }
            else if (label.equals("Refresh")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Refresh.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                StatusMessage("Screen Refreshed.");
                repaint();
            }
            else if (label.equals("Fit To Window")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/FitToWindow.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                Dimension d = petriTool_.designPanel_.getDimension();
                Dimension max__ = petriTool_.designPanel_.getMaxDimension();
                Dimension min__ = petriTool_.designPanel_.getMinDimension();

                // The +2 corrects for the two grid spaces lost to the border
                int width__ = d.width / (max__.width - min__.width + 2);
                int height__ = d.height / (max__.height - min__.height + 2);
                int minSize__ = Math.min (width__, height__);
                if (minSize__ != -1) {
                    if (petriTool_.gridStep_ > minSize__) {
                        petriTool_.designPanel_.adjustFonts(
                            -1*(double)petriTool_.gridStep_ / (double)minSize__);
                    }
                    else {
                        petriTool_.designPanel_.adjustFonts(
                        (double)minSize__ / (double)petriTool_.gridStep_);
                    }
                    petriTool_.setGridStep(minSize__);
                    petriTool_.designPanel_.adjustScrollbars
                        (min__.width*petriTool_.gridStep_,
                         min__.height*petriTool_.gridStep_);
                }
                repaint();
            }
            else if (label.equals("View Entire Grid")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/ViewGrid.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                Dimension d = petriTool_.designPanel_.getDimension();

                // The +2 corrects for the two grid spaces lost to the border
                int width__ = d.width / (petriTool_.gridWidth_+ 1);
                int height__ = d.height / (petriTool_.gridHeight_+ 1);
                int newStep__ = Math.min (width__, height__);
                if (petriTool_.gridStep_ > newStep__) {
                    petriTool_.designPanel_.adjustFonts(
                       -1*(double)petriTool_.gridStep_ / (double)newStep__);
                }
                else {
                    petriTool_.designPanel_.adjustFonts(
                       (double)newStep__ / (double)petriTool_.gridStep_);
                }

                petriTool_.setGridStep(newStep__);
                petriTool_.designPanel_.adjustScrollbars(0,0);
                repaint();
            }
            else if (label.equals("Zoom Out\tCtrl-Q")) {
//                // If the help button is active, display help file
//                if (helpWanted()) {
//                    try {
//                        helpDialog_ = new FileViewer("help/ZoomOut.help");
//                    }
//                    catch (IOException e) {
//                        StatusMessage("Error opening help file.");
//                    }
//                    return (true);
//                }
//
//                // If help button not active, carry out action
//                if (petriTool_.gridStep_ / 2 <= petriTool_.MIN_GRID_SIZE) {
//                    StatusMessage("Cannot zoom out any further.");
//                }
//                else {
//                    StatusMessage("Zoom Out");
//                    petriTool_.setGridStep(petriTool_.gridStep_ / 2);
//                    petriTool_.designPanel_.adjustFonts(-2);
//                    petriTool_.designPanel_.repaint();
//                }
            	selectZoomOut();
            }
            else if (label.equals("Zoom In\tCtrl-Z")) {
//                // If the help button is active, display help file
//                if (helpWanted()) {
//                    try {
//                        helpDialog_ = new FileViewer("help/ZoomIn.help");
//                    }
//                    catch (IOException e) {
//                        StatusMessage("Error opening help file.");
//                    }
//                    return (true);
//                }
//
//                // If help button not active, carry out action
//                if (petriTool_.gridStep_ / 2 >= petriTool_.MAX_GRID_SIZE) {
//                    StatusMessage("Cannot zoom in any further.");
//                }
//                else {
//                    StatusMessage("Zoom In");
//                    petriTool_.setGridStep(petriTool_.gridStep_ * 2);
//                    petriTool_.designPanel_.adjustFonts(2);
//                    petriTool_.designPanel_.repaint();
//                }
            	System.out.println("good!!!!!!!");
            	selectZoomIn();
            }
            else if (label.equals("Place")) {
                petriTool_.controlPanel_.updateButtons("Place");
                petriTool_.controlPanel_.userWantsPlace();
            }
            else if (label.equals("Token")) {
                petriTool_.controlPanel_.updateButtons("Token");
                petriTool_.controlPanel_.userWantsToken();
            }
            else if (label.equals("Transition")) {
                petriTool_.controlPanel_.updateButtons("Transition");
                petriTool_.controlPanel_.userWantsTransition();
            }
            else if (label.equals("Arc")) {
                petriTool_.controlPanel_.updateButtons("Arc");
                petriTool_.controlPanel_.userWantsArc();
            }
            else if (label.equals("Text")) {
                petriTool_.controlPanel_.updateButtons("Text");
                petriTool_.controlPanel_.userWantsText();
            }
            else if (label.equals("Select")) {
                petriTool_.controlPanel_.updateButtons("Pointer");
                petriTool_.controlPanel_.userWantsPointer();
            }
            else if (label.equals("Preferences")) {
                // If the help button is active, display help file
                if (helpWanted()) {
                    try {
                        helpDialog_ = new FileViewer("help/Preferences.help");
                    }
                    catch (IOException e) {
                        StatusMessage("Error opening help file.");
                    }
                    return (true);
                }

                // If help button not active, carry out action
                petriTool_.optionsFrame_ =
                    new OptionsFrame(petriTool_);
            }
            else if (label.equals("Reset")) {
                petriTool_.controlPanel_.updateButtons("Reset");
                petriTool_.controlPanel_.userWantsReset();
            }
            else if (label.equals("Forward Step")) {
                petriTool_.controlPanel_.updateButtons("ForStep");
                petriTool_.controlPanel_.userWantsForStep();
            }
            else if (label.equals("Reverse Step")) {
                petriTool_.controlPanel_.updateButtons("RevStep");
                petriTool_.controlPanel_.userWantsRevStep();
            }
            else if (label.equals("Run")) {
                petriTool_.controlPanel_.updateButtons("Run");
                petriTool_.controlPanel_.userWantsRun();
            }
            else if(label.equals("Pause")) {
                petriTool_.controlPanel_.updateButtons("Pause");
                petriTool_.controlPanel_.userWantsPause();
            }
            else if (label.equals("Stop")) {
                petriTool_.controlPanel_.updateButtons("Stop");
                petriTool_.controlPanel_.userWantsStop();
            }
            else if (label.equals("Calculate Reachability Tree")) {
                petriTool_.controlPanel_.updateButtons("Calc");
                petriTool_.controlPanel_.userWantsCalc();
            }
            else if (label.equals("Show Reachability Tree")) {
                petriTool_.controlPanel_.updateButtons("Show");
                petriTool_.controlPanel_.userWantsShow();
            }
            else if (label.equals("Net Properties")) {
                petriTool_.controlPanel_.updateButtons("Prop");
                petriTool_.controlPanel_.userWantsProp();
            }
            else if (label.equals("Search Reachability Tree")) {
                petriTool_.controlPanel_.userWantsSearch();
            }
            else if (label.equals("About")) {
                InfoDialog d;
                d = new InfoDialog(this, "About EventPhoenix's Petri Net Tool",
                    "Author: BluePhoenix Group\n" +
                    "Date:   July 1, 2017\n" +
                    "\nCopyright (c) 2017\n" +
                    "Huaqiao University");
                d.setVisible(true);
               
               
            }
            else if (label.equals("Select Item")) {
                petriTool_.controlPanel_.updateButtons("Help");
                petriTool_.controlPanel_.userWantsHelp();
            }
            return true;
        }
        return false;
    }
    
    
    public boolean selectNew()
    {
        // If the help button is active, display help file
        if (helpWanted()) {
            try {
                helpDialog_ = new FileViewer("help/New.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            return (true);
        }

        // If help button not active, carry out action
        YesNoDialog d = new NewDesignDialog(this);
        d.setVisible(true);
        return false;
    }
    
    public boolean selectOpen()
    {
    	 // If the help button is active, display help file
        if (helpWanted()) {
            try {
                helpDialog_ = new FileViewer("help/Open.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            return (true);
        }

        // If help button not active, carry out action
        // Create a file selection dialog box
        openFileDialog_ = new FileDialog(this, "Open", FileDialog.LOAD);
        openFileDialog_.setDirectory(".");
        openFileDialog_.setFile("*");
        openFileDialog_.setVisible(true);  // blocks until user selects a file
        if (openFileDialog_.getFile() != null) {
            saveFileName_ = openFileDialog_.getFile();
            saveFileDirectory_ = openFileDialog_.getDirectory();
            petriTool_.newDesign();
            
            if(saveFileName_.substring(saveFileName_.length()-4).equalsIgnoreCase(".xml"))
            	petriTool_.designPanel_.loadFromXML(saveFileDirectory_+saveFileName_);
            if(saveFileName_.substring(saveFileName_.length()-4).equalsIgnoreCase(".pnt"))
            	petriTool_.designPanel_.openDesign (saveFileDirectory_ +
            			saveFileName_);

            // Enable saveMenuItem_ now that we have a name
            saveMenuItem_.setEnabled(true);
        }
        return false;
    }
    
    
    
    public boolean selectSave()
    {
        // If the help button is active, display help file
        if (helpWanted()) {
            try {
                helpDialog_ = new FileViewer("help/Save.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            return (true);
        }

        saveFileDialog_ = new FileDialog(this, "Save", FileDialog.SAVE);
        saveFileDialog_.setDirectory(".");
        saveFileDialog_.setFile("*.jpg");
        saveFileDialog_.setVisible(true);  // blocks until user selects a file
        if (saveFileDialog_.getFile() != null) {
        	
            saveFileName_ = saveFileDialog_.getFile();
            saveFileDirectory_ = saveFileDialog_.getDirectory();
                       
            // Due to annomilies within the FileDialog class
            // which tacks on an extra *.* to getFile(), and
            // FilenameFilter not being implemented...
           String tempString__ = saveFileName_;
           new ImageControl(petriTool_, petriTool_.designPanel_.placeVector_,  petriTool_.designPanel_.transitionVector_,
					 petriTool_.designPanel_.tokenVector_,  petriTool_.designPanel_.arcVector_).
           DrawImage(saveFileDirectory_+saveFileName_);
           saveMenuItem_.setEnabled(true);
        }
        return false;
    }
    
    
    
    
    public boolean selectSaveAs()
    {
        // If the help button is active, display help file
        if (helpWanted()) {
            try {
                helpDialog_ = new FileViewer("help/Save_As.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            return (true);
        }
        // If help button not active, carry out action
        // Create a file selection dialog box
        saveFileDialog_ = new FileDialog(this, "Save As", FileDialog.SAVE);
        saveFileDialog_.setDirectory(".");
        saveFileDialog_.setFile("*.xml");
        
        saveFileDialog_.setVisible(true);  // blocks until user selects a file
        if (saveFileDialog_.getFile() != null) {
        	
            saveFileName_ = saveFileDialog_.getFile();
            saveFileDirectory_ = saveFileDialog_.getDirectory();
                       
            // Due to annomilies within the FileDialog class
            // which tacks on an extra *.* to getFile(), and
            // FilenameFilter not being implemented...
           String tempString__ = saveFileName_;
           
          /* int index__ = tempString__.lastIndexOf('*');
            index__ -= 3;*/
                      
			if(saveFileName_.substring(saveFileName_.length()-4).equalsIgnoreCase(".xml"))
				petriTool_.designPanel_.saveAsXmL(saveFileDirectory_+saveFileName_);
            try {
  //              saveFileName_ = tempString__.substring(0, index__);
            	System.out.println(saveFileName_.substring(saveFileName_.length()-4));
            	if(saveFileName_.substring(saveFileName_.length()-4).equalsIgnoreCase(".pnt"))
            		petriTool_.designPanel_.saveDesign (saveFileDirectory_ +
            				saveFileName_);

                // Enable saveMenuItem_ now that we have a name
                saveMenuItem_.setEnabled(true);
            }
            catch (StringIndexOutOfBoundsException e) {
                StatusMessage("Error saving file, bad file name "  
                             + tempString__);
            }
        }
        return false;
    }
    
    
    public boolean selectPrint()
    {
        // If the help button is active, display help file
        if (helpWanted()) {
            try {
                helpDialog_ = new FileViewer("help/Print.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            return (true);
        }
        // If help button is not active, carry out action
              
//        PrinterJob job_ = PrinterJob.getPrinterJob();
//        PageFormat pageFormat = new PageFormat();
//    	Paper paper=new Paper();
//        paper.setSize(petriTool_.gridWidth_*petriTool_.gridStep_,
//        		petriTool_.gridHeight_*petriTool_.gridStep_);
//        paper.setImageableArea(0, 0, petriTool_.gridWidth_*petriTool_.gridStep_,
//        		petriTool_.gridHeight_*petriTool_.gridStep_);
//        pageFormat.setPaper(paper);
//        pageFormat.setOrientation(pageFormat.PORTRAIT);
//        Book book=new Book();
//        try {
//			book.append(new PrintControl(petriTool_, petriTool_.designPanel_.placeVector_,  petriTool_.designPanel_.transitionVector_,
//					 petriTool_.designPanel_.tokenVector_,  petriTool_.designPanel_.arcVector_),pageFormat);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//       
//        job_.setPageable(book);
//        
//        try {
//             // Show the printDialog for the user to confirm the print job. 
//             boolean  printOk = job_.printDialog();
//             
//             if(printOk)
//            {
//                 job_.print(); 
//            }
//
//         }catch (PrinterException e) {
//         e.printStackTrace();
//        }
        PrintControl canvas=new PrintControl(petriTool_, petriTool_.designPanel_.placeVector_,  petriTool_.designPanel_.transitionVector_,
					 petriTool_.designPanel_.tokenVector_,  petriTool_.designPanel_.arcVector_);
        
        PrintRequestAttributeSet attributes=new HashPrintRequestAttributeSet();
        try 
        {
			PrinterJob job=PrinterJob.getPrinterJob();
//			Book book=new Book();
			PageFormat pageFormat=new PageFormat();
			Paper paper=new Paper();
			paper.setSize(petriTool_.gridWidth_*petriTool_.gridStep_,
	        		petriTool_.gridHeight_*petriTool_.gridStep_);
	        paper.setImageableArea(0, 0, petriTool_.gridWidth_*petriTool_.gridStep_,
	        		petriTool_.gridHeight_*petriTool_.gridStep_);
			pageFormat.setPaper(paper);
//			book.append(canvas, pageFormat);
//			job.setPageable(book);
			job.setPrintable(canvas, pageFormat);
			if(job.printDialog(attributes))
				job.print(attributes);
        }
        catch(PrinterException e)
        {
        	e.printStackTrace();
        }
        return false;
    }
    
    
    public boolean selectZoomIn()
    {
        // If the help button is active, display help file
        if (helpWanted()) {
            try {
                helpDialog_ = new FileViewer("help/ZoomIn.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            return (true);
        }

        // If help button not active, carry out action
        if (PetriTool.gridStep_ / 2 >= PetriTool.MAX_GRID_SIZE) {
            StatusMessage("Cannot zoom in any further.");
        }
        else {
            StatusMessage("Zoom In");
            PetriTool.setGridStep(PetriTool.gridStep_ * 2);
            petriTool_.designPanel_.adjustFonts(2);
            petriTool_.designPanel_.repaint();
        }
        return true;
    }
    
    public boolean selectZoomOut()
    {
        // If the help button is active, display help file
        if (helpWanted()) {
            try {
                helpDialog_ = new FileViewer("help/ZoomOut.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            return (true);
        }

        // If help button not active, carry out action
        if (petriTool_.gridStep_ / 2 <= petriTool_.MIN_GRID_SIZE) {
            StatusMessage("Cannot zoom out any further.");
        }
        else {
            StatusMessage("Zoom Out");
            petriTool_.setGridStep(petriTool_.gridStep_ / 2);
            petriTool_.designPanel_.adjustFonts(-2);
            petriTool_.designPanel_.repaint();
        }
        return false;
    }
    
}



/**
 * Listen the WindowEvent to shutdown
 * **/
class Win extends WindowAdapter {
	/**
	 * The key to fix the can't close bug
	 * **/
    public void windowClosing(WindowEvent e) 
    {
        e.getWindow().setVisible(false);
        ((Window) e.getComponent()).dispose();
        System.exit(0);
    }
}