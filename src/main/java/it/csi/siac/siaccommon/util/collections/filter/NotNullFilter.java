/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections.filter;

import it.csi.siac.siaccommon.util.collections.Filter;

public class NotNullFilter<S> implements Filter<S> {

	public NotNullFilter() {}

	@Override
	public boolean isAcceptable(S source) {
		return source != null;
	}
}