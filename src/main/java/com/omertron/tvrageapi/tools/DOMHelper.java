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
package com.omertron.tvrageapi.tools;

import com.omertron.tvrageapi.TVRageException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.yamj.api.common.exception.ApiExceptionType;
import org.yamj.api.common.http.DigestedResponse;
import org.yamj.api.common.http.DigestedResponseReader;

/**
 * Generic set of routines to process the DOM model data
 *
 * @author Stuart.Boston
 *
 */
public class DOMHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DOMHelper.class);

    private static CloseableHttpClient httpClient;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final Charset CHARSET = Charset.forName(DEFAULT_CHARSET);
    private static final String UNABLE_TO_PARSE = "Unable to parse response, please try again later.";

    // Hide the constructor
    protected DOMHelper() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    public static void setHttpClient(CloseableHttpClient httpClient) {
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
            return tagNodeList.item(0).getNodeValue();
        }
    }

    /**
     * Get a DOM document from the supplied URL
     *
     * @param url
     * @return
     * @throws com.omertron.tvrageapi.TVRageException
     */
    public static Document getEventDocFromUrl(String url) throws TVRageException {
        Document doc = null;
        InputStream in = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("accept", "application/xml");
            final DigestedResponse response = DigestedResponseReader.requestContent(httpClient, httpGet, CHARSET);

            if (response.getStatusCode() >= 500) {
                throw new TVRageException(ApiExceptionType.HTTP_503_ERROR, url);
            } else if (response.getStatusCode() >= 300) {
                throw new TVRageException(ApiExceptionType.HTTP_404_ERROR, url);
            }

            in = new ByteArrayInputStream(response.getContent().getBytes(DEFAULT_CHARSET));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(in);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException error) {
            throw new TVRageException(ApiExceptionType.MAPPING_FAILED, UNABLE_TO_PARSE, url, error);
        } catch (SAXException error) {
            throw new TVRageException(ApiExceptionType.MAPPING_FAILED, UNABLE_TO_PARSE, url, error);
        } catch (IOException error) {
            throw new TVRageException(ApiExceptionType.MAPPING_FAILED, UNABLE_TO_PARSE, url, error);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // Input Stream was already closed or null
                LOG.trace("Stream already closed for getEventDocFromUrl", ex);
            }
        }
        return doc;
    }
}
