package de.unitrier.daalft.pali.phonology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.unitrier.daalft.pali.phonology.element.SandhiRule;
import de.unitrier.daalft.pali.phonology.element.SandhiTableEntry;
import de.unitrier.daalft.pali.phonology.element.SplitResult;
/**
 * Class responsible for splitting words according
 * to the rules of sandhi
 * @author David
 *
 */
public class SandhiSplit {

	/**
	 * Sandhi manager instance
	 */
	private SandhiManager sm;
	
	public SandhiSplit () {
		sm  = new SandhiManager();
	}
	
	public SandhiSplit (int d) {
		this();
		defaultDepth = d;
	}
	/**
	 * Default splitting depth
	 */
	private int defaultDepth = 2;
	
	/**
	 * Splits a word into possible constituent words according to
	 * the rules of sandhi
	 * <p>
	 *
	 * <em>depth</em> specifies the depth of the split. The method will be 
	 * called at most <em>depth</em> times. If the parameter <em>depth</em> is
	 * specified as <em>0</em>, the default depth of 3 will be used
	 * @param word word to split
	 * @param depth depth of split
	 * @return possible splits
	 */
	public List<SplitResult> split (String word, int depth) {
		if (depth == 0)
			depth = defaultDepth;
		List<SplitResult> out = new ArrayList<SplitResult>();
		List<SplitResult> result = splitWord(word);
		out.addAll(result);
		
		Set<SplitResult> set = new LinkedHashSet<SplitResult>(out);
		List<SplitResult> finalOut = new ArrayList<SplitResult>(set);
		Collections.sort(finalOut);
		return finalOut;
	}
	
	/**
	 * Creates the initial set of split words from a word
	 * @param word word to split
	 * @return split result
	 */
	private List<SplitResult> splitWord (String word) {

		List<SplitResult> result = new ArrayList<SplitResult>();
		SandhiTable st = new SandhiTable();
		for (int i = 0; i < word.length(); i++) {
			for (SandhiRule sr : sm.getReverseRules()) {
				if (sr.isApplicable(word.substring(i))) {
					st.push(i, sr);
				}
			}
		}
		for (SandhiTableEntry e : st) {
			SplitResult sr = new SplitResult(e.getRule().applyPosSplit(e.getPosition(),word), e.getPosition(), e.getRule());
			if (!result.contains(sr))
				result.add(sr);
		}
		return cleanList(result);
	}
	
	/**
	 * Removes any invalid split results from a list
	 * @param list list to clean
	 * @return cleaned list
	 */
	private List<SplitResult> cleanList (List<SplitResult> list) {
		List<SplitResult> copy = new ArrayList<SplitResult>();
		copy.addAll(list);
		for (SplitResult sr : copy)
			if (!sr.isValid())
				list.remove(sr);
		return list;
	}
}
