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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.daisy.factory.Factory;
import org.daisy.factory.FactoryCatalog;

/**
 * Provides an abstract base for command line UI's.
 * @author Joel Håkansson
 */
public abstract class AbstractUI {
	public enum ExitCode {OK, MISSING_ARGUMENT, UNKNOWN_ARGUMENT, FAILED_TO_READ, MISSING_RESOURCE, ILLEGAL_ARGUMENT_VALUE};
	public final static String ARG_PREFIX = "required-";
	private String delimiter;
	private String optionalArgumentPrefix;

	public static class Definition {
		private final String name;
		private final String desc;
		
		public Definition(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}

		public String getName() {
			return name;
		}
		
		public String getDescription() {
			return desc;
		}
	}
	
	public static class Argument extends Definition {
		private final List<Definition> values;
		
		public Argument(String name, String desc) {
			this(name, desc, null);
		}
		
		public Argument(String name, String desc, List<Definition> values) {
			super(name, desc);
			this.values = values;
		}

		public boolean hasValues() {
			return values!=null && values.size()>0;
		}
		
		public List<Definition> getValues() {
			return values;
		}
	}

	public static class OptionalArgument extends Argument {
		private final String defaultValue;
		
		public OptionalArgument(String name, String description, String defaultValue) {
			super(name, description);
			this.defaultValue = defaultValue;
		}
		
		public OptionalArgument(String name, String description, List<Definition> values, String defaultValue) {
			super(name, description, values);
			this.defaultValue = defaultValue;
		}
		
		public String getDefault() {
			return defaultValue;
		}

	}
	
	/**
	 * Expands the short form of the value with the given key in the provided map using the specified resolver.
	 * @param map
	 * @param key
	 * @param resolver
	 */
	public void expandShortForm(Map<String, String> map, String key, ShortFormResolver resolver) {
		String value = map.get(key);
		if (value!=null) {
			String id = resolver.resolve(value);
			if (id!=null) {
				map.put(key, id);
			} else {
				System.out.println("Unknown value for "+key+": '" + value + "'");
				System.exit(-ExitCode.ILLEGAL_ARGUMENT_VALUE.ordinal());
			}
		}
	}
	
	public List<Definition> getDefinitionList(FactoryCatalog<? extends Factory> catalog, ShortFormResolver resolver) {
		List<Definition> ret = new ArrayList<Definition>();
		for (String key : resolver.getShortForms()) {
			ret.add(new Definition(key, catalog.get(resolver.resolve(key)).getDescription()));
		}
		return ret;
	}
	
	public AbstractUI() {
		setKeyValueDelimiter("=");
		setOptionalArgumentPrefix("-");
	}
	
	public void setKeyValueDelimiter(String value) {
		delimiter = value;
	}
	
	public void setOptionalArgumentPrefix(String value) {
		if (ARG_PREFIX.equals(value)) {
			throw new IllegalArgumentException("Prefix is reserved: " + ARG_PREFIX);
		}
		optionalArgumentPrefix = value;
	}
	
	/**
	 * Gets the name for the UI
	 * @return returns the UI name
	 */
	public abstract String getName();
	
	public abstract List<Argument> getRequiredArguments();
	
	public abstract List<OptionalArgument> getOptionalArguments();

	public Map<String, String> toMap(String[] args) {
		Hashtable<String, String> p = new Hashtable<String, String>();
		int i = 0;
		String[] t;
		for (String s : args) {
			s = s.trim();
			t = s.split(delimiter, 2);
			if (s.startsWith(optionalArgumentPrefix) && t.length==2) {
				p.put(t[0].substring(1), t[1]);
			} else {
				p.put(ARG_PREFIX+i, s);
			}
			i++;
		}
		return p;
	}
	
	public void displayHelp(PrintStream ps) {
		ps.print(getName());
		if (getRequiredArguments()!=null && getRequiredArguments().size()>0) {
			for (Argument a : getRequiredArguments()) {
				ps.print(" ");
				ps.print(a.getName());
			}
		}
		if (getOptionalArguments()!=null && getOptionalArguments().size()>0) {
			ps.print(" [options ... ]");
		}
		ps.println();
		ps.println();
		if (getRequiredArguments()!=null && getRequiredArguments().size()>0) {
			for (Argument a : getRequiredArguments()) {
				ps.println("Required argument:");
				ps.println("\t" + a.getName()+ " - " + a.getDescription());
				ps.println();
				if (a.hasValues()) {
					ps.println("\tValues:");
					for (Definition value : a.getValues()) {
						ps.println("\t\t'"+value.getName() + "' - " + value.getDescription());
					}
					ps.println();
				}
			}
		}
		if (getOptionalArguments()!=null && getOptionalArguments().size()>0) {
			ps.println("Optional arguments:");
			for (OptionalArgument a : getOptionalArguments()) {
				ps.print("\t" + optionalArgumentPrefix + a.getName() + delimiter + "[value]");
				if (!a.hasValues()) {
					ps.print(" (default '"  + a.getDefault() + "')");
				}
				ps.println();
				ps.println("\t\t" + a.getDescription());
				if (a.hasValues()) {
					ps.println("\t\tValues:");
					for (Definition value : a.getValues()) {
						ps.print("\t\t\t'"+value.getName() + "' - " + value.getDescription());
						if (value.getName().equals(a.getDefault())) {
							ps.println(" (default)");
						} else {
							ps.println();
						}
					}
				}
			}
			ps.println();
		}
	}
}
