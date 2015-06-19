package de.unitrier.daalft.pali.lexicon;

import java.util.HashMap;
import java.util.HashSet;

import de.general.json.JObject;

public class CachedDictionaryLookup implements DictionaryLookup {

	private long timestamp;
	private long decayTime = 300000; // 5 minutes
	private HashMap<String, Boolean> booleanCache;
	private LexiconAdapter lexiconAdapter;
	
	public CachedDictionaryLookup () {
		this("germa232.uni-trier.de", 8080, "testrw", "testrw");
	}
	
	public CachedDictionaryLookup(String domain, int port, String user, String pw) {
		try {
			lexiconAdapter = new LexiconAdapter(domain, port, user, pw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		booleanCache = new HashMap<>();
	}
	
	@Override
	public boolean lemmaExists(String lemma) {
		if ((System.currentTimeMillis() - timestamp) > decayTime) {
			clearCache();
			timestamp = System.currentTimeMillis();
		}
		if (booleanCache.containsKey(lemma)) return booleanCache.get(lemma);
		boolean lookupValue = false;
		try {
			lookupValue = lexiconAdapter.lemmaContains(lemma);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		booleanCache.put(lemma, lookupValue);
		return lookupValue;
	}

	private void clearCache() {
		booleanCache.clear();
	}

	@Override
	public JObject[] getLemmata(String lemma) {
		// TODO Auto-generated method stub
		return null;
	}

}
