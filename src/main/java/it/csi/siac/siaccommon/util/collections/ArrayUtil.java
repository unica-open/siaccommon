/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.collections;

import java.lang.reflect.Array;

public final class ArrayUtil {
	
	private ArrayUtil() {}
	

	
	public static <S, D> D[] map(S[] source, Class<D> cls, Function<S, D> mapper) {
		if(source == null || mapper == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		D[] dest = (D[]) Array.newInstance(cls, source.length);
		
		for(int i = 0; i<source.length; i++) {
			dest[i] = mapper.map(source[i]);
		}		
	
		return dest;
	}
	
	public static <S> int findFirst(S[] array, Filter<S> filter) {
		if(array == null || filter == null) {
			return -1;
		}

		for (int i = 0; i < array.length; i++) {
			S source = array[i];
			if(filter.isAcceptable(source)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public static <S, D> D reduce(S[] array, Reductor<S, D> reductor, D initialValue) {
		if(array == null || reductor == null) {
			return null;
		}

		D accumulator = initialValue;
		
		for(int i = 0; i<array.length; i++) {
			S currentValue = array[i];
			accumulator = reductor.reduce(accumulator, currentValue, i);
		}
		return accumulator;
	}


	@SafeVarargs
	public static <T> T[] toArray(T...objs) {
		return objs;
	}
	
	@SafeVarargs
	public static <T> T[] concat(Class<T> cls, T obj, T[]...objArrays) {
		return concat(cls, toArray(obj), internalConcat(cls, objArrays));
	}
	
	@SafeVarargs
	public static <T> T[] concat(Class<T> cls, T[] objArray, T...objs) {
		return internalConcat(cls, objArray, objs);
	}
	
	@SafeVarargs
	public static <T> T[] concat(Class<T> cls, T[]...objArrays) {
		return internalConcat(cls, objArrays);
	}
	
	@SafeVarargs
	private static <T> T[] internalConcat(Class<T> cls, T[]...objArrays) {
		int totalLength = 0;
		for (T[] array : objArrays) {
			totalLength += array.length;
		}

		@SuppressWarnings("unchecked")
		T[] result = (T[]) Array.newInstance(cls, totalLength);

		int destPos = 0;
		for (T[] array : objArrays) {
			System.arraycopy(array, 0, result, destPos, array.length);
			destPos += array.length;
		}

		return result;
	}
}
