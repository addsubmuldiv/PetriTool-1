

//****************************************************************************
// CLASS NAME:	DesignPanel.java
//
// AUTHOR:	Rick Brink
//		rick@mail.csh.rit.edu
//		http://www.csh.rit.edu/~rick
//
// VERSION:	1.0
//
// HISTORY:	4/16/96		Initial Version
//         10/06/96     Modified for support of multi-point arcs
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

import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.Event;
import java.awt.Dimension;
import java.awt.Scrollbar;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MenuItem;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI.ScrollListener;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import java.util.Iterator;
import java.util.StringTokenizer;

class DesignPanel extends Panel implements MouseListener,MouseMotionListener{
	/**The select rectangle's coordinates
	 * (x1,y1) and (x2,y2)
	 * **/
    private int selectRectX1_;
    private int selectRectY1_;
    private int selectRectX2_;
    private int selectRectY2_;
	
    
    
    /**
     * mouse's coordinate
     * **/
    private int mouseX_,mouseY_;
    
    

    /**
     * When you drag components more than one, you need to calculate the offset,
     * this is the way
     * **/
    private int oldPositionX_;
    private int oldPositionY_;
    private int newPositionX_;
    private int newPositionY_;
    
    /** Controls horizontal scrolling of the DesignPanel **/
    protected Scrollbar horizontalScrollbar_;

    /** Controls vertical scrolling of the DesignPanel **/
    protected Scrollbar verticalScrollbar_;

    /** Used to write the Petri Net design to a file **/
    protected FileOutputStream fileOutputStream_;

    /** Used to read a Petri Net design file **/
    protected FileInputStream fileInputStream_;

    /** Used to format the output file stream **/
    protected PrintStream printStream_;

    /** Used to format the input file stream **/
    protected DataInputStream dataInputStream_;

    /**
      * Local handle to the PetriTool object for accessing
      * global design parameters and the StatusPanel for
      * messages to the user
    **/
    protected PetriTool petriTool_;

    /** Temporary handle to an Arc when Arcs are drawn **/
    protected Arc newArc_ = null;

    /** Vector of all Places in the design **/
    protected Vector placeVector_;

    /** Vector of all Tokens in the design **/
    protected Vector tokenVector_;

    /** Vector of all Transitions in the design **/
    protected Vector transitionVector_;

    /** Vector of all Arcs in the design **/
    protected Vector arcVector_;

    /** Vector of all Captions in the design **/
    protected Vector captionVector_;

    /** Clipboard Vector of all Places selected for Cut/Copy/Paste **/
    protected Vector placeVectorBuf_;

    /** Clipboard Vector of all Tokens selected for Cut/Copy/Paste **/
    protected Vector tokenVectorBuf_;

    /** Clipboard Vector of all Transitions selected for Cut/Copy/Paste **/
    protected Vector transitionVectorBuf_;

    /** Clipboard Vector of all Arcs selected for Cut/Copy/Paste **/
    protected Vector arcVectorBuf_;

    /** Clipboard Vector of all Captions selected for Cut/Copy/Paste **/
    protected Vector captionVectorBuf_;

    /** Used by horizontal Scrollbar to determine current X position **/
    protected int gridXOffset_ = 0;

    /** Used by vertical Scrollbar to determine current Y position **/
    protected int gridYOffset_ = 0;

    /** Used for translating X coordinates for scrollbar **/
    protected int dx = 0;

    /** Used for translating Y coordinates for scrollbar **/
    protected int dy = 0;

    /** Currently selected button in the ControlPanel **/
    protected String selectedButton_;


    /** Slope of the Arc being drawn **/
    protected double arcSlope_;

    /**
      * Number of pixels to move when a Scrollbar SCROLL_LINE_UP
      * or SCROLL_LINE_DOWN event occurs
    **/
    protected final int LINE_INCREMENT = 50;

    /**
      * Number of pixels to move when a Scrollbar SCROLL_PAGE_UP
      * or SCROLL_PAGE_DOWN event occurs
    **/
    protected final int PAGE_INCREMENT = 250;

    /** Vector representing the initial marking of the Petri Net **/
    protected Vector initialMarkingVector_;

    /** Indicates the x coordinate of the dangling component **/
    protected int xDangle_;

    /** Indicates the y coordinate of the dangling component **/
    protected int yDangle_;

    /** Indicates if the current design is valid, i.e. no dangling components **/
    protected boolean validDesign_ = false;

    /**
      * Construct a new DesignPanel.  This is where the Petri Net
      * design will be drawn by the user.
    **/
    JPopupMenu pMenu,tMenu,aMenu;
    
    
    
    
    /**
     * three Inner class to listen the menuItem clicked event,
     * and pass the mouse coordinate to the edit dialog
     * **/
    
    class placeEditListener implements ActionListener
    {
		DesignPanel myself;
		
		public placeEditListener(DesignPanel designPanel) {
			// TODO Auto-generated constructor stub
			myself=designPanel;
		}
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			EditPlace editPlaceDialog=new EditPlace(petriTool_, myself);
			editPlaceDialog.setTokenEdited_(getToken(mouseX_, mouseY_));
			editPlaceDialog.setPlaceEdited_(getPlace(mouseX_, mouseY_));
			editPlaceDialog.setVisible(true);
		}
    }
    
    class placeDeleteListemer implements ActionListener
    {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Place placeToDelete=getPlace(mouseX_, mouseY_);
			placeVector_.removeElement(placeToDelete);
			tokenVector_.removeElement(getToken(mouseX_, mouseY_));
			deleteArcs();
			repaint();
		}
    }
    /**when delete a place or transition, delete the arcs link to them together**/
    private void deleteArcs()
    {
    	Iterator<Arc> deleteArcIterator=arcVector_.iterator();
		while(deleteArcIterator.hasNext())
		{
			Arc dArc=deleteArcIterator.next();
			if((dArc.getFirstXCoordinate()==(mouseX_/petriTool_.gridStep_)
					&&dArc.getFirstYCoordinate()==(mouseY_/petriTool_.gridStep_))
					||
					(dArc.getLastXCoordinate()==(mouseX_/petriTool_.gridStep_)
					&&dArc.getLastYCoordinate()==(mouseY_/petriTool_.gridStep_)))
			{
				deleteArcIterator.remove();
			}
		}
    }
    class transitionEditListener implements ActionListener
    {
    	DesignPanel myself;
    	
    	public transitionEditListener(DesignPanel designPanel) {
			// TODO Auto-generated constructor stub
    		myself=designPanel;
		}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			EditTransition editTransitionDialog=new EditTransition(petriTool_,myself);
			editTransitionDialog.setTransitionEdited_(getTransition(mouseX_, mouseY_));
			editTransitionDialog.setVisible(true);
		}
    	
    }
    
    
    
    class transitionDeleteListener implements ActionListener
    {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Transition transitionToDelete=getTransition(mouseX_, mouseY_);
			transitionVector_.removeElement(transitionToDelete);
			arcVector_.removeElement(getArc(mouseX_, mouseY_));
			deleteArcs();
			repaint();
		}
    	
    }
    
    
    
    
    class ArcEditListener implements ActionListener
    {
    	DesignPanel myself;
    	public ArcEditListener(DesignPanel designPanel) {
			// TODO Auto-generated constructor stub
    		myself=designPanel;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			EditArc editArcDialog=new EditArc(petriTool_,myself);
			editArcDialog.setArcEdited_(getArc(mouseX_, mouseY_));
			editArcDialog.setVisible(true);
		}
    	
    }
    
    class ArcDeleteListener implements ActionListener
    {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Arc arcToDelete=getArc(mouseX_, mouseY_);
			arcVector_.removeElement(arcToDelete);
			repaint();
		}
    	
    }
    
    
    
    
    
    
    
    
    @SuppressWarnings("deprecation")
	public DesignPanel(PetriTool app) {
        petriTool_ = app;
        setLayout( new BorderLayout());
        super.setLayout( new BorderLayout());
  //      myListener_=new DesignPanelListener();
    //    this.addMouseListener(myListener_);
        // Add scrollbars
        verticalScrollbar_ = new Scrollbar(Scrollbar.VERTICAL);

        // Under JDK1.02 on Win95, the set increments do not work
        verticalScrollbar_.setLineIncrement (LINE_INCREMENT);
        verticalScrollbar_.setPageIncrement (PAGE_INCREMENT);
        add( "East", verticalScrollbar_);

        horizontalScrollbar_ = new Scrollbar(Scrollbar.HORIZONTAL);

        // Under JDK1.02 on Win95, the set increments do not work
        horizontalScrollbar_.setLineIncrement (LINE_INCREMENT);
        horizontalScrollbar_.setPageIncrement (PAGE_INCREMENT);
        add( "South", horizontalScrollbar_);
        repaint();

        // Initialize vectors for design
        placeVector_ = new Vector();
        tokenVector_ = new Vector();
        transitionVector_ = new Vector();
        arcVector_ = new Vector();
        captionVector_ = new Vector();

        // Initialize buffer vectors for design cut, copy, paste
        placeVectorBuf_ = new Vector();
        tokenVectorBuf_ = new Vector();
        transitionVectorBuf_ = new Vector();
        arcVectorBuf_ = new Vector();
        captionVectorBuf_ = new Vector();

        // Initialize values for simulation
        initialMarkingVector_ = new Vector();
        
        /**
         * Deleted the old code and changed it to new version
         * The functions are the same as the original code
         * Set the DesignPanel it self as its listener
         * **/
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        /**
         * Initialize the place menu
         * **/
        pMenu=new JPopupMenu();
        
		JMenuItem editPlace=new JMenuItem("Edit place");
		
		editPlace.addActionListener(new placeEditListener(this));
		
		JMenuItem deletePlace=new JMenuItem("delete");
		
		deletePlace.addActionListener(new placeDeleteListemer());
		
		pMenu.add(editPlace);
		pMenu.add(deletePlace);
		
		
		/**
		 * Initialize the transition menu
		 * **/
		tMenu=new JPopupMenu();
		JMenuItem editTransition=new JMenuItem("Edit transition");
		editTransition.addActionListener(new transitionEditListener(this));
		
		JMenuItem deleteTransition=new JMenuItem("delete");
		
		deleteTransition.addActionListener(new transitionDeleteListener());
		
		tMenu.add(editTransition);
		tMenu.add(deleteTransition);
		
		/**
		 * Initialize the arc menu
		 * **/
		aMenu=new JPopupMenu();
		JMenuItem editArc=new JMenuItem("Edit arc");
		editArc.addActionListener(new ArcEditListener(this));
		JMenuItem deleteArc=new JMenuItem("delete");
		
		deleteArc.addActionListener(new ArcDeleteListener());
		
		aMenu.add(editArc);
		aMenu.add(deleteArc);
    }

    /**
      * Save the necessary design information to a file by calling
      * the saveToFile() routines of all the design components.
      * Comments in the design file start with a '#'.
    **/
    public void saveDesign(String filename) {
        StatusMessage("Saving to file " + filename);
        
               
        // Create an output steam
        try {
            fileOutputStream_ = new FileOutputStream(filename);
            printStream_ = new PrintStream(fileOutputStream_);

            // Print header and system setup values
            petriTool_.saveToFile(printStream_);

            // Print Places
            printStream_.println("#");
            printStream_.println("# PLACES: x, y, label");
            for (int i__ = 0; i__ < placeVector_.size(); i__++) {
                Place tempPlace__ = (Place) placeVector_.
                                        elementAt(i__);
                tempPlace__.saveToFile(printStream_);
            }

            // Print Transitions
            printStream_.println("#");
            printStream_.println("# TRANSITIONS: x, y, label");
            for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
                Transition tempTransition__ = (Transition) transitionVector_.
                                        elementAt(i__);
                tempTransition__.saveToFile(printStream_);
            }

            // Print Tokens
            printStream_.println("#");
            printStream_.println("# TOKENS: x, y, tokensRepresented");
            for (int i__ = 0; i__ < tokenVector_.size(); i__++) {
                Token tempToken__ = (Token) tokenVector_.
                                        elementAt(i__);
                tempToken__.saveToFile(printStream_);
            }

            // Print Arcs
            printStream_.println("#");
            printStream_.println("# ARCS: number Coordinates, x1, y1, ... xN, yN, " +
                                 "x1Draw, y1Draw, ... xNDraw, yNDraw, " +
                                 "region, tokensToEnable, start Component");
            for (int i__ = 0; i__ < arcVector_.size(); i__++) {
                Arc tempArc__ = (Arc) arcVector_.
                                        elementAt(i__);
                tempArc__.saveToFile(printStream_);
            }

            // Print Captions
            printStream_.println("#");
            printStream_.println("# CAPTIONS: x, y, actualSize, fontName, " +
                                 "fontStyle, fontSize, ~^~captionText");
            for (int i__ = 0; i__ < captionVector_.size(); i__++) {
                Caption tempCaption__ = (Caption) captionVector_.
                                        elementAt(i__);
                tempCaption__.saveToFile(printStream_);
            }
            printStream_.println("# END OF DESIGN FILE");
        }
        catch (IOException e) {
            StatusMessage("Error saving design.");
        }

        try {
            fileOutputStream_.close();
        }
        catch (IOException e) {
            StatusMessage("Error saving design.");
        }

    }
    
    
    
    
    public void saveAsXmL(String path)
    {
    	Document document=DocumentHelper.createDocument();
    	document.setXMLEncoding("UTF-8");
    	Element pnml = document.addElement("pnml");
    	Element net=pnml.addElement("net");
    	net.addAttribute("id", "Net-One");
    	net.addAttribute("type", "P/T net");
    	Element token=net.addElement("token");
    	token.addAttribute("id", "Default");
    	token.addAttribute("enable", "true");
    	token.addAttribute("red", "0");
    	token.addAttribute("green", "0");
    	token.addAttribute("blue", "0");
    	
    	for(Object p: placeVector_)
    	{
    		Place place=(Place)p;
    		place.passToDOM(net);
    	}
    	
    	for(Object p: transitionVector_)
    	{
    		Transition place=(Transition)p;
    		place.passToDOM(net);
    	}
  
    	for(Object p:arcVector_)
    	{
    		Arc arc=(Arc)p;
    		arc.passToDOM(net);
    	}
    	
    	
    	try
    	(	
    		FileOutputStream fos = new FileOutputStream(path);
    		OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
        )
    	{  
			OutputFormat of = new OutputFormat();  
			of.setEncoding("UTF-8");  
			of.setIndent(true);  
			of.setIndent("    ");  
			of.setNewlines(true);  
			XMLWriter writer = new XMLWriter(osw, of);  
			writer.write(document);
         } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    
    public void loadFromXML(String path)
    {
    	newDesign();
    	StatusMessage("Loading from "+path);
    	SAXReader reader=new SAXReader();
    	try {
			Document document=reader.read(new File(path));
			Element pnml=document.getRootElement();
			Element net=pnml.element("net");
			java.util.List<Element> placeList=net.elements("place");
			for(Iterator<Element> iterator=placeList.iterator();iterator.hasNext();)
			{
				Element place=iterator.next();
				Place tempPlace=Place.loadFromDOM(place, petriTool_.getPlaceLabel(), petriTool_.gridStep_);
				placeVector_.addElement(tempPlace);
				if(tempPlace.token_!=null)
					tokenVector_.addElement(tempPlace.token_);
			}
			
			java.util.List<Element> transitionList=net.elements("transition");
			for(Iterator<Element> iterator=transitionList.iterator();iterator.hasNext();)
			{
				Element transition=iterator.next();
				Transition tempTransition=Transition.loadFromXML(transition, petriTool_.getTransitionLabel(), petriTool_.gridStep_);
				transitionVector_.addElement(tempTransition);
			}
			
			java.util.List<Element> arcList=net.elements("arc");
			for(Iterator<Element> iterator=arcList.iterator();iterator.hasNext();)
			{
				Element arc=iterator.next();
				Arc tempArc=Arc.loadFromXML(arc, this);
				arcVector_.addElement(tempArc);
			}
			
			setInitialMarking();
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    
    

    /**
      * Restore a design from a file.  The variables of a particular
      * component are separated by commas, with each component starting
      * on a new line.  The components are grouped next to like
      * components, with comments separating different types of
      * components.  The design file ends with a string
      * '# END OF DESIGN FILE'
    **/
    public void openDesign(String filename) {
        newDesign();
        StatusMessage("Opening file " + filename);
        // Create an input steam
        try {
            fileInputStream_ = new FileInputStream(filename);
            dataInputStream_ = new DataInputStream(fileInputStream_);

            // Load header and system setup values
            petriTool_.loadFromFile(dataInputStream_);

            // Load Places
            // Elliminate any comments
            String input__ = dataInputStream_.readLine();
            while ((input__.startsWith("#")) &&
                   (!input__.startsWith("# END OF DESIGN FILE"))) {
                input__ = dataInputStream_.readLine();
            }
            StringTokenizer tokenizer__ = new StringTokenizer(input__, ", \t\n\r");

            while (tokenizer__.nextToken().equals("Place:")) {
                Place tempPlace__ = new Place();
                tempPlace__.loadFromFile(tokenizer__);
                placeVector_.addElement(tempPlace__);
                input__ = dataInputStream_.readLine();
                tokenizer__ = new StringTokenizer(input__, ", \t\n\r");
            }

            // Load Transitions
            // Elliminate any comments
            while ((input__.startsWith("#")) &&
                   (!input__.startsWith("# END OF DESIGN FILE"))) {
                input__ = dataInputStream_.readLine();
            }
            tokenizer__ = new StringTokenizer(input__, ", \t\n\r");

            while (tokenizer__.nextToken().equals("Transition:")) {
                Transition tempTransition__ = new Transition();
                tempTransition__.loadFromFile(tokenizer__);
                transitionVector_.addElement(tempTransition__);
                input__ = dataInputStream_.readLine();
                tokenizer__ = new StringTokenizer(input__, ", \t\n\r");
            }

            // Load Tokens
            // Elliminate any comments
            while ((input__.startsWith("#")) &&
                   (!input__.startsWith("# END OF DESIGN FILE"))) {
                input__ = dataInputStream_.readLine();
            }
            tokenizer__ = new StringTokenizer(input__, ", \t\n\r");

            while (tokenizer__.nextToken().equals("Token:")) {
                Token tempToken__ = new Token();
                tempToken__.loadFromFile(tokenizer__);
                tokenVector_.addElement(tempToken__);
                input__ = dataInputStream_.readLine();
                tokenizer__ = new StringTokenizer(input__, ", \t\n\r");
            }

            // Load Arcs
            // Elliminate any comments
            while ((input__.startsWith("#")) &&
                   (!input__.startsWith("# END OF DESIGN FILE"))) {
                input__ = dataInputStream_.readLine();
            }
            tokenizer__ = new StringTokenizer(input__, ", \t\n\r");

            while (tokenizer__.nextToken().equals("Arc:")) {
                Arc tempArc__ = new Arc();
                tempArc__.loadFromFile(tokenizer__);
                arcVector_.addElement(tempArc__);
                input__ = dataInputStream_.readLine();
                tokenizer__ = new StringTokenizer(input__, ", \t\n\r");
            }

            // Load Captions
            // Elliminate any comments
            while ((input__.startsWith("#")) &&
                   (!input__.startsWith("# END OF DESIGN FILE"))) {
                input__ = dataInputStream_.readLine();
            }
            tokenizer__ = new StringTokenizer(input__, ", \t\n\r");

            while (!tokenizer__.nextToken().startsWith("#")) {
                Caption tempCaption__ = new Caption();
                tempCaption__.loadFromFile(tokenizer__, input__);
                captionVector_.addElement(tempCaption__);
                input__ = dataInputStream_.readLine();
                tokenizer__ = new StringTokenizer(input__, ", \t\n\r");
            }
        }
        catch (IOException e) {
            StatusMessage("Error loading design.");
        }

        try {
            fileInputStream_.close();
        }
        catch (IOException e) {
            StatusMessage("Error loading design.");
        }

        // Re-calculate the initial marking
        setInitialMarking();

        // Set Place and Transition labels to
        // maximum current label plus one
        int maxLabel__ = 0;
        for (int i__ = 0; i__ < placeVector_.size(); i__++) {
            Place place__ = (Place) placeVector_.elementAt(i__);
            if (place__.getLabel() > maxLabel__) {
                maxLabel__ = place__.getLabel();
            }
        }
        petriTool_.setPlaceLabel(maxLabel__+1);

        maxLabel__ = 0;
        for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
            Transition transition__ = (Transition) transitionVector_.elementAt(i__);
            if (transition__.getLabel() > maxLabel__) {
                maxLabel__ = transition__.getLabel();
            }
        }
        petriTool_.setTransitionLabel(maxLabel__+1);

        repaint();
    }

    /**
      * Returns the Vector representing the initial marking
    **/
    public Vector getInitialMarking() {
        return ((Vector)initialMarkingVector_.clone());
    }

    /**
      * Search through the Vector of Places and set the
      * initialConditionVector to the initial marking.
    **/
    public void setInitialMarking() {
        initialMarkingVector_ = new Vector();
        int gridStep__ = petriTool_.gridStep_;

        for (int i__ = 0; i__ < placeVector_.size(); i__++) {
            Place tempPlace__ = (Place) placeVector_.elementAt(i__);
            int x__ = tempPlace__.getXCoordinate() * gridStep__;
            int y__ = tempPlace__.getYCoordinate() * gridStep__;
            if (tokenOccupies(x__, y__)) {
                Token token__ = getToken(x__, y__);
                tempPlace__.setToken(token__);
            }
            else {
                tempPlace__.setToken(null);
            }
            if (!tempPlace__.tokenInitialized_) {
                initialMarkingVector_.addElement(new Integer(0));
            }
            else {
                initialMarkingVector_.addElement(new Integer(
                    tempPlace__.token_.getTokensRepresented()));
            }
        }
    }


    /**
      * Returns the dimension of the design panel.
    **/
    public Dimension getDimension() {
        return(size());
    }

    /**
      * Returns the maximum X and Y coordinate of any component
      * currently on the panel.  Note that the two coordinates
      * are not necessarily from the same component.
    **/
    public Dimension getMaxDimension() {
        Dimension maxDim__ = new Dimension(0,0);
        for (int i__ = 0; i__ < placeVector_.size(); i__++) {
            Place tempPlace__ = (Place) placeVector_.
                                        elementAt(i__);
            maxDim__.width = Math.max (maxDim__.width,
                                       tempPlace__.getXCoordinate());
            maxDim__.height = Math.max (maxDim__.height,
                                        tempPlace__.getYCoordinate());

        }
        for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
            Transition tempTransition__ = (Transition) transitionVector_.
                                        elementAt(i__);
            maxDim__.width = Math.max (maxDim__.width,
                                       tempTransition__.getXCoordinate());
            maxDim__.height = Math.max (maxDim__.height,
                                        tempTransition__.getYCoordinate());

        }
        return (maxDim__);
    }

    /**
      * Returns the minimum X and Y coordinate of any component
      * currently on the panel.  Note that the two coordinates
      * are not necessarily from the same component.
    **/
    public Dimension getMinDimension() {
        Dimension minDim__ = new Dimension(size().width, size().height);
        for (int i__ = 0; i__ < placeVector_.size(); i__++) {
            Place tempPlace__ = (Place) placeVector_.
                                        elementAt(i__);
            minDim__.width = Math.min (minDim__.width,
                                       tempPlace__.getXCoordinate());
            minDim__.height = Math.min (minDim__.height,
                                        tempPlace__.getYCoordinate());

        }
        for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
            Transition tempTransition__ = (Transition) transitionVector_.
                                        elementAt(i__);
            minDim__.width = Math.min (minDim__.width,
                                       tempTransition__.getXCoordinate());
            minDim__.height = Math.min (minDim__.height,
                                        tempTransition__.getYCoordinate());

        }
        return (minDim__);
    }

    /**
      * Handle Scrollbar events for this Panel, all other events
      * propagate up with super.handleEvent
    **/
    public boolean handleEvent(Event e) {
        if (e.id == Event.WINDOW_MOVED) {
            Dimension d = size();
            int step__ = petriTool_.gridStep_;
            int width__ = petriTool_.gridWidth_;
            int height__ = petriTool_.gridHeight_;

            horizontalScrollbar_.setValues(horizontalScrollbar_.getValue(), width__, 0,width__);
            verticalScrollbar_.setValues(verticalScrollbar_.getValue(), height__, 0,height__);
        }
        else if (e.id == Event.SCROLL_ABSOLUTE ||
                 e.id == Event.SCROLL_LINE_DOWN ||
                 e.id == Event.SCROLL_LINE_UP ||
                 e.id == Event.SCROLL_PAGE_DOWN ||
                 e.id == Event.SCROLL_PAGE_UP) {
            translate(horizontalScrollbar_.getValue(), verticalScrollbar_.getValue());
            return true;
        }

        return super.handleEvent(e);
   }

    /**
      * Assigns values to the two translation variables dx and dy.
    **/
   public void translate (int x, int y) {
       dx = x;
       dy = y;
       repaint();
   }


    /**
      * Display a status message in the status panel
    **/
    public void StatusMessage(String msg) {
        petriTool_.statusPanel_.StatusMessage(msg);
    }

    /**
      * Set new values of the Scrollbars according to the visible area
    **/
    public void adjustScrollbars() {

        String OSName;
        gridXOffset_ = horizontalScrollbar_.getValue();
        gridYOffset_ = verticalScrollbar_.getValue();
        int gridStep__ = petriTool_.gridStep_;

        int VisibleXGridPoints = (size().width / gridStep__) - 1;
        int VisibleYGridPoints = (size().height / gridStep__) - 1;

        // Determine OS because of scrollbar implementation differences
        // between Win95/NT and other operating systems.
        OSName = System.getProperty("os.name");
        if (OSName.equals ("Windows 95") ||
            OSName.equals ("Windows NT")) {
            verticalScrollbar_.setValues(gridYOffset_, VisibleYGridPoints, 0, petriTool_.maxYPoints_ + 1);
            horizontalScrollbar_.setValues(gridXOffset_, VisibleXGridPoints, 0, petriTool_.maxXPoints_ + 1);
        } else {
            verticalScrollbar_.setValues(gridYOffset_, VisibleYGridPoints, 0, petriTool_.maxYPoints_ + 1 - VisibleYGridPoints);
            horizontalScrollbar_.setValues(gridXOffset_, VisibleXGridPoints, 0, petriTool_.maxXPoints_ + 1 - VisibleXGridPoints);
        }

    }

    /**
      * Set new values of the Scrollbars for the Fit to Window command
    **/
    public void adjustScrollbars(int minX, int minY) {

        String OSName;
        int gridStep__ = petriTool_.gridStep_;

        int VisibleXGridPoints = (size().width / gridStep__) - 1;
        int VisibleYGridPoints = (size().height / gridStep__) - 1;

        // Determine OS because of scrollbar implementation differences
        // between Win95/NT and other operating systems.
        OSName = System.getProperty("os.name");
        if (OSName.equals ("Windows 95") ||
            OSName.equals ("Windows NT")) {
            verticalScrollbar_.setValues(minY, VisibleYGridPoints, 0, petriTool_.maxYPoints_ + 1);
            horizontalScrollbar_.setValues(minX, VisibleXGridPoints, 0, petriTool_.maxXPoints_ + 1);
        } else {
            verticalScrollbar_.setValues(minY, VisibleYGridPoints, 0, petriTool_.maxYPoints_ + 1 - VisibleYGridPoints);
            horizontalScrollbar_.setValues(minX, VisibleXGridPoints, 0, petriTool_.maxXPoints_ + 1 - VisibleXGridPoints);
        }
        translate(horizontalScrollbar_.getValue(), verticalScrollbar_.getValue());

    }
    
    /**
      * Paint the DesignPanel and all of its components to the screen
    **/
    public void paint(Graphics g) {
    	
    	float[] g2dDashWidth= {(float) 5.0,(float) 5.0};
    	
    	Graphics2D g2d = (Graphics2D)getGraphics();
    	BasicStroke dashed = new BasicStroke(1.0f, 
                BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_MITER, 
                20.0f,g2dDashWidth,
                0.0f);
    
    	g2d.setStroke(dashed);
    	//g2d's coordinate translate, if don't do this, the select rectangle will have some trouble
    	g2d.translate(-dx, -dy);
    	g.translate(-dx, -dy);
       	adjustScrollbars();
        int step__ = petriTool_.gridStep_;
       	int width__ = petriTool_.gridWidth_;
        int height__ = petriTool_.gridHeight_;
       	Color foregroundColor__ = petriTool_.foregroundColor_;
       	g.setColor (petriTool_.backgroundColor_);
       	g.fillRect (step__ / 2, step__ / 2,
       	            width__ * step__, height__ * step__);
	
       	if (petriTool_.showGrid_) {
       	    g.setColor (petriTool_.gridColor_);
	
       	    // Draw the background grid
       	   for (int x = 1; x <= width__; x++) {
       	        for (int y = 1; y <= height__; y++) {
       	            g.drawLine (x * step__, y * step__,
       	                        x * step__, y * step__);
       	        }
       	    }
       	}
	
       	// Draw the border
       	g.setColor (petriTool_.borderColor_);
       	g.drawRect (step__ , step__ / 2, step__ * width__,
       	            step__ * height__);
       	if(!(placeDraged_!=null||transitionDraged_!=null||tokenDraged_!=null)&&resizing!=true)
       	{
       		g2d.setColor(foregroundColor__);
       		if(selectRectX2_>selectRectX1_&&selectRectY2_>selectRectY1_)
       			g2d.drawRect(selectRectX1_, selectRectY1_, (selectRectX2_-selectRectX1_), (selectRectY2_-selectRectY1_));
       		else if(selectRectX2_<selectRectX1_&&selectRectY2_<selectRectY1_)
       			g2d.drawRect(selectRectX2_, selectRectY2_, selectRectX1_-selectRectX2_, selectRectY1_-selectRectY2_);
       		else if(selectRectX2_>selectRectX1_&&selectRectY2_<selectRectY1_)
       			g2d.drawRect(selectRectX1_, selectRectY2_, selectRectX2_-selectRectX1_, selectRectY1_-selectRectY2_);
       		else if(selectRectX2_<selectRectX1_&&selectRectY2_>selectRectY1_)
       			g2d.drawRect(selectRectX2_, selectRectY1_, selectRectX1_-selectRectX2_, selectRectY2_-selectRectY1_);
       	}
       	
       	
        if(mouseDraging_==false)
        {
        	// Draw the Places
        	for (int i__ = 0; i__ < placeVector_.size(); i__++) {
        	    Place tempPlace__ = (Place) placeVector_.elementAt(i__);
        	    	tempPlace__.draw(g, step__, foregroundColor__,
							 petriTool_.placeLabels_);
        	}
	
        	// Draw the Transitions
        	for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
        	    Transition tempTransition__ = (Transition) transitionVector_.
        	                                elementAt(i__);
        	    tempTransition__.draw(g, step__, foregroundColor__,
        	                          petriTool_.transitionLabels_);
        	}
	
        	// Draw the Tokens
        	for (int i__ = 0; i__ < tokenVector_.size(); i__++) {
        	    Token tempToken__ = (Token) tokenVector_.elementAt(i__);
        	    tempToken__.draw(g, step__, foregroundColor__);
        	}
	
        	// Draw the Arcs
        	for (int i__ = 0; i__ < arcVector_.size(); i__++) {
        	    Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
        	    tempArc__.draw(g, step__, foregroundColor__);
        	}
	
        	// Draw the Captions
        	for (int i__ = 0; i__ < captionVector_.size(); i__++) {
        	    Caption tempCaption__ = (Caption) captionVector_.elementAt(i__);
        	    tempCaption__.draw(g, step__, petriTool_.captionColor_);
        	}
       	}
        else
        {
        	if(placeDraged_!=null)
        		placeDraged_.draw(g, step__, foregroundColor__,petriTool_.placeLabels_);
       // 	g.setColor(foregroundColor__);
       //     g.drawOval(placeDraged_.xCoordinate_ * step__, placeDraged_.yCoordinate_ * step__,
        //               step__,step__);
        	if(tokenDraged_!=null)
        		tokenDraged_.draw(g, step__, foregroundColor__);
        	if(transitionDraged_!=null)
        		transitionDraged_.draw(g, step__, foregroundColor__, petriTool_.transitionLabels_);
        	/*if(arcIn_!=null)
        		arcIn_.draw(g, step__, foregroundColor__);
        	if(arcOut_!=null)
        		arcOut_.draw(g, step__, foregroundColor__);*/
        	if(!arcsInVector_.isEmpty())
        	{
        		Iterator<Arc> arcsInItor=arcsInVector_.iterator();
        		while(arcsInItor.hasNext())
        		{
        			arcsInItor.next().draw(g, step__, foregroundColor__);
        		}
        	}
        	if(!arcsOutVector_.isEmpty())
        	{
        		Iterator<Arc> arcsOutItor=arcsOutVector_.iterator();
        		while(arcsOutItor.hasNext())
        		{
        			arcsOutItor.next().draw(g, step__, foregroundColor__);
        		}
        	}
        }
	}
    

    /**
      * User selected "New", clear out all design panel elements
    **/
    public void newDesign() {
        placeVector_.removeAllElements();
        tokenVector_.removeAllElements();
        transitionVector_.removeAllElements();
        arcVector_.removeAllElements();
        captionVector_.removeAllElements();
        setInitialMarking();
        adjustScrollbars(0,0);
        StatusMessage("New design initialized.");
        repaint();
    }


    /**
      * Determine if the coordinate (x,y) is within the border.
    **/
    public boolean validDimension (int x, int y) {
        int step__ = petriTool_.gridStep_;
        int width__ = petriTool_.gridWidth_;
        int height__ = petriTool_.gridHeight_;
        if ((x >= step__) && (x <= width__*step__ - step__)) {
            if ((y >= step__) && (y <= height__*step__ - step__)) {
                return (true);
            }
        }
        return (false);
    }

    /**
      * Determine if grid square with upper left corner
      * coordinates (x,y) is already occupied.
    **/
    public boolean gridSpaceOccupied(int x, int y) {
        if (placeOccupies (x, y, false) || transitionOccupies (x, y, false)) {
            return (true);
        }
        else {
            return (false);
        }
    }

    /**
      * Check to see if two sets of coordinates (x1, y1) and
      * (x2, y2) are within one grid square of each other,
      * and return true if they are.  If the boolean argument
      * to this function is true, only check to see if the
      * two sets of coordinates are equal.
    **/
    public boolean checkBorderingGridSquares (int x1, int y1,
                                              int x2, int y2,
                                              boolean exact) {
        int x__ = x1;
        int y__ = y1;
        int x = x2;
        int y = y2;

    	// Check the space that was chosen
        if (x__ == x) {
            if (y__ == y) {
                return (true);
            }
        }
        // If the caller only wanted to check the selected grid space
        if (exact) {
            return (false);
        }
        // Check the space up and to the left
        if (x__ == x - 1) {
            if (y__ == y - 1) {
                return (true);
            }
        }
        // Check the space directly above
        if (x__ == x) {
            if (y__ == y - 1) {
                return (true);
            }
        }
        // Check the space up and to the right
        if (x__ == x + 1) {
            if (y__ == y - 1) {
                return (true);
            }
        }
        // Check the space to the right
        if (x__ == x + 1) {
            if (y__ == y) {
                return (true);
            }
        }
        // Check the space down and to the right
        if (x__ == x + 1) {
            if (y__ == y + 1) {
                return (true);
            }
        }
        // Check the space directly below
        if (x__ == x) {
            if (y__ == y + 1) {
                return (true);
            }
        }
        // Check the space down and to the left
        if (x__ == x - 1) {
            if (y__ == y + 1) {
                return (true);
            }
        }
        // Check the space directly to the left
        if (x__ == x - 1) {
            if (y__ == y) {
                return (true);
            }
        }
        return (false);
    }

    /**
      * Determine if grid square with upper left corner
      * coordinates (x,y) is occupied by a Place
    **/
    public boolean placeOccupies (int x, int y, boolean exact) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < placeVector_.size(); i__++) {
            Place tempPlace__ = (Place)placeVector_.elementAt(i__);
            int x__ = tempPlace__.getXCoordinate();
            int y__ = tempPlace__.getYCoordinate();
            if (checkBorderingGridSquares(x__, y__, x, y, exact)) {
            	return (true);
            }
        }
        return (false);
    }


    /**
      * Checks to see if any Arc starts at (x,y)
    **/
    public boolean arcStartsAt (int x, int y, boolean exact) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < arcVector_.size(); i__++) {
            Arc tempArc__ = (Arc)arcVector_.elementAt(i__);
            int x__ = tempArc__.getFirstXCoordinate();
            int y__ = tempArc__.getFirstYCoordinate();
            if (checkBorderingGridSquares(x__, y__, x, y, exact)) {
            	return (true);
            }
        }
        return (false);
    }
    
    
    /**
     * return the set of arcs those start from (x,y)
     * @param x
     * @param y
     * @return
     */
    public Vector<Arc> arcStartsAt(int x,int y)
    {
   // 	x = x / petriTool_.gridStep_;
   //     y = y / petriTool_.gridStep_;
        Vector<Arc> startVector=new Vector<>();
        for(int i=0;i<arcVector_.size();i++)
        {
        	Arc tempArc_=(Arc)arcVector_.elementAt(i);
        	int x__=tempArc_.getFirstXCoordinate();
        	int y__=tempArc_.getFirstYCoordinate();
        	if(checkBorderingGridSquares(x__, y__, x, y, true))
        	{
        		startVector.add(tempArc_);
        	}
        }
        return startVector;
    }
    
    /**
     * return the set of arcs those end at (x,y)
     * @param x
     * @param y
     * @return
     */
    public Vector<Arc> arcEndsAt(int x,int y)
    {
 //   	x = x / petriTool_.gridStep_;
 //       y = y / petriTool_.gridStep_;
        Vector<Arc> endVector=new Vector<>();
        for(int i=0;i<arcVector_.size();i++)
        {
        	Arc tempArc__=(Arc)arcVector_.elementAt(i);
        	int x__=tempArc__.getLastXCoordinate();
        	int y__=tempArc__.getLastYCoordinate();
        	if(checkBorderingGridSquares(x__, y__, x, y, true))
        	{
        		endVector.add(tempArc__);
        	}
        }
        return endVector;
    }
    /**
      * Checks to see if any Arc ends at (x,y)
    **/
    public boolean arcEndsAt (int x, int y, boolean exact) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < arcVector_.size(); i__++) {
            Arc tempArc__ = (Arc)arcVector_.elementAt(i__);
            int x__ = tempArc__.getLastXCoordinate();
            int y__ = tempArc__.getLastYCoordinate();
            if (checkBorderingGridSquares(x__, y__, x, y, exact)) {
            	return (true);
            }

        }
        return (false);
    }


    /**
      * Return the Place occupying grid square with
      * upper left corner coordinates (x,y)
    **/
    public Place getPlace (int x, int y) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < placeVector_.size(); i__++) {
            Place tempPlace__ = (Place)placeVector_.elementAt(i__);
            if ((x == tempPlace__.getXCoordinate()) &&
                (y == tempPlace__.getYCoordinate())) {
                return (tempPlace__);
            }
        }
        return (null);
    }
    
    
    
    public Arc getArc(int x,int y)
    {
    	x+=dx;
    	y+=dy;
    	for (int i__ = 0; i__ < arcVector_.size(); i__++) 
    	{
            Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
            if(tempArc__.mouseDownAt(x, y,petriTool_.gridStep_)==true)
            {
            	return tempArc__;
            }
        }
    	return null;
    }
    
    
    
    
    
    
    
    
    
    
    

    /**
      * Return the Transition occupying grid square with
      * upper left corner coordinates (x,y)
    **/
    public Transition getTransition (int x, int y) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
            Transition tempTransition__ = (Transition)transitionVector_.elementAt(i__);
            if ((x == tempTransition__.getXCoordinate()) &&
                (y == tempTransition__.getYCoordinate())) {
                return (tempTransition__);
            }
        }
        return (null);
    }

    /**
      * Determine if grid square with upper left corner
      * coordinates (x,y) is occupied by a Transition
    **/
    public boolean transitionOccupies (int x, int y, boolean exact) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
            Transition tempTransition__ = (Transition)transitionVector_.elementAt(i__);
            int x__ = tempTransition__.getXCoordinate();
            int y__ = tempTransition__.getYCoordinate();
            if (checkBorderingGridSquares(x__, y__, x, y, exact)) {
            	return(true);
            }
        }
        return (false);
    }

    /**
      * Determine if grid square with upper left corner
      * coordinates (x,y) is occupied by a Token
    **/
    public boolean tokenOccupies (int x, int y) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < tokenVector_.size(); i__++) {
            Token tempToken__ = (Token)tokenVector_.elementAt(i__);

            if ((x == tempToken__.getXCoordinate()) &&
                (y == tempToken__.getYCoordinate())) {
                    return (true);
            }
        }
        return (false);
    }

    /**
      * Return the Token occupying grid square with
      * upper left corner coordinates (x,y)
    **/
    public Token getToken (int x, int y) {
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;
        for (int i__ = 0; i__ < tokenVector_.size(); i__++) {
            Token tempToken__ = (Token)tokenVector_.elementAt(i__);
            if ((x == tempToken__.getXCoordinate()) &&
                (y == tempToken__.getYCoordinate())) {
                return (tempToken__);
            }
        }
        return (null);
    }

    /**
      * Calls the adjustSize(double) routine of all the Caption
      * components when the size of the grid changes during
      * Zoom In/Out events.
    **/
    public void adjustFonts(double factor) {
        for (int i__ = 0; i__ < captionVector_.size(); i__++) {
            Caption tempCaption__ = (Caption) captionVector_.
                                        elementAt(i__);
            tempCaption__.adjustSize(factor);
        }
    }






    /**
      * This routine is called when the user is performing a
      * select operation in a grid space with both a Place
      * and a Token.  It returns true if the location of
      * the selection is outside the area of the Token.
    **/
    public boolean selectPlaceOverToken (int x, int y) {
        int gridStep__ = petriTool_.gridStep_;
        int xRem__ = x % gridStep__;
        int yRem__ = y % gridStep__;

        int upperLimit__ = gridStep__ / 4;
        int lowerLimit__ = gridStep__ - gridStep__ / 4;

        if ((xRem__ > upperLimit__) && (xRem__ < lowerLimit__)) {
            if ((yRem__ > upperLimit__) && (yRem__ < lowerLimit__)) {
                return (false);
            }
        }
        return (true);

    }

    /**
      * When the user selects delete, all of the items currently
      * selected will be deleted by this routine.
    **/
    public boolean deleteSelected() {
        boolean itemsDeleted__ = false;
        Vector obsoleteXVector__ = new Vector();
        Vector obsoleteYVector__ = new Vector();
        int offset__ = 0;
        int maxRange__ = 0;
        int gridStep__ = petriTool_.gridStep_;

        // Remove all selected places
        maxRange__ = placeVector_.size();
        for (int i__ = 0; i__ < maxRange__ ; i__++) {
            Place tempPlace__ = (Place) placeVector_.elementAt(i__ - offset__);
            if (tempPlace__.isSelected()) {
                obsoleteXVector__.addElement
                    (new Integer(tempPlace__.getXCoordinate()));
                obsoleteYVector__.addElement
                    (new Integer(tempPlace__.getYCoordinate()));
                placeVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsDeleted__ = true;

            }
        }

        // Remove all selected transitions
        offset__ = 0;
        maxRange__ = transitionVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Transition tempTransition__ = (Transition) transitionVector_.elementAt(i__ - offset__);
            if (tempTransition__.isSelected()) {
                obsoleteXVector__.addElement
                    (new Integer (tempTransition__.getXCoordinate()));
                obsoleteYVector__.addElement
                    (new Integer (tempTransition__.getYCoordinate()));
                transitionVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsDeleted__ = true;
            }
        }

        // Auto-select arcs that were connected to deleted places/transitions
        // Also, auto-select tokens contained in deleted places
        for (int j__ = 0; j__ < obsoleteXVector__.size(); j__++) {
            Integer tempX__ = (Integer) obsoleteXVector__.elementAt(j__);
            Integer tempY__ = (Integer) obsoleteYVector__.elementAt(j__);
            for (int i__ = 0; i__ < arcVector_.size(); i__++) {
                Arc tempArc__ = (Arc) arcVector_.elementAt(i__);

                if ((tempArc__.getFirstXCoordinate() == tempX__.intValue()) &&
                    (tempArc__.getFirstYCoordinate() == tempY__.intValue())) {
                            tempArc__.setSelected(true);
                }
                else if ((tempArc__.getLastXCoordinate() == tempX__.intValue()) &&
                        (tempArc__.getLastYCoordinate() == tempY__.intValue())) {
                            tempArc__.setSelected(true);
                }
            }
            for (int k__ = 0; k__ < tokenVector_.size(); k__++) {
                Token tempToken__ = (Token) tokenVector_.elementAt(k__);
                if ((tempToken__.getXCoordinate() == tempX__.intValue()) &&
                    (tempToken__.getYCoordinate() == tempY__.intValue())) {
                            tempToken__.setSelected(true);
                }
            }

        }

        // Remove all selected tokens
        offset__ = 0;
        maxRange__ = tokenVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Token tempToken__ = (Token) tokenVector_.elementAt(i__ - offset__);
            if (tempToken__.isSelected()) {
                tokenVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsDeleted__ = true;
                // Update Places to set tokenInitialized = false
                if (placeOccupies(tempToken__.getXCoordinate() * gridStep__,
                                    tempToken__.getYCoordinate() * gridStep__, true)) {
                    Place tempPlace__ = getPlace(tempToken__.getXCoordinate() * gridStep__,
                                        tempToken__.getYCoordinate() * gridStep__);
                    tempPlace__.tokenInitialized_ = false;
                }
            }
        }

        // Remove all selected arcs
        offset__ = 0;
        maxRange__ = arcVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Arc tempArc__ = (Arc) arcVector_.elementAt(i__ - offset__);
            if (tempArc__.isSelected()) {
                arcVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsDeleted__ = true;
            }
        }

        // Remove all selected captions
        offset__ = 0;
        maxRange__ = captionVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Caption tempCaption__ = (Caption) captionVector_.
                                    elementAt(i__ - offset__);
            if (tempCaption__.isSelected()) {
                captionVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsDeleted__ = true;
            }
        }

        // Re-calculate the initial marking
        setInitialMarking();

        return(itemsDeleted__);
    }

    /**
      * When the user selects cut, all of the currently selected
      * components will be moved from the design Vectors to
      * a second buffer vector, where they may be reused by
      * the Paste command
    **/
    public boolean cutSelected() {
        boolean itemsCut__ = false;
        Vector obsoleteXVector__ = new Vector();
        Vector obsoleteYVector__ = new Vector();
        int offset__ = 0;
        int maxRange__ = 0;
        int gridStep__ = petriTool_.gridStep_;

        // Clear out all the clipboard buffers
        placeVectorBuf_.removeAllElements();
        transitionVectorBuf_.removeAllElements();
        arcVectorBuf_.removeAllElements();
        tokenVectorBuf_.removeAllElements();
        captionVectorBuf_.removeAllElements();

        // Remove all selected places
        maxRange__ = placeVector_.size();
        for (int i__ = 0; i__ < maxRange__ ; i__++) {
            Place tempPlace__ = (Place) placeVector_.elementAt(i__ - offset__);


            if (tempPlace__.isSelected()) {

                obsoleteXVector__.addElement
                    (new Integer(tempPlace__.getXCoordinate()));
                obsoleteYVector__.addElement
                    (new Integer(tempPlace__.getYCoordinate()));
                placeVectorBuf_.addElement(placeVector_.elementAt(i__- offset__));
                placeVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsCut__ = true;

                // Delete link to token if token not selected
                // in order to keep simulation vectors correct
                if (tokenOccupies(tempPlace__.getXCoordinate() * gridStep__,
                              tempPlace__.getYCoordinate() * gridStep__)) {
                    Token tempToken__ = getToken(tempPlace__.getXCoordinate() * gridStep__,
                                             tempPlace__.getYCoordinate()* gridStep__);
                    if (!tempToken__.isSelected()) {
                        tempPlace__.tokenInitialized_ = false;
                        tempPlace__.token_ = null;
                    }
                }
            }
        }

        // Remove all selected transitions
        offset__ = 0;
        maxRange__ = transitionVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Transition tempTransition__ = (Transition) transitionVector_.elementAt(i__ - offset__);
            if (tempTransition__.isSelected()) {
                obsoleteXVector__.addElement
                    (new Integer (tempTransition__.getXCoordinate()));
                obsoleteYVector__.addElement
                    (new Integer (tempTransition__.getYCoordinate()));
                transitionVectorBuf_.addElement(transitionVector_.elementAt(i__- offset__));
                transitionVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsCut__ = true;
            }
        }

        // Remove all selected tokens
        offset__ = 0;
        maxRange__ = tokenVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Token tempToken__ = (Token) tokenVector_.elementAt(i__ - offset__);
            if (tempToken__.isSelected()) {
                tokenVectorBuf_.addElement(tokenVector_.elementAt(i__- offset__));
                tokenVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsCut__ = true;
                // Update Places to set tokenInitialized = false
                if (placeOccupies(tempToken__.getXCoordinate() * gridStep__,
                                    tempToken__.getYCoordinate() * gridStep__, true)) {
                    Place tempPlace__ = getPlace(tempToken__.getXCoordinate() * gridStep__,
                                        tempToken__.getYCoordinate() * gridStep__);
                    tempPlace__.tokenInitialized_ = false;
                }
            }
        }
        // Remove all selected arcs
        offset__ = 0;
        maxRange__ = arcVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Arc tempArc__ = (Arc) arcVector_.elementAt(i__ - offset__);
            if (tempArc__.isSelected()) {
                arcVectorBuf_.addElement(arcVector_.elementAt(i__- offset__));
                arcVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsCut__ = true;
            }
        }
        // Remove all selected captions
        offset__ = 0;
        maxRange__ = captionVector_.size();
        for (int i__ = 0; i__ < maxRange__; i__++) {
            Caption tempCaption__ = (Caption) captionVector_.
                                    elementAt(i__ - offset__);
            if (tempCaption__.isSelected()) {
                captionVectorBuf_.addElement(captionVector_.elementAt(i__- offset__));
                captionVector_.removeElementAt(i__ - offset__);
                offset__++;
                itemsCut__ = true;
            }
        }
        // Re-calculate the initial marking
        setInitialMarking();
        return (itemsCut__);
    }

    /**
      * This routine makes a copy of all selected components in the
      * design Vectors and places them into the buffer Vectors where
      * they may be reused by a Paste command
    **/
    public boolean copySelected() {
        boolean itemsCopied__ = false;
        int gridStep__ = petriTool_.gridStep_;

        // Clear out all of the clipboard buffers
        placeVectorBuf_.removeAllElements();
        transitionVectorBuf_.removeAllElements();
        arcVectorBuf_.removeAllElements();
        tokenVectorBuf_.removeAllElements();
        captionVectorBuf_.removeAllElements();

        // Copy all selected places
        for (int i__ = 0; i__ < placeVector_.size(); i__++) {
            Place tempPlace__ = (Place) placeVector_.elementAt(i__);
            Place copyPlace__ = (Place) tempPlace__.clone();
            if (tempPlace__.isSelected()) {
                placeVectorBuf_.addElement(copyPlace__);
                itemsCopied__ = true;

                // Delete link to token if token not selected
                // in order to keep simulation vectors correct
                if (tokenOccupies(copyPlace__.getXCoordinate() * gridStep__,
                              copyPlace__.getYCoordinate() * gridStep__)) {
                    Token tempToken__ = getToken(copyPlace__.getXCoordinate() * gridStep__,
                                             copyPlace__.getYCoordinate()* gridStep__);
                    if (!tempToken__.isSelected()) {
                        copyPlace__.tokenInitialized_ = false;
                        copyPlace__.token_ = null;
                    }
                }
            }
        }

        // Copy all selected transitions
        for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
            Transition tempTransition__ = (Transition) transitionVector_.elementAt(i__);
            Transition copyTransition__ = (Transition) tempTransition__.clone();
            if (tempTransition__.isSelected()) {
                transitionVectorBuf_.addElement(copyTransition__);
                itemsCopied__ = true;
            }
        }

        // Copy all selected tokens
        for (int i__ = 0; i__ < tokenVector_.size(); i__++) {
            Token tempToken__ = (Token) tokenVector_.elementAt(i__);
            Token copyToken__ = (Token) tempToken__.clone();
            if (tempToken__.isSelected()) {
                tokenVectorBuf_.addElement(copyToken__);
                itemsCopied__ = true;
            }
        }
        // Copy all selected arcs
        for (int i__ = 0; i__ < arcVector_.size(); i__++) {
            Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
            Arc copyArc__ = (Arc) tempArc__.clone();
            if (tempArc__.isSelected()) {
                arcVectorBuf_.addElement(copyArc__);
                itemsCopied__ = true;
            }
        }
        // Copy all selected captions
        for (int i__ = 0; i__ < captionVector_.size(); i__++) {
            Caption tempCaption__ = (Caption) captionVector_.
                                    elementAt(i__);
            Caption copyCaption__ = (Caption) tempCaption__.clone();
            if (tempCaption__.isSelected()) {
                captionVectorBuf_.addElement(copyCaption__);
                itemsCopied__ = true;
            }
        }

        return (itemsCopied__);
    }

    /**
      * Returns true if the Arc passed as a parameter to this routine
      * already exists, based on the starting and ending coordinates
      * of the Arc
    **/
    public boolean duplicateArc(Arc newArc) {
        // Don't check the last Arc, since that is the current one
        for (int i__ = 0; i__ < arcVector_.size()-1; i__++) {
            Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
            if ((tempArc__.getFirstXCoordinate() == newArc.getFirstXCoordinate()) &&
                (tempArc__.getFirstYCoordinate() == newArc.getFirstYCoordinate()) &&
                (tempArc__.getLastXCoordinate() == newArc.getLastXCoordinate()) &&
                (tempArc__.getLastYCoordinate() == newArc.getLastYCoordinate())) {
                    return (true);
            }
        }
        return(false);
    }

    /**
      * Attempts to paste the contents of the paste buffer to
      * the design.  The minimum X and Y coordinates of all the
      * components in the select buffer will be placed where
      * the user clicked the mouse as the destination.  If any of
      * the components will overlap already existing componenents
      * in the design, the paste will not occur and the routine
      * will return false.
    **/
    public boolean pasteDesination (int x, int y) {

        int xMin__ = petriTool_.gridWidth_;
        int yMin__ = petriTool_.gridHeight_;
        int xTranslate__ = 0;
        int yTranslate__ = 0;
        x = x / petriTool_.gridStep_;
        y = y / petriTool_.gridStep_;

        // Find upper left square of virtual box surrounding
        // the selected components
        for (int i__ = 0; i__ < placeVectorBuf_.size(); i__++) {
            Place tempPlace__ = (Place)placeVectorBuf_.elementAt(i__);
            if (tempPlace__.getXCoordinate() < xMin__) {
                xMin__ = tempPlace__.getXCoordinate();
            }
            if (tempPlace__.getYCoordinate() < yMin__) {
                yMin__ = tempPlace__.getYCoordinate();
            }
        }
        for (int i__ = 0; i__ < transitionVectorBuf_.size(); i__++) {
            Transition tempTransition__ =
                (Transition)transitionVectorBuf_.elementAt(i__);
            if (tempTransition__.getXCoordinate() < xMin__) {
                xMin__ = tempTransition__.getXCoordinate();
            }
            if (tempTransition__.getYCoordinate() < yMin__) {
                yMin__ = tempTransition__.getYCoordinate();
            }
        }
        for (int i__ = 0; i__ < tokenVectorBuf_.size(); i__++) {
            Token tempToken__ =
                (Token)tokenVectorBuf_.elementAt(i__);
            if (tempToken__.getXCoordinate() < xMin__) {
                xMin__ = tempToken__.getXCoordinate();
            }
            if (tempToken__.getYCoordinate() < yMin__) {
                yMin__ = tempToken__.getYCoordinate();
            }
        }
        for (int i__ = 0; i__ < arcVectorBuf_.size(); i__++) {
            Arc tempArc__ =
                (Arc)arcVectorBuf_.elementAt(i__);
            if (tempArc__.getMinXCoordinate() < xMin__) {
                xMin__ = tempArc__.getMinXCoordinate();
            }
            if (tempArc__.getMinYCoordinate() < yMin__) {
                yMin__ = tempArc__.getMinYCoordinate();
            }
        }
        for (int i__ = 0; i__ < captionVectorBuf_.size(); i__++) {
            Caption tempCaption__ =
                (Caption)captionVectorBuf_.elementAt(i__);
            if (tempCaption__.getXCoordinate() < xMin__) {
                xMin__ = tempCaption__.getXCoordinate();
            }
            if (tempCaption__.getYCoordinate() < yMin__) {
                yMin__ = tempCaption__.getYCoordinate();
            }
        }

        // Find translation quantity in x and y directions
        xTranslate__ = x - xMin__;
        yTranslate__ = y - yMin__;

        Vector xVector__ = new Vector();
        Vector yVector__ = new Vector();

        // Create matching x,y vectors of translated
        // coordinates of the selected objects
        for (int i__ = 0; i__ < placeVectorBuf_.size(); i__++) {
            Place tempPlace__ = (Place)placeVectorBuf_.elementAt(i__);
            xVector__.addElement(new Integer(tempPlace__.getXCoordinate() +
                                 xTranslate__));
            yVector__.addElement(new Integer(tempPlace__.getYCoordinate() +
                                 yTranslate__));
            tempPlace__.translateCoordinates(xTranslate__,yTranslate__);
        }
        for (int i__ = 0; i__ < transitionVectorBuf_.size(); i__++) {
            Transition tempTransition__ = (Transition)transitionVectorBuf_.elementAt(i__);
            xVector__.addElement(new Integer(tempTransition__.getXCoordinate() +
                                 xTranslate__));
            yVector__.addElement(new Integer(tempTransition__.getYCoordinate() +
                                 yTranslate__));
            tempTransition__.translateCoordinates(xTranslate__,yTranslate__);
        }
        for (int i__ = 0; i__ < tokenVectorBuf_.size(); i__++) {
            Token tempToken__ = (Token)tokenVectorBuf_.elementAt(i__);
            xVector__.addElement(new Integer(tempToken__.getXCoordinate() +
                                 xTranslate__));
            yVector__.addElement(new Integer(tempToken__.getYCoordinate() +
                                 yTranslate__));
            tempToken__.translateCoordinates(xTranslate__,yTranslate__);
        }
        // THIS SHOULD BE DESIGNED TO ADD ALL COORDINATES, WE'LL
        // JUST DO FIRST AND LAST COORDINATES FOR NOW AND ALLOW
        // OTHER ARC POINTS OVERLAP
        for (int i__ = 0; i__ < arcVectorBuf_.size(); i__++) {
            Arc tempArc__ = (Arc)arcVectorBuf_.elementAt(i__);
            xVector__.addElement(new Integer(tempArc__.getFirstXCoordinate() +
                                 xTranslate__));
            yVector__.addElement(new Integer(tempArc__.getFirstYCoordinate() +
                                 yTranslate__));
            xVector__.addElement(new Integer(tempArc__.getLastXCoordinate() +
                                 xTranslate__));
            yVector__.addElement(new Integer(tempArc__.getLastYCoordinate() +
                                 yTranslate__));
            tempArc__.translateCoordinates(xTranslate__,yTranslate__);
        }
        for (int i__ = 0; i__ < captionVectorBuf_.size(); i__++) {
            Caption tempCaption__ = (Caption)captionVectorBuf_.elementAt(i__);
            xVector__.addElement(new Integer(tempCaption__.getXCoordinate() +
                                 xTranslate__));
            yVector__.addElement(new Integer(tempCaption__.getYCoordinate() +
                                 yTranslate__));

            // Make sure fonts do not span beyond border
            FontMetrics fm = getFontMetrics(tempCaption__.getFont());
            int width__ = fm.stringWidth(tempCaption__.getText());
            int height__ = fm.getHeight();
            xVector__.addElement(new Integer(tempCaption__.getXCoordinate() +
                                 xTranslate__ + width__ / petriTool_.gridStep_));
            yVector__.addElement(new Integer(tempCaption__.getYCoordinate() +
                                 yTranslate__ - height__ / petriTool_.gridStep_));

            tempCaption__.translateCoordinates(xTranslate__,yTranslate__);
        }

        // Check each of the translated coordinates to see if there
        // is already a component there
        for (int i__ = 0; i__ < xVector__.size(); i__++) {
            Integer xVal__ = (Integer)xVector__.elementAt(i__);
            Integer yVal__ = (Integer)yVector__.elementAt(i__);
            if (gridSpaceOccupied(xVal__.intValue() * petriTool_.gridStep_,
                                  yVal__.intValue() * petriTool_.gridStep_)) {
                StatusMessage("Components overlap, select another destination.");
                return (false);
            }
            if (!validDimension(xVal__.intValue() * petriTool_.gridStep_,
                                yVal__.intValue() * petriTool_.gridStep_)) {
                StatusMessage("Components span beyond border, select another destination.");
                return (false);
            }
        }



        // If we get here, there was no conflict, so add the
        // the selected components to their respective vectors
        for (int i__ = 0; i__ < placeVectorBuf_.size(); i__++) {
            Place tempPlace__ = (Place)placeVectorBuf_.elementAt(i__);
            Place copyPlace__ = (Place) tempPlace__.clone();
            copyPlace__.setLabel(petriTool_.getPlaceLabel());
            placeVector_.addElement(copyPlace__);
        }
        for (int i__ = 0; i__ < transitionVectorBuf_.size(); i__++) {
            Transition tempTransition__ = (Transition)transitionVectorBuf_.elementAt(i__);
            Transition copyTransition__ = (Transition) tempTransition__.clone();
            copyTransition__.setLabel(petriTool_.getTransitionLabel());
            transitionVector_.addElement(copyTransition__);
        }
        for (int i__ = 0; i__ < tokenVectorBuf_.size(); i__++) {
            Token tempToken__ = (Token)tokenVectorBuf_.elementAt(i__);
            Token copyToken__ = (Token) tempToken__.clone();
            tokenVector_.addElement(copyToken__);
        }
        for (int i__ = 0; i__ < arcVectorBuf_.size(); i__++) {
            Arc tempArc__ = (Arc)arcVectorBuf_.elementAt(i__);
            Arc copyArc__ = (Arc) tempArc__.clone();
            arcVector_.addElement(copyArc__);
        }
        for (int i__ = 0; i__ < captionVectorBuf_.size(); i__++) {
            Caption tempCaption__ = (Caption)captionVectorBuf_.elementAt(i__);
            Caption copyCaption__ = (Caption) tempCaption__.clone();
            captionVector_.addElement(copyCaption__);
        }

        // Re-calculate the initial marking
        setInitialMarking();
        return (true);

    }
    
    
    
    private boolean mouseDraging_=false;
	
	private Place placeDraged_=null;
	private Transition transitionDraged_=null;
	private Token tokenDraged_=null;
//	private Arc arcIn_=null;
	//private Arc arcOut_=null;
	
	
	/**Vector of the arcs of component that is dragged**/
	private Vector<Arc> arcsInVector_=new Vector<>();
	private Vector<Arc> arcsOutVector_=new Vector<>();
	
	
	
	/**Declare the vector of arcs of components those are dragged,
	 * which are not being dragged by mouse, but together with it
	 *  **/
	private Vector<Arc> arcsInVector__=new Vector<>();
	private Vector<Arc> arcsOutVector__=new Vector<>();
	/**==============================**/
	
    

    
    
    
    

   

    /**
      * Handle the event where the user drags the mouse
      * (mouse button is down)
    **/
/*
    public synchronized boolean mouseDrag(Event evt, int x, int y) {
        int gridStep__ = petriTool_.gridStep_;

        x += dx;
        y += dy;

        if (x == -1 || y == -1) {
            return (true);
        }

        if (selectedButton_.equals("Arc")) {
            if (newArc_ != null) {
                // COMMENTED OUT FOR ARC UPGRADE
                //newArc_.set2ndCoordinate(x / gridStep__, y / gridStep__);
                //newArc_.setEndDrawCoordinates(0, 0);
                repaint();
                return (true);
            }
            repaint();
        }
        return (false);
    }
*/

    /**
      * Checks the design for validity.  The design is invalid if
      * there are Tokens outside of Places, or if there are Places,
      * Transitions, or Arcs that are unconnected.
    **/
    public boolean validDesign() {
        StatusMessage("Validating design, please wait...");

        if (danglingToken(tokenVector_, placeVector_)) {
            StatusMessage("The design contains a dangling Token at " +
                          "x = " + xDangle_ + ", y = " + yDangle_);
        }
        else if (danglingPlace(placeVector_, arcVector_)) {
            StatusMessage("The design contains an unconnected Place at " +
                          "x = " + xDangle_ + ", y = " + yDangle_);
        }
        else if (danglingTransition(transitionVector_, arcVector_)) {
            StatusMessage("The design contains an unconnected Transition at " +
                          "x = " + xDangle_ + ", y = " + yDangle_);
        }
        else if (danglingArc(arcVector_)) {
            StatusMessage("The design contains an unconnected Arc at " +
                          "x = " + xDangle_ + ", y = " + yDangle_);
        }
        else if (invalidArc(arcVector_)) {
            StatusMessage("The design contains an invalid Arc at " +
                          "x = " + xDangle_ + ", y = " + yDangle_);
        }
        else {
            StatusMessage("Design is valid.");
            validDesign_ = true;
        }
        return(validDesign_);
    }

    /**
      * Checks for Tokens which are not contained within Places
    **/
    private boolean danglingToken(Vector tokenVector, Vector placeVector) {
        for (int i__ = 0; i__ < tokenVector.size(); i__++) {
            Token tempToken__ = (Token) tokenVector.elementAt(i__);

            int x__ = tempToken__.getXCoordinate() * petriTool_.gridStep_;
            int y__ = tempToken__.getYCoordinate() * petriTool_.gridStep_;

            if (!petriTool_.designPanel_.placeOccupies(x__, y__, true)) {
                xDangle_ = x__ / petriTool_.gridStep_;
                yDangle_ = y__ / petriTool_.gridStep_;
                return (true);
            }
        }
        return(false);
    }

    /**
      * Checks for Places which do not have any input or output Arcs
    **/
    private boolean danglingPlace(Vector placeVector, Vector arcVector) {
        for (int i__ = 0; i__ < placeVector.size(); i__++) {
            Place tempPlace__ = (Place) placeVector.elementAt(i__);
            int x__ = tempPlace__.getXCoordinate() * petriTool_.gridStep_;
            int y__ = tempPlace__.getYCoordinate() * petriTool_.gridStep_;

            if (!petriTool_.designPanel_.arcStartsAt(x__, y__, true)) {
                if (!petriTool_.designPanel_.arcEndsAt(x__, y__, true)) {
                    xDangle_ = x__ / petriTool_.gridStep_;
                    yDangle_ = y__ / petriTool_.gridStep_;
                    return (true);
                }
            }
        }
        return(false);
    }

    /**
      * Checks for Transitions which do not have either an
      * input Arc or an output Arc
    **/
    private boolean danglingTransition(Vector transitionVector, Vector arcVector) {
        for (int i__ = 0; i__ < transitionVector.size(); i__++) {
            Transition tempTransition__ = (Transition) transitionVector.elementAt(i__);
            int x__ = tempTransition__.getXCoordinate() * petriTool_.gridStep_;
            int y__ = tempTransition__.getYCoordinate() * petriTool_.gridStep_;
            if (!petriTool_.designPanel_.arcStartsAt(x__, y__, true)) {
//                xDangle_ = x__ / petriTool_.gridStep_;
//                yDangle_ = y__ / petriTool_.gridStep_;
//                return (true);
//            }
            if (!petriTool_.designPanel_.arcEndsAt(x__, y__, true)) {
                xDangle_ = x__ / petriTool_.gridStep_;
                yDangle_ = y__ / petriTool_.gridStep_;
                return (true);
            }
        }
        }    
        return(false);
    }

    /**
      * Checks for Arcs which do not either start at a Place or
      * Transition or end at a Place or Transition
    **/
    private boolean danglingArc(Vector arcVector) {
        for (int i__ = 0; i__ < arcVector.size(); i__++) {
            Arc tempArc__ = (Arc) arcVector.elementAt(i__);
            int x__ = tempArc__.getFirstXCoordinate() * petriTool_.gridStep_;
            int y__ = tempArc__.getFirstYCoordinate() * petriTool_.gridStep_;
            if (!petriTool_.designPanel_.placeOccupies(x__, y__, true)) {
                if (!petriTool_.designPanel_.transitionOccupies(x__, y__, true)) {
                    xDangle_ = x__ / petriTool_.gridStep_;
                    yDangle_ = y__ / petriTool_.gridStep_;
                    return (true);
                }
            }
            x__ = tempArc__.getLastXCoordinate() * petriTool_.gridStep_;
            y__ = tempArc__.getLastYCoordinate() * petriTool_.gridStep_;
            if (!petriTool_.designPanel_.placeOccupies(x__, y__, true)) {
                if (!petriTool_.designPanel_.transitionOccupies(x__, y__, true)) {
                    xDangle_ = x__ / petriTool_.gridStep_;
                    yDangle_ = y__ / petriTool_.gridStep_;
                    return (true);
                }
            }
        }
        return(false);
    }

    /**
      * Checks for Arcs which span between two places or
      * between two transitions
    **/
    private boolean invalidArc(Vector arcVector) {
        boolean startPlace__;
        boolean endPlace__;
        boolean noStartComponent__;
        boolean noEndComponent__;

        for (int i__ = 0; i__ < arcVector.size(); i__++) {
            Arc tempArc__ = (Arc) arcVector.elementAt(i__);
            int x1__ = tempArc__.getFirstXCoordinate() * petriTool_.gridStep_;
            int y1__ = tempArc__.getFirstYCoordinate() * petriTool_.gridStep_;

            if (petriTool_.designPanel_.placeOccupies(x1__, y1__, true)) {
                startPlace__ = true;
            }
            else {
                startPlace__ = false;
            }

            int x2__ = tempArc__.getLastXCoordinate() * petriTool_.gridStep_;
            int y2__ = tempArc__.getLastYCoordinate() * petriTool_.gridStep_;

            if (petriTool_.designPanel_.placeOccupies(x2__, y2__, true)) {
                endPlace__ = true;
            }
            else {
                endPlace__ = false;
            }

            if (startPlace__ == endPlace__) {
                xDangle_ = x1__ / petriTool_.gridStep_;
                yDangle_ = y1__ / petriTool_.gridStep_;
                return (true);
            }
        }
        return(false);
    }


    /**When drawing a dashed frame, determine which components are in the 
     * box by passing in parameters**/
    public boolean isInRect(int x1,int y1,int x2,int y2,int x,int y)
    {
    	
    	x1=x1/petriTool_.gridStep_;
    	x2=x2/petriTool_.gridStep_;
    	y1=y1/petriTool_.gridStep_;
    	y2=y2/petriTool_.gridStep_;
    	
    	
    	if(x1<x2&&y1<y2)
    	{
    		if(x>x1-1&&x<x2+1&&y>y1-1&&y<y2+1)
    			return true;
    	}
    	else if(x2<x1&&y1<y2)
    	{
    		if(x>x2-1&&x<x1+1&&y>y1-1&&y<y2+1)
    			return true;
    	}
    	else if(x1<x2&&y2<y1)
    	{
    		if(x>x1-1&&x<x2+1&&y>y2-1&&y<y1+1)
    			return true;
    	}
    	else
    	{
    		if(x>x2-1&&x<x1+1&&y>y2-1&&y<y1+1)
    			return true;
    	}
    	return false;
    }	
    
    public void changePosTogether(int gridPosChangeX,int gridPosChangeY)
    {
		Iterator<Transition> transitionIterator=transitionVector_.iterator();
		Iterator<Place> placeIterator=placeVector_.iterator();
		Iterator<Token> tokenIterator=tokenVector_.iterator();
		while(transitionIterator.hasNext())
		{
			Transition tempTransition=transitionIterator.next();
			if(tempTransition.isSelected()&&tempTransition!=transitionDraged_)
			{
				int finalPosX=tempTransition.getXCoordinate()+gridPosChangeX;
				int finalPosY=tempTransition.getYCoordinate()+gridPosChangeY;
				arcsInVector__=arcEndsAt(tempTransition.getXCoordinate(), tempTransition.getYCoordinate());
				arcsOutVector__=arcStartsAt(tempTransition.getXCoordinate(), tempTransition.getYCoordinate());
				tempTransition.setxCoordinate_(finalPosX);
				tempTransition.setyCoordinate_(finalPosY);
			/**if the red border is not big enough, set it bigger**/
				if(finalPosX>petriTool_.gridWidth_)
				{
					petriTool_.gridWidth_=finalPosX;
				}
				if(finalPosY>petriTool_.gridHeight_)
				{
					petriTool_.gridHeight_=finalPosY;
				}
				tempTransition.draw(getGraphics(), petriTool_.gridStep_, petriTool_.foregroundColor_, petriTool_.transitionLabels_);
				if(!arcsInVector__.isEmpty())
				{
					Iterator<Arc> arcInItor=arcsInVector__.iterator();
					while(arcInItor.hasNext())
					{
						Arc tempArc=arcInItor.next();
						tempArc.setEndCoordinate(finalPosX, finalPosY);
						tempArc.calculateSlopes();
						tempArc.setArcDrawCoordinates();
						tempArc.draw(getGraphics(), petriTool_.gridStep_,petriTool_.foregroundColor_ );
					}
				}
				if(!arcsOutVector__.isEmpty())
				{
					Iterator<Arc> arcOutItor=arcsOutVector__.iterator();
					while(arcOutItor.hasNext())
					{
						Arc tempArc=arcOutItor.next();
						tempArc.setFirstCoordinate(finalPosX, finalPosY);
						tempArc.calculateSlopes();
						tempArc.setArcDrawCoordinates();
						tempArc.draw(getGraphics(), petriTool_.gridStep_,petriTool_.foregroundColor_ );
					}
				}
			}
		}
		while(placeIterator.hasNext())
		{
			Place tempPlace=placeIterator.next();
			if(tempPlace.isSelected()&&tempPlace!=placeDraged_)
			{
				int finalPosX=tempPlace.getXCoordinate()+gridPosChangeX;
				int finalPosY=tempPlace.getYCoordinate()+gridPosChangeY;
				arcsInVector__=arcEndsAt(tempPlace.getXCoordinate(), tempPlace.getYCoordinate());
				arcsOutVector__=arcStartsAt(tempPlace.getXCoordinate(), tempPlace.getYCoordinate());
				tempPlace.setxCoordinate_(finalPosX);
				tempPlace.setyCoordinate_(finalPosY);
			/**if the red border is not big enough, set it bigger**/
				if(finalPosX>petriTool_.gridWidth_)
				{
					petriTool_.gridWidth_=finalPosX;
				}
				if(finalPosY>petriTool_.gridHeight_)
				{
					petriTool_.gridHeight_=finalPosY;
				}
				tempPlace.draw(getGraphics(), petriTool_.gridStep_, petriTool_.foregroundColor_, petriTool_.placeLabels_);
				if(!arcsInVector__.isEmpty())
				{
					Iterator<Arc> arcInItor=arcsInVector__.iterator();
					while(arcInItor.hasNext())
					{
						Arc tempArc=arcInItor.next();
						tempArc.setEndCoordinate(finalPosX, finalPosY);
						tempArc.draw(getGraphics(), petriTool_.gridStep_,petriTool_.foregroundColor_ );
					}
				}
				if(!arcsOutVector__.isEmpty())
				{
					Iterator<Arc> arcOutItor=arcsOutVector__.iterator();
					while(arcOutItor.hasNext())
					{
						Arc tempArc=arcOutItor.next();
						tempArc.setFirstCoordinate(finalPosX, finalPosY);
						tempArc.draw(getGraphics(), petriTool_.gridStep_,petriTool_.foregroundColor_ );
					}
				}
			}
		}
		while(tokenIterator.hasNext())
		{
			Token tempToken=tokenIterator.next();
			if(tempToken.isSelected()&&tempToken!=tokenDraged_)
			{
				int finalPosX=tempToken.getXCoordinate()+gridPosChangeX;
				int finalPosY=tempToken.getYCoordinate()+gridPosChangeY;
				tempToken.setxCoordinate_(finalPosX);
				tempToken.setyCoordinate_(finalPosY);
				tempToken.draw(getGraphics(), petriTool_.gridStep_, petriTool_.foregroundColor_);
			}
		}

		/**Everytime the old coordinate value copys to new coordinator
		 * Otherwise, they will not synchronize**/
		oldPositionX_=newPositionX_;
		oldPositionY_=newPositionY_;
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
//		x += dx;
//        y += dy;
        int xOriginal__ = x;
        int yOriginal__ = y;
        x = x - (x % petriTool_.gridStep_);
        y = y - (y % petriTool_.gridStep_);
        mouseX_=x;
        mouseY_=y;
		if(e.getButton()==MouseEvent.BUTTON3)
		{
//			for (int i__ = 0; i__ < arcVector_.size(); i__++) {
//                Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
//                tempArc__.mouseDownAt(xOriginal__, yOriginal__,
//                                      petriTool_.gridStep_);
//                if(tempArc__.isSelected()==true)
//                {
//                	aMenu.show(this, e.getX(), e.getY());
//                }
//            }
			
			if(getArc(x, y)!=null)
			{
				aMenu.show(this, e.getX(), e.getY());
			}
			
			
			
			
			if(getPlace(x, y)!=null/*&&selectedButton_=="Pointer"*/)
			{
				pMenu.show(this, e.getX(), e.getY());
			}
			if(getTransition(x, y)!=null/*&&selectedButton_=="Pointer"*/)
			{
				tMenu.show(this, e.getX(), e.getY());
			}
		}
	}
	
	
	private void setAllNotSelected() {
		// TODO Auto-generated method stub
		setOneKindOfComponentsNotSelected(arcVector_);
		setOneKindOfComponentsNotSelected(placeVector_);
		setOneKindOfComponentsNotSelected(transitionVector_);
		setOneKindOfComponentsNotSelected(tokenVector_);
	}
	
	private void setOneKindOfComponentsNotSelected(Vector<PetriComponent> petriVector)
	{
		Iterator<PetriComponent> petriIterator=petriVector.iterator();
		while(petriIterator.hasNext())
		{
			PetriComponent petriComponent=petriIterator.next();
			petriComponent.setNotSelected();
		}
	}
	
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	/**
     * Handles mouseDown events within the DesignPanel, which
     * will trigger certain actions depending on the state of
     * the ControlPanel and other previous events.
   **/
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!gridSpaceOccupied(e.getX(), e.getY()))
		{
			setAllNotSelected();
		}	
//		if(e.getButton()==e.BUTTON3)
//			return;
		int x=e.getX();
		int y=e.getY();
		int gridStep__ = petriTool_.gridStep_;

        // For scrollbar translation
        x += dx;
        y += dy;

        int xOriginal__ = x;
        int yOriginal__ = y;

        // Align coordinates so that component is centered
        // in a grid square
        x = x - (x % gridStep__);
        y = y - (y % gridStep__);

        selectedButton_ = petriTool_.controlPanel_.getCurrentButton();
        
        selectRectX1_=x;
        selectRectY1_=y; 
        
        
        
        if(mouseDraging_==false)
        {
        	if(validDimension(x,y))
    		{
    			if(gridSpaceOccupied(x,y))
    			{
    				mouseDraging_=true;
    				placeDraged_=getPlace(x, y);
    				tokenDraged_=getToken(x, y);
    				transitionDraged_=getTransition(x, y);
    				arcsOutVector_=arcStartsAt(x/ petriTool_.gridStep_, y/ petriTool_.gridStep_);
    				arcsInVector_=arcEndsAt(x/petriTool_.gridStep_, y/petriTool_.gridStep_);
    				oldPositionX_=x;
    				oldPositionY_=y;
    			}
    		}	
        }
        
        
        if (selectedButton_.equals("Pointer")) {
            if (petriTool_.captionInProgress_) {
                if (validDimension(x, y)) {
                    FontMetrics fm = getFontMetrics(petriTool_.captionFont_);
                    int width__ = fm.stringWidth(petriTool_.captionText_);
                    int height__ = fm.getHeight();
                    if (validDimension(x+width__, y-height__)) {
                        if (!gridSpaceOccupied(x, y)) {
                            Caption caption__ = new Caption
                                (x / gridStep__, y / gridStep__ +1,
                                petriTool_.captionText_,
                                petriTool_.captionFont_);
                            caption__.setSelected(false);
                            captionVector_.addElement(caption__);
                            petriTool_.captionInProgress_ = false;
                            petriTool_.captionText_ = "";
                        }
                        else {
                            StatusMessage("Captions may not be drawn within 1 grid space of other elements");
                        }
                    }
                    else {
                        StatusMessage("Caption dimensions extend beyond border.");
                    }
                }
                else {
                    StatusMessage("Captions must be drawn within the border.");
                }
            }
            else if (petriTool_.pasteInProgress_) {
                if (pasteDesination(x,y)) {
                    petriTool_.pasteInProgress_ = false;
                    petriTool_.destroySimulation();
                }
            }
            else if (placeOccupies(x, y, true)) {
                if (tokenOccupies(x,y)) {
                    if (selectPlaceOverToken(xOriginal__, yOriginal__)) {
                        Place tempPlace__ = getPlace(x,y);
                        tempPlace__.toggleSelected();
                    }
                    else {
                        Token tempToken__ = getToken(x,y);
                        tempToken__.toggleSelected();
                    }
                }
                else {
                    Place tempPlace__ = getPlace(x,y);
                    tempPlace__.toggleSelected();
                }
            }
            else if (transitionOccupies(x, y, true)) {
                Transition tempTransition__ = getTransition(x,y);
                tempTransition__.toggleSelected();
            }
            else if (tokenOccupies(x, y)) {
                Token tempToken__ = getToken(x,y);
                tempToken__.toggleSelected();
            }
            // Inform Captions and Arcs of mouseDown event
            // so that they may determine if they were selected
            for (int i__ = 0; i__ < captionVector_.size(); i__++) {
                Caption tempCaption__ = (Caption) captionVector_.
                                        elementAt(i__);
                tempCaption__.mouseDownAt(xOriginal__, yOriginal__,
                                          gridStep__);
            }
            for (int i__ = 0; i__ < arcVector_.size(); i__++) {
                Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
                tempArc__.mouseDownAt(xOriginal__, yOriginal__,
                                      gridStep__);
            }
        }
        else if (selectedButton_.equals("Place")) {
            if (validDimension(x, y)) {
                if (!gridSpaceOccupied(x, y)) {
                    placeVector_.addElement(new Place
                        (x / gridStep__, y / gridStep__,petriTool_.getPlaceLabel()));
                        petriTool_.destroySimulation();
                        setInitialMarking();
                }
                else {
                    StatusMessage("Places may not be drawn within 1 grid space of other elements");
                }
            }
            else {
                StatusMessage("Places must be drawn within the border.");
            }
        }
        else if (selectedButton_.equals("Token")) {
            if (validDimension(x, y)) {
                if (placeOccupies(x, y, true)) {
                    if (!tokenOccupies(x,y)) {
                        int numTokens__ = petriTool_.componentPanel_.
                            getNumTokens();
                        Token tempToken__ = new Token
                                (x / gridStep__, y / gridStep__,
                                numTokens__);
                        tokenVector_.addElement(tempToken__);
                        petriTool_.destroySimulation();
                        Place tempPlace__ = getPlace(x, y);
                        tempPlace__.setToken(tempToken__);
                        setInitialMarking();
                    }
                    else {
                        StatusMessage("Tokens already occupy this Place.");
                    }
                }
                else {
                    StatusMessage("Tokens must be drawn within Places.");
                }
            }
            else {
                StatusMessage("Tokens must be drawn within the border.");
            }
        }
        else if (selectedButton_.equals("Transition")) {
            if (validDimension(x, y)) {
                if (!gridSpaceOccupied(x, y)) {
                    transitionVector_.addElement(new Transition
                        (x / gridStep__, y / gridStep__,
                         petriTool_.getTransitionLabel()));
                    petriTool_.destroySimulation();
                }
                else {
                    StatusMessage("Transitions may not be drawn within 1 grid space of other elements");
                }
            }
            else {
                StatusMessage("Transitions must be drawn within the border.");
            }
        }
        else if (selectedButton_.equals("Arc")) {
            if (validDimension(x, y)) {
                // If no Arc started yet
                if (newArc_ == null) {
                    // Arcs must start at either a Place or Transition
                    if (placeOccupies (x, y, true)) {
                        newArc_ = new Arc (x / gridStep__, y / gridStep__);
                        newArc_.setStartComponent("Place");
                        newArc_.setStartPlace_(getPlace(x, y));
                        arcVector_.addElement(newArc_);
                        StatusMessage("Select next Arc coordinate.");
                        return;
                    }
                    else if (transitionOccupies(x, y, true)) {
                        newArc_ = new Arc (x / gridStep__, y / gridStep__);
                        newArc_.setStartComponent("Transition");
                        newArc_.setStartTransition_(getTransition(x, y));
                        arcVector_.addElement(newArc_);
                        StatusMessage("Select next Arc coordinate.");
                        return;
                    }
                    else {
                        StatusMessage("Arcs must start at either a Place or Transition.");
                        return;
                    }
                }
                else {
                    // Cannot start and end on same component
                    if (newArc_.getFirstXCoordinate() * gridStep__ == x &&
                        newArc_.getFirstYCoordinate() * gridStep__  == y) {
                            StatusMessage("Arcs cannot be drawn between the same component.");
                            return;
                    }


                    // Determine if a Place or Transition occupy the coordinate
                    if (placeOccupies (x, y, true)) {
                        newArc_.setEndComponent("Place");
                        newArc_.setDestinationPlace_(getPlace(x, y));
                        newArc_.addCoordinates(x / gridStep__, y / gridStep__);
                        newArc_.setTokensToEnable(petriTool_.componentPanel_.
                            getTokensToEnable());
                        String start__ = newArc_.getStartComponent();
                        if (start__.equals("Place")) {
                            StatusMessage("Arcs cannot be drawn between like components.");
                            arcVector_.removeElementAt(arcVector_.size()-1);
                            newArc_= null;
                            return;
                        }
                        if (duplicateArc(newArc_)) {
                            StatusMessage("An Arc is already placed there.");
                            arcVector_.removeElementAt(arcVector_.size()-1);
                            newArc_ = null;
                            return;
                        }

                        // Arc completed, delete reference to newArc_
                        newArc_ = null;
                    }
                    else if (transitionOccupies(x, y, true)) {
                        newArc_.setEndComponent("Transition");
                        newArc_.setDestinationTransition_(getTransition(x, y));
                        newArc_.addCoordinates(x / gridStep__, y / gridStep__);
                        newArc_.setTokensToEnable(petriTool_.componentPanel_.
                            getTokensToEnable());
                        String start__ = newArc_.getStartComponent();

                        if (start__.equals("Transition")) {
                            StatusMessage("Arcs cannot be drawn between like components.");
                            arcVector_.removeElementAt(arcVector_.size()-1);
                            newArc_= null;
                            return;
                        }

                        if (duplicateArc(newArc_)) {
                            StatusMessage("An Arc is already placed there.");
                            arcVector_.removeElementAt(arcVector_.size()-1);
                            newArc_ = null;
                            return;
                        }

                        // Arc completed, delete reference to newArc_
                        newArc_ = null;
                    }
                    else {
                        newArc_.addCoordinates(x / gridStep__, y / gridStep__);
                    }
                }
                petriTool_.destroySimulation();
            }
            else {
                StatusMessage("Arcs must be drawn within the border.");
            }
        }

        else if (selectedButton_.equals("Calc")) {

        }
        else if (selectedButton_.equals("Show")) {

        }
        else if (selectedButton_.equals("Prop")) {

        }
        else if (selectedButton_.equals("Help")) {

        }
        else {
        }

        return;
	}
	
	
	
	 /**
     * Handle the event where the user releases the mouse button
     **/
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		

		
		
	   	selectRectX1_=0;
    	selectRectY1_=0;
    	selectRectX2_=0;
    	selectRectY2_=0;
    	
    	mouseDraging_=false;
        update(getGraphics());
        tokenDraged_=null;
        transitionDraged_=null;
        placeDraged_=null;
        //arcIn_=null;
        //arcOut_=null;
        arcsInVector_=new Vector<>();
        arcsOutVector_=new Vector<>();
        return;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(resizing==false)
		{
			if(selectedButton_=="Arc")
			
			{
				JOptionPane.showMessageDialog(null, "Please select pointer first");
				return;
			}
			int x=e.getX();
			int y=e.getY();
			x+=dx;
			y+=dy;
			int gridStep__ = petriTool_.gridStep_;
			int xOriginal__ = x;
			int yOriginal__ = y;
			// Align coordinates so that component is centered
			// in a grid square
			x = x - (x % gridStep__);
			y = y - (y % gridStep__);
			
			/**Set the dragged components' new position **/
			newPositionX_=x;
			newPositionY_=y;
			/**Set the second point of dash rectangle**/
			selectRectX2_=x;
			selectRectY2_=y;
			
			/**
			 * Change the x,y of mouse to grid's x,y
			 * **/
			int gridPosChangedX__=(newPositionX_-oldPositionX_)/petriTool_.gridStep_;
			int gridPosChangedY__=(newPositionY_-oldPositionY_)/petriTool_.gridStep_;
			/**===============**/
			/**Declare iterators to traverse the vector of place,
			 * transition, token. And judge if they are in the dash rectangle
			 * ============================================**/
			Iterator<Transition> transitionIterator=transitionVector_.iterator();
			Iterator<Place> placeIterator=placeVector_.iterator();
			Iterator<Token> tokenIterator=tokenVector_.iterator();
			while(placeIterator.hasNext())
			{
				Place tempPlace=placeIterator.next();
				/**
				 * Detect if there are any place in the dash rectangle, if so, set it as selected,
				 * otherwise set it unselected
				 * **/
				if(isInRect(selectRectX1_, selectRectY1_, selectRectX2_, selectRectY2_, 
						tempPlace.getXCoordinate(), tempPlace.getYCoordinate()))
				{	
					tempPlace.setSelected();
				}
				else if(mouseDraging_==false)		//Prevent the selected become unselected
				{									//when you drag component more than one			
					tempPlace.setNotSelected();
				}
			}
			while(transitionIterator.hasNext())
			{
				Transition tempTransition=transitionIterator.next();
				/**
				 * Just the same as place
				 */
				if(isInRect(selectRectX1_, selectRectY1_, selectRectX2_, selectRectY2_, 
						tempTransition.getXCoordinate(), tempTransition.getYCoordinate()))
				{
					tempTransition.setSelected();
				}
				else if(mouseDraging_==false)
				{
					tempTransition.setNotSelected();
				}
			}
			while(tokenIterator.hasNext())
			{
				Token tempToken=tokenIterator.next();
				/**
				 * Just the same as place
				 */
				if(isInRect(selectRectX1_, selectRectY1_, selectRectX2_, selectRectY2_, 
						tempToken.getXCoordinate(), tempToken.getYCoordinate()))
				{
					tempToken.setSelected();
				}
				else if(mouseDraging_==false)
				{
					tempToken.setNotSelected();
				}

			}
			/**========================================================================**/
			/**Single drag and move===============================================================**/
			if(mouseDraging_==true)
			{
				
				if(placeDraged_!=null)
				{	
					placeDraged_.setxCoordinate_(x/gridStep__);
					placeDraged_.setyCoordinate_(y/gridStep__);
				}
				if(tokenDraged_!=null)
				{	
					tokenDraged_.setxCoordinate_(x/gridStep__);
					tokenDraged_.setyCoordinate_(y/gridStep__);
				}
				if(transitionDraged_!=null)
				{
					transitionDraged_.setxCoordinate_(x/gridStep__);
					transitionDraged_.setyCoordinate_(y/gridStep__);
				}
				
				/**Change all components' position who are selected**/
				changePosTogether(gridPosChangedX__, gridPosChangedY__);

				
				/**if the red border is not big enough, set it bigger**/
				if(x/gridStep__>petriTool_.gridWidth_)
				{
					petriTool_.gridWidth_=x/gridStep__;
				}
				if(y/gridStep__>petriTool_.gridHeight_)
				{
					petriTool_.gridHeight_=y/gridStep__;
				}
				
				
				
				
				
				
				
				
			/*	if(arcIn_!=null)
				{
					arcIn_.setEndCoordinate(x/gridStep__, y/gridStep__);
				}
				if(arcOut_!=null)
				{
					arcOut_.setFirstCoordinate(x/gridStep__, y/gridStep__);
				}*/
				
				if(!arcsInVector_.isEmpty())
				{
					Iterator<Arc> arcInItor=arcsInVector_.iterator();
					while(arcInItor.hasNext())
					{
						Arc arcInTemp=arcInItor.next();
						arcInTemp.setEndCoordinate(x/gridStep__, y/gridStep__);
						arcInTemp.calculateSlopes();
						arcInTemp.setArcDrawCoordinates();
						
					}
				}
				if(!arcsOutVector_.isEmpty())
				{
					Iterator<Arc> arcOutItor=arcsOutVector_.iterator();
					while(arcOutItor.hasNext())
					{
						Arc arcOutTemp=arcOutItor.next();
						arcOutTemp.setFirstCoordinate(x/gridStep__, y/gridStep__);
						arcOutTemp.calculateSlopes();
						arcOutTemp.setArcDrawCoordinates();
					}
				}
			}
			/**========================================================================**/
		}
		else
		{
			if(seResizing)
			{
				petriTool_.gridHeight_=e.getY()/petriTool_.gridStep_;
				petriTool_.gridWidth_=e.getX()/petriTool_.gridStep_;
			}
			else if(sResizing)
			{
				petriTool_.gridHeight_=e.getY()/petriTool_.gridStep_;
			}
			else if(eResizing)
			{
				petriTool_.gridWidth_=e.getX()/petriTool_.gridStep_;
			}
		}
    	update(getGraphics());
    	return;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stupb
		if(e.getX()/petriTool_.gridStep_==petriTool_.gridWidth_+1&&
				(double)e.getY()/petriTool_.gridStep_>(double)petriTool_.gridHeight_&&
				(double)e.getY()/petriTool_.gridStep_<(double)petriTool_.gridHeight_+1)
		{		
			this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
			seResizing=true;
			resizing=true;
			eResizing=false;
			sResizing=false;
		}
		else if(e.getX()/petriTool_.gridStep_==petriTool_.gridWidth_+1)
		{	
			this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			eResizing=true;
			resizing=true;
			sResizing=false;
			seResizing=false;
		}
		else if((double)e.getY()/petriTool_.gridStep_>(double)petriTool_.gridHeight_&&
				(double)e.getY()/petriTool_.gridStep_<(double)petriTool_.gridHeight_+1)
		{	
			this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
			sResizing=true;
			resizing=true;
			eResizing=false;
			seResizing=false;
		}
		else
		{
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			eResizing=false;
			sResizing=false;
			seResizing=false;
			resizing=false;
		}
		}
	private boolean eResizing=false;
	private boolean sResizing=false;
	private boolean seResizing=false;
	private boolean resizing=false;
}





