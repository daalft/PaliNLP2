package de.unitrier.daalft.pali.phonology;

import java.io.*;
import java.util.*;

import de.unitrier.daalft.pali.lexicon.CachedDictionaryLookup;
import de.unitrier.daalft.pali.phonology.element.Rule;
import de.unitrier.daalft.pali.phonology.tools.*;
/**
 * A rule-based sandhi splitter
 * @author David
 *
 */
public class SandhiSolver {

	enum RuleSet {
		RULE_SET_A, RULE_SET_B, SPECIAL_RULE
	}
	
	/**
	 * Rule set
	 */
	private List<Rule> ruleSetA, ruleSetB, specialRule;
	
	private CachedDictionaryLookup cachedDictionaryLookup;

	/**
	 * Constructor
	 * @param file file containing sandhi rules
	 * @throws Exception 
	 */
	public SandhiSolver(File fileA, File fileB, String domain, int port, String user, String pw, int maxCacheSize, int maxCacheDurationInSeconds) throws Exception {
		SandhiFileReader sfr = new SandhiFileReader();
		try {
			ruleSetA = sfr.parse(fileA);
			ruleSetB = sfr.parse(fileB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		specialRule = new ArrayList<>();
		specialRule.add(new Rule("oti$", "o ti"));
		cachedDictionaryLookup = new CachedDictionaryLookup(domain, port, user, pw, maxCacheSize, maxCacheDurationInSeconds);
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

	private String[] applyRegexRules (String input, RuleSet ruleset) {
		String s = input;
		List<Rule> rules = ruleset.equals(RuleSet.RULE_SET_A)?ruleSetA:
								ruleset.equals(RuleSet.RULE_SET_B)?ruleSetB:
								specialRule;
		for (Rule rule : rules) {
			s = rule.applyFirst(s);
		}
		return s.split(" ");
	}


	private ArrayList<String> splitSingleWord(String input) {
		// PASS 1 : apply RULE SET A
		String[] temp = applyRegexRules(input, RuleSet.RULE_SET_A);

		// INTERMEDIATE : check for special rule for each compound returned
		List<String> temp2 = new ArrayList<>();
		for(String s : temp) {
			if (s.endsWith("oti") && !s.endsWith("karoti") && !cachedDictionaryLookup.lemmaExists(s)) {
				for (String t : applyRegexRules(s, RuleSet.SPECIAL_RULE))
					temp2.add(t);
			} else {
				temp2.add(s);

			}
		}
		// PASS 2 : apply RULE SET B
		ArrayList<String> finalResult = new ArrayList<String>();
		for (String s : temp2) {
			for (String t : applyRegexRules(s, RuleSet.RULE_SET_B))
				finalResult.add(t);
		}
		//finalResult.add(input);
		return finalResult;
	}
}

