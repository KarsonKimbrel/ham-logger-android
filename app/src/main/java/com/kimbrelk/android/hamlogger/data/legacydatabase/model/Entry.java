package com.kimbrelk.android.hamlogger.data.legacydatabase.model;

import android.database.Cursor;
import com.kimbrelk.android.hamlogger.data.legacydatabase.LegacyDatabaseContract;
import com.kimbrelk.android.hamlogger.utils.UtilsLegacy;

import java.util.Calendar;
import java.util.TimeZone;

public class Entry {
	
	public String id;
	public String band;
	public String bookId;
	public String callOp;
	public String callTx;
	public String callRx;
	public String contestCheck;
	public String contestClass;
	public int contestClassTransmitters;
	public String contestId;
	public String contestSection;
	public double frequency;
	public String mode;
	public String modeSub;
	public String powerReportRx;
	public String powerReportTx;
	public String signalReportRx;
	public String signalReportTx;
	public String locGridTx;
	public String locGridRx;
	public String commentsRx;
	public String commentsTx;
	public String time;
	public String date;
	
	public Entry() {
		id = UtilsLegacy.createNewUUID();
		band = "";
		bookId = "";
		callOp = "";
		callTx = "";
		callRx = "";
		contestCheck = "";
		contestClass = "";
		contestClassTransmitters = 0;
		contestId = "";
		contestSection = "";
		frequency = 0.0;
		mode = "";
		modeSub = "";
		powerReportRx = "";
		powerReportTx = "";
		signalReportRx = "";
		signalReportTx = "";
		locGridTx = "";
		locGridRx = "";
		commentsRx = "";
		commentsTx = "";
		date = "";
		time = "";
	}
	
	public Entry(Cursor cursor) {
		id = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry._ID));
		band = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.BAND));
		bookId = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.BOOK_ID));
		callOp = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CALLSIGN_OP));
		callTx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CALLSIGN_TX));
		callRx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CALLSIGN_RX));
		contestCheck = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CONTEST_CHECK_RX));
		contestClass = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CONTEST_CLASS_RX));
		contestClassTransmitters = cursor.getInt(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CONTEST_CLASS_TRANSMITTERS_RX));
		contestId = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CONTEST_ID));
		contestSection = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.CONTEST_SECTION_RX));
		frequency = cursor.getDouble(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.FREQUENCY));
		mode = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.MODE));
		modeSub = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.MODE_SUB));
		powerReportTx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.POWER_TX));
		powerReportRx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.POWER_RX));
		signalReportTx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.SIGNAL_TX));
		signalReportRx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.SIGNAL_RX));
		locGridTx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.LOC_GRID_TX));
		locGridRx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.LOC_GRID_RX));
		commentsRx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.COMMENTS_RX));
		commentsTx = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.COMMENTS_TX));
		date = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.DATE));
		time = cursor.getString(cursor.getColumnIndexOrThrow(LegacyDatabaseContract.LogEntry.TIME));
	}
	
	public long getTimestamp() {
		if (date == null || time == null || date.equals("") || time.equals("")) {
			return 0;
		}
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6)) - 1;
		int day = Integer.parseInt(date.substring(6, 8));
		int hour = Integer.parseInt(time.substring(0, 2));
		int min = Integer.parseInt(time.substring(2, 4));
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(year, month, day, hour, min);
		return calendar.getTimeInMillis();
	}
	
	
}