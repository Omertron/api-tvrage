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

import static com.moviejukebox.tvrage.TVRage.convertStrToInt;

public class EpisodeNumber implements Comparable<EpisodeNumber> {
    private static final int factor = 1000;
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
        return ((season * factor) + episode);
    }

    @Override
    public int compareTo(EpisodeNumber anotherEpisodeNumber) {
        int otherSeason = ((EpisodeNumber) anotherEpisodeNumber).getSeason();
        int otherEpisode = ((EpisodeNumber) anotherEpisodeNumber).getEpisode();
        
        return calculateAbsolute(season, episode) - calculateAbsolute(otherSeason, otherEpisode);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass())
            return false;

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
        StringBuffer sb = new StringBuffer();
        sb.append("Season: " + season + ", ");
        sb.append("Episode: " + episode + ", ");
        sb.append("Absolute: " + absolute);
        
        return sb.toString();
    }

    public boolean isValid() {
        if (season > 0 && episode > 0) {
            return true;
        } else {
            return false;
        }
    }

}
