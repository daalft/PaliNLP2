package de.unitrier.daalft.pali.phonology.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unitrier.daalft.pali.phonology.element.ConstantDefinition;
import de.unitrier.daalft.pali.phonology.element.MyDictionary;
import de.unitrier.daalft.pali.phonology.element.SandhiRule;

/**
 * Provides a list of methods applicable to sandhi rules
 * @author David
 *
 */
public class RuleMethods {
	
	/**
	 * Dictionary
	 */
	private static MyDictionary dict;

	/**
	 * Sets the current dictionary
	 * @param dict
	 */
	public static void setDictionary (MyDictionary dict) {
		RuleMethods.dict = dict;
	}

	/**
	 * Replaces back-references by their reference
	 * and references by the back-reference
	 * @param rule rule
	 * @return rule
	 */
	public static SandhiRule swap (SandhiRule rule) {
		if (!rule.getReplacement().contains("$"))
			return rule;
		// get mappings
		Map<String, String> map = identifyAndMap(rule);
		// save mappings in rule
		rule.setMap(map);
		// for each map entry
		for (Entry<String, String> e : map.entrySet()) {
			// replace pattern
			rule.setPattern(rule.getPattern().replace(e.getValue(), e.getKey()));
			// replace replacement
			rule.setReplacement(rule.getReplacement().replace(e.getKey(), e.getValue()));
		}
		return rule;
	}

	/**
	 * Replaces the rule's pattern by its replacement
	 * and the replacement by the pattern
	 * @param rule rule
	 * @return rule
	 */
	public static SandhiRule flip (SandhiRule rule) {
		String help = rule.getPattern();
		rule.setPattern(rule.getReplacement());
		rule.setReplacement(help);
		return rule;
	}

	/**
	 * Expands the right side hand of a rule
	 * containing expandable elements into separate
	 * combinations
	 * @param rule rule
	 * @return list of separate rules
	 */
	public static List<SandhiRule> expand (SandhiRule rule) {
		List<SandhiRule> out = new ArrayList<SandhiRule>();
		String r = rule.getReplacement();
		if (!r.matches(".*\\s.*")) {
			r = r.replaceAll("(.{1,})\\(", "$1 \\(");
			r = r.replaceAll("\\)(.{1,})", "\\) $1");
		}
		String[] sp = r.split(" ");
		for (String res : Expander.expand(sp)) {
			out.add(new SandhiRule(rule.getPattern(), res));
		}
		return out;
	}

	/**
	 * Replaces all constants on the right hand side of the rule by their definition
	 * @param rule rule
	 * @return rule 
	 */
	public static SandhiRule replaceConstantsRHS (SandhiRule rule) {
		for (ConstantDefinition cd : dict.getConstantDefinitions()) {
			// constraint: constant preceded by either opening parenthesis or pipe character
			// and followed by either closing parenthesis or pipe character
			// to avoid replacement of substrings (i.e. VOWEL in LONG_VOWEL)
			rule.setReplacement(rule.getReplacement().replaceAll("(?<=[\\(\\|]{1})"+cd.getName()+"(?=[\\)\\|]{1})", cd.getElementsAsOrGroup()));
		}
		return rule;
	}
	
	/**
	 * Replaces all constants within the rule
	 * @param rule rule
	 * @return rule
	 */
	public static SandhiRule replaceConstants (SandhiRule rule) {
		for (ConstantDefinition cd : dict.getConstantDefinitions()) {
			// constraint: constant preceded by either opening parenthesis or pipe character
			// and followed by either closing parenthesis or pipe character
			// to avoid replacement of substrings (i.e. VOWEL in LONG_VOWEL)
			rule.setPattern(rule.getPattern().replaceAll("(?<=[\\(\\|]{1})"+cd.getName()+"(?=[\\)\\|]{1})", cd.getElementsAsOrGroup()));
			rule.setReplacement(rule.getReplacement().replaceAll("(?<=[\\(\\|]{1})"+cd.getName()+"(?=[\\)\\|]{1})", cd.getElementsAsOrGroup()));
		}
		return rule;
	}

	/**
	 * Removes functions from the left hand side of 
	 * a rule by invoking it with every element and
	 * returning a list of separate rules without
	 * functions
	 * @param rule rule
	 * @return list of rules without functions
	 */
	public static List<SandhiRule> unfunction (SandhiRule rule) {
		// output list
		List<SandhiRule> out = new ArrayList<SandhiRule>();
		// replace constants
		replaceConstants(rule);
		// create pattern for function name and argument of function
		Pattern func = Pattern.compile("\\+\\w+(?=\\(\\()");
		Pattern arg = Pattern.compile("(?<=\\(\\().+?(?=\\)\\))");
		Matcher m = func.matcher(rule.getPattern());
		Matcher n = arg.matcher(rule.getPattern());
		// if match
		if (m.find() && n.find()) {
			// get function name without prefix
			String fn = m.group().substring(1);
			// get argument
			String argn = n.group();
			// split argument
			String[] sp = argn.split("\\|");
			for (String st : sp) {
				// create new rule
				// replace function by result of function with argument
				// replace back-reference by argument
				SandhiRule genRule = new SandhiRule(rule.getPattern().replaceFirst("\\+"+fn+"\\(\\(.+?\\)\\)", dict.lookupMethod(fn, st)), rule.getReplacement().replace(getRef(argn, rule), st));
				// pass map
				genRule.setMap(rule.getMap());
				out.add(genRule);
				
			}
		} else {
			System.err.println("Could not undo function");
		}
		// create copy for iterating
		List<SandhiRule> ac = new ArrayList<SandhiRule>();
		ac.addAll(out);
		// replace rules still containing functions
		for (int i = 0; i < ac.size(); i++) {
			if (containsFunction(ac.get(i))) {
				out.remove(ac.get(i));
				out.addAll(unfunction(ac.get(i)));
			}
		}
		return out;
	}

	/**
	 * Checks whether the given rule contains expandables
	 * <b>on the right hand side</b>
	 * @param rule rule
	 * @return true if rule contains expandable
	 */
	public static boolean containsExpandables (SandhiRule rule) {
		if (rule.getReplacement().matches(".*\\(.+?\\).*")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the given rule contains functions
	 * <b>on the left hand side</b>
	 * @param rule rule
	 * @return true if rule contains function
	 */
	public static boolean containsFunction (SandhiRule rule) {
		if (rule.getPattern().contains("+"))
			return true;
		return false;
	}

	/**
	 * Returns the back-reference that is referenced
	 * by this group
	 * <p>
	 * Internally, the group is first looked up to get
	 * the constant that refers to this group, then
	 * the constant is looked up to get the back-reference
	 * that refers to this constant
	 * @param in group
	 * @param rule rule
	 * @return back-reference
	 */
	private static String getRef (String in, SandhiRule rule) {
		String cname = dict.reverseLookupConstant(in);
		return reverseMapLookup(cname, rule);		
	}

	/**
	 * Returns the back-reference that is referenced 
	 * by this constant
	 * @param name constant name
	 * @param rule rule
	 * @return back-reference
	 */
	private static String reverseMapLookup (String name, SandhiRule rule) {
		for (Entry<String, String> e : rule.getMap().entrySet()) {
			if (trim(e.getValue()).equals(name))
				return e.getKey();
		}
		return "";
	}

	/**
	 * Identifies back-references and maps
	 * back-references to their respective
	 * reference
	 * @return Map containing the mappings from back reference to reference
	 */
	private static Map<String, String> identifyAndMap (SandhiRule rule) {
		Map<String, String> map = new HashMap<String, String>();
		// only second part can contain back reference prior to swap
		String r = rule.getReplacement();
		Pattern p = Pattern.compile("\\$\\d");
		Matcher m = p.matcher(r);
		while (m.find()) {
			String br = m.group();
			// discard dollar sign
			int i = Integer.parseInt(br.substring(1));
			// find group number i
			Pattern p2 = Pattern.compile("\\(.+?\\)");
			Matcher m2 = p2.matcher(rule.getPattern());
			List<String> list = new LinkedList<String>();
			while (m2.find()) {
				list.add(m2.group());
			}
			map.put(br, list.get(i-1));
		}
		return map;
	}

	/**
	 * Returns the argument with the first and last character
	 * removed
	 * @param s argument
	 * @return trimmed argument
	 */
	private static String trim (String s) {
		return s.substring(1, s.length()-1);
	}

}
