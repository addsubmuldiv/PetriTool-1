package PetriTool;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

public class Conversion implements ActionListener,Observer{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser();
        //only txt files are allowable
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //allow multiple files to be selected
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileFilter()
        {
            public boolean accept(File f)
            {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }

            public String getDescription()
            {
                return "Text file(*.txt)";
            }
        });
        //has choose one text file
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File[] arF = fc.getSelectedFiles();
            for (int i = 0; i < arF.length; i++)
            {
                File f = arF[i];
                String fn = f.getAbsolutePath();
                fn = fn.substring(0, fn.length() - 3) + "mxml";
                try
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new
                        FileInputStream(f)));
                    PrintWriter pw = new PrintWriter(fn);
                    pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    pw.println("<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"WorkflowLog.xsd\" description=\"\">");
                    pw.println("\t<Data>");
                    pw.println("\t\t<Attribute name=\"info\">no info available</Attribute>");
                    pw.println("\t</Data>");
                    pw.println("\t<Source program=\"staffware\">");
                    pw.println("\t\t<Data>");
                    pw.println("\t\t\t<Attribute name=\"info\">no info available</Attribute>");
                    pw.println("\t\t</Data>");
                    pw.println("\t</Source>");
                    pw.println("\t<Process id=\"main_process\">");
                    pw.println("\t\t<Data>");
                    pw.println("\t\t\t<Attribute name=\"info\">no info available</Attribute>");
                    pw.println("\t\t</Data>");

                    String line = null;
                    int nc = 0;
                    while ( (line = br.readLine()) != null && !line.equals(""))
                    {
                    	int num = Integer.parseInt(line.substring(0, line.indexOf("*")));
                    	String traces = line.substring(line.indexOf("*")+2);
                    	System.out.println(num);
                    	System.out.println(traces);
                    	
                    	for(int j=0; j<num; j++){                   		
                    		pw.println("\t\t<ProcessInstance id=\"case_" + nc + "\">");
                            pw.println("\t\t\t<Data>");
                            pw.println("\t\t\t\t<Attribute name=\"info\">no info available</Attribute>");
                            pw.println("\t\t\t</Data>");

                            StringTokenizer st = new StringTokenizer(traces);
                            while (st.hasMoreElements())
                            {
                                String tn = (String) st.nextElement();
                                pw.println("\t\t\t<AuditTrailEntry>");
                                pw.println("\t\t\t\t<Data>");
                                pw.println(
                                    "\t\t\t\t\t<Attribute name=\"info\">no info available</Attribute>");
                                pw.println("\t\t\t\t</Data>");

                                //task name
                                pw.println("\t\t\t\t<WorkflowModelElement>" + tn +
                                           "</WorkflowModelElement>");
                                //event type
                                pw.println("\t\t\t\t<EventType>complete</EventType>");
                                //timestamp
                                pw.println(
                                    "\t\t\t\t<Timestamp>2002-04-08T10:56:00.000+01:00</Timestamp>");

                                pw.println("\t\t\t</AuditTrailEntry>");
                            }
                            nc ++;
                            pw.println("\t\t</ProcessInstance>");
                    	}                
                    }

                    pw.println("\t</Process>");
                    pw.println("</WorkflowLog>");

                    br.close();
                    pw.close();
                }
                catch (FileNotFoundException ex1)
                {
                    ex1.printStackTrace(System.out);
                }
                catch (IOException ex2)
                {
                    ex2.printStackTrace(System.out);
                }
            }
            //javax.swing.JOptionPane.showMessageDialog(null, "Converted Successfully!");
            System.out.println("Converted Successfully!");
        }
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		this.actionPerformed(new ActionEvent(this, 0, ""));
	}

}
