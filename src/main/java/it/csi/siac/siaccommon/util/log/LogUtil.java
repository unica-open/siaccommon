/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.log;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;

import it.csi.siac.siaccommon.model.UserSessionInfo;

public class LogUtil {

	private static final String PATTERN = "[%s.%s] %s - %s";

	protected final String className;
	protected Logger logger;

	public LogUtil(Class<?> cls) {
		logger = Logger.getLogger(cls);
		className = cls.getSimpleName();
	}
	
	public LogUtil(String category, Class<?> cls) {
		logger = Logger.getLogger(category);
		className = cls.getSimpleName();
	}
	
	public void debugStart(String methodName, Object message) {
		debug(methodName, "Start. " + message);
	}

	public void debugEnd(String methodName, Object message) {
		debug(methodName, "End. " + message);
	}

	public void infoStart(String methodName, Object message) {
		info(methodName, "Start. " + message);
	}
	
	public void infoEnd(String methodName, Object message) {
		info(methodName, "End. " + message);
	}

	public void debugStart(String methodName) {
		debugStart(methodName, "");
	}

	public void debugEnd(String methodName) {
		debugEnd(methodName, "");
	}

	
	public void infoStart(String methodName) {
		infoStart(methodName, "");
	}

	public void infoEnd(String methodName) {
		infoEnd(methodName, "");
	}
	
	public void info(Object message) {
		info("", message);
	}

	public void trace(String methodName, Object message) {
		logger.trace(composeMessage(methodName, message));
	}

	public void debug(String methodName, Object message) {
		logger.debug(composeMessage(methodName, message));
	}

	public void info(String methodName, Object message) {
		logger.info(composeMessage(methodName, message));
	}

	public void warn(String methodName, Object message) {
		logger.warn(composeMessage(methodName, message));
	}

	public void warn(String methodName, Object message, Throwable t) {
		logger.warn(composeMessage(methodName, message), t);
	}

	public void error(String methodName, Object message) {
		logger.error(composeMessage(methodName, message));
	}

	public void error(String methodName, Object message, Throwable t) {
		logger.error(composeMessage(methodName, message), t);
	}
	
	public void fatal(String methodName, Object message) {
		logger.fatal(composeMessage(methodName, message));
	}

	public void fatal(String methodName, Object message, Throwable t) {
		logger.fatal(composeMessage(methodName, message), t);
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger log) {
		this.logger = log;
	}
	
	protected String composeMessage(String methodName, Object message) {
		return String.format(PATTERN, className, methodName, getInternalUserSessionInfo().toString(), message);
	}

	protected UserSessionInfo getInternalUserSessionInfo() {
		return UserSessionInfo.EMPTY;
	}

	/**
	 * Logga un oggetto complesso con annotazione XmlType.
	 * 
	 * @param obj
	 *            oggetto con annotazione XmlType da convertire in xml per il
	 *            logging
	 * @param msg
	 *            nome del parametro da loggare (per il logging)
	 */
	public void logXmlTypeObject(Object obj, String msg) {
		String methodName = "logXmlTypeObject";
		if (isDebugEnabled()) {
			String result;
			try {
				result = toXml(obj);
			}
			catch (RuntimeException e) {
				result = e.getMessage();
				warn(methodName, "Impossibile loggare " + msg, e);
			}

			debug(methodName, msg + ": " + result);
		}
	}

	/**
	 * Trasforma un oggetto con annotazione XmlType in una stringa xml.
	 * 
	 * @param obj
	 * @return
	 * @throws JAXBException
	 * @throws PropertyException
	 */
	private static String toXml(Object obj) {
		if (obj == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JAXB.marshal(obj, baos);
		return baos.toString();

	}

}
