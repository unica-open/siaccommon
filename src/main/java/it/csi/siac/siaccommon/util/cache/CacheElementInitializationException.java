/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Exception in cached element initialization
 * @author Marchino Alessandro
 */
public class CacheElementInitializationException extends RuntimeException {

	/** For serialization purpose */
	private static final long serialVersionUID = 5133871139267484485L;
	private final List<String> errors;

	/**
	 * Full constructor
	 * @param error the error
	 * @param cause the cause
	 */
	public CacheElementInitializationException(String error, Throwable cause) {
		super(error, cause);
		this.errors = Arrays.asList(error);
	}

	/**
	 * Constructor specifying the error message
	 * @param error the error
	 */
	public CacheElementInitializationException(String error) {
		super(error);
		this.errors = Arrays.asList(error);
	}

	/**
	 * Constructor specifying the underlying cause
	 * @param cause the cause
	 */
	public CacheElementInitializationException(Throwable cause) {
		super(cause);
		this.errors = Arrays.asList(cause.getMessage());
	}
	
	/**
	 * Constructor specifying the underlying errors
	 * @param errors the errors
	 */
	public CacheElementInitializationException(Collection<String> errors) {
		super();
		this.errors = new ArrayList<String>(errors);
	}
	
	/**
	 * Constructor specifying the underlying cause and errors
	 * @param errors the errors
	 * @param cause the cause
	 */
	public CacheElementInitializationException(Collection<String> errors, Throwable cause) {
		super(cause);
		this.errors = new ArrayList<String>(errors);
	}
	
	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return new ArrayList<String>(this.errors);
	}

}
