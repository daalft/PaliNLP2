package de.unitrier.daalft.pali.lexicon;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.cl.dictclient.DictWord;
import de.cl.dictclient.DictionaryClient;
import de.cl.dictclient.DictionaryCollection;
import de.cl.dictclient.impl.ConnectionProfile;
import de.general.json.JObject;

/**
 * Provides functionality related to the lexical database<br/>
 * Requires an active internet connection
 * @author David
 *
 */
public class LexiconAdapter {

	/**
	 * Dictionary names
	 */
	private final static String WORDFORMS = "paliwordforms",
			LEMMA = "pali_main_new", GENERATED = "paligenerated";

	/**
	 * Client
	 */
	private DictionaryClient dc;
	/**
	 * Connection profile
	 */
	private ConnectionProfile cp = new ConnectionProfile("germa232.uni-trier.de", 8080, "testrw", "testrw");//TODO = StandardProfile.getProfile();

	/**
	 * Constructor
	 * @throws Exception
	 */
	public LexiconAdapter() throws Exception {
		dc = new DictionaryClient(cp);
	}

	/**
	 * Performs a ping
	 * @throws Exception
	 */
	public void ping () throws Exception {
		dc.ping();
	}

	/**
	 * Returns the names of all collections currently in the lexical database
	 * @throws Exception
	 */
	public void listCollections () throws Exception {
		for (String s : dc.listCollections()) 
			System.out.println(s);
	}

	/**
	 * Checks whether the wordform collection contains the given word
	 * @param form word
	 * @return true if wordform collection contains word
	 * @throws Exception
	 */
	public boolean wordformContains (String form) throws Exception {
		DictionaryCollection c = dc.getCollection(WORDFORMS);
		return c.getWordsByTags("word", form).length > 0;
	}

	/**
	 * Checks whether the lemma collection contains the given word
	 * @param lemma word
	 * @return true if lemma collection contains word
	 * @throws Exception
	 */
	public boolean lemmaContains (String lemma) throws Exception {
		DictionaryCollection c = dc.getCollection(LEMMA);

		return c.getWordsByTags("form.lemma", lemma).length > 0;
	}

	/**
	 * Checks whether the generated collection contains the given word
	 * @param word word
	 * @return true if generated collection contains word
	 * @throws Exception
	 */
	public boolean generatedContains (String word) {
		DictionaryCollection c;
		try {
			c = dc.getCollection(GENERATED);
		} catch (Exception e) {
			return false;
		}
		
		try {
			return c.getWordsByTags("word", word).length > 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Returns the dictionary words corresponding to the given
	 * word, extracted from the generated collection
	 * @param word word to extract
	 * @return dictionary words
	 * @throws Exception
	 */
	public String getGenerated (String word) throws Exception {
		if (generatedContains(word)) {
			DictionaryCollection c = dc.getCollection(GENERATED);
			DictWord[] dws = c.getWordsByTags("word", word);
			if (dws.length > 1) {
				StringBuilder out = new StringBuilder("[");
				for (DictWord dw : dws) {
					out.append(dw.toJSON()).append(",");
				}
				out.deleteCharAt(out.length()-1);
				out.append("]");
				return out.toString();
			} else {
				return dws[0].toJSON();
			}
		} else {
			System.err.println("Wordform " + word + " cannot be found in " + GENERATED);
		}
		return null;
	}
	
	/**
	 * Returns the dictionary words corresponding to the given
	 * word, extracted from the wordform collection
	 * @param form word to extract
	 * @return dictionary words
	 * @throws Exception
	 */
	public List<DictWord> getWordform (String form) throws Exception {
		if (wordformContains(form)) {
			DictionaryCollection c = dc.getCollection(WORDFORMS);
			DictWord[] dws = c.getWordsByTags("word", form);
			if (dws.length > 1) {
				return Arrays.asList(dws);
			} else {
				return Collections.singletonList(dws[0]);
			}
		} else
			System.err.println("Wordform " + form + " cannot be found in " + WORDFORMS);
		return null;
	}
	
	/**
	 * Returns the dictionary words corresponding to the given
	 * word, extracted from the lemma collection
	 * @param lemma word to extract
	 * @return dictionary words
	 * @throws Exception
	 */
	public String getLemma (String lemma) throws Exception {
		DictionaryCollection c = dc.getCollection(LEMMA);
		if (lemmaContains(lemma)) {
			DictWord[] dws = c.getWordsByTags("form.lemma", lemma);
			if (dws.length > 1) {
				StringBuilder out = new StringBuilder("[");
				for (DictWord dw : dws) {
					out.append(dw.toJSON()).append(",");
				}
				out.deleteCharAt(out.length()-1);
				out.append("]");
				return out.toString();
			} else {
				return dws[0].toJSON();
			}
		} else {
			System.err.println("Lemma " + lemma + " cannot be found in " + LEMMA);
		}
		return null;
	}
	
	public JObject[] getLemmaEntriesAsJObjectArray (String lemma) throws Exception {
		DictionaryCollection c = dc.getCollection(LEMMA);
		if (lemmaContains(lemma)) {
			return c.getWordsByTags("form.lemma", lemma);
		}
		return null;
	}
	
	/**
	 * Checks whether the given word is contained in any collection
	 * @param word word to check
	 * @return true if any collection contains word
	 * @throws Exception
	 */
	public boolean wordInDict (String word) throws Exception {
		return wordformContains(word) || lemmaContains(word) || generatedContains(word);
	}
	
	public static void main(String[] args) {
		try {
			new LexiconAdapter().test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void test () throws Exception {
		this.listCollections();
	}
	public List<DictWord> getAllWordforms() throws Exception {
		List<DictWord> list = new ArrayList<DictWord>();
		DictionaryCollection c = dc.getCollection(LEMMA);
		Iterator<DictWord> i = c.getWordIterator(200);
		
		for (DictWord id = i.next(); i.hasNext(); id = i.next()) {
			list.add(id);
			
		}
		DictionaryCollection c2 = dc.getCollection(WORDFORMS);
		Iterator<DictWord> i2 = c2.getWordIterator(200);
		for (DictWord id = i2.next(); i2.hasNext(); id = i2.next()) {
			if (!list.contains(id))
				list.add(id);
		}
		return list;
	}
	
}
