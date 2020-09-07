package com.kimbrelk.android.hamlogger.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kimbrelk.android.hamlogger.data.legacydatabase.model.Mode;
import com.kimbrelk.android.hamlogger.utils.Listable;

public final class ModeSubModePair implements Listable, Comparable<ModeSubModePair> {
	private @NonNull Mode mode;
	private @Nullable String subMode;
	
	public ModeSubModePair(@NonNull Mode mode, @Nullable String subMode) {
		this.mode = mode;
		this.subMode = subMode;
	}
	
	public final @NonNull Mode getMode() {
		return mode;
	}
	
	public final @Nullable String getSubMode() {
		return subMode;
	}
	
	@NonNull
	@Override
	public final String getLabel() {
		return (subMode != null && !subMode.equals("")) ? subMode : mode.toString();
	}
	
	@Override
	public int compareTo(@NonNull ModeSubModePair o) {
		return getLabel().compareTo(o.getLabel());
	}
}