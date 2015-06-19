var SignalStrength = function() {};
/*
 * @func getStrengthPercentage
 * Retrieves WiFi signal strength in percent
 * @params successCallback -  callback function to be called on success
 * @params errorCallback - callback function to be called on error 
 */
 SignalStrength.prototype.getStrengthPercentage = function(successCallback, errorCallback) {
	//TODO 
	/*In order to extend the signal strength check for 2g and 3g as well in addition to WiFi, replace the plugin
	 name "WiFiSignalStrength" with "SignalStrength". 
	 Note that for this to work GSMActivity.java should be updated to add listener as explained in the RFE doc for Threshold network parameter
	 */
	 try{
		 cordova.exec(successCallback, errorCallback, "WifiSignalStrength", "getSignalStrength", []);
	 }
	 catch(e){
		 if(errorCallback){
			 errorCallback.call(null);
		 }		 
	 }	
};

navigator.signalStrength = new SignalStrength();

