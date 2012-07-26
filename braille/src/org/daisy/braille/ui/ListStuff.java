package org.daisy.braille.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserCatalog;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;

class ListStuff extends AbstractUI {
	private final List<Argument> reqArgs;
	private final List<OptionalArgument> optionalArgs;
	
	private final static String embossersKey = "embossers";
	private final static String tablesKey = "tables";
	
	public ListStuff() {
		reqArgs = new ArrayList<Argument>();
		ArrayList<Definition> defs = new ArrayList<Definition>();
		defs.add(new Definition(embossersKey, "to list available embossers"));
		defs.add(new Definition(tablesKey, "to list available tables"));
		reqArgs.add(new Argument("type_of_objects", "What to list", defs));
		optionalArgs = new ArrayList<OptionalArgument>();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ListStuff ui = new ListStuff();
		if (args.length<1) {
			System.out.println("Expected at least one more argument.");
			System.out.println();
			ui.displayHelp(System.out);
			System.exit(-ExitCode.MISSING_ARGUMENT.ordinal());
		}
		Map<String, String> p = ui.toMap(args);
		String type = p.remove(ARG_PREFIX+0);
		
		if (embossersKey.equalsIgnoreCase(type)) {
			EmbosserCatalog ec = EmbosserCatalog.newInstance();
			Embosser[] ea = ec.list().toArray(new Embosser[]{});
			Arrays.sort(ea, new Comparator<Embosser>() {
				public int compare(Embosser o1, Embosser o2) {
					return o1.getDisplayName().compareTo(o2.getDisplayName());
				}});
			for (Embosser e : ea) {
				System.out.println("  * " + e.getDisplayName());
			}
		} else if (tablesKey.equalsIgnoreCase(type)) {
		
			TableCatalog tc = TableCatalog.newInstance();
			Table[] ta = tc.list().toArray(new Table[]{});
			Arrays.sort(ta, new Comparator<Table>(){
				public int compare(Table o1, Table o2) {
					return o1.getDisplayName().compareTo(o2.getDisplayName());
				}});
			System.out.println();
			for (Table t : ta) {
				System.out.println("  * " + t.getDisplayName());
			}
		}

	}

	@Override
	public List<Argument> getRequiredArguments() {
		return reqArgs;
	}

	@Override
	public List<OptionalArgument> getOptionalArguments() {
		return optionalArgs;
	}

	@Override
	public String getName() {
		return BasicUI.list;
	}

}
