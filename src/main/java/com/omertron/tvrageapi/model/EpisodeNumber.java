/*
 *      Copyright (c) 2004-2013 Stuart Boston
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

import static com.omertron.tvrageapi.TVRageApi.convertStrToInt;
import java.io.Serializable;

public class EpisodeNumber implements Comparable<EpisodeNumber>, Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    private static final int FACTOR = 1000;
    private int absolute;   // The absolute episode number across all seasons
    private int episode;    // The episode number within the season
    private int season;     // The show season

    public EpisodeNumber() {
        this.season = 0;
        this.episode = 0;
        this.absolute = 0;
    }

    public EpisodeNumber(int season, int episode) {
        this.season = season;
        this.episode = episode;
        // Calculate the absolute if we are not passed one
        this.absolute = calculateAbsolute(season, episode);
    }

    public EpisodeNumber(int season, int episode, int absolute) {
        this.season = season;
        this.episode = episode;
        this.absolute = absolute;
    }

    public EpisodeNumber(String season, String episode) {
        this.season = convertStrToInt(season);
        this.episode = convertStrToInt(episode);
        // Calculate the absolute if we are not passed one
        this.absolute = calculateAbsolute(this.season, this.episode);
    }

    public EpisodeNumber(String season, String episode, String absolute) {
        this.season = convertStrToInt(season);
        this.episode = convertStrToInt(episode);
        this.absolute = convertStrToInt(absolute);
    }

    private int calculateAbsolute(int season, int episode) {
        // Make the season very large for comparison purposes (will handle up to 1,000 episodes)
        return ((season * FACTOR) + episode);
    }

    @Override
    public int compareTo(EpisodeNumber anotherEpisodeNumber) {
        int otherSeason = ((EpisodeNumber) anotherEpisodeNumber).getSeason();
        int otherEpisode = ((EpisodeNumber) anotherEpisodeNumber).getEpisode();

        return calculateAbsolute(season, episode) - calculateAbsolute(otherSeason, otherEpisode);
    }

    public int getAbsolute() {
        return absolute;
    }

    public int getEpisode() {
        return episode;
    }

    public int getSeason() {
        return season;
    }

    public String getSxE() {
        return String.format("%dx%d", season, episode);
    }

    public String getSxxEyy() {
        return String.format("S%2dE%2d", season, episode);
    }

    public void setAbsolute(int absolute) {
        this.absolute = absolute;
    }

    public void setAbsolute(String absolute) {
        this.absolute = convertStrToInt(absolute);
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public void setEpisode(String episode) {
        this.episode = convertStrToInt(episode);
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setSeason(String season) {
        this.season = convertStrToInt(season);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[EpisodeNumber=[absolute=");
        builder.append(absolute);
        builder.append("][episode=");
        builder.append(episode);
        builder.append("][season=");
        builder.append(season);
        builder.append("]]");
        return builder.toString();
    }

    public boolean isValid() {
        // False if either is 0
        return (season * episode > 0);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + absolute;
        result = prime * result + episode;
        result = prime * result + season;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        EpisodeNumber other = (EpisodeNumber) obj;

        if (absolute == other.absolute) {
            // If the absolute matches, then assume the season and episode will
            return true;
        }

        if (season != other.season) {
            return false;
        }

        if (episode != other.episode) {
            return false;
        }

        return true;
    }
}
