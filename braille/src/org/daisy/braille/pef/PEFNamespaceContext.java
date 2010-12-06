/*
 * Braille Utils (C) 2010 Daisy Consortium 
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.namespace.NamespaceContext;

/**
 * Provides a NamespaceContext implementation for PEF 1.0.
 * It includes namespace prefix mapping for namespaces in PEF 1.0, namely:
 * <dl>
 * 	<dt>pef</dt>
 * 	<dd>http://www.daisy.org/ns/2008/pef</dd>
 * 	<dt>dc</dt>
 * 	<dd>http://purl.org/dc/elements/1.1/</dd>
 * </dl>
 *  
 * @author Joel Håkansson, TPB
 *
 */
public class PEFNamespaceContext implements NamespaceContext {
	private final static HashMap<String, String> namespaces;
	private final static HashMap<String, String> prefixes;
	
	static {
		namespaces = new HashMap<String, String>();
		prefixes = new HashMap<String, String>();
		namespaces.put("pef", "http://www.daisy.org/ns/2008/pef");
		namespaces.put("dc", "http://purl.org/dc/elements/1.1/");
		for (String s : namespaces.keySet()) {
			prefixes.put(namespaces.get(s), s);
		}		
	}
	
	public PEFNamespaceContext() {}

	public String getNamespaceURI(String prefix) {
		return namespaces.get(prefix);
	}

	public String getPrefix(String namespaceURI) {
		return prefixes.get(namespaceURI);
	}

	public Iterator<Entry<String, String>> getPrefixes(String namespaceURI) {
		return prefixes.entrySet().iterator();
	}

}