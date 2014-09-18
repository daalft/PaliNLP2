package de.unitrier.daalft.pali.phonology;

import java.io.*;
import java.util.*;

import de.unitrier.daalft.pali.phonology.element.Rule;
import de.unitrier.daalft.pali.phonology.tools.*;
/**
 * A rule-based sandhi splitter
 * @author David
 *
 */
public class SandhiSolver {
	/**
	 * Rule set
	 */
	private List<Rule> rules;
	
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
	}
	
	/**
	 * Resolves a single Pali word
	 * <p>
	 * Returns an empty list if no rule can be
	 * applied to the given word (i.e. the word 
	 * probably doesn't have to be resolved)
	 * @param s word
	 * @return resolved word(s)
	 */
	public ArrayList<String> resolveSandhiSingleWord(String s) {
		ArrayList<String> out = new ArrayList<String>();
		for (Rule r : rules) {
			if (r.isApplicable(s)) {
				String solved = r.applyAll(s);
				if (!out.contains(solved))
					out.add(solved);
			}
		}
		return out;
	}
	
	/**
	 * Resolved a Pali sentence
	 * @param sentence sentence
	 * @return resolved sentence
	 */
	public List<List<String>> resolveSandhiSentence(String sentence) {
		return resolveSandhiWordlist(Arrays.asList(sentence.split(" ")));
	}
	
	/**
	 * Resolves a list of Pali words
	 * @param words words
	 * @return resolved words
	 */
	public List<List<String>> resolveSandhiWordlist(List<String> words) {
		List<List<String>> out = new ArrayList<List<String>>();
		for (int i = 0; i < words.size(); i++) {
			out.add(resolveSandhiSingleWord(words.get(i)));
		}
		return out;
	}
}
