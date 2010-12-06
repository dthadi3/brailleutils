package org_daisy;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.BrailleConstants;
import org.daisy.braille.table.AbstractConfigurableTableProvider;
import org.daisy.braille.table.BrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.table.EmbosserTable;
import org.daisy.braille.table.Table;

public class EmbosserTableProvider extends AbstractConfigurableTableProvider<EmbosserTableProvider.TableType> {
	
	enum TableType {
		/* EN_US, // US computer braille, compatible with
				// "Braillo USA 6 DOT 001.00" */
		EN_GB, // US computer braille (lower case), compatible with
				// "Braillo ENGLAND 6 DOT 044.00" which is identical to
				// "Braillo USA 6 DOT 001.00"
		DA_DK, 
		DE_DE, 
		ES_ES, 
		IT_IT_FIRENZE, 
		//SV_SE_CX, 
		//SV_SE_MIXED, 
		UNICODE_BRAILLE, 
		/*BRAILLO_6DOT_046_01, // compatible with "Braillo SWEDEN 6 DOT 046.01"
		BRAILLO_6DOT_047_01 // compatible with "Braillo NORWAY 6 DOT 047.01"*/
	};

	private final ArrayList<Table> tables;

	public EmbosserTableProvider() {
		super(EightDotFallbackMethod.values()[0], '\u2800');
		tables = new ArrayList<Table>(); 
		//tables.add(new EmbosserTable<TableType>("US computer braille", "Commonly used embosser table", TableType.EN_US, this));
		tables.add(new EmbosserTable<TableType>("British computer braille", "", TableType.EN_GB, this));
		tables.add(new EmbosserTable<TableType>("Danish computer braille", "", TableType.DA_DK, this));
		tables.add(new EmbosserTable<TableType>("German computer braille", "", TableType.DE_DE, this));
		tables.add(new EmbosserTable<TableType>("Spanish computer braille", "", TableType.ES_ES, this));
		tables.add(new EmbosserTable<TableType>("Italian computer braille", "", TableType.IT_IT_FIRENZE, this));
		//tables.add(new EmbosserTable<TableType>("Swedish CX", "Matches the Swedish representation in CX", TableType.SV_SE_CX, this));
		//tables.add(new EmbosserTable<TableType>("Swedish mixed", "", TableType.SV_SE_MIXED, this));
		tables.add(new EmbosserTable<TableType>("Unicode braille", "", TableType.UNICODE_BRAILLE, this));
		/*
		tables.add(new EmbosserTable<TableType>("Braillo SWEDEN 6 DOT 046.01", "Compatible with Braillo SWEDEN 6 DOT 046.01", TableType.BRAILLO_6DOT_046_01, this).putProperty(IS_DISPLAY_FORMAT, false));
		tables.add(new EmbosserTable<TableType>("Braillo NORWAY 6 DOT 047.01", "Compatible with Braillo NORWAY 6 DOT 047.01", TableType.BRAILLO_6DOT_047_01, this).putProperty(IS_DISPLAY_FORMAT, false));
		*/
	}


	/**
	 * Get a new table instance based on the factory's current settings.
	 * 
	 * @param t
	 *            the type of table to return, this will override the factory's
	 *            default table type.
	 * @return returns a new table instance.
	 */
	public BrailleConverter newTable(TableType t) {
		switch (t) {
	/*	case EN_US:
			return new EmbosserBrailleConverter(
					new String(
							" A1B'K2L@CIF/MSP\"E3H9O6R^DJG>NTQ,*5<-U8V.%[$+X!&;:4\\0Z7(_?W]#Y)="),
					Charset.forName("UTF-8"), fallback, replacement, true);*/
		case EN_GB:
			return new EmbosserBrailleConverter(
					new String(
							" a1b'k2l@cif/msp\"e3h9o6r^djg>ntq,*5<-u8v.%[$+x!&;:4\\0z7(_?w]#y)="),
					Charset.forName("UTF-8"), fallback, replacement, true);
		case DA_DK:
			return new EmbosserBrailleConverter(
					new String(
							" a,b.k;l'cif/msp`e:h*o!r~djgæntq@å?ê-u(v\\îøë§xèç^û_ü)z\"à|ôwï#yùé"),
					Charset.forName("IBM850"), fallback, replacement, true);
		case DE_DE:
			return new EmbosserBrailleConverter(
					new String(
							" a,b.k;l\"cif|msp!e:h*o+r>djg`ntq'1?2-u(v$3960x~&<5/8)z={_4w7#y}%"),
					Charset.forName("UTF-8"), fallback, replacement, true);
		case ES_ES:
			return new EmbosserBrailleConverter(
					new String(
							" a,b.k;l'cif/msp@e:h}o+r^djg|ntq_1?2-u<v{3960x$&\"5*8>z=(%4w7#y)\\"),
					Charset.forName("UTF-8"), fallback, replacement, true);
		case IT_IT_FIRENZE:
			return new EmbosserBrailleConverter(
					new String(
							" a,b'k;l\"cif/msp)e:h*o!r%djg&ntq(1?2-u<v#396^x\\@+5.8>z=[$4w7_y]0"),
					Charset.forName("UTF-8"), fallback, replacement, true);
			/*
		case BRAILLO_6DOT_046_01: // sv-SE
			return new EmbosserBrailleConverter(
					new String(
							" a,b'k;l^cif/msp!e:h*o+r\"djg[ntq_1?2-u<v%396]x\\&#5.8>z=($4w70y)@"),
					Charset.forName("UTF-8"), fallback, replacement, true);
		case BRAILLO_6DOT_047_01: // no-NO
			return new EmbosserBrailleConverter(
					new String(
							" A,B.K;L`CIF/MSP'E:H@O!RaDJG[NTQ*]?r-U\"Vqm\\h&Xli_e%u$Z=k|dWg#Ynj"),
					Charset.forName("UTF-8"), fallback, replacement, false);
					
		case SV_SE_CX:
			return new EmbosserBrailleConverter(
					new String(
							" a,b.k;l^cif/msp'e:h*o!r~djgäntq_å?ê-u(v@îöë§xèç\"û+ü)z=à|ôwï#yùé"),
					Charset.forName("UTF-8"), fallback, replacement, true);
		case SV_SE_MIXED:
			StringBuffer sb = new StringBuffer(
					" a,b.k;l_cif/msp'e:h_o!r_djgäntq_å?_-u(v__öë_xè_\"_+ü)z=___w__y__");
			for (int i = 0; i < 64; i++) {
				if (sb.charAt(i) == '_') {
					sb.setCharAt(i, (char) (0x2800 + i));
				}
			}
			return new EmbosserBrailleConverter(sb.toString(), Charset.forName("UTF-8"),
					fallback, replacement, true);
					*/
		case UNICODE_BRAILLE:
			return new EmbosserBrailleConverter(BrailleConstants.BRAILLE_PATTERNS_256,
					Charset.forName("UTF-8"), fallback, replacement, true);
		default:
			throw new IllegalArgumentException("Cannot find table type "
					+ t);
		}
	}

	@Override
	public Collection<Table> list() {
		return tables;
	}

}
