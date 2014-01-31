package de.unitrier.daalft.pali;

import java.util.List;

import lu.cl.dictclient.DictWord;
import lu.general.json.JProperty;
import lu.general.json.JValue;
import de.unitrier.daalft.pali.morphology.Lemmatizer;
import de.unitrier.daalft.pali.morphology.MorphologyAnalyzer;
import de.unitrier.daalft.pali.morphology.MorphologyGenerator;
import de.unitrier.daalft.pali.morphology.NaiveStemmer;
import de.unitrier.daalft.pali.phonology.SandhiMerge;
import de.unitrier.daalft.pali.phonology.SandhiSplit;
import de.unitrier.daalft.pali.phonology.element.SplitResult;
import de.unitrier.daalft.pali.tools.WordConverter;

public class PaliNLP {

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
	public static List<DictWord> lemmatize (String word) {
		try {
			return WordConverter.toDictWord(Lemmatizer.lemmatize(word));
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
	public static List<DictWord> lemmatize (DictWord word) throws Exception {
		return WordConverter.toDictWord(Lemmatizer.lemmatize(word));
	}
	
	/**
	 * Lemmatizes a word using the lexical database, with fall-back to offline mode
	 * if dictionary lookup fails
	 * @param word word to analyze
	 * @return lemmata of word
	 * @throws Exception
	 */
	public static List<DictWord> lemmatizeWithDictionary (String word) throws Exception {
		return Lemmatizer.lemmatizeWithDictionary(word);
	}
	
	/**
	 * Lemmatizes a word using the lexical database, with fall-back to offline mode
	 * if dictionary lookup fails
	 * @param word word to analyze
	 * @return lemmata of word
	 * @throws Exception
	 */
	public static List<DictWord> lemmatizeWithDictionary (DictWord word) throws Exception {
		return Lemmatizer.lemmatizeWithDictionary(word);
	}
	
	/**
	 * Returns morphological analyses of a given word using rules
	 * @param word word to analyse
	 * @return possible analyses
	 * @throws Exception
	 */
	public static List<DictWord> analyze (String word) {
		try {
			return WordConverter.toDictWord(MorphologyAnalyzer.analyze(word));
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
	public static List<DictWord> analyze (DictWord word) throws Exception {
		return WordConverter.toDictWord(MorphologyAnalyzer.analyze(word));
	}
	
	/**
	 * Returns morphological analyses of a given word using the lexical database,
	 * falling back to rule based analysis if lexical database lookup fails
	 * @param word word to analyse
	 * @return possible analyses
	 * @throws Exception
	 */
	public static List<DictWord> analyzeWithDictionary (String word) throws Exception {
		return MorphologyAnalyzer.analyzeWithDictionary(word);
	}
	
	/**
	 * Returns morphological analyses of a given word using the lexical database,
	 * falling back to rule based analysis if lexical database lookup fails
	 * @param word word to analyse
	 * @return possible analyses
	 * @throws Exception
	 */
	public static List<DictWord> analyzeWithDictionary (DictWord word) throws Exception {
		return MorphologyAnalyzer.analyzeWithDictionary(word);
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
	public static String stem (String word) {
		NaiveStemmer ns = new NaiveStemmer();
		return ns.stem(word);
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
	public static DictWord stem (DictWord word) {
		DictWord w = new DictWord();
		NaiveStemmer ns = new NaiveStemmer();
		String stem = ns.stem(word.getValue("word").toString());
		for (JProperty p : word.getProperties()) {
			if (p.getName().equals("word")) {
				w.add("word", new JValue(stem));
			} else {
				w.add(p.getName(), p.getValue());
			}
		}
		return w;
	}
	
	/**
	 * Generates all possible morphological forms of the given word
	 * @param word word
	 * @param wordclass wordclass
	 * @param options options
	 * @return all morphological forms
	 */
	public static List<DictWord> generate (String word, String wordclass, String... options) {
		try {
			return WordConverter.toDictWord(MorphologyGenerator.generate(word, wordclass, options));
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
	public static List<String> merge (String...words) {
		return SandhiMerge.merge(words);
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
	public static List<SplitResult> split (String word, int depth) {
		return SandhiSplit.split(word, depth);
	}
}
