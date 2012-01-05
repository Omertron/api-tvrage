/*
 *      Copyright (c) 2004-2012 YAMJ Members
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
package com.moviejukebox.tvrage.model;

import static com.moviejukebox.tvrage.TVRage.isValidString;

import com.moviejukebox.tvrage.TVRage;

/**
 * Class to hold country along with a generic detail string
 * @author Stuart.Boston
 *
 */
public class CountryDetail {
    private static final String UNKNOWN = TVRage.UNKNOWN;
	private String country;
	private String detail;
	
	public CountryDetail() {
	    this.country = UNKNOWN;
	    this.detail = UNKNOWN;
	}
	
	public CountryDetail(String country, String detail) {
        this.country = country;
        this.detail = detail;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
	    if (isValidString(country)) {
	        this.country = country.trim();
	    } else {
	        this.country = UNKNOWN;
	    }
	}
	
	public String getDetail() {
		return detail;
	}
	
	public void setDetail(String detail) {
        if (isValidString(detail)) {
            this.detail = detail.trim();
        } else {
            this.detail = UNKNOWN;
        }
	}
	
	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[CountryDetail=[country=");
        builder.append(country);
        builder.append("][detail=");
        builder.append(detail);
        builder.append("]]");
        return builder.toString();
    }

	public boolean isValid() {
	    if (!isValidString(country)) {
	        return false;
	    }
	    
	    if (!isValidString(detail)) {
	        return false;
	    }
	    
	    return true;
	}
}
