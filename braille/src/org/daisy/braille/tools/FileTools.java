package org.daisy.braille.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

public class FileTools {
	private final static Logger logger = Logger.getLogger(FileTools.class.getCanonicalName());
	
	public static void copy(InputStream is, OutputStream os) throws IOException {
		InputStream bis = new BufferedInputStream(is);
		OutputStream bos = new BufferedOutputStream(os);
		int b;
		while ((b = bis.read())!=-1) {
			bos.write(b);
		}
		bos.flush();
		bos.close();
		bis.close();
	}
	
	public static File[] listFiles(File dir, final String ext) {
		return dir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(ext);
			}});
	}
	
	public static URL[] toURL(File[] files) {
		ArrayList<URL> jars = new ArrayList<URL>();
		if (files!=null && files.length>0) {
			int i = 0;
			for (File f : files) {
				try {
					jars.add(f.toURI().toURL());
				} catch (MalformedURLException e) {
					logger.warning("Failed to convert " + f + " into an URL.");
				}
				i++;
			}
		}
		return jars.toArray(new URL[]{});
	}

}
