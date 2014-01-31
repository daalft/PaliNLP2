package de.unitrier.daalft.pali.morphology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;

/**
 * Class to stem words
 * @author David
 *
 */
public class NaiveStemmer {

	/**
	 * List with word endings
	 */
	private List<String> endings;
	
	/**
	 * Constructor
	 */
	public NaiveStemmer () {
		endings = new ArrayList<String>();
		readAndFetch();
	}
	
	/**
	 * Reads paradigms, fetches paradigms and populates list of endings
	 */
	private void readAndFetch () {
		ParadigmAccessor pa = new ParadigmAccessor();
		Paradigm lp = pa.getParadigms();
		endings = retainEndingsOnly(lp);
	}
	
	/**
	 * Retains only endings from paradigms and discards all other information
	 * @param lp list of paradigms
	 * @return list of endings
	 */
	private List<String> retainEndingsOnly (Paradigm lp) {
		List<String> l = new ArrayList<String>();		
			for (Morph m : lp.getEndings()) {
				l.add(m.getMorph());
			}
		return l;
	}
	
	/**
	 * Applies the stemming algorithm on a word
	 * <p>
	 * Internally, the word is stemmed recursively by
	 *  {@link #stemRecursive(String)} to eliminate multiple endings
	 * @param word word to stem
	 * @return stemmed word
	 */
	public String stem (String word) {
		sort();
		return stemRecursive(word);
	}
	
	/**
	 * Recursively stems the word to eliminate all possible endings
	 * @param word word to stem
	 * @return stemmed word
	 */
	private String stemRecursive (String word) {
		for (String end : endings) {
			if (word.endsWith(end)) {
				int index = word.lastIndexOf(end);
				return stemRecursive(word.substring(0, index));
			}
		}
		return word;
	}
	
	/**
	 * Sorts the endings by length in descending order
	 */
	private void sort () {
		Collections.sort(endings, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1.length() > o2.length())
					return -1;
				if (o1.length() < o2.length())
					return 1;
				return 0;
			}
		});
	}
}
