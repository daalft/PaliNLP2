package de.unitrier.daalft.pali.phonology;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unitrier.daalft.pali.phonology.element.MyDictionary;
import de.unitrier.daalft.pali.phonology.element.SandhiRule;
import de.unitrier.daalft.pali.phonology.element.SplitResult;
import de.unitrier.daalft.pali.phonology.tools.RuleMethods;
/**
 * Class that manages sandhi functionality
 * @author David
 *
 */
public class SandhiManager {

	/**
	 * Sandhi rules
	 */
	private List<SandhiRule> rules, revRules, soundRules;
	/**
	 * Dictionary
	 */
	private MyDictionary dict;

	public SandhiManager() {
		rules = new ArrayList<SandhiRule>();
		revRules = new ArrayList<SandhiRule>();
		soundRules = new ArrayList<SandhiRule>();
		dict = new MyDictionary();
		init();
	}

	/**
	 * Initializer
	 */
	private void init () {
		SandhiReader sr = new SandhiReader();
		try {
			sr.run(null,null,null,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dict = sr.getDictionary();
		RuleMethods.setDictionary(dict);
		rules = expand(sr.getRules());
		revRules = expand(sr.getReverseRules());
		soundRules = sr.getSoundRules();
	}

	/**
	 * Expands expandables 
	 * @param list list
	 * @return expanded rules
	 */
	private static List<SandhiRule> expand (List<SandhiRule> list) {
		List<SandhiRule> out = new ArrayList<SandhiRule>();
		for (SandhiRule sr : list) {
			out.add(RuleMethods.replaceConstants(sr));
		}
		return out;
	}
	
	/**
	 * Returns merging rules
	 * @return merging rules
	 */
	public List<SandhiRule> getRules () {
		return rules;
	}
	
	/**
	 * Returns this dictionary
	 * @return dictionary
	 */
	public MyDictionary getDictionary () {
		return dict;
	}
	
	/**
	 * Returns splitting rules
	 * @return splitting rules
	 */
	public List<SandhiRule> getReverseRules () {
		return revRules;
	}
	
	/**
	 * Returns sound replacing rules
	 * @return sound replacing rules
	 */
	public List<SandhiRule> getSoundRules () {
		return soundRules;
	}
	
	/**
	 * Resolves function constructs
	 * @param res string to resolve
	 * @return list with resolved function
	 */
	public List<String> resolveFunction (String res) {
		List<String> out = new ArrayList<String>();
		Pattern func = Pattern.compile("\\+\\w+(?=\\()");
		Pattern arg = Pattern.compile("(?<=\\().+?(?=\\))");
		Matcher m = func.matcher(res);
		Matcher n = arg.matcher(res);
		// if match
		while (m.find() && n.find()) {
			// get function name without prefix
			String fn = m.group().substring(1);
			// get argument
			String argn = n.group();
			out.add(res.replaceFirst("\\+"+fn+"\\(.+?\\)", dict.lookupMethod(fn, argn)));
		}
		return out;
	}
	
	/**
	 * Checks whether the specified string contains 
	 * function-like constructs
	 * @param res string to check
	 * @return true if string contains function-like constructs
	 */
	public boolean containsFunction (String res) {
		return res.matches(".*\\+.*");
	}
	
	/**
	 * Merges two or more words according to the rules of sandhi
	 * @param words words to merge
	 * @return merged words
	 */
	public List<String> merge (String... words) {
		SandhiMerge sm = new SandhiMerge();
		return sm.merge(words);
	}
	
	/**
	 * Splits a word according to the rules of sandhi
	 * @param word word to split
	 * @param depth depth
	 * @return split word
	 */
	public List<SplitResult> split (String word, int depth) {
		SandhiSplit sp = new SandhiSplit();
		return sp.split(word, depth);
	}
}
