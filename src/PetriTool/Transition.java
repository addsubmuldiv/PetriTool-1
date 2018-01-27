//****************************************************************************// CLASS NAME:	Transition.java//// AUTHOR:	Rick Brink//		rick@mail.csh.rit.edu//		http://www.csh.rit.edu/~rick//// VERSION:	1.0//// HISTORY:	4/16/96		Initial Version//// COPYRIGHT INFORMATION://// This program and the Java source is in the public domain.// Permission to use, copy, modify, and distribute this software// and its documentation for NON-COMMERCIAL purposes and// without fee is hereby granted.////    Copyright 1996////    Rick Brink//    1266 Brighton-Henrietta Townline Rd.//    Rochester, NY 14623//// DISCLAIMER://// The author claims no responsibility for any damage, direct or indirect,// to any harware or software as a result of using this program.//****************************************************************************package PetriTool;import java.io.PrintStream;import java.util.StringTokenizer;import java.util.Vector;import org.dom4j.Element;import java.awt.Graphics;import java.awt.Color;import java.awt.Font;/**  * A class representing a Transition in a Petri Net diagram.  *  * @see PetriComponent  * @see java.awt.Font  * @see java.awt.Graphics  * @see java.awt.Color  * @see java.io.PrintStream  * @see java.util.StringTokenizer  * @see java.util.Vector  *  * @version 1.0 July 3, 1996  *  * @author  Rick Brink**/class Transition extends PetriComponent {    /** Unique numeric label for each Transition object **/    protected int label_;        /**     * The speed rate of this transition     */    protected int speed_;        /**     * Transition's name     */    protected String transitionName_;    /**      * Vector used in simulation to store pointers to Places which      * supply Tokens to this Transition    **/    protected Vector sourcePlaceVector_;    /**      * Vector used in simulation to store pointers to Places which      * will receive Tokens after this Transition is activated    **/    protected Vector destinationPlaceVector_;    /**      * Vector used in simulation to store the respective number      * of Tokens needed from each Place in sourcePlaceVector_      * in order to enable this Transition    **/    protected Vector tokensNeededVector_;    /**      * Vector used in simulation to store the respective number      * of Tokens distributed to each Place in destinationPlaceVector_      * after this Transition has been enabled    **/    protected Vector tokensDistributedVector_;    /**      * Used to tell if this transition is enable during a simulation      * cylce, and hence should be drawn differently in order      * to distinguish itself from Transitions that are not enabled.    **/    protected boolean enabled_ = false;    /**      * Used during simulation to indicate how many times this      * transition has been activated.    **/    protected long activationCount_ = 0L;    /**      * Used during simulation to compare relative frequency of      * activation as compared to other transitions to allow      * fairness in firing.    **/    protected double activationWeight_ = 0.0;    /**      * Vector representing how the marking changes when this      * transition is fired.  This is initialized when a      * ReachabilityTree is created.    **/    protected Vector changeMarking_;    /**      * Vector representing how many tokens are needed from      * the respective Places in order to be enabled    **/    protected Vector needMarking_;    /**      * Construct a new Transition, with coordinates of      * (x,y) which are the absolute coordinates divided by      * gridStep_.  Also note, all coordinates represent the      * upper left hand corner of a grid square.  A unique      * numeric identifier should also be passed into the constructor.    **/    public Transition (int x, int y, int label) {        xCoordinate_ = x;        yCoordinate_ = y;        label_ = label;        transitionName_="T"+label;        sourcePlaceVector_ = new Vector();        destinationPlaceVector_ = new Vector();        tokensNeededVector_ = new Vector();        tokensDistributedVector_ = new Vector();        speed_ = PetriTool.getSimulationSpeed();    }    /**      * Construct a new Transition without specifying any parameters.    **/    public Transition() {    }    /**      * Create a copy of the current Transition Object    **/    public Object clone() {        Transition t = new Transition(xCoordinate_, yCoordinate_, label_);        t.sourcePlaceVector_ = (Vector) sourcePlaceVector_.clone();        t.destinationPlaceVector_ = (Vector) destinationPlaceVector_.clone();        t.tokensNeededVector_ = (Vector) tokensNeededVector_.clone();        t.tokensDistributedVector_ = (Vector) tokensDistributedVector_.clone();        return (t);    }    /**      * Set the unique numeric label for this Transition    **/    public void setLabel(int label) {        label_ = label;    }    /**      * Returns the integer label for this Transition    **/    public int getLabel() {        return (label_);    }        private boolean theNameHasBeenEdited=false;    /**      * Draw the Transition to the screen.    **/    public void draw(Graphics g, int gridStep, Color transitionColor,                     boolean displayLabel) {        g.setColor(transitionColor);        g.drawRect(xCoordinate_ * gridStep, yCoordinate_ * gridStep +                   gridStep / 4, gridStep, gridStep / 2);        if (enabled_) {            g.setColor(Color.red);            g.fillRect(xCoordinate_ * gridStep + 1,                       yCoordinate_ * gridStep + gridStep / 4 + 1,                       gridStep - 1, gridStep / 2 - 1);        }        if (selected_) {            int x__ = xCoordinate_ * gridStep;            int y__ = yCoordinate_ * gridStep;            g.setColor(Color.green);            g.fillRect(x__ - 2, y__ - 2, SELECT_SIZE, SELECT_SIZE);            g.fillRect(x__ + gridStep - 2, y__ - 2, SELECT_SIZE, SELECT_SIZE);            g.fillRect(x__ - 2, y__ + gridStep - 2, SELECT_SIZE, SELECT_SIZE);            g.fillRect(x__ + gridStep - 2, y__ + gridStep - 2, SELECT_SIZE, SELECT_SIZE);        }//        if (displayLabel) {//            g.setColor(Color.red);//            Font labelFont__ = new Font("Dialog", Font.PLAIN, 12);//            g.setFont(labelFont__);//            g.drawString("T" + label_, xCoordinate_ * gridStep,//                         yCoordinate_ * gridStep);//        }                                if(displayLabel)        {        	g.setColor(Color.red);            Font labelFont__ = new Font("Dialog", Font.PLAIN, 12);            g.setFont(labelFont__);            g.drawString(transitionName_, xCoordinate_ * gridStep,                         yCoordinate_ * gridStep);		}            }    /**      * Used to enable/disable the Transition for simulations    **/    public void setEnabled(boolean state, boolean forward) {        enabled_ = state;        if (state == true) {            if (forward == true) {                activationCount_++;            }            else {                activationCount_--;            }        }    }    /**      * Returns whether or not the Transition is enabled    **/    public boolean isEnabled() {        return (enabled_);    }    /**      * Reset the Transition for a new simulation.    **/    public void simulationReset() {        enabled_ = false;        activationCount_ = 0L;        activationWeight_ = 0.0;    }    /**      * Used to set the activationWeight_ during each simulation      * cycle.    **/    public void setActivationWeight(long cycle) {        activationWeight_ = (double)((double) activationCount_ /                                     (double) cycle);    }    /**      * Used to retrieve the activationWeight_ during each simulation      * cycle.    **/    public double getActivationWeight() {        return(activationWeight_);    }    /**      * Initialize the change marking to have a specified      * amount of Integer values, and set them to 0    **/    public void initializeChangeMarking(int numValues) {        Integer tempInt__;        changeMarking_ = new Vector();        for (int i__ = 0; i__ < numValues; i__++) {            changeMarking_.addElement(new Integer(0));        }    }    /**      * Initialize the need marking to have a specified      * amount of Integer values, and set them to 0    **/    public void initializeNeedMarking(int numValues) {        Integer tempInt__;        needMarking_ = new Vector();        for (int i__ = 0; i__ < numValues; i__++) {            needMarking_.addElement(new Integer(0));        }    }    /**      * Updates the changeMarking_ by adding a specified amount      * to a specified location in the changeMarking    **/    public void updateChangeMarking(int index, int amount) {        Integer tempInt__;        int newValue__;        tempInt__ = (Integer) changeMarking_.elementAt(index);        newValue__ = tempInt__.intValue() + amount;        try {            changeMarking_.setElementAt(new Integer(newValue__), index);        }        catch (ArrayIndexOutOfBoundsException e) {            System.out.println("Error - " + e);        }    }    /**      * Updates the needMarking_ by adding a specified amount      * to a specified location in the needMarking    **/    public void updateNeedMarking(int index, int amount) {        Integer tempInt__;        int newValue__;        tempInt__ = (Integer) needMarking_.elementAt(index);        newValue__ = tempInt__.intValue() + amount;        try {            needMarking_.setElementAt(new Integer(newValue__), index);        }        catch (ArrayIndexOutOfBoundsException e) {            System.out.println("Error - " + e);        }    }    /**      * Save the necessary Transition information to a PrintStream    **/    public void saveToFile(PrintStream printStream) {        printStream.println("Transition: " + xCoordinate_ + "," +                            yCoordinate_ + "," + label_ + "," + transitionName_);    }            public void passToDOM(Element net)    {    	Element transition=net.addElement("transition");    	transition.addAttribute("id", transitionName_);        	    	Element graphics=transition.addElement("graphics");    	Element position=graphics.addElement("position");    	position.addAttribute("x", String.valueOf(xCoordinate_*PetriTool.gridStep_));    	position.addAttribute("y", String.valueOf(yCoordinate_*PetriTool.gridStep_));        	    	Element name=transition.addElement("name");    	name.addElement("value").addText(transitionName_);    	Element nameGraphics=name.addElement("graphics");    	nameGraphics.addElement("offset").addAttribute("x", "-5.0").addAttribute("y", "35.0");    	    	    	Element orientation=transition.addElement("orientation");    	orientation.addElement("value").addText("0");    	    	Element rate=transition.addElement("rate");    	rate.addElement("value").addText("1.0");    	    	Element timed=transition.addElement("timed");    	timed.addElement("value").addText("false");    	    	    	Element infiniteServer=transition.addElement("infiniteServer");    	infiniteServer.addElement("value").addText("false");    	    	    	Element priority=transition.addElement("priority");    	priority.addElement("value").addText("1");    }                /**      * Given a StringTokenizer, set the Transition values.  Useful      * for restoring a Transition from a file.    **/    public void loadFromFile(StringTokenizer tokenizer) {        xCoordinate_ = Integer.parseInt(tokenizer.nextToken());        yCoordinate_ = Integer.parseInt(tokenizer.nextToken());        label_ = Integer.parseInt(tokenizer.nextToken());        transitionName_=tokenizer.nextToken();        sourcePlaceVector_ = new Vector();        destinationPlaceVector_ = new Vector();        tokensNeededVector_ = new Vector();        tokensDistributedVector_ = new Vector();        return;    }            public static Transition loadFromXML(Element transition,int label,int gridStep)    {    	Transition newTransition=new Transition();    	int x=(int)Double.parseDouble(transition.element("graphics").element("position").attribute("x").getText());    	int y=(int)Double.parseDouble(transition.element("graphics").element("position").attribute("y").getText());    	newTransition.setxCoordinate_(x/gridStep);    	newTransition.setyCoordinate_(y/gridStep);    	newTransition.label_=label;    	newTransition.setTransitionName_(transition.element("name").element("value").getText());    	newTransition.sourcePlaceVector_=new Vector<>();    	newTransition.destinationPlaceVector_=new Vector<>();    	newTransition.tokensNeededVector_=new Vector<>();    	newTransition.tokensDistributedVector_=new Vector<>();    	return newTransition;    	    }	public String getTransitionName_() {		return transitionName_;	}	public void setTransitionName_(String transitionName_) {		this.transitionName_ = transitionName_;	}        public void setSpeed(int speed) {    	this.speed_ = speed;    }        public int getSpeed() {    	return this.speed_;    }    }