/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.mapper;

public interface MapperDecorator<A, B> {

	public void decorate(A a, B b);
}
