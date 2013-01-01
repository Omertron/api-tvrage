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
import static com.omertron.tvrageapi.TVRageApi.isValidString;
import java.io.Serializable;

/**
 * Class to hold country along with a generic detail string
 *
 * @author Stuart.Boston
 *
 */
public class CountryDetail implements Serializable {

    /*
     * Serial Version
     */
    private static final long serialVersionUID = 1L;
    /*
     * Properties
     */
    private String country;
    private String detail;

    public CountryDetail() {
        this.country = TVRageApi.UNKNOWN;
        this.detail = TVRageApi.UNKNOWN;
    }

    public CountryDetail(String country, String detail) {
        this.country = country;
        this.detail = detail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (isValidString(country)) {
            this.country = country.trim();
        } else {
            this.country = TVRageApi.UNKNOWN;
        }
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        if (isValidString(detail)) {
            this.detail = detail.trim();
        } else {
            this.detail = TVRageApi.UNKNOWN;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[CountryDetail=[country=");
        builder.append(country);
        builder.append("][detail=");
        builder.append(detail);
        builder.append("]]");
        return builder.toString();
    }

    public boolean isValid() {
        if (!isValidString(country)) {
            return false;
        }

        if (!isValidString(detail)) {
            return false;
        }

        return true;
    }
}
