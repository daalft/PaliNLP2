package de.unitrier.daalft.pali.phonology.element;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a constant definition
 * @author David
 *
 */
public class ConstantDefinition {
	/**
	 * Constant name
	 */
	private String name;
	/**
	 * Contained elements
	 */
	private List<String> elements;
	
	/**
	 * Constructor
	 * @param info information
	 */
	public ConstantDefinition (String info) {
		elements = new ArrayList<String>();
		String[] s = info.split(":");
		this.name = s[0];
		for (String e : s[1].split(",")) {
			elements.add(e);
		}
	}
	
	/**
	 * Returns this constant's name
	 * @return name
	 */
	public String getName () {
		return name;
	}
	
	/**
	 * Returns this constant's elements
	 * @return elements
	 */
	public List<String> getElements () {
		return elements;
	}
	
	/**
	 * Returns all the elements in this definitions,
	 * separated by the pipe character |
	 * @return string of elements
	 */
	public String getElementsAsOrGroup () {
		StringBuilder sb = new StringBuilder();
		for (String s : elements) {
			sb.append(s).append("|");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}