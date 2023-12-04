/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.core.GenericTypeResolver;

import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.log.LogUtil;

public class CoreUtil {
	private static final LogUtil LOG = new LogUtil(CoreUtil.class);
	private static final Pattern MESSAGE_FORMAT_PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\d+)\\}");

	
	private CoreUtil() {
		// Prevent instantiation
	}

	public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
		Map<V, K> rev = new HashMap<V, K>();

		for (Map.Entry<K, V> e : map.entrySet()) {
			rev.put(e.getValue(), e.getKey());
		}

		return rev;
	}

	public static <T> Map<String, T> getEnumMap(Class<T> c) {
		Map<String, T> reverseMap = new HashMap<String, T>();

		T[] enumConstants = c.getEnumConstants();
		if(enumConstants != null) {
			for (T elem : enumConstants) {
				reverseMap.put(elem.toString(), elem);
			}
		}

		return reverseMap;
	}

	public static <E, O> Map<O, E> getEnumMap(Class<E> enumClass, Function<E, O> func) {
		E[] enumNames = enumClass.getEnumConstants();
		
		if(enumNames == null) {
			return null;
		}
		
		Map<O, E> reverseMap = new HashMap<O, E>();

		for (E elem : enumNames) {
			reverseMap.put(func.map(elem), elem);
		}

		return reverseMap;
	}

	public static void logXmlTypeObject(Object obj, String msg) {
		logXmlTypeObject(LOG, obj, msg);
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
	public static void logXmlTypeObject(LogUtil logger, Object obj, String msg) {
		String methodName = "logXmlTypeObject - ";
		if (logger.isDebugEnabled()) {
			String result;
			try {
				result = toXml(obj);
			} catch (PropertyException e) {
				result = e.getMessage();
				logger.warn("Impossibile loggare " + msg, e);
			} catch (JAXBException e) {
				result = e.getMessage();
				logger.warn("Impossibile loggare " + msg, e);
			}

			logger.debug(methodName, msg + ": " + result);
		}
	}

	/**
	 * Trasforma un oggetto con annotazione XmlType in una stringa xml.
	 * 
	 * @param obj
	 * @return
	 * @throws JAXBException
	 */
	public static String toXml(Object obj) throws JAXBException {
		if (obj == null) {
			return null;
		}
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter sw = new StringWriter();
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JAXBElement jx = new JAXBElement(new QName(obj.getClass().getSimpleName()), obj.getClass(), obj);
		marshaller.marshal(jx, sw);
		return sw.toString();

	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> clazz, String source) throws JAXBException {
		if (source == null) {
			return null;
		}
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		StringReader sr = new StringReader(source);
		return (T) unmarshaller.unmarshal(sr);

	}

	@SuppressWarnings("unchecked")
	public static <T> T dupObject(T obj) {
		try {
			return mergeObjects(obj, (T) obj.getClass().newInstance());
		} catch (Exception e) {
			final String methodName = "dupObject";
			LOG.warn(methodName, e.getMessage(), e);

			return null;
		}
	}

	public static <T> T mergeObjects(T obj1, T obj2) {
		mergeObjects(obj1, obj2, obj1.getClass());

		return obj2;
	}

	private static <T> void mergeObjects(T obj1, T obj2, Class<?> cls) {
		if (cls.getSuperclass() != null)
			mergeObjects(obj1, obj2, cls.getSuperclass());

		for (Field field : cls.getDeclaredFields()) {
			field.setAccessible(true);

			try {
				Object value = field.get(obj1);

				if (value != null && field.get(obj2) == null)
					field.set(obj2, value);
			} catch (IllegalArgumentException e) {
				final String methodName = "mergeObjects";
				LOG.warn(methodName, e.getMessage(), e);
			} catch (IllegalAccessException e) {
				final String methodName = "mergeObjects";
				LOG.warn(methodName, e.getMessage(), e);
			}
		}

	}

	public static String buildKey(String... params) {
		return StringUtils.join(params, "/");
	}

	/**
	 * @deprecated Use {@link ReflectionUtil#silentlyBuildInstance(Class)}
	 */
	@Deprecated
	public static <T> T instantiateNewClass(Class<T> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Impossibile istanziare la classe " + cls.getName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Impossibile istanziare la classe " + cls.getName(), e);
		}
	}
	
	public static String objectToString(Object obj) {
		return objectToString(obj, ToStringStyle.MULTI_LINE_STYLE);
	}

	public static String objectToString(Object obj, ToStringStyle style) {
		return ReflectionToStringBuilder.toString(obj, style);
	}

	
	
	@SuppressWarnings("unchecked")
	public static <T> T instantiateNewGenericType(Class<?> cls, Class<?> genericIfc, int genericTypeArgumentsIndex) {
		try {
			return (T) getGenericTypeClass(cls, genericIfc, genericTypeArgumentsIndex)
					.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Errore instanziamento automatico. "
					+ "Deve esistere un costruttore vuoto. Per esigenze più complesse "
					+ "sovrascrivere il metodo a livello di servizio.", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"Errore instanziamento automatico. Il costruttore vuoto non è accessibile.", e);
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericTypeClass(Class<?> cls, Class<?> genericIfc,
			int genericTypeArgumentsIndex) {
		try {
			@SuppressWarnings("rawtypes")
			Class[] genericTypeArguments = GenericTypeResolver.resolveTypeArguments(cls, genericIfc);
			return genericTypeArguments[genericTypeArgumentsIndex];
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalArgumentException("Errore instanziamento automatico. ", t);
		}
	}
	
	public static <T> T arrayGet(T[] array, int index) {
		return 
			array == null ? null : 
			index < 0 ? null : 
			index < array.length ? array[index] : null;
	}
	
	/**
	 * Metodo di utilita' per il controllo di liste nulle
	 * 
	 * @deprecated @see it.csi.siac.siaccommon.util.collections.CollectionUtil.defaultList(List<E>)
	 * @param <T> Generic
	 * @param list la lista utilizzata per il controllo
	 * @return La lista passata in esame o una nuova lista vuota dello stesso tipo
	 */
	@Deprecated
	public static <T> List<T> checkList(List<T> list){
		return list == null ? new ArrayList<T>() : list;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Object> T checkEntity(T entity, Class<?> clazz) {
		//clazz must not be null
		return entity == null ? (T) instantiateNewClass(clazz) : entity;
	}
	
	/**
	 * SIAC-8506
	 * Metodo di utilita' l'eccedenza o meno di un campo di testo
	 * 
	 * @param <String> str la stringa passata in esame
	 * @param <Integer> maxLenght la lunghezza massima 
	 * @return true se la lunghezza dela stringa supera il valore passato altrimenti false
	 */
	public static boolean isLengthGreatherThan(String str, Integer maxLenght) {
		if(str == null) {
			return false;
		}
		return str.length() > (maxLenght != null ? maxLenght : 0);
	}

	/**
	 * SIAC-8506
	 * Metodo di utilita' l'eccedenza o meno di un campo di testo
	 * 
	 * @param <String> str la stringa passata in esame
	 * @param <Integer> maxLenght la lunghezza massima 
	 * @return true se la lunghezza dela stringa e' inferiore o uguale al valore passato altrimenti false
	 */
	public static boolean isLengthLessThan(String str, Integer maxLenght) {
		return !isLengthGreatherThan(str, maxLenght);
	}

	/**
	 * SIAC-8506
	 * Metodo di utilita' per adattare una stringa ad una determinata lunghezza
	 * 
	 * @param <String> str la stringa passata in esame
	 * @param <Integer> maxLenght la lunghezza massima 
	 * @return la stringa eventualmente adattata
	 */
	public static String truncToMaxLength(String str, Integer maxLength) {
		if(StringUtils.isBlank(str)) return str;
		
		String trimmed = str.trim();
		// se usando un trim risulta minore allora ritorno il valore
		if(isLengthLessThan(trimmed, maxLength)) {
			return trimmed;
		}
		
		// se il numero di caratteri e' comunque superiore adatto al valore massimo
		return str.substring(0, maxLength);
	}

	public static boolean isDevMode( ) {
		return "true".equalsIgnoreCase(System.getProperty("dev.mode"));
	}
	
	public static <T> T getOption(boolean condition, T trueOption, T falseOption) {
		return condition ? trueOption : falseOption;
	}
	
	
	public static String formatStringWithDefaultReplacements(String format, String defaultSubstitution, Object... replacements) {
		return MESSAGE_FORMAT_PLACEHOLDER_PATTERN.matcher(MessageFormat.format(format, replacements)).replaceAll(defaultSubstitution);
	}

	
}
