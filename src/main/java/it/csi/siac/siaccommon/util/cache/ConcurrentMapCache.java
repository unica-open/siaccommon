/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.cache;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cache backed by a concurrent map
 * @author Marchino Alessandro
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ConcurrentMapCache<K, V> implements Cache<K, V> {

	/** The underlying map */
	private final ConcurrentMap<K, V> map = new ConcurrentHashMap<K, V>();
	/** The lock to use (as of Java6) */
	private final Lock lock = new ReentrantLock();

	@Override
	public V get(K key) {
		return map.get(key);
	}

	@Override
	public V get(K key, CacheElementInitializer<K, V> initializer) {
		if(initializer == null) {
			throw new NullPointerException("Null initializer");
		}
		if(!map.containsKey(key)) {
			try {
				lock.lock();
				if(!map.containsKey(key)) {
					map.put(key, initializer.initialize(key));
				}
			} finally {
				lock.unlock();
			}
		}
		return map.get(key);
	}

	@Override
	public V put(K key, V value) {
		return map.put(key, value);
	}

	@Override
	public V remove(K key) {
		return map.remove(key);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

}
