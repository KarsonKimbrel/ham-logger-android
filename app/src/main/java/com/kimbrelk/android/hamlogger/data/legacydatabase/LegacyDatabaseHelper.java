package com.kimbrelk.android.hamlogger.data.legacydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class LegacyDatabaseHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 10;
	public static final String DATABASE_NAME = "database.db";
	
	public LegacyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(LegacyDatabaseContract.BookEntry.SQL_CREATE);
		db.execSQL(LegacyDatabaseContract.LogEntry.SQL_CREATE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= 1 && oldVersion < DATABASE_VERSION) {
			if (oldVersion <= 1) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.CONTEST_SECTION_RX + " VARCHAR(16)");
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.CONTEST_CHECK_RX + " VARCHAR(16)");
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.BookEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.BookEntry.SHOW_CONTEST_FIELDDAY + " BOOLEAN");
				db.execSQL(
						"UPDATE " + LegacyDatabaseContract.BookEntry.TABLE_NAME + " " +
								"SET show_contest = " + LegacyDatabaseContract.BookEntry.SHOW_CONTEST_FIELDDAY);
			}
			if (oldVersion <= 2) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.CONTEST_CLASS_RX + " VARCHAR(16)");
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.CONTEST_ID + " VARCHAR(16)");
			}
			if (oldVersion <= 3) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.CONTEST_CLASS_TRANSMITTERS_RX + " INT");
			}
			if (oldVersion <= 4) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.MODE_SUB + " VARCHAR(16)");
			}
			if (oldVersion <= 5) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.BookEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.BookEntry.SHOW_COMMENTS + " BOOLEAN");
			}
			if (oldVersion <= 6) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.POWER_RX + " REAL");
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.POWER_TX + " REAL");
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.SIGNAL_RX + " VARCHAR(3)");
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.SIGNAL_TX + " VARCHAR(3)");
			}
			if (oldVersion <= 7) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.BAND + " VARCHAR(8)");
			}
			if (oldVersion <= 8) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.IMPORT + " BLOB");
			}
			if (oldVersion <= 9) {
				db.execSQL(
						"ALTER TABLE " + LegacyDatabaseContract.LogEntry.TABLE_NAME + " " +
								"ADD " + LegacyDatabaseContract.LogEntry.COMMENTS_RX + " TEXT");
			}
		}
		else if (oldVersion != newVersion) {
			db.execSQL(LegacyDatabaseContract.BookEntry.SQL_DELETE);
			db.execSQL(LegacyDatabaseContract.LogEntry.SQL_DELETE);
			onCreate(db);
		}
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}