package com.kimbrelk.android.hamlogger.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.vending.billing.IInAppBillingService;
import com.kimbrelk.android.hamlogger.utils.Constants;
import java.util.ArrayList;

public final class LegacyServiceAuth extends Service {
	private final String LOG_TAG = "ServiceAuth";
	private final int IAB_VERSION = 3;
	private final String IAB_TYPE = "inapp";
	private final String SKU_PRO = "com.kimbrelk.hamlogger.premium";
	//private final String SKU_PRO = "android.test.purchased";

	private final IBinder mBinder = new LocalBinder();

	private IInAppBillingService mInAppBillingService;
	private boolean hasPurchasedPro;
	private SharedPreferences prefs;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public LegacyServiceAuth getService() {
			return LegacyServiceAuth.this;
		}
	}

	@Override
	public final void onCreate() {
		super.onCreate();
		Intent inAppBillingServiceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		inAppBillingServiceIntent.setPackage("com.android.vending");
		bindService(inAppBillingServiceIntent, mInAppBillingServiceConn, Context.BIND_AUTO_CREATE);
		prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		hasPurchasedPro = prefs.getBoolean(Constants.Prefs.IS_PRO, true);
	}

	@Override
	public final void onDestroy() {
		super.onDestroy();
		if (mInAppBillingService != null) {
			unbindService(mInAppBillingServiceConn);
		}
	}

	public final boolean shouldShowAds() {
		return !isPro();
	}

	public final boolean isPro() {
		return hasPurchasedPro || prefs.getBoolean(Constants.Prefs.IS_PRO, false);
	}

	private ServiceConnection mInAppBillingServiceConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mInAppBillingService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mInAppBillingService = IInAppBillingService.Stub.asInterface(service);
			new Thread() {
				public void run() {
					try {

						// Verify IAB is supported
						int result = mInAppBillingService.isBillingSupported(IAB_VERSION,
								getPackageName(), IAB_TYPE);
						if (result != 0) {
							Log.w(LOG_TAG, "IAB is not supported.");
							hasPurchasedPro = false;
							prefs.edit()
									.putBoolean(Constants.Prefs.IS_PRO, hasPurchasedPro)
									.commit();
							return;
						}

						// Get the price list
						/*ArrayList<String> mSkus = new ArrayList<String>();
						mSkus.add(SKU_PRO);
						Bundle querySkus = new Bundle();
						querySkus.putStringArrayList("ITEM_ID_LIST", mSkus);
						Bundle skuDetails = mInAppBillingService.getSkuDetails(IAB_VERSION,
								getPackageName(), IAB_TYPE, querySkus);
						result = skuDetails.getInt("RESPONSE_CODE");
						if (result == 0) {
							ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
							for (String thisResponse : responseList) {
								JSONObject object = new JSONObject(thisResponse);
								String sku = object.getString("productId");
								String price = object.getString("price");
								if (sku.equals(SKU_PRO)) {

								}
							}
						}*/

						// Get the purchased SKUs
						hasPurchasedPro = false;
						Bundle ownedItems = mInAppBillingService.getPurchases(IAB_VERSION,
								getPackageName(), IAB_TYPE, null);
						int response = ownedItems.getInt("RESPONSE_CODE");
						if (response == 0) {
							ArrayList<String> ownedSkus =
									ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
							ArrayList<String>  purchaseDataList =
									ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
							ArrayList<String>  signatureList =
									ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
							String continuationToken =
									ownedItems.getString("INAPP_CONTINUATION_TOKEN");

							for (int i = 0; i < purchaseDataList.size(); ++i) {
								String purchaseData = purchaseDataList.get(i);
								String signature = signatureList.get(i);
								String sku = ownedSkus.get(i);
								if (sku.equals(SKU_PRO)) {
									// TODO Verify Signature
									hasPurchasedPro = true;
								}
							}
						}
						else if (response == 2) {
							// If network is unavailable, just make them pro for now
							hasPurchasedPro = true;
						}
						long dateToday = System.currentTimeMillis();
						if (hasPurchasedPro) {
							prefs.edit()
									.putBoolean(Constants.Prefs.IS_PRO, hasPurchasedPro)
									.commit();
							prefs.edit()
									.putLong(Constants.Prefs.LAST_VERIFIED_PRO, dateToday)
									.commit();
						}
						else {
							long dateLastVerified = prefs.getLong(Constants.Prefs.LAST_VERIFIED_PRO, 0);
							if (dateLastVerified >= dateToday - Constants.Time.WEEK) {
								hasPurchasedPro = true;
							}
							else {
								prefs.edit()
										.putBoolean(Constants.Prefs.IS_PRO, false)
										.commit();
							}
						}
					}
					catch(NullPointerException e) {
						e.printStackTrace();
						hasPurchasedPro = true;
					}
					catch(RemoteException e) {
						e.printStackTrace();
						hasPurchasedPro = true;
					}
					if (hasPurchasedPro) {
						Log.i(LOG_TAG, "Licensed as Pro");
					}
					else {
						Log.i(LOG_TAG, "Licensed as Free");
					}
				}
			}.start();
		}
	};

	public final PendingIntent getIntentToPurchasePro() {
		if (mInAppBillingService != null) {
			try {
				Bundle buyIntentBundle = mInAppBillingService.getBuyIntent(IAB_VERSION,
						getPackageName(), SKU_PRO, IAB_TYPE, "");
				return buyIntentBundle.getParcelable("BUY_INTENT");
			}
			catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
}