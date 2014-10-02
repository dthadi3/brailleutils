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
import org.daisy.cli.AbstractUI;
import org.daisy.cli.Argument;
import org.daisy.cli.Definition;
import org.daisy.cli.ExitCode;
import org.daisy.cli.OptionalArgument;
import org.daisy.factory.Factory;
import org.daisy.paper.Paper;
import org.daisy.paper.PaperCatalog;

class ListStuff extends AbstractUI {
	enum Mode {
		NAME,
		IDENTIFIER,
		NAME_IDENTIFIER
	};
	private final List<Argument> reqArgs;
	private final List<OptionalArgument> optionalArgs;
	
	private final static String embossersKey = "embossers";
	private final static String tablesKey = "tables";
	private final static String papersKey = "papers";
	private final static String modeKey = "mode";
	private final static String prefixKey = "prefix";
	private final static String postfixKey = "postfix";
	private final static String separatorKey = "separator";
	
	public ListStuff() {
		reqArgs = new ArrayList<Argument>();
		ArrayList<Definition> defs = new ArrayList<Definition>();
		defs.add(new Definition(embossersKey, "to list available embossers"));
		defs.add(new Definition(tablesKey, "to list available tables"));
		defs.add(new Definition(papersKey, "to list available papers"));
		reqArgs.add(new Argument("type_of_objects", "What to list", defs));
		optionalArgs = new ArrayList<OptionalArgument>();
		ArrayList<Definition> modes = new ArrayList<Definition>();
		modes.add(new Definition(Mode.NAME.toString(), "List display names"));
		modes.add(new Definition(Mode.IDENTIFIER.toString(), "List identifiers"));
		modes.add(new Definition(Mode.NAME_IDENTIFIER.toString(), "List names followed by identifier"));
		optionalArgs.add(new OptionalArgument(modeKey, "Mode", modes, Mode.NAME.toString()));
		optionalArgs.add(new OptionalArgument(prefixKey, "Line prefix.", ""));
		optionalArgs.add(new OptionalArgument(postfixKey, "Line postfix.", ""));
		optionalArgs.add(new OptionalArgument(separatorKey, "Field separator. Only used when there is more than one field on each line.", ""));
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
		String type;
		String prefix;
		String postfix;
		String separator;
		Mode mode = Mode.NAME;
		{
			Map<String, String> p = ui.toMap(args);
			type = p.remove(ARG_PREFIX+0);
			Map<String, String> op = ui.getOptional(args);
			prefix = replaceNullWithEmpty(op.get(prefixKey));
			postfix = replaceNullWithEmpty(op.get(postfixKey));
			separator = replaceNullWithEmpty(op.get(separatorKey));
			String modeStr = op.get(modeKey);
			if (modeStr!=null) {
				try {
					mode = Mode.valueOf(modeStr.toUpperCase());
				} catch (Exception e) {}
			}
		}
		System.out.println();
		if (embossersKey.equalsIgnoreCase(type)) {
			EmbosserCatalog ec = EmbosserCatalog.newInstance();
			Embosser[] ea = ec.list().toArray(new Embosser[]{});
			printList(ea, mode, prefix, separator, postfix);
		} else if (tablesKey.equalsIgnoreCase(type)) {
			TableCatalog tc = TableCatalog.newInstance();
			Table[] ta = tc.list().toArray(new Table[]{});
			printList(ta, mode, prefix, separator, postfix);
		} else if (papersKey.equalsIgnoreCase(type)) {
			PaperCatalog pc = PaperCatalog.newInstance();
			Paper[] pa = pc.list().toArray(new Paper[]{});
			printList(pa, mode, prefix, separator, postfix);
		}
	}
	
	private static String replaceNullWithEmpty(String input) {
		if (input==null) {
			return "";
		} else {
			return input;
		}
	}
	
	private static void printList(Factory[] f, Mode mode, String prefix, String separator, String postfix) {
		switch (mode) {
			case NAME:
				sortByName(f);
				for (Factory p : f) {
					System.out.println(prefix + p.getDisplayName() + postfix);
				}
				break;
			case IDENTIFIER:
				sortById(f);
				for (Factory p : f) {
					System.out.println(prefix + p.getIdentifier() + postfix);
				}
				break;
			case NAME_IDENTIFIER:
				sortByName(f);
				for (Factory p : f) {
					System.out.println(prefix + p.getDisplayName() + separator + p.getIdentifier() + postfix);
				}
		}
	}
	
	private static void sortById(Factory[] f) {
		Arrays.sort(f, new Comparator<Factory>(){
			public int compare(Factory o1, Factory o2) {
				return o1.getIdentifier().compareTo(o2.getIdentifier());
		}});
	}
	
	private static void sortByName(Factory[] f) {
		Arrays.sort(f, new Comparator<Factory>(){
			public int compare(Factory o1, Factory o2) {
				return o1.getDisplayName().compareTo(o2.getDisplayName());
		}});
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
