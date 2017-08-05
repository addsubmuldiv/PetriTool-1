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
import java.awt.print.Paper;
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
		   Vector<Token> tokenVector_, Vector<Arc> arcVector_){
	// TODO 
//	int width = 400;  
//    int height = 400; 
	
//    System.out.println("graphics is printing");
	
	this.tokenVector_=tokenVector_;
	this.transitionVector_=transitionVector_;
	this.arcVector_=arcVector_;
	this.placeVector_=placeVector_;
	this.petriTool_=petriTool_;
	
}


   public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {

		System.out.println("pageIndex="+pageIndex);   
		
		Graphics2D g2 = (Graphics2D) gra;  

		g2.setColor(Color.black);  

		g2.translate(pf.getImageableX(), pf.getImageableY());
		switch(pageIndex){  
			case 0:  
				Font font = new Font("Consolas", Font.PLAIN, 9);  
				g2.setFont(font);



				int step__=15;
				Color foregroundColor__=petriTool_.foregroundColor_;
				// Draw the Places
				for (int i__ = 0; i__ < placeVector_.size(); i__++) {
				Place tempPlace__ = (Place) placeVector_.elementAt(i__);
					tempPlace__.draw(g2, step__, foregroundColor__,
							 petriTool_.placeLabels_);
				}

				// Draw the Transitions
				for (int i__ = 0; i__ < transitionVector_.size(); i__++) {
					Transition tempTransition__ = (Transition) transitionVector_.
												elementAt(i__);
					tempTransition__.draw(g2, step__, foregroundColor__,
										  petriTool_.transitionLabels_);
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
				return PAGE_EXISTS;
			default:
				return NO_SUCH_PAGE;
    	}
   }

}