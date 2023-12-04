/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccommon.util;

public class SystemInfo {

	private static SystemInfo instance = null;
	
	private static SystemInfo getIstance() {
		return instance == null ? instance = new SystemInfo() : instance;
	}

	private SystemInfo() {
	}

	public static String getJbossServerName() {
		return getIstance().getInfo("jboss.server.name");
	}

	public static String getJbossNodeName() {
		return getIstance().getInfo("jboss.node.name");
	}

	public static String getJbossBindAddress() {
		return getIstance().getInfo("jboss.bind.address");
	}

	public static String getJbossPartitionName() {
		return getIstance().getInfo("jboss.partition.name");
	}

	private String getInfo(String info) {
		return System.getProperty(info);
	}
}

