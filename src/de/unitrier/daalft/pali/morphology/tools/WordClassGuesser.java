package de.unitrier.daalft.pali.morphology.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.unitrier.daalft.pali.morphology.element.Morpheme;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNouns;
import de.unitrier.daalft.pali.morphology.paradigm.irregular.IrregularNumerals;

/**
 * Guesses the word class of an input word
 * @author David
 *
 */
public class WordClassGuesser {

	/**
	 * Prune limit
	 */
	private int prune;

	public WordClassGuesser () {
		prune = 10;
	}
	
	public WordClassGuesser (int p) {
		prune = p;
	}
	/**
	 * Guesses the word class of a given word
	 * <p>
	 * The returned result depends on the <em>prune</em> parameter. A
	 * low prune will more likely return more word class results, whereas
	 * a higher prune will tend to cut off the result list early and return
	 * only the top result
	 * @param word word 
	 * @return word class
	 */
	public List<String> guess (String word) {
		ParadigmAccessor pa = new ParadigmAccessor();
		
		IrregularNouns inoun = pa.getIrregularNouns();
		IrregularNumerals inum = pa.getIrregularNumerals();
		
		Map<String, Integer> map = new HashMap<String, Integer>();

		List<String> out = new ArrayList<String>();
		
		if (inoun.isIrregular(word)) {
			return Collections.singletonList("noun");
		} else if (inum.isIrregular(word)) {
			return Collections.singletonList("numeral");
		}
		// add weight to potential verb lemma
		if (word.endsWith("ti"))
			map.put("verb", 10);
		for (Morpheme m : pa.getParadigms().getMorphemes()) {
			if (m.getFeatureByName("paradigm").equals("affix"))
				continue;
			if (m.isApplicable(word)) {
				String wordclass = m.getFeatureByName("paradigm");
				// weigh the result so that longer matches are given more importance than short matches
				map.put(wordclass, inc(wordclass, map)+(m.match(word).length()));
				
			}
		}
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Entry<String, Integer>> () {
			@Override
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				if (o1.getValue() > o2.getValue())
					return -1;
				if (o1.getValue() < o2.getValue())
					return 1;
				return 0;
			}
		});
		if (list.size() == 0) {
			return Collections.singletonList("unknown");
		}
		if (list.size() == 1)
			return Collections.singletonList(list.get(0).getKey());
		if (list.get(0).getValue() - list.get(1).getValue() > prune) {
			return Collections.singletonList(list.get(0).getKey());
		}
		for (int i = 0; i < list.size()-1; i += 1) {
			if ((list.get(i).getValue()-list.get(i+1).getValue()) <= prune) {
				String key = list.get(i).getKey();
				if (!out.contains(key))
					out.add(list.get(i).getKey());
				out.add(list.get(i+1).getKey());
			} else {
				return out;
			}
		}
		return out;
	}

	/**
	 * Guesses the word class of a given lemma
	 * @param lemma lemma
	 * @return word classes
	 */
	public List<String> guessLemma (String lemma) {
		List<String> guesses = new ArrayList<String>();
		ParadigmAccessor pa = new ParadigmAccessor();
		
		IrregularNouns inoun = pa.getIrregularNouns();
		IrregularNumerals inum = pa.getIrregularNumerals();
		
		if (inoun.isIrregular(lemma)) {
			return Collections.singletonList("noun");
		} else if (inum.isIrregular(lemma)) {
			return Collections.singletonList("numeral");
		}
		for (Morpheme m : pa.getPronounParadigm().getMorphemes()) {
			if (m.exactly(lemma)) {
				return Collections.singletonList("pronoun");
			}
		}
		if (lemma.endsWith("ti")) {
			return Collections.singletonList("verb");
		}
		if (ends(lemma, "a", "ā", "i", "in", "ī", "u", "ū", "ar", "an", "ant", "as", "us", "o")) {
			guesses.add("noun");
		}
		if (ends(lemma, "a", "ā", "i", "ī", "u", "ū", "ant", "vā", "mā", "at")) {
			guesses.add("adjective");
		}
		if (ends(lemma, "a", "i", "aṃ", "ma", "ya")) {
			guesses.add("numeral");
		}
		if (ends(lemma, "uṃ")) {
			guesses.add("indeclinable");
		}
		return guesses;
	}
	
	/**
	 * Checks whether the given lemma ends with one of 
	 * the given endings
	 * @param lemma lemma
	 * @param ends endings
	 * @return true if lemma ends with ending
	 */
	private boolean ends (String lemma, String... ends) {
		List<String> lest = Arrays.asList(ends);
		Collections.sort(lest, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				if (arg0.length() > arg1.length())
					return -1;
				if (arg0.length() < arg1.length())
					return 1;
				return 0;
			}
		});
		for (String e : lest) {
			if (lemma.endsWith(e))
				return true;
		}
		return false;
	}
	/**
	 * Increase the frequency of the given string
	 * @param wc word class
	 * @param map map
	 * @return frequency of word class incremented by one
	 */
	private int inc (String wc, Map<String, Integer> map) {
		if (map.containsKey(wc))
			return map.get(wc) + 1;
		return 1;
	}
}
