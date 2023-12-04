/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections;

/**
 * Function for filtering values.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/11/2014
 *
 * @param <S> the source type
 */
public interface Filter<S> {
	
	/**
	 * Checks whether the given source value is acceptable.
	 * 
	 * @param source the source for the expected value
	 * 
	 * @return <code>true</code> if the value is acceptable; <code>false</code> otherwise
	 */
	boolean isAcceptable(S source);

}
