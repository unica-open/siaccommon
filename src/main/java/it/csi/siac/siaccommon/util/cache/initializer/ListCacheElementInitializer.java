/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.cache.initializer;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;

public class ListCacheElementInitializer<K, V> implements CacheElementInitializer<K, List<V>> {

	@Override
	public List<V> initialize(K key) {
		return new ArrayList<V>();
	}

}
