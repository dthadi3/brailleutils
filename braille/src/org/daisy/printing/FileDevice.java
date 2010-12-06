package org.daisy.printing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.print.PrintException;

public class FileDevice implements Device {
	private final File parent;
	private final String prefix;
	private final String suffix;
	private int i;
	
	/**
	 * @param parent
	 * @param prefix
	 */
	public FileDevice(File parent, String prefix, String suffix) {
		if (!parent.isDirectory()) {
			throw new IllegalArgumentException("File is not a directory: " + parent);
		}
		this.parent = parent;
		this.prefix = prefix;
		this.suffix = suffix;
		this.i = 1;
	}
	
	public FileDevice(File parent, String prefix) {
		this(parent, prefix, ".prn");
	}
	
	public FileDevice(File parent) {
		this(parent, "job_");
	}

	@Override
	public void transmit(File file) throws PrintException {
		BufferedInputStream bis;
		BufferedOutputStream bos;
		File out = new File(parent, prefix + i + suffix);
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(out));
		} catch (FileNotFoundException e) {
			throw new PrintException(e);
		}
		try {
			int b;
			while ((b = bis.read()) != -1) {
				bos.write(b);
			}
		} catch (IOException e) {
			throw new PrintException(e);
		} finally {
			try {
				bis.close();
			} catch (IOException e) {}
			try {
				bos.close();
			} catch (IOException e) {}
		}
		i++;
	}

}
