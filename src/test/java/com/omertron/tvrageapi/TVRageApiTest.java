/*
 *      Copyright (c) 2004-2016 Stuart Boston
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
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TVRageApiTest {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(TVRageApiTest.class);
    private static final String APIKEY = "1tyJ0xqGoNMyZTaD1AY7";
    private final TVRageApi tvr;
    private static final String SHOW_ID_STR = "15614";
    private static final int SHOW_ID_INT = 15614;
    private static final String SHOW_NAME = "Chuck";

    public TVRageApiTest() {
        tvr = new TVRageApi(APIKEY);
    }

    @BeforeClass
    public static void setUpClass() {
        TestLogger.Configure();
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetEpisodeInfo() throws TVRageException {
        LOG.info("test getEpisodeInfo");
        Episode episode = tvr.getEpisodeInfo(SHOW_ID_STR, "1", "1");
        assertTrue(episode.getTitle().equals("Chuck Versus the Intersect"));
    }

    @Test
    public void testGetEpisodeList() throws TVRageException {
        LOG.info("test getEpisodeList");
        EpisodeList episodeList = tvr.getEpisodeList(SHOW_ID_STR);
        assertFalse(episodeList.getShowName().equals(TVRageApi.UNKNOWN));
        assertFalse(episodeList.getEpisodes().isEmpty());
    }

    @Test
    public void testGetShowInfoInt() throws TVRageException {
        LOG.info("test getShowInfoInt");
        ShowInfo showInfo = tvr.getShowInfo(SHOW_ID_INT);
        assertFalse(showInfo.getShowName().equals(TVRageApi.UNKNOWN));
    }

    @Test
    public void testGetShowInfoString() throws TVRageException {
        LOG.info("test getShowInfoString");
        ShowInfo showInfo = tvr.getShowInfo(SHOW_ID_STR);
        assertFalse(showInfo.getShowName().equals(TVRageApi.UNKNOWN));
    }

    @Test
    public void testSearchShow() throws TVRageException {
        LOG.info("test searchShow");
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
