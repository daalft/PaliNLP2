package de.unitrier.daalft.pali;

import java.util.ArrayList;
import java.util.List;

import de.cl.dictclient.DictWord;
import de.general.json.JProperty;
import de.general.json.JValue;
import de.general.log.*;
import de.unitrier.daalft.pali.morphology.Lemmatizer;
import de.unitrier.daalft.pali.morphology.MorphologyAnalyzer;
import de.unitrier.daalft.pali.morphology.MorphologyGenerator;
import de.unitrier.daalft.pali.morphology.WordclassStemmer;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;
import de.unitrier.daalft.pali.phonology.SandhiMerge;
import de.unitrier.daalft.pali.phonology.SandhiSplit;
import de.unitrier.daalft.pali.phonology.element.SplitResult;
import de.unitrier.daalft.pali.tools.WordConverter;

public class PaliNLP {

	private MorphologyGenerator mg;
	private MorphologyAnalyzer ma;
	private Lemmatizer l;
	private SandhiMerge sm;
	private SandhiSplit sp;
	private WordclassStemmer wcs;
	
	public PaliNLP()
	{
		try {
			ParadigmAccessor pa = new ParadigmAccessor();

			mg = new MorphologyGenerator(pa);
			ma = new MorphologyAnalyzer(pa);
			l = new Lemmatizer(pa);
			sm = new SandhiMerge();
			sp = new SandhiSplit();
			wcs = new WordclassStemmer(pa);
		} catch (Exception ee) {
			// TODO: change this! the exception is swallowed!
			ee.printStackTrace();
		}
	}

	/**
	 * Lemmatizes a word using rules
	 * <p>
	 * This implementation relies on the morphological analyzer
	 * and therefore is not very fast. A faster variant is the
	 * dictionary lookup lemmatisation {@link #lemmatizeWithDictionary(String)}
	 * @param word word to lemmatize
	 * @return lemmata of word
	 * @see #lemmatizeWithDictionary(String)
	 */
	public List<DictWord> lemmatize (String word) {
		try {
			return WordConverter.toDictWord(l.lemmatize(new PrintLogger(), word));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<DictWord> lemmatize (String word, String option) {
		try {
			return WordConverter.toDictWord(l.lemmatize(new PrintLogger(), word, option));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Lemmatizes a word using rules
	 * <p>
	 * This implementation relies on the morphological analyzer
	 * and therefore is not very fast. A faster variant is the
	 * dictionary lookup lemmatisation {@link #lemmatizeWithDictionary(DictWord)}
	 * @param word word to lemmatize
	 * @return lemmata of word
	 * @see #lemmatizeWithDictionary(DictWord)
	 */
	public List<DictWord> lemmatize (DictWord word) throws Exception {
		return WordConverter.toDictWord(l.lemmatize(new PrintLogger(), word));
	}

	/**
	 * Lemmatizes a word using the lexical database, with fall-back to offline mode
	 * if dictionary lookup fails
	 * @param word word to analyze
	 * @return lemmata of word
	 * @throws Exception
	 */
	public String lemmatizeWithDictionary (String word) throws Exception {
		return l.lemmatizeWithDictionary(new PrintLogger(), word);
	}

	/**
	 * Lemmatizes a word using the lexical database, with fall-back to offline mode
	 * if dictionary lookup fails
	 * @param word word to analyze
	 * @return lemmata of word
	 * @throws Exception
	 */
	public String lemmatizeWithDictionary (DictWord word) throws Exception {
		return l.lemmatizeWithDictionary(new PrintLogger(), word);
	}

	/**
	 * Returns morphological analyses of a given word using rules
	 * @param word word to analyse
	 * @return possible analyses
	 * @throws Exception
	 */
	public List<DictWord> analyze (String word) {
		try {
			return WordConverter.toDictWord(ma.analyzeWithOptions(new PrintLogger(), word));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<DictWord> analyze (String word, String pos) {
		try {
			return WordConverter.toDictWord(ma.analyzeWithOptions(new PrintLogger(), word, pos));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns morphological analyses of a given word using rules
	 * @param word word to analyse
	 * @return possible analyses
	 * @throws Exception
	 */
	public List<DictWord> analyze (DictWord word) throws Exception {
		return WordConverter.toDictWord(ma.analyze(word, new PrintLogger()));
	}

	/**
	 * Returns morphological analyses of a given word using the lexical database,
	 * falling back to rule based analysis if lexical database lookup fails
	 * @param word word to analyse
	 * @return possible analyses
	 * @throws Exception
	 */
	public String analyzeWithDictionary (String word) throws Exception {
		return ma.analyzeWithDictionary(new PrintLogger(), word, null);
	}

	/**
	 * Returns morphological analyses of a given word using the lexical database,
	 * falling back to rule based analysis if lexical database lookup fails
	 * @param word word to analyse
	 * @return possible analyses
	 * @throws Exception
	 */
	public String analyzeWithDictionary (DictWord word) throws Exception {
		String w = word.getProperty("word").toString();
		return ma.analyzeWithDictionary(new PrintLogger(), w, null);
	}

	/**
	 * Stems a word
	 * <p>
	 * Removes all endings from a word
	 * <br/><br/>
	 * <b>The word stem is not equivalent to the word lemma!</b>
	 * @param word word to stem
	 * @return word stem
	 */
	public List<String> stem (String word) {
		return wcs.stem(word);
	}

	/**
	 * Stems a word
	 * <p>
	 * Removes all endings from a word
	 * <br/><br/>
	 * <b>The word stem is not equivalent to the word lemma!</b>
	 * @param word word to stem
	 * @return word stem
	 */
	public List<DictWord> stem (DictWord word) {
		List<String> stems = wcs.stem(word.getValue("word").toString());
		List<DictWord> out = new ArrayList<DictWord>();
		for (String stem : stems) {
			DictWord w = new DictWord();
			for (JProperty p : word.getProperties()) {
				if (p.getName().equals("word")) {
					w.add("word", new JValue(stem));
				} else {
					w.add(p.getName(), p.getValue());
				}
			}
			out.add(w);
		}
		return out;
	}

	/**
	 * Generates all possible morphological forms of the given word
	 * @param word word
	 * @param wordclass wordclass
	 * @param options options
	 * @return all morphological forms
	 */
	public List<DictWord> generate (String word, String wordclass, String... options)
	{
		try {
			// NOTE: it may be reasonable to enhance the generate-method in such a way, that a logger is passed from some caller
			// NOTE: the basic idea might be that regardless of the application type - console, GUI or client/server - all output
			// NOTE: that might be created during execution will be written using the same infrastructure - a logging infrastructure
			return WordConverter.toDictWord(mg.generate(new PrintLogger(), word, wordclass, options));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Merges two or more words according to the rules of sandhi
	 * <p>
	 * Returns possible merges
	 * @param words words to merge
	 * @return merged words
	 */
	public List<String> merge (String...words) {
		return sm.merge(words);
	}

	/**
	 * Returns a list containing the split results
	 * <p>
	 * Each list entry is a SplitResult. SplitResult is a list of strings.
	 * Each SplitResult represents
	 * one possible split for the given word. Additionaly, each
	 * SplitResult contains the confidence of the split being valid 
	 * @param word word to split
	 * @param depth depth
	 * @return split result
	 */
	public List<SplitResult> split (String word, int depth) {
		return sp.split(word, depth);
	}
}
