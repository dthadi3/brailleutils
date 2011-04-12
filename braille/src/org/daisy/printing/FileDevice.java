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

	//jvm1.6@Override
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
