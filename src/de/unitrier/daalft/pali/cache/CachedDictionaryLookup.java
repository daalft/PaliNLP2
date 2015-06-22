package de.unitrier.daalft.pali.cache;

import de.general.json.JObject;
import de.unitrier.daalft.pali.lexicon.LexiconAdapter;

public class CachedDictionaryLookup {

	private Cache<JObject[]> cache;
	private LexiconAdapter lexiconAdapter;
	
	public CachedDictionaryLookup(String domain, int port, String user, String pw, int maxCacheSize, int maxCacheDurationInSeconds) throws Exception {
		cache = new Cache<JObject[]>(maxCacheSize, maxCacheDurationInSeconds);
		lexiconAdapter = new LexiconAdapter(domain, port, user, pw);
	}
		
	public boolean lemmaExists(String lemma) {
		return (cache.get(lemma, false) != null);
	}

	public JObject[] getLemmata (String lemma) throws Exception {
		JObject[] lemmata = cache.get(lemma, false);
		if (lemmata != null) {
			return lemmata;
		}
		lemmata = lexiconAdapter.getLemmaEntriesAsJObjectArray(lemma);
		cache.put(lemma, lemmata);
		return lemmata;
	}
}
