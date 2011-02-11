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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.moviejukebox.tvrage.TVRage;
import com.moviejukebox.tvrage.model.CountryDetail;
import com.moviejukebox.tvrage.model.Episode;
import com.moviejukebox.tvrage.model.EpisodeList;
import com.moviejukebox.tvrage.model.EpisodeNumber;
import com.moviejukebox.tvrage.model.ShowInfo;

public class TVRageParser {
    private static final String logMessage = "TVRage Error: ";
    static Logger logger = TVRage.getLogger();

    public static Episode getEpisodeInfo(String searchUrl) {
        Document doc = null;
        Episode episode = new Episode();
        
        try {
            doc = DOMHelper.getEventDocFromUrl(searchUrl);
        } catch (Exception error) {
            logger.severe(logMessage + error.getMessage());
            return episode;
        }
        
        if (doc == null) {
            return episode;
        }
        
        // The EpisodeInfo contains show information as well, but we will skip this
        NodeList nlEpisode = doc.getElementsByTagName("episode");
        
        if (nlEpisode == null || nlEpisode.getLength() == 0) {
            return episode;
        }
        
        // There's only one episode in the EpisodeInfo node
        Element eEpisode = (Element) nlEpisode.item(0);
        episode = parseEpisodeInfo(eEpisode);
        return episode;
    }

    public static EpisodeList getEpisodeList(String searchUrl) {
        EpisodeList epList = new EpisodeList();
        Episode episode = new Episode();
        Document doc = null;
        
        try {
            doc = DOMHelper.getEventDocFromUrl(searchUrl);
        } catch (Exception error) {
            logger.severe(logMessage + error.getMessage());
            return epList;
        }
        
        if (doc == null) {
            return epList;
        }
        
        NodeList nlEpisodeList;
        Node nEpisodeList;
        Element eEpisodeList;
        
        nlEpisodeList = doc.getElementsByTagName("Show");
        if (nlEpisodeList == null || nlEpisodeList.getLength() == 0) {
            return epList;
        }
        
        // Get the show name and total seasons
        for (int loop = 0; loop < nlEpisodeList.getLength(); loop++) {
            nEpisodeList = nlEpisodeList.item(loop);
            if (nEpisodeList.getNodeType() == Node.ELEMENT_NODE) {
                eEpisodeList = (Element) nEpisodeList;
                epList.setShowName(DOMHelper.getValueFromElement(eEpisodeList, "name"));
                epList.setTotalSeasons(DOMHelper.getValueFromElement(eEpisodeList, "totalseasons"));
            }
        }
        
        // Now process the individual seasons
        nlEpisodeList = doc.getElementsByTagName("Season");
        if (nlEpisodeList == null || nlEpisodeList.getLength() == 0) {
            return epList;
        }
        
        for (int loop = 0; loop < nlEpisodeList.getLength(); loop++) {
            nEpisodeList = nlEpisodeList.item(loop);
            if (nEpisodeList.getNodeType() == Node.ELEMENT_NODE) {
                eEpisodeList = (Element) nEpisodeList;
                String season = eEpisodeList.getAttribute("no"); // Get the season number
                
                NodeList nlEpisode = eEpisodeList.getElementsByTagName("episode");
                if (nlEpisode == null || nlEpisode.getLength() == 0) {
                    continue;
                }
                
                for (int eLoop = 0; eLoop < nlEpisode.getLength(); eLoop++) {
                    Node nEpisode = nlEpisode.item(eLoop);
                    if (nEpisode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eEpisode = (Element) nEpisode;
                        episode = parseEpisode(eEpisode, season);
                        epList.addEpisode(episode);
                    }
                }
            }
        }
        
        return epList;
    }

    public static List<ShowInfo> getSearchShow(String searchUrl) {
        List<ShowInfo> showList = new ArrayList<ShowInfo>();
        ShowInfo showInfo = null;
        Document doc = null;
        
        try {
            doc = DOMHelper.getEventDocFromUrl(searchUrl);
        } catch (Exception error) {
            logger.severe(logMessage + error.getMessage());
            return showList;
        }
        
        if (doc == null) {
            return showList;
        }
        
        NodeList nlShowInfo = doc.getElementsByTagName("show");
        
        if (nlShowInfo == null || nlShowInfo.getLength() == 0) {
            return showList;
        }
        
        for (int loop = 0; loop < nlShowInfo.getLength(); loop++) {
            Node nShowInfo = nlShowInfo.item(loop);
            if (nShowInfo.getNodeType() == Node.ELEMENT_NODE) {
                Element eShowInfo = (Element) nShowInfo;
                showInfo = parseNextShowInfo(eShowInfo);
                showList.add(showInfo);
            }
        }
        
        return showList;
    }

    public static List<ShowInfo> getShowInfo(String searchUrl) {
        List<ShowInfo> showList = new ArrayList<ShowInfo>();
        ShowInfo showInfo = null;
        Document doc = null;
        
        try {
            doc = DOMHelper.getEventDocFromUrl(searchUrl);
        } catch (Exception error) {
            logger.severe(logMessage + error.getMessage());
            return showList;
        }
        
        if (doc == null) {
            return showList;
        }
        
        NodeList nlShowInfo = doc.getElementsByTagName("Showinfo");
        
        if (nlShowInfo == null || nlShowInfo.getLength() == 0) {
            return showList;
        }
        
        for (int loop = 0; loop < nlShowInfo.getLength(); loop++) {
            Node nShowInfo = nlShowInfo.item(loop);
            if (nShowInfo.getNodeType() == Node.ELEMENT_NODE) {
                Element eShowInfo = (Element) nShowInfo;
                showInfo = parseNextShowInfo(eShowInfo);
                showList.add(showInfo);
            }
        }
        
        return showList;
    }

    private static Episode parseEpisode(Element eEpisode, String season) {
        Episode episode = new Episode();
        EpisodeNumber en = new EpisodeNumber();

        en.setSeason(season);
        en.setEpisode(DOMHelper.getValueFromElement(eEpisode, "seasonnum"));
        en.setAbsolute(DOMHelper.getValueFromElement(eEpisode, "epnum"));
        episode.setEpisodeNumber(en);
        
        episode.setProductionId(DOMHelper.getValueFromElement(eEpisode, "prodnum"));
        episode.setAirDate(DOMHelper.getValueFromElement(eEpisode, "airdate"));
        episode.setLink(DOMHelper.getValueFromElement(eEpisode, "link"));
        episode.setTitle(DOMHelper.getValueFromElement(eEpisode, "title"));
        episode.setSummary(DOMHelper.getValueFromElement(eEpisode, "summary"));
        episode.setRating(DOMHelper.getValueFromElement(eEpisode, "rating"));
        episode.setScreenCap(DOMHelper.getValueFromElement(eEpisode, "screencap"));
        
        return episode;
    }
    
    private static Episode parseEpisodeInfo(Element eEpisodeInfo) {
        Episode episode = new Episode();

        episode.setTitle(DOMHelper.getValueFromElement(eEpisodeInfo, "title"));
        episode.setAirDate(DOMHelper.getValueFromElement(eEpisodeInfo, "airdate"));
        episode.setLink(DOMHelper.getValueFromElement(eEpisodeInfo, "url"));
        episode.setSummary(DOMHelper.getValueFromElement(eEpisodeInfo, "summary"));
        
        // Process the season & episode field
        Pattern pattern = Pattern.compile("(\\d)[x](\\d)");
        Matcher matcher = pattern.matcher(DOMHelper.getValueFromElement(eEpisodeInfo, "number"));
        if (matcher.find()) {
            EpisodeNumber en = new EpisodeNumber();
            en.setSeason(matcher.group(1));
            en.setEpisode(matcher.group(2));
            episode.setEpisodeNumber(en);
        }
        
        return episode;
    }

    private static ShowInfo parseNextShowInfo(Element eShowInfo) {
        ShowInfo showInfo = new ShowInfo();

        // ShowID
        showInfo.setShowID(DOMHelper.getValueFromElement(eShowInfo, "showid"));
        
        // ShowName
        showInfo.setShowName(DOMHelper.getValueFromElement(eShowInfo, "showname"));
        showInfo.setShowName(DOMHelper.getValueFromElement(eShowInfo, "name"));
        
        // ShowLink
        showInfo.setShowLink(DOMHelper.getValueFromElement(eShowInfo, "showlink"));
        showInfo.setShowLink(DOMHelper.getValueFromElement(eShowInfo, "link"));
        
        // Country
        showInfo.setCountry(DOMHelper.getValueFromElement(eShowInfo, "country"));
        showInfo.setCountry(DOMHelper.getValueFromElement(eShowInfo, "origin_country"));
        
        // Started
        showInfo.setStarted(DOMHelper.getValueFromElement(eShowInfo, "started"));

        // StartDate
        showInfo.setStartDate(DOMHelper.getValueFromElement(eShowInfo, "startdate"));
        
        // Ended
        showInfo.setEnded(DOMHelper.getValueFromElement(eShowInfo, "ended"));
        
        // Seasons
        showInfo.setTotalSeasons(DOMHelper.getValueFromElement(eShowInfo, "seasons"));
        
        // Status
        showInfo.setStatus(DOMHelper.getValueFromElement(eShowInfo, "status"));

        // Classification
        showInfo.setClassification(DOMHelper.getValueFromElement(eShowInfo, "classification"));

        // Summary
        showInfo.setSummary(DOMHelper.getValueFromElement(eShowInfo, "summary"));
        
        // Runtime
        showInfo.setRuntime(DOMHelper.getValueFromElement(eShowInfo, "runtime"));
        
        // Air Time
        showInfo.setAirTime(DOMHelper.getValueFromElement(eShowInfo, "airtime"));
        
        // Air Day
        showInfo.setAirDay(DOMHelper.getValueFromElement(eShowInfo, "airday"));
        
        // Time Zone
        showInfo.setTimezone(DOMHelper.getValueFromElement(eShowInfo, "timezone"));

        // Network
        NodeList nlNetwork = eShowInfo.getElementsByTagName("network");
        for (int nodeLoop = 0; nodeLoop < nlNetwork.getLength(); nodeLoop++) {
            Node nShowInfo = nlNetwork.item(nodeLoop);
            if (nShowInfo.getNodeType() == Node.ELEMENT_NODE) {
                Element eNetwork = (Element) nShowInfo;
                CountryDetail newNetwork = new CountryDetail();
                newNetwork.setCountry(eNetwork.getAttribute("country"));
                newNetwork.setDetail(eNetwork.getTextContent());
                showInfo.addNetwork(newNetwork);
            }
        }

        // AKAs
        NodeList nlAkas = eShowInfo.getElementsByTagName("aka");
        for (int loop = 0; loop < nlAkas.getLength(); loop++) {
            Node nShowInfo = nlAkas.item(loop);
            if (nShowInfo.getNodeType() == Node.ELEMENT_NODE) {
                Element eAka = (Element) nShowInfo;
                CountryDetail newAka = new CountryDetail();
                newAka.setCountry(eAka.getAttribute("country"));
                newAka.setDetail(eAka.getTextContent());
                showInfo.addAka(newAka);
            }
        }
        
        // Genres
        NodeList nlGenres = eShowInfo.getElementsByTagName("genre");
        for (int loop = 0; loop < nlGenres.getLength(); loop++) {
            Node nGenre = nlGenres.item(loop);
            if (nGenre.getNodeType() == Node.ELEMENT_NODE) {
                Element eGenre = (Element) nGenre;
                showInfo.addGenre(eGenre.getNodeValue());
            }
        }
        
        return showInfo;
    }
}
