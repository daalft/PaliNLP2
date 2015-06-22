package de.unitrier.daalft.pali.cache;

import java.util.HashSet;
import java.util.List;

import de.general.log.ILogInterface;
import de.unitrier.daalft.pali.morphology.Lemmatizer;
import de.unitrier.daalft.pali.morphology.element.ConstructedWord;
import de.unitrier.daalft.pali.morphology.paradigm.ParadigmAccessor;

public class CachedLemmatizer {

	private Cache<List<ConstructedWord>> cache;
	private Lemmatizer lemmatizer;
	private HashSet<String> invalid;
	
	public CachedLemmatizer(ParadigmAccessor pa, int maxCacheSize, int maxCacheDurationInSeconds) throws Exception {
		cache = new Cache<List<ConstructedWord>>(maxCacheSize, maxCacheDurationInSeconds);
		lemmatizer = new Lemmatizer(pa);
		invalid = new HashSet<>();
	}
		
	public boolean lemmaExists(String lemma) {
		return (cache.get(lemma, false) != null);
	}

	public List<ConstructedWord> lemmatize (String word, String pos) throws Exception {
		List<ConstructedWord> lemmata = cache.get(word, false);
		if (lemmata != null) {
			return lemmata;
		}
		lemmata = lemmatizer.lemmatize(null, word, pos);
		if (lemmata == null) {
			invalid.add(word);
			return null;
		}
		cache.put(word, lemmata);
		return lemmata;
	}

	public String lemmatizeWithDictionary(ILogInterface log, String word,
			String pos) throws Exception {
		return lemmatizer.lemmatizeWithDictionary(log, word, pos);
	}
}
