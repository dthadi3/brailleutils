package org.daisy.braille.embosser;

import java.io.File;
import java.io.IOException;

import javax.print.PrintException;

import org.daisy.printing.Device;

/**
 * Provides a bridge between a PrinterDevice and file based EmbosserWriter
 * @author joha
 *
 */
public class FileToDeviceEmbosserWriter implements EmbosserWriter {
	private final EmbosserWriter w;
	private final File f;
	private final Device bd;
	
	public FileToDeviceEmbosserWriter(EmbosserWriter w, File f, Device bd) {
		this.w = w;
		this.bd = bd;
		this.f = f;
	}

	//jvm1.6@Override
	public int getRowGap() {
		return w.getRowGap();
	}

	//jvm1.6@Override
	public boolean isClosed() {
		return w.isClosed();
	}

	//jvm1.6@Override
	public boolean isOpen() {
		return w.isOpen();
	}

	//jvm1.6@Override
	public void newLine() throws IOException {
		w.newLine();
	}

	//jvm1.6@Override
	public void newPage() throws IOException {
		w.newPage();
	}

	//jvm1.6@Override
	public void newSectionAndPage(boolean duplex) throws IOException {
		w.newSectionAndPage(duplex);
	}

	//jvm1.6@Override
	public void newVolumeSectionAndPage(boolean duplex) throws IOException {
		w.newVolumeSectionAndPage(duplex);
	}

	//jvm1.6@Override
	public void open(boolean duplex) throws IOException {
		w.open(duplex);
	}

	//jvm1.6@Override
	public void setRowGap(int value) {
		w.setRowGap(value);
	}

	//jvm1.6@Override
	public void write(String braille) throws IOException {
		w.write(braille);
	}

	//jvm1.6@Override
	public int getMaxHeight() {
		return w.getMaxHeight();
	}

	//jvm1.6@Override
	public int getMaxWidth() {
		return w.getMaxWidth();
	}

	//jvm1.6@Override
	public boolean supports8dot() {
		return w.supports8dot();
	}

	//jvm1.6@Override
	public boolean supportsAligning() {
		return w.supportsAligning();
	}

	//jvm1.6@Override
	public boolean supportsDuplex() {
		return w.supportsDuplex();
	}

	//jvm1.6@Override
	public boolean supportsVolumes() {
		return w.supportsVolumes();
	}

	//jvm1.6@Override
	public void close() throws IOException {
		w.close();
		try {
			bd.transmit(f);
		} catch (PrintException e) {
			IOException e2 = new IOException();
			e2.initCause(e);
			throw e2;
		}
	}

}