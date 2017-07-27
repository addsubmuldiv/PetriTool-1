package PetriTool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageControl {
	PetriTool petriTool_;
	BufferedImage image;
	Vector<Place> placeVector_;
	Vector<Transition> transitionVector_;
	Vector<Token> tokenVector_; 
	Vector<Arc> arcVector_;
	
	public ImageControl(PetriTool petriTool,Vector<Place> placeVector_, Vector<Transition> transitionVector_,
			   Vector<Token> tokenVector_, Vector<Arc> arcVector_)
	{
		this.petriTool_=petriTool;
		this.tokenVector_=tokenVector_;
		this.transitionVector_=transitionVector_;
		this.arcVector_=arcVector_;
		this.placeVector_=placeVector_;
	}

	public void createImage(String fileLocation) 
	{
	    try 
	    {
	      FileOutputStream fos = new FileOutputStream(fileLocation);
	      BufferedOutputStream bos = new BufferedOutputStream(fos);
	      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
	      encoder.encode(image);
	      bos.close();
	    } 
	    catch (Exception e) 
	    {
	      e.printStackTrace();
	    }
	}
	
	public void DrawImage(String fileLocation)
	{
		image=new BufferedImage(petriTool_.gridWidth_*petriTool_.gridStep_, 
				petriTool_.gridHeight_*petriTool_.gridStep_,
				BufferedImage.TYPE_INT_RGB);
		Graphics gra = image.getGraphics();
	    Graphics2D g2 = (Graphics2D) gra;  
	    g2.setColor(Color.white);
	    g2.fillRect(0, 0, petriTool_.gridWidth_*petriTool_.gridStep_, 
	    		petriTool_.gridHeight_*petriTool_.gridStep_);
		int step__=petriTool_.gridStep_;
		Color foregroundColor__=petriTool_.foregroundColor_;
		
		// Draw the Places
		for (int i__ = 0; i__ < placeVector_.size(); i__++) {
		Place tempPlace__ = (Place) placeVector_.elementAt(i__);
			tempPlace__.draw(g2, step__, foregroundColor__,
					 petriTool_.placeLabels_,"");
		}

		// Draw the Transitions
		for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
			Transition tempTransition__ = (Transition) transitionVector_.
										elementAt(i__);
			tempTransition__.draw(g2, step__, foregroundColor__,
								  petriTool_.transitionLabels_,"");
		}

		// Draw the Tokens
		for (int i__ = 0; i__ < tokenVector_.size(); i__++) {
			Token tempToken__ = (Token) tokenVector_.elementAt(i__);
			tempToken__.draw(g2, step__, foregroundColor__);
		}

		// Draw the Arcs
		for (int i__ = 0; i__ < arcVector_.size(); i__++) {
			Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
			tempArc__.draw(g2, step__, foregroundColor__);
		}
		
		createImage(fileLocation);
	}
}
