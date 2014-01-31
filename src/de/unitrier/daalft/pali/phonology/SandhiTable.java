package de.unitrier.daalft.pali.phonology;

import java.util.Iterator;
import java.util.Stack;

import de.unitrier.daalft.pali.phonology.element.SandhiRule;
import de.unitrier.daalft.pali.phonology.element.SandhiTableEntry;
/**
 * Class representing a table with entries
 * @author David
 *
 */
public class SandhiTable implements Iterable<SandhiTableEntry> {
	/**
	 * Stack of entries
	 */
	private Stack<SandhiTableEntry> stack;
	/**
	 * Constructor
	 */
	public SandhiTable () {
		stack = new Stack<SandhiTableEntry>();
	}
	/**
	 * Creates a new table entry from the specified arguments
	 * and pushes the new entry onto the stack
	 * @param i position
	 * @param r rule
	 */
	public void push (int i, SandhiRule r) {
		stack.push(new SandhiTableEntry(i, r));
	}
	/**
	 * Pops the object at the top of this stack and returns it
	 * @return top object
	 */
	public SandhiTableEntry pop () {
		return stack.pop();
	}
	/**
	 * Checks whether this table contains
	 * the entry specified by the arguments
	 * @param i position
	 * @param r rule
	 * @return true if table contains entry
	 */
	public boolean contains (int i, SandhiRule r) {
		SandhiTableEntry entry = new SandhiTableEntry(i,r);
		return stack.contains(entry);
	}
	/**
	 * Checks whether this table contains
	 * the specified entry
	 * @param te table entry
	 * @return true if table contains entry
	 */
	public boolean contains (SandhiTableEntry te) {
		return stack.contains(te);
	}
	/**
	 * Default toString method
	 */
	public String toString () {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stack.size(); i++) {
			sb.append(stack.elementAt(i)).append(System.lineSeparator());
		}
		return sb.toString();
	}

	@Override
	public Iterator<SandhiTableEntry> iterator() {
		return stack.iterator();
	}
	/**
	 * Checks whether this table is empty
	 * <p>
	 * The table is regarded as empty if it
	 * contains no entries
	 * @return true if table is empty
	 */
	public boolean isEmpty() {
		return stack.size() == 0;
	}
}
