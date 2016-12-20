/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author RavirajS
 */
public class Logs {

    private static String logFile = null;
    private static PrintStream logStream = null;

    static {        
        File f = new File("logs");
        if (!f.isDirectory()) {
            f.mkdir();
        }
        
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_hh_mm_ss");        
        String formattedDate = dateFormatter.format(date);
        
        logFile = "DVU_" + formattedDate + ".log";
        
        try {
            logStream = new PrintStream(new File("logs\\" + logFile));
        } catch (FileNotFoundException fnfe) {
            try {
                Runtime.getRuntime().exec("CMD /C ECHO %DATE% %TIME% - FNFE - Failed to access log system >> DUV_SYSTEM.err");            
                
            } catch (IOException ioe) {
                //Couldnot catch..
            }
        } catch (IOException ioe) {
            try {
                Runtime.getRuntime().exec("CMD /C ECHO %DATE% %TIME% - IOE - Failed to access log system >> DUV_SYSTEM.err");
                
            } catch (IOException ioe_1) {
                //Couldnot catch..
            }
        }
    }

    public static void write(String message, Exception e) {
        Date date = new Date();
        logStream.println(date.toString() + ":" + message + " - " + e.getMessage());
        StackTraceElement ste[] = e.getStackTrace();
        for (StackTraceElement s : ste) {
            logStream.println("\t" + s.toString());

        }
    }

    public static void write(String message) {
        Date date = new Date();        
        logStream.println(date.toString() + ":" + message);

    }
    
    public static void write_nodate(String message) {
        Date date = new Date();        
        logStream.println(message);
    }
    
    
}
