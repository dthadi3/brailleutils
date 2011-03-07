package org.daisy.braille.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class FileCompare {
	//private final static String TRANSFORMER_FACTORY_KEY = "javax.xml.transform.TransformerFactory";

	public FileCompare() { }

	public boolean compareXML(InputStream f1, InputStream f2) throws IOException, TransformerException {
		//String originalTransformer = System.getProperty(TRANSFORMER_FACTORY_KEY);
		//System.setProperty(TRANSFORMER_FACTORY_KEY, "net.sf.saxon.TransformerFactoryImpl");
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			factory.setAttribute("http://saxon.sf.net/feature/version-warning", Boolean.FALSE);
		} catch (IllegalArgumentException iae) { 
			iae.printStackTrace();
		}
        File t1 = File.createTempFile("FileCompare", ".tmp");
        File t2 = File.createTempFile("FileCompare", ".tmp");
        try {
	        StreamSource xml1 = new StreamSource(f1);
	        StreamSource xml2 = new StreamSource(f2);
	        Source xslt;
	        Transformer transformer;
	        
	        xslt = new StreamSource(this.getClass().getResourceAsStream("resource-files/normalize.xsl"));
	        transformer = factory.newTransformer(xslt);
	        transformer.transform(xml1, new StreamResult(t1));
	        
	        xslt = new StreamSource(this.getClass().getResourceAsStream("resource-files/normalize.xsl"));
	        transformer = factory.newTransformer(xslt);
	        transformer.transform(xml2, new StreamResult(t2));
	
	        return compareBinary(new FileInputStream(t1), new FileInputStream(t2));
        } finally {
        	if (!t1.delete()) {
        		t1.deleteOnExit();
        	}
        	if (!t2.delete()) {
        		t2.deleteOnExit();
        	}
        	/*
        	if (originalTransformer!=null) {
        		System.setProperty(TRANSFORMER_FACTORY_KEY, originalTransformer);
        	} else {
        		System.clearProperty(TRANSFORMER_FACTORY_KEY);
        	}*/
        }
	}
	
	public boolean compareBinary(InputStream f1, InputStream f2) throws IOException {
		InputStream bf1 = new BufferedInputStream(f1);
		InputStream bf2 = new BufferedInputStream(f2);
		try {
			int b1;
			int b2;
			while ((b1 = bf1.read())!=-1 & b1 == (b2 = bf2.read())) { 
				//continue
			}
			if (b1!=-1 || b2!=-1) {
				return false;
			}
			return true;
		} finally {
			bf1.close();
			bf2.close();
		}
	}
}
