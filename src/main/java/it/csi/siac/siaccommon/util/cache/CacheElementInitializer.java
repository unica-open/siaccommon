/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.cache;

/**
 * Initializer for the cache element
 * @author Marchino Alessandro
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface CacheElementInitializer<K, V> {

	/**
	 * Initializes the cached value given the key to which it is to be registered
	 * @param key the key of the element
	 * @return the initial value
	 * @throws CacheElementInitializationException in case the initialization was unsuccessful
	 */
	V initialize(K key);

}
