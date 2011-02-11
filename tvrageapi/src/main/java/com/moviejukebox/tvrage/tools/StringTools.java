/*
 *      Copyright (c) 2004-2011 YAMJ Members
 *      http://code.google.com/p/moviejukebox/people/list 
 *  
 *      Web: http://code.google.com/p/moviejukebox/
 *  
 *      This software is licensed under a Creative Commons License
 *      See this page: http://code.google.com/p/moviejukebox/wiki/License
 *  
 *      For any reuse or distribution, you must make clear to others the 
 *      license terms of this work.  
 */
package com.moviejukebox.tvrage.tools;

import com.moviejukebox.tvrage.TVRage;

public class StringTools {
    public static int convertStrToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignore) {
            return 0;
        }
    }
    
    public static float convertFloatToString(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception ignore) {
            return 0.0f;
        }
    }

    /**
     * Check the string passed to see if it contains a value.
     * @param testString The string to test
     * @return False if the string is empty, null or UNKNOWN, True otherwise
     */
    public static boolean isValidString(String testString) {
        if ((testString == null)
                || (testString.trim().equals(""))
                || (testString.equalsIgnoreCase(TVRage.UNKNOWN))) {
            return false;
        }
        return true;
    }

}
