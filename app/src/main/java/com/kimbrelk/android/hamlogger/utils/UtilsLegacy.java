package com.kimbrelk.android.hamlogger.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import com.kimbrelk.android.hamlogger.data.legacydatabase.LegacyDatabaseHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.UUID;

public final class UtilsLegacy {
	
	public final static @NonNull String formatFrequency(@NonNull double freq) {
		DecimalFormat df = new DecimalFormat("###,###,###,###,##0.####");
		String result = null;
		String name = null;
		double value = freq;
		if (freq < 0.001) {
			value = freq / 0.000001;
			name = "Hz";
		}
		else if (freq < 1.0) {
			value = freq / 0.001;
			name = "kHz";
		}
		else if (freq < 1000.0) {
			value = freq / 1.0;
			name = "MHz";
		}
		else if (freq < 1000000.0) {
			value = freq / 1000.0;
			name = "GHz";
		}
		else if (freq < 1000000000.0) {
			value = freq / 1000000.0;
			name = "THz";
		}
		result = df.format(value) + " " + name;
		return result;
	}
	
	public final static @NonNull int calculateNoOfColumns(@NonNull Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
		int noOfColumns = (int) (dpWidth / (64 + 4*2 + 4*2));
		return noOfColumns;
	}
	
	public final static @NonNull String createNewUUID() {
		return UUID.randomUUID().toString();
	}
	
	public final static @NonNull String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return formatDate(System.currentTimeMillis());
	}
	
	public final static @NonNull String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return formatTime(System.currentTimeMillis());
	}
	
	public final static @NonNull String formatDate(@NonNull long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return dateFormat.format(new Date(time));
	}
	
	public final static @NonNull String formatTime(@NonNull long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm");
		dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return dateFormat.format(new Date(time));
	}
	
	public final static @NonNull String formatDateReadable(@NonNull long time) {
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		dateFormat.setTimeZone(TimeZone.getDefault());
		return dateFormat.format(new Date(time));
	}
	
	public final static @NonNull String formatTimeReadable(@NonNull long time) {
		DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
		dateFormat.setTimeZone(TimeZone.getDefault());
		return dateFormat.format(new Date(time));
	}
	
	public final static File writeToTempFile(@NonNull Context context, @NonNull File fileBase, @NonNull String fileName,
			String fileContents, boolean isTempFile) throws IOException {
		
		File file = null;
		FileOutputStream outputStream = null;
		fileBase.mkdirs();
		if (isTempFile) {
			file = File.createTempFile(fileName, null, fileBase);
		}
		else {
			file = new File(fileBase, fileName);
			file.createNewFile();
		}
		outputStream = new FileOutputStream(file);
		outputStream.write(fileContents.getBytes());
		outputStream.flush();
		outputStream.close();
		
		return file;
	}
	
	public final static String getPath(final Context context, final Uri uri) {
		
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		
		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
				
				// TODO handle non-primary volumes
			}
			
			// DownloadsProvider
			if (isDownloadsDocument(uri)) {
				
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				
				return getDataColumn(context, contentUri, null, null);
			}
			
			// MediaProvider
			if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
					split[1]
				};
				
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		
		// MediaStore (and general)
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String result = getDataColumn(context, uri, null, null);
			if (result == null) {
				String wholeID = "";
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2){
					wholeID = uri.toString();
				}
				else {
					wholeID = DocumentsContract.getDocumentId(uri);
				}
				String id = wholeID.split(";")[1];
				result = getDataColumn(context, uri, MediaStore.Images.Media._ID + "=?", new String[]{id});
			}
			return result;
		}
		
		// File
		if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		
		if (uri != null) {
			return uri.toString();
		}
		
		return null;
	}
	
	public final static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String[] projection = {
			android.provider.MediaStore.Files.FileColumns.DATA
		};
		
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
				String result = cursor.getString(columnIndex);
				return result;
			}
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
	
	public final static  boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	
	public final static  boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}
	
	public final static  boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	
	public final static SQLiteDatabase getReadableDatabase(@NonNull Context context) {
		LegacyDatabaseHelper mDbHelper = new LegacyDatabaseHelper(context);
		if (mDbHelper != null && context != null) {
			return mDbHelper.getReadableDatabase();
		}
		return null;
	}
	
	public final static SQLiteDatabase getWritableDatabase(@NonNull Context context) {
		LegacyDatabaseHelper mDbHelper = new LegacyDatabaseHelper(context);
		if (mDbHelper != null && context != null) {
			return mDbHelper.getWritableDatabase();
		}
		return null;
	}
	
}
