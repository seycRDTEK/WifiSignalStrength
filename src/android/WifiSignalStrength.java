package com.eyc.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


/**
 * To retrieve WiFi signal strength (in percent)
 * @author cmahesh
 */

@SuppressLint("NewApi")
public class WifiSignalStrength extends CordovaPlugin {
	public CallbackContext connectionCallbackContext;
	/*32 levels are used to be consistent with the range returned by getGSMSignalStrength of SignalStrength object*/    
	private final int levels = 32;
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {		
		this.connectionCallbackContext = callbackContext;
		Activity activity = this.cordova.getActivity();
    	if (action.equals("getSignalStrength")) {    		
    		ConnectivityManager sockMan = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = sockMan.getActiveNetworkInfo();
            int signalStrength;
            if(info.getTypeName().equalsIgnoreCase("wifi")){            	
            	signalStrength = getWifiSignalStrengthPercentage(activity);
            }
            else{            	
        		signalStrength = 100;            
            }
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, String.valueOf(signalStrength));
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
    		return true;
    	
    		
    	} else {
    		return false;
    	}
	}
	
	/**
	 * 
	 * @param activity - Cordova Activity object
	 * @return int - Wifi signal strength (in asu)
	 */
	public int getWifiSignalStrength(Activity activity){
	    int MIN_RSSI        = -100;
	    int MAX_RSSI        = -55;  
	    
	    WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);            
	    WifiInfo info = wifiManager.getConnectionInfo(); 
	    int rssi = info.getRssi();
	   
	    int version = 1;
	    try{
	    	version = android.os.Build.VERSION.SDK_INT;
	    }
	    catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    /*In SDK versions before Android 4.0, there is a bug in calucluateSignalLevel method. hence a conditional check is made to use 
	     * use calucluateSignalLevel method only for SDK versions > =Android 4.0
	     * For previous SDK versions the corrected code (from newer versions of Android) is used directly*/
	    if (version >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
	    	return WifiManager.calculateSignalLevel(rssi, levels);
	    } else {             
	        // this is the code since 4.0.1
	        if (rssi <= MIN_RSSI) {
	            return 0;
	        } else if (rssi >= MAX_RSSI) {
	            return levels - 1;
	        } else {
	            float inputRange = (MAX_RSSI - MIN_RSSI);
	            float outputRange = (levels - 1);
	            return (int)((float)(rssi - MIN_RSSI) * outputRange / inputRange);
	        }	      
	    }	    
	}
	
	/**
	 * Converts WiFi signal strength from ASU to percentage
	 * @param activity - Cordova Activity object
	 * @return int - Wifi signal strength in percentage
	 */
	public int getWifiSignalStrengthPercentage(Activity activity){
		int signalLevel = getWifiSignalStrength(activity);
		return (int)(signalLevel * 100 / (levels - 1));
	}	
	
}
