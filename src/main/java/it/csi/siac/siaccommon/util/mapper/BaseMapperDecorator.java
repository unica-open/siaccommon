/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util.mapper;

public abstract class BaseMapperDecorator<A, B> implements MapperDecorator<A, B> {

	MapperDecorator<?, ?>[] mapperDecorators;

	public MapperDecorator<?, ?>[] getMapperDecorators() {
		return mapperDecorators;
	}

	public void setMapperDecorators(MapperDecorator<?, ?>[] mapperDecorators) {
		this.mapperDecorators = mapperDecorators;
	}
}
