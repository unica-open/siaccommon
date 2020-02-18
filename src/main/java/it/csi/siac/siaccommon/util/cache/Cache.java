/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.cache;

import java.util.Map.Entry;
import java.util.Set;

/**
 * Cache for the various operations. Allows the initialization of the cache content
 * @author Marchino Alessandro
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface Cache<K, V> {

	/**
	 * Obtains the value corresponding to the given key, if present; <code>null</code> otherwise
	 * @param key the key by which to obtain the value
	 * @return the value corresponding to the key
	 */
	V get(K key);
	/**
	 * Obtains the value corresponding to the given key, if present; initializes the cache value with the initializer result otherwise
	 * @param key the key by which to obtain or initialize the value
	 * @param initializer the value initializer
	 * @return the value corresponding to the key
	 * @throws CacheElementInitializationException in case the initialization was unsuccessful
	 */
	V get(K key, CacheElementInitializer<K, V> initializer);
	/**
	 * Sets the value in the cache, corresponding to the given key
	 * @param key the key to set
	 * @param value the value to set for the key
	 * @return the previous value corresponding to the key, if present
	 */
	V put(K key, V value);
	/**
	 * Removes a given value from the cache
	 * @param key the key to remove
	 * @return the mremoved value
	 */
	V remove(K key);
	/**
	 * Obtains the entries for the cache so as to permit iteration over the values
	 * @return the set of the entries
	 */
	Set<Entry<K, V>> entrySet();
	/**
	 * Obtains the cache size
	 * @return the size
	 */
	int size();
	/**
     * Returns <tt>true</tt> if this map contains a mapping for the specified key.
     * More formally, returns <tt>true</tt> if and only if this map contains a mapping for a key <tt>k</tt> such that <tt>(key==null ? k==null : key.equals(k))</tt>.
     * (There can be at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified key
     */
    boolean containsKey(K key);

}
