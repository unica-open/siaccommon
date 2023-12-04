/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.mapper;

public interface Mapper<A, B> {
	
	public void map(A o1, B o2);	
	public B map(A o);

	public B map(A o, @SuppressWarnings("unchecked") MapperDecorator<A, B>... decorators);
	
	public void decorate(A o, B b, @SuppressWarnings("unchecked") MapperDecorator<A, B>... decorators);
}
