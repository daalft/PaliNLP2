package de.unitrier.daalft.pali.phonology.element;
/**
 * Generic table entry class
 * @author David
 *
 * @param <P> first element
 * @param <R> second element
 */
public class TableEntry<P,R> {
	/**
	 * First element
	 */
	private P p;
	/**
	 * Second element
	 */
	private R r;
	/**
	 * Constructor
	 * @param p argument 1
	 * @param r argument 2
	 */
	public TableEntry(P p, R r) {
		this.p = p;
		this.r = r;
	}
	/**
	 * Returns the first element
	 * @return first element
	 */
	public P getFirstElement () {
		return p;
	}
	/**
	 * Returns the second element
	 * @return second element
	 */
	public R getSecondElement () {
		return r;
	}
	/**
	 * Default toString method
	 */
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append(p.toString());
		sb.append(":");
		sb.append(r.toString());
		return sb.toString();
	}
}