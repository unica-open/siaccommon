/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections;

/**
 * Function for appliying predicated to values.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/11/2014
 *
 * @param <S> the source type
 */
public interface Predicate<S> {
	
	/**
	 * Applies the given predicate to the source object.
	 * 
	 * @param source the source for the expected value
	 */
	void apply(S source);

}
