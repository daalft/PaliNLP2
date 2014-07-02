package de.unitrier.daalft.pali.morphology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.general.log.*;

import de.cl.dictclient.DictWord;
import de.unitrier.daalft.pali.lexicon.LexiconAdapter;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.strategy.NumeralStrategy;
import de.unitrier.daalft.pali.morphology.tools.VerbHelper;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.tools.WordConverter;


/**
 * Class responsible for returning the lemma of a given word
 * @author David
 *
 */
public class Lemmatizer {

	private MorphologyAnalyzer ma;

	/**
	 * Constructor
	 */
	public Lemmatizer(ParadigmAccessor pa)
	{
		ma = new MorphologyAnalyzer(pa);
	}

	/**
	 * Lemmatize in offline mode
	 * <p>
	 * Invokes the analyzer and extracts the lemma information<br/><br/>
	 * Possibly slower than dictionary lookup
	 * @param word word to lemmatize
	 * @return lemmata
	 */
	public List<ConstructedWord> lemmatize(ILogInterface log, String word, String... options) {
		List<ConstructedWord> analyses = ma.analyze(log, word, options);
		List<ConstructedWord> out = new ArrayList<ConstructedWord>();
		for (ConstructedWord cw : analyses) {
			ConstructedWord lemma = new ConstructedWord();
			lemma.setWord(word);
			lemma.setLemma(cw.getLemma());
			lemma.setFeatureSet(new FeatureSet("paradigm", cw.getFeatureSet().getFeature("paradigm")));
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
	public List<ConstructedWord> lemmatize(ILogInterface log, DictWord word) {
		String w = word.getValue("word").toString();
		String pos = word.getValue("pos") == null ? "" : word.getValue("pos").toString();
		return lemmatize(log, w, pos);
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
	public String lemmatizeWithDictionary(ILogInterface log, String word, String...opt) throws Exception {
		LexiconAdapter la = new LexiconAdapter();
		if (la.lemmaContains(word)) {
			return la.getLemma(word);
		} else {
			System.err.println("Could not retrieve lemma via lookup. Falling back to offline mode.");
			return WordConverter.toJSONStringLemmatizer(lemmatize(log, word, opt));
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
	public String lemmatizeWithDictionary(ILogInterface log, DictWord word) throws Exception {
		String w = word.getValue("word") == null ? "" : word.getValue("word").toString();
		if (w == null || w.isEmpty()) {
			w = word.getValue("lemma") == null ? "":word.getValue("lemma").toString();
		}
		if (w == null || w.isEmpty()) {
			System.err.println("Could not extract word from " + word.toString());
			return null;
		}
		return lemmatizeWithDictionary(log, w);
	}
	
}
