package de.unitrier.daalft.pali.morphology.element;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an abstract morpheme
 * @author David
 *
 */
public class Morpheme {

	/**
	 * Morphological features
	 */
	private FeatureSet featureSet;
	/**
	 * Concrete morph realizations
	 */
	private List<Morph> allomorphs;
	
	/**
	 * Constructor
	 */
	public Morpheme() {
		featureSet = new FeatureSet();
		allomorphs = new ArrayList<Morph>();
	}
	
	/**
	 * Constructor
	 * @param fs feature set
	 */
	public Morpheme (FeatureSet fs) {
		this();
		this.featureSet = fs;
	}
	
	/**
	 * Constructor
	 * @param fs feature set
	 * @param am allomorphs
	 */
	public Morpheme (FeatureSet fs, List<Morph> am) {
		this(fs);
		this.allomorphs = am;
	}
	
	/**
	 * Constructor
	 * @param fs feature set
	 * @param m morph
	 */
	public Morpheme (FeatureSet fs, Morph m) {
		this(fs);
		allomorphs.add(m);
	}
	
	public Morpheme (String m) {
		this();
		allomorphs.add(new Morph(m));
	}
	
	/**
	 * Returns the feature set
	 * @return feature set
	 */
	public FeatureSet getFeatureSet () {
		return featureSet;
	}
	
	/**
	 * Returns the allomorphs
	 * <p>
	 * Allomorphs are equivalent <b>concrete</b> realizations
	 * of this <em>abstract</em> morpheme
	 * @return allomorphs
	 */
	public List<Morph> getAllomorphs () {
		return allomorphs;
	}
	
	/**
	 * Adds a morph to this morpheme
	 * @param m morph
	 * @return true on success
	 */
	public boolean add (Morph m) {
		return allomorphs.add(m);
	}
	
	/**
	 * Adds a feature set to this morpheme
	 * @param fs feature set
	 */
	public void addFS (FeatureSet fs) {
		featureSet = fs;
	}
	
	/**
	 * Default toString method
	 */
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append(featureSet.toString());
		sb.append("[");
		for (Morph m : allomorphs) {
			sb.append(m.toString()).append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Adds a list of morphs to this morpheme
	 * @param allomorphs list of morphs
	 */
	public void add(List<Morph> allomorphs) {
		this.allomorphs.addAll(allomorphs);
	}

	/**
	 * Checks whether this morpheme has a feature <em>subtype</em>
	 * with the given subtype
	 * @param type subtype
	 * @return true if feature set contains feature 
	 */
	public boolean hasSubtype (String type) {
		return featureSet.satisfies(new FeatureSet("subtype", type));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((featureSet == null) ? 0 : featureSet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Morpheme other = (Morpheme) obj;
		if (featureSet == null) {
			if (other.featureSet != null)
				return false;
		} else if (!featureSet.equals(other.featureSet))
			return false;
		return true;
	}

	/**
	 * Checks whether the given word can be the result
	 * of appending this morpheme to a stem
	 * @param word word to check
	 * @return true if word ends with morpheme
	 */
	public boolean isApplicable(String word) {
		for (Morph m : allomorphs) {
			if (word.endsWith(m.getMorph()))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns a feature value from this feature set by name
	 * @param name name
	 * @return feature value
	 */
	public String getFeatureByName (String name) {
		return featureSet.getFeature(name);
	}

	/**
	 * Returns the morph that the given word ends with
	 * @param word word
	 * @return morph
	 */
	public String match(String word) {
		for (Morph m : allomorphs) {
			if (word.endsWith(m.getMorph())) 
				return m.getMorph();
		}
		return "";
	}

	/**
	 * Checks whether an allomorph from this morph set
	 * matches the given word exactly
	 * @param word word to check
	 * @return true if match
	 */
	public boolean exactly(String word) {
		for (Morph m : allomorphs) {
			if (m.getMorph().equals(word))
				return true;
		}
		return false;
	}
}
