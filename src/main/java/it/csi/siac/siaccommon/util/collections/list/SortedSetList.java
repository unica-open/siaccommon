/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections.list;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementation of {@link SortedList} via a sorted set.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/giu/2014
 *
 * @param <E> the generic item of the list
 */
public class SortedSetList<E> extends AbstractList<E> implements SortedList<E>, Serializable {
	
	private static final long serialVersionUID = 2396945965661398189L;
	private final SortedSet<E> sortedSet;
	
	/**
     * Constructs an empty list with the default hash comparator.
     */
	public SortedSetList() {
		super();
		this.sortedSet = new TreeSet<E>();
	}
	
	/**
	 * Constructs an empty list with the given comparator.
	 * 
	 * @param comparator the comparator to use
	 */
	public SortedSetList(Comparator<? super E> comparator) {
		super();
		this.sortedSet = new TreeSet<E>(comparator);
	}
	
	/**
	 * Constructs an empty list comprising the values of the given collection.
	 * 
	 * @param collection the collection to wrap
	 */
	public SortedSetList(Collection<? extends E> collection) {
		super();
		this.sortedSet = new TreeSet<E>();
		if(collection != null) {
			this.sortedSet.addAll(collection);
		}
	}
	
	/**
	 * Constructs an empty list comprising the values of the given collection and with the given comparator.
	 * 
	 * @param comparator the comparator to use
	 * @param collection the collection to wrap
	 */
	public SortedSetList(Collection<? extends E> collection, Comparator<? super E> comparator) {
		super();
		this.sortedSet = new TreeSet<E>(comparator);
		if(collection != null) {
			this.sortedSet.addAll(collection);
		}
	}
	
	@Override
	public boolean add(E e) {
		return sortedSet.add(e);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return sortedSet.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("positional addAll not supported");
	}
	
	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("positional set not supported");
	}
	
	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException("positional add not supported");
	}
	
	@Override
	public E remove(int index) {
		E res = get(index);
		sortedSet.remove(res);
		return res;
	}
	
	@Override
	public boolean remove(Object o) {
		return sortedSet.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return sortedSet.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return sortedSet.retainAll(c);
	}

	@Override
	public E get(int index) {
		if(index < 0 || index > size()) {
			throw new IndexOutOfBoundsException("index " + index + " is not in the range 0-" + size());
		}
		Iterator<E> it = iterator();
		for(int i = 0; i < index; i++, it.next()) {
			// Doing nothing, just to scroll the iterator to the correct place
		}
		return it.next();
	}

	@Override
	public int size() {
		return sortedSet.size();
	}
	
	@Override
	public boolean contains(Object o) {
		return sortedSet.contains(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return sortedSet.containsAll(c);
	}
	
	@Override
	public Iterator<E> iterator() {
		return sortedSet.iterator();
	}
	
}
