package de.unitrier.daalft.pali.morphology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lu.cl.dictclient.DictWord;

import de.unitrier.daalft.pali.lexicon.LexiconAdapter;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.strategy.NumeralStrategy;
import de.unitrier.daalft.pali.morphology.tools.VerbHelper;
import de.unitrier.daalft.pali.tools.WordConverter;
/**
 * Class responsible for returning the lemma of a given word
 * @author David
 *
 */
public class Lemmatizer {

	/**
	 * Word class specific endings
	 */
	private final static String 
			VERB_ENDING = "ti",
			NUMERAL_ENDING_5_18 = "a";
	/**
	 * Word class specific endings
	 */
	private final static String[] 
			NOUN_ENDINGS = {"as", "a", "u", "us", "i", "in", "ar", "an", "ant", "ā", "ī", "ū"},
			ADJ_ENDINGS = {"a", "i", "u", "ant", "ā", "ī", "ū"},
			NUMERAL_ENDING_19 = {"a", "i", "aṃ"};
	
	/**
	 * Constructor
	 */
	public Lemmatizer () {
		
	}

	/**
	 * Lemmatize in offline mode
	 * <p>
	 * Invokes the analyzer and extracts the lemma information<br/><br/>
	 * Possibly slower than dictionary lookup
	 * @param word word to lemmatize
	 * @return lemmata
	 */
	public static List<ConstructedWord> lemmatize (String word, String... options) {
		List<ConstructedWord> analyses = MorphologyAnalyzer.analyze(word, options);
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		for (ConstructedWord cw : analyses) {
			ConstructedWord lemma = new ConstructedWord();
			lemma.setWord(word);
			lemma.setLemma(cw.getLemma());
			lemma.setInfo(new FeatureSet("paradigm", cw.getInfo().getFeature("paradigm")));
			if (!out.contains(lemma))
				out.add(lemma);
		}
		return out;
	}
	
	/**
	 * Lemmatize in offline mode
	 * <p>
	 * Invokes the analyzer and extracts the lemma information<br/><br/>
	 * Possibly slower than dictionary lookup
	 * @param word word to lemmatize
	 * @return lemmata
	 */
	public static List<ConstructedWord> lemmatize (DictWord word) {
		String w = word.getValue("word").toString();
		String wc = word.getValue("pos") == null ? "" : word.getValue("pos").toString();
		return lemmatize(w, wc);
	}
	
	/**
	 * Lemmatize in online mode
	 * <p>
	 * Looks up the word in the dictionary. If dictionary lookup fails,
	 * falls back to offline mode
	 * @param word word to lemmatize
	 * @return lemmata
	 * @throws Exception
	 */
	public static List<DictWord> lemmatizeWithDictionary (String word) throws Exception {
		LexiconAdapter la = new LexiconAdapter();
		if (la.lemmaContains(word)) {
			return la.getLemma(word);
		} else {
			System.err.println("Could not retrieve lemma via lookup. Falling back to offline mode.");
			return WordConverter.toDictWord(lemmatize(word));
		}
	}
	
	/**
	 * Lemmatize in online mode
	 * <p>
	 * Looks up the word in the dictionary. If dictionary lookup fails,
	 * falls back to offline mode
	 * @param word word to lemmatize
	 * @return lemmata
	 * @throws Exception
	 */
	public static List<DictWord> lemmatizeWithDictionary (DictWord word) throws Exception {
		String w = word.getValue("word") == null ? "" : word.getValue("word").toString();
		if (w == null || w.isEmpty()) {
			w = word.getValue("lemma") == null ? "":word.getValue("lemma").toString();
		}
		if (w == null || w.isEmpty()) {
			System.err.println("Could not extract word from " + word.toString());
			return null;
		}
		return lemmatizeWithDictionary(w);
	}
	
	/**
	 * Returns the lemma built from a given word stem and a given word class
	 * @param stem word stem
	 * @param wc word class
	 * @param strings options
	 * @return lemmata
	 */
	public List<String> lemmaFromStem (String stem, String wc, String...strings) {
		if (strings.length > 0 && !strings[0].isEmpty()) {
			String dec = strings[0];
			return Collections.singletonList(stem + dec);
		}
		List<String> out = new ArrayList<String>();
		switch (wc) {
		case "noun":
			// append all noun declensions
			for (String s : NOUN_ENDINGS)
				out.add(stem + s);
			break; 
		case "verb": 
			VerbHelper vh = new VerbHelper();
			// if we assume that "stem" is in fact a root
			List<String> stems = vh.stemFromRoot(stem);
			// and that "stem" could be the stem
			out.add(stem + VERB_ENDING);
			for (String s : stems)
				out.add(s + VERB_ENDING);
			break;
		case "numeral":
			// one to four should have been caught by analyzer before this point
			if (NumeralStrategy.isFiveTo18Stem(stem)) {
				out.add(stem + NUMERAL_ENDING_5_18);
			} else if (NumeralStrategy.is19upStem(stem)) {
				for (String s : NUMERAL_ENDING_19) {
					out.add(stem + s);
				}
			}
			break;
		case "adjective":
			for (String s : ADJ_ENDINGS)
				out.add(stem + s);
			break;
		case "adverb":
			out.add(stem); break;
		default: break;
		}
		return out;
	}
}
