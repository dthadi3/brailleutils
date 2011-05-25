package be_interpoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import org.daisy.paper.PageFormat;
import org.daisy.paper.Dimensions;
import org.daisy.paper.PaperCatalog;
import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserCatalog;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.facade.PEFConverterFacade;
import org.daisy.braille.pef.PEFHandler;
import org.daisy.braille.tools.FileCompare;
import org.daisy.braille.tools.FileTools;

import org.daisy.braille.embosser.UnsupportedWidthException;

/**
 *
 * @author Bert Frees
 */
public class Interpoint55EmbosserTest {

    private static EmbosserCatalog ec = EmbosserCatalog.newInstance();
    private static Embosser e = ec.get("be_interpoint.InterpointEmbosserProvider.EmbosserType.INTERPOINT_55");
    private static PaperCatalog pc = PaperCatalog.newInstance();
    private static PageFormat a3 = new PageFormat(pc.get("org_daisy.ISO216PaperProvider.PaperSize.A3"), PageFormat.Orientation.DEFAULT);
    private static PageFormat a4 = new PageFormat(pc.get("org_daisy.ISO216PaperProvider.PaperSize.A4"), PageFormat.Orientation.REVERSED);

    @Test
    public void testDimensions() {

        Dimensions dim;
        dim = new Dimensions() { public double getWidth()  { return 340d; }
                                 public double getHeight() { return 297d; }};

        assertTrue("Assert that paper of width 340mm is supported", e.supportsDimensions(dim));

        dim = new Dimensions() { public double getWidth()  { return 341d; }
                                 public double getHeight() { return 297d; }};
        
        assertTrue("Assert that paper wider than 340mm is not supported", !e.supportsDimensions(dim));

        // Assert that an Exception is thrown when creating an EmbosserWriter with an unsupported PageFormat ?
    }

    @Test
    public void testPrintableArea() {

        e.setFeature(EmbosserFeatures.SADDLE_STITCH, false);

        assertEquals("Assert that max width for an A3 paper is 70 cells (if saddle stitch mode is off)", e.getMaxWidth(a3),  70);
        assertEquals("Assert that max height for an A3 paper is 29 lines",                               e.getMaxHeight(a3), 29);

        e.setFeature(EmbosserFeatures.SADDLE_STITCH, true);

        assertEquals("Assert that max width for an A3 paper is 35 cells (if saddle stitch mode is on)",  e.getMaxWidth(a3),  35);

        // Assert that an Exception is thrown when parsing a PEF with too many cells or lines ?
    }

    @Test
    public void testTableFilter() {

        TableCatalog tc = TableCatalog.newInstance();

	assertTrue("Assert that encoding cannot be modified", tc.list(e.getTableFilter()).size() <= 1);
    }

    @Test
    public void testDuplex() {
        assertTrue("Assert that duplex is supported", e.supportsDuplex());
    }

    @Test
    public void test8dot() {
        assertTrue("Assert that 8-dot is not supported", !e.supports8dot());
    }

    @Test
    public void testAligning() {
        assertTrue("Assert that aligning is supported", e.supportsAligning());
    }

    @Test
    public void testEmbosserWriter() throws IOException,
                                            ParserConfigurationException,
                                            SAXException,
                                            UnsupportedWidthException {

        File prn1 = File.createTempFile("test_interpoint55", ".prn");
        File prn2 = File.createTempFile("test_interpoint55", ".prn");
        File pef =  File.createTempFile("test_interpoint55", ".pef");
        
        FileCompare fc = new FileCompare();
        PEFHandler.Builder builder;
        EmbosserWriter w;

        e.setFeature(EmbosserFeatures.PAGE_FORMAT, a4);
        e.setFeature(EmbosserFeatures.SADDLE_STITCH, false);

        // Single sided

        w = e.newEmbosserWriter(new FileOutputStream(prn1));
        builder = new PEFHandler.Builder(w)
                          .range(null)
                          .align(org.daisy.braille.pef.PEFHandler.Alignment.INNER)
                          .offset(0)
                          .topOffset(0);

        FileTools.copy(this.getClass().getResourceAsStream("resource-files/interpoint55_single_sided.pef"), new FileOutputStream(pef));
        FileTools.copy(this.getClass().getResourceAsStream("resource-files/interpoint55_single_sided.prn"), new FileOutputStream(prn2));
        PEFConverterFacade.parsePefFile(pef, builder.build());
        assertTrue("Assert that the contents of the file is as expected.",
                fc.compareBinary(new FileInputStream(prn1), new FileInputStream(prn2))
        );

        // Double sided

        w = e.newEmbosserWriter(new FileOutputStream(prn1));
        builder = new PEFHandler.Builder(w)
                          .range(null)
                          .align(org.daisy.braille.pef.PEFHandler.Alignment.INNER)
                          .offset(0)
                          .topOffset(0);

        FileTools.copy(this.getClass().getResourceAsStream("resource-files/interpoint55_double_sided.pef"), new FileOutputStream(pef));
        FileTools.copy(this.getClass().getResourceAsStream("resource-files/interpoint55_double_sided.prn"), new FileOutputStream(prn2));
        PEFConverterFacade.parsePefFile(pef, builder.build());
        assertTrue("Assert that the contents of the file is as expected.",
                fc.compareBinary(new FileInputStream(prn1), new FileInputStream(prn2))
        );

        prn1.deleteOnExit();
        prn2.deleteOnExit();
        pef.deleteOnExit();
    }
}
