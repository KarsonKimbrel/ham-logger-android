package com.kimbrelk.android.hamlogger.data.legacydatabase.model;

import androidx.annotation.Nullable;
import com.kimbrelk.android.hamlogger.data.model.ModeSubModePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Mode {
	AM(),
	ARDOP(),
	ATV(),
	C4FM(),
	CHIP("CHIP64", "CHIP128"),
	CLO(),
	CONTESTI(),
	CW("PCW"),
	DIGITALVOICE(),
	DOMINO("DOMINOEX", "DOMINOF"),
	DSTAR(),
	FAX(),
	FM(),
	FSK441(),
	FT8(),
	HELL("FMHELL", "FSKHELL", "HELL80", "HFSK", "PSKHELL"),
	ISCAT("ISCAT-A", "ISCAT-B"),
	JT4("JT4A", "JT4B", "JT4C", "JT4D", "JT4E", "JT4F", "JT4G"),
	JT6M(),
	JT9("JT9-1", "JT9-2", "JT9-5", "JT9-10", "JT9-30", "JT9A", "JT9B", "JT9C", "JT9D", "JT9E", "JT9E FAST", "JT9F", "JT9F FAST", "JT9G", "JT9G FAST", "JT9H", "JT9H FAST"),
	JT44(),
	JT65("JT65A", "JT65B", "JT65B2", "JT65C", "JT65C2"),
	MFSK("FSQCALL", "FST4", "FT4", "JS8", "MFSK4", "MFSK8", "MFSK11", "MFSK16", "MFSK22", "MFSK31", "MFSK32", "MFSK64", "MFSK128"),
	MSK144(),
	MT63(),
	OLIVIA("OLIVIA 4/125", "OLIVIA 4/250", "OLIVIA 8/250", "OLIVIA 8/500", "OLIVIA 16/500", "OLIVIA 16/1000", "OLIVIA 32/1000"),
	OPERA("OPERA-BEACON", "OPERA-QSO"),
	PAC("PAC2", "PAC3", "PAC4"),
	PAX("PAX2"),
	PKT(),
	PSK("FSK31", "PSK10", "PSK31", "PSK63", "PSK63F", "PSK125", "PSK250", "PSK500", "PSK1000", "PSKAM10", "PSKAM31", "PSKAM50", "PSKFEC31", "QPSK31", "QPSK63", "QPSK125", "QPSK250", "QPSK500", "SIM31"),
	PSK2K(),
	Q15(),
	QRA64("QRA64A", "QRA64B", "QRA64C", "QRA64D", "QRA64E"),
	ROS("ROS-EME", "ROS-HF", "ROS-MF"),
	RTTY("ASCI"),
	RTTYM(),
	SSB("LSB", "USB"),
	SSTV(),
	T10(),
	THOR(),
	THRB("THRBX"),
	TOR("AMTORFEC", "GTOR"),
	V4(),
	VOI(),
	WINMOR(),
	WSPR();
	
	private String[] subModes;
	
	Mode(@Nullable String... subModes) {
		this.subModes = subModes;
	}
	
	public final boolean hasSubModes() {
		return  subModes != null &&
				subModes.length > 0;
	}
	
	public final boolean hasSubMode(@Nullable String subMode) {
		if (subMode == null || !hasSubModes()) {
			return false;
		}
		subMode = subMode.toUpperCase();
		for(String tSubMode: subModes) {
			if (tSubMode.equals(subMode)) {
				return true;
			}
		}
		return false;
	}
	
	public final @Nullable String[] getSubModes() {
		if (subModes == null) {
			return new String[0];
		}
		return subModes;
	}
	
	public final static List<ModeSubModePair> getModeSubModePairs() {
		List<ModeSubModePair> result = new ArrayList<ModeSubModePair>();
		Mode[] modes = Mode.values();
		for(Mode mode : modes) {
			result.add(new ModeSubModePair(mode, null));
			for(String subMode : mode.subModes) {
				result.add(new ModeSubModePair(mode, subMode));
			}
		}
		Collections.sort(result);
		return result;
	}
}