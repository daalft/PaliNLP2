package de.unitrier.daalft.pali.lexicon;

import java.util.HashSet;

public class CachedDictionaryLookup implements DictionaryLookup {

	private long timestamp;
	private long decayTime = 300000; // 5 minutes
	private HashSet<String> cache;
	private LexiconAdapter lexiconAdapter;
	
	public CachedDictionaryLookup() {
		try {
			lexiconAdapter = new LexiconAdapter();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cache = new HashSet<>();
	}
	
	@Override
	public boolean lemmaExists(String lemma) {
		if ((System.currentTimeMillis() - timestamp) > decayTime) {
			clearCache();
			timestamp = System.currentTimeMillis();
		}
		if (cache.contains(lemma)) return true;
		boolean lookupValue = false;
		try {
			lookupValue = lexiconAdapter.lemmaContains(lemma);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (lookupValue)
			cache.add(lemma);
		return lookupValue;
	}

	private void clearCache() {
		cache.clear();
	}

}
