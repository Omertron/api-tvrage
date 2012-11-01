/*
 *      Copyright (c) 2004-2012 Stuart Boston
 *
 *      This software is licensed under a Creative Commons License
 *      See the LICENCE.txt file included in this package
 *
 *      For any reuse or distribution, you must make clear to others the
 *      license terms of this work.
 */
package com.omertron.tvrageapi;

import com.omertron.tvrageapi.TVRageApi;
import com.omertron.tvrageapi.model.Episode;
import com.omertron.tvrageapi.model.EpisodeList;
import com.omertron.tvrageapi.model.ShowInfo;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TVRageApiTest {

    private static String apikey = "";
    private TVRageApi tvr;
    private static final String SHOW_ID_STR = "15614";
    private static final int    SHOW_ID_INT = 15614;
    private static final String SHOW_NAME   = "Chuck";

    @Before
    public void setUp() throws Exception {
        tvr = new TVRageApi(apikey);
    }

    @Test
    public void testGetEpisodeInfo() {
        Episode episode = tvr.getEpisodeInfo(SHOW_ID_STR, "1", "1");
        assertTrue(episode.getTitle().equals("Pilot"));
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
