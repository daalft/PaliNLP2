package de.unitrier.daalft.pali.phonology;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.unitrier.daalft.pali.phonology.element.SandhiRule;
import de.unitrier.daalft.pali.phonology.element.SandhiTableEntry;
import de.unitrier.daalft.pali.phonology.element.SplitResult;

/**
 * Class used to handle sound changes
 * @author David
 *
 */
public class SoundChanger {

	/**
	 * Returns all spelling alternatives for a given word
	 * @param word word
	 * @return spelling alternatives
	 */
	public List<String> getAlternatives (String word) {
		SandhiManager sm = SandhiManager.getInstance();
		SandhiTable st = new SandhiTable();
		for (int i = 0; i < word.length(); i++) {
			for (SandhiRule sr : sm.getSoundRules()) {
				if (sr.isApplicable(word.substring(i))) {
					st.push(i, sr);
				}
			}
		}
		List<String> out = new ArrayList<String>();
		List<SplitResult> li = new LinkedList<SplitResult>();
		for (SandhiTableEntry e : st) {
			SplitResult sr = new SplitResult(e.getRule().applyPosSplit(e.getPosition(), word), e.getPosition(), e.getRule());
			li.add(sr);
		}
		//List<SplitResult> prev = new LinkedList<SplitResult>();
		// TODO
		// repeat x times:
		// foreach entry in table
		// if rule contains not position (updated)
		// apply rule if applicable (position updated)
		return out;
	}

	/**
	 * Performs the common change from ava=>o and aya=>e
	 * @param word word
	 * @return word with sound change
	 */
	public static String getCommonChange (String word) {
		return word.replace("ava", "o").replace("aya", "e");
	}
}
