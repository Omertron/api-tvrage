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
package com.moviejukebox.tvrage;

import com.moviejukebox.tvrage.model.Episode;
import com.moviejukebox.tvrage.model.EpisodeList;
import com.moviejukebox.tvrage.model.ShowInfo;
import com.moviejukebox.tvrage.tools.FilteringLayout;
import com.moviejukebox.tvrage.tools.TVRageParser;
import com.moviejukebox.tvrage.tools.WebBrowser;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * TV Rage API
 *
 * @author Stuart.Boston
 *
 */
public class TVRage {
    
    private String apiKey = null;
    public static final String UNKNOWN = "UNKNOWN";
    private static final String API_EPISODE_INFO = "episodeinfo.php";
    private static final String API_EPISODE_LIST = "episode_list.php";
    private static final String API_SEARCH = "search.php";
    private static final String API_SHOWINFO = "showinfo.php";
    private static final String API_SITE = "http://services.tvrage.com/myfeeds/";
    private static final Logger LOGGER = Logger.getLogger(TVRage.class);

    /**
     * Constructor, requires the API Key
     *
     * @param apiKey
     */
    public TVRage(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            throw new UnsupportedOperationException("No API Key provided!");
        }

        this.apiKey = apiKey;

        FilteringLayout.addApiKey(apiKey);
    }

    /**
     * Output the API version information to the debug log
     */
    public static void showVersion() {
        String apiTitle = TVRage.class.getPackage().getSpecificationTitle();

        if (StringUtils.isNotBlank(apiTitle)) {
            String apiVersion = TVRage.class.getPackage().getSpecificationVersion();
            String apiRevision = TVRage.class.getPackage().getImplementationVersion();
            StringBuilder sv = new StringBuilder();
            sv.append(apiTitle).append(" ");
            sv.append(apiVersion).append(" r");
            sv.append(apiRevision);
            LOGGER.debug(sv.toString());
        } else {
            LOGGER.debug("API-TVRage version/revision information not available");
        }
    }

    /**
     * Get the information for a specific episode
     *
     * @param showID
     * @param seasonId
     * @param episodeId
     * @return
     */
    public Episode getEpisodeInfo(String showID, String seasonId, String episodeId) {
        if (!isValidString(showID) || !isValidString(seasonId) || !isValidString(episodeId)) {
            return new Episode();
        }

        StringBuilder tvrageURL = buildURL(API_EPISODE_INFO, showID);
        // Append the Season & Episode to the URL
        tvrageURL.append("&ep=").append(seasonId);
        tvrageURL.append("x").append(episodeId);

        return TVRageParser.getEpisodeInfo(tvrageURL.toString());
    }

    /**
     * Get the episode information for all episodes for a show
     *
     * @param showID
     * @return
     */
    public EpisodeList getEpisodeList(String showID) {
        if (!isValidString(showID)) {
            return new EpisodeList();
        }

        String tvrageURL = buildURL(API_EPISODE_LIST, showID).toString();
        return TVRageParser.getEpisodeList(tvrageURL);
    }

    /**
     * Search for the show using the show ID
     *
     * @param showID
     * @return ShowInfo
     */
    public ShowInfo getShowInfo(int showID) {
        if (showID == 0) {
            return new ShowInfo();
        }

        String tvrageURL = buildURL(API_SHOWINFO, Integer.toString(showID)).toString();
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
     *
     * @param showName
     * @return list of matching shows
     */
    public List<ShowInfo> searchShow(String showName) {

        if (!isValidString(showName)) {
            return new ArrayList<ShowInfo>();
        }

        String tvrageURL = buildURL(API_SEARCH, showName).toString();
        return TVRageParser.getSearchShow(tvrageURL);
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
     *
     * @param urlParameter
     * @return
     */
    private StringBuilder buildURL(String urlParameter, String urlData) {
        // apiSite + search.php 	  + apiKey + &show=buffy
        // apiSite + showinfo.php 	  + apiKey + &sid=2930
        // apiSite + episode_list.php + apiKey + &sid=2930

        StringBuilder tvrageURL = new StringBuilder();
        tvrageURL.append(API_SITE);
        tvrageURL.append(urlParameter);
        tvrageURL.append("?key=");
        tvrageURL.append(apiKey);
        tvrageURL.append("&");

        if (urlParameter.equalsIgnoreCase(API_SEARCH)) {
            tvrageURL.append("show=").append(urlData);
        } else if (urlParameter.equalsIgnoreCase(API_SHOWINFO)) {
            tvrageURL.append("sid=").append(urlData);
        } else if (urlParameter.equalsIgnoreCase(API_EPISODE_LIST)) {
            tvrageURL.append("sid=").append(urlData);
        } else if (urlParameter.equalsIgnoreCase(API_EPISODE_INFO)) {
            tvrageURL.append("sid=").append(urlData);
            // Note this needs the season & episode appending to the url
        } else {
            return new StringBuilder(UNKNOWN);
        }

//		System.out.println("Search URL: " + tvrageURL); // XXX DEBUG

        return tvrageURL;
    }

    public static int convertStrToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ignore) {
            return 0;
        }
    }

    /**
     * Check the string passed to see if it contains a value.
     *
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
