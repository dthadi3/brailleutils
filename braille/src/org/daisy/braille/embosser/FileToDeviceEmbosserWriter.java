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

	@Override
	public int getRowGap() {
		return w.getRowGap();
	}

	@Override
	public boolean isClosed() {
		return w.isClosed();
	}

	@Override
	public boolean isOpen() {
		return w.isOpen();
	}

	@Override
	public void newLine() throws IOException {
		w.newLine();
	}

	@Override
	public void newPage() throws IOException {
		w.newPage();
	}

	@Override
	public void newSectionAndPage(boolean duplex) throws IOException {
		w.newSectionAndPage(duplex);
	}

	@Override
	public void newVolumeSectionAndPage(boolean duplex) throws IOException {
		w.newVolumeSectionAndPage(duplex);
	}

	@Override
	public void open(boolean duplex) throws IOException {
		w.open(duplex);
	}

	@Override
	public void setRowGap(int value) {
		w.setRowGap(value);
	}

	@Override
	public void write(String braille) throws IOException {
		w.write(braille);
	}

	@Override
	public int getMaxHeight() {
		return w.getMaxHeight();
	}

	@Override
	public int getMaxWidth() {
		return w.getMaxWidth();
	}

	@Override
	public boolean supports8dot() {
		return w.supports8dot();
	}

	@Override
	public boolean supportsAligning() {
		return w.supportsAligning();
	}

	@Override
	public boolean supportsDuplex() {
		return w.supportsDuplex();
	}

	@Override
	public boolean supportsVolumes() {
		return w.supportsVolumes();
	}

	@Override
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