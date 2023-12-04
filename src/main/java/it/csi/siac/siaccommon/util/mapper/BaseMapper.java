/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.mapper;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccommon.util.CoreUtil;

public abstract class BaseMapper<A, B> implements Mapper<A, B> {

	@Override
	public B map(A a) {
		return internalMap(a);
	}	
	
	@SafeVarargs
	@Override
	public final B map(A a, MapperDecorator<A, B>... decorators) {
		return internalMap(a, decorators);
	}
		
	public List<B> map(List<A> list) {
		return internalMap(list);
	}
	
	@SafeVarargs
	@Override
	public final void decorate(A a, B b, MapperDecorator<A, B>... decorators) {
		
		if (a == null || b ==  null ) {
			return;
		}
		
		if (decorators != null) {
			for (MapperDecorator<A, B> d : decorators) {
				d.decorate(a, b);			
			}
		}
	}	

	@SafeVarargs
	public final List<B> map(List<A> list, MapperDecorator<A, B>... decorators) {
		return internalMap(list, decorators);
	}

	public void map(List<A> list, B b) {
		for (A a : list) {
			map(a, b);
		}
	}
	
	@SafeVarargs
	private final B internalMap(A a, MapperDecorator<A, B>... decorators) {
		
		if (a == null) {
			return null;
		}
		
		B b = CoreUtil.instantiateNewGenericType(this.getClass(), Mapper.class, 1);
		
		map(a, b);
		
		decorate(a, b, decorators);
		
		return b;
	}

	@SafeVarargs
	private final List<B> internalMap(List<A> list, MapperDecorator<A, B>... decorators) {
		if (list == null) {
			return null;
		}
		
		List<B> l = new ArrayList<B>();
		
		for (A a : list) {
			l.add(map(a, decorators));
		}
		
		return l;
	}
}
