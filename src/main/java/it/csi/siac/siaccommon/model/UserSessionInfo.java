/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class UserSessionInfo implements Serializable {
	private static final long serialVersionUID = -7395471514700159206L;

	private static final String AC_ID_LABEL = "AC";
	private static final String SS_ID_LABEL = "SS";
	private static final String RQ_ID_LABEL = "RQ";
	private static final String DFLT_LABEL = "-";
	public static final UserSessionInfo EMPTY = new UserSessionInfo();

	private String accountCode;
	private String sessionId;
	private String requestId;
	
	public UserSessionInfo() {}

	public UserSessionInfo(String accoundCode, String sessionId, String requestId) {
		this();
		this.accountCode = accoundCode;
		this.sessionId = sessionId;
		this.requestId = requestId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	@Override
	public String toString() {
		return 
				AC_ID_LABEL + ":" + StringUtils.defaultString(accountCode, DFLT_LABEL) + " " + 
				SS_ID_LABEL + ":" + StringUtils.defaultString(sessionId, DFLT_LABEL) + " " + 
				RQ_ID_LABEL + ":" + StringUtils.defaultString(requestId, DFLT_LABEL);
	}
}
