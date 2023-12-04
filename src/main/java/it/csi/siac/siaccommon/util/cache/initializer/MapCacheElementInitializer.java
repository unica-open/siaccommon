/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.cache.initializer;

import java.util.HashMap;
import java.util.Map;

import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;

public class MapCacheElementInitializer<K, K2, V> implements CacheElementInitializer<K, Map<K2, V>> {

	@Override
	public Map<K2, V> initialize(K key) {
		return new HashMap<K2, V>();
	}

}
