/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.threadlocal.starter;

/**
 * Reflective implementation of the ThreadLocalStarter.
 * <p>
 * The underlying class MUST have a no-arguments constructor
 * @author Marchino Alessandro
 * @param <W> the underlying localized class
 */
public class ThreadLocalStarterReflectiveImpl<A, B extends ThreadLocal<A>> implements ThreadLocalStarter<A> {
	
	/** The underlying class */
	private final Class<B> underlyingClass;
	
	/**
	 * Public constructor. Accepts the underlying class to be reflectively instantiated and introspected
	 * @param underlyingClass the underlying class to be started. MUST be not-null and MUST have a 
	 * no-argument constructor
	 */
	public ThreadLocalStarterReflectiveImpl (Class<B> underlyingClass) {
		if(underlyingClass == null) {
			throw new IllegalArgumentException("The ThreadLocal class to be instantiated by the reflective starter");
		}
		this.underlyingClass = underlyingClass;
	}
	
	@Override
	public ThreadLocal<A> initialize() {
		try {
			return underlyingClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Cannot instantiate " + this.underlyingClass.getName());
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Cannot access " + this.underlyingClass.getName());
		}
	}
	
	@Override
	public String getName() {
		return this.underlyingClass.getName();
	}

}
