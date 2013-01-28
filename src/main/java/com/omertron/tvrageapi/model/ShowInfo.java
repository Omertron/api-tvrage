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

import com.omertron.tvrageapi.TVRageApi;
import static com.omertron.tvrageapi.TVRageApi.convertStrToInt;
import static com.omertron.tvrageapi.TVRageApi.isValidString;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.pojava.datetime.DateTime;

/**
 * Full information about the show
 *
 * @author Stuart.Boston
 *
 */
public class ShowInfo implements Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    private String airDay = TVRageApi.UNKNOWN;
    private String airTime = TVRageApi.UNKNOWN;
    private List<CountryDetail> akas = new ArrayList<CountryDetail>();
    private String classification = TVRageApi.UNKNOWN;
    private String country = TVRageApi.UNKNOWN;
    private String ended = TVRageApi.UNKNOWN;
    private List<String> genres = new ArrayList<String>();
    private List<CountryDetail> network = new ArrayList<CountryDetail>();
    private String originCountry = TVRageApi.UNKNOWN;
    private int runtime = 0;
    private int showID = 0;
    private String showLink = TVRageApi.UNKNOWN;
    private String showName = TVRageApi.UNKNOWN;
    private Date startDate = null;
    private int started = 0;
    private String status = TVRageApi.UNKNOWN;
    private String summary = TVRageApi.UNKNOWN;
    private String timezone = TVRageApi.UNKNOWN;
    private int totalSeasons = 0;

    public boolean isValid() {
        if (showID > 0) {
            return true;
        }
        return false;
    }

    /**
     * Add a single AKA to the list
     *
     * @param newAka
     */
    public void addAka(CountryDetail newAka) {
        if (newAka.isValid()) {
            this.akas.add(newAka);
        }
    }

    /**
     * Add single AKA from a country/aka pairing
     *
     * @param country
     * @param aka
     */
    public void addAka(String country, String aka) {
        if (!isValidString(country) || !isValidString(aka)) {
            return;
        }

        this.akas.add(new CountryDetail(country, aka));
    }

    /**
     * Add a single Genre to the list
     *
     * @param genre
     */
    public void addGenre(String genre) {
        if (isValidString(genre)) {
            this.genres.add(genre);
        }
    }

    /**
     * Add a single network to the list
     *
     * @param newNetwork
     */
    public void addNetwork(CountryDetail newNetwork) {
        if (newNetwork.isValid()) {
            this.network.add(newNetwork);
        }
    }

    /**
     * Add a single network to the list
     *
     * @param country
     * @param networkName
     */
    public void addNetwork(String country, String networkName) {
        if (!isValidString(country) || !isValidString(networkName)) {
            return;
        }

        this.network.add(new CountryDetail(country, networkName));
    }

    public String getAirDay() {
        return airDay;
    }

    public String getAirTime() {
        return airTime;
    }

    public List<CountryDetail> getAkas() {
        return akas;
    }

    public String getClassification() {
        return classification;
    }

    public String getCountry() {
        return country;
    }

    public String getEnded() {
        return ended;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<CountryDetail> getNetwork() {
        return network;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getShowID() {
        return showID;
    }

    public String getShowLink() {
        return showLink;
    }

    public String getShowName() {
        return showName;
    }

    public Date getStartDate() {
        return (Date) startDate.clone();
    }

    public int getStarted() {
        return started;
    }

    public String getStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }

    public void setAirDay(String airDay) {
        if (isValidString(airDay)) {
            this.airDay = airDay;
        } else {
            this.airDay = TVRageApi.UNKNOWN;
        }
    }

    public void setAirTime(String airTime) {
        if (isValidString(airTime)) {
            this.airTime = airTime;
        } else {
            this.airTime = TVRageApi.UNKNOWN;
        }
    }

    public void setAkas(List<CountryDetail> akas) {
        this.akas = akas;
    }

    public void setClassification(String classification) {
        if (isValidString(classification)) {
            this.classification = classification;
        } else {
            this.classification = TVRageApi.UNKNOWN;
        }
    }

    public void setCountry(String country) {
        if (isValidString(country)) {
            this.country = country;
        } else {
            this.country = TVRageApi.UNKNOWN;
        }
    }

    public void setEnded(String ended) {
        if (isValidString(ended)) {
            this.ended = ended;
        } else {
            this.ended = TVRageApi.UNKNOWN;
        }
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setNetwork(List<CountryDetail> network) {
        this.network = network;
    }

    public void setOriginCountry(String originCountry) {
        if (isValidString(originCountry)) {
            this.originCountry = originCountry;
        } else {
            this.originCountry = TVRageApi.UNKNOWN;
        }
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = convertStrToInt(runtime);
    }

    public void setShowID(int showID) {
        this.showID = showID;
    }

    public void setShowID(String showID) {
        this.showID = convertStrToInt(showID);
    }

    public void setShowLink(String showLink) {
        if (isValidString(showLink)) {
            this.showLink = showLink;
        } else {
            this.showLink = TVRageApi.UNKNOWN;
        }
    }

    public void setShowName(String showName) {
        if (isValidString(showName)) {
            this.showName = showName;
        } else {
            this.showName = TVRageApi.UNKNOWN;
        }
    }

    public void setStartDate(Date startDate) {
        this.startDate = (Date) startDate.clone();
    }

    public void setStartDate(String startDate) {
        if (isValidString(startDate)) {
            try {
                this.startDate = (new DateTime(startDate)).toDate();
            } catch (Exception ignore) {
                // We can't do anything about this error, so return
                this.startDate = null;
            }
        }
    }

    public void setStarted(int started) {
        this.started = started;
    }

    public void setStarted(String started) {
        this.started = convertStrToInt(started);
    }

    public void setStatus(String status) {
        if (isValidString(status)) {
            this.status = status;
        } else {
            this.status = TVRageApi.UNKNOWN;
        }
    }

    public void setSummary(String summary) {
        if (isValidString(summary)) {
            this.summary = summary;
        } else {
            this.summary = TVRageApi.UNKNOWN;
        }
    }

    public void setTimezone(String timezone) {
        if (isValidString(timezone)) {
            this.timezone = timezone;
        } else {
            this.timezone = TVRageApi.UNKNOWN;
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
        builder.append("[ShowInfo=[airDay=");
        builder.append(airDay);
        builder.append("][airTime=");
        builder.append(airTime);
        builder.append("][akas=");
        builder.append(akas);
        builder.append("][classification=");
        builder.append(classification);
        builder.append("][country=");
        builder.append(country);
        builder.append("][ended=");
        builder.append(ended);
        builder.append("][genres=");
        builder.append(genres);
        builder.append("][network=");
        builder.append(network);
        builder.append("][originCountry=");
        builder.append(originCountry);
        builder.append("][runtime=");
        builder.append(runtime);
        builder.append("][showID=");
        builder.append(showID);
        builder.append("][showLink=");
        builder.append(showLink);
        builder.append("][showName=");
        builder.append(showName);
        builder.append("][startDate=");
        builder.append(startDate);
        builder.append("][started=");
        builder.append(started);
        builder.append("][status=");
        builder.append(status);
        builder.append("][summary=");
        builder.append(summary);
        builder.append("][timezone=");
        builder.append(timezone);
        builder.append("][totalSeasons=");
        builder.append(totalSeasons);
        builder.append("]]");
        return builder.toString();
    }
}
