package org.daisy.braille.pef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.daisy.factory.AbstractFactoryObject;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.util.PropertyMapBuilder;
import com.thaiopensource.validate.ValidateProperty;
import com.thaiopensource.validate.ValidationDriver;

public class PEFValidator extends AbstractFactoryObject implements org.daisy.validator.Validator {
	public enum Mode {LIGHT_MODE, FULL_MODE};
	private File report;
	
	public PEFValidator() {
		this(null);
		report = null;
	}
	
	private PEFValidator(String id) {
		super("PEF Validator", "A validator for PEF 1.0 files.", PEFValidator.class.getCanonicalName());
	}
	
	public boolean validate(URL input) {
		return validate(input, Mode.FULL_MODE);
	}
	
	public boolean validate(URL input, Mode mode) {
		boolean hasSchematron = true;
		String schemaPath = "pef-2008-1-full.rng";
		switch (mode) {
			case FULL_MODE: 
				schemaPath = "pef-2008-1-full.rng";
				hasSchematron = true;
				break;
			case LIGHT_MODE: 
				schemaPath = "pef-2008-1-light.rng";
				hasSchematron = false;
				break;
		}
		PrintStream orErr = System.err;
		try {
			report = File.createTempFile("Validator", ".tmp");
			report.deleteOnExit();
		} catch (IOException e1) {
			report = null;
		}
		
		PrintStream ps = null;
		try {
			ps = new PrintStream(report);
		} catch (FileNotFoundException e) { }
		try {

			PropertyMapBuilder propertyBuilder = new PropertyMapBuilder();
			propertyBuilder.put(ValidateProperty.ERROR_HANDLER, new TestError());
			System.setErr(ps);
			
			//ValidationDriver vd = new ValidationDriver(propertyBuilder.toPropertyMap());
			//InputStream schemaStream = ClassLoader.getSystemResourceAsStream("org/daisy/pef/pef-2008-1.rng");
			//vd.loadSchema(new InputSource(schemaStream));
			boolean ok;
			ok = runValidation(propertyBuilder.toPropertyMap(), input, this.getClass().getResource(schemaPath));
			if (hasSchematron) {
				try {
					File schematron = transformSchematron(this.getClass().getResource(schemaPath));
					ok &= runValidation(propertyBuilder.toPropertyMap(), input,schematron.toURI().toURL());
				} catch (Exception e) {
					e.printStackTrace();
					ok = false;
				}
			}
			return ok;
		} finally {
			if (ps != null) {
				ps.close();
			}
			System.setErr(orErr);
		}
	}
	
	private boolean runValidation(PropertyMap map, URL url, URL schema) {
        ValidationDriver vd = new ValidationDriver(map);
        try {
			vd.loadSchema(new InputSource(schema.openStream()));
			return vd.validate(new InputSource(url.openStream()));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private File transformSchematron(URL schema) throws IOException, SAXException, TransformerException {

        InputSource schSource;
        File schematronSchema = File.createTempFile("schematron", ".tmp");
        schematronSchema.deleteOnExit();

        // Use XSLT to strip out Schematron rules
        Source xml = new StreamSource(schema.toString());
        
        Source xslt = new StreamSource(this.getClass().getResourceAsStream("RNG2Schtrn.xsl"));
        TransformerFactory factory = TransformerFactory.newInstance();
        System.err.println(factory.getClass().getName()); 
		try {
			factory.setAttribute("http://saxon.sf.net/feature/version-warning", Boolean.FALSE);
		} catch (IllegalArgumentException iae) { }
        Transformer transformer = factory.newTransformer(xslt);

        transformer.transform(xml, new StreamResult(schematronSchema.toURI().toString()));
        schSource = new InputSource(schematronSchema.toURI().toString());
        schSource.setSystemId(schematronSchema.toURI().toString());

        return schematronSchema;
	}
	
	class TestError implements ErrorHandler {

		@Override
		public void warning(SAXParseException exception) throws SAXException {
			buildErrorMessage("Warning", exception);
		}

		@Override
		public void error(SAXParseException exception) throws SAXException {
			buildErrorMessage("Error", exception);
		}

		@Override
		public void fatalError(SAXParseException exception) throws SAXException {
			buildErrorMessage("Fatal error", exception);
		}
		
		private void buildErrorMessage(String type, SAXParseException e) {
			int line = e.getLineNumber();
			int column = e.getColumnNumber();
			System.err.print(type);
			if (line > -1 || column > -1) {
				System.err.print(" at");
				if (line > -1) {
					System.err.print(" line " + line);
				}
				if (line > -1 && column > -1) {
					System.err.print(",");
				}
				if (column > -1) {
					System.err.print(" column " + column);
				}
			}
			System.err.println(": " + e.getMessage());
		}
	}

	public InputStream getReportStream() {
		if (report==null) {
			return null;
		}
		try {
			return new FileInputStream(report);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public Object getFeature(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFeature(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

}