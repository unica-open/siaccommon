/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections;

/**
 * Function for reducing values.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/08/2015
 *
 * @param <S> the source type
 * @param <R> the reduced type
 */
public interface Reductor<S, R> {
	
	/**
	 * Reduces the current value of the collection to an accumulator.
	 * 
	 * @param accumulator  the accumulator up to the previous step
	 * @param currentValue the current value
	 * @param index        the step index
	 * 
	 * @return the reduced value
	 */
	R reduce(R accumulator, S currentValue, int index);

}
