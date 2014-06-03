package de.unitrier.daalft.pali.morphology.element;

/**
 * Represents a morphological key-value pair
 * @author David
 *
 */
public class Feature {

	/**
	 * Key-value
	 */
	private String key, value;
	
	/**
	 * Constructor
	 */
	public Feature () {
		
	}
	
	/**
	 * Constructor with values
	 * @param key key
	 * @param value value
	 */
	public Feature (String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Returns the key of the key-value pair
	 * @return key
	 */
	public String getKey () {
		return key;
	}
	
	/**
	 * Returns the value of the key-value pair
	 * @return value
	 */
	public String getValue () {
		return value;
	}
	
	/**
	 * Returns a string representation
	 * @return string
	 */
	public String toString () {
		return key + "->" + value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Feature other = (Feature) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public boolean setValue(String featureValue) {
		this.value = featureValue;
		return true;
	}
}
