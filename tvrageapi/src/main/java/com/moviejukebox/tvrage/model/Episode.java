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
import static com.moviejukebox.tvrage.TVRage.isValidString;
import java.io.Serializable;
import java.util.Date;
import org.pojava.datetime.DateTime;

public class Episode implements Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    private static final String UNKNOWN = TVRage.UNKNOWN;
    private EpisodeNumber episodeNumber = new EpisodeNumber(0, 0);
    private String productionId = UNKNOWN;
    private Date airDate = null;
    private String link = UNKNOWN;
    private String title = UNKNOWN;
    private String summary = UNKNOWN;
    private float rating = 0.0f;
    private String screenCap = UNKNOWN;

    public void setEpisodeNumber(EpisodeNumber episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public EpisodeNumber getEpisodeNumber() {
        return episodeNumber;
    }

    public boolean isValid() {
        if (episodeNumber.isValid() && isValidString(title)) {
            return true;
        }
        return false;
    }

    /**
     * Added as a convenience method
     *
     * @return
     */
    public int getEpisode() {
        return episodeNumber.getEpisode();
    }

    /**
     * Added as a convenience method
     *
     * @return
     */
    public int getSeason() {
        return episodeNumber.getSeason();
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        if (isValidString(productionId)) {
            this.productionId = productionId.trim();
        } else {
            this.productionId = UNKNOWN;
        }
    }

    public Date getAirDate() {
        return (Date) airDate.clone();
    }

    public void setAirDate(Date airDate) {
        this.airDate = (Date) airDate.clone();
    }

    public void setAirDate(String airDate) {
        if (isValidString(airDate)) {
            try {
                this.airDate = (new DateTime(airDate)).toDate();
            } catch (Exception ignore) {
                this.airDate = null;
            }
        } else {
            this.airDate = null;
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        if (isValidString(link)) {
            this.link = link.trim();
        } else {
            this.link = UNKNOWN;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (isValidString(title)) {
            this.title = title.trim();
        } else {
            this.title = UNKNOWN;
        }
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        if (isValidString(summary)) {
            this.summary = summary.trim();
        } else {
            this.summary = UNKNOWN;
        }
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setRating(String rating) {
        try {
            this.rating = Float.parseFloat(rating);
        } catch (Exception ignore) {
            this.rating = 0.0f;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[Episode=");
        builder.append("[episodeNumber=").append(episodeNumber);
        builder.append("][productionId=").append(productionId);
        builder.append("][airDate=").append(airDate);
        builder.append("][link=").append(link);
        builder.append("][title=").append(title);
        builder.append("][summary=").append(summary);
        builder.append("][rating=").append(rating);
        builder.append("][screenCap=").append(screenCap);
        builder.append("]]");
        return builder.toString();
    }

    public String getScreenCap() {
        return screenCap;
    }

    public void setScreenCap(String screenCap) {
        if (isValidString(screenCap)) {
            this.screenCap = screenCap.trim();
        } else {
            this.screenCap = UNKNOWN;
        }
    }
}
