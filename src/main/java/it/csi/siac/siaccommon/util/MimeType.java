/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util;

public enum MimeType {
	PDF("application/pdf"),
	PLAIN_TEXT("text/plain"),
	CSV("text/csv"),
	XML("application/xml"),
	OCTET_STREAM("application/octet-stream"),
	XLS("application/vnd.ms-excel"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	;
	
	
	private String mimeType;
	
	MimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public String getMimeType() {
		return mimeType;
	}
}
