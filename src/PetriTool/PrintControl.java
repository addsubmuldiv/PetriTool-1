package PetriTool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.smartcardio.ResponseAPDU;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PrintControl  implements Printable{
   /**
   * @param Graphic show the printed graphics environment
   * @param PageFormatIndicate the printed page format (page size to point for measuring unit, 1/72 of the 1 to 1, 
   *                                                     1 inch is 25.4 mm. A4 paper roughly 595 x 842)
   * @param pageIndex the page number
   **/
   
  // Graphics tempg;  

	PetriTool petriTool_;
	Vector<Place> placeVector_;
	Vector<Transition> transitionVector_;
	Vector<Token> tokenVector_; 
	Vector<Arc> arcVector_;
	
   public PrintControl(PetriTool petriTool_, Vector<Place> placeVector_, Vector<Transition> transitionVector_,
		   Vector<Token> tokenVector_, Vector<Arc> arcVector_) throws IOException {
	// TODO 
	int width = 200;  
    int height = 400; 
	
//    System.out.println("graphics is printing");
	
	this.tokenVector_=tokenVector_;
	this.transitionVector_=transitionVector_;
	this.arcVector_=arcVector_;
	this.placeVector_=placeVector_;
	this.petriTool_=petriTool_;
	
}


public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {
	int step__=petriTool_.gridStep_;
	Color foregroundColor__=petriTool_.foregroundColor_;
	for (int i__ = 0; i__ < placeVector_.size(); i__++) {
	    Place tempPlace__ = (Place) placeVector_.elementAt(i__);
	    	tempPlace__.draw(gra, step__, foregroundColor__,
					 petriTool_.placeLabels_,"");
	}

	// Draw the Transitions
	for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
	    Transition tempTransition__ = (Transition) transitionVector_.
	                                elementAt(i__);
	    tempTransition__.draw(gra, step__, foregroundColor__,
	                          petriTool_.transitionLabels_,"");
	}

	// Draw the Tokens
	for (int i__ = 0; i__ < tokenVector_.size(); i__++) {
	    Token tempToken__ = (Token) tokenVector_.elementAt(i__);
	    tempToken__.draw(gra, step__, foregroundColor__);
	}

	// Draw the Arcs
	for (int i__ = 0; i__ < arcVector_.size(); i__++) {
	    Arc tempArc__ = (Arc) arcVector_.elementAt(i__);
	    tempArc__.draw(gra, step__, foregroundColor__);
	}

//	// Draw the Captions
//	for (int i__ = 0; i__ < captionVector_.size(); i__++) {
//	    Caption tempCaption__ = (Caption) captionVector_.elementAt(i__);
//	    tempCaption__.draw(gra, step__, petriTool_.captionColor_);
//	}
	 //  System.out.println("pageIndex="+pageIndex);
       Component c = null;
      //print string
//      String str = "asdlhsalkdj";
      
     /* System.out.println(gra);
      System.out.println(pf);
      System.out.println(pageIndex);
      */
      
      // Transformed to Graphics2D
      Graphics2D g2 = (Graphics2D) gra;
      
      //Set the print color is black
      g2.setColor(Color.black);
        
 //     Image g = (Image) tempg;
     
      
      
      //Print starting point coordinates
//      double x = pf.getImageableX();
//      double y = pf.getImageableY();
//       
//      switch(pageIndex){
//         case 0:
//           //Set the print font (font name, style, and size)
//           //As defined by the Java for five kinds of fonts��Serif��SansSerif��Monospaced��Dialog �� DialogInput
//           Font font = new Font("Serif", Font.PLAIN, 9);
//           g2.setFont(font);//set font
//           float[]   dash1   =   {4.0f}; 
//           g2.setStroke(new   BasicStroke(0.5f,   BasicStroke.CAP_BUTT,   BasicStroke.JOIN_MITER,   4.0f,   dash1,   0.0f));
//
//           float heigth = font.getSize2D();//set font height
//       //    System.out.println("x="+x);
//                      
//        
//		   Image src = Toolkit.getDefaultToolkit().getImage("");
//           g2.drawImage(src,(int)x,(int)y,c);
//           int img_Height=src.getHeight(c);
//           int img_width=src.getWidth(c);
//           //System.out.println("img_Height="+img_Height+"img_width="+img_width) ;
//           
////           g2.drawString(str, (float)x, (float)y+1*heigth+img_Height);
//                      
//           g2.drawImage(src,(int)x,(int)(y+1*heigth+img_Height+11),c);
//           
//             
      		return PAGE_EXISTS;
//         default:
//         return NO_SUCH_PAGE;
//      }
      
   }
}