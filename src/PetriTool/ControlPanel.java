
//****************************************************************************
// CLASS NAME:	ControlPanel.java
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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Vector;

import com.sun.media.jfxmedia.control.VideoDataBuffer;

import java.util.Observable;
import java.io.IOException;

/**
  * A class representing a collection of ImageButtons used as a
  * ControlPanel or toolbar
  *
  * @see ImageButton
  * @see java.awt.Panel
  * @see java.awt.Image
  * @see java.awt.Toolkit
  * @see java.awt.MediaTracker
  * @see java.awt.Graphics
  * @see java.awt.Color
  * @see java.awt.FlowLayout
  * @see java.util.Vector
  * @see java.io.IOException
  *
  * @version 1.0 July 3, 1996
  *
  * @author  Rick Brink
**/
public class ControlPanel extends Panel {
    /** The normal Image of a button - not pressed, disabled, or active **/
    protected Image normal_;

    /** The Image of a pressed button **/
    protected Image pressed_;

    /** The Image of a disabled button **/
    protected Image disabled_;

    /** The Image of an active button **/
    protected Image active_;

    /** Collection of ImageButton objects within the ControlPanel **/
    protected Vector imageButtonList_;

    /** Collection of String names of the ImageButton objects **/
    protected Vector imageButtonNames_;

    /** Number of ImageButton objects in the ControlPanel **/
    protected int numButtons_;

    /** Temporary ImageButton used for internal manipulations **/
    protected ImageButton tempButton_;

    /** Toolkit object used to load Images **/
    protected Toolkit toolkit_;

    /** MediaTracker object used to load Images **/
    protected MediaTracker tracker_;

    /**
      * Handle to the main PetriTool object, used to send messages
      * back to the StatusPanel
    **/
    protected PetriTool petriTool_;

    /** Used for viewing the contents of help files **/
    protected FileViewer helpDialog_;

    /** Used to prompt the user for Caption text **/
    protected TextDialog textDialog_;

    /** Indicates if the user selected the Help button **/
    protected boolean helpWanted_ = false;

    /** String name of the currently selected button **/
    protected String currentButton_ = new String("");
    
    
    protected static CommunicationMethod communication_;
    
    private CommunicationObservable communicationObservable_;
    private MiningObservable miningObservable_;
   /**an inner class just be used to call the button observer**/
    public class CommunicationObservable extends Observable
    {
    	private	boolean called=false;
    	
    	public boolean isCalled() {
			return called;
		}
		public void setCalled(boolean called) {
			this.called = called;
			setChanged();
		}
    }
    
    
   /**an inner class just be used to call the button observer**/
    public class MiningObservable extends Observable
    {
    	private boolean called=false;

		public boolean isCalled() {
			return called;
		}

		public void setCalled(boolean called) {
			this.called = called;
			setChanged();
		}
    	
    }
    
    
    
    

    /**
      * Construct a new ControlPanel, given a list of button
      * names to load.  The names, when appended with "_n.gif",
      * "_p.gif", "_d.gif", and "_a.gif" represent the file names
      * of the GIF images for a normal, pressed, disabled, and
      * active button
    **/
    public ControlPanel(String[] buttonList, PetriTool petriTool) {
        tracker_ = new MediaTracker(this);
        toolkit_ = Toolkit.getDefaultToolkit();
        numButtons_ = buttonList.length;
        petriTool_ = petriTool;
        imageButtonList_ = new Vector(numButtons_);
        imageButtonNames_ = new Vector(numButtons_);

        for (int i__ = 0; i__ < numButtons_; i__++) {
            normal_ = toolkit_.getImage("images/" + buttonList[i__] +
                                        "_n.gif");
            tracker_.addImage(normal_, 0);

            pressed_ = toolkit_.getImage("images/" + buttonList[i__] +
                                         "_p.gif");
            tracker_.addImage(pressed_, 1);

            disabled_ = toolkit_.getImage("images/" + buttonList[i__] +
                                          "_d.gif");
            tracker_.addImage(disabled_, 2);

            active_ = toolkit_.getImage("images/" + buttonList[i__] +
                                        "_a.gif");
            tracker_.addImage(active_, 3);

            // Wait for all variations of the button to load
            try {
                tracker_.waitForAll();
            }
            catch (InterruptedException e) {
                StatusMessage("Error loading image " +
                                    buttonList[i__]);
            }

            // Create a new ImageButton
            tempButton_ = new ImageButton(normal_, pressed_,
                                          disabled_, active_,
                                          buttonList[i__], this);

            imageButtonList_.addElement(tempButton_);
            imageButtonNames_.addElement(buttonList[i__]);
        }
   	    this.setLayout(new FlowLayout(FlowLayout.CENTER,2,6));

        for (int i__ = 0; i__ < imageButtonList_.size(); i__++) {
	        this.add((ImageButton) imageButtonList_.elementAt(i__));
	    }
        
        /**
         * Use the observable pattern
         * **/
        communication_=new CommunicationMethod(petriTool_);
        communicationObservable_=new CommunicationObservable();
        communicationObservable_.addObserver(communication_);
        
        miningObservable_=new MiningObservable();
        miningObservable_.addObserver(new MiningMethod(petriTool_));
	}

    /**
      * Paint the ControlPanel to the screen
    **/
    public void paint (Graphics g) {
	    g.setColor(Color.lightGray);
	    g.fillRect(0, 0, size().width, size().height);
	    g.setColor(Color.black);
	    g.drawLine(0, 0, size().width, 0);
	    g.drawLine(0, size().height-1, size().width-1, size().height-1);
    }

    /**
      * Display a status message in the status panel
    **/
    public void StatusMessage(String msg) {
        petriTool_.statusPanel_.StatusMessage(msg);
    }

    /**
      * Return whether or not the help button is depressed
    **/
    public boolean helpWanted() {
        return (helpWanted_);
    }

    /**
      * Update the ImageButtons so that the selected button is
      * activated, and the others are deactivated.
    **/
    public void updateButtons(String buttonName) {
        ImageButton tempButton__;
        for (int i__ = 0; i__ < numButtons_; i__++) {
            tempButton__ = (ImageButton) imageButtonList_.elementAt(i__);
            if (!tempButton__.getName().equals(buttonName)) {
                tempButton__.deactivate();
            }
            else {
                tempButton__.activate();
            }
        }
    }

    /**
      * Used during simulation to disable all buttons except Stop
    **/
    public void disableButtons() {
        ImageButton tempButton__;
        for (int i__ = 0; i__ < numButtons_; i__++) {
            tempButton__ = (ImageButton) imageButtonList_.elementAt(i__);
            if (!tempButton__.getName().equals("Stop")&&!tempButton__.getName().equals("Pause")) {
                tempButton__.disable();
            }
        }
    }

    /**
      * Used after simulation to enable all buttons
    **/
    public void enableButtons() {
        ImageButton tempButton__;
        for (int i__ = 0; i__ < numButtons_; i__++) {
            tempButton__ = (ImageButton) imageButtonList_.elementAt(i__);
            tempButton__.enable();
        }
    }

    /**
      * Return the name of the currently activated button
    **/
    public String getCurrentButton() {
        return (currentButton_);
    }

    /**
      * Pop up the button whose name is passed as a parameter.
      * Useful for buttons that do not stay activated, but rather
      * are pushed down and immediately pop back up.
    **/
    public void popUpButton(String buttonName) {
        ImageButton tempButton__;
        for (int i__ = 0; i__ < imageButtonList_.size(); i__++) {
	        tempButton__ = (ImageButton) imageButtonList_.elementAt(i__);
	        if (tempButton__.getName().equals(buttonName)) {
	            tempButton__.deactivate();
	        }
	    }
	}
//-----------------------The General button on the control panel--------------------------
    /**
     * Enable the New button in the control panel
   **/
   public void userWantsNew() {
           
           currentButton_ = "New";
           petriTool_.petriToolFrame_.selectNew();
           StatusMessage("Move the mouse to the design position and draw your new model.");
   }

   /**
    * Enable the Open button in the control panel
  **/
  public void userWantsOpen() {
          
          currentButton_ = "Open";
          petriTool_.petriToolFrame_.selectOpen();
          StatusMessage("Open a existing model.");
  }  
  
  /**
   * Enable the Save button in the control panel
 **/
 public void userWantsSave() {
         
         currentButton_ = "Save";
         petriTool_.petriToolFrame_.selectSave();
         StatusMessage("Save the current model.");
 }
 
 /**
  * Enable the Save as button in the control panel
**/
public void userWantsSaveas() {
        
        currentButton_ = "Save_as";
        petriTool_.petriToolFrame_.selectSaveAs();
        StatusMessage("Save the current model as the file format you wanted.");
        
}


/**
 * Enable the Save as button in the control panel
**/
public void userWantsPrint() {
       
       currentButton_ = "Print";
       petriTool_.petriToolFrame_.selectPrint();
       StatusMessage("Print the current model.");
       
}

/**
 * Enable the Zoom in button in the control panel
**/
public void userWantsZoomin() {
       
       currentButton_ = "Zoom_in";
       petriTool_.petriToolFrame_.selectZoomIn();
       StatusMessage("Zoom in the current model.");
       
}

/**
 * Enable the Zoom out button in the control panel
**/
public void userWantsZoomout() {
       
       currentButton_ = "Zoom_out";
       petriTool_.petriToolFrame_.selectZoomOut();
       StatusMessage("Zoom out the current model.");
       
}

//-----------------------The Design button on the control panel--------------------------    
    /**
      * Enable the Pointer button in the control panel
    **/
    public void userWantsPointer() {
            if (helpWanted_) {
                try {
                    helpDialog_ = new FileViewer("help/Pointer.help");
                }
                catch (IOException e) {
                    StatusMessage("Error opening help file.");
                }
                helpWanted_ = false;
                updateButtons("");
                return;
            }
            currentButton_ = "Pointer";
            StatusMessage("Move the cursor to a petri net " +
                          "component and press a mouse button.");
    }

    /**
      * Enable the Place button in the control panel
    **/
    public void userWantsPlace() {

            if (helpWanted_) {
                try {
                    helpDialog_ = new FileViewer("help/Place.help");
                }
                catch (IOException e) {
                    StatusMessage("Error opening help file.");
                }
                helpWanted_ = false;
                updateButtons("");
                return;
            }
            currentButton_ = "Place";
            StatusMessage("Move the mouse to the desired position, click the mouse button to draw a Place.");
    }

    /**
      * Enable the Token button in the control panel
    **/
    public void userWantsToken() {
            if (helpWanted_) {
                try {
                    helpDialog_ = new FileViewer("help/Token.help");
                }
                catch (IOException e) {
                    StatusMessage("Error opening help file.");
                }
                helpWanted_ = false;
                updateButtons("");
                return;
            }
            currentButton_ = "Token";
            StatusMessage("Move the mouse to the desired Place, and click the mouse button to add a Token.");
    }

    /**
      * Enable the Transition button in the control panel
    **/
    public void userWantsTransition() {
            if (helpWanted_) {
                try {
                    helpDialog_ = new FileViewer("help/Transition.help");
                }
                catch (IOException e) {
                    StatusMessage("Error opening help file.");
                }
                helpWanted_ = false;
                updateButtons("");
                return;
            }
            currentButton_ = "Transition";
            StatusMessage("Move the mouse to the desired position, and click the mouse button to add a Transition.");
    }

    /**
      * Enable the Arc button in the control panel
    **/
    public void userWantsArc() {
            if (helpWanted_) {
                try {
                    helpDialog_ = new FileViewer("help/Arc.help");
                }
                catch (IOException e) {
                    StatusMessage("Error opening help file.");
                }
                helpWanted_ = false;
                updateButtons("");
                return;
            }
            currentButton_ = "Arc";
            StatusMessage("Move the mouse to the desired position, and click and hold the mouse button to add an Arc.");
    }

    /**
      * Enable the Text button in the control panel
    **/
    public void userWantsText() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/Text.help");
            }
            catch (Exception e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "Text";
        StatusMessage("Please type your caption text in the dialog box.");
        textDialog_ = new TextDialog (petriTool_.petriToolFrame_, "", true);
    }

    /**
      * Enable the Reset Simulation button in the control panel
    **/
    public void userWantsReset() {
            if (helpWanted_) {
                try {
                    helpDialog_ = new FileViewer("help/Reset.help");
                }
                catch (IOException e) {
                    StatusMessage("Error opening help file.");
                }
                helpWanted_ = false;
                updateButtons("");
                return;
            }
            currentButton_ = "Reset";
            popUpButton("Reset");
            if(SerialPortManagement.isAutoSendPressed())
            	userWantsStop();
            petriTool_.initializeSimulation();
    }

    /**
      * Enable the Reverse Step Simulation button in the control panel
    **/
    public void userWantsRevStep() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/ReverseStep.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "ReverseStep";
        popUpButton("RevStep");

        if (petriTool_.simulationInitialized_) {
            petriTool_.petriSimulation_.reverseStep();
        }
        else {
            StatusMessage("There is no simulation initialized, " +
                          "select Reset, ForwardStep, or Run.");
        }
    }

    /**
      * Enable the Forward Step Simulation button in the control panel
    **/
    public void userWantsForStep() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/ForwardStep.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "ForwardStep";
        popUpButton("ForStep");

        if (petriTool_.simulationInitialized_) {
            petriTool_.petriSimulation_.forwardStep();
        }
        else {
            petriTool_.initializeSimulation();
            petriTool_.petriSimulation_.forwardStep();
        }
    }

    /**
      * Enable the Run Simulation button in the control panel
    **/
    public void userWantsRun() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/Run.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "Run";
        disableButtons();

        if (petriTool_.simulationInitialized_) {
            petriTool_.petriSimulation_.runSimulation();
        }
        else {
            petriTool_.initializeSimulation();
            petriTool_.petriSimulation_.runSimulation();
        }
    }

    
    public void userWantsPause()
    {
        currentButton_ = "Pause";
        popUpButton("Pause");
        enableButtons();
        
        if (petriTool_.simulationInitialized_) {
            petriTool_.petriSimulation_.pauseSimulation();
        }
        else if (petriTool_.analysisRunning_) {
            petriTool_.reachabilityTree_.stopAnalysis();
        }
        else {
            StatusMessage("There is no simulation or analysis currently running.");
        }
    }
    
    
    
    /**
      * Enable the Stop Simulation button in the control panel
    **/
    public void userWantsStop() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/Stop.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "Stop";
        popUpButton("Stop");
        enableButtons();

        if (petriTool_.simulationInitialized_) {
            petriTool_.petriSimulation_.stopSimulation();
        }
        else if (petriTool_.analysisRunning_) {
            petriTool_.reachabilityTree_.stopAnalysis();
        }
        else {
            StatusMessage("There is no simulation or analysis currently running.");
        }
    }

    /**
      * Enable the Calculate Reachability Tree button in the control panel
    **/
    public void userWantsCalc() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/CalcReachability.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "CalcReachability";
        popUpButton("Calc");
        disableButtons();
        StatusMessage("Calculating the reachability tree...");
        petriTool_.reachabilityTree_ = new ReachabilityTree(petriTool_);

    }

    /**
      * Enable the Show Reachability Tree button in the control panel
    **/
    public void userWantsShow() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/ShowReachability.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "ShowReachability";
        popUpButton("Show");

        if (petriTool_.reachabilityTree_ != null) {
            petriTool_.reachabilityTree_.displayTree();
        }
        else {
            StatusMessage("No reachability tree currently " +
                          "constructed, try Calculate " +
                          "Reachability Tree first.");
        }
    }

    /**
      * Enable the Show Petri Net Properties button in the control panel
    **/
    public void userWantsProp() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/Properties.help");
            }
            catch (IOException e) {
                StatusMessage("Error opening help file.");
            }
            helpWanted_ = false;
            updateButtons("");
            return;
        }
        currentButton_ = "Properties";
        popUpButton("Prop");

        if (petriTool_.reachabilityTree_ != null) {
            petriTool_.reachabilityTree_.displayProperties();
        }
        else {
            StatusMessage("No reachability tree currently " +
                          "constructed, try Calculate " +
                          "Reachability Tree first.");
        }
    }

    /**
      * Prompt user for a marking to search for in the reachability tree,
      * and perform the search.
    **/
    public void userWantsSearch() {
        if (helpWanted_) {
            try {
                helpDialog_ = new FileViewer("help/SearchReachability.help");
            }
            catch (Exception e) {
                StatusMessage("Error opening help file.");
            }

            helpWanted_ = false;
            updateButtons("");
            return;
        }
        if (petriTool_.reachabilityTree_ != null) {
            StatusMessage("Please type the marking to search for in the dialog box.");
            petriTool_.petriToolFrame_.userWantsSearch_ = true;
            textDialog_ = new TextDialog (petriTool_.petriToolFrame_, "", true);
        }
        else {
            StatusMessage("No reachability tree currently " +
                          "constructed, try Calculate " +
                          "Reachability Tree first.");
        }
        updateButtons("");

    }

    /**
      * Enable the Help button in the control panel
    **/
    public void userWantsHelp() {
            if (helpWanted_) {
                try {
                    helpDialog_ = new FileViewer("help/Help.help");
                }
                catch (IOException e) {
                    StatusMessage("Error opening help file.");
                }
                helpWanted_ = false;
                updateButtons("");
                return;
            } else {
                StatusMessage ("Choose a component, menu or button to get help about it.");
                helpWanted_ = true;
            }

            currentButton_ = "Help";
    }

  //-----------------------The Communication button on the control panel--------------------------   
   
    
    /**
     * Enable the Wifi button in the control panel
    **/
    public void userWantsConnectToDevice() {
           
           currentButton_ = "ConnectToDevice";
           communicationObservable_.setCalled(true);
           communicationObservable_.notifyObservers();
           StatusMessage("Connect to device");
           
    } 
    
        

    
    //-----------------------The Process mining button on the control panel--------------------------   
    
    /**
     * Enable the alpha mining button in the control panel
    **/
    public void userWantsalphamining() {
           
           currentButton_ = "alpha_mining";
           miningObservable_.setCalled(true);
           miningObservable_.notifyObservers("alpha mining");
           StatusMessage("We can dig a model from the log by alpha mining algorithm.");
           
    }  
    
    
    /**
     * Enable the RS232 button in the control panel
    **/
    public void userWantsdeltamining() {
           
           currentButton_ = "delta_mining";
           miningObservable_.setCalled(true);
           miningObservable_.notifyObservers("delta mining");
           StatusMessage("We can dig a model from the log by delta mining algorithm.");
           
    }  
    
    
    
    
    /**
      * Handle events on the various buttons within the ControlPanel
      * given a String name of the button receiving activity
    **/
    public void buttonAction (String buttonName) {
        updateButtons(buttonName);
        
        if (buttonName.equals("New")) {
            userWantsNew();
        }
        else if (buttonName.equals("Open")) {
            userWantsOpen();
        } 
        else if (buttonName.equals("Save")) {
            userWantsSave();
        } if (buttonName.equals("Save_as")) {
            userWantsSaveas();
        } 
        else if (buttonName.equals("Print")) {
            userWantsPrint();
        } 
        else if (buttonName.equals("Zoom_in")) {
            userWantsZoomin();
        }
        else if (buttonName.equals("Zoom_out")) {
            userWantsZoomout();
        }
        else if (buttonName.equals("Pointer")) {
            userWantsPointer();
        }
        else if (buttonName.equals("Pointer")) {
            userWantsPointer();
        }
        else if (buttonName.equals("Place")) {
            userWantsPlace();
        }
        else if (buttonName.equals("Token")) {
            userWantsToken();
        }
        else if (buttonName.equals("Transition")) {
            userWantsTransition();
        }
        else if (buttonName.equals("Arc")) {
            userWantsArc();
        }
        else if (buttonName.equals("Text")) {
            userWantsText();
        }
        else if (buttonName.equals("Reset")) {
            userWantsReset();
        }
        else if (buttonName.equals("RevStep")) {
            userWantsRevStep();
        }
        else if (buttonName.equals("ForStep")) {
            userWantsForStep();
        }
        else if (buttonName.equals("Run")) {
            userWantsRun();
        }
        else if(buttonName.equals("Pause")) {
        	userWantsPause();
        }
        else if (buttonName.equals("Stop")) {
            userWantsStop();
        }
        else if (buttonName.equals("Calc")) {
            userWantsCalc();
        }
        else if (buttonName.equals("Show")) {
            userWantsShow();
        }
        else if (buttonName.equals("Prop")) {
            userWantsProp();
        }
        else if (buttonName.equals("Help")) {
            userWantsHelp();
        }
        else if (buttonName.equals("ConnectToDevice")) {
            userWantsConnectToDevice();
        }
        else if (buttonName.equals("alpha_mining")) {
            userWantsalphamining();
        }
        else if (buttonName.equals("delta_mining")) {
            userWantsdeltamining();
        }
    }

}
