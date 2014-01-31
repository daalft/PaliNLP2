package de.unitrier.daalft.pali.phonology.element;
/**
 * Class representing a sandhi table entry
 * @author David
 *
 */
public class SandhiTableEntry extends TableEntry<Integer, SandhiRule> {
	/**
	 * Constructor
	 * @param i position
	 * @param r rule
	 */
	public SandhiTableEntry(Integer i, SandhiRule r) {
		super(i, r);
	}
	/**
	 * Returns this position
	 * <p>
	 * Equivalent to getFirst() from superclass
	 * @return position
	 */
	public Integer getPosition () {
		return super.getFirstElement();
	}
	/**
	 * Returns this rule
	 * <p>
	 * Equivalent to getSecond() from superclass
	 * @return rule
	 */
	public SandhiRule getRule () {
		return super.getSecondElement();
	}
}
