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

   public PrintControl() throws IOException {
	// TODO 自动生成的构造函数存根
	int width = 200;  
    int height = 400; 
	
    System.out.println("graphics is printing");
	
	
	/*BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	Graphics graphics2 = image.createGraphics();
	//graphics2 = graphics;
    // image.getGraphics();
	//graphics.dispose();  
    // 保存文件      
    ImageIO.write(image, "png", new File("C:/Users/Jianhong YE/Desktop/新建文件夹 (2)/test.png"));*/ 
}


public int print(Graphics gra, PageFormat pf, int pageIndex) throws PrinterException {
      
	 //  System.out.println("pageIndex="+pageIndex);
       Component c = null;
      //print string
      String str = "中华民族是勤劳、勇敢和富有智慧的伟大民族。";
      
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
      double x = pf.getImageableX();
      double y = pf.getImageableY();
       
      switch(pageIndex){
         case 0:
           //Set the print font (font name, style, and size)
           //As defined by the Java for five kinds of fonts：Serif、SansSerif、Monospaced、Dialog 和 DialogInput
           Font font = new Font("Serif", Font.PLAIN, 9);
           g2.setFont(font);//set font
           float[]   dash1   =   {4.0f}; 
           g2.setStroke(new   BasicStroke(0.5f,   BasicStroke.CAP_BUTT,   BasicStroke.JOIN_MITER,   4.0f,   dash1,   0.0f));

           float heigth = font.getSize2D();//set font height
       //    System.out.println("x="+x);
                      
        
		   Image src = Toolkit.getDefaultToolkit().getImage("");
           g2.drawImage(src,(int)x,(int)y,c);
           int img_Height=src.getHeight(c);
           int img_width=src.getWidth(c);
           //System.out.println("img_Height="+img_Height+"img_width="+img_width) ;
           
           g2.drawString(str, (float)x, (float)y+1*heigth+img_Height);
                      
           g2.drawImage(src,(int)x,(int)(y+1*heigth+img_Height+11),c);
           
             
         return PAGE_EXISTS;
         default:
         return NO_SUCH_PAGE;
      }
      
   }
}