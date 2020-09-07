package com.kimbrelk.android.hamlogger.data.legacydatabase.model;

import androidx.annotation.Nullable;
import com.kimbrelk.android.hamlogger.utils.Listable;

public enum PropagationMode implements Listable {
	AS("Aircraft scatter"),
	AUE("Aurora-E"),
	AUR("Aurora"),
	BS("Back scatter"),
	ECH("EchoLink"),
	EME("Earth-Moon-Earth"),
	ES("Sporadic E"),
	F2("F2 reflection"),
	FAI("Field Aligned Irregularities"),
	INTERNET("Internet-assisted"),
	ION("Ionoscatter"),
	IRL("IRLP"),
	MS("Meteor scatter"),
	RPT("Repeater or transponder"),
	RS("Rain scatter"),
	SAT("Satellite"),
	TEP("Trans-equatorial"),
	TR("Tropospheric ducting");
	
	private String description;
	
	PropagationMode() {
		this(null);
	}
	
	PropagationMode(@Nullable String description) {
		this.description = description;
	}
	
	public final @Nullable String getDescription() {
		return description;
	}
	
	public final String getLabel() {
		return description != null ? description : name();
	}
	
}