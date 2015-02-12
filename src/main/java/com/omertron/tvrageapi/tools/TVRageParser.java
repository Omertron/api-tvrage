/*
 *      Copyright (c) 2004-2015 Stuart Boston
 *
 *      This file is part of the TVRage API.
 *
 *      TVRage API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TVRage API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TVRage API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.omertron.tvrageapi.tools;

import com.omertron.tvrageapi.TVRageApi;
import com.omertron.tvrageapi.TVRageException;
import com.omertron.tvrageapi.model.CountryDetail;
import com.omertron.tvrageapi.model.Episode;
import com.omertron.tvrageapi.model.EpisodeList;
import com.omertron.tvrageapi.model.EpisodeNumber;
import com.omertron.tvrageapi.model.ShowInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TVRageParser {

    // Literals
    private static final String EPISODE = "episode";
    private static final String SUMMARY = "summary";
    private static final String TITLE = "title";
    private static final String AIRDATE = "airdate";
    private static final String COUNTRY = "country";
    // Constants
    private static final int MATCH_SEASON = 1;
    private static final int MATCH_EPISODE = 2;

    // Hide the constructor
    protected TVRageParser() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    private static Document getDocFromUrl(String searchUrl) throws TVRageException {
        return DOMHelper.getEventDocFromUrl(searchUrl);
    }

    public static Episode getEpisodeInfo(String searchUrl) throws TVRageException {
        Episode episode = new Episode();
        Document doc = getDocFromUrl(searchUrl);

        // The EpisodeInfo contains show information as well, but we will skip this
        NodeList nlEpisode = doc.getElementsByTagName(EPISODE);

        if (nlEpisode == null || nlEpisode.getLength() == 0) {
            return episode;
        }

        // There's only one episode in the EpisodeInfo node
        Element eEpisode = (Element) nlEpisode.item(0);
        episode = parseEpisodeInfo(eEpisode);
        return episode;
    }

    public static EpisodeList getEpisodeList(String searchUrl) throws TVRageException {
        EpisodeList epList = new EpisodeList();
        Document doc = getDocFromUrl(searchUrl);

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
        processSeasons(epList, doc.getElementsByTagName("Season"));

        return epList;
    }

    /**
     * process the individual seasons
     *
     * @param epList
     * @param nlSeasons
     */
    private static void processSeasons(EpisodeList epList, NodeList nlSeasons) {
        if (nlSeasons == null || nlSeasons.getLength() == 0) {
            return;
        }

        Node nEpisodeList;
        for (int loop = 0; loop < nlSeasons.getLength(); loop++) {
            nEpisodeList = nlSeasons.item(loop);
            if (nEpisodeList.getNodeType() == Node.ELEMENT_NODE) {
                processSeasonEpisodes((Element) nEpisodeList, epList);
            }
        }
    }

    /**
     * Process the episodes in the season and add them to the EpisodeList
     *
     * @param eEpisodeList
     * @param epList
     * @return
     */
    private static void processSeasonEpisodes(Element eEpisodeList, EpisodeList epList) {
        // Get the season number
        String season = eEpisodeList.getAttribute("no");

        NodeList nlEpisode = eEpisodeList.getElementsByTagName(EPISODE);
        if (nlEpisode == null || nlEpisode.getLength() == 0) {
            return;
        }

        for (int eLoop = 0; eLoop < nlEpisode.getLength(); eLoop++) {
            Node nEpisode = nlEpisode.item(eLoop);
            if (nEpisode.getNodeType() == Node.ELEMENT_NODE) {
                epList.addEpisode(parseEpisode((Element) nEpisode, season));
            }
        }

    }

    public static List<ShowInfo> getSearchShow(String searchUrl) throws TVRageException {
        return processShowInfo(searchUrl, "show");
    }

    public static List<ShowInfo> getShowInfo(String searchUrl) throws TVRageException {
        return processShowInfo(searchUrl, "Showinfo");
    }

    /**
     * Get a list of the ShowInfo from the specified tag
     *
     * @param searchUrl
     * @param tagName
     * @return
     */
    private static List<ShowInfo> processShowInfo(String searchUrl, String tagName) throws TVRageException {
        List<ShowInfo> showList = new ArrayList<ShowInfo>();
        ShowInfo showInfo;

        Document doc = getDocFromUrl(searchUrl);
        NodeList nlShowInfo = doc.getElementsByTagName(tagName);

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

    /**
     * Parse the episode node into an Episode object
     *
     * @param eEpisode
     * @param season
     * @return
     */
    private static Episode parseEpisode(Element eEpisode, String season) {
        Episode episode = new Episode();
        EpisodeNumber en = new EpisodeNumber();

        en.setSeason(season);
        en.setEpisode(DOMHelper.getValueFromElement(eEpisode, "seasonnum"));
        en.setAbsolute(DOMHelper.getValueFromElement(eEpisode, "epnum"));
        episode.setEpisodeNumber(en);

        episode.setProductionId(DOMHelper.getValueFromElement(eEpisode, "prodnum"));
        episode.setAirDate(DOMHelper.getValueFromElement(eEpisode, AIRDATE));
        episode.setLink(DOMHelper.getValueFromElement(eEpisode, "link"));
        episode.setTitle(DOMHelper.getValueFromElement(eEpisode, TITLE));
        episode.setSummary(DOMHelper.getValueFromElement(eEpisode, SUMMARY));
        episode.setRating(DOMHelper.getValueFromElement(eEpisode, "rating"));
        episode.setScreenCap(DOMHelper.getValueFromElement(eEpisode, "screencap"));

        return episode;
    }

    /**
     * Parse the episode info node into an Episode object
     *
     * @param eEpisodeInfo
     * @return
     */
    private static Episode parseEpisodeInfo(Element eEpisodeInfo) {
        Episode episode = new Episode();

        episode.setTitle(DOMHelper.getValueFromElement(eEpisodeInfo, TITLE));
        episode.setAirDate(DOMHelper.getValueFromElement(eEpisodeInfo, AIRDATE));
        episode.setLink(DOMHelper.getValueFromElement(eEpisodeInfo, "url"));
        episode.setSummary(DOMHelper.getValueFromElement(eEpisodeInfo, SUMMARY));

        // Process the season & episode field
        Pattern pattern = Pattern.compile("(\\d*)[x](\\d*)");
        Matcher matcher = pattern.matcher(DOMHelper.getValueFromElement(eEpisodeInfo, "number"));
        if (matcher.find()) {
            EpisodeNumber en = new EpisodeNumber();
            en.setSeason(matcher.group(MATCH_SEASON));
            en.setEpisode(matcher.group(MATCH_EPISODE));
            episode.setEpisodeNumber(en);
        }

        return episode;
    }

    /**
     * Parse the show info element into a ShowInfo object
     *
     * @param eShowInfo
     * @return
     */
    private static ShowInfo parseNextShowInfo(Element eShowInfo) {
        ShowInfo showInfo = new ShowInfo();
        String text;

        // ShowID
        showInfo.setShowID(DOMHelper.getValueFromElement(eShowInfo, "showid"));

        // ShowName
        text = DOMHelper.getValueFromElement(eShowInfo, "showname");
        if (!TVRageApi.isValidString(text)) {
            text = DOMHelper.getValueFromElement(eShowInfo, "name");
        }
        showInfo.setShowName(text);

        // ShowLink
        text = DOMHelper.getValueFromElement(eShowInfo, "showlink");
        if (!TVRageApi.isValidString(text)) {
            text = DOMHelper.getValueFromElement(eShowInfo, "link");
        }
        showInfo.setShowLink(text);

        // Country
        text = DOMHelper.getValueFromElement(eShowInfo, COUNTRY);
        if (!TVRageApi.isValidString(text)) {
            text = DOMHelper.getValueFromElement(eShowInfo, "origin_country");
        }
        showInfo.setCountry(text);

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
        showInfo.setSummary(DOMHelper.getValueFromElement(eShowInfo, SUMMARY));

        // Runtime
        showInfo.setRuntime(DOMHelper.getValueFromElement(eShowInfo, "runtime"));

        // Air Time
        showInfo.setAirTime(DOMHelper.getValueFromElement(eShowInfo, "airtime"));

        // Air Day
        showInfo.setAirDay(DOMHelper.getValueFromElement(eShowInfo, "airday"));

        // Time Zone
        showInfo.setTimezone(DOMHelper.getValueFromElement(eShowInfo, "timezone"));

        // Network
        processNetwork(showInfo, eShowInfo);

        // AKAs
        processAka(showInfo, eShowInfo);

        // Genres
        processGenre(showInfo, eShowInfo);

        return showInfo;
    }

    /**
     * Process network information
     *
     * @param showInfo
     * @param eShowInfo
     */
    private static void processNetwork(ShowInfo showInfo, Element eShowInfo) {
        NodeList nlNetwork = eShowInfo.getElementsByTagName("network");
        for (int nodeLoop = 0; nodeLoop < nlNetwork.getLength(); nodeLoop++) {
            Node nShowInfo = nlNetwork.item(nodeLoop);
            if (nShowInfo.getNodeType() == Node.ELEMENT_NODE) {
                Element eNetwork = (Element) nShowInfo;
                CountryDetail newNetwork = new CountryDetail();
                newNetwork.setCountry(eNetwork.getAttribute(COUNTRY));
                newNetwork.setDetail(eNetwork.getTextContent());
                showInfo.addNetwork(newNetwork);
            }
        }
    }

    /**
     * Process AKA information
     *
     * @param showInfo
     * @param eShowInfo
     */
    private static void processAka(ShowInfo showInfo, Element eShowInfo) {
        NodeList nlAkas = eShowInfo.getElementsByTagName("aka");
        for (int loop = 0; loop < nlAkas.getLength(); loop++) {
            Node nShowInfo = nlAkas.item(loop);
            if (nShowInfo.getNodeType() == Node.ELEMENT_NODE) {
                Element eAka = (Element) nShowInfo;
                CountryDetail newAka = new CountryDetail();
                newAka.setCountry(eAka.getAttribute(COUNTRY));
                newAka.setDetail(eAka.getTextContent());
                showInfo.addAka(newAka);
            }
        }
    }

    /**
     * Process Genres
     *
     * @param showInfo
     * @param eShowInfo
     */
    private static void processGenre(ShowInfo showInfo, Element eShowInfo) {
        NodeList nlGenres = eShowInfo.getElementsByTagName("genre");
        for (int loop = 0; loop < nlGenres.getLength(); loop++) {
            Node nGenre = nlGenres.item(loop);
            if (nGenre.getNodeType() == Node.ELEMENT_NODE) {
                Element eGenre = (Element) nGenre;
                if (eGenre.getFirstChild() != null) {
                    showInfo.addGenre(eGenre.getFirstChild().getNodeValue());
                }
            }
        }
    }
}
