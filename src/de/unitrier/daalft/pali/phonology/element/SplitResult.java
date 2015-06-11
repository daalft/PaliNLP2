package de.unitrier.daalft.pali.phonology.element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.unitrier.daalft.pali.lexicon.LexiconAdapter;
import de.unitrier.daalft.pali.morphology.MorphologyAnalyzer;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.validation.Validator;
/**
 * Class representing the result of sandhi splitting
 * @author David
 *
 */
public class SplitResult implements Comparable <SplitResult> {
	Validator v;
	MorphologyAnalyzer ma;
	/**
	 * List of strings resulting from split
	 */
	private List<String> list;
	/**
	 * List of rules already applied
	 */
	private List<SandhiTableEntry> applied;
	/**
	 * Lexicon adapter
	 */
	private LexiconAdapter la;
	/**
	 * Confidence of split
	 */
	private double confidence;
	/**
	 * Constructor
	 */
	public SplitResult () {
		list = new LinkedList<String>();
		applied = new ArrayList<SandhiTableEntry>();
		v =  new Validator();
		try {
			ma = new MorphologyAnalyzer(new ParadigmAccessor(),la);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Constructor
	 * @param result split result
	 * @param pos position
	 * @param rule rule
	 */
	public SplitResult (String[] result, int pos, SandhiRule rule) {
		this();
		for (String s : result)
			list.add(s);
		applied.add(new SandhiTableEntry(pos, rule));
		try {
			selfvalidate();
		} catch (Exception e) {
		}
	}
	/**
	 * Constructor
	 * @param word word
	 */
	public SplitResult (String word) {
		this();
		list.add(word);
		try {
			selfvalidate();
		} catch (Exception e) {
		}
	}
	/**
	 * Returns the confidence of this split
	 * @return confidence
	 * @throws Exception
	 */
	public double getConfidence () throws Exception {
		double confidence = 0.0;
		//calculateConfidence();
		return confidence;
	}
	/**
	 * Calculates the confidence of this split
	 * @throws Exception 
	 */
	private void calculateConfidence () throws Exception {
		confidence = (getNom())/list.size();
	}
	/**
	 * Calculates the numerator for this confidence fraction
	 * @return numerator
	 * @throws Exception
	 */
	private double getNom () throws Exception {
		double n = 0.0;
		for (String s : list) {
			if (inDict(s)) {
				n++;
			}
		}
		return n;
	}
	/**
	 * Checks whether a word is in the dictionary
	 * @param s word to check
	 * @return true if word is in dictionary
	 * @throws Exception
	 */
	private boolean inDict (String s) throws Exception {
		if (la == null) {
			try {
				la = new LexiconAdapter();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Could not establish connection to database!");
			}
		}
		return la.lemmaContains(s) || la.wordformContains(s);// || la.generatedContains(s);
	}
	/**
	 * Self-validates this split result
	 * @throws Exception
	 */
	private void selfvalidate () throws Exception {
		
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			// invalidate if improbable word is encountered
			if (i < list.size()-1 && !ma.isLemmaForm(s)) {
				invalidate();
				return;
			}
//			System.err.println("Check " + s);
//			if (!inDict(s)) {
//				invalidate();
//			} 
			if (!v.isProbableWord(s)) {
				invalidate();
			}
		}
	}
	/**
	 * Invalidates this split result
	 */
	private void invalidate () {
		list = null;
	}
	/**
	 * Checks whether this split result is not invalidated
	 * @return true if split result is valid
	 */
	public boolean isValid () {
		return list != null;
	}
	
	public String toString () {
		// premature return for invalid results
		if (list == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s).append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	/**
	 * Returns the words from this split result
	 * @return words
	 */
	public List<String> getSplit() {
		return list;
	}

	@Override
	public int compareTo(SplitResult o) {
		if (o.confidence > this.confidence)
			return 1;
		if (o.confidence < this.confidence)
			return -1;
		return 0;
	}
}