package de.unitrier.daalft.pali.lexicon;

import de.cl.dictclient.DictWord;
import de.general.json.JObject;
/**
 * Subclass to access the protected JObject constructor 
 * of the superclass
 * @author David
 *
 */
public class DictWordN extends DictWord {

	/**
	 * Inherited constructor
	 * @param obj JObject
	 * @throws Exception
	 */
	public DictWordN (JObject obj) throws Exception {
		super(obj);
	}
}
