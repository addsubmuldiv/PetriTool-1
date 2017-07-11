//****************************************************************************// CLASS NAME:	PetriTool.java//// AUTHOR:	Rick Brink//		    rick@mail.csh.rit.edu//		    http://www.csh.rit.edu/~rick//// VERSION:	1.0//// HISTORY:	4/16/96		Initial Version//// COPYRIGHT INFORMATION://// This program and the Java source is in the public domain.// Permission to use, copy, modify, and distribute this software// and its documentation for NON-COMMERCIAL purposes and// without fee is hereby granted.////    Copyright 1996////    Rick Brink//    1266 Brighton-Henrietta Townline Rd.//    Rochester, NY 14623//// DISCLAIMER://// The author claims no responsibility for any damage, direct or indirect,// to any harware or software as a result of using this program.//****************************************************************************package PetriTool;import java.awt.Color;import java.awt.Font;import java.awt.Event;import java.awt.MenuItem;import java.io.PrintStream;import java.io.DataInputStream;import java.io.IOException;import java.util.StringTokenizer;/**  * The main program for the Petri Net Tool.  The major responsibility  * of this class is to maintain the global system variables.  *  * @see PetriToolFrame  * @see ControlPanel  * @see DesignPanel  * @see StatusPanel  * @see OptionsFrame  * @see java.awt.Color  * @see java.awt.MenuItem  * @see java.awt.Font  * @see java.awt.Event  * @see java.io.PrintStream  * @see java.io.DataInputStream  * @see java.io.IOException  * @see java.util.StringTokenizer  *  * @version 1.0 July 3, 1996  *  * @author  Rick Brink**/public class PetriTool  {    /** Frame holding all the components of the Petri Net Tool **/    protected PetriToolFrame petriToolFrame_;    /** Panel of Image Buttons for drawing, simulating, and analysis **/    protected ControlPanel controlPanel_;    /** Panel for the display of status messages **/    protected StatusPanel statusPanel_;    /** Panel for the graphical input of the Petri Net **/    protected DesignPanel designPanel_;    /**      * Panel for the selection of options relating to the      * different Petri Net components in a design    **/    protected ComponentPanel componentPanel_;    /** Frame for allowing user input of options **/    protected OptionsFrame optionsFrame_;    /** The number of pixels between grid points **/    static protected int gridStep_ = 20;    /** Background color of the drawing area **/    static protected Color backgroundColor_ = Color.white;    /** Foreground color of the drawing area **/    static protected Color foregroundColor_ = Color.black;    /** Color of the grid points **/    static protected Color gridColor_ = Color.gray;    /** Color of the border around the drawing area **/    static protected Color borderColor_ = Color.red;    /** Color of the caption text **/    static protected Color captionColor_ = Color.blue;    /** String value of the background color of the drawing area **/    static protected String backgroundColorStr_ = "white";    /** String value of the foreground color of the drawing area **/    static protected String foregroundColorStr_ = "black";    /** String value of the color of the grid points **/    static protected String gridColorStr_ = "gray";    /** String value of the color of the border around the drawing area **/    static protected String borderColorStr_ = "red";    /** String value of the color of caption text **/    static protected String captionColorStr_ = "blue";    /** Current font style to be used for Caption text **/    static protected String fontStyle_ = "Helvetica";    /** Current font size to be used for Caption Text **/    static protected int fontSize_ = 10;    /** Whether or not the current Caption text will be Bold face **/    static protected boolean fontBold_ = false;    /** Whether or not the current Caption text will be Italicized **/    static protected boolean fontItalic_ = false;    /** Whether or not the labels for Places will be displayed **/    static protected boolean placeLabels_ = true;    /** Whether or not the labels for Transitions will be displayed **/    static protected boolean transitionLabels_ = true;    /** Maximum number of Tokens allowed in a Place **/    static protected int maxPlaceCapacity_ = 16;    /** Whether or not the grid points will be shown **/    static protected boolean showGrid_ = true;    /** Number of grid squares tall the drawing area will be **/    static protected int gridHeight_ = 25;    /** Number of grid squares wide the drawing area will be **/    static protected int gridWidth_ = 25;    /** Relative speed of the simulation cycles (ranging 1 - 10) **/    static protected int simulationSpeed_ = 5;    /** The number of simulation cycles to save during a simulation **/    static protected int historySize_ = 100;    /** Whether or not the analysis time should be bounded **/    static protected boolean boundAnalysisTime_ = true;    /** If boundAnalysisTime_, then this is the bounding time **/    static protected int boundTime_ = 1;    /** Number of pixels wide the drawing area is **/    static protected int maxXPoints_ = gridWidth_ * gridStep_;    /** Number of pixels high the drawing area is **/    static protected int maxYPoints_ = gridHeight_ * gridStep_;    /** The lowest possible gridStep_ value allowed **/    static protected int MIN_GRID_SIZE = 4;    /** The highest possible gridStep_ value allowed **/    static protected int MAX_GRID_SIZE = 50;    /** Whether or not a Caption is waiting to be placed **/    static protected boolean captionInProgress_ = false;    /** Whether or not a Paste buffer is waiting to be placed **/    static protected boolean pasteInProgress_ = false;    /** Text of the current Caption waiting to be placed **/    static protected String captionText_ = new String("");    /** The Font of the Caption waiting to be placed **/    static protected Font captionFont_;    /** List of supported font sizes **/    int[] fontSizeList_ = {4, 5, 6, 7, 8, 9, 10,                           11, 12, 13, 14, 15, 20,                           25, 30, 35, 40, 45, 50};    /** The number of system variables needing initialization **/    static protected final int INIT_VARIABLES = 16;    /** Current value to be used as the label for the next Place **/    static protected int currentPlaceLabel_ = 0;    /** Current value to be used as the label for the next Transition **/    static protected int currentTransitionLabel_ = 0;    /** A Petri Simulation context **/    static protected PetriSimulation petriSimulation_;    /** A Reachability Tree context **/    static protected ReachabilityTree reachabilityTree_;    /** Whether or not the simulation has been initialized **/    static protected boolean simulationInitialized_ = false;    /** Whether or not an analysis is running **/    static protected boolean analysisRunning_ = false;    /** Construct a new PetriTool by instantiating a PetriToolFrame **/    public PetriTool (String title) {        petriToolFrame_ = new PetriToolFrame(this);    }    /** Calls the constructor for PetriTool **/    public static void main(String[] args) {        new PetriTool ("Rick's Petri Net Tool");    }    /**      * Set the current gridStep_, and update the variables that      * rely on gridStep_ (maxXPoints_ & maxYPoints_).      *      * @param gridStep The value of the new gridStep_    **/    public static void setGridStep (int gridStep) {        gridStep_ = gridStep;        maxXPoints_ = gridWidth_ * gridStep_;        maxYPoints_ = gridHeight_ * gridStep_;    }    /**      * Set the current gridWidth_ and gridHeight_, and update      * the variables that rely on these variables      * (maxXPoints_ & maxYPoints_).      *      * @param gridWidth The number of grid squares wide the drawing      *                  area should be      * @param gridHeight The number of grid squares high the drawing      *                   area should be    **/    public static void setGridSize (int gridWidth, int gridHeight) {        gridWidth_ = gridWidth;        gridHeight_ = gridHeight;        maxXPoints_ = gridWidth_ * gridStep_;        maxYPoints_ = gridHeight_ * gridStep_;    }    /**      * Return the current value for Place labels (currentPlaceLabel_),      * and then increment it.    **/    public int getPlaceLabel() {        return (currentPlaceLabel_++);    }    /**      * Set the current value for Place labels (currentPlaceLabel_).    **/    public void setPlaceLabel(int label) {        currentPlaceLabel_ = label;    }    /**      * Return the current value for Transition labels      * (currentTransitionLabel_), and then increment it.    **/    public int getTransitionLabel() {        return (currentTransitionLabel_++);    }    /**      * Set the current value for Transition labels      * (currentTransitionLabel_).    **/    public void setTransitionLabel(int label) {        currentTransitionLabel_ = label;    }    /**      * Creates a PetriSimulation object    **/    public void initializeSimulation() {        petriSimulation_ = new PetriSimulation(this);        simulationInitialized_ = true;    }    /**      * Destroys the PetriSimulation object    **/    public static void destroySimulation() {        petriSimulation_ = null;        simulationInitialized_ = false;    }    /**      * Write the system variables out to the given PrintStream      *      * @param printStream Destination for the system variables    **/    public static void saveToFile (PrintStream printStream) {        printStream.println("# PetriTool v. 1.0");        printStream.println("# Author: Rick Brink");        printStream.println("#");        printStream.println("# COLOR: background, foreground, " +                            "grid, border, caption");        printStream.println(backgroundColorStr_ + "," +                            foregroundColorStr_ + "," +                            gridColorStr_ + "," +                            borderColorStr_ + "," +                            captionColorStr_);        printStream.println("# FONT: style, size, bold, italic");        printStream.println(fontStyle_ + "," + fontSize_ + "," +                            fontBold_ + "," + fontItalic_);        printStream.println("# GRID: step, show, height, width");        printStream.println(gridStep_ + "," + showGrid_ + "," +                            gridHeight_ + "," + gridWidth_);        printStream.println("# SIMULATION: speed, historySize");        printStream.println(simulationSpeed_ + "," + historySize_);        printStream.println("# ANALYSIS: boundAnalysisTime, boundTime");        printStream.println(boundAnalysisTime_ + "," + boundTime_);        printStream.println("# MISCELLANEOUS: placeLabels, transitionLabels, placeCapacity");        printStream.println(placeLabels_ + "," + transitionLabels_ + "," + maxPlaceCapacity_);    }    /**      * Given a DataInputStream, load the values of the system      * variables.      *      * @param inputStream Source for the loading of system variables    **/    public static void loadFromFile (DataInputStream inputStream) {        Integer tempInt__ = new Integer(0);        int itemsRead__ = 0;        int spaceIndex__ = 0;        String input__ = new String("");        StringTokenizer tokenizer__;        try {            input__ = inputStream.readLine();            //-------------------------------------------------------            // Elliminate all header comments (lines starting with #)            //-------------------------------------------------------            while (input__.startsWith("#")) {                input__ = inputStream.readLine();            }            tokenizer__ = new StringTokenizer(input__, ",");            //-------------------------------------------------------            // Read in Color parameters            //-------------------------------------------------------            backgroundColorStr_ = tokenizer__.nextToken();            backgroundColor_ = stringToColor(backgroundColorStr_);            foregroundColorStr_ = tokenizer__.nextToken();            foregroundColor_ = stringToColor(foregroundColorStr_);            gridColorStr_ = tokenizer__.nextToken();            gridColor_ = stringToColor(gridColorStr_);            borderColorStr_ = tokenizer__.nextToken();            borderColor_ = stringToColor(borderColorStr_);            captionColorStr_ = tokenizer__.nextToken();            captionColor_ = stringToColor(captionColorStr_);            //-------------------------------------------------------            // Read in Font parameters            //-------------------------------------------------------            input__ = inputStream.readLine();            while (input__.startsWith("#")) {                input__ = inputStream.readLine();            }            tokenizer__ = new StringTokenizer(input__, ",");            fontStyle_ = tokenizer__.nextToken();            fontSize_ = Integer.parseInt(tokenizer__.nextToken());            if (tokenizer__.nextToken().equals("true")) {                fontBold_ = true;            }            else {                fontBold_ = false;            }            if (tokenizer__.nextToken().equals("true")) {                fontItalic_ = true;            }            else {                fontItalic_ = false;            }            //-------------------------------------------------------            // Read in Grid parameters            //-------------------------------------------------------            input__ = inputStream.readLine();            while (input__.startsWith("#")) {                input__ = inputStream.readLine();            }            tokenizer__ = new StringTokenizer(input__, ",");            gridStep_ = Integer.parseInt(tokenizer__.nextToken());            if (tokenizer__.nextToken().equals("true")) {                showGrid_ = true;            }            else {                showGrid_ = false;            }            gridHeight_ = Integer.parseInt(tokenizer__.nextToken());            gridWidth_ = Integer.parseInt(tokenizer__.nextToken());            //-------------------------------------------------------            //Read in Simulation parameters            //-------------------------------------------------------            input__ = inputStream.readLine();            while (input__.startsWith("#")) {                input__ = inputStream.readLine();            }            tokenizer__ = new StringTokenizer(input__, ",");            simulationSpeed_ = Integer.parseInt(tokenizer__.nextToken());            historySize_ = Integer.parseInt(tokenizer__.nextToken());            //-------------------------------------------------------            //Read in Analysis parameters            //-------------------------------------------------------            input__ = inputStream.readLine();            while (input__.startsWith("#")) {                input__ = inputStream.readLine();            }            tokenizer__ = new StringTokenizer(input__, ",");            if (tokenizer__.nextToken().equals("true")) {                boundAnalysisTime_ = true;            }            else {                boundAnalysisTime_ = false;            }            boundTime_ = Integer.parseInt(tokenizer__.nextToken());            //-------------------------------------------------------            //Read in Misc parameters            //-------------------------------------------------------            input__ = inputStream.readLine();            while (input__.startsWith("#")) {                input__ = inputStream.readLine();            }            tokenizer__ = new StringTokenizer(input__, ",");            if (tokenizer__.nextToken().equals("true")) {                placeLabels_ = true;            }            else {                placeLabels_ = false;            }            if (tokenizer__.nextToken().equals("true")) {                transitionLabels_ = true;            }            else {                transitionLabels_ = false;            }            maxPlaceCapacity_ = Integer.parseInt(tokenizer__.nextToken());        }        catch (IOException e) {        }    }    /**      * Set the values of the system variables, typically called      * from OptionsFrame in response to user changes to the      * options.      *      * @param foregroundColor Color of the foreground for      *                        drawing space      * @param backgroundColor Color of the background for      *                        drawing space      * @param gridColor Color of the grid dots      * @param borderColor Color of the border surrounding      *                    the drawing space      * @param captionColor Color of the caption text      * @param placeLabel Whether or not to show Place labels      * @param transitionLabel Whether ior not to show Transition labels      * @param showGrid Whether or not to show grid dots      * @param simulationSpeed Speed of simulation cycles      * @param boundAnalysisTime Whether or not to bound the      *                          amount of time used for analysis      * @param boundTime Minutes to bound analysis by    **/    public static void setSystemVariables(        String foregroundColor, String backgroundColor,        String gridColor,       String borderColor,        String captionColor,    boolean placeLabel,        boolean transitionLabel,boolean showGrid,        int simulationSpeed,    int historySize,        boolean boundAnalysisTime, int boundTime,        int maxPlaceCapacity) {        foregroundColorStr_ = foregroundColor;        backgroundColorStr_ = backgroundColor;        gridColorStr_ = gridColor;        borderColorStr_ = borderColor;        captionColorStr_ = captionColor;        backgroundColor_ = stringToColor(backgroundColorStr_);        foregroundColor_ = stringToColor(foregroundColorStr_);        gridColor_ = stringToColor(gridColorStr_);        borderColor_ = stringToColor(borderColorStr_);        captionColor_ = stringToColor(captionColorStr_);        placeLabels_ = placeLabel;        transitionLabels_ = transitionLabel;        maxPlaceCapacity_ = maxPlaceCapacity;        showGrid_ = showGrid;        simulationSpeed_ = simulationSpeed;        historySize_ = historySize;        boundAnalysisTime_ = boundAnalysisTime;        boundTime_ = boundTime;    }    /**      * Converts a String name for a Color to an actual      * Color object and returns that object.  Only the      * colors specifically named in the Color class are      * supported.      *      * @param colorString String name for a color    **/    public static Color stringToColor (String colorString) {        if (colorString.equals("black")) {            return (Color.black);        }        else if (colorString.equals("blue")) {            return (Color.blue);        }        else if (colorString.equals("cyan")) {            return (Color.cyan);        }        else if (colorString.equals("darkGray")) {            return (Color.darkGray);        }        else if (colorString.equals("gray")) {            return (Color.gray);        }        else if (colorString.equals("green")) {            return (Color.green);        }        else if (colorString.equals("lightGray")) {            return (Color.lightGray);        }        else if (colorString.equals("magenta")) {            return (Color.magenta);        }        else if (colorString.equals("orange")) {            return (Color.orange);        }        else if (colorString.equals("pink")) {            return (Color.pink);        }        else if (colorString.equals("red")) {            return (Color.red);        }        else if (colorString.equals("white")) {            return (Color.white);        }        else if (colorString.equals("yellow")) {            return (Color.yellow);        }        return (Color.pink);    }    /**      * Perform initializations associated with a new design.    **/    public static void newDesign() {        setDefaults();        currentPlaceLabel_ = 0;        currentTransitionLabel_ = 0;        destroySimulation();        reachabilityTree_ = null;    }    /**      * Initializes all system variables to their default values    **/    public static void setDefaults() {        gridStep_ = 20;        backgroundColor_ = Color.white;        foregroundColor_ = Color.black;        gridColor_ = Color.gray;        borderColor_ = Color.red;        captionColor_ = Color.blue;        backgroundColorStr_ = "white";        foregroundColorStr_ = "black";        gridColorStr_ = "gray";        borderColorStr_ = "red";        captionColorStr_ = "blue";        fontStyle_ = "Helvetica";        fontSize_ = 10;        fontBold_ = false;        fontItalic_ = false;        placeLabels_ = true;        transitionLabels_ = true;        maxPlaceCapacity_ = 16;        showGrid_ = true;        gridHeight_ = 25;        gridWidth_ = 40;        simulationSpeed_ = 5;        historySize_ = 100;        boundAnalysisTime_ = true;        boundTime_ = 1;        maxXPoints_ = gridWidth_ * gridStep_;        maxYPoints_ = gridHeight_ * gridStep_;    }    /**      * Handle any events that have propagated up to this level.      * Note that this is very few since most of the lower      * classes handle their own events.    **/    public boolean handleEvent(Event event) {        switch(event.id) {        // Most components generate ACTION_EVENT        // We test the target field to find out which component.        case Event.ACTION_EVENT:            if (event.target instanceof MenuItem) {            }            else {                //  Unknown action event            }            break;        // These are some events pertaining to the window itself.        case Event.WINDOW_DESTROY:             System.exit(0);             break;        case Event.WINDOW_ICONIFY:             break;        case Event.WINDOW_DEICONIFY:             break;        case Event.WINDOW_MOVED:             break;        // We print a message about each of these mouse and key events,        // but return false after so that they can still        // be properly handled by their correct recipient.        case Event.MOUSE_DOWN:             return false;        case Event.MOUSE_UP:              return false;        case Event.MOUSE_DRAG:             return false;        case Event.KEY_PRESS:        case Event.KEY_ACTION:             return false;        case Event.KEY_RELEASE:        case Event.KEY_ACTION_RELEASE:             return false;        // We ignore these event types.        case Event.GOT_FOCUS:        case Event.LOST_FOCUS:        case Event.MOUSE_ENTER:        case Event.MOUSE_EXIT:        case Event.MOUSE_MOVE:            return false;        // We shouldn't ever get this...        default:               break;        }        return true;    }}