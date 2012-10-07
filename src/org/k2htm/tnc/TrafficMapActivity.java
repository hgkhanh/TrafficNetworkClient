package org.k2htm.tnc;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class TrafficMapActivity extends MapActivity implements LocationListener {
	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	private GeoPoint currentPoint;
	private Location currentLocation = null;
	private TrafficOverlay currPos;
	private Button btnReport;
	public static final int REQUEST_CODE = 1;
	public static final String TAG = "Traffic Map";
	public static final String LONG = "longitude";
	public static final String LAT = "latitude";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_map);
		// mapview setting
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapController = mapView.getController();
		mapController.setZoom(15);
		getLastLocation();
		animateToCurrentLocation();
		drawCurrPositionOverlay();
		// set button listener
		btnReport = (Button) findViewById(R.id.btnReport);
		btnReport.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle oBundle = new Bundle();
				oBundle.putInt(LAT, currentPoint.getLatitudeE6());
				oBundle.putInt(LONG, currentPoint.getLongitudeE6());
				Intent oIntent = new Intent(TrafficMapActivity.this,
						ReportMapActivity.class);
				oIntent.putExtras(oBundle);
				startActivityForResult(oIntent, REQUEST_CODE);
			}
		});

	}

	public void getLastLocation() {
		String provider = getBestProvider();
		currentLocation = locationManager.getLastKnownLocation(provider);
		if (currentLocation != null) {
			setCurrentLocation(currentLocation);
		} else {
			Toast.makeText(this, "Location not yet acquired", Toast.LENGTH_LONG)
					.show();
		}
		((TextView) findViewById(R.id.providerText)).setText("Provider :"
				+ getBestProvider());
	}

	public void animateToCurrentLocation() {
		if (currentPoint != null) {
			mapController.animateTo(currentPoint);
		}

	}

	public String getBestProvider() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		return bestProvider;
	}

	public void setCurrentLocation(Location location) {
		int currLatitude = (int) (location.getLatitude() * 1E6);
		int currLongitude = (int) (location.getLongitude() * 1E6);
		currentPoint = new GeoPoint(currLatitude, currLongitude);

		((TextView) findViewById(R.id.latitudeText)).setText("Latitude : "
				+ String.valueOf((int) (currentLocation.getLatitude() * 1E6)));
		((TextView) findViewById(R.id.longitudeText)).setText("Longitude : "
				+ String.valueOf((int) (currentLocation.getLongitude() * 1E6)));
		((TextView) findViewById(R.id.accuracyText)).setText("Accuracy : "
				+ String.valueOf(location.getAccuracy()) + " m");
		drawCurrPositionOverlay();
	}

	public void centerToCurrentLocation(View view) {
		animateToCurrentLocation();
	}

	public void drawCurrPositionOverlay() {
		List<Overlay> overlays = mapView.getOverlays();
		overlays.remove(currPos);
		Drawable marker = getResources().getDrawable(R.drawable.me);
		currPos = new TrafficOverlay(marker, mapView);
		if (currentPoint != null) {
			OverlayItem overlayitem = new OverlayItem(currentPoint, "Me",
					"Here I am!");
			currPos.addOverlay(overlayitem);
			overlays.add(currPos);
			currPos.setCurrentLocation(currentLocation);
		}
	}

	public void drawMalls() {
		Drawable marker = getResources().getDrawable(R.drawable.incidents);
		TrafficOverlay mallsPos = new TrafficOverlay(marker, mapView);
		GeoPoint[] mallCoords = new GeoPoint[6];
		// Load Some Random Coordinates in Miami, FL
		mallCoords[0] = new GeoPoint(21027664, 10583955);// The Oaks Mall
		mallCoords[1] = new GeoPoint(21029864, 105824955);// Creekside mall
		mallCoords[2] = new GeoPoint(21021964, 105823955);// Millhopper Shopping
															// Center
		mallCoords[3] = new GeoPoint(21023964, 105802155);// Northside Shopping
															// Center
		mallCoords[4] = new GeoPoint(21021964, 105813955);// Gainesville Mall
		mallCoords[5] = new GeoPoint(21020864, 105803955);// Gainesville
															// Shopping Center
		List<Overlay> overlays = mapView.getOverlays();
		/*
		 * OverlayItem overlayItem = new OverlayItem(mallCoords[0],
		 * "The Oaks Mall", "6419 W Newberry Rd, Gainesville, FL 32605");
		 * mallsPos.addOverlay(overlayItem); overlayItem = new
		 * OverlayItem(mallCoords[1], "Creekside Mall",
		 * "3501 Southwest 2nd Avenue, Gainesville, FL");
		 * mallsPos.addOverlay(overlayItem); overlayItem = new
		 * OverlayItem(mallCoords[2], "Millhopper Shopping Center",
		 * "NW 43rd St & NW 16th Blvd. Gainesville, FL");
		 * mallsPos.addOverlay(overlayItem); overlayItem = new
		 * OverlayItem(mallCoords[3], "Northside Shopping Center",
		 * "Gainesville, FL"); mallsPos.addOverlay(overlayItem); overlayItem =
		 * new OverlayItem(mallCoords[4], "Gainesville Mall",
		 * "2624 Northwest 13th Street Gainesville, FL 32609-2834");
		 * mallsPos.addOverlay(overlayItem); overlayItem = new
		 * OverlayItem(mallCoords[5], "Gainesville Shopping Center",
		 * "1344 N Main St Gainesville, Florida 32601");
		 */

		/*
		 * read from file
		 */
		FileInputStream fis;
		Log.i(TAG, "read file");
		try {
			fis = openFileInput("incidents.txt");
			DataInputStream dataIO = new DataInputStream(fis);
			String strLine = null;
			int tmp_lat;
			int tmp_long;
			while ((strLine = dataIO.readLine()) != null) {
				tmp_lat = Integer.parseInt(strLine);
				Log.i(TAG, "stored string:" + strLine);
				strLine = dataIO.readLine();
				tmp_long = Integer.parseInt(strLine);
				Log.i(TAG, "stored string:" + strLine);
				OverlayItem overlayItem = new OverlayItem(new GeoPoint(tmp_lat,
						tmp_long), "Saved Point", "you selected this before");
				mallsPos.addOverlay(overlayItem);
			}

			dataIO.close();
			fis.close();

		} catch (Exception e) {
		}

		overlays.add(mallsPos);
		mallsPos.setCurrentLocation(currentLocation);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_traffic_map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		setCurrentLocation(location);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locationManager
				.requestLocationUpdates(getBestProvider(), 1000, 1, this);

		drawMalls();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}

}
