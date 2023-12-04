/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.threadlocal.starter;

/**
 * Starter for the ThreadLocal instance
 * @author Marchino Alessandro
 * @param <W> the localized class
 */
public interface ThreadLocalStarter<W> {
	/**
	 * Initializer for the underlying ThreadLocal
	 * @return the initialized ThreadLocal
	 */
	ThreadLocal<W> initialize();
	
	/**
	 * The ThreadLocal name.
	 * <p>
	 * Should be unique application-wide so as to preclude collisions
	 * @return the name
	 */
	String getName();

}
