package de.unitrier.daalft.pali.morphology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.paradigm.Paradigm;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.morphology.tools.WordClassGuesser;


/**
 * Class to stem words
 * @author David
 *
 */
public class WordclassStemmer
{

	/**
	 * List with word endings
	 */
	private List<String> endings;

	private Paradigm p;
	private WordClassGuesser wcg;

	/**
	 * Constructor
	 */
	public WordclassStemmer(ParadigmAccessor pa)
	{
		initialize(pa);
	}

	/**
	 * Reads paradigms, fetches paradigms and populates list of endings
	 */
	private void initialize(ParadigmAccessor pa)
	{
		p = pa.getParadigms();
		endings = retainEndingsOnly(p);
		wcg = new WordClassGuesser(pa);
	}

	/**
	 * Retains only endings from paradigms and discards all other information
	 * @param lp list of paradigms
	 * @return list of endings
	 */
	private List<String> retainEndingsOnly (Paradigm lp) {
		if (lp == null)
			return null;
		List<String> l = new ArrayList<String>();		
		for (Morph m : lp.getEndings()) {
			l.add(m.getMorph());
		}
		return l;
	}

	public List<String> stem (String word, String wc) {
		sort(endings);
		List<String> out = new ArrayList<String>();
		List<String> endings = null;
		Set<String> set = new HashSet<String>();
		endings = retainEndingsOnly(p.getParadigmByFeatures(new FeatureSet("paradigm", wc)));
		if (endings == null)
			endings = this.endings;
		sort(endings);
		set.add(stemRecursive(word, endings));
		out.addAll(set);
		return out;
	}

	/**
	 * Applies the stemming algorithm on a word
	 * <p>
	 * Internally, the word is stemmed recursively by
	 *  {@link #stemRecursive(String)} to eliminate multiple endings
	 * @param word word to stem
	 * @return stemmed word
	 */
	public List<String> stem (String word) {
		sort(endings);
		List<String> out = new ArrayList<String>();
		List<String> wordclasses = wcg.guessWordClassFromWordForm(word);
		List<String> endings = null;
		Set<String> set = new HashSet<String>();
		if (wordclasses.size() == 0) {
			out.add(stemRecursive(word, this.endings));
			return out;
		}
		for (String wc : wordclasses) {
			endings = retainEndingsOnly(p.getParadigmByFeatures(new FeatureSet("paradigm", wc)));
			if (endings == null)
				endings = this.endings;
			sort(endings);
			set.add(stemRecursive(word, endings));
		}
		out.addAll(set);
		return out;
	}

	/**
	 * Recursively stems the word to eliminate all possible endings
	 * @param word word to stem
	 * @return stemmed word
	 */
	private String stemRecursive (String word, List<String> endings) {
		for (String end : endings) {
			if (word.endsWith(end)) {
				int index = word.lastIndexOf(end);
				return stemRecursive(word.substring(0, index), endings);
			}
		}
		return word;
	}

	/**
	 * Sorts the endings by length in descending order
	 */
	private void sort (List<String> list) {
		Collections.sort(list, new Comparator<String>() {
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
