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
package com.omertron.tvrageapi.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;

public class EpisodeNumber implements Comparable<EpisodeNumber>, Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    private static final int FACTOR = 1000;
    // The absolute episode number across all seasons
    private int absolute;
    // The episode number within the season
    private int episode;
    // The show season
    private int season;

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
        this.season = NumberUtils.toInt(season, 0);
        this.episode = NumberUtils.toInt(episode, 0);
        // Calculate the absolute if we are not passed one
        this.absolute = calculateAbsolute(this.season, this.episode);
    }

    public EpisodeNumber(String season, String episode, String absolute) {
        this.season = NumberUtils.toInt(season, 0);
        this.episode = NumberUtils.toInt(episode, 0);
        this.absolute = NumberUtils.toInt(absolute, 0);
    }

    private int calculateAbsolute(int season, int episode) {
        // Make the season very large for comparison purposes (will handle up to 1,000 episodes)
        return (season * FACTOR) + episode;
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
        this.absolute = NumberUtils.toInt(absolute, 0);
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public void setEpisode(String episode) {
        this.episode = NumberUtils.toInt(episode, 0);
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setSeason(String season) {
        this.season = NumberUtils.toInt(season, 0);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public boolean isValid() {
        // False if either is 0
        return season * episode > 0;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(absolute)
                .append(episode)
                .append(season)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EpisodeNumber) {
            final EpisodeNumber other = (EpisodeNumber) obj;
            return new EqualsBuilder()
                    .append(absolute, other.absolute)
                    .append(episode, other.episode)
                    .append(season, other.season)
                    .isEquals();
        } else {
            return false;
        }
    }
}
