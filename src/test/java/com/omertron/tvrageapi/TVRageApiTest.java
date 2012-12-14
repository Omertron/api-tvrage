/*
 *      Copyright (c) 2004-2012 Stuart Boston
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
package com.omertron.tvrageapi;

import com.omertron.tvrageapi.model.Episode;
import com.omertron.tvrageapi.model.EpisodeList;
import com.omertron.tvrageapi.model.ShowInfo;
import com.omertron.tvrageapi.tools.FilteringLayout;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TVRageApiTest {

    private static String apikey = "1tyJ0xqGoNMyZTaD1AY7";
    private TVRageApi tvr;
    private static final String SHOW_ID_STR = "15614";
    private static final int    SHOW_ID_INT = 15614;
    private static final String SHOW_NAME   = "Chuck";

    @Before
    public void setUp() throws Exception {
        tvr = new TVRageApi(apikey);
        // Set the logger level to TRACE
        Logger.getRootLogger().setLevel(Level.TRACE);
        // Make sure the filter isn't applied to the test output
        FilteringLayout.addReplacementString("DO_NOT_MATCH");
    }

    @Test
    public void testGetEpisodeInfo() {
        Episode episode = tvr.getEpisodeInfo(SHOW_ID_STR, "1", "1");
        assertTrue(episode.getTitle().equals("Chuck Versus the Intersect"));
    }

    @Test
    public void testGetEpisodeList() {
        EpisodeList episodeList = tvr.getEpisodeList(SHOW_ID_STR);
        assertFalse(episodeList.getShowName().equals(TVRageApi.UNKNOWN));
        assertFalse(episodeList.getEpisodeList().isEmpty());
    }

    @Test
    public void testGetShowInfoInt() {
        ShowInfo showInfo = tvr.getShowInfo(SHOW_ID_INT);
        assertFalse(showInfo.getShowName().equals(TVRageApi.UNKNOWN));
    }

    @Test
    public void testGetShowInfoString() {
        ShowInfo showInfo = tvr.getShowInfo(SHOW_ID_STR);
        assertFalse(showInfo.getShowName().equals(TVRageApi.UNKNOWN));
    }

    @Test
    public void testSearchShow() {
        boolean found = false;

        List<ShowInfo> showList = tvr.searchShow(SHOW_NAME);
        for (ShowInfo showInfo : showList) {
            if (showInfo.getShowName().equalsIgnoreCase(SHOW_NAME)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

}
