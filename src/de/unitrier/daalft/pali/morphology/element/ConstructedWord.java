package de.unitrier.daalft.pali.morphology.element;
/**
 * Class representing constructed words or words in construction
 * @author David
 *
 */
public class ConstructedWord {

	/**
	 * Fields
	 */
	private String word, stem, lemma;
	/**
	 * Morphological information
	 */
	private FeatureSet featureSet;
	/**
	 * Constructor
	 */
	public ConstructedWord () {
		featureSet = new FeatureSet();
	}
	
	/**
	 * Constructor
	 * @param s word
	 */
	public ConstructedWord (String s) {
		this();
		word = s;
	}
	
	/**
	 * Constructor
	 * @param s word
	 * @param fs feature set
	 */
	public ConstructedWord (String s, FeatureSet fs) {
		word = s;
		featureSet = fs;
	}
	
	/**
	 * Returns the word string
	 * @return word
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Sets the word string
	 * @param s word
	 */
	public void setWord(String s) {
		word = s;
	}
	
	/**
	 * Sets the feature set
	 * @param fs feature set
	 */
	public void setFeatureSet (FeatureSet fs) {
		featureSet = fs;
	}
	
	/**
	 * Returns the feature set
	 * @return feature set
	 */
	public FeatureSet getFeatureSet () {
		return featureSet;
	}
	
	/**
	 * Default toString method
	 */
	public String toString () {
		String l = lemma == null ? "" : "\nLemma:\n" + lemma;
		return "Constructed word: " + word + l + "\nFeatures:\n" + featureSet.toString();
	}
	
	/**
	 * Adds a feature to this feature set
	 * @param f feature
	 */
	public void addFeature (Feature f) {
		featureSet.add(f);
	}
	
	/**
	 * Returns the stem string
	 * @return stem
	 */
	public String getStem () {
		return stem;
	}
	
	/**
	 * Sets the stem string
	 * @param s stem
	 */
	public void setStem (String s) {
		stem = s;
	}

	/**
	 * Sets the lemma string
	 * @param lemma2 lemma
	 */
	public void setLemma(String lemma2) {
		lemma = lemma2;
	}

	/**
	 * Returns the lemma string
	 * @return lemma
	 */
	public String getLemma() {
		return lemma;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((featureSet == null) ? 0 : featureSet.hashCode());
		result = prime * result + ((lemma == null) ? 0 : lemma.hashCode());
		result = prime * result + ((stem == null) ? 0 : stem.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		ConstructedWord other = (ConstructedWord) obj;
		if (featureSet == null) {
			if (other.featureSet != null)
				return false;
		} else if (!featureSet.equals(other.featureSet))
			return false;
		if (lemma == null) {
			if (other.lemma != null)
				return false;
		} else if (!lemma.equals(other.lemma))
			return false;
		if (stem == null) {
			if (other.stem != null)
				return false;
		} else if (!stem.equals(other.stem))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
	
	/**
	 * Returns a deep copy of this object
	 */
	public ConstructedWord clone () {
		ConstructedWord copy = new ConstructedWord();
		copy.stem = this.stem;
		copy.lemma = this.lemma;
		copy.word = this.word;
		FeatureSet fs = new FeatureSet();
		for (Feature f : this.featureSet) {
			fs.add(new Feature(f.getKey(), f.getValue()));
		}
		copy.setFeatureSet(featureSet);
		return copy;
	}
}