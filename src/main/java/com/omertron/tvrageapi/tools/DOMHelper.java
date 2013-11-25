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
package com.omertron.tvrageapi.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.yamj.api.common.http.CommonHttpClient;

/**
 * Generic set of routines to process the DOM model data
 *
 * @author Stuart.Boston
 *
 */
public class DOMHelper {

    private static CommonHttpClient httpClient;

    // Hide the constructor
    protected DOMHelper() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    public static void setHttpClient(CommonHttpClient httpClient) {
        DOMHelper.httpClient = httpClient;
    }

    /**
     * Gets the string value of the tag element name passed
     *
     * @param element
     * @param tagName
     * @return
     */
    public static String getValueFromElement(Element element, String tagName) {
        NodeList elementNodeList = element.getElementsByTagName(tagName);
        if (elementNodeList == null) {
            return "";
        } else {
            Element tagElement = (Element) elementNodeList.item(0);
            if (tagElement == null) {
                return "";
            }

            NodeList tagNodeList = tagElement.getChildNodes();
            if (tagNodeList == null || tagNodeList.getLength() == 0) {
                return "";
            }
            return ((Node) tagNodeList.item(0)).getNodeValue();
        }
    }

    /**
     * Get a DOM document from the supplied URL
     *
     * @param url
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static Document getEventDocFromUrl(String url)
            throws IOException, ParserConfigurationException, SAXException {
        Document doc = null;
        InputStream in = null;
        String webPage;

        try {
            webPage = httpClient.requestContent(url);

            in = new ByteArrayInputStream(webPage.getBytes("UTF-8"));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(in);
            doc.getDocumentElement().normalize();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return doc;
    }
}
