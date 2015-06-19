package de.unitrier.daalft.pali.lexicon;

import de.general.json.JObject;

public interface DictionaryLookup {

	public boolean lemmaExists (String lemma);

	public JObject[] getLemmata(String lemma);
	
}
