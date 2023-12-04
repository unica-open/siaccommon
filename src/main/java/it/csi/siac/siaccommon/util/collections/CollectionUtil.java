/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.ReflectionUtil;

/**
 * Utility class for collection usage.
 * 
 * @author Marchino Alessandro, AR
 * @version 1.0.0 - 20/11/2014
 *
 */
public final class CollectionUtil {
	
	/** Just an util class with static methods */
	private CollectionUtil() {
	}
	
	
	public static <E> List<E> asList(@SuppressWarnings("unchecked") E...elements) {
		return new ArrayList<E>(Arrays.asList(elements));
	}
	
	/**
	 * Collects a collection given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceCollection the source collection to extract the data from
	 * @param mapper           the mapping function
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D> Collection<D> map(Collection<S> sourceCollection, Function<S, D> mapper) {
		Collection<D> result = new ArrayList<D>();
		if(sourceCollection == null) {
			return result;
		}
		map(sourceCollection.iterator(), mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection backed by an iterator given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceIterator the source iterator to extract the data from
	 * @param mapper         the mapping function
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D> Collection<D> map(Iterator<S> sourceIterator, Function<S, D> mapper) {
		Collection<D> result = new ArrayList<D>();
		if(sourceIterator == null) {
			return result;
		}
		map(sourceIterator, mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection given the collector into a specified class.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param <C> the collection type
	 * @param sourceCollection the source collection to extract the data from
	 * @param mapper           the mapping function
	 * @param collectionClass  the resulting collection class
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D, C extends Collection<D>> C map(Collection<S> sourceCollection, Function<S, D> mapper, Class<C> collectionClass) {
		C result;
		try {
			result = collectionClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Instantiation exception for class " + collectionClass.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Access exception for class " + collectionClass.getSimpleName(), e);
		}
		if(sourceCollection == null) {
			return result;
		}
		map(sourceCollection.iterator(), mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection backed by an iterator given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param <C> the collection type
	 * @param sourceIterator  the source iterator to extract the data from
	 * @param mapper          the mapping function
	 * @param collectionClass the resulting collection class
	 * 
	 * @return the collected values
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D, C extends Collection<D>> Collection<D> map(Iterator<S> sourceIterator, Function<S, D> mapper, Class<C> collectionClass) {
		C result;
		try {
			result = collectionClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Instantiation exception for class " + collectionClass.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Access exception for class " + collectionClass.getSimpleName(), e);
		}
		if(sourceIterator == null) {
			return result;
		}
		map(sourceIterator, mapper, result);
		return result;
	}
	
	/**
	 * Collects a collection given the collector into a specified collection.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceCollection      the source collection to extract the data from
	 * @param mapper                the mapping function
	 * @param destinationCollection the destination collection to collect the data into
	 * 
	 * @see #map(Iterator, Function, Collection)
	 */
	public static <S, D> void map(Collection<S> sourceCollection, Function<S, D> mapper, Collection<D> destinationCollection) {
		if(sourceCollection == null) {
			return;
		}
		map(sourceCollection.iterator(), mapper, destinationCollection);
	}
	
	/**
	 * Collects a collection backed by an iterator given the collector.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param sourceIterator        the source iterator to extract the data from
	 * @param mapper                the mapping function
	 * @param destinationCollection the destination collection to collect the data into
	 * 
	 * @throws UnsupportedOperationException if the destination exception does not support the {@link Collection#add(Object)} method
	 */
	public static <S, D> void map(Iterator<S> sourceIterator, Function<S, D> mapper, Collection<D> destinationCollection) {
		if(sourceIterator == null || mapper == null) {
			return;
		}
		while(sourceIterator.hasNext()) {
			D collectedValue = mapper.map(sourceIterator.next());
			destinationCollection.add(collectedValue);
		}
	}
	
	/**
	 * Filters a given collection and applies the data in a collection of the same class.
	 * @param <S> the source type
	 * @param sourceCollection the collection to filter
	 * @param filter           the filtering function
	 * 
	 * @return the filtered collection
	 */
	public static <S> List<S> filter(Collection<S> sourceCollection, Filter<S> filter) {
		if(sourceCollection == null || filter == null) {
			return null;
		}
		
		return (List<S>) filter(sourceCollection, filter, new ArrayList<S>());
	}

	/**
	 * Find first element of a given collection according to filter.
	 * @param <S> the source type
	 * @param sourceCollection the collection to filter
	 * @param filter           the filtering function
	 * 
	 * @return the first element according to filter
	 */
	public static <S> S findFirst(Collection<S> sourceCollection, Filter<S> filter) {
		if(sourceCollection == null || filter == null) {
			return null;
		}

		for(S source : sourceCollection) {
			if(filter.isAcceptable(source)) {
				return source;
			}
		}
		
		return null;
	}
	
	/**
	 * Filters a given collection <em>in situ</em>, modifying the original.
	 * @param <S> the source type
	 * @param sourceCollection the collection to filter
	 * @param filter           the filtering function
	 */
	public static <S> void filterModifying(Collection<S> sourceCollection, Filter<S> filter) {
		if(sourceCollection == null || filter == null) {
			return;
		}
		Iterator<S> iterator = sourceCollection.iterator();
		while(iterator != null && iterator.hasNext()) {
			if(!filter.isAcceptable(iterator.next())) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * Filters a given collection and applies the data in the given destination collection.
	 * @param <S> the source type
	 * @param sourceCollection      the collection to filter
	 * @param filter                the filtering function
	 * @param destinationCollection the collection in which to add the filtered data
	 * 
	 * @return the filtered collection
	 */
	public static <S> Collection<S> filter(Collection<S> sourceCollection, Filter<S> filter, Collection<S> destinationCollection) {
		if(sourceCollection == null) {
			return destinationCollection;
		}
		if(filter == null) {
			destinationCollection.addAll(sourceCollection);
			return destinationCollection;
		}
		for(S source : sourceCollection) {
			if(filter.isAcceptable(source)) {
				destinationCollection.add(source);
			}
		}
		return destinationCollection;
	}
	
	/**
	 * Filters a given collection and applies the data in a collection of the given class.
	 * @param <S> the source type
	 * @param <C> the collection type
	 * @param sourceCollection the collection to filter
	 * @param filter           the filtering function
	 * @param collectionClass  the class of the resulting collection
	 * 
	 * @return the filtered collection
	 * @see CollectionUtil#filter(Collection, Filter, Collection)
	 */
	@SuppressWarnings("unchecked")
	public static <S, C extends Collection<S>> C filter(Collection<S> sourceCollection, Filter<S> filter, Class<C> collectionClass) {
		C destinationCollection;
		try {
			destinationCollection = collectionClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Instantiation exception for class " + collectionClass.getSimpleName(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Access exception for class " + collectionClass.getSimpleName(), e);
		}
		if(sourceCollection == null) {
			return destinationCollection;
		}
		return (C) filter(sourceCollection, filter, destinationCollection);
	}
	
	/**
	 * Applies the given predicate to the given collection.
	 * @param <S> the source type
	 * @param collection the collection to filter
	 * @param predicate  the filtering function
	 */
	public static <S> void apply(Collection<S> collection, Predicate<S> predicate) {
		if(collection == null || predicate == null) {
			return;
		}
		for(S source : collection) {
			predicate.apply(source);
		}
	}
	
	// TODO: Not yet implemented
//	public static <S, D> D reduce(Collection<S> collection, Reductor<S, D> reductor) {
//		return reduce(collection.iterator(), reductor);
//	}
//	public static <S, D> D reduce(Iterator<S> iterator, Reductor<S, D> reductor) {
//		return reduce(iterator, reductor, null);
//	}
//	public static <S, D> D reduce(Iterator<S> iterator, Reductor<S, D> reductor, D initialValue) {
//		return null;
//	}
	
		
	/**
	 * Reduces the values in the given collection to a single accumulated value.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param collection   the collection to reduce
	 * @param reductor     the reductor
	 * @param initialValue the initial value
	 * 
	 * @return the reduced value
	 */
	public static <S> S reduce(Collection<S> collection, Reductor<S, S> reductor) {
		if(isEmpty(collection)) {
			return null;
		}

		return reduce(collection, reductor, collection.iterator().next());
	}
	
	public static <S, D> D reduce(Collection<S> collection, Reductor<S, D> reductor, D initialValue) {
		return reduce(collection.iterator(), reductor, initialValue);
	}
	
	/**
	 * Reduces the value of the iterator backing the collection to a single accumulated value.
	 * @param <S> the source type
	 * @param <D> the destination type
	 * @param iterator     the iterator
	 * @param collection   the collection backing the iterator
	 * @param reductor     the reductor
	 * @param initialValue the initial value
	 * 
	 * @return the reduced value
	 */
	private static <S, D> D reduce(Iterator<S> iterator, Reductor<S, D> reductor, D initialValue) {
		if(reductor == null) {
			return null;
		}

		D accumulator = initialValue;
		for(int index = 0; iterator.hasNext(); index++) {
			S currentValue = iterator.next();
			accumulator = reductor.reduce(accumulator, currentValue, index);
		}
		return accumulator;
	}
	
	
	public static <S> Collection<S> distinct(Collection<S> collection, Function<S, String> distinctKeyMapper) {
		
		Map<String, S> distinctMap = new ConcurrentHashMap<String, S>();
		
		for (S s : collection) {
			distinctMap.putIfAbsent(distinctKeyMapper.map(s), s);
		}
		
		return distinctMap.values();
	}
	
	/**
	 * Adds a char sequence to the collection if it is not null nor empty.
	 * @param <C> the char sequence type
	 * @param collection the collection to populate
	 * @param added      the term to add
	 */
	public static <C extends CharSequence> void addIfNotNullNorEmpty(Collection<C> collection, C added) {
		if(StringUtils.isNotBlank(added)) {
			collection.add(added);
		}
	}
	
	/**
	 * Joins char sequences if they are not null nor empty
	 * @param <C> the char sequence type
	 * @param separator the chunks separator
	 * @param chunks    the chunks to join
	 * @return the joined chunks
	 */
	public static <C extends CharSequence> String join(String separator, C... chunks) {
		Collection<C> coll = new ArrayList<C>();
		for(C chunk : chunks) {
			addIfNotNullNorEmpty(coll, chunk);
		}
		return StringUtils.join(coll, separator);
	}


	
	/**
	 * Rewraps an iterable source in a list, to prevent any unsupported operations on the given iterable
	 * @param <T> the iterable type
	 * @param iterable the iterable source
	 * @return the list rewrapping the iterable source's content
	 */
	public static <T> List<T> rewrap(Iterable<T> iterable) {
		List<T> res = new ArrayList<T>();
		for(T t : iterable) {
			res.add(t);
		}
		return res;
	}
	
	/**
	 * Checks whether the given collection has a single element
	 * @param coll the collection to check
	 * @return <code>true</code> if the collection is not-null and has a single element; <code>false</code> otherwise
	 */
	public static boolean hasOneSingleElement(Collection<?> coll) {
		return coll != null && coll.size() == 1;
	}
	
	/**
	 * Checks whether the given collection has at most a single element
	 * @param coll the collection to check
	 * @return <code>true</code> if the collection is not-null and has at most a single element; <code>false</code> otherwise
	 */
	public static boolean hasAtMostOneSingleElement(Collection<?> coll) {
		return coll != null && coll.size() <= 1;
	}
	
	/**
	 * Gets the first element of a collection (useful for collections without the 'get' method)
	 * @param <E> the collection type
	 * @param coll the collection
	 * @return the first element, if present; <code>null</code> otherwise
	 */
	public static <E> E getFirst(Collection<E> coll) {
		if(coll == null || coll.isEmpty()) {
			return null;
		}
		
		for(E e : coll) {
			return e;
		}

		throw new IllegalStateException();
	}
	
	
	public static <T> T getNth(List<T> list, int n) {
		if (list == null || list.isEmpty() || n >= list.size() || n < 0) {
			return null;
		}
		
		return list.get(n);
	}

	public static <T> T getLast(List<T> list) {
		return list == null ? null : getNth(list, list.size()-1);
	}

	
	public static <E, K, V> Map<K, V> convertToMap(Collection<E> collection, Function<E, K> keyFunction, Function<E, V> valueFunction) {
		
		if (collection == null) {
			return null;
		}
		
		Map<K, V> map = new HashMap<K, V>();
		
		for (E e : collection) {
			K key = keyFunction.map(e);
			if (key != null) {
				map.put(key, valueFunction.map(e));
			}
		}
		
		return map;
	}

	/**
	 * Estrae la sottolista corrispondente al tipo indicato
	 * @param <A> il tipo della lista originale
	 * @param <B> il tipo da estrarre
	 * @param list la lista da filtrare
	 * @param clazz la classe da estrarre
	 * @return i record relativi alla classe
	 */
	@SuppressWarnings("unchecked")
	public static <A, B extends A> List<B> extractByType(Iterable<A> list, Class<B> clazz) {
		List<B> res = new ArrayList<B>();
		for(A acc : list) {
			if(clazz.isInstance(acc)) {
				res.add((B)acc);
			}
		}
		return res;
	}

	public static <E> Integer getSize(Collection<E> c) {
		return c == null ? null : c.size();
	}

	
	/**
	 * Fornisce un valore di default alla lista fornita in input nel caso in cui essa sia <code>null</code>.
	 * @param <E> la tipizzazione della lista
	 * 
	 * @param list la lista da defaultare
	 * @return la lista originale, se non <code>null</code>; una lista vuota in caso contrario
	 */
	public static <E> List<E> defaultList(List<E> list) {
		return defaultList(list, new ArrayList<E>());
	}

	/**
	 * Fornisce un valore di default alla lista fornita in input nel caso in cui essa sia <code>null</code>.
	 * @param <E> la tipizzazione della lista
	 * 
	 * @param list la lista da defaultare
	 * @param defaultList la lista alternativa
	 * @return la lista originale, se non <code>null</code>; defaultList in caso contrario
	 */
	public static <E> List<E> defaultList(List<E> list, List<E> defaultList) {
		return list == null ? defaultList : list;
	}

	
	public static <E> E getOneOnly(Collection<E> collection) {
		if (collection == null || collection.isEmpty()) {
			return null;
		}
		
		if (collection.size() > 1) {
			throw new IllegalArgumentException("Collection has many elements");
		}

		return getFirst(collection);
	}

	public static <E> void addAll(Collection<E> toAdd, Collection<E> toBeAdded) {
		if (toAdd == null) {
			return;
		}
		
		if (toBeAdded != null && !toBeAdded.isEmpty()) {
			toAdd.addAll(toBeAdded);
		}
	}

	public static <E> E[] toArray(Collection<E> collection, Class<E> cls) {
		if (collection == null) {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		E[] eArr = (E[]) Array.newInstance(cls, collection.size());

		int i = 0;
		for (E e : collection) {
			eArr[i++] = e;
		}
		
		return eArr;
	}
	
	public static <E> List<E> toList(Collection<E> coll) {
		return new ArrayList<E>(coll);
	}
	
	public static <E> boolean isEmpty(Collection<E> coll) {
		return (coll == null || coll.isEmpty());
	}
	
	public static <E> boolean isNotEmpty(Collection<E> coll) {
		return !isEmpty(coll);
	}

	/**
	 * @return Returns a sorted copy list of the specified list
	 */
	public static <E> List<E> getSortedList(List<E> coll, Comparator<E> comparator) {
		if (coll == null || comparator == null) {
			return coll;
		}
		return sort(new ArrayList<E>(coll), comparator);
	}
	
	/**
	 * @return Returns a sorted copy list of the specified list
	 */
	public static <E> List<E> getSortedList(List<E> coll, Class<? extends Comparator<E>> comparatorClass) {
		return getSortedList(coll, ReflectionUtil.silentlyBuildInstance(comparatorClass));
	}
	
	/**
	 * @return Sorts the specified list and returns it
	 */
	public static <E> List<E> sort(List<E> coll, Comparator<E> comparator) {
		if (coll == null || comparator == null) {
			return coll;
		}
		
		Collections.sort(coll, comparator);
		
		return coll;
	}

	/**
	 * @return Sorts the specified list and returns it
	 */
	public static <E> List<E> sort(List<E> coll, Class<? extends Comparator<E>> comparatorClass) {
		return sort(coll, ReflectionUtil.silentlyBuildInstance(comparatorClass));
	}
}
