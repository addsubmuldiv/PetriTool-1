//****************************************************************************// CLASS NAME:	Place.java//// AUTHOR:	Rick Brink//		rick@mail.csh.rit.edu//		http://www.csh.rit.edu/~rick//// VERSION:	1.0//// HISTORY:	4/16/96		Initial Version//// COPYRIGHT INFORMATION://// This program and the Java source is in the public domain.// Permission to use, copy, modify, and distribute this software// and its documentation for NON-COMMERCIAL purposes and// without fee is hereby granted.////    Copyright 1996////    Rick Brink//    1266 Brighton-Henrietta Townline Rd.//    Rochester, NY 14623//// DISCLAIMER://// The author claims no responsibility for any damage, direct or indirect,// to any harware or software as a result of using this program.//****************************************************************************package PetriTool;import java.io.PrintStream;import java.util.StringTokenizer;import java.util.Vector;import java.awt.Graphics;import java.awt.Color;import java.awt.Font;import org.dom4j.*;/**  * A class representing a Place in a Petri Net diagram.  *  * @see PetriComponent  * @see java.awt.Font  * @see java.awt.Graphics  * @see java.awt.Color  * @see java.io.PrintStream  * @see java.util.StringTokenizer  * @see java.util.Vector  *  * @version 1.0 July 3, 1996  *  * @author  Rick Brink**/class Place extends PetriComponent {	protected String placeName_=null;	/**Name's getter and setter**/	    public String getplaceName_() {		return placeName_;	}	public void setplaceName_(String name) {		this.placeName_ = name;	}	/** Unique numeric label for each Place object **/    protected int label_ = 0;    /** Pointer to a Token which may be contained by this Place **/    protected Token token_=null;    /** Indicates if token_ is a valid Token object **/    protected boolean tokenInitialized_ = false;    /**      * Construct a new Place, with coordinates of      * (x,y) which are the absolute coordinates divided by      * gridStep_.  Also note, all coordinates represent the      * upper left hand corner of a grid square.  A unique      * numeric identifier should also be passed into the constructor.    **/    public Place (int x, int y, int label) {        xCoordinate_ = x;        yCoordinate_ = y;        label_ = label;        placeName_="P"+label;    }    /**      * Construct a new Place without specifying any parameters.    **/    public Place () {    }    /**      * Create a copy of the current Place Object    **/    public Object clone() {        Place p = new Place(xCoordinate_, yCoordinate_, label_);        p.tokenInitialized_ = tokenInitialized_;        p.token_ = token_;        return p;    }    /**      * Set the unique numeric label for this Place    **/    public void setLabel(int label) {        label_ = label;    }    /**      * Get numeric label for this Place    **/    public int getLabel() {        return(label_);    }    /**      * Initializes the token_ object that this Place holds a pointer      * to.    **/    public void setToken (Token token) {        token_ = token;        if (token != null) {            tokenInitialized_ = true;        }        else {            tokenInitialized_ = false;        }    }    public Token getToken_() {		return token_;	}	/**      * Returns the number of Tokens the token_ object of this      * Place currently represents, else 0 if the token_ Object      * of this Place is not yet initialized.    **/    public int getNumTokens() {        if (tokenInitialized_) {            return (token_.getTokensRepresented());        }        else {            return (0);        }    }        public void setNonToken()    {    	token_=null;    	tokenInitialized_=false;    }    /**      * Add to the number of Tokens that this Place currently      * holds.  If it currently holds none, a new Token object is      * created with numTokens as the number of tokens the Token      * object represents, and this is added to tokenVector.      * Note also that this method can be used to      * delete from the number of Token by sending a negative value      * in for numTokens.  If this is the case, and all Tokens are      * removed, then the Token object is removed from tokenVector.    **/    public void addTokens(int numTokens, Vector tokenVector,                          int maxCapacity) {        if (tokenInitialized_) {            int numTokens__ = token_.getTokensRepresented() + numTokens;            numTokens__ = Math.min(maxCapacity, numTokens__);            token_.setTokensRepresented(numTokens__);            // if numTokens is actually a negative number, as in            // reverse steps, we could be removing all the tokens            // in this place, so set the appropriate values and            // remove the Token from tokenVector            if (numTokens__ == 0) {                tokenInitialized_ = false;                for (int i__ = 0; i__ < tokenVector.size(); i__++) {                    Token token__ = (Token)tokenVector.elementAt(i__);                    if ((token__.getXCoordinate() == xCoordinate_) &&                       (token__.getYCoordinate() == yCoordinate_)) {                        tokenVector.removeElementAt(i__);                        token_ = null;                    }                }            }        }        else {            numTokens = Math.min(maxCapacity, numTokens);            Token newToken__ = new Token(xCoordinate_, yCoordinate_,                numTokens);            token_ = newToken__;            tokenInitialized_ = true;            tokenVector.addElement(newToken__);        }    }    	private boolean theNameHasBeenEdited=false;    /**      * Draw the Place to the screen.    **/    public void draw(Graphics g, int gridStep, Color placeColor,                     boolean displayLabel) {        g.setColor(placeColor);        g.drawOval(xCoordinate_ * gridStep, yCoordinate_ * gridStep,                   gridStep,gridStep);        if (selected_) {            int x__ = xCoordinate_ * gridStep;            int y__ = yCoordinate_ * gridStep;            g.setColor(Color.green);            g.fillRect(x__ - 2, y__ - 2, SELECT_SIZE, SELECT_SIZE);            g.fillRect(x__ + gridStep - 2, y__ - 2, SELECT_SIZE, SELECT_SIZE);            g.fillRect(x__ - 2, y__ + gridStep - 2, SELECT_SIZE, SELECT_SIZE);            g.fillRect(x__ + gridStep - 2, y__ + gridStep - 2, SELECT_SIZE, SELECT_SIZE);        }        /**The label of place is not just a string, but there is a variable that controls the number of          * the place, so we add a string variable whose name is placeName to set its name,         * at first the placeName is null, and the label such as 'P0' will show in the screen,         * but if you has set the placeName, the placeName will show, and the label will disappear          * **///        if (displayLabel&&name==""&&theNameHasBeenEdited==false) {//            g.setColor(Color.red);//            Font labelFont__ = new Font("Dialog", Font.PLAIN, 12);//            g.setFont(labelFont__);//            g.drawString("P" + label_, xCoordinate_ * gridStep,//                         yCoordinate_ * gridStep);//        }        g.setColor(Color.red);        if(displayLabel)        	g.drawString(placeName_, xCoordinate_ * gridStep,                         	yCoordinate_ * gridStep);        }//    public void draw(Graphics g,int gridStep, Color placeColor, String name)//    {//    	//    }        public static Place loadFromDOM(Element place,int label,int gridStep)    {    	Place newplace=new Place();    	newplace.label_=label;    	int x=(int)Double.parseDouble(place.element("graphics").element("position").attribute("x").getText());    	int y=(int)Double.parseDouble(place.element("graphics").element("position").attribute("y").getText());    	newplace.setxCoordinate_(x/gridStep);    	newplace.setyCoordinate_(y/gridStep);    	newplace.setplaceName_(place.element("name").element("value").getText());    	String tokenNumStr=place.element("initialMarking").element("value").getText();    	int offsetOfTokenNum=tokenNumStr.indexOf(",");    	int tokenNum=Integer.parseInt(tokenNumStr.substring(offsetOfTokenNum+1));    	if(tokenNum!=0)    	{    		Token tempToken=new Token(x/gridStep, y/gridStep, tokenNum);    		newplace.setToken(tempToken);    	} 	    	return newplace;    }                /**      * Save the necessary Place information to a PrintStream    **/    public void saveToFile(PrintStream printStream) {        printStream.println("Place: " + xCoordinate_ + "," +                            yCoordinate_ + "," + label_ + "," + placeName_);    }                    public void passToDOM(Element net)    {    	Element place=net.addElement("place");    	place.addAttribute("id", placeName_);        	    	Element graphics=place.addElement("graphics");    	Element position=graphics.addElement("position");    	position.addAttribute("x", String.valueOf(xCoordinate_*PetriTool.gridStep_));    	position.addAttribute("y", String.valueOf(yCoordinate_*PetriTool.gridStep_));        	    	Element name=place.addElement("name");    	name.addElement("value").addText(placeName_);    	Element nameGraphics=name.addElement("graphics");    	nameGraphics.addElement("offset").addAttribute("x", "0.0").addAttribute("y", "0.0");    	    	    	    	Element initialMarking=place.addElement("initialMarking");    	if(token_!=null)    		initialMarking.addElement("value").addText("Default,"+token_.getTokensRepresented());    	else    		initialMarking.addElement("value").addText("Default,0");    	Element initialMarkingGraphics=initialMarking.addElement("graphics");    	initialMarkingGraphics.addElement("offset").addAttribute("x", "0.0").addAttribute("y", "0.0");    	    	Element capacity=place.addElement("capacity");    	capacity.addElement("value").addText("0");    }                        /**      * Given a StringTokenizer, set the Place values.  Useful      * for restoring a Place from a file.    **/    public void loadFromFile(StringTokenizer tokenizer) {        xCoordinate_ = Integer.parseInt(tokenizer.nextToken());        yCoordinate_ = Integer.parseInt(tokenizer.nextToken());        label_ = Integer.parseInt(tokenizer.nextToken());        placeName_ = tokenizer.nextToken();        return;    }    }