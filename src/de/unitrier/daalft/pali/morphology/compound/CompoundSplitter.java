package de.unitrier.daalft.pali.morphology.compound;

import de.unitrier.daalft.pali.lexicon.CachedDictionaryLookup;

/**
 * Class responsible for splitting Pali compound words,
 * using the dictionary as knowledge source
 * @author s2daalft
 *
 */
public class CompoundSplitter {

	/**
	 * Set the minimum postfix length
	 * <p>
	 * This value will be used to determine whether
	 * a split produced a word that is too short and hence
	 * invalid.
	 */
	private int minimumPostfixLength = 3;
	/**
	 * Dictionary lookup utility to check for existence of word in dictionary
	 */
	private CachedDictionaryLookup dictionaryLookup;

	/**
	 * Constructor
	 * @throws Exception 
	 */
	public CompoundSplitter(String domain, int port, String user, String pw, int maxCacheSize, int maxCacheDurationInSeconds) throws Exception {
		dictionaryLookup = new CachedDictionaryLookup(domain, port, user, pw, maxCacheSize, maxCacheDurationInSeconds);
	}

	/**
	 * Split a compound word
	 * <p>
	 * If the word exists in the dictionary,
	 * the word itself is returned <b>unless</b>
	 * the flag <code>force</code> is specified.
	 * In that case the word is split even if it
	 * occurs in the dictionary.
	 * <br/><br/>
	 * If the word cannot be split, <code>null</code>
	 * is returned
	 * @param lemma lemma of compound to split
	 * @param force flag indicating whether to split regardless of dictionary occurrence
	 * @return split compound or word itself or <code>null</code>
	 * @throws Exception
	 */
	public String[] split(String lemma, boolean force) throws Exception {
		if (lemma==null || lemma.isEmpty())
			return null;
		boolean exists = dictionaryLookup.lemmaExists(lemma);
		if (exists)
			if (!force)
				return new String[]{lemma};
			else  // forcibly split even if word in dictionary
				return _split(lemma);
		else // word not in dict; split 
			return _split(lemma);
	}

	/**
	 * Private split method
	 * @param lemma lemma of compound to split
	 * @return split of compound or input or null
	 * @throws Exception
	 */
	private String[] _split (String lemma) throws Exception {
		for (int i = 1; i < lemma.length(); i++) {
			String postfix = lemma.substring(i);
			if (dictionaryLookup.lemmaExists(postfix)) {
				int postfixLength = postfix.length();
				if (postfixLength >= minimumPostfixLength) {
					String stem = lemma.substring(0, lemma.indexOf(postfix));
					if (dictionaryLookup.lemmaExists(stem)) {
						// word and postfix in dict
						// probably [stem+postfix] correct splitting
						return new String[]{stem,postfix};
					} else {
						// stem not in dictionary
						// => multiple compound?
						// => other reason?
						// System.err.println("Stem [" + stem + "] not found in dictionary");
						return null;
					}
				} else { // postfix is shorter than minimum required length
					// System.err.println("Postfix length < min length: " + postfix);
					return null;
				}
			}
		}
		// split failed
		return null;
	}

	public static void main(String[] args) throws Exception {
//		CompoundSplitter i = new CompoundSplitter();
//		String[] split = i._split("buddhaguṇa");
//		if (split == null)
//			return;
//		for (String a : split) {
//			System.out.println(a);
//		}
		
	}
}
