package com.kimbrelk.android.hamlogger.utils;

public final class Constants {
	
	public final static String PREFS_NAME = "prefs";
	
	public final static class Extras {
		public final static String BOOK_ID = "com.kimbrelk.android.hamlogger.book_id";
		public final static String BOOK_NAME = "com.kimbrelk.android.hamlogger.book_name";
		public final static String ENTRY_ID = "com.kimbrelk.android.hamlogger.entry_id";
		public final static String IS_NEW = "com.kimbrelk.android.hamlogger.is_new";
	}
	
	public final static class Prefs {
		public final static String FIRST_RUN_TIMESTAMP = "first_run_timestamp";
		public final static String IS_FIRST_RUN = "is_first_run";
		public final static String IS_PRO = "is_pro";
		public final static String LAST_VERIFIED_PRO = "last_verified_pro";
		public final static String LAST_FREQ = "last_freq";
		public final static String LAST_MODE = "last_mode";
		public final static String LAST_MODE_SUB = "last_mode_sub";
		public final static String LAST_OPERATOR = "last_op";
		public final static String SORT_MODE = "sort_mode";
	}
	
	public final static class ScreenIds {
		public final static String VIEW_BOOKS = "ViewBooks";
		public final static String VIEW_ENTRIES = "ViewEntries";
		public final static String VIEW_ABOUT = "ViewAbout";
		public final static String EDIT_BOOK = "EditBook";
		public final static String EDIT_ENTRY = "EditEntry";
	}
	
	public final static class EventParams {
		public final static String ERROR = "error";
	}
	
	public final static class EventIds {
		public final static String CONTACT_EMAIL = "contact_email";
		public final static String CONTACT_TWITTER = "contact_twitter";
		public final static String CONTACT_PLAY = "contact_play";
		
		public final static String CREATE_BOOK = "create_book";
		public final static String CREATE_ENTRY = "create_entry";
		
		public final static String DELETE_BOOK = "delete_book";
		public final static String DELETE_ENTRY = "delete_entry";
		
		public final static String DISCARD_BOOK = "discard_book";
		public final static String DISCARD_ENTRY = "discard_entry";
		
		public final static String EDIT_BOOK = "edit_book";
		public final static String EDIT_ENTRY = "edit_entry";
		
		public final static String PURCHASE_PRO_START = "purchase_pro_start";
		public final static String PURCHASE_PRO_FAILED = "purchase_pro_failed";
		public final static String PURCHASE_PRO_SUCCESS = "purchase_pro_success";

		public final static String TAP_PRIVACY = "view_privacy_policy";
		public final static String TAP_RATE = "rate_app";
	}
	
	public final static class BillingResult {
		public final static int OK = 0;
		public final static int USER_CANCELED = 1;
		public final static int SERVICE_UNAVAILABLE = 2;
		public final static int BILLING_UNAVAILABLE = 3;
		public final static int ITEM_UNAVAILABLE = 4;
		public final static int DEVELOPER_ERROR = 5;
		public final static int ERROR = 6;
		public final static int ITEM_ALREADY_OWNED = 7;
		public final static int ITEM_NOT_OWNED = 8;
	}
	
	public final static class Request {
		public final static int PURCHASE_PRO = 1;
		public final static int IMPORT_ADI = 2;
		public final static int PERMISSION_STORAGE_EXTERNAL_READ = 3;
	}
	
	public final static class Time {
		public final static long MILLISECOND = 1;
		public final static long SECOND = MILLISECOND * 1000;
		public final static long MINUTE = SECOND * 60;
		public final static long HOUR = MINUTE * 60;
		public final static long HALF_DAY = HOUR * 12;
		public final static long DAY = HOUR * 24;
		public final static long WEEK = DAY * 7;
		public final static long MONTH = DAY * 30;
	}
	
}