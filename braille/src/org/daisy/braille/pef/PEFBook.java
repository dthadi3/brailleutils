/*
 * Braille Utils (C) 2010-2011 Daisy Consortium 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.daisy.braille.pef;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Provides metadata about a PEF-file. 
 * 
 */
public class PEFBook {
	private static final Pattern eightDotPattern = Pattern.compile("[\u2840-\u28ff]");
	
	private final HashMap<String, List<String>> metadata;
	
	// Book properties
	private final int volumes;
	private final int pageTags;
	private final int pages;
	private final int maxWidth;
	private final int maxHeight;
	private final String inputEncoding;
	private final boolean containsEightDot;
	private final int[] startPages;

	/**
	 * Parses the given uri as a PEF-file and collects information about the file.
	 * @param uri an uri to a PEF-file
	 * @deprecated deprecated because of poor exception handling, use static method load instead.
	 */
	public PEFBook(URI uri) {
		PEFBook book;
		try {
			book = load(uri, true);
			// copy fields to this object
			this.volumes = book.volumes;
			this.pages = book.pages;
			this.startPages = book.startPages;
			this.containsEightDot = book.containsEightDot;
			this.pageTags = book.pageTags;
			this.inputEncoding = book.inputEncoding;
			this.metadata = book.metadata;
			this.maxWidth = book.maxWidth;
			this.maxHeight = book.maxHeight;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static PEFBook load(URI uri) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
		return load(uri, false);
	}

	private static PEFBook load(URI uri, boolean continueOnError) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		HashMap<String, List<String>> metadata;
		// Book properties
		int volumes;
		int pageTags;
		int pages;
		int maxWidth;
		int maxHeight;
		String inputEncoding;
		boolean containsEightDot;
		int[] startPages;
		int tmp = 0;
		Document d = null;
		String encoding = null;
		metadata = new HashMap<String, List<String>>();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			d = db.parse(uri.toString());
			encoding = d.getInputEncoding();
			List<String> al;
			NodeList nl = d.getDocumentElement().getElementsByTagName("meta").item(0).getChildNodes();
			for (int i=0; i<nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n!=null && n.getNodeType()==Node.ELEMENT_NODE) {
					if ("http://purl.org/dc/elements/1.1/".equals(n.getNamespaceURI())) { 
						String name = n.getLocalName();
						if (metadata.containsKey(name)) {
							al = metadata.remove(name);
						} else {
							al = new ArrayList<String>();
						}
						al.add(n.getTextContent());
						metadata.put(name, al);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			if (continueOnError) {
				e.printStackTrace();
			} else {
				throw e;
			}
		} catch (SAXException e) {
			if (continueOnError) {
				e.printStackTrace();
			} else {
				throw e;
			}
		} catch (IOException e) {
			if (continueOnError) {
				e.printStackTrace();
			} else {
				throw e;
			}
		}
		
		inputEncoding = encoding;

		XPath xp = XPathFactory.newInstance().newXPath();
		xp.setNamespaceContext(new PEFNamespaceContext());

		// Count volumes
		tmp = 0;
		try {
			tmp = ((Double)xp.evaluate("count(//pef:volume)", d, XPathConstants.NUMBER)).intValue();
		} catch (XPathExpressionException e) {
			tmp = 0;
		}
		volumes = tmp;
		
		// Count page tags
		tmp = 0;
		try {
			tmp = ((Double)xp.evaluate("count(//pef:page)", d, XPathConstants.NUMBER)).intValue();
		} catch (XPathExpressionException e) {
			if (continueOnError) {
				tmp = 0;
			} else {
				throw e;
			}
		}
		pageTags = tmp;

		// Count pages including blank
		tmp = 0;
		try {
			tmp = ((Double)xp.evaluate(
				"count(//pef:section[ancestor-or-self::pef:*[@duplex][1][@duplex='false']]/descendant::pef:page)*2 + count(//pef:section[ancestor-or-self::pef:*[@duplex][1][@duplex='true']]/descendant::pef:page) + count(//pef:section[count(descendant::pef:page) mod 2 = 1][ancestor-or-self::pef:*[@duplex][1][@duplex='true']])-count(((//pef:section)[last()])[count(descendant::pef:page) mod 2 = 1][ancestor-or-self::pef:*[@duplex][1][@duplex='true']])", d, XPathConstants.NUMBER)).intValue();
		} catch (XPathExpressionException e) {
			if (continueOnError) {
				tmp = 0;
			} else {
				throw e;
			}
		}
		pages = tmp;
		
		// Get max width
		tmp = 0;
		try {
			NodeList ns = (NodeList)xp.evaluate("//pef:*/@cols", d, XPathConstants.NODESET);
			for (int i = 0; i < ns.getLength(); ++i) {
				Attr attr = (Attr)ns.item(i);
				String colsValue = attr.getNodeValue();
				tmp = Math.max(tmp, Integer.valueOf(colsValue));				
			}
		} catch (XPathExpressionException e) {
			if (continueOnError) {
				tmp = 0;
			} else {
				throw e;
			}
		}
		maxWidth = tmp;
		
		// Get max height
		tmp = 0;
		try {
			NodeList ns = (NodeList)xp.evaluate("//pef:*/@rows", d, XPathConstants.NODESET);
			for (int i = 0; i < ns.getLength(); ++i) {
				Attr attr = (Attr)ns.item(i);
				String colsValue = attr.getNodeValue();
				tmp = Math.max(tmp, Integer.valueOf(colsValue));				
			}
		} catch (XPathExpressionException e) {
			if (continueOnError) {
				tmp = 0;
			} else {
				throw e;
			}
		}
		maxHeight = tmp;
		
		// Contains eight dot?
		boolean bTmp = false;
		try {
			NodeList texts = (NodeList)xp.evaluate("//pef:row/text()", d, XPathConstants.NODESET);
			for (int i = 0; i < texts.getLength(); ++i) {
				String text = texts.item(i).getTextContent();
				if (eightDotPattern.matcher(text).find()) {
					bTmp = true;
				}
			}
		} catch (XPathExpressionException e) {
			if (!continueOnError) {
				throw e;
			}
		}
		containsEightDot = bTmp;
		
		// get start pages
		startPages = new int[volumes];
		for (int i = 1; i <= volumes; i++) {
			try {
				Node page = (Node)xp.evaluate("(//pef:volume)[position()="+(i)+"]/descendant::pef:page[1]", d, XPathConstants.NODE);
				int pageOffset = ((Double)xp.evaluate("count(preceding::pef:section[ancestor-or-self::pef:*[@duplex][1][@duplex='false']]/descendant::pef:page)*2 + count(preceding::pef:section[ancestor-or-self::pef:*[@duplex][1][@duplex='true']]/descendant::pef:page) + count(preceding::pef:section[count(descendant::pef:page) mod 2 = 1][ancestor-or-self::pef:*[@duplex][1][@duplex='true']])", page, XPathConstants.NUMBER)).intValue();
				startPages[i-1] = pageOffset + 1;
			} catch (XPathExpressionException e) { 
				if (continueOnError) {
					e.printStackTrace();
					startPages[i-1] = 0;
				} else {
					throw e;
				}
			}
		}
		return new PEFBook(metadata, volumes, pages, pageTags, maxWidth, maxHeight, inputEncoding, containsEightDot, startPages);
	}
	
	private PEFBook(HashMap<String, List<String>> metadata, int volumes, int pages, int pageTags, int maxWidth, int maxHeight, String inputEncoding, boolean containsEightDot, int[] startPages) {
		this.metadata = metadata;
		this.volumes = volumes;
		this.pages = pages;
		this.pageTags = pageTags;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.inputEncoding = inputEncoding;
		this.containsEightDot = containsEightDot;
		this.startPages = startPages;
	}

	/**
	 * Gets the encoding used for this document at the time of the parsing.
	 */
	public String getInputEncoding() {
		return inputEncoding;
	}
	
	/**
	 * Gets the number of volumes in this document.
	 */
	public int getVolumes() {
		return volumes;
	}
	
	/**
	 * Gets the total number of pages in this document
	 */
	public int getPages() {
		return pages;
	}
	
	/**
	 * Gets the number of page tags in this document.
	 */
	public int getPageTags() {
		return pageTags;
	}
	
	/**
	 * Gets the number of sheets in this document.
	 */
	public int getSheets() {
		return (pages+1)/2;
	}

	/**
	 * Gets the number of sheets in the specified volume
	 * @param volume the desired volume, where the first volume is 1 and the last equals getVolumes
	 * @return returns the number of sheets in the specified volume
	 * @throws IllegalArgumentException if the volume is less than 1 or greater than getVolumes
	 */
	public int getSheets(int volume) {
		if (volume<1 || volume > getVolumes()) {
			throw new IllegalArgumentException("Value out of range: " + volume);
		}
		return ((getLastPage(volume)-(getFirstPage(volume)-1))+1)/2;
	}
	
	/**
	 * Gets the first page number in the specified volume
	 * @param volume the desired volume, where the first volume is 1 and the last equals getVolumes.
	 * @return returns the first page number in the specified volume
	 * @throws IllegalArgumentException if the volume is less than 1 or greater than getVolumes
	 */
	public int getFirstPage(int volume) {
		if (volume<1 || volume > getVolumes()) {
			throw new IllegalArgumentException("Value out of range: " + volume);
		}
		return startPages[volume - 1];
	}
	
	/**
	 * Gets the last page number in the specified volume
	 * @param volume the desired volume (the first volume is 1 and the last is getVolumes)
	 * @return returns the last page number in the specified volume
	 * @throws IllegalArgumentException if the volume is less than 1 or greater than getVolumes
	 */
	public int getLastPage(int volume) {
		if (volume<1 || volume > getVolumes()) {
			throw new IllegalArgumentException("Value out of range: " + volume);
		}
		if (volume == getVolumes()) {
			return getPages();
		} else {
			// since startPages is zero based, "volume" means next volume
			return startPages[volume] - 1;
		}
	}
	
	/**
	 * Gets the maximum defined page width, in chars 
	 */
	public int getMaxWidth() {
		return maxWidth;
	}
	
	/**
	 * Gets the maximum defined page height, in rows
	 */
	public int getMaxHeight() {
		return maxHeight;
	}
	
	/**
	 * Returns true if this document contains eight dot patterns, false otherwise
	 */
	public boolean containsEightDot() {
		return containsEightDot;
	}
/*
	public HashMap<String, List<String>> getMetadata() {
		return metadata;
	}
*/	
	/**
	 * Gets a collection of all metadata keys in this document.
	 * A metadata key is a local element name in the http://purl.org/dc/elements/1.1/ namespace.
	 * 
	 */
	public Iterable<String> getMetadataKeys() {
		return metadata.keySet();
	}

	/**
	 * Gets a collection of values for a specfied metadata key.
	 * A metadata key is a local element name in the http://purl.org/dc/elements/1.1/ namespace.
	 * @param key the metadata to get values for
	 */
	public Iterable<String> getMetadata(String key) {
		List<String> c = metadata.get(key);
		if (c!=null) {
			return c;
		}
		return null;
		/*
		if (metadata.containsKey(key)) {
			String[] ret = new String[metadata.get(key).size()];
			metadata.get(key).toArray(ret);
			return ret;
		}
		return null;*/
	}
	
	/**
	 * Gets the document title from this document's metadata. Convenience method for getMetadata("title")  
	 */
	public Iterable<String> getTitle() {
		return getMetadata("title");
	}
	
	/**
	 * Gets the document authors from this document's metadata. Convenience method for getMetadata("creator")
	 */
	public Iterable<String> getAuthors() {
		return getMetadata("creator");
	}

}