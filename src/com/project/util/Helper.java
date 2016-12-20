/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.util;

import com.project.logger.Logs;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author RavirajS
 */
public class Helper {

    public static String convertVetorToCSV(Vector<String> a)
    {
        StringBuilder csv = new StringBuilder();
        Enumeration<String> enumerate = a.elements();
        int i = 0;
        while(enumerate.hasMoreElements())
        {
            if(i == 0)
            {
                i++;
            }
            else
            {
                csv.append(", ");
            }
            String data = enumerate.nextElement();
            csv.append(data);
        }
        return csv.toString();
    }

    public static void logHashMapData(HashMap<String,String> map1, HashMap<String,String> map2)
    {
        Set<String> key = map1.keySet();
        Iterator<String> itr = key.iterator();
        while(itr.hasNext())
        {
            String k = itr.next();
            Logs.write("Key: " + k + ", Map1: " + map1.get(k) + ", Map2: " + map2.get(k));
        }
    }

    public static void writeVectorContentsInFile(String filePath, Vector<String> contents) throws FileNotFoundException
    {
        File f = new File(filePath);
        PrintStream out = new PrintStream(f);
        Enumeration<String> results = contents.elements();
        while(results.hasMoreElements())
        {
            out.println(results.nextElement());
        }
        out.flush();
        out.close();
    }

    public static void main(String args[])
    {
        Vector<String> a = new Vector<String>();
        a.add("test");
        Vector<String> b = new Vector<String>();
        b.add("test");
        System.out.println(a.equals(b));
    }

}
