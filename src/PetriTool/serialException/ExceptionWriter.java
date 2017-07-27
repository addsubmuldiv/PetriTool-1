package PetriTool.serialException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author zhong
 *Responsible for extracting the error information from the 
 *incoming Exception and converting it to a string
 */
public class ExceptionWriter {

	public static String getErrorInfoFromException(Exception e) { 
	    	
	    	StringWriter sw = null;
	    	PrintWriter pw = null;
	    	
	        try {  
	            sw = new StringWriter();  
	            pw = new PrintWriter(sw);  
	            e.printStackTrace(pw);  
	            return "\r\n" + sw.toString() + "\r\n";  
	            
	        } catch (Exception e2) {  
	            return "Error!The error message has not been obtained, please check and try again!";  
	        } finally {
	        	try {
	            	if (pw != null) {
	            		pw.close();
	            	}
	            	if (sw != null) {
	    				sw.close();
	            	}
	        	} catch (IOException e1) {
	        		e1.printStackTrace();
	        	}
	        }
	    }
}

