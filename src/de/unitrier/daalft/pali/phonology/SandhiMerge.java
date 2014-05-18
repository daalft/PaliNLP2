package de.unitrier.daalft.pali.phonology;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.unitrier.daalft.pali.phonology.element.SandhiRule;

/**
 * The merging direction of sandhis
 * @author David
 *
 */
public class SandhiMerge {

	/**
	 * Sandhi manager
	 */
	private SandhiManager sm;
	
	public SandhiMerge () {
		sm  = new SandhiManager();
	}

	/**
	 * Merges two words by using sandhi rules
	 * <p>
	 * Only applies merging rules
	 * @param w1 first word
	 * @param w2 second word
	 * @return merged word
	 */
	private List<String> mergeTwo (String w1, String w2) {
		List<SandhiRule> rules = sm.getRules();
		Set<String> out = new TreeSet<String>();
		for (SandhiRule sr : rules) {
			if (sr.isApplicable(w1, w2)) {
				String res = sr.apply(w1, w2);
				if (sm.containsFunction(res)) {
					out.addAll(sm.resolveFunction(res));
				} else {
					out.add(res);
				}
			} else {
				out.add(w1 + " " + w2);
			}
		}
		return Arrays.asList(out.toArray(new String[1]));
	}
	
	/**
	 * Merges two or more words according to the rules of sandhi
	 * @param words word to merge
	 * @return merged words
	 */
	public List<String> merge (String... words) {
		Set<String> out = new TreeSet<String>();
		List<String> first = mergeTwo(words[0], words[1]);
		out.addAll(first);
		for (int i = 2; i < words.length; i++) {
			for (String s : first) {
				out.addAll(mergeTwo(s, words[i]));
			}
		}
		return Arrays.asList(out.toArray(new String[1]));
	}
}
