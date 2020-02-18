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

/**
 * 
 * Utility centralizzata per il logging.
 * 
 *
 */
public class LogUtil {

	private static final String PATTERN = "[%s.%s] - %s";
	private static final String START_PATTERN = "[%s.%s] - Start. %s";
	private static final String END_PATTERN = "[%s.%s] - End. %s";

	private final String className;
	private Logger logger;

	public LogUtil(Class<?> clazz) {
		logger = Logger.getLogger(clazz);
		className = clazz.getSimpleName();
	}

	public void debugStart(String methodName, Object message) {
		logger.debug(String.format(START_PATTERN, className, methodName, message));
	}

	public void debugEnd(String methodName, Object message) {
		logger.debug(String.format(END_PATTERN, className, methodName, message));
	}

	public void infoStart(String methodName, Object message) {
		logger.info(String.format(START_PATTERN, className, methodName, message));
	}

	public void infoStart(String methodName) {
		infoStart(methodName, "");
	}

	public void infoEnd(String methodName) {
		infoEnd(methodName, "");
	}

	public void infoEnd(String methodName, Object message) {
		logger.info(String.format(END_PATTERN, className, methodName, message));
	}

	public void trace(String methodName, Object message) {
		logger.trace(String.format(PATTERN, className, methodName, message));
	}

	public void debug(String methodName, Object message) {
		logger.debug(String.format(PATTERN, className, methodName, message));
	}

	public void info(String methodName, Object message) {
		logger.info(String.format(PATTERN, className, methodName, message));
	}

	public void warn(String methodName, Object message) {
		logger.warn(String.format(PATTERN, className, methodName, message));
	}

	public void warn(String methodName, Object message, Throwable t) {
		logger.warn(String.format(PATTERN, className, methodName, message), t);
	}

	public void error(String methodName, Object message) {
		logger.error(String.format(PATTERN, className, methodName, message));
	}

	public void error(String methodName, Object message, Throwable t) {
		logger.error(String.format(PATTERN, className, methodName, message), t);
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
