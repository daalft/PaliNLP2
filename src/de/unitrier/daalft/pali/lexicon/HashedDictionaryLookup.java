package de.unitrier.daalft.pali.lexicon;

import java.util.HashMap;
import java.util.HashSet;
import de.general.json.JObject;

public class HashedDictionaryLookup implements DictionaryLookup {

	private HashSet<String> invalid;
	private HashMap<String, JObject[]> valid;
	private LexiconAdapter lexicon;
	private long timestamp;
	private long decayTime = 300000; // 5 minutes
	
	public HashedDictionaryLookup(String domain, int port, String user, String pw) throws Exception {
		lexicon = new LexiconAdapter(domain, port, user, pw);
		invalid = new HashSet<>();
		valid = new HashMap<>();
	}
	
	public void setDecayTime (int minutes) {
		decayTime = minutes * 60 * 1000;
	}
	
	@Override
	public boolean lemmaExists(String lemma) {
		if ((System.currentTimeMillis()-timestamp) > decayTime) {
			invalid.clear();
			valid.clear();
			timestamp = System.currentTimeMillis();
		}
		if (invalid.contains(lemma))
			return false;
		if (valid.containsKey(lemma)) {
			return true;
		} else {
			try {
				JObject[] lemmata = lexicon.getLemmaEntriesAsJObjectArray(lemma);
				if (lemmata != null) {
					return true;
				} else {
					
					invalid.add(lemma);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return false;
	}

	public JObject[] getLemmata (String lemma) {
		if (!lemmaExists(lemma))
			return null;
		
		return valid.get(lemma);
	}
}
