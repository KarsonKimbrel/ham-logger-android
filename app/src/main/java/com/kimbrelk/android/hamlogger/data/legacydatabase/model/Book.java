package com.kimbrelk.android.hamlogger.data.legacydatabase.model;

import android.database.Cursor;
import com.kimbrelk.android.hamlogger.data.legacydatabase.LegacyDatabaseContract;
import com.kimbrelk.android.hamlogger.utils.UtilsLegacy;

public class Book {
	
	public String id;
	public String title;
	public String description;
	public String callsign;
	public String gridSquare;
	public boolean hasMultipleOps;
	public boolean showComments;
	public boolean showContestFieldday;
	public boolean showLocGrid;
	public boolean showLocCity;
	public boolean showPower;
	public boolean showSignal;
	
	public Book() {
		this.id = UtilsLegacy.createNewUUID();
		this.title = "";
		this.description = "";
		this.callsign = "";
		this.gridSquare = "";
		this.hasMultipleOps = false;
		this.showComments = false;
		this.showContestFieldday = false;
		this.showLocGrid = false;
		this.showLocCity = false;
		this.showPower = false;
		this.showSignal = false;
	}
	
	public Book(String id) {
		this.id = id;
		this.title = "";
		this.description = "";
		this.callsign = "";
		this.gridSquare = "";
		this.hasMultipleOps = false;
		this.showComments = false;
		this.showContestFieldday = false;
		this.showLocGrid = false;
		this.showLocCity = false;
		this.showPower = false;
		this.showSignal = false;
	}
	
	public Book(String id, String title, String description, String callsign) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.callsign = callsign;
		this.gridSquare = "";
		this.hasMultipleOps = false;
		this.showComments = false;
		this.showContestFieldday = false;
		this.showLocGrid = false;
		this.showLocCity = false;
		this.showPower = false;
		this.showSignal = false;
	}
	
	public Book(Cursor cursor) {
		this.id = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry._ID));
		this.title = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.TITLE));
		this.description = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.DESCRIPTION));
		this.callsign = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.CALLSIGN));
		this.gridSquare = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.GRID_SQUARE));
		this.hasMultipleOps = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.HAS_MULTIPLE_OPS)) == 1;
		this.showComments = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.SHOW_COMMENTS)) == 1;
		this.showContestFieldday = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.SHOW_CONTEST_FIELDDAY)) == 1;
		this.showLocGrid = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.SHOW_LOC_GRID)) == 1;
		this.showLocCity = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.SHOW_LOC_CITY)) == 1;
		this.showPower = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.SHOW_POWER)) == 1;
		this.showSignal = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.BookEntry.SHOW_SIGNAL)) == 1;
	}
}