
//****************************************************************************
// CLASS NAME:	Arc.java
//
// AUTHOR:	Rick Brink
//		    rick@mail.csh.rit.edu
//		    http://www.csh.rit.edu/~rick
//
// VERSION:	1.0
//
// HISTORY:	4/16/96		Initial Version
//         10/06/96     Revised 2 point arc to multi-point arc
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

import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;

import org.dom4j.Element;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.awt.Color;
import java.awt.Graphics;

/**
  * A class representing an Arc in a Petri Net diagram.
  *
  * @see PetriComponent
  * @see java.io.PrintStream
  * @see java.util.StringTokenizer
  * @see java.util.Vector
  * @see java.util.NoSuchElementException
  * @see java.awt.Color
  * @see java.awt.Graphics
  *
  * @version 1.0 July 3, 1996
  *
  * @author  Rick Brink
**/
class Arc extends PetriComponent {
    /**
      * Vector of x coordinates which define the Arc.
    **/
    protected Vector xCoordinateVector_;

    /**
      * Vector of y coordinates which define the Arc.
    **/
    protected Vector yCoordinateVector_;

    /**
      * Set of offsets for each x coordinate to describe
      * where the point should be drawn with repect
      * to the actual coordinate which is the upper left
      * corner of a grid square.
    **/
    protected Vector xDrawCoordinateVector_;

    /**
      * Set of offsets for each y coordinate to describe
      * where the point should be drawn with repect
      * to the actual coordinate which is the upper left
      * corner of a grid square.
    **/
    protected Vector yDrawCoordinateVector_;

    /** X coordinates describing arrow head polygon **/
    protected double[] xPoints_ = new double[3];

    /** Y coordinates describing arrow head polygon **/
    protected double[] yPoints_ = new double[3];

    /**
      * Integer of 1,2,3, or 4 describing whether the Arc is to
      * be drawn on the left, top, right, or bottom of the
      * Place or Transition.
    **/
    protected int region_;

    /** The slope of the Arc **/
    protected Vector slopeVector_;

    /** The number of Tokens needed to enable this Arc **/
    protected int tokensToEnable_;

    
    private Place startPlace_;
    
    
    /** Handle to the Place that this Arc goes to, or null **/
    protected Place destinationPlace_;

    
    private Transition startTransition_;
    
    /** Handle to the Transition that this Arc goes to, or null **/
    protected Transition destinationTransition_;

    /**
      * Tells which of destinationPlace_ or destinationTransition_
      * should be valid during simulation.
    **/
    protected boolean destinationIsPlace_ = false;

    /** Size of the arrow head **/
    protected final double ARROW_SIZE = 5.0;

    /** Number of pixels defining how large the bounding box
      * is surrounding the Arc when determining whether or not
      * a mouseDown event selects the Arc
    **/
    protected final int BOUND_SIZE = 10;

    /** Arrow head to be draw on left **/
    protected final int REGION_1 = 1;

    /** Arrow head to be draw on top **/
    protected final int REGION_2 = 2;

    /** Arrow head to be draw on right **/
    protected final int REGION_3 = 3;

    /** Arrow head to be draw on bottom **/
    protected final int REGION_4 = 4;

    /** Indicates if the Arc starts at a Place or Transition **/
    protected String startComponent_ = "";

    /** Indicates if the Arc ends at a Place or Transition **/
    protected String endComponent_ = "";


    /**
      * Construct a new Arc, with starting coordinates of
      * (x,y) which are the absolute coordinates divided by
      * gridStep_.  Also note, all coordinates represent the
      * upper left hand corner of a grid square.
      *
      * @param x The X coordinate of the starting point of the Arc
      * @param y The Y coordinate of the starting point of the Arc
    **/
    public Arc (int x, int y) {
        xCoordinateVector_ = new Vector();
        yCoordinateVector_ = new Vector();
        xDrawCoordinateVector_ = new Vector();
        yDrawCoordinateVector_ = new Vector();
        slopeVector_ = new Vector();

        try {
            xCoordinateVector_.insertElementAt(new Integer (x), 0);
            yCoordinateVector_.insertElementAt(new Integer (y), 0);
        }
        catch (ArrayIndexOutOfBoundsException e) {
           System.out.println("Error - " + e);
        }
    }

    /**
      * Construct a new Arc without specifying any parameters.
    **/
    public Arc() {
    }

    /**
      * Create a copy of the current Arc Object
    **/
    public Object clone() {
        Arc a = new Arc();
        a.xCoordinateVector_ = (Vector) xCoordinateVector_.clone();
        a.yCoordinateVector_ = (Vector) yCoordinateVector_.clone();
        a.xDrawCoordinateVector_ = (Vector) xDrawCoordinateVector_.clone();
        a.yDrawCoordinateVector_ = (Vector) yDrawCoordinateVector_.clone();
        a.slopeVector_ = (Vector) slopeVector_.clone();
        a.region_ = region_;
        a.setArrowHead(region_);
        a.setTokensToEnable(tokensToEnable_);
        return a;
    }

    /**
      * Add an (x,y) coordinate to xCoordinateVector_ and yCoordinateVector_
    **/
    public void addCoordinates(int x, int y) {
        int index__ = xCoordinateVector_.size() ;
        xCoordinateVector_.insertElementAt(new Integer(x), index__);
        yCoordinateVector_.insertElementAt(new Integer(y), index__);
        calculateSlopes();
        setArcDrawCoordinates();
    }

    /**
      * Get the value of the starting X coordinate.
    **/
    public int getFirstXCoordinate() {
        int firstX__ = 0;
        try {
            firstX__ = ((Integer)xCoordinateVector_.firstElement()).intValue();
        }
        catch (NoSuchElementException e) {
            System.out.println("Error - " + e);
        }

        return (firstX__);
    }
    
    
    private final int START=0;
  //  private final int END=1;
    /**
     * ������ʼ������ 
     * @param x
     * @param y
     */
    public void setFirstCoordinate(int x,int y)
    {
    	try{
    		xCoordinateVector_.setElementAt(new Integer(x), START);
    		yCoordinateVector_.setElementAt(new Integer(y), START);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     * ������ֹ�������
     * @param x
     * @param y
     */
    public void setEndCoordinate(int x,int y)
    {
    	try{
    		xCoordinateVector_.setElementAt(new Integer(x), xCoordinateVector_.size()-1);
    		yCoordinateVector_.setElementAt(new Integer(y), yCoordinateVector_.size()-1);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    /**
      * Get the value of the minimum X coordinate.
    **/
    public int getMinXCoordinate() {
        int xTemp__;
        int xMin__;
        xMin__ = ((Integer)xCoordinateVector_.firstElement()).intValue();

        for (int i__ = 0; i__ < xCoordinateVector_.size(); i__++) {
            xTemp__ = ((Integer)xCoordinateVector_.elementAt(i__)).intValue();
            if (xTemp__ < xMin__) {
                xMin__ = xTemp__;
            }
        }

        return (xMin__);
    }

    /**
      * Get the value of the minimum X coordinate.
    **/
    public int getMinYCoordinate() {
        int yTemp__;
        int yMin__;
        yMin__ = ((Integer)yCoordinateVector_.firstElement()).intValue();

        for (int i__ = 0; i__ < yCoordinateVector_.size(); i__++) {
            yTemp__ = ((Integer)yCoordinateVector_.elementAt(i__)).intValue();
            if (yTemp__ < yMin__) {
                yMin__ = yTemp__;
            }
        }

        return (yMin__);
    }


    /**
      * Get the value of the starting Y coordinate.
    **/
    public int getFirstYCoordinate() {
        int firstY__ = 0;
        try {
            firstY__ = ((Integer)yCoordinateVector_.firstElement()).intValue();
        }
        catch (NoSuchElementException e) {
            System.out.println("Error - " + e);
        }

        return (firstY__);
    }

    /**
      * Get the value of the ending X coordinate.
    **/
    public int getLastXCoordinate() {
        int lastX__ = 0;
        try {
            lastX__ = ((Integer)xCoordinateVector_.lastElement()).intValue();
        }
        catch (NoSuchElementException e) {
            System.out.println("Error - " + e);
        }

        return (lastX__);
    }

    /**
      * Get the value of the ending Y coordinate.
    **/
    public int getLastYCoordinate() {
        int lastY__ = 0;
        try {
            lastY__ = ((Integer)yCoordinateVector_.lastElement()).intValue();
        }
        catch (NoSuchElementException e) {
            System.out.println("Error - " + e);
        }

        return (lastY__);
    }

    /**
      * Set the values of the staring and ending X and Y coordinates.
    **/
    public void translateCoordinates(int xTranslate, int yTranslate) {
        int i__;
        Integer xInteger__;
        Integer yInteger__;
        int x__;
        int y__;

        for (i__ = 0; i__ < xCoordinateVector_.size(); i__++) {
            xInteger__ = (Integer)xCoordinateVector_.elementAt(i__);
            x__ = xInteger__.intValue() + xTranslate;
            try {
                xCoordinateVector_.setElementAt(new Integer(x__), i__);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error - " + e);
            }
            xInteger__ = null;
        }

        for (i__ = 0; i__ < yCoordinateVector_.size(); i__++) {
            yInteger__ = (Integer)yCoordinateVector_.elementAt(i__);
            y__ = yInteger__.intValue() + yTranslate;
            try {
                yCoordinateVector_.setElementAt(new Integer(y__), i__);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error - " + e);
            }
            yInteger__ = null;
        }

    }


    /**
      * Set the number of tokens needed to enable this Arc
    **/
    public void setTokensToEnable(int numTokens) {
        tokensToEnable_ = numTokens;
    }

    /**
      * Get the number of tokens needed to enable this Arc
    **/
    public int getTokensToEnable() {
        return(tokensToEnable_);
    }

    /**
      * Set the type of starting component. Must be "Place" or
      * "Transition".
    **/
    public void setStartComponent(String start) {
        startComponent_ = start;
    }

    /**
      * Set the type of ending component. Must be "Place" or
      * "Transition".
    **/
    public void setEndComponent(String end) {
        endComponent_ = end;
    }

    /**
      * Set the type of starting component. Must be "Place" or
      * "Transition".
    **/
    public String getStartComponent() {
        return(startComponent_);
    }

    /**
      * Set the type of ending component. Must be "Place" or
      * "Transition".
    **/
    public String getEndComponent() {
        return(endComponent_);
    }

    /**
      * Save the necessary Arc information to a PrintStream
    **/
    public void saveToFile(PrintStream printStream) {
        printStream.print("Arc: " + xCoordinateVector_.size() + ",");

        for (int i__ = 0; i__ < xCoordinateVector_.size(); i__++) {
            printStream.print((Integer)xCoordinateVector_.
                              elementAt(i__) + ",");
            printStream.print((Integer)yCoordinateVector_.
                              elementAt(i__) + ",");
        }

        for (int i__ = 0; i__ < xDrawCoordinateVector_.size(); i__++) {
            printStream.print((Double)xDrawCoordinateVector_.
                              elementAt(i__) + ",");
            printStream.print((Double)yDrawCoordinateVector_.
                              elementAt(i__) + ",");
        }

        printStream.println(region_ + "," +
                            tokensToEnable_ + "," +
                            startComponent_);
    }

    
    public void passToDOM(Element net)
    {
    	Element arc=net.addElement("arc");
    	if(startComponent_.equals("Place"))
    	{
    		if(destinationTransition_==null)
    			return;
    		arc.addAttribute("id", "P"+startPlace_.label_+" to T"+destinationTransition_.label_);
    		arc.addAttribute("source", startPlace_.getplaceName_());
    		arc.addAttribute("target", destinationTransition_.getTransitionName_());
    	}
    	else
    	{
    		if(destinationPlace_==null)
    			return;
    		arc.addAttribute("id", "T"+startTransition_.label_+ " to P"+destinationPlace_.label_);
    		arc.addAttribute("source", startTransition_.getTransitionName_());
    		arc.addAttribute("target", destinationPlace_.getplaceName_());
    	}
    	
    	Element inscription=arc.addElement("inscription");
    	inscription.addElement("value").addText("Default,"+tokensToEnable_);
    	Element tagged=arc.addElement("tagged");
    	tagged.addElement("value").addText("false");
    	
    	for(int i=0;i<xCoordinateVector_.size();i++)
    	{
    		Element arcpath=arc.addElement("arcpath");
    		String arcID="";
    		if(i/10==0)
    			arcID="00"+i;
    		else if(i/100==0)
    			arcID="0"+i;
    		else if(i/1000==0)
    			arcID=String.valueOf(i);	
    		arcpath.addAttribute("id", arcID);
    		arcpath.addAttribute("x", String.valueOf(((Integer)xCoordinateVector_.get(i)).intValue()*PetriTool.gridStep_));
    		arcpath.addAttribute("y", String.valueOf(((Integer)yCoordinateVector_.get(i)).intValue()*PetriTool.gridStep_));
    		arcpath.addAttribute("curvePoint", "false");
    	}
    	arc.addElement("type").addAttribute("value", "normal");

    }
    
    
    
    
    
    public void setStartPlace_(Place startPlace_) {
		this.startPlace_ = startPlace_;
	}

	public void setStartTransition_(Transition startTransition_) {
		this.startTransition_ = startTransition_;
	}

	public void setDestinationPlace_(Place destinationPlace_) {
		this.destinationPlace_ = destinationPlace_;
	}

	public void setDestinationTransition_(Transition destinationTransition_) {
		this.destinationTransition_ = destinationTransition_;
	}

	/**
      * Draw the Arc to the screen.
    **/
    public void draw (Graphics g, int gridStep, Color arcColor) {
        double arrowSize__ = (double)gridStep / 4.0;
        int x2DrawTemp__ = 0;
        int y2DrawTemp__ = 0;
        int x1Draw__ = 0;
        int y1Draw__ = 0;
        int x2Draw__ = 0;
        int y2Draw__ = 0;

        // Draw the actual line of the arc
        g.setColor(arcColor);

        for (int i__ = 0; i__ < xCoordinateVector_.size() - 1; i__++) {
            x1Draw__ = (int) (((Double)xDrawCoordinateVector_.elementAt(i__))
                               .doubleValue() * (double)gridStep) +
                             ((Integer)xCoordinateVector_.elementAt(i__))
                               .intValue() * gridStep;
            y1Draw__ = (int)(((Double)yDrawCoordinateVector_.elementAt(i__))
                               .doubleValue() * (double)gridStep) +
                            ((Integer)yCoordinateVector_.elementAt(i__))
                               .intValue() * gridStep;
            x2Draw__ = (int)(((Double)xDrawCoordinateVector_.elementAt(i__+1))
                               .doubleValue() * (double)gridStep) +
                            ((Integer)xCoordinateVector_.elementAt(i__+1))
                               .intValue() * gridStep;
            y2Draw__ = (int)(((Double)yDrawCoordinateVector_.elementAt(i__+1))
                               .doubleValue() * (double)gridStep) +
                            ((Integer)yCoordinateVector_.elementAt(i__+1))
                               .intValue() * gridStep;

            if (i__ == xCoordinateVector_.size() - 2) {
                x2DrawTemp__ = translate(x2Draw__, true, (int) arrowSize__);
                y2DrawTemp__ = translate(y2Draw__, false, (int) arrowSize__);
            }
            else {
                x2DrawTemp__ = x2Draw__;
                y2DrawTemp__ = y2Draw__;
            }


            g.drawLine(x1Draw__, y1Draw__, x2DrawTemp__, y2DrawTemp__);

            if (selected_) {
                g.setColor(Color.green);
                g.fillRect(x1Draw__ - 2, y1Draw__ - 2, SELECT_SIZE, SELECT_SIZE);
                g.fillRect(x2Draw__ - 2, y2Draw__ - 2, SELECT_SIZE, SELECT_SIZE);
                g.setColor(arcColor);
            }
        }

        // Draw the triangular shaped arrow head pointer of the arc
        int xPoints__[] = new int[3];
        int yPoints__[] = new int[3];
        xPoints__[0] = x2Draw__;
        yPoints__[0] = y2Draw__;

        for (int j__ = 1; j__ < 3; j__++) {
            xPoints__[j__] = x2Draw__ + (int)(xPoints_[j__] * arrowSize__);
            yPoints__[j__] = y2Draw__ + (int)(yPoints_[j__] * arrowSize__);
        }

        g.fillPolygon(xPoints__, yPoints__, 3);


        // Calculate an x,y pair for the middle of the arc
        // Then draw a slash line and number of tokens it takes
        // to enable this arc
        g.setColor(arcColor);
        int tempX__ = Math.abs(x1Draw__ - x2DrawTemp__) / 2 +
                      Math.min(x1Draw__, x2DrawTemp__);
        int tempY__ = Math.abs(y1Draw__ - y2DrawTemp__) / 2 +
                      Math.min(y1Draw__, y2DrawTemp__);

        switch (region_) {
            case 1:
            case 3: g.drawLine(tempX__, tempY__ - 4,
                               tempX__, tempY__ + 4);
                    if(tokensToEnable_!=1)
                    	g.drawString(String.valueOf(tokensToEnable_), tempX__ - 2, tempY__ - 6);
                    break;
            case 2:
            case 4: g.drawLine(tempX__ - 4, tempY__,
                               tempX__ + 4, tempY__);
                    if(tokensToEnable_!=1)
                    	g.drawString(String.valueOf(tokensToEnable_), tempX__ + 6, tempY__ - 2);
                    break;
        }
    }

    /**
      * Inform the Arc of a mouseDown event so that the Arc may
      * determine if the mouseDown occurred somewhere along its
      * length, and if so, set the Arc as selected.
      * 
      * 
      * 
      * 
      * If you want to get the arc that you clicked, you can traverse all arcs, and call their 
      * 'mouseDonwAt', if the method returns true, then it is the arc you need
    **/
    public boolean mouseDownAt(int xClick, int yClick, int gridStep) {
        int x1Draw__;
        int y1Draw__;
        int x2Draw__;
        int y2Draw__;
        int x1Temp__;
        int x2Temp__;
        int y1Temp__;
        int y2Temp__;
        int x3Temp__;
        int y3Temp__;
        double zTemp__;
        double slope__;
        boolean done__ = false;
        boolean theArcClicked__=false;
        for (int i__ = 0; i__ < xCoordinateVector_.size() - 1; i__++) {
            slope__ = ((Double)slopeVector_.elementAt(i__)).doubleValue();
            x1Draw__ = (int) (((Double)xDrawCoordinateVector_.elementAt(i__))
                               .doubleValue() * (double)gridStep) +
                             ((Integer)xCoordinateVector_.elementAt(i__))
                               .intValue() * gridStep;
            y1Draw__ = (int)(((Double)yDrawCoordinateVector_.elementAt(i__))
                               .doubleValue() * (double)gridStep) +
                            ((Integer)yCoordinateVector_.elementAt(i__))
                               .intValue() * gridStep;
            x2Draw__ = (int)(((Double)xDrawCoordinateVector_.elementAt(i__+1))
                               .doubleValue() * (double)gridStep) +
                            ((Integer)xCoordinateVector_.elementAt(i__+1))
                               .intValue() * gridStep;
            y2Draw__ = (int)(((Double)yDrawCoordinateVector_.elementAt(i__+1))
                               .doubleValue() * (double)gridStep) +
                            ((Integer)yCoordinateVector_.elementAt(i__+1))
                               .intValue() * gridStep;


            if (slope__ == 0.0) {
                if (x1Draw__ < x2Draw__) {
                    x1Temp__ = x1Draw__;
                    x2Temp__ = x2Draw__;
                }
                else {
                    x1Temp__ = x2Draw__;
                    x2Temp__ = x1Draw__;
                }
                y1Temp__ = y1Draw__;
                y2Temp__ = y2Draw__;

                if ((xClick >= x1Temp__) && (xClick <= x2Temp__)) {
                    if ((yClick >= y1Temp__ - BOUND_SIZE) &&
                        (yClick <= y1Temp__ + BOUND_SIZE)) {
                    toggleSelected();
                    theArcClicked__=true;
                    }
                }
            }
            else if ((Math.abs(slope__) == Double.POSITIVE_INFINITY) ||
                    (Math.abs(slope__) == Double.NEGATIVE_INFINITY)) {
                if (y1Draw__ < y2Draw__) {
                    y1Temp__ = y1Draw__;
                    y2Temp__ = y2Draw__;
                }
                else {
                    y1Temp__ = y2Draw__;
                    y2Temp__ = y1Draw__;
                }
                x1Temp__ = x1Draw__;
                x2Temp__ = x2Draw__;

                if ((yClick >= y1Temp__) && (yClick <= y2Temp__)) {
                    if ((xClick >= x1Temp__ - BOUND_SIZE) &&
                        (xClick <= x1Temp__ + BOUND_SIZE)) {
                    toggleSelected();
                    theArcClicked__=true;
                    }
                }
            }
            else {
                if (x1Draw__ < x2Draw__) {
                    x1Temp__ = x1Draw__;
                    x2Temp__ = x2Draw__;
                    y1Temp__ = y1Draw__;
                    y2Temp__ = y2Draw__;
                }
                else {
                    x1Temp__ = x2Draw__;
                    x2Temp__ = x1Draw__;
                    y1Temp__ = y2Draw__;
                    y2Temp__ = y1Draw__;
                }
                x3Temp__ = x1Temp__;
                y3Temp__ = y1Temp__;

                do {
                    if ((xClick >= x3Temp__ - BOUND_SIZE) &&
                        (xClick <= x3Temp__ + BOUND_SIZE)) {
                            if ((yClick >= y3Temp__ - BOUND_SIZE) &&
                                (yClick <= y3Temp__ + BOUND_SIZE)) {
                                    toggleSelected();
                                    theArcClicked__=true;
                                    done__ = true;
                            }
                    }

                    x3Temp__ += 1;
                    zTemp__ = (double) y1Temp__ - slope__ * (double)(x1Temp__ - x3Temp__);
                    if (slope__ < 0.0) {
                        while (y3Temp__ > zTemp__) {
                            y3Temp__--;
                        }
                    }
                    else {
                        while (y3Temp__ < zTemp__) {
                            y3Temp__++;
                        }
                    }
                } while ((x3Temp__ < x2Temp__) && (!done__)) ;
            }
        }
        return theArcClicked__;
    }

    /**
      * Given a StringTokenizer, set the Arc values.  Useful
      * for restoring an Arc from a file.
    **/
    public void loadFromFile(StringTokenizer tokenizer) {
        xCoordinateVector_ = new Vector();
        yCoordinateVector_ = new Vector();
        xDrawCoordinateVector_ = new Vector();
        yDrawCoordinateVector_ = new Vector();
        slopeVector_ = new Vector();

        int numCoordinates__ = Integer.parseInt(tokenizer.nextToken());

        for (int i__ = 0; i__ < numCoordinates__; i__++) {
            xCoordinateVector_.insertElementAt(
                new Integer (tokenizer.nextToken()), i__);
            yCoordinateVector_.insertElementAt(
                new Integer (tokenizer.nextToken()), i__);
        }

        for (int i__ = 0; i__ < numCoordinates__; i__++) {
            xDrawCoordinateVector_.insertElementAt(
                new Double (tokenizer.nextToken()), i__);
            yDrawCoordinateVector_.insertElementAt(
                new Double (tokenizer.nextToken()), i__);
        }

        setArrowHead(Integer.parseInt(tokenizer.nextToken()));
        tokensToEnable_ = Integer.parseInt(tokenizer.nextToken());
        startComponent_ = tokenizer.nextToken();

        if (startComponent_.equals("Place")) {
            endComponent_ = "Transition";
        }
        else {
            endComponent_ = "Place";
        }

        calculateSlopes();
        setArcDrawCoordinates();
        return;
    }

    
    
    public static Arc loadFromXML(Element arc,DesignPanel designPanel)
    {
    	Arc newArc=new Arc();
    	newArc.xCoordinateVector_=new Vector<>();
    	newArc.yCoordinateVector_=new Vector<>();
    	newArc.xDrawCoordinateVector_=new Vector<>();
    	newArc.yDrawCoordinateVector_=new Vector<>();
    	newArc.slopeVector_=new Vector<>();
    	String arcID=arc.attribute("id").getText();
    	if(arcID.startsWith("P"))
    	{	
    		newArc.setStartComponent("Place");
    		newArc.setEndComponent("Transition");
    	}
    	else
    	{
    		newArc.setStartComponent("Transition");
    		newArc.setEndComponent("Place");
    		
    	}
    	
    	String source=arc.attribute("source").getText();
    	String target=arc.attribute("target").getText();
    	
    	if(newArc.startComponent_.equals("Place"))
    	{
    		for(Iterator<Place> iterator=designPanel.placeVector_.iterator();iterator.hasNext();)
    		{
    			Place tempPlace=iterator.next();
    			if(tempPlace.placeName_.equals(source))
    			{
    				newArc.setStartPlace_(tempPlace);
    			}
    		}
    		for(Iterator<Transition> iterator=designPanel.transitionVector_.iterator();iterator.hasNext();)
    		{
    			Transition tempTransition=iterator.next();
    			if(tempTransition.transitionName_.equals(target))
    			{
    				newArc.setDestinationTransition_(tempTransition);
    			}
    		}
    	}
    	else
    	{
    		for(Iterator<Transition> iterator=designPanel.transitionVector_.iterator();iterator.hasNext();)
    		{
    			Transition tempTransition=iterator.next();
    			if(tempTransition.transitionName_.equals(source))
    			{
    				newArc.setStartTransition_(tempTransition);
    			}
    		}
    		for(Iterator<Place> iterator=designPanel.placeVector_.iterator();iterator.hasNext();)
    		{
    			Place tempPlace=iterator.next();
    			if(tempPlace.placeName_.equals(target))
    			{
    				newArc.setDestinationPlace_(tempPlace);
    			}
    		}
    	}
    	String arcNumStr=arc.element("inscription").element("value").getText();
    	int offsetOfArcNum=arcNumStr.indexOf(",");
    	int arcNum=Integer.parseInt(arcNumStr.substring(offsetOfArcNum+1));
    	newArc.setTokensToEnable(arcNum);
    	
    	List<Element> arcPaths=arc.elements("arcpath");
    	boolean loadFromPipe=false;
    	for(Iterator<Element> iterator=arcPaths.iterator();iterator.hasNext();)
    	{
    		Element arcPath=iterator.next();
    		int x=Integer.parseInt(arcPath.attribute("x").getText());
    		int y=Integer.parseInt(arcPath.attribute("y").getText());
    		newArc.xCoordinateVector_.addElement(x/designPanel.petriTool_.gridStep_);
    		newArc.yCoordinateVector_.addElement(y/designPanel.petriTool_.gridStep_);
    		if(x%designPanel.petriTool_.gridStep_!=0)
    			loadFromPipe=true;
    	}
    	if(loadFromPipe)
		{
    		if(newArc.startComponent_.equals("Place"))
			{
				newArc.xCoordinateVector_.set(0, newArc.startPlace_.xCoordinate_);
				newArc.yCoordinateVector_.set(0, newArc.startPlace_.yCoordinate_);
				newArc.xCoordinateVector_.set(newArc.xCoordinateVector_.size()-1, newArc.destinationTransition_.xCoordinate_);
				newArc.yCoordinateVector_.set(newArc.yCoordinateVector_.size()-1, newArc.destinationTransition_.yCoordinate_);
			}
			else
			{
				newArc.xCoordinateVector_.set(0, newArc.startTransition_.xCoordinate_);
				newArc.yCoordinateVector_.set(0, newArc.startTransition_.yCoordinate_);
				newArc.xCoordinateVector_.set(newArc.xCoordinateVector_.size()-1, newArc.destinationPlace_.xCoordinate_);
				newArc.yCoordinateVector_.set(newArc.yCoordinateVector_.size()-1, newArc.destinationPlace_.yCoordinate_);
			}
		}
    	
    	
    	newArc.calculateSlopes();
        newArc.setArcDrawCoordinates();
    	return newArc;
    }
    
    
    
    
    
    
    
    
    
    
    

    /**
      * Depending on the slope of the Arc, set the starting and
      * ending Draw coordinates of the arc.  Also set the
      * arrow head of the arc appropriately from the four possible
      * types of arrow heads.
    **/
    public void setArcDrawCoordinates() {
        double slope__;
        xDrawCoordinateVector_.removeAllElements();
        yDrawCoordinateVector_.removeAllElements();
        // Initialize DrawCoordinateVectors
        for (int i__ = 0; i__ <= slopeVector_.size(); i__++) {
            xDrawCoordinateVector_.insertElementAt(new Double(0), i__);
            yDrawCoordinateVector_.insertElementAt(new Double(0), i__);
        }
        for (int i__ = 0; i__ < slopeVector_.size(); i__ ++) {
            slope__ = ((Double) slopeVector_.elementAt(i__)).doubleValue();
            int x1__ = ((Integer) xCoordinateVector_.elementAt(i__)).intValue();
            int y1__ = ((Integer) yCoordinateVector_.elementAt(i__)).intValue();
            int x2__ = ((Integer) xCoordinateVector_.elementAt(i__+1)).intValue();
            int y2__ = ((Integer) yCoordinateVector_.elementAt(i__+1)).intValue();
            if (i__ == 0) {
                if (Math.abs(slope__) < 1) {
                    if (x1__ > x2__) {
                        xDrawCoordinateVector_.setElementAt(
                            new Double (0), 0);
                        yDrawCoordinateVector_.setElementAt(
                            new Double (0.5), 0);
                    }
                    else {
                        xDrawCoordinateVector_.setElementAt(
                            new Double (1), 0);
                        yDrawCoordinateVector_.setElementAt(
                            new Double (0.5), 0);
                    }
                }
                else { // Math.abs(slope__) > 1
                    if (y1__ > y2__) {
                        if (startComponent_.equals("Place")) {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), 0);
                            yDrawCoordinateVector_.setElementAt(
                                new Double (0), 0);
                        }
                        else {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), 0);
                            yDrawCoordinateVector_.setElementAt(
                                new Double (0.25), 0);
                        }
                    }
                    else {
                        if (startComponent_.equals("Place")) {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), 0);
                            yDrawCoordinateVector_.setElementAt(
                                new Double (1), 0);
                        }
                        else {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), 0);
                            yDrawCoordinateVector_.setElementAt(
                                new Double (0.75), 0);
                        }
                    }
                }
                xDrawCoordinateVector_.setElementAt(new Double (0.5), 1);
                yDrawCoordinateVector_.setElementAt(new Double (0.5), 1);
            }
            if (i__ == slopeVector_.size() - 1) {
                if (Math.abs(slope__) < 1) {
                    if (x1__ > x2__) {
                        xDrawCoordinateVector_.setElementAt(
                            new Double (1), slopeVector_.size());
                        yDrawCoordinateVector_.setElementAt(
                            new Double (0.5), slopeVector_.size());
                        setArrowHead(REGION_1);
                    }
                    else {
                        xDrawCoordinateVector_.setElementAt(
                            new Double (0), slopeVector_.size());
                        yDrawCoordinateVector_.setElementAt(
                            new Double (0.5), slopeVector_.size());
                        setArrowHead(REGION_3);
                    }
                }
                else { // Math.abs(arcSlope_) > 1
                    if (y1__ > y2__) {
                        if (endComponent_.equals("Place")) {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), slopeVector_.size());
                            yDrawCoordinateVector_.setElementAt(
                                new Double (1), slopeVector_.size());
                        }
                        else {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), slopeVector_.size());
                            yDrawCoordinateVector_.setElementAt(
                                new Double (0.75), slopeVector_.size());
                        }
                        setArrowHead(REGION_4);
                    }
                    else {
                        if (endComponent_.equals("Place")) {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), slopeVector_.size());
                            yDrawCoordinateVector_.setElementAt(
                                new Double (0), slopeVector_.size());
                        }
                        else {
                            xDrawCoordinateVector_.setElementAt(
                                new Double (0.5), slopeVector_.size());
                            yDrawCoordinateVector_.setElementAt(
                                new Double (0.25), slopeVector_.size());
                        }
                        setArrowHead(REGION_2);
                    }
                }
            }
            else {
                xDrawCoordinateVector_.setElementAt(new Double (0.5), i__+1);
                yDrawCoordinateVector_.setElementAt(new Double (0.5), i__+1);
            }
        }

        if (endComponent_.equals("")) {
            xDrawCoordinateVector_.setElementAt(new Double (0.5),
                                                slopeVector_.size());
            yDrawCoordinateVector_.setElementAt(new Double (0.5),
                                                slopeVector_.size());
        }
    }

    /**
      * Set the xPoints_ and yPoints_ arrays to define the
      * arrow head, based on which region_ the Arc ends in.
    **/
    public void setArrowHead(int region) {
        region_ = region;
        xPoints_[0] = ((Double) xDrawCoordinateVector_.lastElement()).
                        doubleValue();
        yPoints_[0] = ((Double) yDrawCoordinateVector_.lastElement()).
                        doubleValue();

        switch (region) {
            case 1: xPoints_[1] = 1;
                    xPoints_[2] = 1;
                    yPoints_[1] = -1;
                    yPoints_[2] = 1;
                    break;
            case 2: xPoints_[1] = -1;
                    xPoints_[2] = 1;
                    yPoints_[1] = -1;
                    yPoints_[2] = -1;
                    break;
            case 3: xPoints_[1] = -1;
                    xPoints_[2] = -1;
                    yPoints_[1] = -1;
                    yPoints_[2] = 1;
                    break;
            case 4: xPoints_[1] = -1;
                    xPoints_[2] = 1;
                    yPoints_[1] = 1;
                    yPoints_[2] = 1;
                    break;
        }
    }

    /**
      * Calculate the slopes of the different lengths of the Arc.
    **/
    public void calculateSlopes() {
        double slope__;
        int x1__;
        int x2__;
        int y1__;
        int y2__;

        slopeVector_.removeAllElements();
        // Assume there are at least two coordinates, and that there
        // are the same number of x and y coordinates, which should always
        // be true for a valid Arc.
        for (int i__ = 0; i__ < xCoordinateVector_.size() - 1; i__++) {
            x1__ = ((Integer)xCoordinateVector_.elementAt(i__)).intValue();
            x2__ = ((Integer)xCoordinateVector_.elementAt(i__+1)).intValue();
            y1__ = ((Integer)yCoordinateVector_.elementAt(i__)).intValue();
            y2__ = ((Integer)yCoordinateVector_.elementAt(i__+1)).intValue();
            slope__ = (double) (y2__ - y1__) / (double) (x2__ - x1__);
            slopeVector_.insertElementAt(new Double(slope__), i__);
        }
    }


    /**
      * Translate a coordinate based on a region_ and whether
      * the coordinate is an X or Y coordinate.
    **/
    public int translate(int coordinate, boolean isX, int translation) {
        if (isX) {
            if (region_ == 1) {
                return (coordinate + translation);
            }
            else if (region_ == 3) {
                return (coordinate - translation);
            }
        }
        else {
            if (region_ == 2) {
                return (coordinate - translation);
            }
            else if (region_ == 4) {
                return (coordinate + translation);
            }
        }
        return (coordinate);
    }

    /**
      * Sets the destination object and destinationIsPlace_ accordingly
    **/
//    public void setDestination(String destination, PetriComponent
//                               destinationObject) {
//        if (destination.equals("Place")) {
//            destinationIsPlace_ = true;
//            destinationPlace_ = (Place) destinationObject;
//        }
//        else { // destination must be Transition
//            destinationIsPlace_ = false;
//            destinationTransition_ = (Transition) destinationObject;
//        }
//    }

}
