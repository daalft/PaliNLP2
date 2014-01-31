package de.unitrier.daalft.pali.phonology.element;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a dictionary
 * @author David
 *
 */
public class MyDictionary {
	/**
	 * Constant definitions
	 */
	private List<ConstantDefinition> cd;
	/**
	 * Method definitions
	 */
	private List<MethodDefinition> md;
	/**
	 * Constructor
	 */
	public MyDictionary () {
		cd = new ArrayList<ConstantDefinition>();
		md = new ArrayList<MethodDefinition>();
	}
	/**
	 * Adds a constant definition to this dictionary
	 * @param cd constant definition
	 */
	public void add (ConstantDefinition cd) {
		this.cd.add(cd);
	}
	/**
	 * Adds a method definition to this dictionary
	 * @param md method definition
	 */
	public void add (MethodDefinition md) {
		this.md.add(md);
	}
	/**
	 * Returns all constant definitions
	 * @return constant definitions
	 */
	public List<ConstantDefinition> getConstantDefinitions () {
		return cd;
	}
	/**
	 * Returns all method definitions
	 * @return method definitions
	 */
	public List<MethodDefinition> getMethodDefinitions () {
		return md;
	}
	/**
	 * Looks up a constant by name and returns the attached
	 * definition on success
	 * <p>
	 * Returns the empty string on failure
	 * @param name constant name
	 * @return constant definition
	 */
	public String lookupConstant (String name) {
		for (ConstantDefinition c : cd) {
			if (c.getName().equals(name)) {
				return c.getElementsAsOrGroup();
			}
		}
		return "";
	}
	/**
	 * Looks up a function by name and returns the result
	 * of applying the function definition to the given
	 * argument on success
	 * <p>
	 * Returns the empty string on failure
	 * @param name function name
	 * @param arg function argument
	 * @return applied function definition
	 */
	public String lookupMethod (String name, String arg) {
		for (MethodDefinition m : md) {
			if (m.getName().equals(name)) {
				return m.getResult(arg);
			}
		}
		return "";
	}
	/**
	 * Looks up a constant by its definition and 
	 * returns the constant's name on success
	 * <p>
	 * Returns the empty string on failure
	 * @param s constant definition
	 * @return constant name
	 */
	public String reverseLookupConstant (String s) {
		for (ConstantDefinition c : cd) {
			if (c.getElementsAsOrGroup().equals(s))
				return c.getName();
		}
		return "";
	}	
}
