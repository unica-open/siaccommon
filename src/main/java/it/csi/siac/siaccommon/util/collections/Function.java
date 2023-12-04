/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections;

/**
 * Function for collecting collection values.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/11/2014
 *
 * @param <S> the source type
 * @param <D> the destination type
 */
public interface Function<S, D> {
	
	/**
	 * Maps the value from the source.
	 * 
	 * @param source the source for the expected value
	 * 
	 * @return the collected value
	 */
	D map(S source);

}
