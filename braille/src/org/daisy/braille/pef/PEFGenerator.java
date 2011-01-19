package org.daisy.braille.pef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Provides a way to generate PEF-files for testing purposes.
 * The files can be configured to contain a specified number of
 * volumes, pages, rows and columns. The duplex property can also
 * be set. The file is filed with random content in the specified
 * braille range (6- or 8-dot). 
 * 
 * @author Joel Håkansson
 *
 */
public class PEFGenerator {
	/**
	 * Key used in the settings map passed to the constructor. Its value defines
	 * the number of volumes
	 * in the generated file 
	 */
	public static String KEY_VOLUMES = "volumes";
	/**
	 * Key used in the settings map passed to the constructor. Its value defines
	 * the number of pages per volume
	 * in the generated file 
	 */

	public static String KEY_PPV = "pages";
	/**
	 * Key used in the settings map passed to the constructor. Its value defines
	 * if eight dot should be used (true/false) 
	 */
	public static String KEY_EIGHT_DOT = "eightdot";
	/**
	 * Key used in the settings map passed to the constructor. Its value defines
	 * the maximum number of rows on a page
	 * in the generated file 
	 */
	public static String KEY_ROWS = "rows";
	/**
	 * Key used in the settings map passed to the constructor. Its value defines
	 * the maximum number of columns on a page
	 * in the generated file 
	 */
	public static String KEY_COLS = "cols";
	/**
	 * Key used in the settings map passed to the constructor. Its value defines
	 * the value of the duplex property (true/false).
	 * Note that the value of this property does not affect the number of pages generated in each volume.
	 */
	public static String KEY_DUPLEX = "duplex";

	private final static Map<String, String> defaults;
	static {
		defaults = new HashMap<String, String>();
		defaults.put(KEY_VOLUMES, "3");
		defaults.put(KEY_PPV, "20");
		defaults.put(KEY_EIGHT_DOT, "false");
		defaults.put(KEY_ROWS, "29");
		defaults.put(KEY_COLS, "32");
		defaults.put(KEY_DUPLEX, "true");
	}
	
	private int volumes;
	private int pagesPerVolume;
	private boolean eightDot;
	private int rows;
	private int cols;
	private boolean duplex;
	
	/**
	 * Creates a new PEFGenerator with the default settings.
	 */
	public PEFGenerator() {
		this(new HashMap<String, String>());
	}
	
	/**
	 * Creates a new PEF generator with the supplied optional settings. See the
	 * enums of this class for a list of possible keys and their values.
	 * @param p a map containing optional settings
	 */
	public PEFGenerator(Map<String, String> p) {
		volumes = Integer.parseInt(get(KEY_VOLUMES, p));
		pagesPerVolume = Integer.parseInt(get(KEY_PPV, p));
		eightDot = Boolean.parseBoolean(get(KEY_EIGHT_DOT, p));
		rows = Integer.parseInt(get(KEY_ROWS, p));
		cols = Integer.parseInt(get(KEY_COLS, p));
		duplex = Boolean.parseBoolean(get(KEY_DUPLEX, p));
		if (volumes<1) {
			throw new IllegalArgumentException("Volumes must be larger than 0");
		}
		if (pagesPerVolume<1) {
			throw new IllegalArgumentException("Pages per volume must be larger than 0");
		}
		if (rows<1) {
			throw new IllegalArgumentException("Rows must be larger than 0");
		}
		if (cols<1) {
			throw new IllegalArgumentException("Cols must be larger than 0");
		}
	}
	
	/**
	 * Gets a list of all keys which has default values 
	 * @return returns a list of keys
	 */
	public static Set<String> getOptionalArgumentKeys() {
		return defaults.keySet();
	}
	
	/**
	 * Gets the default value for a specified key.
	 * @param key The key to get the default value for
	 * @return returns the value for the key, or null if the key is not found
	 */
	public static String getDefaultValue(String key) {
		return defaults.get(key);
	}

	String get(String key, Map<String, String> options) {
		return checkEmpty(options.get(key), defaults.get(key));
	}
	
	String checkEmpty(String input, String def) {
		if (input==null || "".equals(input)) {
			return def;
		}
		return input;
	}

	/**
	 * Generates a new PEF-file and writes it to the supplied path 
	 * @param output the output file
	 * @throws FileNotFoundException If the given file object does not denote an existing, 
	 * writable regular file and a new regular file of that name cannot be created, or if
	 * some other error occurs while opening or creating the file
	 */
	public void generateTestBook(File output) throws FileNotFoundException {
		PrintWriter pw;
		try {
			pw = new PrintWriter(output, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Should never happen for UTF-8
			throw new RuntimeException("Unexpected error.");
		}
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		pw.println("<pef xmlns=\"http://www.daisy.org/ns/2008/pef\" version=\"2008-1\">");
		pw.println("\t<head>");
		pw.println("\t\t<meta xmlns:dc=\"http://purl.org/dc/elements/1.1/\">");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		pw.println("\t\t\t<dc:identifier>"+Integer.toHexString((int)(Math.random()*1000000))+" "+sdf.format((new Date())) + "</dc:identifier>");
		pw.println("\t\t\t<dc:format>application/x-pef+xml</dc:format>");
		pw.println("\t\t\t<dc:title>Generated document</dc:title>");
		pw.println("\t\t\t<dc:creator>PEF Generator</dc:creator>");
		pw.println("\t\t\t<dc:description>Document generated for test purposes containing random characters.</dc:description>");
		pw.println("\t\t</meta>");
		pw.println("\t</head>");
		pw.println("\t<body>");
		int cpr = 0;
		int rpp = 0;
		int range = (eightDot?256:64);
		int rowgap = (eightDot?1:0);
		for (int v=0; v<volumes; v++) {
			pw.println("\t\t<volume cols=\""+cols+"\" rows=\""+(rows + (int)Math.ceil((rows*rowgap)/4d))+"\" rowgap=\""+rowgap+"\" duplex=\""+duplex+"\">");
			pw.println("\t\t\t<section>");
			for (int ppv=0; ppv<pagesPerVolume; ppv++) {
				pw.println("\t\t\t\t<page>");
				rpp = (int)(Math.floor(Math.random()*(rows+1)));
				for (int r=0; r<rpp; r++) {
					pw.print("\t\t\t\t\t<row>");
					cpr = (int)(Math.floor(Math.random()*(cols+1)));
					for (int c=0; c<cpr; c++) {
						pw.print((char)(0x2800+(int)Math.floor(Math.random()*(range))));
					}
					pw.println("</row>");
				}
				pw.println("\t\t\t\t</page>");
			}
			pw.println("\t\t\t</section>");
			pw.println("\t\t</volume>");
		}
		pw.println("\t</body>");
		pw.print("</pef>");
		pw.close();
	}
	
}
