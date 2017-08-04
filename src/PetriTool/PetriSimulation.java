
//****************************************************************************
// CLASS NAME:	PetriSimulation.java
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

import java.util.Vector;
import java.util.stream.Collector;
import java.util.stream.Stream;

import PetriTool.DesignPanel.transitionDeleteListener;
import PetriTool.serialException.TooManyListeners;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.lang.InterruptedException;
import java.lang.Thread;
import java.awt.Toolkit;
import java.beans.IntrospectionException;

import static java.util.stream.Collectors.toList;
/**
  * A class representing the simulation of a Petri Net Design
  *
  * @see java.util.Vector
  * @see java.util.NoSuchElementException
  * @see java.lang.InterruptedException
  * @see java.lang.Thread
  * @see java.awt.Toolkit
  *
  * @version 1.0 July 3, 1996
  *
  * @author  Rick Brink
**/
class PetriSimulation extends Thread {

    /** Used to access the design and simulation variables **/
    protected PetriTool petriTool_;


    /** Vector representing the current marking of the Petri Net **/
    protected Vector currentMarkingVector_;

    /** Current simulation cycle number **/
    protected long cycleNumber_ = 1L;

    /** Indicates if the current design is valid, i.e. no dangling components **/
    protected boolean validDesign_ = false;

    /**
      * Tells whether the Thread should run ForwardStep, ReverseStep,
      * or Run continuously
    **/
    protected String modeString_ = "";

    /**
      * Vector containing simulation cycles.  Each simulation cycle
      * is itself a Vector of Transitions which fired for a given
      * cycle.
    **/
    protected Vector historyVector_;

    /**
      * Construct a new PetriSimulation.  Check design for dangling
      * components such as Tokens with no Place, unconnected Places
      * or Transitions, or Arcs that start or end in empty spaces.
    **/
    public PetriSimulation(PetriTool app) {
        petriTool_ = app;
        reset();
    }


    /**
      * Display a status message in the status panel
    **/
    public void StatusMessage(String msg) {
        petriTool_.statusPanel_.StatusMessage(msg);
    }

    /**When the simulation is forced to be stopped, the status of each place**/
    protected String stopStatus;

    
    
    public static List<Place> mayStopPlaces;
    
    /**
      * Sets up pointers between Petri Net Components, specifically:
      * 1) Places are given links to the Tokens they contain.
    **/
    public void setComponentLinks(Vector transitionVector,
                                   Vector tokenVector,
                                   Vector arcVector) {
        Transition tempTransition__;
        Vector tempArcVector__;
        Token tempToken__;
        Place tempPlace__;
        Arc tempArc__;
        int i__;
        int j__;
        int x__;
        int y__;
        int x1__;
        int y1__;
        int x2__;
        int y2__;

        // Loop through Transitions,
        tempArcVector__ = (Vector) arcVector.clone();
        for (i__ = 0; i__ < transitionVector.size(); i__++) {
            tempTransition__ = (Transition) transitionVector.elementAt(i__);

            // Clear simulation data from previous simulations
            tempTransition__.sourcePlaceVector_.removeAllElements();
            tempTransition__.tokensNeededVector_.removeAllElements();
            tempTransition__.destinationPlaceVector_.removeAllElements();
            tempTransition__.tokensDistributedVector_.removeAllElements();

            x__ = tempTransition__.getXCoordinate() * petriTool_.gridStep_;
            y__ = tempTransition__.getYCoordinate() * petriTool_.gridStep_;

            for (j__ = 0; j__ < tempArcVector__.size(); j__++) {
                tempArc__ = (Arc) tempArcVector__.elementAt(j__);
                x2__ = tempArc__.getLastXCoordinate() * petriTool_.gridStep_;
                y2__ = tempArc__.getLastYCoordinate() * petriTool_.gridStep_;

                if ((x__ == x2__) && (y__ == y2__)) {
                    tempPlace__ = petriTool_.designPanel_.getPlace(
                        tempArc__.getFirstXCoordinate() * petriTool_.gridStep_,
                        tempArc__.getFirstYCoordinate() * petriTool_.gridStep_);
                    tempTransition__.sourcePlaceVector_.addElement(tempPlace__);
                    tempTransition__.tokensNeededVector_.
                        addElement(new Integer(tempArc__.getTokensToEnable()));
                }

                x1__ = tempArc__.getFirstXCoordinate() * petriTool_.gridStep_;
                y1__ = tempArc__.getFirstYCoordinate() * petriTool_.gridStep_;

                if ((x__ == x1__) && (y__ == y1__)) {
                    tempPlace__ = petriTool_.designPanel_.getPlace(
                        tempArc__.getLastXCoordinate() * petriTool_.gridStep_,
                        tempArc__.getLastYCoordinate() * petriTool_.gridStep_);
                    tempTransition__.destinationPlaceVector_.addElement(tempPlace__);
                    tempTransition__.tokensDistributedVector_.
                        addElement(new Integer(tempArc__.getTokensToEnable()));
                }
            }
        }
    }

    /**
      * This routine renumbers the place and transition components
      * so that they start at 0, and continue up to the last
      * component.  This is helpful because when Cut and Paste
      * operations are performed, the elements become unordered
      * and more difficult to deal with.
    **/
    private void reNumberComponents(Vector placeVector,
                                    Vector transitionVector) {

        for (int i__ = 0; i__ < placeVector.size(); i__++) {
            Place tempPlace__ = (Place) placeVector.elementAt(i__);
            tempPlace__.setLabel(i__);
        }

        petriTool_.setPlaceLabel(placeVector.size());

        for (int i__ = 0; i__ < transitionVector.size(); i__++) {
            Transition tempTransition__ =
                (Transition) transitionVector.elementAt(i__);
            tempTransition__.setLabel(i__);
        }

        petriTool_.setTransitionLabel(transitionVector.size());
    }

    /**
      * Increments the cycle count for forward simulation
    **/
    public void incrementCycle() {
        cycleNumber_++;
    }

    /**
      * Increments the cycle count for forward simulation
    **/
    public void decrementCycle() {
        cycleNumber_--;
    }
    /**
      * Resets all the Transitions in the Petri Net
    **/
    public void resetAllTransitions(Vector transitionVector) {
        for (int i__ = 0; i__ < transitionVector.size(); i__++) {
            Transition tempTransition__ = (Transition) transitionVector.elementAt(i__);
            tempTransition__.simulationReset();
        }
    }


    /**
      * Resets all the Tokens in the Petri Net
    **/
    public void resetAllTokens(Vector placeVector,
                               Vector tokenVector) {
        tokenVector.removeAllElements();
        for (int i__ = 0; i__ < placeVector.size(); i__++) {
            Place tempPlace__ = (Place) placeVector.elementAt(i__);
            Integer tempInt__ = (Integer) currentMarkingVector_.elementAt(i__);
            if (tempInt__.intValue() == 0) {
                tempPlace__.setToken(null);
            }
            else {
                Token token__ = new Token(tempPlace__.getXCoordinate(),
                                          tempPlace__.getYCoordinate(),
                                          tempInt__.intValue());
                tempPlace__.setToken(token__);
                tokenVector.addElement(token__);
            }

        }
    }


    /**
      * Reset the simulation
    **/
    public void reset() {
        validDesign_ = petriTool_.designPanel_.validDesign();
        if (validDesign_) {
            // Set the current Marking of the Net to the initial marking
            currentMarkingVector_ = petriTool_.designPanel_.getInitialMarking();

            // Reset Tokens to match initial marking
            resetAllTokens(petriTool_.designPanel_.placeVector_,
                           petriTool_.designPanel_.tokenVector_);

            setComponentLinks(petriTool_.designPanel_.transitionVector_,
                              petriTool_.designPanel_.tokenVector_,
                              petriTool_.designPanel_.arcVector_);

            // Reset all Transitions to pre-simulation state
            resetAllTransitions(petriTool_.designPanel_.transitionVector_);

            historyVector_ = new Vector(petriTool_.historySize_);

            // Repaint the design
            petriTool_.designPanel_.repaint();

            modeString_ = new String("Stop");
            this.start();
        }
    }

    /**
      * Stop the simulation
    **/
    public void stopSimulation() {
        modeString_ = new String("Stop");
        if(SerialPortManagement.isAutoSendPressed())
        {	
        	CommunicationMethod.serialPortManagement.btn_SendAuto.setEnabled(true);
        	Vector<Place> places=petriTool_.designPanel_.placeVector_;
        	Optional<String> zeroData=places.stream().map(p->"0").reduce((a,b)->a+" "+b);
        	zeroData.ifPresent(CommunicationMethod.serialPortManagement::getDataAndSend);
        	SerialPortManagement.setAutoSendPressed(false);
        	historyVector_.removeAllElements();
        }
    }

    public void pauseSimulation() {
    	modeString_ = "Stop";
    }

    /**
      * Continuously run the simulation
    **/
    public void runSimulation() {
        modeString_ = new String("Run");
        this.resume();
    }


    /**
      * Make the simulation wait, based on the simulation speed
    **/
    public void sleepSimulation() {
        try {
            switch(petriTool_.simulationSpeed_) {
                case 1: sleep(5000); break;
                case 2: sleep(4000); break;
                case 3: sleep(3000); break;
                case 4: sleep(2000); break;
                case 5: sleep(1000); break;
                case 6: sleep(500); break;
                case 7: sleep(400); break;
                case 8: sleep(300); break;
                case 9: sleep(200); break;
                case 10: sleep(1); break;
                default: sleep(1000); break;
            }
        }
        catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }
    }


    /**
      * Sets the relative activation weight of each of the transitions
    **/
    public void setActivationWeights() {
        Transition transition__;
        Vector transitionVector__ = petriTool_.designPanel_.transitionVector_;

        // Loop through transitions and set activationWeight_
        for (int i__ = 0; i__ < transitionVector__.size(); i__++) {
            transition__ = (Transition) transitionVector__.elementAt(i__);
            transition__.setActivationWeight(cycleNumber_);
        }
    }

    /**
      * Repaint the design
    **/
    public void simulationRepaint() {
        petriTool_.designPanel_.repaint();
        // Force the repaint
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.sync();
    }

    /**
      * Proceed one step forward in simulation
    **/
    public void goForward() {
        Transition transition__;
        Place place__;
        Integer tempInt__;
        Vector placeVector__ = petriTool_.designPanel_.placeVector_;
        Vector transitionVector__ = petriTool_.designPanel_.transitionVector_;
        Vector tokenVector__ = petriTool_.designPanel_.tokenVector_;
        boolean enabled__;
        int numPlaces__;
        int tokensAvailable__;
        int tokensNeeded__;
        int i__;
        int j__;
        Vector enabledTransitionVector__ = new Vector();

        // Set the activation weights
        setActivationWeights();

        // Sort the transitions, smallest weights first
        sortTransitions(transitionVector__);

        for (i__ = 0; i__ < transitionVector__.size(); i__++) {
            // Check that all Places providing Tokens to the Transition
            // have enough Tokens to activate the Transition

            enabled__ = true;
            transition__ = (Transition) transitionVector__.elementAt(i__);
            numPlaces__ = transition__.sourcePlaceVector_.size();

            for (j__ = 0; j__ < numPlaces__; j__++) {
                place__ = (Place) transition__.sourcePlaceVector_.elementAt(j__);
                tokensAvailable__ = place__.getNumTokens();
                tempInt__ = (Integer) transition__.tokensNeededVector_.elementAt(j__);
                tokensNeeded__ = tempInt__.intValue();

                if (tokensAvailable__ < tokensNeeded__) {
                    enabled__ = false;
                }
            }

            if (enabled__) {
                // Enable the Transition
                transition__.setEnabled(true, true);

                // Save for historyVector
                enabledTransitionVector__.addElement(
                    new Integer(transition__.getLabel()));


                // Deduct Tokens from all source Places
                for (j__ = 0; j__ < numPlaces__; j__++) {
                    place__ = (Place) transition__.sourcePlaceVector_.elementAt(j__);
                    tempInt__ = (Integer) transition__.tokensNeededVector_.elementAt(j__);
                    tokensNeeded__ = tempInt__.intValue() * -1;
                    place__.addTokens(tokensNeeded__, tokenVector__,
                                      petriTool_.maxPlaceCapacity_);
                }
            }
        }

        try {
            // Save this simulation cycle to historyVector
            if (enabledTransitionVector__.size() > 0) {
                if (historyVector_.size() == petriTool_.historySize_) {
                    historyVector_.removeElementAt(petriTool_.historySize_ - 1);
                }
                historyVector_.insertElementAt(enabledTransitionVector__, 0);
            }
            else {
                StatusMessage("No Transitions are enabled, simulation " +
                              "cannot proceed.");
                modeString_ = "Stop";
                if(SerialPortManagement.isAutoSendPressed())
                {
                	historyVector_.removeAllElements();
                	Optional dataToSend= petriTool_.designPanel_.placeVector_.
                			stream().map((p)->{return ((Place) p).getNumTokens();}).map(i->(i.toString()))
                            .reduce((a,b)->(String)a+" "+(String)b);
                	dataToSend.ifPresent(CommunicationMethod.serialPortManagement::getDataAndSend);
                	stopStatus=(String) dataToSend.get();
//                	System.out.println(stopStatus);
                	Vector<Place> places=petriTool_.designPanel_.placeVector_;
                	mayStopPlaces = places.stream().
                		filter((place)->(place.getNumTokens()!=0)).collect(toList());
                }
                petriTool_.controlPanel_.enableButtons();
            }

            enabledTransitionVector__ = null;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error - " + e);
        }

        // Repaint the design
        simulationRepaint();

        // Wait for a period of time
        sleepSimulation();
        // Add Tokens to all sink Places
        for (i__ = 0; i__ < transitionVector__.size(); i__++) {
            transition__ = (Transition) transitionVector__.elementAt(i__);
            if (transition__.isEnabled()) {
                numPlaces__ = transition__.destinationPlaceVector_.size();

                for (j__ = 0; j__ < numPlaces__; j__++) {
                    place__ = (Place) transition__.destinationPlaceVector_.elementAt(j__);
                    tempInt__ = (Integer) transition__.tokensDistributedVector_.elementAt(j__);
                    int tokensToAdd__ = tempInt__.intValue();
                    place__.addTokens(tokensToAdd__, tokenVector__,
                                      petriTool_.maxPlaceCapacity_);
                }
                transition__.setEnabled(false, true);
            }
        }
        // Repaint the design
        simulationRepaint();

        // Increment the cycle count
        incrementCycle();
    }

    /**
      * Proceed one step backward in simulation
    **/
    public void goBackward() {
        Transition transition__;
        Place place__;
        Integer tempInt__;
        Vector placeVector__ = petriTool_.designPanel_.placeVector_;
        Vector transitionVector__ = petriTool_.designPanel_.transitionVector_;
        Vector tokenVector__ = petriTool_.designPanel_.tokenVector_;
        boolean enabled__;
        int numPlaces__;
        int tokensAvailable__;
        int tokensNeeded__;
        int i__;
        int j__;
        int k__;
        Vector reverseVector__ = new Vector();

        if (historyVector_.size() > 0) {
            try {
                reverseVector__ = (Vector) historyVector_.firstElement();
            }
            catch (NoSuchElementException e) {
                System.out.println("Error - " + e);
            }

            try {
                historyVector_.removeElementAt(0);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error - " + e);
            }

            // Activate the transitions whose label appears in the
            // history vector, and delete tokens from destination
            for (i__ = 0; i__ < reverseVector__.size(); i__++) {
                Integer reverseInt__ = (Integer)reverseVector__.elementAt(i__);
                for (j__ = 0; j__ < transitionVector__.size(); j__++) {
                    transition__ = (Transition) transitionVector__.elementAt(j__);
                    if (transition__.getLabel() == reverseInt__.intValue()) {
                        transition__.setEnabled(true, false);
                        numPlaces__ = transition__.destinationPlaceVector_.size();

                        for (k__ = 0; k__ < numPlaces__; k__++) {
                            place__ = (Place) transition__.destinationPlaceVector_.elementAt(k__);
                            tempInt__ = (Integer) transition__.tokensDistributedVector_.elementAt(k__);
                            int tokensToDelete__ = tempInt__.intValue() * -1;
                            place__.addTokens(tokensToDelete__, tokenVector__,
                                              petriTool_.maxPlaceCapacity_);
                        }
                    }
                }
            }

            // Repaint the design
            simulationRepaint();

            // Wait for a period of time
            sleepSimulation();

            // Place tokens as indicated by sourcePlaceVector
            for (i__ = 0; i__ < transitionVector__.size(); i__++) {
                transition__ = (Transition) transitionVector__.elementAt(i__);

                if (transition__.isEnabled()) {
                    // Disable the Transition
                    transition__.setEnabled(false, false);
                    numPlaces__ = transition__.sourcePlaceVector_.size();

                    // Add Tokens back to all source Places
                    for (j__ = 0; j__ < numPlaces__; j__++) {
                        place__ = (Place) transition__.sourcePlaceVector_.elementAt(j__);
                        tempInt__ = (Integer) transition__.tokensNeededVector_.elementAt(j__);
                        int tokensToAdd__ = tempInt__.intValue();
                        place__.addTokens(tokensToAdd__, tokenVector__,
                                          petriTool_.maxPlaceCapacity_);
                    }
                }
            }
        }
        else {
            StatusMessage("The history size is currently 0, cannot Reverse Step.");
        }
        if(historyVector_.size()==0&&SerialPortManagement.isAutoSendPressed())
        {
        	setBackwardStatus();
        	CommunicationMethod.serialPortManagement.getDataAndSend(stopStatus);
        }
        // Repaint the design
        simulationRepaint();

		// Decrement the cycle count
		decrementCycle();


    }
    
    /**Set the status back to which before the lower computer sent data**/
    private void setBackwardStatus() {
    	String[] stopStatusArray=stopStatus.split(" ");
    	int tokenNums;
    	Vector<Place> pVector=petriTool_.designPanel_.placeVector_;
    	Vector<Token> tVector=petriTool_.designPanel_.tokenVector_;
    	for(int i=0;i<stopStatusArray.length;i++)
    	{
    		tokenNums=Integer.parseInt(stopStatusArray[i]);
    		Place tempPlace=pVector.get(i);
    		if(tokenNums==0)
    		{
    			tVector.remove(tempPlace.getToken_());
    			tempPlace.setNonToken();
    		}
    		else if(tokenNums!=tempPlace.getNumTokens())
    		{
    			tempPlace.token_.setTokensRepresented(tokenNums);
    		}
    	}	
	}

    private void printIntegerVector(Vector printVector) {
        String returnString__ = new String("(");
        Integer tempInt__ = new Integer(0);

        for (int i__ = 0; i__ < printVector.size(); i__++) {
            tempInt__ = (Integer)printVector.elementAt(i__);
            returnString__ += tempInt__.intValue();
            if (i__ < printVector.size()-1) {
                returnString__ += ",";
            }
        }

        returnString__ += ")";
        System.out.println(returnString__);
    }


    public void run() {
        while(true) {
            if (validDesign_) {
                if (modeString_.equals("Run")) {
                    goForward();
                    // Pause for user to see new marking
                    sleepSimulation();
                }
                else if (modeString_.equals("ForwardStep")) {
                    goForward();
                    modeString_= new String("Stop");
                }
                else if (modeString_.equals("ReverseStep")) {
                    goBackward();
                    modeString_ = new String("Stop");
                }
                else if (modeString_.equals("Stop")) {
                    this.suspend();
                }
            }
            else {
                StatusMessage("Design is not valid.");
            }
        }
    }

    /**
      * Reverse the simulation by 1 cycle
    **/
    public void reverseStep() {
        modeString_ = new String("ReverseStep");
        this.resume();
    }

    /**
      * Forward step the simulation by 1 cycle
    **/
    public void forwardStep() {
        modeString_ = new String("ForwardStep");
        this.resume();
    }

    /**
      * Sorts a Vector of Transitions based on their activationWeight_
      * such that the Transitions are ordered from smallest to
      * largest weight.  Also, bring all of the selected Transitions
      * to the front of the Vector, also sorted by weight.  This feature
      * can be used by the user to put priority on select Transitions.
    **/
    public void sortTransitions(Vector transitionVector) {
        Transition transition__;
        Transition transition2__;
        double weight__;
        double weight2__;
        int i__;
        int j__;
        boolean done__;

        j__ = transitionVector.size();
        do {
            done__ = true;
            j__ = j__ - 1;
            for (i__ = 0; i__ < j__; i__++) {
                transition__ = (Transition) transitionVector.elementAt(i__);
                weight__ = transition__.getActivationWeight();
                transition2__ = (Transition) transitionVector.elementAt(i__+1);
                weight2__ = transition2__.getActivationWeight();
                if (weight__ > weight2__) {
                    done__ = false;
                    try {
                        transitionVector.removeElementAt(i__);
                        transitionVector.insertElementAt(transition__, i__+1);
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Error - " + e);
                    }
                }
            }
        } while (!done__);

        // Bring selected Transitions to front of Vector, and they
        // already in weight order from the previous sort.
        int index__ = 0;
        for (i__ = 0; i__ < transitionVector.size(); i__++) {
            transition__ = (Transition) transitionVector.elementAt(i__);
            if (transition__.isSelected()) {
                try {
                        transitionVector.removeElementAt(i__);
                        transitionVector.insertElementAt(transition__, index__);
                        index__++;
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Error - " + e);
                    }
            }
        }
    }
}
