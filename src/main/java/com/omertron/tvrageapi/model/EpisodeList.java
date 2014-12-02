/*
 *      Copyright (c) 2004-2014 Stuart Boston
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
package com.omertron.tvrageapi.model;

import com.omertron.tvrageapi.TVRageApi;
import static com.omertron.tvrageapi.TVRageApi.isValidString;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * A list of episode in a HashMap format for easy searching and retrieval
 *
 * @author stuart.boston
 *
 */
public class EpisodeList implements Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    private String showName;
    private int totalSeasons;
    private Map<EpisodeNumber, Episode> episodes;

    public EpisodeList() {
        showName = TVRageApi.UNKNOWN;
        totalSeasons = 0;
        episodes = new TreeMap<EpisodeNumber, Episode>();
    }

    public boolean isValid() {
        return isValidString(showName) && !episodes.isEmpty();
    }

    public Map<EpisodeNumber, Episode> getEpisodes() {
        return episodes;
    }

    @Deprecated
    /**
     * Use getEpisodes instead
     */
    public Map<EpisodeNumber, Episode> getEpisodeList() {
        return episodes;
    }

    public void setEpisodeList(Map<EpisodeNumber, Episode> episodeList) {
        this.episodes = episodeList;
    }

    public void addEpisode(Episode episode) {
        episodes.put(episode.getEpisodeNumber(), episode);
    }

    public Episode getEpisode(EpisodeNumber episodeNumber) {
        return episodes.get(episodeNumber);
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
            this.showName = TVRageApi.UNKNOWN;
        }
    }

    public void setTotalSeasons(int totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public void setTotalSeasons(String totalSeasons) {
        this.totalSeasons = NumberUtils.toInt(totalSeasons, 0);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
