package com.kimbrelk.android.hamlogger.data.legacydatabase.model;

import com.kimbrelk.android.hamlogger.utils.Listable;

public enum ARRL_SECTION implements Listable {
	AL("Alabama"),
	AK("Alaska"),
	AB("Alberta"),
	AR("Arkansas"),
	AZ("Arizona"),
	BC("British Columbia"),
	CO("Colorado"),
	CT("Connecticut"),
	DE("Delaware"),
	EB("East Bay"),
	EMA("Eastern Massachusetts"),
	ENY("Eastern New York"),
	EPA("Eastern Pennsylvania"),
	EWA("Eastern Washington"),
	GA("Georgia"),
	GTA("Greater Toronto Area"),
	ID("Idaho"),
	IL("Illinois"),
	IN("Indiana"),
	IA("Iowa"),
	KS("Kansas"),
	KY("Kentucky"),
	LAX("Los Angeles"),
	LA(),
	ME(),
	MB(),
	MAR(),
	MDC(),
	MI(),
	MN(),
	MS(),
	MO(),
	MT(),
	NE(),
	NV(),
	NH(),
	NLI(),
	NL(),
	NC(),
	ND(),
	NTX(),
	NFL(),
	NNJ(),
	NNY(),
	NT(),
//	NWT(),
	OH(),
	OK(),
//	ON(),
	ONE(),
	ONN(),
	ONS(),
	ORG(),
	OR(),
	PAC(),
	PR(),
	QC(),
	RI(),
	SV(),
	SDG(),
	SF(),
	SJV(),
	SB(),
	SCV(),
	SK(),
	SC(),
	SD(),
	STX(),
	SFL(),
	SNJ(),
	TN(),
	VI(),
	UT(),
	VT(),
	VA(),
	WCF(),
	WTX(),
	WV(),
	WMA(),
	WNY(),
	WPA(),
	WWA(),
	WI(),
	WY();
	
	private String name;
	
	ARRL_SECTION() {
		this.name = null;
	}
	
	ARRL_SECTION(String name) {
		this.name = name;
	}
	
	public String getLabel() {
		return name();
	}
}
