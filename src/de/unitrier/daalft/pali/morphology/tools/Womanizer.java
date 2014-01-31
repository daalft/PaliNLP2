package de.unitrier.daalft.pali.morphology.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that forms feminine bases from masculine bases
 * <p>
 * For example:<table border=1><tr><th>Masculine base</th><th>Translation</th>
 * <th>Derived feminine base</th><th>Translation</th></tr>
 * <tr><td>sīha</td><td>Lion</td>
 * <td>sīhī, sīhinī</td><td>Lioness</td></tr>
 * <tr><td>rājā</td><td>King</td>
 * <td>rājinī</td><td>Queen</td></tr>
 * <tr><td>bhikkhu</td><td>Buddhist monk</td>
 * <td>bhikkhunī</td><td>Buddhist nun</td></tr>
 * </table>
 * @author David
 *
 */
public class Womanizer {

	/**
	 * Rules for the formation of feminine bases
	 * <p>
	 * Rules are written in the format:<br/>
	 * word-ending, replacement
	 * <br/>
	 * 
	 */
	private static final String[][] RULES = {
		// short a
		{"a", "ā"},
		{"a", "ī"},
		{"a", "inī"},
		{"a", "ānī"},
		// long a
		{"ā", "inī"},
		// short u
		{"u", "unī"},
		// long u
		{"ū", "unī"},
		// long i
		{"ī", "inī"},
		// short i 
		{"i", "inī"},
		{"i", "ānī"}
		}; 
	
	/**
	 * Returns a list of possible feminine bases formed
	 * from the given masculine base
	 * @param base masculine base
	 * @return feminine bases
	 */
	public static List<String> getFeminineBases (String base) {
		// initialize output list
		List<String> result = new ArrayList<String>();
		// for each rule
		for (int i = 0; i < RULES.length; i++) {
			// retrieve first element (aka FROM)
			String f = RULES[i][0];
			// retrieve second element (aka TO)
			String t = RULES[i][1];
			// if lemma ends with FROM, replace FROM by TO
			if (base.endsWith(f)) {
				// add to output list
				result.add(base.replaceFirst(f+"$", t));
			}
		}
		// return output
		return result;
	}
}
