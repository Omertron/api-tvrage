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

import com.moviejukebox.tvrage.TVRage;
import static com.moviejukebox.tvrage.TVRage.convertStrToInt;
import static com.moviejukebox.tvrage.TVRage.isValidString;
import java.util.Map;
import java.util.TreeMap;

/**
 * A list of episode in a HashMap format for easy searching and retrieval
 * @author stuart.boston
 *
 */
public class EpisodeList {
    private static final String UNKNOWN = TVRage.UNKNOWN;

    private String showName;
    private int totalSeasons;
    private Map<EpisodeNumber, Episode> episodeList;

    public EpisodeList() {
        showName = UNKNOWN;
        totalSeasons = 0;
        episodeList = new TreeMap<EpisodeNumber, Episode>();
    }

    public boolean isValid() {
        if (isValidString(showName) && !episodeList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Map<EpisodeNumber, Episode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(Map<EpisodeNumber, Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public void addEpisode(Episode episode) {
        episodeList.put(episode.getEpisodeNumber(), episode);
    }

    public Episode getEpisode(EpisodeNumber episodeNumber) {
        return episodeList.get(episodeNumber);
    }

    public Episode getEpisode(int season, int episode) {
        return getEpisode(new EpisodeNumber(season, episode));
    }

    public Episode getEpisode(String season, String episode) {
        return getEpisode(new EpisodeNumber(season, episode));
    }

    public String getShowName() {
        return showName;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }

    public void setShowName(String showName) {
        if (isValidString(showName)) {
            this.showName = showName.trim();
        } else {
            this.showName = UNKNOWN;
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
        builder.append("[EpisodeList=[showName=");
        builder.append(showName);
        builder.append("][totalSeasons=");
        builder.append(totalSeasons);
        builder.append("][episodeList=");
        builder.append(episodeList);
        builder.append("]]");
        return builder.toString();
    }
}
