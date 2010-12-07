package org.daisy.braille.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.daisy.factory.FactoryObject;

public class InputHelper {
	private Preferences pr;
	private LineNumberReader ln;
	
	public InputHelper(@SuppressWarnings("rawtypes") Class c) {
		pr = Preferences.userNodeForPackage(c);
		ln =  new LineNumberReader(new InputStreamReader(System.in));
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
	
	public String select(String key, List<FactoryObject> select, String name, boolean verify) {
		String value = getKey(key);
		if (value!=null) {
			// check value
			boolean ok = false;
			for (FactoryObject s : select) {
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
			for (FactoryObject s : select) {
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
