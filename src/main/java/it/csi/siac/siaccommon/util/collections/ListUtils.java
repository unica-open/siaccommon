/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections;

import java.util.List;

public class ListUtils {

	public static <T> T get(List<T> c, int i) {
		if (c == null || c.isEmpty()) {
			return null;
		}
		
		return c.get(i);
	}

	public static <T> T getFirst(List<T> c) {
		return get(c, 0);
	}
}
