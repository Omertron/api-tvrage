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
package com.moviejukebox.tvrage;

import static com.moviejukebox.tvrage.tools.StringTools.convertStrToInt;
import static com.moviejukebox.tvrage.tools.StringTools.isValidString;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.moviejukebox.tvrage.model.Episode;
import com.moviejukebox.tvrage.model.EpisodeList;
import com.moviejukebox.tvrage.model.ShowInfo;
import com.moviejukebox.tvrage.tools.LogFormatter;
import com.moviejukebox.tvrage.tools.TVRageParser;
import com.moviejukebox.tvrage.tools.WebBrowser;

/**
 * TV Rage API
 * @author Stuart.Boston
 *
 */
public class TVRage {
    private String apiKey = null;
    public static final String UNKNOWN = "UNKNOWN"; 
	private static final String LOGGERNAME = "TVRage";
	private static final String API_EPISODE_INFO   = "episodeinfo.php";
	private static final String API_EPISODE_LIST   = "episode_list.php";
	private static final String API_SEARCH         = "search.php";
	private static final String API_SHOWINFO       = "showinfo.php";
    private static final String API_SITE           = "http://services.tvrage.com/myfeeds/";

    private static Logger logger = null;
    private static LogFormatter mjbFormatter = new LogFormatter();
    
    /**
     * Constructor, requires the API Key
     * @param apiKey
     */
    public TVRage(String apiKey) {
        if (logger == null) {
            setLogger(LOGGERNAME);
        }
        
        if (this.apiKey == null) {
            this.apiKey = apiKey;
            mjbFormatter.addApiKey(apiKey);
        }
    }
    
    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(String loggerName) {
        setLogger(Logger.getLogger(loggerName));
    }

	public static void setLogger(Logger newLogger) {
	    logger = newLogger;
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(mjbFormatter);
        ch.setLevel(Level.FINE);
        logger.addHandler(ch);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
    }
	
	/**
	 * Get the information for a specific episode
	 * @param showID
	 * @param seasonId
	 * @param episodeId
	 * @return
	 */
	public Episode getEpisodeInfo(String showID, String seasonId, String episodeId) {
	    if (!isValidString(showID) || !isValidString(seasonId) || !isValidString(episodeId)) {
	        return new Episode();
	    }

	    String tvrageUrl = buildURL(API_EPISODE_INFO, showID);
	    // Append the Season & Episode to the URL
	    tvrageUrl += seasonId + "x" + episodeId;
	    
	    return TVRageParser.getEpisodeInfo(tvrageUrl);
	}
	
	/**
	 * Get the episode information for all episodes for a show
	 * @param showID
	 * @return
	 */
	public EpisodeList getEpisodeList(String showID) {
		if (!isValidString(showID)) {
			return new EpisodeList();
		}
		
        String tvrageURL = buildURL(API_EPISODE_LIST, showID);
		return TVRageParser.getEpisodeList(tvrageURL);
	}

	/**
	 * Search for the show using the show ID
	 * @param showID
	 * @return ShowInfo
	 */
	public ShowInfo getShowInfo(int showID) {
		String tvrageURL = null;
		
		if (showID == 0) {
			return new ShowInfo();
		}
		
		tvrageURL = buildURL(API_SHOWINFO, Integer.toString(showID));
		List<ShowInfo> showList = TVRageParser.getShowInfo(tvrageURL);
		if (showList.isEmpty()) {
		    return new ShowInfo();
		} else {
		    return showList.get(0);		
		}
	}
	
	public ShowInfo getShowInfo(String showID) {
	    if (!isValidString(showID)) {
	        return new ShowInfo();
	    }
	    
	    return getShowInfo(convertStrToInt(showID));
	}

	/**
	 * Search for the show using the show name
	 * @param showName
	 * @return list of matching shows
	 */
	public List<ShowInfo> searchShow(String showName) {
		String tvrageURL = null;
		
		if (!isValidString(showName)) {
			return new ArrayList<ShowInfo>();
		}
		
		tvrageURL = buildURL(API_SEARCH, showName);
		return TVRageParser.getSearchShow(tvrageURL);
	}
	
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        mjbFormatter.addApiKey(apiKey);
    }
    
    public void setProxy(String host, String port, String username, String password) {
        WebBrowser.setProxyHost(host);
        WebBrowser.setProxyPort(port);
        WebBrowser.setProxyUsername(username);
        WebBrowser.setProxyPassword(password);
    }

    public void setTimeout(int webTimeoutConnect, int webTimeoutRead) {
        WebBrowser.setWebTimeoutConnect(webTimeoutConnect);
        WebBrowser.setWebTimeoutRead(webTimeoutRead);
    }
	
    /**
	 * Build the API web URL for the process
	 * @param urlParameter
	 * @return
	 */
	private String buildURL(String urlParameter, String urlData) {
		// apiSite + search.php 	  + apiKey + &show=buffy
		// apiSite + showinfo.php 	  + apiKey + &sid=2930
		// apiSite + episode_list.php + apiKey + &sid=2930	
		String tvrageURL = API_SITE + urlParameter + "?key=" + apiKey + "&";
		
		if (urlParameter.equalsIgnoreCase(API_SEARCH)) {
			tvrageURL += "show=" + urlData;
		} else if (urlParameter.equalsIgnoreCase(API_SHOWINFO)) {
			tvrageURL += "sid=" + urlData;
		} else if (urlParameter.equalsIgnoreCase(API_EPISODE_LIST)) {
			tvrageURL += "sid=" + urlData;
		} else if (urlParameter.equalsIgnoreCase(API_EPISODE_INFO)) {
		    tvrageURL += "sid=" + urlData;
		    // Note this needs the season & episode appending to the url
		} else {
			tvrageURL = UNKNOWN;
		}
		
		logger.finer("Search URL: " + tvrageURL); // XXX DEBUG
		
		return tvrageURL;
	}
	
}
