package de.unitrier.daalft.pali.morphology.paradigm;

import java.util.ArrayList;
import java.util.List;

import de.unitrier.daalft.pali.morphology.element.FeatureSet;
import de.unitrier.daalft.pali.morphology.element.Morph;
import de.unitrier.daalft.pali.morphology.element.Morpheme;
/**
 * Class representing paradigms
 * @author David
 *
 */
public class Paradigm {

	/**
	 * Feature map
	 */
	protected List<Morpheme> morphemes;

	/**
	 * No-argument constructor
	 */
	public Paradigm () {
		morphemes = new ArrayList<Morpheme>();
	}

	/**
	 * Standard toString method
	 * @return string representation
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("Paradigm");
		sb.append(System.lineSeparator());
		for (Morpheme m : morphemes) {
			sb.append(m.toString()).append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * Returns the endings in this paradigm
	 * @return endings
	 */
	public List<Morph> getEndings () {
		List<Morph> out = new ArrayList<Morph>();
		for (Morpheme m : morphemes) {
			out.addAll(m.getAllomorphs());
		}
		return out;
	}

	/**
	 * Adds morphological information to the paradigm
	 * <p>
	 * <em>morpheme</em> represents any type of morphological
	 * feature (i.e. nominal ending) and <em>features</em>
	 * represents the associated morphological features
	 * @param m
	 */
	public void add (Morpheme m) {
		if (morphemes.contains(m)) {
	
			morphemes.get(morphemes.indexOf(m)).add(m.getAllomorphs());
		} else {
			morphemes.add(m);
		}
	}

	/**
	 * Returns the complete paradigm
	 * <p>
	 * The complete paradigm consists of the morphemes and
	 * their related morphological information as a list of 
	 * mappings
	 * @return paradigm
	 */
	public List<Morpheme> getMorphemes () {
		return morphemes;
	}

	/**
	 * Returns whether the specified word could belong
	 * to this paradigm, or in other words, whether this
	 * paradigm is applicable to the specified word 
	 * @param word word
	 * @return whether paradigm is applicable
	 */
	public boolean isApplicable (String word) {
		for (Morph end : getEndings()) {
			if (word.endsWith(end.getMorph()))
				return true;
		}
		return false;
	}

	/**
	 * Returns the paradigm that matches the specified 
	 * feature set
	 * <p>
	 * The feature set can cover all features, but 
	 * generally only specifies a few features (i.e. word 
	 * class)
	 * <br/><br/>
	 * <b>If the resulting Paradigm is empty, null is returned!</b>
	 * @param feat feature set
	 * @return paradigm matching feature set
	 */
	public Paradigm getParadigmByFeatures (FeatureSet feat) {
		Paradigm out = new Paradigm();
		for (Morpheme m : morphemes) {
			if (m.getFeatureSet().satisfies(feat)) {
					out.add(m);
			}
		}
		return (out.isEmpty())?null:out;
	}

	public boolean isEmpty () {
		return morphemes.size() == 0;
	}

	/**
	 * Checks whether this Paradigm has the given subtype
	 * <p>
	 * This Paradigm is said to have the given subtype,
	 * <em>if and only if</em> all of the contained morphemes
	 * have the given subtype
	 * @param type subtype to check
	 * @return true if all morphemes in paradigm have given subtype
	 */
	public boolean hasSubtype (String type) {
		for (Morpheme m : morphemes)
			if (!m.hasSubtype(type))
				return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((morphemes == null) ? 0 : morphemes.hashCode());
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
		Paradigm other = (Paradigm) obj;
		if (morphemes == null) {
			if (other.morphemes != null)
				return false;
		} else if (!morphemes.equals(other.morphemes))
			return false;
		return true;
	}

	/**
	 * Calculates the difference between this paradigm
	 * and a given paradigm
	 * <p>
	 * Returns a paradigm that consists of this paradigm
	 * with all elements from the given paradigm removed 
	 * and the given paradigm
	 * @param p2 given paradigm
	 * @return difference
	 */
	public Paradigm difference (Paradigm p2) {
		Paradigm copy = new Paradigm();
		for (Morpheme m : this.morphemes) {
			Morpheme morpheme = new Morpheme();
			for (Morph mo : m.getAllomorphs()) {
				Morph morph = new Morph(mo.getMorph(), mo.getOccurrence());
				morpheme.add(morph);
			}
			morpheme.addFS(m.getFeatureSet());
			copy.add(morpheme);
		}
		copy.morphemes.removeAll(p2.morphemes);
		return copy;
	}

	/**
	 * Returns the feature set associated with the given word
	 * @param word word
	 * @return feature set
	 */
	public FeatureSet getFeatureSet(String word) {
		for (Morpheme m : morphemes) {
			if (m.exactly(word))
				return m.getFeatureSet();
		}
		System.err.println("Could not retrieve feature set for " + word);
		return null;
	}
	
	/**
	 * Changes a feature in this paradigm to the <em>newValue</em>
	 * @param targetFeatureName
	 * @param newValue
	 * @return
	 */
	public Paradigm changeFeature(String targetFeatureName, String newValue) {
		Paradigm out = new Paradigm();
		
		for (Morpheme m : this.morphemes) {
			if (!m.getFeatureSet().getFeature(targetFeatureName).isEmpty()) {
				m.getFeatureSet().setFeature(targetFeatureName, newValue);
			}
			out.add(m);
		}
		
		return out;
	}
}
