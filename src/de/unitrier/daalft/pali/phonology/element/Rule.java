package de.unitrier.daalft.pali.phonology.element;

import java.util.List;
import de.general.simpletokenizing.Token;
/**
 * Represents a sandhi replacement rule
 * @author David
 *
 */
public class Rule {

	private String left, right;

	////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////
	
	public Rule() {

	}
	
	public Rule(String l, String r) {
		left = l.trim();
		right = r.trim().replace("\\", "$");
	}

	public Rule(Token[] array) {
		left = strip(array[0]).trim();
		right = strip(array[2]).trim().replace("\\", "$");
	}

	public Rule(List<Token> ret) {
		this(ret.toArray(new Token[3]));
	}

	////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////
	
	public boolean isApplicable(String s) {
		return s.matches(".*"+left+".*");
	}

	public String applyFirst(String s) {
		return s.replaceFirst(left, right);
	}

	public String applyAll(String s) {
		return s.replaceAll(left, right);
	}

	@Override
	public String toString () {
		return left + " -> " + right;
	}

	private String strip(Token t) {
		String tr = t.toString();
		int l = tr.length()-1;
		return tr.substring(1, l);
	}
}
