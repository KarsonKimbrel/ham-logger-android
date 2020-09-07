package com.kimbrelk.android.hamlogger.data.legacydatabase;

import android.provider.BaseColumns;

public final class LegacyDatabaseContract {
	
	public final static String COMMA_SEP = ", ";
	
	public final static class BookEntry implements BaseColumns {
		public final static String TABLE_NAME = 			"books";
		public final static String TITLE = 					"title";
		public final static String DESCRIPTION = 			"description";
		public final static String CALLSIGN = 				"callsign";
		public final static String GRID_SQUARE = 			"grid_square";
		public final static String HAS_MULTIPLE_OPS = 		"has_multiple_ops";
		public final static String SHOW_COMMENTS = 			"show_comments";
		public final static String SHOW_CONTEST_FIELDDAY = 	"show_contest_fieldday";
		public final static String SHOW_LOC_GRID = 			"show_loc_grid";
		public final static String SHOW_LOC_CITY = 			"show_loc_city";
		public final static String SHOW_POWER = 			"show_power";
		public final static String SHOW_SIGNAL = 			"show_signal";
		
		public final static String SQL_CREATE =
				"CREATE TABLE " + TABLE_NAME + " (" +
						_ID + 					" CHARACTER(36) PRIMARY KEY" + COMMA_SEP +
						TITLE + 				" VARCHAR(255)" + COMMA_SEP +
						DESCRIPTION + 			" VARCHAR(255)" + COMMA_SEP +
						CALLSIGN + 				" VARCHAR(16)" + COMMA_SEP +
						GRID_SQUARE + 			" VARCHAR(6)" + COMMA_SEP +
						HAS_MULTIPLE_OPS + 		" BOOLEAN" + COMMA_SEP +
						SHOW_COMMENTS + 		" BOOLEAN" + COMMA_SEP +
						SHOW_CONTEST_FIELDDAY + " BOOLEAN" + COMMA_SEP +
						SHOW_LOC_GRID + 		" BOOLEAN" + COMMA_SEP +
						SHOW_LOC_CITY + 		" BOOLEAN" + COMMA_SEP +
						SHOW_POWER + 			" BOOLEAN" + COMMA_SEP +
						SHOW_SIGNAL + 			" BOOLEAN)";
		
		public final static String SQL_DELETE =
				"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public final static class LogEntry implements BaseColumns {
		public final static String TABLE_NAME = 		"entries";
		public final static String BAND = 				"band";
		public final static String BOOK_ID = 			"book_id";
		public final static String CALLSIGN_OP = 		"callsign_op";
		public final static String CALLSIGN_TX = 		"callsign_tx";
		public final static String CALLSIGN_RX = 		"callsign_rx";
		public final static String COMMENTS_TX = 		"comments";
		public final static String COMMENTS_RX = 		"notes";
		public final static String CONTEST_CHECK_RX =	"contest_check";
		public final static String CONTEST_CLASS_RX =	"contest_class";
		public final static String CONTEST_CLASS_TRANSMITTERS_RX =	"contest_class_transmitters";
		public final static String CONTEST_ID =			"contest_id";
		public final static String CONTEST_SECTION_RX =	"contest_section";
		public final static String FREQUENCY = 			"frequency";
		public final static String MODE = 				"mode";
		public final static String MODE_SUB = 			"mode_sub";
		public final static String LOC_GRID_TX = 		"loc_grid_tx";
		public final static String LOC_GRID_RX = 		"loc_grid_rx";
		public final static String POWER_TX = 			"power_tx";
		public final static String POWER_RX = 			"power_rx";
		public final static String SIGNAL_TX = 			"signal_tx";
		public final static String SIGNAL_RX = 			"signal_rx";
		public final static String DATE = 				"date";
		public final static String TIME = 				"time";
		public final static String IMPORT = 			"import";
		
		public final static String SQL_CREATE =
				"CREATE TABLE " + TABLE_NAME + " (" +
						_ID + 				" CHARACTER(36) PRIMARY KEY" + COMMA_SEP +
						BOOK_ID + 			" CHARACTER(36)" + COMMA_SEP +
						BAND + 				" VARCHAR(8)" + COMMA_SEP +
						CALLSIGN_OP + 		" VARCHAR(16)" + COMMA_SEP +
						CALLSIGN_TX + 		" VARCHAR(16)" + COMMA_SEP +
						CALLSIGN_RX + 		" VARCHAR(16)" + COMMA_SEP +
						CONTEST_CHECK_RX +	" VARCHAR(16)" + COMMA_SEP +
						CONTEST_CLASS_RX +	" VARCHAR(16)" + COMMA_SEP +
						CONTEST_CLASS_TRANSMITTERS_RX +	" INT" + COMMA_SEP +
						CONTEST_ID +		" VARCHAR(16)" + COMMA_SEP +
						CONTEST_SECTION_RX +" VARCHAR(16)" + COMMA_SEP +
						FREQUENCY + 		" REAL" + COMMA_SEP +
						MODE + 				" VARCHAR(16)" + COMMA_SEP +
						MODE_SUB + 			" VARCHAR(16)" + COMMA_SEP +
						SIGNAL_TX + 		" VARCHAR(3)" + COMMA_SEP +
						SIGNAL_RX + 		" VARCHAR(3)" + COMMA_SEP +
						LOC_GRID_TX + 		" VARCHAR(6)" + COMMA_SEP +
						LOC_GRID_RX + 		" VARCHAR(6)" + COMMA_SEP +
						POWER_RX + 			" REAL" + COMMA_SEP +
						POWER_TX + 			" REAL" + COMMA_SEP +
						COMMENTS_RX + 		" TEXT" + COMMA_SEP +
						COMMENTS_TX + 		" TEXT" + COMMA_SEP +
						DATE + 				" CHARACTER(8)" + COMMA_SEP +
						TIME + 				" CHARACTER(4)" + COMMA_SEP +
						IMPORT + 			" BLOB)";
		
		public final static String SQL_DELETE =
				"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
}