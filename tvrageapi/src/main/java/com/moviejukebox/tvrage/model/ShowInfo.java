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
package com.moviejukebox.tvrage.model;

import java.util.ArrayList;
import java.util.Date;

import org.pojava.datetime.DateTime;
import static com.moviejukebox.tvrage.TVRage.convertStrToInt;
import static com.moviejukebox.tvrage.TVRage.isValidString;

import com.moviejukebox.tvrage.TVRage;

/**
 * Full information about the show
 * @author Stuart.Boston
 *
 */
public class ShowInfo {
    private static final String UNKNOWN = TVRage.UNKNOWN;
	private String airDay          = UNKNOWN;
	private String airTime         = UNKNOWN;
	private ArrayList<CountryDetail> akas     = new ArrayList<CountryDetail>();
	private String classification  = UNKNOWN;
	private String country         = UNKNOWN;
	private String ended           = UNKNOWN;
	private ArrayList<String>        genres   = new ArrayList<String>();
	private ArrayList<CountryDetail> network  = new ArrayList<CountryDetail>();
	private String originCountry   = UNKNOWN;
	private int    runtime         = 0;
	private int    showID          = 0;
	private String showLink        = UNKNOWN;
	private String showName        = UNKNOWN;
	private Date   startDate       = null;
	private int    started         = 0;
    private String status          = UNKNOWN;
    private String summary         = UNKNOWN;
	private String timezone        = UNKNOWN;
	private int    totalSeasons    = 0;
	
	public boolean isValid() {
	    if (showID > 0) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	/**
	 * Add a single AKA to the list
	 * @param newAka
	 */
	public void addAka(CountryDetail newAka) {
		if (newAka.isValid()) {
			this.akas.add(newAka);
		}
	}
	
	/**
	 * Add single AKA from a country/aka pairing
	 * @param country
	 * @param aka
	 */
	public void addAka(String country, String aka) {
		if (!isValidString(country) || !isValidString(aka)) {
			return;
		}
		
		this.akas.add(new CountryDetail(country, aka));
	}
	
	/**
	 * Add a single Genre to the list
	 * @param genre
	 */
	public void addGenre(String genre) {
	    if (isValidString(genre)) {
	        this.genres.add(genre);
	    }
	}
	
	/**
	 * Add a single network to the list
	 * @param newNetwork
	 */
	public void addNetwork(CountryDetail newNetwork) {
		if (newNetwork.isValid()) {
			this.network.add(newNetwork);
		}
	}
	
	/** 
	 * Add a single network to the list
	 * @param country
	 * @param network
	 */
	public void addNetwork(String country, String networkName) {
       if (!isValidString(country) || !isValidString(networkName)) {
            return;
        }
        
        this.network.add(new CountryDetail(country, networkName));
	}
	
	public String getAirDay() {
		return airDay;
	}
	
	public String getAirTime() {
		return airTime;
	}

	public ArrayList<CountryDetail> getAkas() {
		return akas;
	}

	public String getClassification() {
		return classification;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getEnded() {
		return ended;
	}
	
    public ArrayList<String> getGenres() {
		return genres;
	}
    
	public ArrayList<CountryDetail> getNetwork() {
		return network;
	}
	
	public String getOriginCountry() {
		return originCountry;
	}
	
	public int getRuntime() {
		return runtime;
	}
	
	public int getShowID() {
		return showID;
	}
	
	public String getShowLink() {
		return showLink;
	}
	
	public String getShowName() {
		return showName;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public int getStarted() {
		return started;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public String getTimezone() {
		return timezone;
	}
	
	public int getTotalSeasons() {
		return totalSeasons;
	}
	
	public void setAirDay(String airDay) {
	    if (isValidString(airDay)) {
	        this.airDay = airDay;
	    } else {
	        this.airDay = UNKNOWN;
	    }
	}
	
	public void setAirTime(String airTime) {
	    if (isValidString(airTime)) {
	        this.airTime = airTime;
	    } else {
	        this.airTime = UNKNOWN;
	    }
	}
	
	public void setAkas(ArrayList<CountryDetail> akas) {
		this.akas = akas;
	}
	
	public void setClassification(String classification) {
	    if (isValidString(classification)) {
	        this.classification = classification;
	    } else {
	        this.classification = UNKNOWN;
	    }
	}
	
    public void setCountry(String country) {
        if (isValidString(country)) {
            this.country = country;
        } else {
            this.country = UNKNOWN;
        }
	}
    
    public void setEnded(String ended) {
        if (isValidString(ended)) {
            this.ended = ended;
        } else {
            this.ended = UNKNOWN;
        }
	}
    
	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}
	
	public void setNetwork(ArrayList<CountryDetail> network) {
		this.network = network;
	}
	
	public void setOriginCountry(String originCountry) {
        if (isValidString(originCountry)) {
            this.originCountry = originCountry;
        } else {
            this.originCountry = UNKNOWN;
        }
	}
	
	public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
	
	public void setRuntime(String runtime) {
        this.runtime = convertStrToInt(runtime);
    }
	
	public void setShowID(int showID) {
		this.showID = showID;
	}
	
	public void setShowID(String showID) {
        this.showID = convertStrToInt(showID);
	}
	
	public void setShowLink(String showLink) {
        if (isValidString(showLink)) {
            this.showLink = showLink;
        } else {
            this.showLink = UNKNOWN;
        }
	}
	
	public void setShowName(String showName) {
        if (isValidString(showName)) {
            this.showName = showName;
        } else {
            this.showName = UNKNOWN;
        }
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public void setStartDate(String startDate) {
	    if (isValidString(startDate)) {
	        try {
	            this.startDate = (new DateTime(startDate)).toDate();
	        } catch (Exception ignore) {
	            // We can't do anything about this error, so return
	            this.startDate = null;
	        }
	    }
    }
	
	public void setStarted(int started) {
        this.started = started;
    }
	
	public void setStarted(String started) {
        this.started = convertStrToInt(started);
    }
	
	public void setStatus(String status) {
        if (isValidString(status)) {
            this.status = status;
        } else {
            this.status = UNKNOWN;
        }
	}

	public void setSummary(String summary) {
        if (isValidString(summary)) {
            this.summary = summary;
        } else {
            this.summary = UNKNOWN;
        }
	}
	
	public void setTimezone(String timezone) {
        if (isValidString(timezone)) {
            this.timezone = timezone;
        } else {
            this.timezone = UNKNOWN;
        }
	}

	public void setTotalSeasons(int totalSeasons) {
		this.totalSeasons = totalSeasons;
	}

	public void setTotalSeasons(String totalSeasons) {
	    this.totalSeasons = convertStrToInt(totalSeasons);
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ShowInfo=[airDay=");
        builder.append(airDay);
        builder.append("][airTime=");
        builder.append(airTime);
        builder.append("][akas=");
        builder.append(akas);
        builder.append("][classification=");
        builder.append(classification);
        builder.append("][country=");
        builder.append(country);
        builder.append("][ended=");
        builder.append(ended);
        builder.append("][genres=");
        builder.append(genres);
        builder.append("][network=");
        builder.append(network);
        builder.append("][originCountry=");
        builder.append(originCountry);
        builder.append("][runtime=");
        builder.append(runtime);
        builder.append("][showID=");
        builder.append(showID);
        builder.append("][showLink=");
        builder.append(showLink);
        builder.append("][showName=");
        builder.append(showName);
        builder.append("][startDate=");
        builder.append(startDate);
        builder.append("][started=");
        builder.append(started);
        builder.append("][status=");
        builder.append(status);
        builder.append("][summary=");
        builder.append(summary);
        builder.append("][timezone=");
        builder.append(timezone);
        builder.append("][totalSeasons=");
        builder.append(totalSeasons);
        builder.append("]]");
        return builder.toString();
    }

}
