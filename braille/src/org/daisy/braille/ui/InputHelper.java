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
package org.daisy.braille.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.daisy.factory.Factory;

public class InputHelper {
	private Preferences pr;
	private LineNumberReader ln;
	
	public InputHelper(@SuppressWarnings("rawtypes") Class c) {
		pr = Preferences.userNodeForPackage(c);
		ln =  new LineNumberReader(new InputStreamReader(System.in));
	}
	
	public InputHelper() {
		// Determining the calling class.
		// ca[0] is the anonymous security manager
		// ca[1] is this class
		// ca[2] is the calling class
		this((new SecurityManager() { @SuppressWarnings("rawtypes")
			   public Class[] getClassContext() { return super.getClassContext(); }}.getClassContext())[2]
		);
	}

	public String select(String key, String[] select, String name, boolean verify) {
		String value = getKey(key);
		if (value!=null) {
			// check value
			boolean ok = false;
			for (String s : select) {
				if (value.equals(s)) {
					ok = true;
					break;
				}
			}
			if (!ok) {
				// reset value
				value = null;
			}
		}
		if (value==null || verify) {
			// ask user
			System.out.println("Choose " + name + ": ");
			int i = 1;
			for (String s : select) {
				System.out.print(i + ". " + s);
				if (value!=null && s.equals(value)) {
					System.out.print(" (current value, hit enter to keep this value)");
				}
				System.out.println();				i++;
			}
			int sel = getInput()-1;
			if (sel==-1 && value!=null) {
				return value;
			} else if (sel<0) {
				throw new RuntimeException("Exception");
			}
			value = select[sel];
			pr.put(key, value);
		}
		return value;
	}
	
	public String select(String key, List<Factory> select, String name, boolean verify) {
		String value = getKey(key);
		if (value!=null) {
			// check value
			boolean ok = false;
			for (Factory s : select) {
				if (value.equals(s.getIdentifier())) {
					ok = true;
					break;
				}
			}
			if (!ok) {
				// reset value
				value = null;
			}
		}
		if (value==null || verify) {
			// ask user
			System.out.println("Choose " + name + ": ");
			int i = 1;
			for (Factory s : select) {
				System.out.print(i + ". " + s.getDisplayName());
				if (value!=null && s.getIdentifier().equals(value)) {
					System.out.print(" (current value, hit enter to keep this value)");
				}
				System.out.println();
				i++;
			}
			int sel = getInput()-1;
			if (sel==-1 && value!=null) {
				return value;
			} else if (sel<0) {
				throw new RuntimeException("Exception");
			}
			value = select.get(sel).getIdentifier();
			pr.put(key, value);
		}
		return value;
	}
	
	public String getKey(String key) {
		return pr.get(key, null);
	}
	
	public int getInput()  {
		System.out.print("Input: ");
		try {
			String line = ln.readLine();
			if (line.equals("")) {
				return 0;
			} else {
				return Integer.parseInt(line);
			}
		} catch (IOException e) {
			return -1;
		}
	}
	
	public void clearSettings() throws BackingStoreException {
		pr.clear();
		pr.flush();
	}

}
