package de.unitrier.daalft.pali.morphology.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Represents a set of features
 * @author David
 *
 */
public class FeatureSet implements Iterable<Feature> {

	/**
	 * Features
	 */
	private List<Feature> features;
	
	/**
	 * Constructor
	 */
	public FeatureSet () {
		features = new ArrayList<Feature>();
	}
	
	/**
	 * Constructor with feature
	 * @param f feature
	 */
	public FeatureSet (Feature f) {
		this();
		features.add(f);
	}
	
	/**
	 * Constructor with key-value pair
	 * @param k key
	 * @param v value
	 */
	public FeatureSet (String k, String v) {
		this(new Feature(k,v));
	}
	
	/**
	 * Adds a feature to this feature set
	 * @param f feature
	 * @return true on success
	 */
	public boolean add (Feature f) {
		return features.add(f);
	}

	/**
	 * Returns an iterator
	 */
	@Override
	public Iterator<Feature> iterator() {
		return features.iterator();
	}
	
	/**
	 * Returns a string representation
	 * @return string
	 */
	public String toString () {
		StringBuilder sb = new StringBuilder("{");
		for (Feature f : features) {
			sb.append(f.toString());
			sb.append(";");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Checks whether the specified feature set <em>fs</em>
	 * is a subset of this feature set
	 * @param fs specified feature set
	 * @return true if fs is a subset of this feature set
	 */
	public boolean satisfies (FeatureSet fs) {
		for (Feature f : fs) {
			if (!features.contains(f)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((features == null) ? 0 : features.hashCode());
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
		FeatureSet other = (FeatureSet) obj;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		return true;
	}

	public boolean contains(Feature f) {
		for (Feature fe : features) {
			if (fe.getKey().equals(f.getKey()) &&
					fe.getValue().equals(f.getValue()))
				return true;
		}
		return false;
	}

	public String getFeature(String name) {
		for (Feature f : features) {
			if (f.getKey().equals(name)) {
				return f.getValue();
			}
		}
		return "";
	}
}
