/*
 * MIT license
 */
package de.unitrier.daalft.pali.lexicon;


import java.util.HashMap;


/**
 * This class implements a cache.
 *
 * @author knauth
 */
public class Cache<T>
{

	////////////////////////////////////////////////////////////////
	// Nested Classes
	////////////////////////////////////////////////////////////////

	private class CacheRecord<T>
	{

		public CacheRecord predecessor;
		public CacheRecord successor;
		public long timeStampLastAccess;
		public final String key;
		public T value;

		/**
		 * Constructor.
		 */
		public CacheRecord(String key, T value, long timeStampLastAccess)
		{
			this.key = key;
			this.value = value;
			this.timeStampLastAccess = timeStampLastAccess;
		}

	}

	////////////////////////////////////////////////////////////////
	// Constants
	////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////
	// Variables
	////////////////////////////////////////////////////////////////

	private CacheRecord<T> beforeFirst;
	private CacheRecord<T> afterLast;
	private int count;

	private HashMap<String, CacheRecord<T>> map;

	private int maxCacheSize;
	private long maxCacheDuration;

	////////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////////

	/**
	 * Constructor.
	 *
	 * @param	maxCacheSize				The maximum number of elements in the cache.
	 * @param	maxCacheDurationSeconds		The maximum number of seconds an element may be cached.
	 */
	public Cache(int maxCacheSize, int maxCacheDurationSeconds)
	{
		this.maxCacheSize = maxCacheSize;
		if (maxCacheDurationSeconds <= 0) {
			this.maxCacheDuration = -1;
		} else {
			this.maxCacheDuration = maxCacheDurationSeconds * 1000L;
		}

		beforeFirst = new CacheRecord<>(null, null, Long.MAX_VALUE);
		afterLast = new CacheRecord<>(null, null, Long.MAX_VALUE);

		afterLast.predecessor = beforeFirst;
		beforeFirst.successor = afterLast;

		map = new HashMap<>();
	}

	////////////////////////////////////////////////////////////////
	// Methods
	////////////////////////////////////////////////////////////////

	/**
	 * Get the number of elements currently stored in the cache.
	 */
	public int size()
	{
		return count;
	}

	/**
	 * Explicite cache cleaning. Typically you do not need to call this method directly.
	 */
	public void cleanCache()
	{
		synchronized (this) {
			__cleanCache();
		}
	}

	private void __cleanCache()
	{
		if (maxCacheSize > 0) {
			while (count > maxCacheSize) {
				map.remove(afterLast.predecessor.key);
				__remove(afterLast.predecessor);
			}
		}
		if (maxCacheDuration > 0) {
			long timeLimit = System.currentTimeMillis() - maxCacheDuration;
			while (afterLast.predecessor.timeStampLastAccess < timeLimit) {
				map.remove(afterLast.predecessor.key);
				__remove(afterLast.predecessor);
			}
		}
	}

	/**
	 * Lookup a cache element. <code>null</code> is returned if the element is not cached.
	 *
	 * @param		key			The key to look for
	 * @param		bTouch		Specify <code>true</code> here, if you want to touch the element and therefor
	 *							to raise it within the cache. (The elements at the bottom of the cache will be
	 *							removed at some other time.)
	 */
	public T get(String key, boolean bTouch)
	{
		CacheRecord<T> n;
		if (bTouch) {
			synchronized (this) {
				n = map.get(key);
				if (n == null) return null;
				n.timeStampLastAccess = System.currentTimeMillis();
				__remove(n);
				__insertAfter(beforeFirst, n);
			}
			return n.value;
		} else {
			synchronized (this) {
				n = map.get(key);
			}
			if (n == null) return null;
			return n.value;
		}
	}

	/**
	 * Explicitely remove an element from the cache.
	 *
	 * @param		key		The key to look for
	 * @return		Returns <code>true</code> if the element has been cached, <code>false</code> otherwise.
	 */
	public boolean remove(String key)
	{
		CacheRecord<T> n;
		synchronized (this) {
			n = map.get(key);
			if (n == null) return false;
			map.remove(key);
			__remove(n);
		}
		return true;
	}

	/**
	 * Put an element into the cache. If the cach contains such an element already, the existing one is removed
	 * from the cache.
	 *
	 * @param		key		The key
	 * @param		value		The value
	 * @return		Returns <code>true</code> if the element has been cached, <code>false</code> otherwise.
	 */
	public void put(String key, T value)
	{
		CacheRecord<T> nNew = new CacheRecord<>(key, value, System.currentTimeMillis());
		synchronized (this) {
			CacheRecord<T> nOld = map.get(key);
			if (nOld != null) {
				__remove(nOld);
				map.remove(key);
			}
			__insertAfter(beforeFirst, nNew);
			map.put(key, nNew);
			__cleanCache();
		}
	}

	private void __insertAfter(CacheRecord<T> a, CacheRecord<T> n)
	{
		CacheRecord<T> b = a.successor;

		n.predecessor = a;
		a.successor = n;

		n.successor = b;
		b.predecessor = n;

		count++;
	}

	private void __insertBetween(CacheRecord<T> a, CacheRecord<T> n, CacheRecord<T> b)
	{
		n.predecessor = a;
		a.successor = n;

		n.successor = b;
		b.predecessor = n;

		count++;
	}

	private void __remove(CacheRecord<T> n)
	{
		CacheRecord<T> a = n.predecessor;
		CacheRecord<T> b = n.successor;

		a.successor = b;
		b.predecessor = a;

		n.predecessor = null;
		n.successor = null;

		count--;
	}

}
