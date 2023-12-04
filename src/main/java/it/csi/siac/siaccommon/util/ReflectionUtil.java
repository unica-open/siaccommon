/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Classe di utilit&agrave; per la Reflection.
 * 
 * @author Alessandro Marchino
 * @version 1.0.1
 *
 */
public final class ReflectionUtil {
	
	/** Per la conversione della classe padre nelle sottoclassi */
	private static BeanUtilsBean beanUtilsBean = BeanUtilsBean.getInstance();
	
	private static final LogUtil LOG = new LogUtil(ReflectionUtil.class);
	
	/** Inizializza in modo statico l'istanza del BeanUtilsBean per la conversione. */
	static {
		beanUtilsBean.getConvertUtils().register(false, true, 0);
	}
	
	/** Non instanziare la classe */
	private ReflectionUtil() {
	}
	
	/**
	 * Invoca un metodo nullario.
	 * @param <T> la tipizzazione del return type del metodo
	 * @param obj        l'oggetto su cui invocare il metodo
	 * @param methodName il nome del metodo
	 * @return il valore ritornato dal metodo
	 * @throws IllegalArgumentException nel caso in cui non sia possibile effettuare l'invocazione
	 */
	public static <T> T invokeNullaryMethod(Object obj, String methodName) {
		Method m = silentlyFindMethod(obj, methodName);
		return silentlyInvokeMethod(m, obj);
	}
	
	/**
	 * Invoca il setter.
	 * @param <T> la tipizzazione dell'oggetto da impostare nel setter
	 * @param obj       l'oggetto su cui invocare il setter
	 * @param setMethod il nome del setter
	 * @param setClazz  la classe del setter
	 * @param setObj    l'oggetto su cui invocare il setter
	 * @throws IllegalArgumentException nel caso in cui non sia possibile effettuare l'invocazione
	 */
	public static <T> void invokeSetterMethod(Object obj, String setMethod, Class<T> setClazz, T setObj) {
		Method m = silentlyFindMethod(obj, setMethod, setClazz);
		silentlyInvokeMethod(m, obj, setObj);
	}
	
	public static <T> void invokeSetterMethodWithFieldName(Object obj, String fieldName, Class<T> setClazz, T setObj) {
		invokeSetterMethod(obj, buildSetterName(fieldName), setClazz, setObj);
	}
	
	/**
	 * Imposta un campo all'interno dell'oggetto.
	 * 
	 * @param obj		 l'oggetto in cui injettare il campo
	 * @param fieldName	 il nome del campo
	 * @param fieldValue il valore da injettare
	 */
	public static void setField(Object obj, String fieldName, Object fieldValue) {
		try {
			String setterName = buildSetterName(fieldName);
			Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
			Method setter = ReflectionUtils.findMethod(obj.getClass(), setterName, field.getType());
			ReflectionUtils.invokeMethod(setter, obj, fieldValue);
		} catch(RuntimeException e) {
			throw new UnsupportedOperationException("Il metodo non e' applicabile", e);
		}
	}

	public static String buildSetterName(String fieldName) {
		return "set" + StringUtils.capitalize(fieldName);
	}
	
	/**
	 * Imposta un campo all'interno dell'oggetto.
	 * 
	 * @param obj		 l'oggetto in cui injettare il campo
	 * @param field		 il campo
	 * @param fieldValue il valore da injettare
	 */
	public static void setField(Object obj, Field field, Object fieldValue) {
		setField(obj, field.getName(), fieldValue);
	}
	
	/**
	 * Legge un campo dall'oggetto.
	 * 
	 * @param obj		l'oggetto da cui leggere il campo
	 * @param fieldName il nome del campo
	 * 
	 * @return il valore del campo, se presente; <code>null</code> altrimenti
	 */
	public static Object getField(Object obj, String fieldName) {
		return getField(obj, fieldName, Object.class);
	}
	
	/**
	 * Legge un campo dall'oggetto.
	 * 
	 * @param obj	l'oggetto da cui leggere il campo
	 * @param field il campo
	 * 
	 * @return il valore del campo, se presente; <code>null</code> altrimenti
	 */
	public static Object getField(Object obj, Field field) {
		return getField(obj, field, field.getType());
	}
	
	/**
	 * Legge un campo dall'oggetto.
	 * 
	 * @param obj		l'oggetto da cui leggere il campo
	 * @param fieldName il nome del campo
	 * @param clazz		la classe dell'oggetto di ritorno
	 * 
	 * @param <T> la tipizzazione
	 * 
	 * @return il valore del campo, se presente; <code>null</code> altrimenti
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getField(Object obj, String fieldName, Class<T> clazz) {
		try {
			String getterName = "get" + StringUtils.capitalize(fieldName);
			Method getter = ReflectionUtils.findMethod(obj.getClass(), getterName);
			return (T) ReflectionUtils.invokeMethod(getter, obj);
		} catch(Exception e) {
			throw new UnsupportedOperationException("Il metodo non e' applicabile", e);
		}
	}
	
	/**
	 * Legge un campo dall'oggetto.
	 * 
	 * @param obj	l'oggetto da cui leggere il campo
	 * @param field il campo
	 * @param clazz	la classe dell'oggetto di ritorno
	 * 
	 * @param <T> la tipizzazione
	 * 
	 * @return il valore del campo, se presente; <code>null</code> altrimenti
	 */
	public static <T> T getField(Object obj, Field field, Class<T> clazz) {
		return getField(obj, field.getName(), clazz);
	}
	
	/**
	 * Legge un campo dall'oggetto.
	 * 
	 * @param obj		l'oggetto da cui leggere il campo
	 * @param fieldName il nome del campo
	 * 
	 * @return il valore del campo, se presente; <code>null</code> altrimenti
	 */
	public static Boolean getBooleanField(Object obj, String fieldName) {
		try {
			String getterName = "get" + StringUtils.capitalize(fieldName);
			Method getter = ReflectionUtils.findMethod(obj.getClass(), getterName);
			if(getter == null) {
				// Essendo un boolean potrei avere isMetodo...
				getterName = "is" + StringUtils.capitalize(fieldName);
				getter = ReflectionUtils.findMethod(obj.getClass(), getterName);
			}
			return (Boolean) ReflectionUtils.invokeMethod(getter, obj);
		} catch(Exception e) {
			throw new UnsupportedOperationException("Il metodo non e' applicabile", e);
		}
	}
	
	/**
	 * Copia un campo da un oggetto a un altro.
	 * 
	 * @param source	  l'oggetto di partenza
	 * @param destination l'oggetto di destinazione
	 * @param fieldName	  il nome del campo
	 */
	public static void copyField(Object source, Object destination, String fieldName) {		
		try {
			Object argument = getField(source, fieldName);
			if(argument != null) {
				setField(destination, fieldName, argument);
			}
		} catch (Exception e) {
			throw new UnsupportedOperationException("Il metodo non e' applicabile", e);
		}
	}
	
	/**
	 * Effettua una clonazione forte dell'oggetto.
	 * @param <T> la tipizzazione dell'oggetto da clonare
	 * @param source l'oggetto di partenza
	 * @return una copia dell'oggetto di partenza
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deepClone(T source) {
		// Caso limite
		if(source == null) {
			return null;
		}
		if(source instanceof Serializable) {
			return (T) SerializationUtils.clone((Serializable)source);
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLEncoder out = new XMLEncoder(bos);
		out.writeObject(source);
		out.close();
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		XMLDecoder in = new XMLDecoder(bis);
		
		T result = (T) in.readObject();
		in.close();
		return result;
	}
	
	/**
	 * Controlla se un dato oggetto ha un determinato metodo.
	 * 
	 * @param source     l'oggetto sorgente
	 * @param methodName il nome del metodo da cercare
	 * @param parameters i parametri del metodo
	 * 
	 * @return <code>true</code> se l'oggetto possiede il metodo; <code>false</code> in caso contrario
	 */
	public static boolean hasMethod(Object source, String methodName, Class<?>... parameters) {
		boolean existantMethod = false;
		try {
			source.getClass().getMethod(methodName, parameters);
			existantMethod = true;
		} catch (Exception e) {
			LOG.error("hasMethod", "Errore nella ricerca del metodo", e);
		}
		
		return existantMethod;
	}
	
	/**
	 * Aggiunge un valore intero ad un altro Integer.
	 * 
	 * @param source    l'oggetto sorgente
	 * @param fieldName il nome del campo
	 * @param augend    l'addendo
	 * 
	 * @return il valore aumentato del campo
	 */
	public static Integer addToInteger(Object source, String fieldName, Integer augend) {
		Integer result = null;
		try {
			Integer oldInt = (Integer) getField(source, fieldName);
			result = oldInt.intValue() + augend.intValue();
			setField(source, fieldName, result);
		} catch(Exception e) {
			LOG.error("addToInteger", "Errore nell'addizione", e);
		}
		return result;
	}
	
	/**
	 * Aggiunge un valore intero ad un altro Integer.
	 * 
	 * @param source    l'oggetto sorgente
	 * @param fieldName il nome del campo
	 * @param augend    l'addendo
	 * 
	 * @return il valore aumentato del campo
	 */
	public static Long addToLong(Object source, String fieldName, Long augend) {
		Long result = null;
		try {
			Long oldLong = (Long) getField(source, fieldName);
			result = oldLong.longValue() + augend.longValue();
			setField(source, fieldName, result);
		} catch(Exception e) {
			LOG.error("addToInteger", "Errore nell'addizione", e);
		}
		return result;
	}
	
	/**
	 * Aggiunge un valore BigDecimal ad un altro BigDecimal.
	 * 
	 * @param source    l'oggetto sorgente
	 * @param fieldName il nome del campo
	 * @param augend    l'addendo
	 * 
	 * @return il valore aumentato del campo
	 */
	public static BigDecimal addToBigDecimal(Object source, String fieldName, BigDecimal augend) {
		BigDecimal result = null;
		try {
			BigDecimal oldBD = (BigDecimal) getField(source, fieldName);
			result = oldBD.add(augend);
			setField(source, fieldName, result);
		} catch(Exception e) {
			LOG.error("addToBigDecimal", "Errore nell'addizione", e);
		}
		return result;
	}
	
	/**
	 * Aggiunge un valore ad una lista.
	 * @param <T> la tipizzazione dell'elemento da impostare nella lista
	 * @param source    l'oggetto sorgente
	 * @param fieldName il nome del campo
	 * @param augend    l'addendo
	 */
	@SuppressWarnings("unchecked")
	public static <T> void addToList(Object source, String fieldName, T augend) {
		try {
			List<T> list = (List<T>) getField(source, fieldName);
			list.add(augend);
		} catch(Exception e) {
			LOG.error("addToList", "Errore nell'addizione alla lista", e);
		}
	}
	
	/**
	 * Effettua un downcast di un oggetto verso una sottoclasse.
	 * @param <SUP> la tipizzazione della superclasse
	 * @param <SUB> la tipizzazione della sottoclasse
	 * 
	 * @param source l'oggetto sorgente
	 * @param dest   l'oggetto destinazione
	 */
	public static <SUP, SUB extends SUP> void downcastByReflection(SUP source, SUB dest) {
		for(Method m : source.getClass().getMethods()) {
			if(m.getName().startsWith("get")) {
				tryToInvokeGetterIfSetterExists(source, dest, m);
			}
		}
	}
	
	/**
	 * Prova ad invocare il getter di un metodo nel caso in cui esista anche il setter.
	 * 
	 * @param source l'oggetto sorgente
	 * @param dest   l'oggetto destinazione
	 * @param getter il metodo da invocare
	 */
	@SuppressWarnings("rawtypes")
	private static <SUP, SUB extends SUP> void tryToInvokeGetterIfSetterExists(SUP source, SUB dest, Method getter) {
		final String methodName = "tryToInvokeGetterIfSetterExists";
		String getterName = getter.getName();
		String setterName = getterName.replaceFirst("^(get)", "set");
		Class<?> getterClass = getter.getReturnType();
		
		Method setter = null;
		try {
			setter = silentlyFindMethod(dest, setterName, getterClass);
		} catch(IllegalArgumentException iae) {
			LOG.debug(methodName, "Errore nell'ottenimento del setter per la classe " + dest.getClass().getName() + ". Ignoro il getter " + getterName +
					": " + iae.getMessage());
			// Non ho un setter: ignoro la property
			return;
		}
		
		Object getterReturnObject = silentlyInvokeMethod(getter, source);
		if(getterReturnObject == null || (getterReturnObject instanceof Collection && ((Collection)getterReturnObject).isEmpty())) {
			// Non ho l'oggetto: ignoro la property
			return;
		}
		
		silentlyInvokeMethod(setter, dest, getterReturnObject);
	}
	

	/**
	 * Costruisce silenziosamente un'istanza della classe fornita in input.
	 * @param <A> la tipizzazione dell'istanza da creare
	 * @param clazz la classe di cui ottenere un'istanza
	 * @return un'istanza della classe fornita
	 * @throws IllegalArgumentException nel caso in cui l'instanziazione non vada a buon fine
	 * @throws NullPointerException     nel caso in cui il parametro clazz sia <code>null</code>
	 */
	public static <A> A silentlyBuildInstance(Class<A> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("InstantiationException during instance building", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("IllegalAccessException during instance building", e);
		} catch(ExceptionInInitializerError e) {
			throw new IllegalArgumentException("ExceptionInInitializerError during instance building", e);
		} catch(SecurityException e) {
			throw new IllegalArgumentException("SecurityException during instance building", e);
		}
	}
	
	/**
	 * Ottiene silenziosamente il metodo richiesto dalla classe dell'oggetto fornito in input.
	 * 
	 * @param source     l'oggetto sorgente
	 * @param methodName il nome del metodo cercato
	 * @param classes    le classi componenti la firma del metodo
	 * 
	 * @return il metodo ricercato
	 * 
	 * @throws IllegalArgumentException nel caso in cui il metodo non si sia potuto ottenere
	 * @throws NullPointerException     nel caso in cui il parametro source sia <code>null</code>
	 */
	public static Method silentlyFindMethod(Object source, String methodName, Class<?>... classes) {
		return silentlyFindMethod(source.getClass(), methodName, classes);
	}
	
	
	/**
	 * Ottiene silenziosamente il metodo richiesto della classe.
	 * 
	 * @param clazz      la classe sorgente
	 * @param methodName il nome del metodo cercato
	 * @param classes    le classi componenti la firma del metodo
	 * 
	 * @return il metodo ricercato
	 * 
	 * @throws IllegalArgumentException nel caso in cui il metodo non si sia potuto ottenere
	 * @throws NullPointerException     nel caso in cui il parametro source sia <code>null</code>
	 */
	public static Method silentlyFindMethod(Class<?> clazz, String methodName, Class<?>... classes) {
		try {
			return clazz.getMethod(methodName, classes);
		} catch (SecurityException e) {
			throw new IllegalArgumentException("SecurityException in obtaining method " + clazz.getSimpleName() + "#" + methodName + "()", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("NoSuchMethodException in obtaining method " + clazz.getSimpleName() + "#" + methodName + "()", e);
		}
	}
	
	/**
	 * Ottiene silenziosamente il metodo richiesto dalla classe dell'oggetto fornito in input.
	 * @param <T> la tipizzazione del return type del metodo
	 * @param method il metodo da invocare
	 * @param source l'oggetto su cui invocare il metodo
	 * @param params i parametri del metodo
	 * @return il risultato del metodo
	 * @throws IllegalArgumentException nel caso in cui il metodo non si sia potuto ottenere
	 * @throws NullPointerException     nel caso in cui il parametro method sia <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T silentlyInvokeMethod(Method method, Object source, Object... params) {
		Class<?> clazz = method.getDeclaringClass();
		String methodName = method.getName();
		try {
			return (T) method.invoke(source, params);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("IllegalArgumentException in invoking method " + clazz.getSimpleName() + "#" + methodName + "()", e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("IllegalAccessException in invoking method " + clazz.getSimpleName() + "#" + methodName + "()", e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("InvocationTargetException in invoking method " + clazz.getSimpleName() + "#" + methodName + "()", e);
		}
	}
	

	/**
	 * Copia i field aventi lo stesso nome.
	 * 
	 * @param src  l'oggetto da cui ottenere i dati
	 * @param dest l'oggetto in cui apporre i dati
	 */
	public static void copySameNamedFields(Object src, Object dest) {
		final String methodName = "copySameNamedFields";
		try {
			beanUtilsBean.copyProperties(dest, src);
		} catch (IllegalAccessException e) {
			LOG.error(methodName, "IllegalAccessException in property copying: ", e);
		} catch (InvocationTargetException e) {
			LOG.error(methodName, "InvocationTargetException in property copying: ", e);
		}
	}
	
}