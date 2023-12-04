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

import org.apache.commons.collections.SortedBag;
import org.apache.commons.collections.bag.TreeBag;

/**
 * Implementation of {@link SortedList} via a sorted bag.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/giu/2014
 *
 * @param <E> the generic item of the list
 */
public class SortedBagList<E> extends AbstractList<E> implements SortedList<E>, Serializable {
	
	private static final long serialVersionUID = 2396945965661398189L;
	private final SortedBag sortedBag;
	
	/**
	 * Constructs an empty list with the default hash comparator.
	 */
	public SortedBagList() {
		super();
		this.sortedBag = new TreeBag();
	}
	
	/**
	 * Constructs an empty list with the given comparator.
	 * 
	 * @param comparator the comparator to use
	 */
	public SortedBagList(Comparator<? super E> comparator) {
		super();
		this.sortedBag = new TreeBag(comparator);
	}
	
	/**
	 * Constructs an empty list comprising the values of the given collection.
	 * 
	 * @param collection the collection to wrap
	 */
	@SuppressWarnings("unchecked")
	public SortedBagList(Collection<? extends E> collection) {
		super();
		this.sortedBag = new TreeBag();
		if(collection != null) {
			this.sortedBag.addAll(collection);
		}
	}
	
	/**
	 * Constructs an empty list comprising the values of the given collection and with the given comparator.
	 * 
	 * @param comparator the comparator to use
	 * @param collection the collection to wrap
	 */
	@SuppressWarnings("unchecked")
	public SortedBagList(Collection<? extends E> collection, Comparator<? super E> comparator) {
		super();
		this.sortedBag = new TreeBag(comparator);
		if(collection != null) {
			this.sortedBag.addAll(collection);
		}
	}
	
	@Override
	public boolean add(E e) {
		return sortedBag.add(e);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean addAll(Collection<? extends E> c) {
		return sortedBag.addAll(c);
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
		sortedBag.remove(res);
		return res;
	}
	
	@Override
	public boolean remove(Object o) {
		return sortedBag.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return sortedBag.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return sortedBag.retainAll(c);
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
		return sortedBag.size();
	}
	
	@Override
	public boolean contains(Object o) {
		return sortedBag.contains(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return sortedBag.containsAll(c);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		return sortedBag.iterator();
	}
	
}
