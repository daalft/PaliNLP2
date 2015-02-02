package de.unitrier.daalft.pali.phonology;

import java.io.*;
import java.util.*;

import de.unitrier.daalft.pali.lexicon.CachedDictionaryLookup;
import de.unitrier.daalft.pali.lexicon.DictionaryLookup;
import de.unitrier.daalft.pali.phonology.element.Rule;
import de.unitrier.daalft.pali.phonology.tools.*;
/**
 * A rule-based sandhi splitter
 * @author David
 *
 */
public class SandhiSolver {
	
	enum RegexRuleSet {
		// These values have to be changed if the sandhi rule file changes!
		RULE_SET_A(0,42), 
		SPECIAL_RULE(42,44), 
		RULE_SET_B(44,182);
		
		private final int from, toExclusive;
		
		private RegexRuleSet(int from, int toExclusive) {
			this.from = from;
			this.toExclusive = toExclusive;
		}
		
		public int getFrom () {
			return from;
		}
		
		public int getTo () {
			return toExclusive;
		}
	}
	
	/**
	 * Rule set
	 */
	private List<Rule> rules;
	private DictionaryLookup cachedDictionaryLookup;
	
	/**
	 * Constructor
	 * @param file file containing sandhi rules
	 */
	public SandhiSolver(File file) {
		SandhiFileReader sfr = new SandhiFileReader();
		try {
			rules = sfr.parse(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cachedDictionaryLookup = new CachedDictionaryLookup();
	}
	
	public ArrayList<String> sandhiSplit (String input) {
		ArrayList<String> result = splitSingleWord(input);
		
		if (result.size() == 1) {
			return result; 
		}
		
		ArrayList<String> finalResult = new ArrayList<String>();
		for (String s : result) {
			
			finalResult.addAll(sandhiSplit(s));
		}
		return finalResult;
	}

	private String[] applyRegexRules (String input, RegexRuleSet ruleset) {
		String s = input;
		List<Rule> rules = this.rules.subList(ruleset.from, ruleset.toExclusive);
		for (Rule rule : rules) {
			s = rule.applyFirst(s);
		}
		return s.split(" ");
	}
	
	private ArrayList<String> splitSingleWord(String input) {
		// PASS 1 : apply RULE SET A
		String[] temp = applyRegexRules(input, RegexRuleSet.RULE_SET_A);
		System.err.println("Call with " + input);
		for (String s : temp)
			System.out.println(s);
		// INTERMEDIATE : check for special rule for each compound returned
		List<String> temp2 = new ArrayList<>();
		for(String s : temp) {
			if (s.endsWith("oti") && !s.endsWith("karoti") && !cachedDictionaryLookup.lemmaExists(s)) {
				for (String t : applyRegexRules(s, RegexRuleSet.SPECIAL_RULE))
					temp2.add(t);
			} else {
				temp2.add(s);
			}
		}
		System.out.println(temp2);
		// PASS 2 : apply RULE SET B
		ArrayList<String> finalResult = new ArrayList<String>();
		for (String s : temp2) {
			for (String t : applyRegexRules(s, RegexRuleSet.RULE_SET_B))
				finalResult.add(t);
		}
		//finalResult.add(input);
		return finalResult;
	}
}
