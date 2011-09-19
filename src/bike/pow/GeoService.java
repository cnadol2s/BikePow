package bike.pow;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GeoService extends Service implements LocationListener {

	public final static String SERVICE_GEO_ACTION = "chris.intent.action.GEO_ACTION";
	private String myServiceStatus;
	private String myProviderList;
	private String myProvider;
	private String myCoordinates;
	private double myLatitude;
	private double myLongitude;
	private String myAddress;
	private String mySpeed;
	private String myBearing;
	private String myAltitude;
	private String myAccuracy;
	private LocationManager locationManager;
	private LocationProvider locationProvider;
	private Location location;
	private DecimalFormat df;
	private double latitude;
	private double longitude;
	private double altitude;
	private double speed;
	private double bearing;
	private double accuracy;
	
	
	public void onCreate() {
		this.myLatitude = -1;
		this.myLongitude = -1;
		this.myCoordinates = "Waiting for Satellite fix ...";
		this.myAddress = "Waiting for Geocoder ...";
		this.mySpeed = "waiting for Speed ...";
		this.myBearing = "Waiting for Bearing ...";
		this.myAltitude = "Waiting for Altitude ...";
		this.myAccuracy = "Waiting for Accuracy ...";
		this.df = new DecimalFormat("#.###");
		super.onCreate();
	}
	
	private void broadcastResults() {
		Intent intent = new Intent(SERVICE_GEO_ACTION);
		intent.putExtra("myServiceStatus", this.myServiceStatus);
		intent.putExtra("myProviderList", this.myProviderList);
		intent.putExtra("myProvider", this.myProvider);
		intent.putExtra("myCoordinates", this.myCoordinates);
		intent.putExtra("myLatitude", this.myLatitude);
		intent.putExtra("myLongitude", this.myLongitude);
		intent.putExtra("myAddress", this.myAddress);
		intent.putExtra("mySpeed", this.mySpeed);
		intent.putExtra("myBearing", this.myBearing);
		intent.putExtra("myAltitude", this.myAltitude);
		intent.putExtra("myAccuracy", this.myAccuracy);
		intent.putExtra("latitude", this.latitude);
		intent.putExtra("longitude", this.longitude);
		intent.putExtra("altitude", this.altitude);
		intent.putExtra("speed", this.speed);
		intent.putExtra("bearing", this.bearing);
		intent.putExtra("accuracy", this.accuracy);
		this.getApplicationContext().sendBroadcast(intent);
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		this.myProviderList = this.getProviderListString();
		this.registerProvider(LocationManager.GPS_PROVIDER);
		this.myServiceStatus = "Service running!";
		this.broadcastResults();
	}
	
	private String getProviderListString() {
		StringBuffer sb = new StringBuffer();
		List<String> providers = this.getProviderList();
		if (providers != null) {
			sb.append(providers);
		} else {
			sb.append("No providers available");
		}
		return sb.toString();
	}
	
	private List<String> getProviderList() {
		List<String> myProviders = null;
		try {
        	this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        	myProviders = this.locationManager.getAllProviders();
        } catch (Exception e) { ; }
		return myProviders;
	}
	
	private void registerProvider(String provider) {
    	if (provider != null) {
    		this.myProvider = provider;
    		this.locationProvider = this.locationManager.getProvider(provider);
    		this.locationManager.requestLocationUpdates(this.locationProvider.getName(), 1000, 1, this);
    	}        	
	}
	
	private String getCoordinatesString(Location location) {
		String coordinates;
    	if (location != null) {
    		double lat = location.getLatitude();
    		double lng = location.getLongitude();
    		coordinates = "Lat: " + lat + "\nLng: " + lng;
    		return coordinates;
    	} else {
    		return "Coordinates not available";
    	}
	}
	
	private double getLatitude(Location location) {
		if (location != null) {
			return location.getLatitude();
		}
		return -1;
	}
	
	private double getLongitude(Location location) {
		if (location != null) {
			return location.getLongitude();
		}
		return -1;
	}
	
	private String getAddressString(Location location) {
    	String addressString = "Address not available";
    	if (location != null) {
    		double lat = location.getLatitude();
    		double lng = location.getLongitude();
    		Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
    		try {
    			List<Address> addresses = gc.getFromLocation(lat, lng, 1);
    			StringBuilder sb = new StringBuilder();
    			if (addresses.size() > 0) {
    				Address address = addresses.get(0);
    				sb.append(address.getAddressLine(0)).append("\n");
					sb.append(address.getLocality()).append("\n");
					sb.append(address.getPostalCode()).append("\n");
					sb.append(address.getCountryName()).append("\n");
    			}
    			addressString = sb.toString();
    		} catch (IOException e) {
    			;
    		}
    	}
    	return addressString;
	}
	
	private double getSpeed(Location location) {
		if (location != null && location.hasSpeed()) {
			return (double) location.getSpeed();		// meters per second
		} else {
			return -1.0;
		}
	}
	
	private String getSpeedString(Location location) {
		if (location != null && location.hasSpeed()) {
			float speed_ms = location.getSpeed();
			double speed_kmh = ((3600.0/1000.0) * speed_ms);
			return "Speed: " + df.format(speed_kmh).replaceAll(",", ".") + " km/h";
		} else {
			return "Speed not available";
		}
	}
	
	private double getBearing(Location location) {
		if (location != null && location.hasBearing()) {
			return (double) location.getBearing();
		} else {
			return -1.0;
		}
	}
	
	private String getBearingString(Location location) {
		if (location != null && location.hasBearing()) {
			float degrees = location.getBearing();
			return "Bearing: " + degrees + "°";
		} else {
			return "Bearing not available";
		}
	}
	
	private double getAltitude(Location location) {
		if (location != null && location.hasAltitude()) {
			return (double) location.getAltitude();
		} else {
			return -1.0;
		}
	}
	
	private String getAltitudeString(Location location) {
		if (location != null && location.hasAltitude()) {
			double altitude = location.getAltitude();
			return "Altitude: " + altitude + " m";
		} else {
			return "Altitude not available";
		}
	}
	
	private double getAccuracy(Location location) {
		if (location != null && location.hasAccuracy()) {
			return (double) location.getAccuracy();
		} else {
			return -1.0;
		}
	}
	
	private String getAccuracyString(Location location) {
		if (location != null && location.hasAccuracy()) {
			float accuracy = location.getAccuracy();
			return "Accuracy: " + df.format(accuracy).replaceAll(",", ".") + " m";
		} else {
			return "Accuracy not available";
		}
	}
	
	@Override
	public void onDestroy() {
		this.locationManager.removeUpdates(this);
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		Thread geoThread = new Thread(null, evaluateLocation, "GeoThread");
		geoThread.start();
	}
	
	private Runnable evaluateLocation = new Runnable() {
		@Override
		public void run() {
			GeoService.this.myCoordinates = GeoService.this.getCoordinatesString(location);
			GeoService.this.myAddress = GeoService.this.getAddressString(location);
			GeoService.this.mySpeed = GeoService.this.getSpeedString(location);
			GeoService.this.myBearing = GeoService.this.getBearingString(location);
			GeoService.this.myAltitude = GeoService.this.getAltitudeString(location);
			GeoService.this.myAccuracy = GeoService.this.getAccuracyString(location);
			GeoService.this.myLatitude = GeoService.this.getLatitude(location);
			GeoService.this.myLongitude = GeoService.this.getLongitude(location);
			GeoService.this.latitude = GeoService.this.getLatitude(location);
			GeoService.this.longitude = GeoService.this.getLongitude(location);
			GeoService.this.altitude = GeoService.this.getAltitude(location);
			GeoService.this.speed = GeoService.this.getSpeed(location);
			GeoService.this.bearing = GeoService.this.getBearing(location);
			GeoService.this.accuracy = GeoService.this.getAccuracy(location);
			GeoService.this.broadcastResults();
		}
	};

	@Override
	public void onProviderDisabled(String provider) {
		Log.d("GeoService", "onProviderDisabled[enter+return]");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("GeoService", "onProviderEnabled[enter+return]");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.d("GeoService", "onStatusChanged[enter+return]");
	}

}
